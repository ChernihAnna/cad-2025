package ru.bsuedu.cad.lab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {

    public static void main(String[] args) {

        try (var context =
                     new AnnotationConfigApplicationContext(
                             AppConfig.class
                     )) {

            ProductProvider provider =
                    context.getBean(ProductProvider.class);

            Renderer renderer =
                    context.getBean(Renderer.class);

            renderer.render(
                    provider.getProducts()
            );

            CategoryRequest categoryRequest =
                    context.getBean(CategoryRequest.class);

            categoryRequest.execute();
        }
    }
}