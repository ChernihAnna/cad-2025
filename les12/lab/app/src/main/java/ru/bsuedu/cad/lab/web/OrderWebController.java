package ru.bsuedu.cad.lab.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.bsuedu.cad.lab.entity.Customer;
import ru.bsuedu.cad.lab.entity.Order;
import ru.bsuedu.cad.lab.repository.CustomerRepository;
import ru.bsuedu.cad.lab.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/orders")
public class OrderWebController {

    private final OrderService orderService;
    private final CustomerRepository customerRepository;

    public OrderWebController(
            OrderService orderService,
            CustomerRepository customerRepository
    ) {
        this.orderService = orderService;
        this.customerRepository = customerRepository;
    }

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute(
                "orders",
                orderService.getAllOrders()
        );

        return "orders";
    }

    @GetMapping("/new")
    public String newOrderForm(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute(
                "customers",
                customerRepository.findAll()
        );

        return "order-form";
    }

@PostMapping
public String createOrder(
        @RequestParam("customerId") Integer customerId,
        @RequestParam("totalPrice") BigDecimal totalPrice,
        @RequestParam("status") String status,
        @RequestParam("shippingAddress") String shippingAddress
) {
    System.out.println("=== WEB CREATE ORDER ===");

    Customer customer = customerRepository
            .findById(customerId)
            .orElseThrow();

    Order order = new Order(
            customer,
            LocalDateTime.now(),
            totalPrice,
            status,
            shippingAddress
    );

    orderService.createOrder(order);

    return "redirect:/orders";
}
    @GetMapping("/{id}/edit")
    public String editOrderForm(
            @PathVariable("id") Integer id,
            Model model
    ) {
        Order order = orderService
                .getOrderById(id)
                .orElseThrow();

        model.addAttribute("order", order);
        model.addAttribute(
                "customers",
                customerRepository.findAll()
        );

        return "order-form";
    }

    @PostMapping("/{id}")
public String updateOrder(
        @PathVariable("id") Integer id,
        @RequestParam("customerId") Integer customerId,
        @RequestParam("totalPrice") BigDecimal totalPrice,
        @RequestParam("status") String status,
        @RequestParam("shippingAddress") String shippingAddress
) {
        Order order = orderService
                .getOrderById(id)
                .orElseThrow();

        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow();

        order.setCustomer(customer);
        order.setTotalPrice(totalPrice);
        order.setStatus(status);
        order.setShippingAddress(shippingAddress);

        orderService.updateOrder(order);

        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String deleteOrder(
            @PathVariable("id") Integer id
    ) {
        orderService.deleteOrder(id);

        return "redirect:/orders";
    }
}