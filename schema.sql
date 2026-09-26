-- =============================================
-- Expense Tracker - Database Schema
-- Run this file in MySQL Workbench once
-- =============================================

-- Create the database
CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

-- -----------------------------------------------
-- Table: categories
-- Stores the fixed expense categories
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS categories (
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- -----------------------------------------------
-- Table: expenses
-- Stores all expense entries
-- category_id is a FK referencing categories
-- -----------------------------------------------
CREATE TABLE IF NOT EXISTS expenses (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    title        VARCHAR(255)   NOT NULL,
    amount       DECIMAL(10, 2) NOT NULL,
    expense_date DATE           NOT NULL,
    description  VARCHAR(255),
    category_id  INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- -----------------------------------------------
-- Seed default categories
-- -----------------------------------------------
INSERT IGNORE INTO categories (name) VALUES
    ('Food'),
    ('Travel'),
    ('Shopping'),
    ('Bills'),
    ('Entertainment'),
    ('Health'),
    ('Other');
