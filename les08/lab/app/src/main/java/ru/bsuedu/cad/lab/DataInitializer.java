package ru.bsuedu.cad.lab;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.bsuedu.cad.lab.entity.Category;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Product;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.entity.OrderDetail;
import ru.bsuedu.cad.lab.repository.CategoryRepository;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.repository.ProductRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer
        implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger log =
            LoggerFactory.getLogger(DataInitializer.class);

    private final CategoryRepository categoryRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderService orderService;

    public DataInitializer(
            CategoryRepository categoryRepository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            OrderService orderService
    ) {
        this.categoryRepository = categoryRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        try {
            loadCategories();
            loadCustomers();
            loadProducts();

            log.info("Данные из CSV загружены.");
            log.info("Категорий: {}", categoryRepository.count());
            log.info("Покупателей: {}", customerRepository.count());
            log.info("Товаров: {}", productRepository.count());

            createTestOrder();

        } catch (Exception e) {
            log.error("Ошибка при инициализации данных", e);
            throw new RuntimeException(e);
        }
    }

    private void loadCategories() throws Exception {

        List<String> lines = Files.readAllLines(
                Path.of("src/main/resources/category.csv"),
                StandardCharsets.UTF_8
        );

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 3);

            Category category = new Category(
                    parts[1],
                    parts[2]
            );

            category.setCategoryId(
                    Integer.parseInt(parts[0])
            );

            categoryRepository.save(category);
        }
    }

    private void loadCustomers() throws Exception {

        List<String> lines = Files.readAllLines(
                Path.of("src/main/resources/customer.csv"),
                StandardCharsets.UTF_8
        );

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 5);

            Customer customer = new Customer(
                    parts[1],
                    parts[2],
                    parts[3],
                    parts[4]
            );

            customer.setCustomerId(
                    Integer.parseInt(parts[0])
            );

            customerRepository.save(customer);
        }
    }

    private void loadProducts() throws Exception {

        List<String> lines = Files.readAllLines(
                Path.of("src/main/resources/products.csv"),
                StandardCharsets.UTF_8
        );

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 9);

            Category category = categoryRepository
                    .findById(Integer.parseInt(parts[3]))
                    .orElseThrow();

            Product product = new Product(
                    parts[1],
                    parts[2],
                    category,
                    Double.parseDouble(parts[4]),
                    Integer.parseInt(parts[5]),
                    parts[6],
                    LocalDate.parse(parts[7]).atStartOfDay(),
                    LocalDate.parse(parts[8]).atStartOfDay()
            );

            product.setProductId(
                    Integer.parseInt(parts[0])
            );

            productRepository.save(product);
        }
    }

    private void createTestOrder() {

        Customer customer = customerRepository
                .findById(1)
                .orElseThrow();

        Product product = productRepository
                .findById(1)
                .orElseThrow();

        Order order = new Order(
                customer,
                LocalDateTime.now(),
                BigDecimal.valueOf(1500),
                "NEW",
                customer.getAddress()
        );

        OrderDetail detail = new OrderDetail(
                order,
                product,
                1,
                BigDecimal.valueOf(1500)
        );

        order.addOrderDetail(detail);

        Order savedOrder = orderService.createOrder(order);

        log.info(
                "Заказ успешно создан. ID = {}",
                savedOrder.getOrderId()
        );

        log.info(
                "Количество заказов в БД: {}",
                orderService.getAllOrders().size()
        );
    }
}