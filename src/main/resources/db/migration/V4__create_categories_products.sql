create table categories
(
    id   TINYINT AUTO_INCREMENT
        PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

create table products
(
    id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    category_id TINYINT,
    CONSTRAINT products_categories_id_fk
    FOREIGN KEY (category_id) REFERENCES categories (id)
    ON DELETE RESTRICT -- Prevent deletion of a category if there are associated products
);