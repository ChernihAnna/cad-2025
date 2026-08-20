package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Primary;

@Component
@Primary
public class HTMLTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {

        StringBuilder html = new StringBuilder();

        html.append("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>Товары</title>
                    <style>
                        table {
                            border-collapse: collapse;
                            width: 600px;
                        }
                        th, td {
                            border: 1px solid black;
                            padding: 8px;
                            text-align: left;
                        }
                        th {
                            background-color: #eeeeee;
                        }
                    </style>
                </head>
                <body>
                    <h1>Товары</h1>
                    <table>
                        <tr>
                            <th>Название</th>
                            <th>Цена</th>
                            <th>Количество</th>
                        </tr>
                """);

        for (Product product : products) {
            html.append("<tr>")
                    .append("<td>").append(product.getName()).append("</td>")
                    .append("<td>").append(product.getPrice()).append("</td>")
                    .append("<td>").append(product.getQuantity()).append("</td>")
                    .append("</tr>");
        }

        html.append("""
                    </table>
                </body>
                </html>
                """);

        try (FileWriter writer = new FileWriter("products.html")) {
            writer.write(html.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}