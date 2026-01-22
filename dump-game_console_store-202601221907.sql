CREATE DATABASE game_console_store CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE game_console_store;

-- 1. Bảng Thương hiệu 
CREATE TABLE Brands (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT	-- Dùng để viết mô tả về thương hiệu
);

-- 2. Bảng Danh mục (Loại máy chơi game)
CREATE TABLE Categories (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT	-- Dùng để viết mô tả về loại máy chơi game
);

-- 3. Bảng Người dùng (Hỗ trợ tài khoản Google)
CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NULL, -- NULL khi dùng Google đăng ký
    provider VARCHAR(50) DEFAULT 'local', -- 'google' hoặc 'local'
    provider_id VARCHAR(255) NULL, -- ID định danh từ Google
    avatar VARCHAR(255) NULL,
    role ENUM('admin', 'customer') DEFAULT 'customer',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP	-- Ngày tạo tài khoản
);

-- 4. Bảng Sản phẩm
CREATE TABLE Products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    brand_id INT,
    category_id INT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(15, 2) NOT NULL CHECK(price >=0),	-- Giá không được bé hơn 0
    stock_quantity INT DEFAULT 0 CHECK(stock_quantity >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (brand_id) REFERENCES Brands(id) ON DELETE SET NULL,	-- Khi xóa thương hiệu thì các sản phẩm vẫn còn & chỉ mất thương hiệu
    FOREIGN KEY (category_id) REFERENCES Categories(id) ON DELETE SET NULL,
    INDEX (name),	-- Thêm INDEX để tối ưu thời gian tìm kiếm sản phẩm
    INDEX (brand_id),
    INDEX (category_id)
);

-- 5. Bảng Hình ảnh sản phẩm
CREATE TABLE Product_Images (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    image_url VARCHAR(255) NOT NULL,
    is_main BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE	-- Khi xóa bảng cha thì bảng con cũng mất dữ liệu (Tránh để lại dữ liệu rác)
);

-- 6. Bảng Địa chỉ người dùng đặt hàng
CREATE TABLE Addresses (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    phone_number VARCHAR(20),
    address_line VARCHAR(255),
    city VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

-- 7. Bảng Đơn hàng
CREATE TABLE Orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    address_id INT,
    total_amount DECIMAL(15, 2),
    shipping_address_snapshot TEXT, -- Lưu địa chỉ dạng chữ tại lúc đặt hàng
    status ENUM('pending', 'processing', 'shipped', 'cancelled') DEFAULT 'pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (address_id) REFERENCES Addresses(id) ON DELETE SET NULL,
    INDEX (user_id),
    INDEX (status)
);

-- 8. Bảng Chi tiết đơn hàng
CREATE TABLE Order_Items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    product_id INT,
    quantity INT NOT NULL,
    price_at_time DECIMAL(15, 2) NOT NULL, -- Lưu giá lúc mua tại thời điểm đó (Khi giảm giá sản phẩm thì hóa đơn cũ không bị đổi giá)
    FOREIGN KEY (order_id) REFERENCES Orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE SET NULL,
    INDEX (order_id),
    INDEX (product_id)
);

-- 9. Bảng Giỏ hàng
CREATE TABLE Cart (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    product_id INT,
    quantity INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE,
    UNIQUE (user_id, product_id) -- Một người dùng có thể có nhiều sản phẩm, nhưng một cặp (user-product) chỉ xuất hiện 1 dòng
);

-- 10. Bảng Đánh giá của người dùng
CREATE TABLE Reviews (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    product_id INT,
    rating INT CHECK (rating >= 1 AND rating <= 5),	-- Mức đánh giá theo số sao
    comment TEXT,	-- Mô tả sản phẩm từ phía người dùng
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(id) ON DELETE CASCADE,
    INDEX (product_id)
);


-- Thương hiệu & Danh mục
INSERT INTO Brands (name, description) VALUES ('Sony', 'PlayStation Division'), ('Nintendo', 'Switch & Handheld');
INSERT INTO Categories (name, description) VALUES ('Consoles', 'Home gaming systems'), ('Accessories', 'Controllers, Cables...');

-- Sản phẩm mẫu
INSERT INTO Products (brand_id, category_id, name, description, price, stock_quantity) 
VALUES (1, 1, 'PlayStation 5 Pro', 'Mạnh mẽ nhất hiện nay', 19000000, 5);

-- Người dùng mẫu
INSERT INTO Users (full_name, email, role) VALUES ('Admin Test', 'admin@gamestore.com', 'admin');

-- Test Giỏ hàng (Thử chạy 2 lần dòng này để xem UNIQUE có hoạt động không)
INSERT INTO Cart (user_id, product_id, quantity) VALUES (1, 1, 1);



