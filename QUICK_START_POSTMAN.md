# 🚀 Quick Start - Test Profile API với Postman

## ⚡ Cách nhanh nhất (Import Collection)

### Bước 1: Import Collection vào Postman

1. Mở **Postman**
2. Click **Import** (góc trên bên trái)
3. Chọn file `TutorHub_Profile_API.postman_collection.json`
4. Click **Import**

✅ Bạn sẽ thấy collection "TutorHub - Profile API" với 4 folders:
- 1. Authentication
- 2. Get Profile
- 3. Update Profile - Valid Cases
- 4. Update Profile - Validation Errors

### Bước 2: Chạy từng request theo thứ tự

#### 📝 Test Flow:

**1. Login as Student** (hoặc Login as Tutor)
   - Mở request này
   - Click **Send**
   - ✅ Token sẽ tự động được lưu vào biến `{{token}}`

**2. Get Current User Profile**
   - Click **Send**
   - ✅ Xem thông tin profile hiện tại

**3. Update All Fields**
   - Click **Send**
   - ✅ Cập nhật personalEmail, phoneNumber, address

**4. Get Current User Profile** (lần 2)
   - Click **Send**
   - ✅ Verify dữ liệu đã được cập nhật

**5. Thử các test cases khác**
   - Update Only Email
   - Update Only Phone
   - Update Only Address
   - Invalid Email Format (expect 400)
   - Phone Too Short (expect 400)
   - Phone With Letters (expect 400)

---

## 📋 Cách thủ công (Không import)

### Bước 1: Tạo Environment

1. Click biểu tượng **⚙️ Settings** (góc trên bên phải)
2. Click **Environments** → **Create Environment**
3. Tên: `TutorHub Local`
4. Thêm 2 biến:
   - `base_url` = `http://localhost:8080/api`
   - `token` = (để trống)
5. Click **Save**
6. Chọn environment "TutorHub Local" ở dropdown góc trên bên phải

### Bước 2: Tạo Request đầu tiên - Login

1. Click **New** → **HTTP Request**
2. Đặt tên: `Login as Student`
3. Method: **POST**
4. URL: `{{base_url}}/auth/signin`
5. Tab **Body**:
   - Chọn **raw**
   - Chọn **JSON**
   - Paste:
   ```json
   {
     "email": "test@example.com",
     "password": "password123"
   }
   ```
6. Tab **Tests** (để tự động lưu token):
   ```javascript
   var jsonData = pm.response.json();
   pm.environment.set("token", jsonData.accessToken);
   ```
7. Click **Send**
8. ✅ Copy `accessToken` từ response

### Bước 3: Get Profile

1. Tạo request mới
2. Method: **GET**
3. URL: `{{base_url}}/profile`
4. Tab **Authorization**:
   - Type: **Bearer Token**
   - Token: `{{token}}`
5. Click **Send**

### Bước 4: Update Profile

1. Tạo request mới
2. Method: **PUT**
3. URL: `{{base_url}}/profile`
4. Tab **Authorization**:
   - Type: **Bearer Token**
   - Token: `{{token}}`
5. Tab **Body**:
   - Chọn **raw**
   - Chọn **JSON**
   - Paste:
   ```json
   {
     "personalEmail": "myemail@gmail.com",
     "phoneNumber": "0123456789",
     "address": "123 Nguyen Van Linh, Quan 7, TP.HCM"
   }
   ```
6. Click **Send**

---

## 🎯 Checklist Test nhanh

Sau khi import collection, chạy theo thứ tự:

- [ ] **1.1** Login as Student → Lưu token
- [ ] **2.1** Get Current User Profile → Xem profile ban đầu
- [ ] **3.1** Update All Fields → Cập nhật tất cả
- [ ] **2.1** Get Current User Profile → Verify đã update
- [ ] **3.2** Update Only Email → Test update từng field
- [ ] **4.1** Invalid Email Format → Test validation
- [ ] **4.2** Phone Too Short → Test validation
- [ ] **1.2** Login as Tutor → Test với Tutor account
- [ ] Lặp lại các bước trên với Tutor

---

## 💡 Tips

### Xem Response đẹp hơn
- Click tab **Pretty** trong response
- Chọn **JSON** format

### Xem Request đã gửi
- Click tab **Console** (góc dưới bên trái)
- Xem chi tiết request/response

### Chạy toàn bộ Collection
1. Click vào collection "TutorHub - Profile API"
2. Click **Run**
3. Chọn các requests muốn chạy
4. Click **Run TutorHub - Profile API**
5. Xem kết quả tổng hợp

### Debug khi có lỗi
- Xem tab **Console** để xem request/response chi tiết
- Kiểm tra biến `{{token}}` có giá trị chưa
- Kiểm tra server có đang chạy không (http://localhost:8080/api)

---

## 📊 Expected Results

### ✅ Thành công:
- Login: Status 200, có accessToken
- Get Profile: Status 200, có đầy đủ thông tin user
- Update Profile: Status 200, dữ liệu đã thay đổi

### ❌ Lỗi mong đợi:
- Get Profile không có token: Status 401
- Invalid email: Status 400
- Phone quá ngắn: Status 400
- Phone có chữ: Status 400

---

## 🆘 Troubleshooting

**Token không tự động lưu?**
- Kiểm tra đã chọn environment "TutorHub Local" chưa
- Kiểm tra tab Tests của request Login có script chưa

**401 Unauthorized?**
- Token đã hết hạn → Login lại
- Kiểm tra Authorization header format
- Kiểm tra biến `{{token}}` có giá trị

**Connection refused?**
- Server chưa chạy → Chạy `mvn spring-boot:run`
- Sai port → Kiểm tra server chạy ở port 8080

**400 Bad Request?**
- Kiểm tra JSON format
- Xem message trong response để biết lỗi validation

---

## 🎓 Học thêm

Xem file `POSTMAN_TESTING_GUIDE.md` để có hướng dẫn chi tiết hơn về:
- Tất cả test cases
- Validation rules
- Response examples
- Advanced Postman features

