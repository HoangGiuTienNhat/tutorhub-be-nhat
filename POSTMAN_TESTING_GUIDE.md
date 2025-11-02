# 🧪 Hướng dẫn Test API Profile với Postman

## 📌 Thông tin cơ bản

- **Base URL**: `http://localhost:8080/api`
- **Server đang chạy**: Port 8080
- **Context Path**: `/api`

---

## 🔐 BƯỚC 1: Đăng nhập để lấy JWT Token

### Endpoint: POST /auth/signin

**URL**: `http://localhost:8080/api/auth/signin`

**Method**: `POST`

**Headers**:
```
Content-Type: application/json
```

**Body** (chọn raw → JSON):
```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

**Các tài khoản test có sẵn**:
1. **Student**: 
   - Email: `test@example.com`
   - Password: `password123`

2. **Tutor**: 
   - Email: `tutor@example.com`
   - Password: `password123`

3. **Faculty**: 
   - Email: `faculty@example.com`
   - Password: `password123`

4. **Phòng CTSV**: 
   - Email: `ctsv@example.com`
   - Password: `password123`

5. **Phòng Đào tạo**: 
   - Email: `daotao@example.com`
   - Password: `password123`

### ✅ Response mẫu (200 OK):
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjk...",
  "tokenType": "Bearer",
  "user": {
    "uid": 1,
    "userName": "Test User",
    "email": "test@example.com",
    "role": "STUDENT",
    "personalEmail": null,
    "phoneNumber": null,
    "address": null
  }
}
```

**📝 Lưu ý**: Copy giá trị `accessToken` để dùng cho các request tiếp theo!

---

## 👤 BƯỚC 2: Lấy thông tin Profile hiện tại

### Endpoint: GET /profile

**URL**: `http://localhost:8080/api/profile`

**Method**: `GET`

**Headers**:
```
Authorization: Bearer <YOUR_ACCESS_TOKEN>
```

### Cách thêm Authorization trong Postman:
1. Chọn tab **Headers**
2. Thêm key: `Authorization`
3. Value: `Bearer ` + token (có dấu cách sau chữ Bearer)
   - Ví dụ: `Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiaWF0IjoxNjk...`

**HOẶC** dùng tab **Authorization**:
1. Type: `Bearer Token`
2. Token: paste token vào (không cần chữ "Bearer")

### ✅ Response mẫu (200 OK):
```json
{
  "uid": 1,
  "userName": "Test User",
  "email": "test@example.com",
  "role": "STUDENT",
  "personalEmail": null,
  "phoneNumber": null,
  "address": null
}
```

### ❌ Lỗi thường gặp:

**401 Unauthorized** - Token không hợp lệ hoặc thiếu:
```json
{
  "timestamp": "2025-11-02T16:55:07.123+00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/profile"
}
```
→ **Giải pháp**: Kiểm tra lại token và header Authorization

---

## ✏️ BƯỚC 3: Cập nhật thông tin Profile

### Endpoint: PUT /profile

**URL**: `http://localhost:8080/api/profile`

**Method**: `PUT`

**Headers**:
```
Authorization: Bearer <YOUR_ACCESS_TOKEN>
Content-Type: application/json
```

**Body** (chọn raw → JSON):

### Test Case 1: Cập nhật tất cả các trường
```json
{
  "personalEmail": "myemail@gmail.com",
  "phoneNumber": "0123456789",
  "address": "123 Nguyen Van Linh, Quan 7, TP.HCM"
}
```

### Test Case 2: Cập nhật chỉ email
```json
{
  "personalEmail": "newemail@gmail.com"
}
```

### Test Case 3: Cập nhật chỉ số điện thoại
```json
{
  "phoneNumber": "0987654321"
}
```

### Test Case 4: Cập nhật chỉ địa chỉ
```json
{
  "address": "456 Le Van Viet, Thu Duc, TP.HCM"
}
```

### ✅ Response mẫu (200 OK):
```json
{
  "uid": 1,
  "userName": "Test User",
  "email": "test@example.com",
  "role": "STUDENT",
  "personalEmail": "myemail@gmail.com",
  "phoneNumber": "0123456789",
  "address": "123 Nguyen Van Linh, Quan 7, TP.HCM"
}
```

### ❌ Lỗi Validation:

**400 Bad Request** - Email không hợp lệ:
```json
{
  "personalEmail": "invalid-email"
}
```
Response:
```json
{
  "timestamp": "2025-11-02T16:55:07.123+00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Personal email must be a valid email address",
  "path": "/api/profile"
}
```

**400 Bad Request** - Số điện thoại không hợp lệ:
```json
{
  "phoneNumber": "123"
}
```
Response:
```json
{
  "message": "Phone number must be between 10 and 15 digits"
}
```

**400 Bad Request** - Địa chỉ quá dài (>500 ký tự):
```json
{
  "address": "very long address..."
}
```
Response:
```json
{
  "message": "Address must be less than 500 characters"
}
```

---

## 🧪 BƯỚC 4: Kiểm tra lại sau khi cập nhật

Sau khi cập nhật thành công, gọi lại **GET /profile** để xác nhận dữ liệu đã được lưu:

**URL**: `http://localhost:8080/api/profile`

**Method**: `GET`

**Headers**:
```
Authorization: Bearer <YOUR_ACCESS_TOKEN>
```

### ✅ Response sẽ hiển thị thông tin đã cập nhật:
```json
{
  "uid": 1,
  "userName": "Test User",
  "email": "test@example.com",
  "role": "STUDENT",
  "personalEmail": "myemail@gmail.com",
  "phoneNumber": "0123456789",
  "address": "123 Nguyen Van Linh, Quan 7, TP.HCM"
}
```

---

## 📊 Tổng kết các Test Cases

### ✅ Test Cases cần thực hiện:

1. **Authentication**
   - [ ] Đăng nhập với Student account
   - [ ] Đăng nhập với Tutor account
   - [ ] Lưu JWT token

2. **Get Profile**
   - [ ] Lấy profile với token hợp lệ
   - [ ] Lấy profile không có token (expect 401)
   - [ ] Lấy profile với token không hợp lệ (expect 401)

3. **Update Profile - Valid Cases**
   - [ ] Cập nhật tất cả 3 trường
   - [ ] Cập nhật chỉ personalEmail
   - [ ] Cập nhật chỉ phoneNumber
   - [ ] Cập nhật chỉ address
   - [ ] Cập nhật 2 trong 3 trường

4. **Update Profile - Validation Errors**
   - [ ] Email không hợp lệ (expect 400)
   - [ ] Số điện thoại < 10 số (expect 400)
   - [ ] Số điện thoại > 15 số (expect 400)
   - [ ] Số điện thoại có ký tự không phải số (expect 400)
   - [ ] Địa chỉ > 500 ký tự (expect 400)

5. **Verify Changes**
   - [ ] GET profile sau mỗi lần update để verify

---

## 💡 Tips khi test với Postman

1. **Tạo Environment**:
   - Tạo environment "TutorHub Local"
   - Thêm biến `base_url` = `http://localhost:8080/api`
   - Thêm biến `token` để lưu JWT token
   - Sử dụng `{{base_url}}` và `{{token}}` trong requests

2. **Tự động lưu token**:
   - Trong tab **Tests** của request signin, thêm:
   ```javascript
   pm.test("Save token", function () {
       var jsonData = pm.response.json();
       pm.environment.set("token", jsonData.accessToken);
   });
   ```

3. **Tạo Collection**:
   - Tạo collection "Profile API Tests"
   - Thêm tất cả requests vào collection
   - Set Authorization ở collection level

4. **Test Scripts**:
   - Thêm assertions để tự động verify response
   ```javascript
   pm.test("Status code is 200", function () {
       pm.response.to.have.status(200);
   });
   
   pm.test("Response has personalEmail", function () {
       var jsonData = pm.response.json();
       pm.expect(jsonData).to.have.property('personalEmail');
   });
   ```

---

## 🎯 Kết quả mong đợi

Sau khi test xong, bạn sẽ:
- ✅ Có thể đăng nhập và lấy JWT token
- ✅ Có thể xem thông tin profile của user hiện tại
- ✅ Có thể cập nhật personalEmail, phoneNumber, address
- ✅ Validation hoạt động đúng với dữ liệu không hợp lệ
- ✅ Dữ liệu được lưu vào database và persist sau khi cập nhật

---

## 🐛 Troubleshooting

**Vấn đề**: Server không chạy
- Kiểm tra MySQL đã chạy chưa
- Kiểm tra port 8080 có bị chiếm không
- Xem log trong terminal

**Vấn đề**: 401 Unauthorized
- Kiểm tra token có đúng không
- Kiểm tra header Authorization format: `Bearer <token>`
- Token có thể đã hết hạn, đăng nhập lại

**Vấn đề**: 400 Bad Request
- Kiểm tra format JSON có đúng không
- Kiểm tra validation rules
- Xem message trong response để biết lỗi cụ thể

**Vấn đề**: 500 Internal Server Error
- Xem log trong terminal backend
- Kiểm tra database connection
- Kiểm tra dữ liệu trong request

