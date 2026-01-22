-- =======================================================================================
-- PHẦN 1: KHỞI TẠO DATABASE
-- =======================================================================================
-- Sử dụng utf8mb4 để hỗ trợ Tiếng Việt có dấu và Emoji (quan trọng cho phần Review/Comment)
CREATE DATABASE IF NOT EXISTS game_console_store 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE game_console_store;

-- =======================================================================================
-- PHẦN 2: CÁC BẢNG MASTER DATA (Dữ liệu nền tảng, ít thay đổi)
-- =======================================================================================

-- 1. Bảng Thương hiệu (Brands)
-- Mapping Java: @Entity class Brand
CREATE TABLE Brands (
    brand_id INT PRIMARY KEY AUTO_INCREMENT, -- Khóa chính tự tăng
    
    brand_name VARCHAR(100) NOT NULL,        -- Tên hãng (Sony, Nintendo, Microsoft...)
    
    brand_description TEXT,                  -- Mô tả ngắn về hãng
    
    logo_url VARCHAR(255),                   -- Lưu đường dẫn ảnh (Cloudinary/Firebase), KHÔNG lưu file blob vào DB
    
    -- Audit fields (Dùng cho JPA Auditing: @CreatedDate, @LastModifiedDate)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, 
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. Bảng Danh mục (Categories)
-- Mapping Java: @Entity class Category
CREATE TABLE Categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    
    category_name VARCHAR(100) NOT NULL,     -- Tên loại (Console, Handheld, Accessories...)
    
    category_description TEXT,
    
    -- SOFT DELETE: Thay vì xóa hẳn dòng dữ liệu, ta chỉ đổi cờ này thành FALSE
    -- Giúp giữ lại lịch sử đơn hàng cũ liên quan đến danh mục này.
    is_active BOOLEAN DEFAULT TRUE,          
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- =======================================================================================
-- PHẦN 3: NGƯỜI DÙNG & PHÂN QUYỀN
-- =======================================================================================

-- 3. Bảng Người dùng (Users)
-- Mapping Java: @Entity class User implements UserDetails (Spring Security)
CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    
    full_name VARCHAR(255) NOT NULL,         -- Họ tên hiển thị
    
    email VARCHAR(255) UNIQUE NOT NULL,      -- Email là duy nhất, dùng làm username đăng nhập
    
    -- Mật khẩu có thể NULL nếu user đăng nhập bằng Google/Facebook
    -- Nếu đăng nhập thường: Lưu chuỗi mã hóa BCrypt (bắt đầu bằng $2a$...)
    password VARCHAR(255) NULL, 
    
    phone_number VARCHAR(15),                -- SĐT cá nhân
    
    -- Xác định nguồn tạo tài khoản (Logic OAuth2)
    provider ENUM('LOCAL', 'GOOGLE', 'FACEBOOK') DEFAULT 'LOCAL',
    
    provider_id VARCHAR(255) NULL,           -- ID định danh từ Google/Facebook trả về
    
    avatar_url VARCHAR(255) NULL,
    
    -- Phân quyền cho Spring Security
    role ENUM('ADMIN', 'STAFF', 'CUSTOMER') DEFAULT 'CUSTOMER',
    
    is_active BOOLEAN DEFAULT TRUE,          -- Admin có thể khóa tài khoản (ban user) bằng cột này
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 6. Bảng Địa chỉ nhận hàng (Addresses)
-- Tách riêng để một User có thể lưu nhiều địa chỉ (Nhà riêng, Cơ quan...)
CREATE TABLE Addresses (
    address_id INT PRIMARY KEY AUTO_INCREMENT,
    
    user_id INT NOT NULL,                    -- FK: Thuộc về user nào
    
    recipient_name VARCHAR(100),             -- Tên người nhận (có thể khác tên chủ tài khoản)
    phone_number VARCHAR(20),                -- SĐT người nhận
    
    address_line VARCHAR(255) NOT NULL,      -- Số nhà, tên đường
    city VARCHAR(100),                       -- Tỉnh/Thành phố
    district VARCHAR(100),                   -- Quận/Huyện
    
    is_default BOOLEAN DEFAULT FALSE,        -- Địa chỉ mặc định sẽ tự fill khi Checkout
    
    -- Khi xóa User, xóa luôn danh sách địa chỉ của họ
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);

-- =======================================================================================
-- PHẦN 4: SẢN PHẨM (CORE PRODUCT)
-- =======================================================================================

-- 4. Bảng Sản phẩm (Products)
CREATE TABLE Products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    
    brand_id INT,                            -- FK: Thuộc hãng nào
    category_id INT,                         -- FK: Thuộc danh mục nào
    
    product_name VARCHAR(255) NOT NULL,      -- Tên máy (PlayStation 5 Slim...)
    
    -- Dùng TEXT hoặc LONGTEXT để chứa HTML từ trình soạn thảo (CKEditor/TinyMCE)
    product_description TEXT, 
    
    -- Dùng DECIMAL cho tiền tệ để tránh lỗi làm tròn số của FLOAT/DOUBLE
    price DECIMAL(15, 2) NOT NULL CHECK (price >= 0),
    
    -- Quản lý kho hàng (Inventory)
    stock_quantity INT DEFAULT 0 CHECK (stock_quantity >= 0),
    
    thumbnail_url VARCHAR(255),              -- Ảnh đại diện (hiển thị ở trang danh sách cho nhẹ)
    
    is_active BOOLEAN DEFAULT TRUE,          -- Ẩn sản phẩm (hết hàng/ngừng kinh doanh) mà không xóa
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- Foreign Keys: Nếu xóa Brand/Category, sản phẩm chỉ bị set NULL chứ không bị xóa
    FOREIGN KEY (brand_id) REFERENCES Brands(brand_id) ON DELETE SET NULL,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id) ON DELETE SET NULL,
    
    -- Indexing: Tăng tốc độ tìm kiếm và sắp xếp
    INDEX idx_product_name (product_name),
    INDEX idx_price (price)
);

-- 5. Bảng Hình ảnh chi tiết (Product_Images)
-- Một sản phẩm có nhiều ảnh (Góc trái, phải, trên, dưới...)
CREATE TABLE Product_Images (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    
    product_id INT NOT NULL,
    
    image_url VARCHAR(255) NOT NULL,
    
    is_main BOOLEAN DEFAULT FALSE,           -- Cờ đánh dấu đây là ảnh chính (nếu thumbnail bị lỗi)
    
    -- Nếu xóa sản phẩm, xóa luôn bộ ảnh của nó (Tránh rác database)
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE
);

-- =======================================================================================
-- PHẦN 5: GIAO DỊCH & ĐƠN HÀNG (QUAN TRỌNG NHẤT)
-- =======================================================================================

-- 9. Bảng Giỏ hàng (Cart_Items)
-- Lưu ý: Giỏ hàng chỉ là nơi lưu tạm trước khi đặt hàng.
CREATE TABLE Cart_Items (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    
    quantity INT DEFAULT 1 CHECK (quantity > 0), -- Số lượng mua
    
    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    
    -- Ràng buộc: Một User với một Product chỉ xuất hiện 1 dòng trong bảng này.
    -- Nếu user thêm tiếp sản phẩm đó, ta Update quantity chứ không Insert dòng mới.
    UNIQUE KEY unique_cart_item (user_id, product_id)
);

-- 7. Bảng Đơn hàng (Orders)
-- Chứa thông tin tổng quan của một đơn hàng
CREATE TABLE Orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    
    user_id INT,                             -- Người đặt (có thể NULL nếu User bị xóa, nhưng đơn vẫn phải còn)
    
    -- SNAPSHOT DATA (Sao chụp dữ liệu):
    -- Ta phải lưu cứng Tên, SĐT, Địa chỉ TẠI THỜI ĐIỂM ĐẶT HÀNG.
    -- Vì nếu sau này User đổi địa chỉ trong profile, đơn hàng cũ KHÔNG được phép thay đổi theo.
    full_name VARCHAR(100), 
    phone_number VARCHAR(20), 
    shipping_address TEXT,                   -- Gộp toàn bộ địa chỉ thành chuỗi để in vận đơn
    
    total_amount DECIMAL(15, 2) NOT NULL,    -- Tổng tiền cuối cùng (đã trừ khuyến mãi, cộng ship)
    
    -- State Machine: Quy trình xử lý đơn hàng
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPING', 'DELIVERED', 'CANCELLED', 'RETURNED') DEFAULT 'PENDING',
    
    -- Phương thức thanh toán
    payment_method ENUM('COD', 'VNPAY', 'PAYPAL', 'BANK_TRANSFER') DEFAULT 'COD',
    
    -- Trạng thái thanh toán (Quan trọng để quyết định có ship hàng không)
    payment_status ENUM('UNPAID', 'PAID', 'REFUNDED') DEFAULT 'UNPAID',
    
    note TEXT,                               -- Ghi chú của khách (vd: "Giao giờ hành chính")
    
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL,
    
    -- Index giúp lọc đơn hàng nhanh theo trạng thái hoặc ngày tháng (Thống kê doanh thu)
    INDEX idx_order_status (status),
    INDEX idx_order_date (order_date)
);

-- 8. Bảng Chi tiết đơn hàng (Order_Items)
-- Lưu danh sách các món hàng trong đơn đó
CREATE TABLE Order_Items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    
    order_id INT NOT NULL,
    product_id INT,                          -- Sản phẩm đã mua
    
    quantity INT NOT NULL CHECK (quantity > 0),
    
    -- PRICE FREEZING (Đóng băng giá):
    -- CỰC KỲ QUAN TRỌNG: Lưu giá sản phẩm TẠI THỜI ĐIỂM MUA.
    -- Nếu sau này Admin sửa giá Product tăng lên gấp đôi, hóa đơn cũ vẫn phải giữ giá cũ.
    price_at_purchase DECIMAL(15, 2) NOT NULL, 
    
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE SET NULL
);

-- 11. Bảng Lịch sử giao dịch (Transactions)
-- Dùng để tích hợp cổng thanh toán Online (VNPay/Momo/Stripe)
CREATE TABLE Transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    
    order_id INT NOT NULL,
    
    payment_provider VARCHAR(50),            -- Ví dụ: 'VNPAY', 'PAYPAL'
    
    transaction_code VARCHAR(100),           -- Mã giao dịch từ phía Ngân hàng trả về (dùng để đối soát)
    
    amount DECIMAL(15, 2),                   -- Số tiền đã giao dịch
    
    status ENUM('SUCCESS', 'FAILED', 'PENDING') DEFAULT 'PENDING', -- Trạng thái giao dịch
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE
);

-- =======================================================================================
-- PHẦN 6: TƯƠNG TÁC NGƯỜI DÙNG
-- =======================================================================================

-- 10. Bảng Đánh giá (Reviews)
CREATE TABLE Reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    
    -- Logic Verified Purchase: Phải mua hàng (có order_id) mới được đánh giá
    order_id INT NOT NULL, 
    
    rating TINYINT CHECK (rating BETWEEN 1 AND 5), -- Chỉ cho phép từ 1 đến 5 sao
    
    comment TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    
    -- Ràng buộc: Mỗi người chỉ được đánh giá 1 lần cho 1 sản phẩm trong 1 đơn hàng cụ thể
    UNIQUE KEY unique_review (user_id, order_id, product_id)
);
