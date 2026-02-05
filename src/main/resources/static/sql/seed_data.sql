=======================================================================================
-- PHẦN 7: INSERT
-- =======================================================================================

-- 1. brands
INSERT INTO brands (brand_name, brand_description, logo_url) VALUES
('Sony', 'Hãng console PlayStation', 'sony.png'),
('Microsoft', 'Hãng Xbox', 'microsoft.png'),
('Nintendo', 'Hãng console gia đình', 'nintendo.png'),
('Valve', 'Steam & Steam Deck', 'valve.png'),
('Asus', 'ROG gaming devices', 'asus.png'),
('MSI', 'Gaming hardware', 'msi.png'),
('Razer', 'Gaming accessories', 'razer.png'),
('Logitech', 'Gaming gear', 'logitech.png'),
('HyperX', 'Gaming audio', 'hyperx.png'),
('Anbernic', 'Retro handheld console', 'anbernic.png');

-- 2. categories
INSERT INTO categories (category_name, category_description) VALUES
('Console', 'Máy chơi game gia đình'),
('Handheld', 'Máy chơi game cầm tay'),
('Accessories', 'Phụ kiện gaming'),
('Controller', 'Tay cầm chơi game'),
('Headset', 'Tai nghe gaming'),
('Keyboard', 'Bàn phím gaming'),
('Mouse', 'Chuột gaming'),
('Charging Dock', 'Dock sạc'),
('Storage', 'Ổ cứng, thẻ nhớ'),
('Retro', 'Máy chơi game cổ điển');

-- 3. users
INSERT INTO users (full_name, email, password, phone_number, role) VALUES
('Nguyen Van A', 'a@gmail.com', '$2a$demo', '090000001', 'CUSTOMER'),
('Tran Van B', 'b@gmail.com', '$2a$demo', '090000002', 'CUSTOMER'),
('Le Van C', 'c@gmail.com', '$2a$demo', '090000003', 'CUSTOMER'),
('Pham Van D', 'd@gmail.com', '$2a$demo', '090000004', 'CUSTOMER'),
('Hoang Van E', 'e@gmail.com', '$2a$demo', '090000005', 'CUSTOMER'),
('Admin', 'admin@gmail.com', '$2a$admin', '099999999', 'ADMIN'),
('Staff 1', 'staff1@gmail.com', '$2a$staff', '088888881', 'STAFF'),
('Staff 2', 'staff2@gmail.com', '$2a$staff', '088888882', 'STAFF'),
('Customer X', 'x@gmail.com', '$2a$demo', '090000010', 'CUSTOMER'),
('Customer Y', 'y@gmail.com', '$2a$demo', '090000011', 'CUSTOMER');

-- 4. addresses
INSERT INTO addresses (user_id, recipient_name, phone_number, address_line, city, district, is_default) VALUES
(1,'Nguyen Van A','090000001','123 Le Loi','HCM','Q1',TRUE),
(2,'Tran Van B','090000002','456 Nguyen Hue','HCM','Q1',TRUE),
(3,'Le Van C','090000003','789 Tran Hung Dao','HCM','Q5',TRUE),
(4,'Pham Van D','090000004','12 Ly Thuong Kiet','HN','Hai Ba Trung',TRUE),
(5,'Hoang Van E','090000005','34 Kim Ma','HN','Ba Dinh',TRUE),
(6,'Admin','099999999','FPT Building','HN','Cau Giay',TRUE),
(7,'Staff 1','088888881','Nguyen Trai','HCM','Q5',TRUE),
(8,'Staff 2','088888882','Vo Van Tan','HCM','Q3',TRUE),
(9,'Customer X','090000010','Pham Van Dong','HN','Bac Tu Liem',TRUE),
(10,'Customer Y','090000011','Tan Ky Tan Quy','HCM','Tan Phu',TRUE);

-- 5. products
INSERT INTO products (brand_id, category_id, product_name, price, stock_quantity) VALUES
(1,1,'PlayStation 5 Slim',13000000,50),
(2,1,'Xbox Series X',14000000,40),
(3,1,'Nintendo Switch OLED',9000000,60),
(4,2,'Steam Deck OLED',16000000,30),
(5,2,'ROG Ally',17000000,25),
(6,4,'DualSense Controller',1800000,100),
(7,4,'Xbox Controller',1700000,100),
(8,5,'Razer BlackShark V2',2500000,80),
(9,7,'Logitech G Pro Mouse',2200000,70),
(10,10,'Anbernic RG35XX',2500000,40);

-- 6. product_images
INSERT INTO product_images (product_id, image_url, is_main) VALUES
(1,'ps5.jpg',TRUE),
(2,'xbox.jpg',TRUE),
(3,'switch.jpg',TRUE),
(4,'steamdeck.jpg',TRUE),
(5,'rogally.jpg',TRUE),
(6,'dualsense.jpg',TRUE),
(7,'xbox_controller.jpg',TRUE),
(8,'razer_headset.jpg',TRUE),
(9,'logitech_mouse.jpg',TRUE),
(10,'anbernic.jpg',TRUE);

-- 7. cart_items
INSERT INTO cart_items (user_id, product_id, quantity) VALUES
(1,1,1),(2,2,1),(3,3,2),(4,4,1),(5,5,1),
(6,6,2),(7,7,1),(8,8,1),(9,9,1),(10,10,1);

-- 8. orders
INSERT INTO orders (user_id, full_name, phone_number, shipping_address, total_amount, status) VALUES
(1,'Nguyen Van A','090000001','123 Le Loi, HCM',13000000,'DELIVERED'),
(2,'Tran Van B','090000002','456 Nguyen Hue, HCM',14000000,'DELIVERED'),
(3,'Le Van C','090000003','789 Tran Hung Dao, HCM',18000000,'SHIPPING'),
(4,'Pham Van D','090000004','12 Ly Thuong Kiet, HN',16000000,'CONFIRMED'),
(5,'Hoang Van E','090000005','34 Kim Ma, HN',17000000,'PENDING'),
(6,'Admin','099999999','FPT Building',3600000,'DELIVERED'),
(7,'Staff 1','088888881','Nguyen Trai',1700000,'DELIVERED'),
(8,'Staff 2','088888882','Vo Van Tan',2500000,'DELIVERED'),
(9,'Customer X','090000010','Pham Van Dong',2200000,'DELIVERED'),
(10,'Customer Y','090000011','Tan Ky Tan Quy',2500000,'DELIVERED');

-- 9. order_items
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase) VALUES
(1,1,1,13000000),
(2,2,1,14000000),
(3,3,2,9000000),
(4,4,1,16000000),
(5,5,1,17000000),
(6,6,2,1800000),
(7,7,1,1700000),
(8,8,1,2500000),
(9,9,1,2200000),
(10,10,1,2500000);

-- 10. transactions
INSERT INTO transactions (order_id, payment_provider, transaction_code, amount, status) VALUES
(1,'VNPAY','VN001',13000000,'SUCCESS'),
(2,'PAYPAL','PP002',14000000,'SUCCESS'),
(3,'VNPAY','VN003',18000000,'PENDING'),
(4,'COD','COD004',16000000,'PENDING'),
(5,'COD','COD005',17000000,'PENDING'),
(6,'VNPAY','VN006',3600000,'SUCCESS'),
(7,'COD','COD007',1700000,'SUCCESS'),
(8,'PAYPAL','PP008',2500000,'SUCCESS'),
(9,'VNPAY','VN009',2200000,'SUCCESS'),
(10,'VNPAY','VN010',2500000,'SUCCESS');

-- 11. reviews
INSERT INTO reviews (user_id, product_id, order_id, rating, comment) VALUES
(1,1,1,5,'Rất hài lòng'),
(2,2,2,5,'Máy mạnh'),
(3,3,3,4,'Chơi gia đình rất vui'),
(4,4,4,5,'Steam Deck quá ngon'),
(5,5,5,4,'Hiệu năng tốt'),
(6,6,6,5,'Tay cầm xịn'),
(7,7,7,4,'Cầm chắc tay'),
(8,8,8,5,'Âm thanh hay'),
(9,9,9,5,'Chuột nhẹ'),
(10,10,10,4,'Chơi game retro ổn');