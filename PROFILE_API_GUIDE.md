# Profile Management API Guide

## Tổng quan

API này cho phép Student và Tutor chỉnh sửa thông tin cá nhân của họ. Các trường có thể chỉnh sửa bao gồm:
- **Personal Email**: Email cá nhân (khác với email đăng nhập)
- **Phone Number**: Số điện thoại
- **Address**: Địa chỉ

## Endpoints

### 1. Lấy thông tin profile hiện tại

**Endpoint:** `GET /profile`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
```

**Response Success (200):**
```json
{
  "uid": 1,
  "userName": "Nguyễn Văn A",
  "email": "student@tutorhub.com",
  "role": "STUDENT",
  "personalEmail": "nguyenvana@gmail.com",
  "phoneNumber": "0123456789",
  "address": "123 Đường ABC, Quận 1, TP.HCM"
}
```

**Response Error (401):**
```json
"User not authenticated"
```

### 2. Cập nhật thông tin profile

**Endpoint:** `PUT /profile`

**Headers:**
```
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

**Request Body:**
```json
{
  "personalEmail": "newemail@gmail.com",
  "phoneNumber": "0987654321",
  "address": "456 Đường XYZ, Quận 2, TP.HCM"
}
```

**Lưu ý:**
- Tất cả các trường đều là **optional** - bạn có thể gửi chỉ một hoặc nhiều trường cần cập nhật
- Các trường không gửi sẽ giữ nguyên giá trị cũ

**Validation Rules:**
- `personalEmail`: Phải là email hợp lệ
- `phoneNumber`: Phải là số từ 10-15 chữ số
- `address`: Tối đa 500 ký tự

**Response Success (200):**
```json
{
  "uid": 1,
  "userName": "Nguyễn Văn A",
  "email": "student@tutorhub.com",
  "role": "STUDENT",
  "personalEmail": "newemail@gmail.com",
  "phoneNumber": "0987654321",
  "address": "456 Đường XYZ, Quận 2, TP.HCM"
}
```

**Response Error (400) - Validation Failed:**
```json
{
  "timestamp": "2024-01-01T10:00:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Phone number must be between 10 and 15 digits"
}
```

**Response Error (401):**
```json
"User not authenticated"
```

## Ví dụ sử dụng

### Sử dụng cURL

#### 1. Lấy thông tin profile
```bash
curl -X GET http://localhost:8080/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 2. Cập nhật toàn bộ thông tin
```bash
curl -X PUT http://localhost:8080/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "personalEmail": "newemail@gmail.com",
    "phoneNumber": "0987654321",
    "address": "456 Đường XYZ, Quận 2, TP.HCM"
  }'
```

#### 3. Cập nhật chỉ số điện thoại
```bash
curl -X PUT http://localhost:8080/profile \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "phoneNumber": "0987654321"
  }'
```

### Sử dụng JavaScript/Fetch

```javascript
// Lấy thông tin profile
async function getProfile() {
  const token = localStorage.getItem('token');
  
  const response = await fetch('http://localhost:8080/profile', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  
  if (response.ok) {
    const profile = await response.json();
    console.log('Profile:', profile);
    return profile;
  } else {
    console.error('Failed to get profile');
  }
}

// Cập nhật profile
async function updateProfile(data) {
  const token = localStorage.getItem('token');
  
  const response = await fetch('http://localhost:8080/profile', {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });
  
  if (response.ok) {
    const updatedProfile = await response.json();
    console.log('Profile updated:', updatedProfile);
    return updatedProfile;
  } else {
    console.error('Failed to update profile');
  }
}

// Ví dụ sử dụng
updateProfile({
  personalEmail: 'newemail@gmail.com',
  phoneNumber: '0987654321',
  address: '456 Đường XYZ, Quận 2, TP.HCM'
});
```

## Database Schema

Các trường mới đã được thêm vào bảng `users`:

```sql
ALTER TABLE users 
ADD COLUMN personal_email VARCHAR(255),
ADD COLUMN phone_number VARCHAR(15),
ADD COLUMN address VARCHAR(500);
```

**Lưu ý:** Nếu bạn đang sử dụng `spring.jpa.hibernate.ddl-auto=update`, các cột này sẽ được tự động tạo khi khởi động ứng dụng.

## Security

- Tất cả các endpoint đều yêu cầu JWT authentication
- User chỉ có thể xem và chỉnh sửa profile của chính họ
- Email đăng nhập (`email`), `userName`, và `role` **KHÔNG THỂ** được thay đổi qua API này
- Password không được trả về trong response

## Testing

### Test với Postman

1. **Đăng nhập để lấy JWT token:**
   - POST `http://localhost:8080/auth/signin`
   - Body: 
     ```json
     {
       "email": "student@tutorhub.com",
       "password": "student123"
     }
     ```
   - Copy JWT token từ response

2. **Lấy profile:**
   - GET `http://localhost:8080/profile`
   - Headers: `Authorization: Bearer <JWT_TOKEN>`

3. **Cập nhật profile:**
   - PUT `http://localhost:8080/profile`
   - Headers: 
     - `Authorization: Bearer <JWT_TOKEN>`
     - `Content-Type: application/json`
   - Body:
     ```json
     {
       "personalEmail": "test@gmail.com",
       "phoneNumber": "0123456789",
       "address": "Test Address"
     }
     ```

## Troubleshooting

### Lỗi 401 Unauthorized
- Kiểm tra JWT token có hợp lệ không
- Kiểm tra token có được gửi trong header `Authorization` không
- Kiểm tra token có hết hạn không

### Lỗi 400 Bad Request
- Kiểm tra format của email có đúng không
- Kiểm tra số điện thoại có từ 10-15 chữ số không
- Kiểm tra địa chỉ có vượt quá 500 ký tự không

### Lỗi 500 Internal Server Error
- Kiểm tra logs của server
- Kiểm tra database connection
- Kiểm tra các trường trong request body có đúng format không

