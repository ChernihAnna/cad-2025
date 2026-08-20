package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Primary
public class DataBaseRenderer implements Renderer {

    private final JdbcTemplate jdbcTemplate;
    private final CategoryProvider categoryProvider;

    public DataBaseRenderer(
            JdbcTemplate jdbcTemplate,
            CategoryProvider categoryProvider
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.categoryProvider = categoryProvider;
    }

    @Override
    public void render(List<Product> products) {

        System.out.println(">>> DataBaseRenderer ЗАПУЩЕН");

        List<Category> categories = categoryProvider.getCategories();

        for (Category category : categories) {

            jdbcTemplate.update(
                    """
                    INSERT INTO CATEGORIES (ID, NAME, DESCRIPTION)
                    VALUES (?, ?, ?)
                    """,
                    category.getId(),
                    category.getName(),
                    category.getDescription()
            );
        }

        for (Product product : products) {

            jdbcTemplate.update(
                    """
                    INSERT INTO PRODUCTS
                    (ID, NAME, DESCRIPTION, CATEGORY_ID, PRICE,
                     STOCK_QUANTITY, IMAGE_URL, CREATED_AT, UPDATED_AT)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """,
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getCategoryId(),
                    product.getPrice(),
                    product.getStockQuantity(),
                    product.getImageUrl(),
                    product.getCreatedAt(),
                    product.getUpdatedAt()
            );
        }

        System.out.println("Данные успешно сохранены в базу данных.");
    }
}