# Отчет о лабораторной работе

## Цель работы

Изучение Gradle и Spring Framework.

## Выполнение работы

- установлен JDK 17;
- установлен Gradle 8.12;
- создан Java Application проект;
- подключен Spring Context;
- реализован интерфейс `Reader`;
- реализован `CSVParser`;
- реализован `ProductProvider`;
- реализован `ConsoleTableRenderer`;
- реализована загрузка CSV-файла;
- реализован вывод таблицы товаров;
- реализован `HTMLTableRenderer`;
- добавлена конфигурация Spring с использованием `@Configuration` и `@ComponentScan`;
- добавлен `ResourceFileReader` с использованием `@Value`;
- добавлен `@PostConstruct` для выполнения метода после создания компонента;
- добавлен аспект для измерения времени парсинга CSV-файла.

## Выводы

В ходе лабораторной работы было создано Java-приложение с использованием
Gradle и Spring Framework.

Была реализована загрузка данных о товарах из CSV-файла, их парсинг
и вывод в виде таблицы. Также была реализована HTML-версия таблицы товаров.

Приложение успешно запускается с помощью команды:

```text
gradle run
```

и корректно обрабатывает данные из CSV-файла.

## UML-диаграмма классов

```mermaid
classDiagram

    class App {
        +main(String[] args)
    }

    class AppConfig {
    }

    class Product {
        -String name
        -double price
        -int quantity
    }

    class ProductProvider {
        <<interface>>
        +getProducts() List~Product~
    }

    class ConcreteProductProvider {
        +getProducts() List~Product~
    }

    class Reader {
        <<interface>>
        +read() String
    }

    class ResourceFileReader {
        -String fileName
        +read() String
        +init()
    }

    class Parser {
        <<interface>>
        +parse(String data) List~Product~
    }

    class CSVParser {
        +parse(String data) List~Product~
    }

    class Renderer {
        <<interface>>
        +render(List~Product~ products)
    }

    class ConsoleTableRenderer {
        +render(List~Product~ products)
    }

    class HTMLTableRenderer {
        +render(List~Product~ products)
    }

    class CSVParserPerformanceAspect {
        +measureParsingTime(ProceedingJoinPoint)
    }

    App --> AppConfig
    App --> ProductProvider
    App --> Renderer

    ProductProvider <|.. ConcreteProductProvider
    Reader <|.. ResourceFileReader
    Parser <|.. CSVParser
    Renderer <|.. ConsoleTableRenderer
    Renderer <|.. HTMLTableRenderer

    ConcreteProductProvider --> Reader
    ConcreteProductProvider --> Parser

    CSVParserPerformanceAspect ..> CSVParser
```