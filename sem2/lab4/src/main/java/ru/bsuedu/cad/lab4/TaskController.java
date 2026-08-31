package ru.bsuedu.cad.lab4;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;
    private final TaskHistoryService taskHistoryService;

    public TaskController(
            TaskService taskService,
            UserService userService,
            TaskHistoryService taskHistoryService) {

        this.taskService = taskService;
        this.userService = userService;
        this.taskHistoryService = taskHistoryService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.getAllTasks());
        model.addAttribute("users", userService.getAllUsers());
        return "index";
    }

    @PostMapping("/addTask")
    public String addTask(
            @ModelAttribute Task task,
            @RequestParam Long userId) {

        User user = userService.getUser(userId);

        if (user != null) {
            task.setUser(user);
        }

        taskService.addTask(task);

        return "redirect:/";
    }

    @GetMapping("/deleteTask/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/";
    }

    @PostMapping("/updateTask/{id}")
    public String updateTask(
            @PathVariable Long id,
            @RequestParam boolean completed) {

        Task task = taskService.getTask(id);

        if (task != null) {

            String oldState = task.isCompleted()
                    ? "Выполнена"
                    : "Не выполнена";

            String newState = completed
                    ? "Выполнена"
                    : "Не выполнена";

            task.setCompleted(completed);
            taskService.updateTask(task);

            TaskHistory history = new TaskHistory();
            history.setTask(task);
            history.setOldState(oldState);
            history.setNewState(newState);

            taskHistoryService.addTaskHistory(history);
        }

        return "redirect:/";
    }
}