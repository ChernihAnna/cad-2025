package ru.bsuedu.cad.lab5.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskManagerForm extends JFrame {

    private final String encodedAuth;

    private final DefaultListModel<Task> taskListModel;
    private final JList<Task> taskList;

    // Список задач, полученных с сервера
    private List<Task> allTasks = new ArrayList<>();

    // Выбор сортировки
    private JComboBox<String> sortComboBox;

    public TaskManagerForm(String encodedAuth) {

        this.encodedAuth = encodedAuth;

        setTitle("Task Manager");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);

        taskList.setFont(new Font("Arial", Font.PLAIN, 14));

        taskList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        // Отображение задачи
        taskList.setCellRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(
                    JList<?> list,
                    Object value,
                    int index,
                    boolean isSelected,
                    boolean cellHasFocus) {

                JLabel label = (JLabel) super
                        .getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                Task task = (Task) value;

                String status = task.isCompleted()
                        ? "Выполнена"
                        : "Не выполнена";

                label.setText(
                        "<html>" +
                                "<b>" + safe(task.getTitle()) + "</b><br>" +
                                "Описание: " + safe(task.getDescription()) + "<br>" +
                                "Категория: " + safe(task.getCategory()) + "<br>" +
                                "Приоритет: " + safe(task.getPriority()) + "<br>" +
                                "Статус: " + status +
                                "</html>"
                );

                label.setBorder(
                        BorderFactory.createEmptyBorder(
                                8, 8, 8, 8
                        )
                );

                return label;
            }
        });

        // Кнопки
        JButton refreshButton =
                new JButton("Обновить задачи");

        JButton addButton =
                new JButton("Добавить задачу");

        JButton deleteButton =
                new JButton("Удалить задачу");

        // Выпадающий список сортировки
        sortComboBox = new JComboBox<>(
                new String[]{
                        "Без сортировки",
                        "По названию",
                        "По категории",
                        "По приоритету",
                        "По статусу"
                }
        );

        // Обновление
        refreshButton.addActionListener(
                e -> loadTasks()
        );

        // Добавление
        addButton.addActionListener(
                e -> addTask()
        );

        // Удаление
        deleteButton.addActionListener(
                e -> deleteSelectedTask()
        );

        // Сортировка
        sortComboBox.addActionListener(
                e -> sortTasks()
        );

        // Верхняя панель
        JPanel topPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        topPanel.add(
                new JLabel("Сортировка:")
        );

        topPanel.add(sortComboBox);

        // Нижняя панель
        JPanel buttonPanel = new JPanel();

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        // Основной layout
        setLayout(new BorderLayout(10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Загрузка задач
        loadTasks();
    }

    private String safe(String value) {

        return value == null || value.isBlank()
                ? "не указано"
                : value;
    }

    /**
     * Получение задач с сервера
     */
    private void loadTasks() {

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "http://localhost:8080/api"
                                    )
                            )
                            .header(
                                    "Authorization",
                                    "Basic " + encodedAuth
                            )
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString(
                                    StandardCharsets.UTF_8
                            )
                    );

            if (response.statusCode() != 200) {

                JOptionPane.showMessageDialog(
                        this,
                        "Сервер вернул ошибку: "
                                + response.statusCode(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            ObjectMapper mapper =
                    new ObjectMapper();

            allTasks = mapper.readValue(
                    response.body(),
                    new TypeReference<List<Task>>() {}
            );

            // Отображаем задачи
            updateTaskList();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Не удалось получить задачи:\n"
                            + ex.getMessage(),
                    "Ошибка",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Обновление списка на экране
     */
    private void updateTaskList() {

        taskListModel.clear();

        for (Task task : allTasks) {
            taskListModel.addElement(task);
        }
    }

    /**
     * Сортировка задач
     */
    private void sortTasks() {

        String selected =
                (String) sortComboBox.getSelectedItem();

        if (selected == null) {
            return;
        }

        switch (selected) {

            case "Без сортировки":

                // Загружаем список заново
                loadTasks();
                return;

            case "По названию":

                allTasks.sort(
                        Comparator.comparing(
                                task -> safe(task.getTitle()),
                                String.CASE_INSENSITIVE_ORDER
                        )
                );

                break;

            case "По категории":

                allTasks.sort(
                        Comparator.comparing(
                                task -> safe(task.getCategory()),
                                String.CASE_INSENSITIVE_ORDER
                        )
                );

                break;

           case "По приоритету":

    allTasks.sort(
            Comparator.comparingInt(
                    (Task task) -> priorityValue(task)
            ).reversed()
    );

    break;

            case "По статусу":

                allTasks.sort(
                        Comparator.comparing(
                                Task::isCompleted
                        )
                );

                break;
        }

        updateTaskList();
    }

    /**
     * Числовое значение приоритета
     */
    private int priorityValue(Task task) {

        String priority =
                safe(task.getPriority());

        switch (priority.toLowerCase()) {

            case "высокий":
                return 3;

            case "средний":
                return 2;

            case "низкий":
                return 1;

            default:
                return 0;
        }
    }

    /**
     * Добавление задачи
     */
    private void addTask() {

        JTextField titleField =
                new JTextField();

        JTextField descriptionField =
                new JTextField();

        JTextField categoryField =
                new JTextField();

        JComboBox<String> priorityBox =
                new JComboBox<>(
                        new String[]{
                                "Высокий",
                                "Средний",
                                "Низкий"
                        }
                );

        JPanel panel =
                new JPanel(
                        new GridLayout(0, 1)
                );

        panel.add(new JLabel("Название:"));
        panel.add(titleField);

        panel.add(new JLabel("Описание:"));
        panel.add(descriptionField);

        panel.add(new JLabel("Категория:"));
        panel.add(categoryField);

        panel.add(new JLabel("Приоритет:"));
        panel.add(priorityBox);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Добавить задачу",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String title =
                titleField.getText().trim();

        String description =
                descriptionField.getText().trim();

        String category =
                categoryField.getText().trim();

        String priority =
                (String) priorityBox.getSelectedItem();

        if (title.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Название задачи не может быть пустым.",
                    "Ошибка",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            Task task = new Task();

            task.setTitle(title);
            task.setDescription(description);
            task.setCategory(category);
            task.setPriority(priority);
            task.setCompleted(false);

            ObjectMapper mapper =
                    new ObjectMapper();

            String json =
                    mapper.writeValueAsString(task);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "http://localhost:8080/api"
                                    )
                            )
                            .header(
                                    "Authorization",
                                    "Basic " + encodedAuth
                            )
                            .header(
                                    "Content-Type",
                                    "application/json"
                            )
                            .POST(
                                    HttpRequest.BodyPublishers
                                            .ofString(
                                                    json,
                                                    StandardCharsets.UTF_8
                                            )
                            )
                            .build();

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString(
                                    StandardCharsets.UTF_8
                            )
                    );

            if (response.statusCode() == 200) {

                JOptionPane.showMessageDialog(
                        this,
                        "Задача добавлена.",
                        "Готово",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadTasks();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Ошибка добавления. Код: "
                                + response.statusCode(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Не удалось добавить задачу:\n"
                            + ex.getMessage(),
                    "Ошибка соединения",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Удаление задачи
     */
    private void deleteSelectedTask() {

        Task selectedTask =
                taskList.getSelectedValue();

        if (selectedTask == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Сначала выберите задачу.",
                    "Удаление",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        "Удалить задачу \"" +
                                selectedTask.getTitle() +
                                "\"?",
                        "Подтверждение удаления",
                        JOptionPane.YES_NO_OPTION
                );

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            HttpClient client =
                    HttpClient.newHttpClient();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(
                                    URI.create(
                                            "http://localhost:8080/api/"
                                                    + selectedTask.getId()
                                    )
                            )
                            .header(
                                    "Authorization",
                                    "Basic " + encodedAuth
                            )
                            .DELETE()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            if (response.statusCode() == 204) {

                JOptionPane.showMessageDialog(
                        this,
                        "Задача удалена.",
                        "Готово",
                        JOptionPane.INFORMATION_MESSAGE
                );

                loadTasks();

            } else if (response.statusCode() == 403) {

                JOptionPane.showMessageDialog(
                        this,
                        "У вас нет прав на удаление задач.\n"
                                + "Удалять задачи может только MODERATOR.",
                        "Доступ запрещён",
                        JOptionPane.ERROR_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Ошибка удаления. Код: "
                                + response.statusCode(),
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Не удалось удалить задачу:\n"
                            + ex.getMessage(),
                    "Ошибка соединения",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}