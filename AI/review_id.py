import mysql.connector



# 데이터베이스 커서 생성
cursor = connection.cursor()

# Step 1: 임시 테이블 생성 및 데이터 복사
cursor.execute("CREATE TABLE IF NOT EXISTS TempCharacterReview AS SELECT * FROM CharacterReview")

# Step 2: 기존 테이블 데이터 삭제
cursor.execute("DELETE FROM CharacterReview")

# Step 3: 임시 테이블에서 데이터 읽기
cursor.execute("SELECT characterName, review, restaurantId FROM TempCharacterReview ORDER BY reviewId")
rows = cursor.fetchall()

# Step 4: 임시 테이블의 데이터를 다시 삽입하며 reviewId를 1부터 부여
new_review_id = 1
for row in rows:
    character_name, review, restaurant_id = row
    query = "INSERT INTO CharacterReview (reviewId, characterName, review, restaurantId) VALUES (%s, %s, %s, %s)"
    values = (new_review_id, character_name, review, restaurant_id)
    cursor.execute(query, values)
    new_review_id += 1

# 변경 사항을 커밋
connection.commit()

# Step 5: 임시 테이블 삭제
cursor.execute("DROP TABLE TempCharacterReview")

# 연결 종료
cursor.close()
connection.close()
