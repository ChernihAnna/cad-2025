package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SubTaskController {

    private final SubTaskService subTaskService;
    private final TaskService taskService;

    public SubTaskController(
            SubTaskService subTaskService,
            TaskService taskService) {
        this.subTaskService = subTaskService;
        this.taskService = taskService;
    }

    @PostMapping("/addSubTask/{taskId}")
    public String addSubTask(
            @PathVariable Long taskId,
            @RequestParam String description) {

        Task task = taskService.getTask(taskId);

        if (task != null) {
            SubTask subTask = new SubTask();
            subTask.setDescription(description);
            subTask.setTask(task);
            subTaskService.addSubTask(subTask);
        }

        return "redirect:/";
    }

    @PostMapping("/updateSubTask/{id}")
    public String updateSubTask(
            @PathVariable Long id,
            @RequestParam boolean completed) {

        SubTask subTask = subTaskService.getSubTask(id);

        if (subTask != null) {
            subTask.setCompleted(completed);
            subTaskService.updateSubTask(subTask);
        }

        return "redirect:/";
    }

    @GetMapping("/deleteSubTask/{id}")
    public String deleteSubTask(@PathVariable Long id) {
        subTaskService.deleteSubTask(id);
        return "redirect:/";
    }
}