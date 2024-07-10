import mysql.connector


# 데이터베이스 커서 생성
cursor = connection.cursor()

# AUTO_INCREMENT 값을 0으로 초기화
query = "ALTER TABLE RestaurantDetailedPicture AUTO_INCREMENT = 1"
cursor.execute(query)

# 변경 사항을 커밋
connection.commit()

# 연결 닫기
cursor.close()
connection.close()
