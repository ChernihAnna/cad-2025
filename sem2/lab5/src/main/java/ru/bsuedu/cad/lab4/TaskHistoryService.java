package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskHistoryService {

    private final TaskHistoryRepository taskHistoryRepository;

    public TaskHistoryService(TaskHistoryRepository taskHistoryRepository) {
        this.taskHistoryRepository = taskHistoryRepository;
    }

    public List<TaskHistory> getAllTaskHistory() {
        return taskHistoryRepository.findAll();
    }

    public TaskHistory addTaskHistory(TaskHistory taskHistory) {
        return taskHistoryRepository.save(taskHistory);
    }
}