package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryRequest {

    private static final Logger logger =
            LoggerFactory.getLogger(CategoryRequest.class);

    private final JdbcTemplate jdbcTemplate;

    public CategoryRequest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void execute() {

        String sql = """
                SELECT c.NAME, COUNT(p.ID) AS PRODUCT_COUNT
                FROM CATEGORIES c
                JOIN PRODUCTS p ON p.CATEGORY_ID = c.ID
                GROUP BY c.ID, c.NAME
                HAVING COUNT(p.ID) > 1
                """;

        List<String> categories = jdbcTemplate.query(
                sql,
                (rs, rowNum) ->
                        rs.getString("NAME")
                                + " - товаров: "
                                + rs.getInt("PRODUCT_COUNT")
        );

        for (String category : categories) {
            logger.info(category);
        }
    }
}