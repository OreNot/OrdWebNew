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
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@PreAuthorize("hasAuthority('GROUPBOSS')")
public class GroupBossController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    WorkGroupRepo workGroupRepo;

    @Autowired
    FioRepo fioRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UrgencyRepo urgencyRepo;

    @Autowired
    GroupManagerRepo groupManagerRepo;

    @Autowired
    StatusRepo statusRepo;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Value("${manupload.path}")
    private String manUpPath;

    SimpleDateFormat addDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

    @GetMapping("/myrgtasks")
    public String showmyrgtasks(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        GroupManager groupManager = groupManagerRepo.findByUser(user);
        WorkGroup workGroup = groupManagerRepo.findByUser(user).getWorkGroup();

        Iterable<Task> myrgtasks = taskRepo.findByworkGroupName(workGroup.getName());
        Iterable<Urgency> urgencys;
        Iterable<Status> statuses;


        urgencys = urgencyRepo.findAll();
        statuses = statusRepo.findAll();


        model.put("statuses", statuses);
        model.put("urgencys", urgencys);


        model.put("urlprefixPath", urlprefixPath);
        model.put("myrgtasks", myrgtasks);

        return "myrgtasks";
    }

    @PostMapping("/myrgtasks")
    public String filtermyrgtasks(
            @RequestParam(required = false, defaultValue = "0") String radiofilter,
            @RequestParam(required = false, defaultValue = "0") String status,
            @RequestParam(required = false, defaultValue = "0") String urgency,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        GroupManager groupManager = groupManagerRepo.findByUser(user);
        WorkGroup workGroup = groupManagerRepo.findByUser(user).getWorkGroup();

        Iterable<Task> myrgtasks = null;
        switch (radiofilter)
        {
            case "statusfilter":

                myrgtasks = !status.equals("Статус") && !status.equals("Все") ? taskRepo.findByStatusNameAndWorkGroupName(status, workGroup.getName()) : taskRepo.findByworkGroupName(workGroup.getName());

                break;

            case "urgencyfilter":
                myrgtasks = !urgency.equals("Важность") && !urgency.equals("Все") ? taskRepo.findByUrgencyNameAndWorkGroupName(urgency, workGroup.getName()) : taskRepo.findByworkGroupName(workGroup.getName());

                break;

        }
        //Iterable<Task> myrgtasks = taskRepo.findByworkGroupName(workGroup.getName());

        Iterable<Urgency> urgencys;
           Iterable<Status> statuses;

                statuses = statusRepo.findAll();
        urgencys = urgencyRepo.findAll();


        model.put("statuses", statuses);
        model.put("urgencys", urgencys);



        model.put("urlprefixPath", urlprefixPath);
        model.put("myrgtasks", myrgtasks);

        return "myrgtasks";
    }


    @GetMapping("/showonetaskforgroupboss")
    public String showonetaskforgroupboss(
            @RequestParam(required = false, defaultValue = "0") String execdate,
            @RequestParam(required = false, defaultValue = "0") String workgroup,
            @RequestParam(required = false, defaultValue = "0") String description,
            @RequestParam(required = false, defaultValue = "0") String urgency,
            @RequestParam(required = false, defaultValue = "0") String tid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {

        Optional<Task> taskOp = taskRepo.findById(Integer.parseInt(tid));
        Task task = taskOp.get();
        String taskFilePath;
        String taskFileName;
        try {
            taskFilePath = task.getTaskFileName();
            taskFileName = task.getTaskFileName().substring(task.getTaskFileName().lastIndexOf("\\") + 1, task.getTaskFileName().length());
        }
        catch (NullPointerException e)
        {
            taskFilePath = null;
            taskFileName = null;
        }
        WorkGroup workGroup = workGroupRepo.findByName(workgroup);
        Urgency editableurgency = urgencyRepo.findByName(urgency);


        List<GroupManager> executorGMlist = groupManagerRepo.findByWorkGroupId(workGroup.getId());

        List<User> executorlist = userRepo.findAll();



        for (GroupManager gm : executorGMlist)
        {
            if (!executorlist.contains(gm.getUser()))
            {
                executorlist.remove(gm.getUser());
            }
        }

        String selectedexecutor;
        try{
            selectedexecutor = task.getExecutor().getUsername();
        }
        catch (NullPointerException e)
        {
            selectedexecutor = null;
        }

        model.put("executorlist", executorlist);
        model.put("chronos", task.getChronos());
        model.put("taskfilepath", taskFilePath);
        model.put("taskFileName", taskFileName);
        model.put("selectedurgency", editableurgency.getName());
        model.put("selectedexecutor", selectedexecutor);
        model.put("selecteddescription", description);
        model.put("selectedexecdate", execdate);
        model.put("selectedworkgroup", workGroup.getName());
        model.put("tid", tid);

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();

        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);

        model.put("urlprefixPath", urlprefixPath);
        return "showonetaskforgroupboss";
    }


    @PostMapping("/showonetaskforgroupboss")
    public String editonetaskforgroupboss(
            @RequestParam(required = false, defaultValue = "0") String editableexecdate,
           // @RequestParam(required = false, defaultValue = "0") String editableworkgroup,
            @RequestParam(required = false, defaultValue = "0") String executor,
            @RequestParam(required = false, defaultValue = "0") String resend,
            @RequestParam(required = false, defaultValue = "0") String comment,
            @RequestParam(required = false, defaultValue = "0") String tid,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {



        String taskFilePath;
        String taskFileName;
        String oldFileName;




        Optional<Task> taskOp = taskRepo.findById(Integer.parseInt(tid));

        Task editableTask = taskOp.get();

        String selectedexecutor = null;

        WorkGroup workGroup = editableTask.getWorkGroup();

        String selectedworkgroup = workGroup.getName();

        String selectedexecdate = editableexecdate;


        List<GroupManager> executorGMlist = groupManagerRepo.findByWorkGroupId(workGroup.getId());

        List<User> executorlist = userRepo.findAll();
        try
        {
            taskFilePath = editableTask.getTaskFileName();
            taskFileName = editableTask.getTaskFileName().substring(editableTask.getTaskFileName().lastIndexOf("\\") + 1, editableTask.getTaskFileName().length());
            oldFileName = editableTask.getTaskFileName().substring(editableTask.getTaskFileName().lastIndexOf("\\") + 1, editableTask.getTaskFileName().length());

        }
        catch (NullPointerException e)
        {
            taskFilePath = null;
            taskFileName = null;
            oldFileName = null;
        }
        StringBuilder chronos = new StringBuilder(editableTask.getChronos());

        for (GroupManager gm : executorGMlist)
        {
            if (!executorlist.contains(gm.getUser()))
            {
                executorlist.remove(gm.getUser());
            }
        }
        StringBuilder editList = new StringBuilder(addDateFormat.format(new Date()) + " Задача изменена пользователем " + user.getUsername() + ". \nСписок изменений: \n");

        if(resend.equals("on"))
        {
            editableTask.setWorkGroup(workGroupRepo.findByName("Не назначена"));
            chronos.append(addDateFormat.format(new Date()) + " Задача возвращена на перераспределение " + user.getUsername() + " с комментарием: "+ comment + "\n");

            editableTask.setChronos(chronos.toString());
try {

           editableTask.setComment(editableTask.getComment().replaceAll("null", "") + comment + "\n");
}catch (NullPointerException e)
{
    editableTask.setComment(comment + "\n");
}
        }
        else
        {
            if (!editableTask.getExecDate().equals(editableexecdate.trim()))
            {
                String oldExecDate = editableTask.getExecDate();
                editableTask.setExecDate(editableexecdate.trim());
                selectedexecdate = editableexecdate.trim();
                editList.append("Дата исполнения изменена с " + oldExecDate + " на " + editableexecdate.trim() + ";\n");
            }


            if (file != null && !file.getOriginalFilename().equals("") && !file.getOriginalFilename().equals(oldFileName))
            {
                File dir;
                if (taskFileName == null) {
                    dir = new File(manUpPath + "\\" + fileNameDateFormat.format(new Date()));
                }
                else {
                    dir = new File(taskFilePath.substring(0, taskFilePath.lastIndexOf("\\")));
                    try {
                        for (File f : dir.listFiles()) {
                            try {
                                Files.deleteIfExists(f.toPath());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }

                if (!dir.exists()) {
                    dir.mkdir();
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
            if(!(executor.equals("0") || executor.equals("Не назначен")))
            {
                editableTask.setExecutor(userRepo.findByUsername(executor));
                editList.append("Задаче назначен исполнитель " + executor + " \n");
                editableTask.setStatus(statusRepo.findByName("Назначен исполнитель"));
                selectedexecutor = userRepo.findByUsername(executor).getUsername();
            }
            else
            {
                selectedexecutor = "Не назначен";
            }

            String newChronos = editableTask.getChronos() + "\n" + editList.toString();
            editableTask.setChronos(newChronos);

            taskRepo.save(editableTask);

        }

        taskRepo.save(editableTask);



        model.put("tid", tid);
        model.put("error", "Задача изменена");
        model.put("executorlist", executorlist);
        model.put("selectedurgency", editableTask.getUrgency().getName());
        model.put("selecteddescription", editableTask.getDescription());
        model.put("selectedexecdate", editableTask.getExecDate());
        model.put("selectedworkgroup", workGroup.getName());
        model.put("selectedexecutor", selectedexecutor);
        model.put("chronos", editableTask.getChronos());
        model.put("taskfilepath", taskFilePath);
        model.put("taskFileName", taskFileName);

        Iterable<Urgency> urgencys;
        Iterable<WorkGroup> workgroups;

        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();




        model.put("urgencys", urgencys);
        model.put("workgroups", workgroups);

        model.put("urlprefixPath", urlprefixPath);

        return "showonetaskforgroupboss";

    }


}