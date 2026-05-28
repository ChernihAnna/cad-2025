package ru.bsuedu.cad.lab;

import java.util.ArrayList;
import java.util.List;

public class CSVParser implements Parser {

    @Override
public List<Product> parse(String data) {

    List<Product> products = new ArrayList<>();

    String[] lines = data.split("\n");

    for (String line : lines) {

        line = line.trim();

        if (line.isEmpty()) {
            continue;
        }

        String[] parts = line.split(",");

        products.add(
                new Product(
                        parts[0],
                        Double.parseDouble(parts[1]),
                        Integer.parseInt(parts[2])
                )
        );
    }

    return products;
}
}