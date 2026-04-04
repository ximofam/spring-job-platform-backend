## 1. Nguyên tắc bắt buộc

* **KHÔNG** dùng Entity để nhận/trả dữ liệu ở Controller. Bắt buộc dùng DTO.
* Tách biệt class nhận dữ liệu (Request) và class trả dữ liệu (Response).

## 2. Cách đặt tên

``
<Tên nghiệp vụ> + <Request hoặc Response>
``

* **Gửi dữ liệu lên (Request):** * Tạo mới: `CreateRequest`
    * Cập nhật: `UpdateRequest`
    * Đăng nhập: `LoginRequest`
* **Trả dữ liệu về (Response):**
    * Lấy danh sách: `ItemResponse` (Chỉ chứa vài field cơ bản).
    * Lấy chi tiết 1 object: `DetailResponse` (Chứa đầy đủ field).

## 3. Cấu trúc Code (Gom nhóm DTO)

Tạo một class cha làm thư mục chứa các class con.

```java
public class CategoryDto {

    // Chặn khởi tạo class cha
    private CategoryDto() {} 

    // --- REQUEST ---
    @Data
    public static class CreateRequest {
        private String name;
        private String description;
    }

    // --- RESPONSE ---
    @Data
    public static class ItemResponse {
        private Integer id;
        private String name;
    }

    @Data
    public static class DetailResponse {
        private Integer id;
        private String name;
        private String description;
    }
}