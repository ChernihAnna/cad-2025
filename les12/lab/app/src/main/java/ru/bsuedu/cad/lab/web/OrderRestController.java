package ru.bsuedu.cad.lab.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderRestController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;

    public OrderRestController(
            OrderService orderService,
            CustomerRepository customerRepository
    ) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(
           @PathVariable("id") Integer id
    ) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest request
    ) {

        Customer customer = customerRepository
                .findById(request.customerId())
                .orElseThrow();

        Order order = new Order(
                customer,
                LocalDateTime.now(),
                request.totalPrice(),
                request.status(),
                request.shippingAddress()
        );

        Order savedOrder = orderService.createOrder(order);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(
            @PathVariable("id") Integer id,
            @RequestBody OrderRequest request
    ) {

        return orderService.getOrderById(id)
                .map(order -> {

                    Customer customer = customerRepository
                            .findById(request.customerId())
                            .orElseThrow();

                    order.setCustomer(customer);
                    order.setTotalPrice(request.totalPrice());
                    order.setStatus(request.status());
                    order.setShippingAddress(
                            request.shippingAddress()
                    );

                    Order updatedOrder =
                            orderService.updateOrder(order);

                    return ResponseEntity.ok(updatedOrder);
                })
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(
          @PathVariable("id") Integer id
    ) {

        if (orderService.getOrderById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        orderService.deleteOrder(id);

        return ResponseEntity.noContent().build();
    }

    public record OrderRequest(
            Integer customerId,
            BigDecimal totalPrice,
            String status,
            String shippingAddress
    ) {
    }
}