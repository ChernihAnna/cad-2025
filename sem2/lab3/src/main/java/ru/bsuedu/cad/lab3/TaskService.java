package ru.bsuedu.cad.lab3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Добавление новой задачи
    public void addTask(Task task) {
        taskRepository.addTask(task);
    }

    // Получение всех задач
    public List<Task> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    // Удаление задачи по ID
    public void deleteTask(Long id) {
        taskRepository.deleteTask(id);
    }
}