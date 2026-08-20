package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ConsoleTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {

        System.out.println("---------------------------------------------");
        System.out.printf(
                "%-30s %-10s %-10s%n",
                "Название",
                "Цена",
                "Количество"
        );
        System.out.println("---------------------------------------------");

        for (Product product : products) {

            System.out.printf(
                    "%-30s %-10.2f %-10d%n",
                    product.getName(),
                    product.getPrice(),
                    product.getStockQuantity()
            );
        }

        System.out.println("---------------------------------------------");
    }
}