package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubTaskService {

    private final SubTaskRepository subTaskRepository;

    public SubTaskService(SubTaskRepository subTaskRepository) {
        this.subTaskRepository = subTaskRepository;
    }

    public List<SubTask> getAllSubTasks() {
        return subTaskRepository.findAll();
    }

    public SubTask getSubTask(Long id) {
        return subTaskRepository.findById(id).orElse(null);
    }

    public SubTask addSubTask(SubTask subTask) {
        return subTaskRepository.save(subTask);
    }

    public SubTask updateSubTask(SubTask subTask) {
        return subTaskRepository.save(subTask);
    }

    public void deleteSubTask(Long id) {
        subTaskRepository.deleteById(id);
    }
}