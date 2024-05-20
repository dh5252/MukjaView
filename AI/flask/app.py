from flask import Flask, render_template, request, redirect, url_for
import os
import requests
from PIL import Image
from io import BytesIO
import base64
from dotenv import load_dotenv
import tempfile
from concurrent.futures import ThreadPoolExecutor

# .env 파일 로드
load_dotenv()

# API 키 가져오기
ailabapi_api_key = os.getenv('AILABAPI_API_KEY')
if not ailabapi_api_key:
    raise ValueError("API key is missing. Please check your .env file and ensure 'AILABAPI_API_KEY' is set.")

app = Flask(__name__)

# 임시 디렉토리 생성/ 설정
UPLOAD_FOLDER = tempfile.mkdtemp()
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# ThreadPoolExecutor 생성
executor = ThreadPoolExecutor(max_workers=10)

# 배경 제거 API 호출 함수
def background_remove_image(image_path, save_path):
    url = "https://www.ailabapi.com/api/cutout/portrait/portrait-background-removal"
    with open(image_path, 'rb') as image_file:
        files = {'image': ('file', image_file, 'application/octet-stream')}
        headers = {'ailabapi-api-key': ailabapi_api_key}
        response = requests.post(url, headers=headers, files=files)

    if response.status_code == 200:
        response_data = response.json()
        image_url = response_data['data']['image_url']

        image_response = requests.get(image_url)

        if image_response.status_code == 200:
            image = Image.open(BytesIO(image_response.content))
            image = image.convert('RGB')  # JPEG 형식은 RGB여야 합니다.
            image.save(save_path, format='JPEG')
            print("배경이 성공적으로 제거되었습니다.")
            return save_path  # 성공적으로 저장된 경로 반환
        else:
            print("배경 제거 이미지를 다운로드하는 데 실패했습니다. 상태 코드:", image_response.status_code)
    else:
        print("배경 제거 API 호출에 실패했습니다. 상태 코드:", response.status_code)

    return None  # 실패한 경우 None 반환

# 카툰화 API 호출 함수
def process_image_with_animation(image_path, save_path):
    url = "https://www.ailabapi.com/api/portrait/effects/portrait-animation"
    with open(image_path, 'rb') as image_file:
        files = {'image': ('file', image_file, 'application/octet-stream')}
        headers = {'ailabapi-api-key': ailabapi_api_key}
        payload = {'type': '3d_cartoon'}
        response = requests.post(url, headers=headers, data=payload, files=files)

    if response.status_code == 200:
        response_data = response.json()
        image_url = response_data['data']['image_url']
        image_response = requests.get(image_url)

        if image_response.status_code == 200:
            image = Image.open(BytesIO(image_response.content))
            image = image.convert('RGB')  # JPEG 형식은 RGB여야 합니다.
            image.save(save_path, format='JPEG')
            print("카툰화 이미지가 성공적으로 저장되었습니다.")
            return save_path  # 성공적으로 저장된 경로 반환
        else:
            print("카툰화 이미지를 다운로드하는 데 실패했습니다. 상태 코드:", image_response.status_code)
    else:
        print("카툰화 API 호출에 실패했습니다. 상태 코드:", response.status_code)

    return None  # 실패한 경우 None 반환

# 표정 변화 처리 API 호출 함수
def process_expression_change_api(input_image_path, output_image_paths, service_choices):
    url = "https://www.ailabapi.com/api/portrait/effects/emotion-editor"
    headers = {'ailabapi-api-key': ailabapi_api_key}

    def call_api(service_choice, output_image_path):
        with open(input_image_path, 'rb') as input_image_file:
            files = {'image_target': ('file', input_image_file, 'application/octet-stream')}
            payload = {'service_choice': service_choice}
            response = requests.post(url, headers=headers, data=payload, files=files)

        if response.status_code == 200:
            response_data = response.json()
            encoded_image = response_data['data']['image']
            
            if encoded_image:
                decoded_image = base64.b64decode(encoded_image)
                image = Image.open(BytesIO(decoded_image))
                image = image.convert('RGB')  # JPEG 형식은 RGB여야 함 !!
                image.save(output_image_path, format='JPEG')
                print(f"{service_choice}번 표정변화 이미지가 성공적으로 저장되었습니다.")
                return output_image_path  # 성공적으로 저장된 경로 반환
            else:
                print(f"{service_choice}번 표정변화 API 응답에서 이미지 데이터를 찾을 수 없습니다.")
        else:
            print(f"{service_choice}번 표정변화 API 호출에 실패했습니다. 상태 코드:", response.status_code)

        return None  # 실패한 경우 None 반환

    futures = [executor.submit(call_api, sc, op) for sc, op in zip(service_choices, output_image_paths)]
    output_paths = [future.result() for future in futures]
    return output_paths

# 배경 제거 함수 호출 후에 임시 저장소에 저장된 이미지 경로 반환
def process_background_removed_images(expression_image_paths, service_choices):
    futures = []
    background_removed_paths = []
    for expression_image_path, service_choice in zip(expression_image_paths, service_choices):
        background_removed_path = os.path.join(app.config['UPLOAD_FOLDER'], f"background_removed_{service_choice}.jpg")
        futures.append(executor.submit(background_remove_image, expression_image_path, background_removed_path))
        background_removed_paths.append((service_choice, background_removed_path))

    background_removed_paths = [(sc, future.result()) for (sc, future) in zip(service_choices, futures)]
    return background_removed_paths

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload_image():
    uploaded_image = request.files['image']
    image_path = os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}.jpg")
    uploaded_image.save(image_path)

    cartoon_image_path = os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_cartoon.jpg")
    cartoon_image_path = process_image_with_animation(image_path, cartoon_image_path)
    if not cartoon_image_path:
        return "카툰화 처리에 실패했습니다.", 500

    service_choices = ['0', '14', '15']
    expression_image_paths = [
        os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_laugh.jpg"),
        os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_cool.jpg"),
        os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_sad.jpg")
    ]

    expression_image_paths = process_expression_change_api(cartoon_image_path, expression_image_paths, service_choices)
    if any(path is None for path in expression_image_paths):
        return "표정 변화 처리에 실패했습니다.", 500

    background_removed_paths = process_background_removed_images(expression_image_paths, service_choices)
    if any(path is None for _, path in background_removed_paths):
        return "배경 제거 처리에 실패했습니다.", 500

    for service_choice, path in background_removed_paths:
        with open(f'background_removed_{service_choice}.txt', 'wb') as f:
            with open(path, 'rb') as image_file:
                f.write(image_file.read())

    os.remove(image_path)
    os.remove(cartoon_image_path)
    for _, path in background_removed_paths:
        os.remove(path)

    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True)
