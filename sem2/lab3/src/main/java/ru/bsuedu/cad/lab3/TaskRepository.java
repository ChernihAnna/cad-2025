package ru.bsuedu.cad.lab3;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TaskRepository {

    private List<Task> taskList = new ArrayList<>();

    // Добавление задачи
    public void addTask(Task task) {
        taskList.add(task);
    }

    // Получение всех задач
    public List<Task> getAllTasks() {
        return taskList;
    }

    // Удаление задачи по ID
    public void deleteTask(Long id) {
        taskList.removeIf(task -> task.getId().equals(id));
    }
}