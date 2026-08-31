# Лабораторная работа 1. Разработка REST API на Spring Boot

## Цель работы

Разработать простое REST API приложение на Spring Boot, реализующее работу с сообщениями. Изучить создание REST-контроллера, обработку HTTP-запросов и реализацию операций GET, POST, PUT и DELETE.

Дополнительно реализовать методы получения количества сообщений и удаления всех сообщений.

---

## 1. Используемые технологии

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Maven
- Apache Tomcat 11
- JUnit
- MockMvc

---

## 2. Структура проекта

```text
lab1/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ru/bsuedu/cad/lab/
│   │   │       ├── Lab1Application.java
│   │   │       └── MessageController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/
│           └── ru/bsuedu/cad/lab/
│               └── Lab1ApplicationTests.java
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

---

## 3. Запуск приложения

Для запуска приложения используется Maven Wrapper:

```powershell
.\mvnw.cmd spring-boot:run
```

После успешного запуска приложение доступно по адресу:

```text
http://localhost:8080
```

В консоли появляется:

```text
Tomcat started on port 8080 (http) with context path '/'
Started Lab1Application
```

---

## 4. Главная страница

Для проверки главной страницы используется GET-запрос:

```http
GET /
```

Команда PowerShell:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/"
```

Результат:

```text
Hello, World!
```

---

## 5. REST API сообщений

Для работы с сообщениями реализован REST-контроллер `MessageController`.

Сообщения хранятся в списке:

```java
private final List<String> userMessages = new ArrayList<>();
```

---

## 6. Получение всех сообщений

Для получения списка сообщений используется:

```http
GET /messages
```

Команда:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/messages"
```

Изначально список сообщений пуст.

Проверка количества элементов:

```powershell
(Invoke-RestMethod -Uri "http://localhost:8080/messages").Count
```

Результат:

```text
0
```

---

## 7. Добавление сообщения

Для добавления сообщения используется:

```http
POST /messages
```

Команда:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/messages" `
  -Method Post `
  -Body '"Первое сообщение"' `
  -ContentType "application/json"
```

Результат:

```text
Message published successfully!
```

После выполнения GET-запроса получено:

```text
"Первое сообщение"
```

---

## 8. Изменение сообщения

Для изменения сообщения используется:

```http
PUT /messages/{index}
```

Например, изменение сообщения с индексом `0`:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/messages/0" `
  -Method Put `
  -Body '"Измененное первое сообщение"' `
  -ContentType "application/json"
```

Результат:

```text
Message updated successfully!
```

---

## 9. Удаление сообщения

Для удаления отдельного сообщения используется:

```http
DELETE /messages/{index}
```

Команда:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/messages/0" `
  -Method Delete
```

Результат:

```text
Message deleted successfully!
```

---

## 10. Обработка ошибочного запроса

Также реализована проверка существования сообщения по индексу.

При попытке удалить сообщение с несуществующим индексом:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/messages/100" `
  -Method Delete
```

получен результат:

```text
Message not found at index 100
```

Таким образом, контроллер корректно обрабатывает ошибочный запрос.

---

## 11. Дополнительный метод: количество сообщений

Для выполнения дополнительного задания реализован метод:

```http
GET /messages/count
```

Он возвращает количество сообщений в списке.

Проверка:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/messages/count"
```

После очистки списка результат:

```text
0
```

После добавления одного сообщения:

```text
1
```

---

## 12. Дополнительный метод: удаление всех сообщений

Для удаления всех сообщений реализован метод:

```http
DELETE /messages
```

Команда:

```powershell
Invoke-RestMethod `
  -Uri "http://localhost:8080/messages" `
  -Method Delete
```

Результат:

```text
All messages deleted successfully!
```

После выполнения команды:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/messages/count"
```

получен результат:

```text
0
```

---

## 13. Итоговая таблица API

| HTTP-метод | URL | Назначение |
|---|---|---|
| GET | `/` | Проверка работы приложения |
| GET | `/messages` | Получение всех сообщений |
| POST | `/messages` | Добавление сообщения |
| PUT | `/messages/{index}` | Изменение сообщения |
| DELETE | `/messages/{index}` | Удаление сообщения |
| GET | `/messages/count` | Получение количества сообщений |
| DELETE | `/messages` | Удаление всех сообщений |

---

## 14. Результаты тестирования

В ходе проверки приложения были протестированы следующие сценарии:

- GET `/` — успешно;
- GET `/messages` — успешно;
- POST `/messages` — успешно;
- PUT `/messages/0` — успешно;
- DELETE `/messages/0` — успешно;
- DELETE `/messages/100` — корректно обработан ошибочный индекс;
- GET `/messages/count` — успешно;
- DELETE `/messages` — успешно.

Пример успешного запуска:

```text
Tomcat started on port 8080 (http) with context path '/'
Started Lab1Application
```

---

## 15. Вывод

В ходе лабораторной работы было разработано REST API приложение на Spring Boot.

Были изучены и реализованы основные HTTP-методы:

- GET;
- POST;
- PUT;
- DELETE.

Реализована работа с коллекцией сообщений, добавление, изменение и удаление элементов.

Дополнительно реализованы:

- получение количества сообщений;
- удаление всех сообщений;
- обработка обращения к несуществующему индексу.

Все реализованные HTTP-операции были проверены с помощью PowerShell и успешно выполнены.

Приложение запускается на порту `8080`, REST API работает корректно.
