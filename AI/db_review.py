import csv
import mysql.connector


csv_file = open(csv_file_path, "r", encoding="utf-8")

# CSV 파일 읽기
csv_reader = csv.reader(csv_file)
next(csv_reader)

# 데이터베이스 커서 생성
cursor = connection.cursor()

# CSV 파일의 각 행을 데이터베이스에 삽입
for row in csv_reader:
    # 각 행을 데이터베이스에 삽입하는 SQL 쿼리 작성
    query = "INSERT INTO CharacterReview (characterName, review, restaurantId) VALUES (%s, %s, %s)"
    values = (row[1], row[2], int(row[0]))  # restaurantId는 bigint(20)으로 변환하여 삽입

    # SQL 쿼리 실행
    cursor.execute(query, values)

# 변경 사항을 커밋
connection.commit()

# 연결 및 파일 닫기
cursor.close()
connection.close()
csv_file.close()
