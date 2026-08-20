package ru.bsuedu.cad.lab;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.nio.file.Files;

@Component
public class ResourceFileReader implements Reader {

    @Value("${products.file}")
    private String fileName;

    @PostConstruct
    public void init() {
        System.out.println(
                "ResourceFileReader инициализирован: " +
                java.time.LocalDateTime.now()
        );
    }

    @Override
    public String read() {

        try {
            var resource = new ClassPathResource(fileName);

            return Files.readString(
                    resource.getFile().toPath()
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}