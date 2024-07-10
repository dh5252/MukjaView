import dropbox

# Dropbox API 토큰
DROPBOX_ACCESS_TOKEN = ''

# Dropbox API 인증
dbx = dropbox.Dropbox(DROPBOX_ACCESS_TOKEN)

def set_folder_shared(folder_path):
    """주어진 폴더 경로에 있는 모든 파일에 대해 공유 설정을 변경합니다."""
    try:
        # 폴더에 있는 모든 파일에 대해 공유 설정 변경
        dbx.sharing_share_folder(folder_path)
        print(f"Shared all files in folder {folder_path}.")
    except dropbox.exceptions.ApiError as e:
        print(f"Error sharing files in folder {folder_path}: {e}")

# 공유 설정을 변경할 폴더 경로
folder_path = '/detail_gunja'

# 폴더의 공유 설정 변경
set_folder_shared(folder_path)
