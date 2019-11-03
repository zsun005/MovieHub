DELIMITER $$
CREATE PROCEDURE insert_sales_transactions(email VARCHAR(50), movieId VARCHAR(10), quantity INT, saleDate DATE, token VARCHAR(50))
BEGIN
    INSERT INTO sales (email, movieId, quantity, saleDate) VALUE (email, movieId, quantity, saleDate);
    INSERT INTO transactions (sId, token) VALUE (LAST_INSERT_ID(), token);
end $$
DELIMITER ;