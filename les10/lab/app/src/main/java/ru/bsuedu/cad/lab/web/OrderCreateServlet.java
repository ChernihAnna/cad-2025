package ru.bsuedu.cad.lab.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
@WebServlet("/order/create")
public class OrderCreateServlet extends HttpServlet {

    private OrderService orderService;
    private CustomerRepository customerRepository;

   @Override
public void init() throws ServletException {

    WebApplicationContext context =
            WebApplicationContextUtils
                    .getRequiredWebApplicationContext(
                            getServletContext()
                    );

    orderService = context.getBean(OrderService.class);
    customerRepository = context.getBean(CustomerRepository.class);
}

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

       response.setContentType("text/html;charset=UTF-8");
response.setCharacterEncoding("UTF-8");

        List<Customer> customers = customerRepository.findAll();

        var out = response.getWriter();

        out.println("""
                <!DOCTYPE html>
                <html lang="ru">
                <head>
                    <meta charset="UTF-8">
                    <title>\u0421\u043E\u0437\u0434\u0430\u043D\u0438\u0435 \u0437\u0430\u043A\u0430\u0437\u0430</title>

                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            max-width: 700px;
                            margin: 40px auto;
                            padding: 20px;
                        }

                        h1 {
                            margin-bottom: 30px;
                        }

                        label {
                            display: block;
                            margin-top: 15px;
                            margin-bottom: 5px;
                        }

                        input, select {
                            width: 100%;
                            box-sizing: border-box;
                            padding: 10px;
                            border: 1px solid #ccc;
                            border-radius: 6px;
                        }

                        button {
                            margin-top: 25px;
                            padding: 11px 20px;
                            background: #2563eb;
                            color: white;
                            border: none;
                            border-radius: 6px;
                            cursor: pointer;
                        }

                        .back {
                            display: inline-block;
                            margin-top: 20px;
                            color: #2563eb;
                            text-decoration: none;
                        }
                    </style>
                </head>

                <body>

                    <h1>\u0421\u043E\u0437\u0434\u0430\u043D\u0438\u0435 \u0437\u0430\u043A\u0430\u0437\u0430</h1>

                    <form method="post" action="/app/order/create">

                        <label for="customerId">
                            \u041F\u043E\u043A\u0443\u043F\u0430\u0442\u0435\u043B\u044C
                        </label>

                        <select id="customerId" name="customerId" required>
                """);

        for (Customer customer : customers) {
            out.println("""
                    <option value="%d">%s</option>
                    """.formatted(
                    customer.getCustomerId(),
                    customer.getName()
            ));
        }

        out.println("""
                        </select>

                        <label for="totalPrice">
                         \u0421\u0443\u043C\u043C\u0430 \u0437\u0430\u043A\u0430\u0437\u0430
                        </label>

                        <input
                            id="totalPrice"
                            name="totalPrice"
                            type="number"
                            step="0.01"
                            min="0"
                            value="1500"
                            required
                        >

                        <label for="shippingAddress">
                           \u0410\u0434\u0440\u0435\u0441 \u0434\u043E\u0441\u0442\u0430\u0432\u043A\u0438
                        </label>

                        <input
                            id="shippingAddress"
                            name="shippingAddress"
                            type="text"
                            placeholder="\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0430\u0434\u0440\u0435\u0441"
                            required
                        >

                        <button type="submit">
                           \u0421\u043E\u0437\u0434\u0430\u0442\u044C \u0437\u0430\u043A\u0430\u0437
                        </button>

                    </form>

                    <a class="back" href="/app/orders">
                        \u2190 \u0412\u0435\u0440\u043D\u0443\u0442\u044C\u0441\u044F \u043A \u0437\u0430\u043A\u0430\u0437\u0430\u043C
                    </a>

                </body>
                </html>
                """);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        request.setCharacterEncoding("UTF-8");

        Integer customerId = Integer.parseInt(
                request.getParameter("customerId")
        );

        BigDecimal totalPrice = new BigDecimal(
                request.getParameter("totalPrice")
        );

        String shippingAddress =
                request.getParameter("shippingAddress");

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow();

        Order order = new Order(
                customer,
                LocalDateTime.now(),
                totalPrice,
                "NEW",
                shippingAddress
        );

        orderService.createOrder(order);

        response.sendRedirect(
                request.getContextPath() + "/orders"
        );
    }
}