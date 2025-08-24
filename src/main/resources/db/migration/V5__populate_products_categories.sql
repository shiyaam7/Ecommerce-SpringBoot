INSERT INTO categories (name) VALUES
('Beverages'),
('Snacks'),
('Dairy'),
('Bakery'),
('Fruits & Vegetables'),
('Meat & Seafood'),
('Household Supplies'),
('Personal Care');

INSERT INTO products (name, price, description, category_id) VALUES
-- Beverages
('Coca Cola 1L', 65.00, 'Refreshing carbonated soft drink', 1),
('Tata Tea Premium 500g', 210.00, 'Rich blend of Assam & Darjeeling tea', 1),
('Nescafe Classic 100g', 320.00, 'Instant coffee powder', 1),

-- Snacks
('Lays Classic Salted 52g', 20.00, 'Crispy potato chips', 2),
('Parle-G Biscuits 200g', 40.00, 'Classic glucose biscuits', 2),
('Kurkure Masala Munch 90g', 30.00, 'Spicy crunchy snack', 2),

-- Dairy
('Amul Butter 500g', 275.00, 'Rich and creamy salted butter', 3),
('Mother Dairy Milk 1L', 60.00, 'Fresh toned milk', 3),
('Britannia Cheese Slices 200g', 120.00, 'Processed cheese slices', 3),

-- Bakery
('Britannia Bread 400g', 45.00, 'Soft and fresh white bread', 4),
('Chocolate Muffin', 55.00, 'Freshly baked chocolate muffin', 4),

-- Fruits & Vegetables
('Banana (1 dozen)', 60.00, 'Fresh ripe bananas', 5),
('Tomato 1kg', 40.00, 'Farm fresh tomatoes', 5),
('Onion 1kg', 55.00, 'Fresh red onions', 5),

-- Meat & Seafood
('Chicken Breast 1kg', 280.00, 'Fresh skinless chicken breast', 6),
('Rohu Fish 1kg', 320.00, 'Freshwater fish', 6),

-- Household Supplies
('Surf Excel 1kg', 220.00, 'Detergent powder for laundry', 7),
('Harpic Toilet Cleaner 500ml', 95.00, 'Powerful toilet cleaning liquid', 7),

-- Personal Care
('Colgate Toothpaste 200g', 110.00, 'Strong teeth and fresh breath', 8),
('Dove Shampoo 340ml', 290.00, 'Moisturizing shampoo for smooth hair', 8);
