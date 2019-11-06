package ucproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ucproject.domain.*;
import ucproject.repos.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@PreAuthorize("hasAuthority('USER')")
public class UserController {


    @Autowired
    UserRepo userRepo;

    @Autowired
    WorkGroupRepo workGroupRepo;

    @Autowired
    TaskRepo taskRepo;

    @Autowired
    UrgencyRepo urgencyRepo;

    @Autowired
    GroupManagerRepo groupManagerRepo;

    @Autowired
    StatusRepo statusRepo;

    @Value("${serveraddress}")
    private String serverAdress;

    @Value("${serverport}")
    private String serverPort;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Value("${manupload.path}")
    private String manUpPath;

    @Value("${smtpserver}")
    private String smtpserver;

    SimpleDateFormat addDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

    @GetMapping("/mytasks")
    public String showmytasks(
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

        //Iterable<Task> myrgtasks = taskRepo.findByExecutor(user);
        Iterable<Task> myrgtasks = taskRepo.findByExecutorAndStatusNot(user, statusRepo.findByName("Выполнено"));
        Iterable<Urgency> urgencys;


        urgencys = urgencyRepo.findAll();


        model.put("urgencys", urgencys);


        model.put("urlprefixPath", urlprefixPath);
        model.put("myrgtasks", myrgtasks);

        return "mytasks";
    }

    @GetMapping("/showonetaskforuser")
    public String showmyonetasks(
            @RequestParam(required = false, defaultValue = "0") String workgroup,
            @RequestParam(required = false, defaultValue = "0") String description,
            @RequestParam(required = false, defaultValue = "0") String urgency,
            @RequestParam(required = false, defaultValue = "0") String tid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

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

        List<Urgency> urgencys = urgencyRepo.findAll();


        //model.put("urgency", urgency);

        model.put("chronos", task.getChronos());
        model.put("execdate", task.getExecDate());
        model.put("comment", task.getComment());
        model.put("selectedurgency", task.getUrgency().getName());
        model.put("taskfilepath", taskFilePath);
        model.put("taskFileName", taskFileName);
        model.put("description", task.getDescription());
        model.put("tid", tid);
        model.put("urlprefixPath", urlprefixPath);


        return "showonetaskforuser";
    }

    @PostMapping("/showonetaskforuser")
    public String editmyonetasks(
            @RequestParam(required = false, defaultValue = "0") String description,
            @RequestParam(required = false, defaultValue = "0") String status,
            @RequestParam(required = false, defaultValue = "0") String report,
            @RequestParam(required = false, defaultValue = "0") String tid,
            @AuthenticationPrincipal User user,
            Map<String, Object> model)
    {

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

        StringBuilder chronos = new StringBuilder(task.getChronos());
        if (!task.getStatus().getName().equals(status))
        {
            task.setStatus(statusRepo.findByName(status));
            chronos.append(addDateFormat.format(new Date()) + " Статус задачи изменен на " + status + "\n");
            if (status.equals("Выполнено"))
            {
                task.setReport(report.equals("0") ? "Без отчета" : report);
                taskRepo.save(task);


                Iterable<Task> myrgtasks = taskRepo.findByExecutorAndStatusNot(user, statusRepo.findByName("Выполнено"));
                Iterable<Urgency> urgencys;

                List<String> recList = new ArrayList<>();
                if (task.getWorkGroup() != null)
                {

                    List<GroupManager> gm = groupManagerRepo.findByWorkGroupId(task.getWorkGroup().getId());
                    GroupManager gb = null;

                    if (gm.size() == 1)
                    {
                        gb = gm.get(0);
                    }
                    else {

                        for (GroupManager gml : gm) {
                            if (gml.getUser().getRoles().contains(Role.GROUPBOSS))
                            {
                                gb = gml;
                                break;
                            }
                        }
                    }

                    try {
                        User groupBoss = gb.getUser();
                        if (!recList.contains(groupBoss.getEmail())) {
                            recList.add(groupBoss.getEmail());
                        }

                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }
                }

                if(!recList.isEmpty()) {
                    Mail mail = new Mail();
                    mail.setRecepientList(recList);
                    mail.setTheme("Задача #" + task.getId() + " выполнена");
                    mail.setText("Добрый день!\n" +
                            "Задача #" + task.getId() + " выполнена Исполнителем - " + task.getExecutor().getFio() + " " + addDateFormat.format(new Date()) + "\n" +
                            // "Задача: http://10.161.193.164:8080/" + urlprefixPath + "/showonetaskforuser?tid=" + task.getId());
                            "Задача: http://" + serverAdress + ":" + serverPort + urlprefixPath + "/showonetaskforgroupboss?tid=" + task.getId());


                    ManagerController.sendEmail(mail, smtpserver);
                }

                //model.put("finstatus", "Выполнено");
                model.put("urlprefixPath", urlprefixPath);
                model.put("myrgtasks", myrgtasks);
                model.put("urgencys", urgencyRepo.findAll());

                return "mytasks";
            }
        }

        taskRepo.save(task);






        model.put("urgency", task.getUrgency().getName());

        model.put("chronos", task.getChronos());
        model.put("execdate", task.getExecDate());
        model.put("comment", task.getComment());
        model.put("selectedurgency", task.getUrgency().getName());
        model.put("taskfilepath", taskFilePath);
        model.put("taskFileName", taskFileName);
        model.put("description", description);
        model.put("tid", tid);
        model.put("urlprefixPath", urlprefixPath);


        return "showonetaskforuser";
    }

}
