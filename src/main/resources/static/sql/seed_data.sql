USE game_console_store;

INSERT INTO Brands (brand_name, brand_description, logo_url) VALUES
('Sony', 'Hãng công nghệ Nhật Bản, nổi tiếng với PlayStation', 'https://logo.sony.png'),
('Microsoft', 'Tập đoàn công nghệ Mỹ, sở hữu Xbox', 'https://logo.microsoft.png'),
('Nintendo', 'Hãng game huyền thoại đến từ Nhật Bản', 'https://logo.nintendo.png');

INSERT INTO Categories (category_name, category_description) VALUES
('Console', 'Máy chơi game gia đình'),
('Handheld', 'Máy chơi game cầm tay'),
('Accessories', 'Phụ kiện chơi game');

INSERT INTO Users (full_name, email, password, phone_number, role) VALUES
('Admin System', 'admin@game.com', '123456', '0900000001', 'ADMIN'),
('Nguyễn Văn John', 'user1@gmail.com', '123456', '0900000002', 'CUSTOMER'),
('Trần Thị Lucy', 'user2@gmail.com', '123456', '0900000003', 'CUSTOMER');

INSERT INTO Addresses (user_id, recipient_name, phone_number, address_line, city, district, is_default) VALUES
(2, 'Nguyễn Văn John', '0900000002', '123 Lê Lợi', 'TP.HCM', 'Quận 1', TRUE),
(3, 'Trần Thị Lucy', '0900000003', '456 Hai Bà Trưng', 'Hà Nội', 'Hoàn Kiếm', TRUE);
INSERT INTO Products (brand_id, category_id, product_name, product_description, price, stock_quantity, thumbnail_url) VALUES
(1, 1, 'PlayStation 5 Slim', 'Máy chơi game thế hệ mới của Sony', 13990000, 10, 'https://ps5.png'),
(2, 1, 'Xbox Series X', 'Hiệu năng mạnh mẽ từ Microsoft', 13500000, 8, 'https://xbox.png'),
(3, 2, 'Nintendo Switch OLED', 'Máy chơi game lai cầm tay', 8990000, 15, 'https://switch.png');

INSERT INTO Product_Images (product_id, image_url, is_main) VALUES
(1, 'https://ps5-main.png', TRUE),
(1, 'https://ps5-2.png', FALSE),
(2, 'https://xbox-main.png', TRUE),
(3, 'https://switch-main.png', TRUE);

INSERT INTO Orders (user_id, full_name, phone_number, shipping_address, total_amount, status, payment_method, payment_status)
VALUES
(2, 'Nguyễn Văn John', '0900000002', '123 Lê Lợi, Quận 1, TP.HCM', 13990000, 'DELIVERED', 'COD', 'PAID');

INSERT INTO Order_Items (order_id, product_id, quantity, price_at_purchase)
VALUES
(1, 1, 1, 13990000);

INSERT INTO Reviews (user_id, product_id, order_id, rating, comment)
VALUES
(2, 1, 1, 5, 'Máy chạy rất mượt, đóng gói cẩn thận, cực kỳ hài lòng 👍');

INSERT INTO Transactions (order_id, payment_provider, transaction_code, amount, status)
VALUES
(1, 'COD', 'COD_001', 13990000, 'SUCCESS');