-- PHẦN 7: INSERT
-- =======================================================================================
USE game_console_store;
-- 1. brands
INSERT INTO brands (brand_name, brand_description, logo_url) VALUES
('Sony', 'Sony Interactive Entertainment - PlayStation', 'https://logo.clearbit.com/playstation.com'),
('Nintendo', 'Nintendo - Switch ecosystem', 'https://logo.clearbit.com/nintendo.com'),
('Microsoft', 'Microsoft - Xbox ecosystem', 'https://logo.clearbit.com/xbox.com');

-- 2. categories
INSERT INTO categories (category_name, category_description) VALUES
('Console', 'Máy chơi game console'),
('Game Disc', 'Đĩa game bản quyền'),
('Controller', 'Tay cầm chơi game'),
('Accessories', 'Phụ kiện gaming'),
('Subscription', 'Gói dịch vụ game');

-- 3. users
INSERT INTO users (username, full_name, email, password, phone_number, provider, role, is_active) VALUES
('admin', 'Admin System', 'adminsystem@gameconsole.com', '123456', '0900000000', 'LOCAL', 'ADMIN', true),
('customer', 'Demo Customer', 'democustomer@gmail.com', '123456', '0988888888', 'LOCAL', 'CUSTOMER', true);

-- 4. addresses
INSERT INTO addresses (user_id, recipient_name, phone_number, address_line, city, district, is_default) VALUES
(2, 'Demo Customer', '0988888888', '123 Nguyễn Trãi', 'TP.HCM', 'Quận 1', true),
(2, 'Demo Customer', '0988888888', '456 Lê Văn Việt', 'TP.HCM', 'TP Thủ Đức', false);

-- 5. products
INSERT INTO products (brand_id, category_id, product_name, price, stock_quantity, thumbnail_url)
VALUES
(1, 1, 'PlayStation 5 Slim', 12990000, 50, 'https://example.com/ps5.jpg'),
(1, 1, 'PlayStation 5 Pro', 18990000, 30, 'https://example.com/ps5pro.jpg'),
(1, 2, 'God of War Ragnarok (PS5)', 1790000, 100, 'https://example.com/gow.jpg'),
(1, 2, 'Spider-Man 2 (PS5)', 1890000, 100, 'https://example.com/spiderman2.jpg'),
(1, 3, 'DualSense Wireless Controller', 1890000, 200, 'https://example.com/dualsense.jpg'),
(1, 4, 'PlayStation VR2', 14990000, 20, 'https://example.com/psvr2.jpg');
INSERT INTO products (brand_id, category_id, product_name, price, stock_quantity, thumbnail_url)
VALUES
(2, 1, 'Nintendo Switch OLED', 8990000, 60, 'https://example.com/switch-oled.jpg'),
(2, 1, 'Nintendo Switch Lite', 4990000, 80, 'https://example.com/switch-lite.jpg'),
(2, 2, 'Zelda: Tears of the Kingdom', 1590000, 120, 'https://example.com/zelda.jpg'),
(2, 2, 'Mario Kart 8 Deluxe', 1490000, 150, 'https://example.com/mariokart.jpg'),
(2, 3, 'Joy-Con Controller Set', 1990000, 200, 'https://example.com/joycon.jpg'),
(2, 4, 'Nintendo Pro Controller', 1790000, 100, 'https://example.com/procontroller.jpg');
INSERT INTO products (brand_id, category_id, product_name, price, stock_quantity, thumbnail_url)
VALUES
(3, 1, 'Xbox Series X', 13990000, 40, 'https://example.com/xbox-x.jpg'),
(3, 1, 'Xbox Series S', 6990000, 70, 'https://example.com/xbox-s.jpg'),
(3, 2, 'Forza Horizon 5', 1590000, 90, 'https://example.com/forza5.jpg'),
(3, 2, 'Halo Infinite', 1490000, 80, 'https://example.com/halo.jpg'),
(3, 3, 'Xbox Wireless Controller', 1790000, 150, 'https://example.com/xbox-controller.jpg'),
(3, 5, 'Xbox Game Pass Ultimate (12 months)', 2990000, 200, 'https://example.com/gamepass.jpg');

-- 6. product_images


-- 7. cart_items
INSERT INTO cart_items (user_id, product_id, quantity)
VALUES
(2, 1, 1),
(2, 9, 2);

-- 8. orders
INSERT INTO orders (user_id, full_name, phone_number, shipping_address, total_amount, status, payment_method, payment_status)
VALUES
(2, 'Demo Customer', '0988888888', '123 Nguyễn Trãi, Quận 1, TP.HCM', 14780000, 'CONFIRMED', 'COD', 'PAID');

-- 9. order_items
INSERT INTO order_items (order_id, product_id, quantity, price_at_purchase)
VALUES
(1, 1, 1, 12990000),
(1, 5, 1, 1790000);

-- 10. transactions
INSERT INTO transactions (order_id, payment_provider, transaction_code, amount, status)
VALUES
(1, 'COD', 'COD2026-0001', 14780000, 'SUCCESS');

-- 11. reviews
INSERT INTO reviews (user_id, product_id, order_id, rating, comment)
VALUES
(2, 1, 1, 5, 'PS5 rất mượt, đáng tiền!'),
(2, 5, 1, 4, 'Tay cầm DualSense rất tốt nhưng hơi đắt.');