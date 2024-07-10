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
    reasonable = True if row[8] == "TRUE" else False
    # 각 행을 데이터베이스에 삽입하는 SQL 쿼리 작성
    query = """
    INSERT INTO Restaurant (restaurantName, address, latitude, longitude, flavorRatio, moodRatio, serviceRatio, reasonable, thumbnailPicture)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
    """

    # 행의 값들을 적재할 때 데이터 유형에 맞게 변환
    values = (
        row[1],  # restaurantName: varchar(255)
        row[2],  # address: varchar(255)
        float(row[3]),  # latitude: double
        float(row[4]),  # longitude: double
        float(row[5]),  # flavorRatio: double
        float(row[6]),  # moodRatio: double
        float(row[7]),  # serviceRatio: double
        reasonable,  # reasonable: boolean
        row[9]  # thumbnailPicture: varchar(255)
    )

    # SQL 쿼리 실행
    cursor.execute(query, values)

# 변경 사항을 커밋
connection.commit()

# 연결 및 파일 닫기
cursor.close()
connection.close()
csv_file.close()
