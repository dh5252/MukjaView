import mysql.connector
import csv



# CSV 파일 읽기
with open(csv_file_path, "r", encoding="utf-8") as csv_file:
    csv_reader = csv.DictReader(csv_file)
    for row in csv_reader:
        # 각 행에서 id와 thumbnailPicture 값 추출
        restaurant_id = row['restaurantId']
        thumbnail_picture = row['thumbnailPicture']
        
        # MySQL 쿼리 생성 및 실행
        update_query = "UPDATE Restaurant SET thumbnailPicture = %s WHERE restaurantId = %s"
        cursor = connection.cursor()
        cursor.execute(update_query, (thumbnail_picture, restaurant_id))
        connection.commit()

# 연결 종료
connection.close()
