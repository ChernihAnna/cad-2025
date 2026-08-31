# Лабораторная работа №4 --- Spring Boot + PostgreSQL

## Назначение

Учебное Spring Boot-приложение для управления списком задач с хранением
данных в PostgreSQL.

Реализовано: - задачи: создание, просмотр, выполнение и удаление; - дата
создания, приоритет и категория задачи; - подзадачи; - пользователи и
связь пользователей с задачами; - авторизация по email и паролю; -
история входов пользователей с IP-адресом; - история изменений состояния
задач; - веб-интерфейс Thymeleaf.

## Технологии

-   Java
-   Spring Boot
-   Spring MVC
-   Spring Data JPA
-   Hibernate
-   Spring Security
-   Thymeleaf
-   PostgreSQL
-   Maven

## Структура проекта

Основной пакет:

``` text
src/main/java/ru/bsuedu/cad/lab4/
```

Основные классы:

``` text
Lab4Application.java
SecurityConfig.java

Task.java
TaskController.java
TaskRepository.java
TaskService.java

SubTask.java
SubTaskController.java
SubTaskRepository.java
SubTaskService.java

User.java
UserController.java
UserRepository.java
UserService.java

LoginHistory.java
LoginHistoryController.java
LoginHistoryRepository.java
LoginHistoryService.java

TaskHistory.java
TaskHistoryController.java
TaskHistoryRepository.java
TaskHistoryService.java
```

Шаблоны:

``` text
src/main/resources/templates/
├── index.html
├── users.html
├── login-history.html
└── task-history.html
```

Настройки:

``` text
src/main/resources/application.properties
```

## PostgreSQL

Пример `application.properties`:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/lab4
spring.datasource.username=postgres
spring.datasource.password=ВАШ_ПАРОЛЬ
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

Создание базы:

``` sql
CREATE DATABASE lab4;
```

Таблицы вручную создавать не обязательно: Hibernate создаёт и обновляет
их по JPA-сущностям.

## Основные таблицы

### tasks

``` text
id
title
description
completed
created_at
priority
category
user_id
```

### sub_task

Хранит подзадачи и связь с родительской задачей.

### users

``` text
id
name
email
password
```

### login_history

``` text
id
login_date
user_id
ip_address
```

### task_history

``` text
id
change_date
old_state
new_state
task_id
```

## Связи сущностей

Один пользователь может иметь несколько задач:

``` java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

Одна задача может иметь несколько подзадач:

``` java
@OneToMany(mappedBy = "task",
        cascade = CascadeType.ALL,
        orphanRemoval = true)
private List<SubTask> subTasks = new ArrayList<>();
```

История изменений привязана к задаче:

``` java
@ManyToOne
@JoinColumn(name = "task_id")
private Task task;
```

История входов привязана к пользователю:

``` java
@ManyToOne
@JoinColumn(name = "user_id")
private User user;
```

## Репозитории

Репозитории используют Spring Data JPA:

``` java
public interface TaskRepository extends JpaRepository<Task, Long> {
}
```

Аналогично работают `UserRepository`, `SubTaskRepository`,
`LoginHistoryRepository` и `TaskHistoryRepository`.

Доступны стандартные операции `save()`, `findAll()`, `findById()` и
`deleteById()`.

## Авторизация

Страница входа:

``` text
http://localhost:8080/login
```

Используются email и пароль.

Для тестового пользователя учебного проекта пароль можно хранить с
префиксом:

``` text
{noop}1234
```

Например:

``` sql
UPDATE users
SET password = '{noop}1234'
WHERE email = 'anya@mail.ru';
```

После этого пароль для входа: `1234`.

`{noop}` используется только для учебного тестирования и означает
отсутствие шифрования пароля.

## История входов

Страница:

``` text
http://localhost:8080/login-history
```

Отображает ID, пользователя, email, дату входа и IP-адрес.

Проверка в PostgreSQL:

``` sql
SELECT * FROM login_history;
```

Каждый успешный вход должен создавать новую запись.

## История изменений задач

Страница:

``` text
http://localhost:8080/task-history
```

Отображает ID, задачу, дату изменения, старое состояние и новое
состояние.

Пример:

``` text
1 | Изучить PostgreSQL | 31.08.2026 21:16 | Не выполнена | Выполнена
2 | Изучить PostgreSQL | 31.08.2026 21:17 | Выполнена | Не выполнена
```

Проверка:

``` sql
SELECT * FROM task_history;
```

## Проверка базы данных

``` sql
SELECT * FROM users;
SELECT * FROM tasks;
SELECT * FROM sub_task;
SELECT * FROM login_history;
SELECT * FROM task_history;
```

## Проверка приложения

Главная:

``` text
http://localhost:8080/
```

Пользователи:

``` text
http://localhost:8080/users
```

Авторизация:

``` text
http://localhost:8080/login
```

История входов:

``` text
http://localhost:8080/login-history
```

История задач:

``` text
http://localhost:8080/task-history
```

### Проверка задач

1.  Добавить задачу.
2.  Заполнить название и описание.
3.  Выбрать приоритет.
4.  Указать категорию.
5.  Выбрать пользователя.
6.  Нажать «Добавить».
7.  Нажать «Выполнить».
8.  Проверить изменение статуса.
9.  Открыть `/task-history`.
10. Проверить новую запись истории.
11. При необходимости удалить задачу.

### Проверка пользователей

1.  Открыть `/users`.
2.  Добавить пользователя.
3.  Убедиться, что он появился в списке.
4.  Проверить запись через PostgreSQL.

### Проверка авторизации

1.  Открыть `/login`.
2.  Ввести email и пароль.
3.  После успешного входа открыть `/login-history`.
4.  Проверить появление новой записи с датой и IP.

## Запуск

Запустить главный класс:

``` text
Lab4Application.java
```

Или из корня проекта:

### Windows

``` cmd
mvnw.cmd spring-boot:run
```

### Linux/macOS

``` bash
./mvnw spring-boot:run
```

После сообщения `Tomcat started on port 8080` открыть:

``` text
http://localhost:8080
```

## Если появляется ошибка PasswordEncoder

Ошибка:

``` text
Given that there is no default password encoder configured,
each password must have a password encoding prefix
```

Для учебного пользователя:

``` sql
UPDATE users
SET password = '{noop}1234'
WHERE email = 'anya@mail.ru';
```

После этого выполнить вход с паролем `1234`.

## Что проверено

В проекте проверены запуск Spring Boot на порту 8080, подключение к
PostgreSQL, создание/обновление таблиц Hibernate, добавление
пользователей и задач, привязка задачи к пользователю, изменение
состояния задачи, запись истории изменений, авторизация, запись истории
входов, отображение обеих историй и наличие данных в PostgreSQL.

## Итог

Создано Spring Boot-приложение, взаимодействующее с PostgreSQL через
Spring Data JPA. Выполнено дополнительное задание лабораторной работы
№4: расширена таблица `tasks`, добавлены `SubTask`, `User`,
`LoginHistory` и `TaskHistory`, реализованы связи сущностей, авторизация
и сохранение данных в PostgreSQL.
