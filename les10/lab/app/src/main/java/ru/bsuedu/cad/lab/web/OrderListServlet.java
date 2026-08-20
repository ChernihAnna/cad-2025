package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/orders")
public class OrderListServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {

        WebApplicationContext context =
                WebApplicationContextUtils
                        .getRequiredWebApplicationContext(
                                getServletContext()
                        );

        orderService = context.getBean(OrderService.class);
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<Order> orders = orderService.getAllOrders();

        PrintWriter out = response.getWriter();

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>\u0417\u0430\u043A\u0430\u0437\u044B</title>

                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            max-width: 1100px;
                            margin: 40px auto;
                            padding: 20px;
                            background: #f8f9fa;
                        }

                        h1 {
                            margin-bottom: 20px;
                        }

                        .button {
                            display: inline-block;
                            padding: 10px 18px;
                            margin-bottom: 25px;
                            background: #2563eb;
                            color: white;
                            text-decoration: none;
                            border-radius: 6px;
                        }

                        .button:hover {
                            background: #1d4ed8;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                            background: white;
                        }

                        th,
                        td {
                            border: 1px solid #ddd;
                            padding: 10px;
                            text-align: left;
                        }

                        th {
                            background: #f1f5f9;
                        }
                    </style>
                </head>

                <body>

                    <h1>\u0421\u043F\u0438\u0441\u043E\u043A \u0437\u0430\u043A\u0430\u0437\u043E\u0432</h1>

                    <a class="button" href="order/create">
                        \u0421\u043E\u0437\u0434\u0430\u0442\u044C \u0437\u0430\u043A\u0430\u0437
                    </a>

                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>\u041F\u043E\u043A\u0443\u043F\u0430\u0442\u0435\u043B\u044C</th>
                                <th>\u0414\u0430\u0442\u0430</th>
                                <th>\u0421\u0442\u0430\u0442\u0443\u0441</th>
                                <th>\u0421\u0443\u043C\u043C\u0430</th>
                                <th>\u0410\u0434\u0440\u0435\u0441 \u0434\u043E\u0441\u0442\u0430\u0432\u043A\u0438</th>
                            </tr>
                        </thead>

                        <tbody>
                """);

        for (Order order : orders) {

            String customerName = "";

            if (order.getCustomer() != null) {
                customerName = order.getCustomer().getName();
            }

            String orderDate = "";

            if (order.getOrderDate() != null) {
                orderDate = order.getOrderDate().format(formatter);
            }

            out.println("""
                            <tr>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                                <td>%s</td>
                            </tr>
                    """.formatted(
                    order.getOrderId(),
                    customerName,
                    orderDate,
                    order.getStatus(),
                    order.getTotalPrice(),
                    order.getShippingAddress()
            ));
        }

        out.println("""
                        </tbody>
                    </table>

                </body>
                </html>
                """);
    }
}