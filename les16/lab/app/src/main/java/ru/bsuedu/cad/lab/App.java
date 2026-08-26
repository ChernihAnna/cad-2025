package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {

        try (var context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            System.out.println("Приложение JPA успешно запущено.");
        }
    }
}