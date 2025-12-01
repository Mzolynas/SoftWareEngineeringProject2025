-- Create a completely new database with the name you want
CREATE DATABASE IF NOT EXISTS shoe_inventory_db;
USE shoe_inventory_db;
DROP TABLE shoes;

-- Create the table
CREATE TABLE shoes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100) NOT NULL,
    size DECIMAL(3,1) NOT NULL,
    color VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    category VARCHAR(100),
    description TEXT,
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert exactly 20 fresh shoes
INSERT INTO shoes (name, brand, size, color, price, quantity, category, description) VALUES
('Air Max 270', 'Nike', 9.0, 'Black/White', 150.00, 15, 'Lifestyle', 'Comfortable lifestyle shoes with Max Air cushioning'),
('Ultraboost 22', 'Adidas', 10.0, 'Core Black', 180.00, 12, 'Running', 'High-performance running shoes with Boost technology'),
('Fresh Foam 1080v12', 'New Balance', 9.5, 'Grey/Blue', 150.00, 8, 'Running', 'Premium running shoes with Fresh Foam cushioning'),
('RS-X Reinvention', 'Puma', 10.5, 'White/Red', 120.00, 20, 'Lifestyle', 'Retro-inspired lifestyle sneakers'),
('Classic Leather', 'Reebok', 9.0, 'White/Navy', 85.00, 25, 'Lifestyle', 'Timeless classic leather sneakers'),
('Chuck Taylor All Star', 'Converse', 8.5, 'Black', 55.00, 30, 'Lifestyle', 'Iconic canvas sneakers'),
('Old Skool', 'Vans', 9.0, 'Black/White', 60.00, 18, 'Skateboarding', 'Classic skate shoes with durable construction'),
('Gel-Kayano 29', 'ASICS', 10.0, 'Blue/Silver', 160.00, 10, 'Running', 'Stability running shoes with Gel cushioning'),
('Air Force 1', 'Nike', 9.5, 'White', 100.00, 22, 'Lifestyle', 'Classic basketball-inspired lifestyle shoes'),
('NMD_R1', 'Adidas', 10.5, 'Core Black', 140.00, 14, 'Lifestyle', 'Modern lifestyle shoes with Boost cushioning'),
('990v6', 'New Balance', 9.0, 'Grey', 195.00, 6, 'Lifestyle', 'Premium made in USA lifestyle sneakers'),
('Cali Star', 'Puma', 8.5, 'Pink/White', 110.00, 16, 'Lifestyle', 'Women''s fashion sneakers with platform sole'),
('Club C 85', 'Reebok', 9.5, 'White/Green', 90.00, 12, 'Lifestyle', 'Vintage tennis-inspired sneakers'),
('Run Star Hike', 'Converse', 8.0, 'Black/White', 130.00, 9, 'Lifestyle', 'Platform sneakers with rugged outsole'),
('Sk8-Hi', 'Vans', 10.0, 'Checkerboard', 75.00, 15, 'Skateboarding', 'High-top skate shoes with iconic pattern'),
('Gel-Nimbus 25', 'ASICS', 10.5, 'Purple/Orange', 160.00, 11, 'Running', 'Neutral running shoes with maximum cushioning'),
('Pegasus 39', 'Nike', 9.5, 'Blue/Orange', 120.00, 17, 'Running', 'Versatile daily training running shoes'),
('Superstar', 'Adidas', 9.0, 'White/Black', 100.00, 20, 'Lifestyle', 'Iconic shell-toe sneakers'),
('574 Core', 'New Balance', 8.5, 'Navy/Red', 85.00, 24, 'Lifestyle', 'Classic heritage running shoes'),
('Basket Classic', 'Puma', 9.0, 'Black/Gold', 95.00, 13, 'Lifestyle', 'Retro basketball-inspired sneakers');

-- Verify the new data
SELECT COUNT(*) as total_shoes FROM shoes;
SELECT id, name, brand, price FROM shoes;

DROP TABLE purchase_order;
CREATE TABLE purchase_order (
    po_id INT AUTO_INCREMENT PRIMARY KEY,
    model VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(50) NOT NULL
);

