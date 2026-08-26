package ru.bsuedu.cad.lab.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository);
    }

    @Test
    void createOrder_shouldReturnSavedOrder() {
        Order order = new Order(
                null,
                LocalDateTime.of(2026, 8, 26, 14, 0),
                new BigDecimal("1500.00"),
                "NEW",
                "Москва, ул. Ленина, д. 10"
        );

        Order savedOrder = new Order(
                null,
                LocalDateTime.of(2026, 8, 26, 14, 0),
                new BigDecimal("1500.00"),
                "NEW",
                "Москва, ул. Ленина, д. 10"
        );
        savedOrder.setOrderId(1);

        when(orderRepository.save(order)).thenReturn(savedOrder);

        Order result = orderService.createOrder(order);

        assertSame(savedOrder, result);
        assertEquals(1, result.getOrderId());
        assertEquals(new BigDecimal("1500.00"), result.getTotalPrice());
        assertEquals("NEW", result.getStatus());

        verify(orderRepository).save(order);
    }

    @Test
    void createOrder_shouldPropagateRepositoryException() {
        Order order = new Order(
                null,
                LocalDateTime.of(2026, 8, 26, 14, 0),
                new BigDecimal("2000.00"),
                "NEW",
                "Москва, ул. Тверская, д. 10"
        );

        RuntimeException exception =
                new RuntimeException("Ошибка сохранения заказа");

        when(orderRepository.save(order)).thenThrow(exception);

        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(order)
        );

        assertSame(exception, result);

        verify(orderRepository).save(order);
    }
}