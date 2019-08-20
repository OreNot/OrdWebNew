package ucproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ucproject.domain.*;
import ucproject.repos.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@PreAuthorize("hasAuthority('MANAGER')")
public class ManagerController {

    @Autowired
    UrgencyRepo urgencyRepo;

    @Autowired
    WorkGroupRepo workGroupRepo;

    @Autowired
    StatusRepo statusRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    FioRepo fioRepo;

    @Autowired
    UserRepo userRepo;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Value("${manupload.path}")
    private String manUpPath;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat dateFormatForDateField = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat addDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

    @GetMapping("/addtask")
    public String addtask(Map<String, Object> model) {
        //     model.put("error", "");
        model.put("urlprefixPath", urlprefixPath);

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);
        return "addtask";
    }

    @PostMapping("/addtask")
    public String addnewtask(
            @RequestParam(required = false, defaultValue = "0") String urgency,
            @RequestParam(required = false, defaultValue = "0") String description,
            @RequestParam(required = false, defaultValue = "0") String execdate,
            @RequestParam(required = false, defaultValue = "0") String workgroup,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        String userName = user.getUsername();
        Fio fio = user.getFio();
        File destFile = null;

        if (!urgency.equals("0") && !description.equals("0") && !execdate.equals("0") && !workgroup.equals("0")) {

            if(file != null && !file.getOriginalFilename().equals(""))
            {
                if (!Files.exists(Paths.get(manUpPath))) {
                    try {
                        Files.createDirectory(Paths.get(manUpPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                File uploadDir = new File(manUpPath + "\\" + fileNameDateFormat.format(new Date()));

                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }

                destFile = new File(uploadDir + "\\" + file.getOriginalFilename());

                try {
                    file.transferTo(destFile);
                } catch (IOException e) {
                    e.printStackTrace();

                }



            }

            String edate = "";
            try {
                edate = dateFormat.format(new SimpleDateFormat("yyyy-MM-dd").parse(execdate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Urgency newUrgency = urgencyRepo.findByName(urgency);
            WorkGroup newWorkGroup = workGroupRepo.findByName(workgroup);
            Status newStatus = statusRepo.findByName("Зарегистрировано");



            String chronos = addDateFormat.format(new Date()) + " Задача зарегистрирована " + userName;



            Task newTask = new Task(newStatus, description, newUrgency, edate, dateFormat.format(new Date()), newWorkGroup, user, chronos);

            if(file != null && !file.getOriginalFilename().equals("")) {
                newTask.setTaskFileName(destFile.getPath());
            }
            taskRepo.save(newTask);







            model.put("error", "Задача зарегистрирована");
        }
        else {
            model.put("error", "*Необходимо заполнить все поля");
        }


        //     model.put("error", "");
        model.put("urlprefixPath", urlprefixPath);

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);

        return "addtask";
    }

    @GetMapping("/showtask")
    public String showtask(
        Map<String, Object> model,
        @RequestParam(required = false, defaultValue = "0") String workgroup,
        @AuthenticationPrincipal User user
        )
    {
        Iterable<Task> tasks = taskRepo.findAll();

        model.put("urlprefixPath", urlprefixPath);
        model.put("tasks", tasks);

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);
        return "showtask";
    }

    @GetMapping("/showonetaskformanager")
    public String showonetaskformanager(
            Map<String, Object> model,
            @RequestParam(required = false, defaultValue = "0") String execdate,
            @RequestParam(required = false, defaultValue = "0") String workgroup,
            @RequestParam(required = false, defaultValue = "0") String description,
            @RequestParam(required = false, defaultValue = "0") String urgency,
            @RequestParam(required = false, defaultValue = "0") String tid,
            @AuthenticationPrincipal User user
    )
    {

        WorkGroup workGroup = workGroupRepo.findByName(workgroup);
        Urgency editableurgency = urgencyRepo.findByName(urgency);

        Optional<Task> taskOp = taskRepo.findById(Integer.parseInt(tid));
        Task task = taskOp.get();
        String taskFilePath = task.getTaskFileName();
        String taskFileName = task.getTaskFileName().substring(task.getTaskFileName().lastIndexOf("\\") + 1, task.getTaskFileName().length());


        model.put("chronos", task.getChronos());
        model.put("taskfilepath", taskFilePath);
        model.put("taskFileName", taskFileName);
        model.put("selectedurgency", editableurgency.getName());
        model.put("selecteddescription", description);
        model.put("selectedexecdate", execdate);
        model.put("selectedworkgroup", workGroup.getName());
        model.put("tid", tid);


        model.put("urlprefixPath", urlprefixPath);

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("urgencys", urgencys);

        model.put("workgroups", workgroups);
        return "showonetaskformanager";
    }

    @PostMapping("/showonetaskformanager")
    public String showeditonetaskformanager(
            Map<String, Object> model,
            @RequestParam(required = false, defaultValue = "0") String editableexecdate,
            @RequestParam(required = false, defaultValue = "0") String editableworkgroup,
            @RequestParam(required = false, defaultValue = "0") String editabledescription,
            @RequestParam(required = false, defaultValue = "0") String editableurgency,
            @RequestParam(required = false, defaultValue = "0") String editabletid,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user
    )
    {

        int taskId = Integer.parseInt(editabletid);
        Optional<Task> editableTaskOp = taskRepo.findById(taskId);
        Task editableTask = editableTaskOp.get();
        WorkGroup editworkGroup = workGroupRepo.findByName(editableworkgroup);
        Urgency editurgency = urgencyRepo.findByName(editableurgency);

        String taskFilePath = editableTask.getTaskFileName();
        String taskFileName = editableTask.getTaskFileName().substring(editableTask.getTaskFileName().lastIndexOf("\\") + 1, editableTask.getTaskFileName().length());

        String selectedurgency = editableurgency;
        String selectedworkgroup = editableworkgroup;
        String selecteddescription = editabledescription;
        String selectedexecdate = editableexecdate;


        String oldFileName = editableTask.getTaskFileName().substring(editableTask.getTaskFileName().lastIndexOf("\\") + 1, editableTask.getTaskFileName().length());

        StringBuilder editList = new StringBuilder(addDateFormat.format(new Date()) + " Задача изменена пользователем " + user.getUsername() + ". \nСписок изменений: \n");

        if (!editableTask.getExecDate().equals(editableexecdate.trim()))
        {
            String oldExecDate = editableTask.getExecDate();
            editableTask.setExecDate(editableexecdate.trim());
            selectedexecdate = editableexecdate.trim();
            editList.append("Дата исполнения изменена с " + oldExecDate + " на " + editableexecdate.trim() + ";\n");
        }
        if (!editableTask.getWorkGroup().getName().equals(editableworkgroup))
        {
            String oldWorkGroup = editableTask.getWorkGroup().getName();
            editableTask.setWorkGroup(workGroupRepo.findByName(editableworkgroup));
            selectedworkgroup = workGroupRepo.findByName(editableworkgroup).getName();
            editList.append("РГ изменена с " + oldWorkGroup + " на " + selectedworkgroup + ";\n");
        }
        if (!editableTask.getDescription().equals(editabledescription))
        {
            editableTask.setDescription(editabledescription);
            selecteddescription = editabledescription.trim();
            editList.append("Описание задачи изменено; \n");
        }
        if (!editableTask.getUrgency().getName().equals(editableurgency))
        {
            String oldUrgency = editableTask.getUrgency().getName();
            editableTask.setUrgency(urgencyRepo.findByName(editableurgency));
            selectedurgency = urgencyRepo.findByName(editableurgency).getName();
            editList.append("Важность изменена с " + oldUrgency + " на " + selectedurgency + ";\n");

        }
        if (file != null && !file.getOriginalFilename().equals("") && !file.getOriginalFilename().equals(oldFileName))
        {
            File dir = new File(taskFilePath.substring(0, taskFilePath.lastIndexOf("\\")));

            for (File f : dir.listFiles())
            {
                try {
                    Files.deleteIfExists(f.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File destFile = new File(dir.getPath() + "\\" + file.getOriginalFilename());

            try {
                file.transferTo(destFile);
            } catch (IOException e) {
                e.printStackTrace();

            }

            editableTask.setTaskFileName(destFile.getPath());
            taskFilePath = destFile.getPath();
            taskFileName = destFile.getName();

            editList.append("Вложение заменено с " + oldFileName + " на " + destFile.getName() + " \n");

        }

        String newChronos = editableTask.getChronos() + "\n" + editList.toString();
        editableTask.setChronos(newChronos);

        taskRepo.save(editableTask);

        model.put("chronos", editableTask.getChronos());

        model.put("taskfilepath", taskFilePath);
        model.put("taskFileName", taskFileName);

        model.put("selectedurgency", selectedurgency);

        model.put("selecteddescription", selecteddescription);
        model.put("selectedexecdate", selectedexecdate);
        model.put("selectedworkgroup", selectedworkgroup);
        model.put("tid", editabletid);



        model.put("urlprefixPath", urlprefixPath);
        model.put("error", "Задача изменена");

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("urgencys", urgencys);

        model.put("workgroups", workgroups);
        return "showonetaskformanager";
    }


}
