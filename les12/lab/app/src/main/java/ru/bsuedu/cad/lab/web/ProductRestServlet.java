package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.repository.ProductRepository;

import java.io.IOException;
import java.util.List;

@WebServlet("/products")
public class ProductRestServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {

        WebApplicationContext context =
                WebApplicationContextUtils
                        .getRequiredWebApplicationContext(
                                getServletContext()
                        );

        productRepository =
                context.getBean(ProductRepository.class);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        List<Product> products =
                productRepository.findAll();

        StringBuilder json = new StringBuilder();

        json.append("[\n");

        for (int i = 0; i < products.size(); i++) {

            Product product = products.get(i);

            json.append("  {\n");

            json.append("    \"name\": \"")
                    .append(escapeJson(product.getName()))
                    .append("\",\n");

            json.append("    \"category\": \"")
                    .append(escapeJson(
                            product.getCategory().getName()
                    ))
                    .append("\",\n");

            json.append("    \"stock\": ")
                    .append(product.getStockQuantity())
                    .append("\n");

            json.append("  }");

            if (i < products.size() - 1) {
                json.append(",");
            }

            json.append("\n");
        }

        json.append("]\n");

        response.getWriter().write(json.toString());
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}