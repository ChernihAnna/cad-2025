package ru.bsuedu.cad.lab;

public class Product {

    private long id;
    private String name;
    private String description;
    private long categoryId;
    private double price;
    private int stockQuantity;
    private String imageUrl;
    private String createdAt;
    private String updatedAt;

    public Product(
            long id,
            String name,
            String description,
            long categoryId,
            double price,
            int stockQuantity,
            String imageUrl,
            String createdAt,
            String updatedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}