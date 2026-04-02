Game Console Store - E-Commerce Platform

Tổng quan dự án (Project Overview)

  Game Console Store là hệ thống thương mại điện tử chuyên biệt dành cho việc kinh doanh các thiết bị chơi game (PlayStation, Xbox, Nintendo) và phụ kiện đi kèm.
  
  Hệ thống được thiết kế theo kiến trúc MVC (Model-View-Controller) sử dụng Spring Boot, đảm bảo hiệu suất cao, dễ dàng bảo trì và khả năng mở rộng trong tương lai. Dự án cung cấp trải nghiệm mua sắm mượt mà cho khách hàng và công cụ quản trị mạnh mẽ (Back-office) cho ban quản lý.

Các tính năng nổi bật (Key Features)

Dành cho Khách hàng (Storefront)

  Xác thực & Bảo mật (Authentication): Đăng nhập/Đăng ký truyền thống và tích hợp đăng nhập qua mạng xã hội (OAuth2 - Google/Facebook).

  Quản lý tài khoản: Cập nhật thông tin cá nhân, thay đổi mật khẩu, quản lý sổ địa chỉ giao hàng.

  Trải nghiệm mua sắm: Xem danh sách/chi tiết sản phẩm, lọc theo danh mục, tìm kiếm sản phẩm.
  
  Giỏ hàng & Đặt hàng: Quản lý giỏ hàng (Cart), quy trình thanh toán (Checkout) bảo mật.

  Quản lý đơn hàng: Theo dõi trạng thái đơn hàng, xem chi tiết hóa đơn, lịch sử mua hàng.

  Danh sách yêu thích (Wishlist): Lưu trữ các sản phẩm quan tâm.

Dành cho Quản trị viên (Admin Dashboard)

  Bảng điều khiển (Dashboard): Thống kê tổng quan về doanh thu, số lượng đơn hàng, người dùng mới. Trực quan hóa dữ liệu bằng biểu đồ (Chart.js).

  Quản lý danh mục (Category Management): Thêm, sửa, xóa danh mục sản phẩm.

  Quản lý sản phẩm (Product Management): CRUD sản phẩm, quản lý hình ảnh, giá cả, số lượng tồn kho.

  Quản lý đơn hàng (Order Management): Duyệt đơn, cập nhật trạng thái giao hàng, xử lý hủy đơn.

  Quản lý người dùng (User Management): Quản lý phân quyền (Role-based access control), khóa/mở khóa tài khoản.

  Báo cáo & Thống kê (Reports): Báo cáo doanh thu theo tháng, danh mục, top sản phẩm bán chạy.

Trải nghiệm Công nghệ (Tech Stack)

  Backend

    Ngôn ngữ: Java 17+

    Framework: Spring Boot 3.x (Spring Web, Spring Data JPA)

    Bảo mật: Spring Security, OAuth2 Client

    Quản lý Template: Thymeleaf

  Frontend
  
    Công nghệ lõi: HTML5, CSS3, JavaScript (ES6+)

    Thư viện: Chart.js (Vẽ biểu đồ phân tích dữ liệu)

    Kiến trúc CSS: Custom CSS tối ưu hóa cho giao diện Admin và Storefront.

  Database & Tools
  
    Database: MySQL 8.0+

    Quản lý phiên bản: Git & GitHub

    Công cụ Build: Maven

📂 Cấu trúc thư mục hệ thống (Project Structure)

sof3021-console-game-spring-boot
 ┣ 📂 src/main/java/com/console/game
 ┃ ┣ 📂 config       # Cấu hình hệ thống (Security, Web, OAuth2)
 ┃ ┣ 📂 controller   # Xử lý HTTP request (Customer & Admin APIs)
 ┃ ┣ 📂 dto          # Data Transfer Objects (Bảo mật data)
 ┃ ┣ 📂 enums        # Các hằng số Enum (Role, OrderStatus, PaymentMethod...)
 ┃ ┣ 📂 model        # Entities - Ánh xạ với CSDL (User, Product, Order...)
 ┃ ┣ 📂 repository   # Lớp giao tiếp với DB (Spring Data JPA)
 ┃ ┗ 📂 service      # Business Logic (Xử lý nghiệp vụ lõi)
 ┣ 📂 src/main/resources
 ┃ ┣ 📂 static       # Assets tĩnh (CSS, JS, Images)
 ┃ ┣ 📂 sql          # File khởi tạo DB (game_console_store.sql, seed_data.sql)
 ┃ ┣ 📂 templates    # Các view Thymeleaf (Admin, Checkout, Auth...)
 ┃ ┗ 📜 application.properties # Biến môi trường & cấu hình DB
 ┗ 📜 pom.xml        # Cấu hình dependency Maven
 
Hướng dẫn cài đặt & Triển khai (Setup & Installation)

1. Yêu cầu hệ thống (Prerequisites)

  JDK 17 trở lên.

  Maven 3.8+ (Có thể dùng Maven Wrapper mvnw đi kèm).

2. Các bước triển khai môi trường Dev

  Bước 1: Clone dự án từ kho lưu trữ
  
    git clone https://github.com/vtrong808/sof3021-console-game-spring-boot.git
    
    cd sof3021-console-game-spring-boot
    
  Bước 2: Cấu hình cơ sở dữ liệu

  Mở MySQL Workbench hoặc CLI, tạo database:
    
    CREATE DATABASE game_console_store;
    
  Import các script có sẵn trong thư mục src/main/resources/static/sql/:

    a. Import game_console_store.sql (Tạo cấu trúc bảng).

    b. Import seed_data.sql (Thêm dữ liệu mẫu).

  Mở file src/main/resources/application.properties và cập nhật thông tin kết nối Database của bạn:
  
    spring.datasource.url=jdbc:mysql://localhost:3306/game_console_store
    
    spring.datasource.username=root
    
    spring.datasource.password=your_password

  Bước 3: Khởi chạy ứng dụng
  
  Sử dụng Maven để build và run project:

    # Đối với Windows

    mvnw.cmd spring-boot:run

    # Đối với Linux/Mac
    
    ./mvnw spring-boot:run
    
  Bước 4: Truy cập hệ thống

  Trang khách hàng: http://localhost:8080/

  Trang quản trị viên: http://localhost:8080/admin (Sử dụng tài khoản có Role ADMIN trong bảng User để đăng nhập).

Cơ chế bảo mật (Security Rules)

  Dự án áp dụng các tiêu chuẩn bảo mật doanh nghiệp cơ bản:

  Mã hóa mật khẩu: Sử dụng BCryptPasswordEncoder để băm mật khẩu người dùng trước khi lưu vào database.

  Phân quyền truy cập (Authorization): * /admin/**: Chỉ cho phép người dùng có quyền ADMIN.

  /cart/**, /order/**: Yêu cầu người dùng phải đăng nhập (USER hoặc ADMIN).

  Bảo vệ tấn công CSRF: Được kích hoạt mặc định qua Spring Security.
  
Tác giả & Đóng góp (Contributors)

Tác giả chính: nhóm PANDA Development

Các thành viên 📂 repository   # Lớp giao tiếp với DB (Spring Data JPA)
 ┃ ┗ 📂 service      # Business Logic (Xử lý nghiệp vụ lõi)
 ┣ 📂 src/main/resources
 ┃ ┣ 📂 static       # Assets tĩnh (CSS, JS, Images)
 ┃ ┣ 📂 sql          # File khởi tạo DB (game_console_store.sql, seed_data.sql)
 ┃ ┣ 📂 templates    # Các view Thymeleaf (Admin, Checkout, Auth...)
 ┃ ┗ 📜 application.properties # Biến môi trường & cấu hình DB
 ┗ 📜 pom.xml        # Cấu hình dependency Maven
 
Hướng dẫn cài đặt & Triển khai (Setup & Installation)

1. Yêu cầu hệ thống (Prerequisites)

  JDK 17 trở lên.

  Maven 3.8+ (Có thể dùng Maven Wrapper mvnw đi kèm).

2. Các bước triển khai môi trường Dev

  Bước 1: Clone dự án từ kho lưu trữ
  
    git clone https://github.com/vtrong808/sof3021-console-game-spring-boot.git
    
    cd sof3021-console-game-spring-boot
    
  Bước 2: Cấu hình cơ sở dữ liệu

  Mở MySQL Workbench hoặc CLI, tạo database:
    
    CREATE DATABASE game_console_store;
    
  Import các script có sẵn trong thư mục src/main/resources/static/sql/:

    a. Import game_console_store.sql (Tạo cấu trúc bảng).

    b. Import seed_data.sql (Thêm dữ liệu mẫu).

  Mở file src/main/resources/application.properties và cập nhật thông tin kết nối Database của bạn:
  
    spring.datasource.url=jdbc:mysql://localhost:3306/game_console_store
    
    spring.datasource.username=root
    
    spring.datasource.password=your_password

  Bước 3: Khởi chạy ứng dụng
  
  Sử dụng Maven để build và run project:

    # Đối với Windows

    mvnw.cmd spring-boot:run

    # Đối với Linux/Mac
    
    ./mvnw spring-boot:run
    
  Bước 4: Truy cập hệ thống

  Trang khách hàng: http://localhost:8080/

  Trang quản trị viên: http://localhost:8080/admin (Sử dụng tài khoản có Role ADMIN trong bảng User để đăng nhập).

Cơ chế bảo mật (Security Rules)

  Dự án áp dụng các tiêu chuẩn bảo mật doanh nghiệp cơ bản:

  Mã hóa mật khẩu: Sử dụng BCryptPasswordEncoder để băm mật khẩu người dùng trước khi lưu vào database.

  Phân quyền truy cập (Authorization): * /admin/**: Chỉ cho phép người dùng có quyền ADMIN.

  /cart/**, /order/**: Yêu cầu người dùng phải đăng nhập (USER hoặc ADMIN).

  Bảo vệ tấn công CSRF: Được kích hoạt mặc định qua Spring Security.
  
Tác giả & Đóng góp (Contributors)

Tác giả chính: nhóm PANDA Development

Các thành viên 

  Võ Văn Trọng - vtrong808

  Nguyễn Bích Trâm - PS45829-NguyenBichTram

  Võ Thị Thúy Ngân - vttngan

  Vũ Đỗ Trường Kỳ - vudotruongky123

Khóa học/Môn học: SOF3021 - Java Framework (Spring Boot)
