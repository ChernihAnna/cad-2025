package ru.bsuedu.cad.lab.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.bsuedu.cad.lab.AppConfig;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void createOrder_shouldSaveOrderToDatabase() {
        Customer customer = new Customer();
        customer.setCustomerId(1);

        Order order = new Order(
                customer,
                LocalDateTime.now(),
                new BigDecimal("2500.00"),
                "NEW",
                "Москва, ул. Ленина, д. 10"
        );

        Order savedOrder = orderService.createOrder(order);

        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getOrderId());

        Order foundOrder =
                orderRepository.findById(savedOrder.getOrderId()).orElseThrow();

        assertEquals(new BigDecimal("2500.00"), foundOrder.getTotalPrice());
        assertEquals("NEW", foundOrder.getStatus());
        assertEquals(
                "Москва, ул. Ленина, д. 10",
                foundOrder.getShippingAddress()
        );
    }

    @Test
    void createOrder_shouldFailWhenCustomerIsMissing() {
        Order order = new Order(
                null,
                LocalDateTime.now(),
                new BigDecimal("1000.00"),
                "NEW",
                "Москва"
        );

        assertThrows(
                Exception.class,
                () -> orderService.createOrder(order)
        );
    }
}
