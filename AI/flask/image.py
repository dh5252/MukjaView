import os

def read_image_from_bytes_file(file_path, output_folder):
    if not os.path.exists(output_folder):
        os.makedirs(output_folder)

    with open(file_path, 'rb') as f:
        data = f.read()

    # 이미지 데이터를 JPEG 포맷으로 저장하기 위해 시작 바이트 추가
    image_data = b'\xff\xd8' + data.split(b'\xff\xd8')[1]

    # 파일명 생성
    base_name = os.path.basename(file_path).split('.')[0]
    output_path = os.path.join(output_folder, f'{base_name}.jpg')
    
    # 이미지 파일 저장
    with open(output_path, 'wb') as image_file:
        image_file.write(image_data)
    
    print(f"Saved {output_path}")

# 현재 스크립트 파일의 디렉토리 경로
current_dir = os.path.dirname(os.path.abspath(__file__))

# 파일 경로와 출력 폴더 지정
file_names = [
    'background_removed_0.txt',
    'background_removed_14.txt',
    'background_removed_15.txt'
]

# 상대 경로를 이용하여 파일 경로 설정
file_paths = [os.path.join(current_dir, name) for name in file_names]

# 출력 폴더 경로 설정
output_folder = os.path.join(current_dir, 'output_images')

# 각 파일에 대해 이미지 추출 및 저장
for file_path in file_paths:
    read_image_from_bytes_file(file_path, output_folder)
