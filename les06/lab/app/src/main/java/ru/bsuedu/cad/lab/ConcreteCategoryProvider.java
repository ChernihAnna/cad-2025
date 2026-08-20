package ru.bsuedu.cad.lab;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConcreteCategoryProvider implements CategoryProvider {
@Override
public List<Category> getCategories() {

        try {
            var resource = new ClassPathResource("category.csv");

            String data = Files.readString(
                    resource.getFile().toPath()
            );

            List<Category> categories = new ArrayList<>();

            String[] lines = data.split("\n");

            for (int i = 1; i < lines.length; i++) {

                String line = lines[i].trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", 3);

                categories.add(
                        new Category(
                                Long.parseLong(parts[0]),
                                parts[1],
                                parts[2]
                        )
                );
            }

            return categories;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}