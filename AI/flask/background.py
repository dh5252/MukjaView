import requests
from PIL import Image
from io import BytesIO

url = "https://www.ailabapi.com/api/cutout/portrait/portrait-background-removal"

# 이미지 파일 경로
image_path = r"C:\Users\wjdgk\Desktop\Sejong_Capstone\AI\flask\static\output\29TJMUV4O7_1_0.jpg"


# 파일 열기
image_file = open(image_path, 'rb')

payload = {}  # payload는 비어있어도 됨
files = {'image': ('file', image_file, 'application/octet-stream')}
headers = {'ailabapi-api-key': 'UikLoZHwT0SYdlB7d6e0HxPJAK6ptFrp9JR4bVn8v2WQDVizASGFKcvmINRgNT5O'}

response = requests.post(url, headers=headers, data=payload, files=files)

# API 응답 확인
if response.status_code == 200:
    # 응답 데이터를 JSON 형식으로 가져옴
    response_data = response.json()

    # 응답에서 이미지 URL 가져오기
    image_url = response_data['data']['image_url']

    # 이미지 다운로드
    image_response = requests.get(image_url)

    # 이미지 다운로드 성공 시
    if image_response.status_code == 200:
        # 이미지를 PIL Image로 열기
        image = Image.open(BytesIO(image_response.content))

        # 이미지를 Google 드라이브에 저장
        save_path = r"C:\Users\wjdgk\Desktop\Sejong_Capstone\AI\flask\static\output\29TJMUV4O7_1back.jpg"

        print("이미지가 성공적으로 저장되었습니다.")
    else:
        print("이미지를 다운로드하는 데 실패했습니다. 상태 코드:", image_response.status_code)
else:
    print("API 호출에 실패했습니다. 상태 코드:", response.status_code)

