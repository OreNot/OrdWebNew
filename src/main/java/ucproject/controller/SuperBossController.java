package ucproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ucproject.domain.*;
import ucproject.repos.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SuperBossController {

    @Autowired
    UrgencyRepo urgencyRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    StatusRepo statusRepo;

    @Autowired
    WorkGroupRepo workGroupRepo;

    @GetMapping("/superbossdashboard")
    public String showsuperbossdash(Map<String, Object> model) {

        List<Task> tasks = taskRepo.findAll();
        List<User> executors = userRepo.findAll();
        List<WorkGroup> wprkGroups = workGroupRepo.findAll();
        Map<String, String> countbyexecutors = new HashMap<>();
        Map<String, String> countbyworkgroups = new HashMap<>();
        Map<String, Map<String, String>> statusByGroupsMap = new HashMap<>();
        List<StatusByWorkGroup> statusByGroups = new ArrayList<>();
        List<UrgencyByWorkGroup> urgencyByGroups = new ArrayList<>();

        for (WorkGroup workGroup : workGroupRepo.findAll())
        {
            StatusByWorkGroup statusByWorkGroup = new StatusByWorkGroup();
            UrgencyByWorkGroup urgencyByWorkGroup = new UrgencyByWorkGroup();

            statusByWorkGroup.setWorkGroupName(workGroup.getName());
            urgencyByWorkGroup.setWorkGroupName(workGroup.getName());

            urgencyByWorkGroup.setMostUrgency(String.valueOf(taskRepo.countByWorkGroupAndUrgencyAndStatusNot(workGroup, urgencyRepo.findByName("Очень важно"),statusRepo.findByName("Выполнено"))));
            urgencyByWorkGroup.setUrgency(String.valueOf(taskRepo.countByWorkGroupAndUrgencyAndStatusNot(workGroup, urgencyRepo.findByName("Важно"),statusRepo.findByName("Выполнено"))));
            urgencyByWorkGroup.setStandart(String.valueOf(taskRepo.countByWorkGroupAndUrgencyAndStatusNot(workGroup, urgencyRepo.findByName("Стандартно"),statusRepo.findByName("Выполнено"))));
            urgencyByWorkGroup.setMinUrgency(String.valueOf(taskRepo.countByWorkGroupAndUrgencyAndStatusNot(workGroup, urgencyRepo.findByName("Менее важно"),statusRepo.findByName("Выполнено"))));

            statusByWorkGroup.setRegStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("Зарегистрировано"))));
            statusByWorkGroup.setSetRGStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("Назначена РГ"))));
            statusByWorkGroup.setSetExecStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("Назначен исполнитель"))));
            statusByWorkGroup.setReturnStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("Возвращено на распределение"))));
            statusByWorkGroup.setInRGWorkStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("В работе РГ"))));
            statusByWorkGroup.setInExecWorkStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("В работе у исполнителя"))));
            statusByWorkGroup.setCompliteStatus(String.valueOf(taskRepo.countByWorkGroupAndStatus(workGroup, statusRepo.findByName("Выполнено"))));

            urgencyByGroups.add(urgencyByWorkGroup);
            statusByGroups.add(statusByWorkGroup);
        }

        for (User executor : executors)
        {
            countbyexecutors.put(executor.getFio(), String.valueOf(taskRepo.countByExecutorAndStatusNot(executor,statusRepo.findByName("Выполнено"))));
        }

        for (WorkGroup workGroup : wprkGroups)
        {
            countbyworkgroups.put(workGroup.getName(), String.valueOf(taskRepo.countByWorkGroupAndStatusNot(workGroup, statusRepo.findByName("Выполнено"))));
        }

        int alltaskcount = tasks.size();
        int executorscount = executors.size();

        Iterable<Urgency> urgencys;
        Iterable<Status> statuses;
        Iterable<WorkGroup> workgroups;

        statuses = statusRepo.findAll();
        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("statuses", statuses);
        model.put("statusByGroups", statusByGroups);
        model.put("urgencyByGroups", urgencyByGroups);
        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);
        model.put("countbyexecutors", countbyexecutors);
        model.put("countbyworkgroups", countbyworkgroups);
        model.put("executors", executors);
        model.put("alltaskcount", alltaskcount);
        model.put("executorscount", executorscount);
        return "/superbossdashboard";
    }

    @PostMapping("/superbossdashboard")
    public String editsuperbossdash(
            @RequestParam(required = false, defaultValue = "0") String radiofilter,
            @RequestParam(required = false, defaultValue = "0") String statusname,
            @RequestParam(required = false, defaultValue = "0") String urgencyname,
            @RequestParam(required = false, defaultValue = "0") String workgroupname,
            @RequestParam(required = false, defaultValue = "0") String executorname,
            Map<String, Object> model
    ) {


        List<Task> tasks = taskRepo.findAll();
        List<User> executors = userRepo.findAll();
        List<WorkGroup> wprkGroups = workGroupRepo.findAll();
        Map<String, String> countbyexecutors = new HashMap<>();
        Map<String, String> countbyworkgroups = new HashMap<>();

        for (User executor : executors) {
            countbyexecutors.put(executor.getFio(), String.valueOf(taskRepo.countByExecutorAndStatusNot(executor, statusRepo.findByName("Выполнено"))));
        }

        for (WorkGroup workGroup : wprkGroups) {
            countbyworkgroups.put(workGroup.getName(), String.valueOf(taskRepo.countByWorkGroupAndStatusNot(workGroup, statusRepo.findByName("Выполнено"))));
        }

        int alltaskcount = tasks.size();
        int executorscount = executors.size();

        Iterable<Urgency> urgencys;
        Iterable<Status> statuses;
        Iterable<WorkGroup> workgroups;

        statuses = statusRepo.findAll();
        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("statuses", statuses);
        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);
        model.put("countbyexecutors", countbyexecutors);
        model.put("countbyworkgroups", countbyworkgroups);
        model.put("executors", executors);
        model.put("alltaskcount", alltaskcount);
        model.put("executorscount", executorscount);
        return "/superbossdashboard";
    }

    @GetMapping("/taskfounder")
    public String showtaskfounder(
            Map<String, Object> model)
    {

        Iterable<Task> tasks = taskRepo.findAll();

        model.put("tasks", tasks);

        return "/taskfounder";
    }

    @PostMapping("/taskfounder")
    public String findtasks(
            @RequestParam(required = false, defaultValue = "0") String findfilter,
            Map<String, Object> model) {

        String findCriteria = findfilter.trim().toLowerCase();
        Iterable<Task> allTasks = taskRepo.findAll();
        List<Task> tasks = new ArrayList<>();

        if (findfilter.equals("0") || findCriteria.trim().equals(""))
        {
            tasks = taskRepo.findAll();
        }
        else {
            for (Task task : allTasks) {
                if (task.getDescription().toLowerCase().contains(findCriteria) ||
                        task.getWorkGroup().getName().toLowerCase().contains(findCriteria) ||
                        task.getStatus().getName().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", "")) ||
                        task.getUrgency().getName().toLowerCase().contains(findCriteria.replaceAll(" ", "")) ||
                        task.getExecDate().contains(findCriteria) ||
                        (task.getTaskFileName() != null && task.getTaskFileName().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", ""))) ||
                        task.getChronos().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", "")) ||
                        (task.getComment() != null && task.getComment().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", ""))) ||
                        (task.getExecutor() != null && task.getExecutor().getFio().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", ""))) ||
                        task.getAutor().getFio().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", "")) ||
                        task.getRegDate().contains(findCriteria) ||
                        (task.getReport() != null && task.getReport().replaceAll(" ", "").toLowerCase().contains(findCriteria.replaceAll(" ", "")))
                        )
                    tasks.add(task);
            }
        }
        model.put("tasks", tasks);

        return "/taskfounder";
    }
}
