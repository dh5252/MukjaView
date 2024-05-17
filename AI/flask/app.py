from flask import Flask, render_template, request, redirect, url_for
import os
import requests
from PIL import Image
from io import BytesIO
import base64
from dotenv import load_dotenv
import tempfile

# .env 파일 로드
load_dotenv()

# API 키 가져오기
ailabapi_api_key = os.getenv('AILABAPI_API_KEY')

app = Flask(__name__)

# 임시 디렉토리를 생성하고 설정합니다.
UPLOAD_FOLDER = tempfile.mkdtemp()
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# 배경 제거 API 호출 함수
def background_remove_image(image_path, save_path):
    url = "https://www.ailabapi.com/api/cutout/portrait/portrait-background-removal"
    image_file = open(image_path, 'rb')

    payload = {}
    files = {'image': ('file', image_file, 'application/octet-stream')}
    headers = {'ailabapi-api-key': ailabapi_api_key}

    response = requests.post(url, headers=headers, data=payload, files=files)

    if response.status_code == 200:
        response_data = response.json()
        image_url = response_data['data']['image_url']

        image_response = requests.get(image_url)

        if image_response.status_code == 200:
            image = Image.open(BytesIO(image_response.content))
            image.save(save_path)
            print("배경이 성공적으로 제거되었습니다.")
        else:
            print("배경 제거 이미지를 다운로드하는 데 실패했습니다. 상태 코드:", image_response.status_code)
    else:
        print("배경 제거 API 호출에 실패했습니다. 상태 코드:", response.status_code)

# 카툰화 API 호출 함수
def process_image_with_animation(image_path, save_path):
    url = "https://www.ailabapi.com/api/portrait/effects/portrait-animation"
    image_file = open(image_path, 'rb')

    payload = {'type': '3d_cartoon'}
    files = {'image': ('file', image_file, 'application/octet-stream')}
    headers = {'ailabapi-api-key': ailabapi_api_key}

    response = requests.post(url, headers=headers, data=payload, files=files)

    if response.status_code == 200:
        response_data = response.json()
        image_url = response_data['data']['image_url']
        image_response = requests.get(image_url)

        if image_response.status_code == 200:
            image = Image.open(BytesIO(image_response.content))
            image.save(save_path)
            print("카툰화 이미지가 성공적으로 저장되었습니다.")
        else:
            print("카툰화 이미지를 다운로드하는 데 실패했습니다. 상태 코드:", image_response.status_code)
    else:
        print("카툰화 API 호출에 실패했습니다. 상태 코드:", response.status_code)

# 표정 변화 처리 API 호출 함수
def process_expression_change_api(input_image_path, output_image_paths, service_choices):
    url = "https://www.ailabapi.com/api/portrait/effects/emotion-editor"
    headers = {'ailabapi-api-key': ailabapi_api_key}

    # 서비스 선택
    for i, service_choice in enumerate(service_choices):
        # 입력 이미지 파일 열기
        with open(input_image_path, 'rb') as input_image_file:
            payload = {'service_choice': service_choice}
            files = {'image_target': ('file', input_image_file, 'application/octet-stream')}

            # API 호출
            response = requests.post(url, headers=headers, data=payload, files=files)

            if response.status_code == 200:
                response_data = response.json()
                encoded_image = response_data['data']['image']
                
                if encoded_image:
                    # base64 디코딩하여 이미지로 변환
                    decoded_image = base64.b64decode(encoded_image)
                    image = Image.open(BytesIO(decoded_image))
                    image.save(output_image_paths[i])
                    print(f"{i+1}번째 표정변화 이미지가 성공적으로 저장되었습니다.")
                else:
                    print(f"{i+1}번째 표정변화 API 응답에서 이미지 데이터를 찾을 수 없습니다.")
            else:
                print(f"{i+1}번째 표정변화 API 호출에 실패했습니다. 상태 코드:", response.status_code)

# 배경 제거 함수 호출 후에 임시 저장소에 저장된 이미지 경로 반환
def process_background_removed_images(expression_image_paths):
    background_removed_paths = []
    for expression_image_path in expression_image_paths:
        background_removed_path = os.path.join(app.config['UPLOAD_FOLDER'], f"background_removed_{os.path.basename(expression_image_path)}")
        background_remove_image(expression_image_path, background_removed_path)
        background_removed_paths.append(background_removed_path)
    return background_removed_paths

@app.route('/')
def index():
    return render_template('index.html')

@app.route('/upload', methods=['POST'])
def upload_image():
    # 사용자가 업로드한 이미지를 받아오기
    uploaded_image = request.files['image']

    # 이미지를 업로드할 경로를 설정
    image_path = os.path.join(app.config['UPLOAD_FOLDER'], uploaded_image.filename)

    # 이미지를 저장하지 않고 바로 임시 파일에 저장
    uploaded_image.save(image_path)

    # 카툰화된 이미지를 저장할 임시 경로 설정
    cartoon_image_path = os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_cartoon.png")

    # 카툰화 함수 호출
    process_image_with_animation(image_path, cartoon_image_path)

    # 표정 변화 처리 API 호출을 위한 설정
    service_choices = ['0', '14', '15']
    expression_image_paths = [
        os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_laugh.png"),
        os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_cool.png"),
        os.path.join(app.config['UPLOAD_FOLDER'], f"{os.path.splitext(uploaded_image.filename)[0]}_sad.png")
    ]

    # 표정 변화 처리 함수 호출
    process_expression_change_api(cartoon_image_path, expression_image_paths, service_choices)

    # 배경 제거 함수 호출 후에 임시 저장소에 저장된 이미지 경로 반환
    background_removed_paths = process_background_removed_images(expression_image_paths)

    # 이미지를 바이트로 변환하여 텍스트 파일에 저장
    with open('images_as_bytes.txt', 'wb') as f:
        for path in background_removed_paths:
            with open(path, 'rb') as image_file:
                f.write(image_file.read())
                f.write(b'\n')  # 이미지 간 구분을 위한 줄 바꿈 추가

    # 처리가 끝난 후 임시 파일 삭제
    os.remove(image_path)
    os.remove(cartoon_image_path)

    for path in background_removed_paths:
        print("배경 제거된 이미지 경로:", path)

    # 업로드 페이지로 리다이렉트
    return redirect(url_for('index'))

if __name__ == '__main__':
    app.run(debug=True)
