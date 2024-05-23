from flask import Flask, render_template, request, redirect, url_for, jsonify
import os
import requests
from PIL import Image
from io import BytesIO
import base64
from dotenv import load_dotenv
import tempfile
import shutil

# .env 파일 로드
load_dotenv()

# API 키와 도메인 가져오기
ailabapi_api_key = os.getenv('AILABAPI_API_KEY')
domain_name = os.getenv('DOMAIN_NAME')

if not ailabapi_api_key:
    raise ValueError("API 키가 없습니다. .env 파일을 확인하고 'AILABAPI_API_KEY'가 설정되어 있는지 확인하세요.")

if not domain_name:
    raise ValueError("도메인 이름이 없습니다. .env 파일을 확인하고 'DOMAIN_NAME'이 설정되어 있는지 확인하세요.")

app = Flask(__name__)

# 임시 디렉토리 생성/설정
UPLOAD_FOLDER = tempfile.mkdtemp()
OUTPUT_FOLDER = 'static/output'  # 이미지 파일을 저장할 폴더
os.makedirs(OUTPUT_FOLDER, exist_ok=True)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['OUTPUT_FOLDER'] = OUTPUT_FOLDER

# 파일 이름 충돌을 피하기 위해 번호를 붙이는 함수
def get_unique_filename(directory, filename):
    base, ext = os.path.splitext(filename)
    counter = 1
    unique_filename = filename
    while os.path.exists(os.path.join(directory, unique_filename)):
        unique_filename = f"{base}({counter}){ext}"
        counter += 1
    return unique_filename

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
            image = image.convert('RGBA')  # 이미지가 RGBA 형식일 수 있으므로
            image.save(save_path, format='PNG')  # PNG 형식으로 저장
            print(f"배경이 성공적으로 제거되었습니다: {save_path}")
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
            print(f"카툰화 이미지가 성공적으로 저장되었습니다: {save_path}")
            return save_path  # 성공적으로 저장된 경로 반환
        else:
            print("카툰화 이미지를 다운로드하는 데 실패했습니다. 상태 코드:", image_response.status_code)
    else:
        print("카툰화 API 호출에 실패했습니다. 상태 코드:", response.status_code)

    return None  # 실패한 경우 None 반환

# 표정 변화 API 호출 함수
def process_expression_change(image_path, save_path, service_choice):
    url = "https://www.ailabapi.com/api/portrait/effects/emotion-editor"
    with open(image_path, 'rb') as image_file:
        files = {'image_target': ('file', image_file, 'application/octet-stream')}
        headers = {'ailabapi-api-key': ailabapi_api_key}
        payload = {'service_choice': service_choice}
        response = requests.post(url, headers=headers, data=payload, files=files)

    if response.status_code == 200:
        response_data = response.json()
        encoded_image = response_data['data']['image']

        if encoded_image:
            decoded_image = base64.b64decode(encoded_image)
            image = Image.open(BytesIO(decoded_image))
            image = image.convert('RGB')  # JPEG 형식은 RGB여야 함 !!
            image.save(save_path, format='JPEG')
            print(f"{service_choice}번 표정변화 이미지가 성공적으로 저장되었습니다: {save_path}")
            return save_path  # 성공적으로 저장된 경로 반환
        else:
            print(f"{service_choice}번 표정변화 API 응답에서 이미지 데이터를 찾을 수 없습니다.")
    else:
        print(f"{service_choice}번 표정변화 API 호출에 실패했습니다. 상태 코드:", response.status_code)

    return None  # 실패한 경우 None 반환

@app.route('/')
def index():
    return render_template('index.html', domain_name=domain_name)

@app.route('/upload', methods=['POST'])
def upload_image():
    uploaded_image = request.files['image']
    original_filename = os.path.splitext(uploaded_image.filename)[0]
    image_path = os.path.join(app.config['UPLOAD_FOLDER'], f"{original_filename}.jpg")
    uploaded_image.save(image_path)
    print(f"업로드된 이미지 경로: {image_path}")

    # 1. 카툰화
    cartoon_image_path = os.path.join(app.config['UPLOAD_FOLDER'], f"{original_filename}_cartoon.jpg")
    cartoon_image_path = process_image_with_animation(image_path, cartoon_image_path)
    if not cartoon_image_path:
        return "카툰화 처리에 실패했습니다.", 500

    # 2. 표정 변화
    service_choices = ['0', '14', '15']
    expression_image_paths = [
        os.path.join(app.config['UPLOAD_FOLDER'], f"{original_filename}_laugh.jpg"),
        os.path.join(app.config['UPLOAD_FOLDER'], f"{original_filename}_cool.jpg"),
        os.path.join(app.config['UPLOAD_FOLDER'], f"{original_filename}_sad.jpg")
    ]

    for service_choice, expression_image_path in zip(service_choices, expression_image_paths):
        expression_image_path = process_expression_change(cartoon_image_path, expression_image_path, service_choice)
        if not expression_image_path:
            return "표정 변화 처리에 실패했습니다.", 500

    # 3. 배경 제거
    background_removed_paths = []
    for expression_image_path in expression_image_paths:
        background_removed_path = expression_image_path.replace('.jpg', '_background_removed.jpg')
        background_removed_path = background_remove_image(expression_image_path, background_removed_path)
        if not background_removed_path:
            return "배경 제거 처리에 실패했습니다.", 500
        background_removed_paths.append(background_removed_path)

    output_image_paths = []
    for service_choice, path in zip(service_choices, background_removed_paths):
        if os.path.exists(path):  # 파일이 존재하는지 확인
            output_image_filename = get_unique_filename(app.config['OUTPUT_FOLDER'], f"{original_filename}_{service_choice}.jpg")
            output_image_path = os.path.join(app.config['OUTPUT_FOLDER'], output_image_filename)
            shutil.move(path, output_image_path)
            print(f"파일이 이동되었습니다: {path} -> {output_image_path}")
            output_image_paths.append(f"{domain_name}/static/output/{output_image_filename}")
        else:
            print(f"파일이 존재하지 않습니다: {path}")

    os.remove(image_path)
    os.remove(cartoon_image_path)
    for path in expression_image_paths:
        if os.path.exists(path):
            os.remove(path)

    return jsonify(output_image_paths)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=3000, debug=True)
