package ru.bsuedu.cad.lab;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;

public class ResourceFileReader implements Reader {

    @Override
    public String read() {

        try {

            var resource =
                    new ClassPathResource("products.csv");

            return Files.readString(
                    resource.getFile().toPath()
            );

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}