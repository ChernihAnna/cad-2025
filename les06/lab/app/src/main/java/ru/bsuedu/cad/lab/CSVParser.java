package ru.bsuedu.cad.lab;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CSVParser implements Parser {

    @Override
    public List<Product> parse(String data) {

        List<Product> products = new ArrayList<>();

        String[] lines = data.split("\\r?\\n");

        for (int i = 1; i < lines.length; i++) {

            String line = lines[i].trim();

            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split(",", 9);

            products.add(
                    new Product(
                            Long.parseLong(parts[0]),
                            parts[1],
                            parts[2],
                            Long.parseLong(parts[3]),
                            Double.parseDouble(parts[4]),
                            Integer.parseInt(parts[5]),
                            parts[6],
                            parts[7],
                            parts[8]
                    )
            );
        }

        return products;
    }
}