package ru.bsuedu.cad.lab;

import java.util.List;

public class ConsoleTableRenderer implements Renderer {

    @Override
    public void render(List<Product> products) {

        System.out.println("--------------------------------");

        for (Product product : products) {

            System.out.printf(
                    "%-15s %-10.2f %-5d%n",
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity()
            );
        }

        System.out.println("--------------------------------");
    }
}