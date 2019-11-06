package ucproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ucproject.domain.*;
import ucproject.repos.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.*;
import javax.validation.constraints.Null;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('GROUPBOSS')")
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
    UserRepo userRepo;

    @Autowired
    GroupManagerRepo groupManagerRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private static JavaMailSenderImpl mailSender;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Value("${manupload.path}")
    private String manUpPath;

    @Value("${smtpserver}")
    private String smtpserver;

    @Value("${serveraddress}")
    private String serverAdress;

    @Value("${serverport}")
    private String serverPort;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat dateFormatForDateField = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat addDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    SimpleDateFormat fileNameDateFormat = new SimpleDateFormat("ddMMyyyyHHmmssSSS");

    static String username = "";
    static String password = "";

    static String[] creds;

    private static String CREDS_FILE_NAME = "C:\\Creds\\creds.txt";

    private static String SMTP_HOST_NAME = "core-s-exh01.gk.rosatom.local";

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
        //Fio fio = user.getFio();
        String fio = user.getFio();
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



            String chronos = addDateFormat.format(new Date()) + " Задача зарегистрирована " + userName + "\n";



            Task newTask = new Task(newStatus, description, newUrgency, edate, dateFormat.format(new Date()), newWorkGroup, user, chronos);

            if(file != null && !file.getOriginalFilename().equals("")) {
                newTask.setTaskFileName(destFile.getPath());
            }
            taskRepo.save(newTask);


            List<String> recList = new ArrayList<>();
            if (newTask.getWorkGroup() != null)
            {

                List<GroupManager> gm = groupManagerRepo.findByWorkGroupId(newTask.getWorkGroup().getId());
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
                mail.setTheme("Новая задача для РГ #" + newTask.getId());
                mail.setText("Добрый день!\n" +
                        "Для Вашей РГ зарегистрирована новая задача #" + newTask.getId() + "\n" +
                        // "Задача: http://10.161.193.164:8080/" + urlprefixPath + "/showonetaskforuser?tid=" + task.getId());
                        "Задача: http://" + serverAdress + ":" + serverPort + urlprefixPath + "/showonetaskforgroupboss?tid=" + newTask.getId());


                sendEmail(mail, smtpserver);
            }

            //mailSending(mail);




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
        @RequestParam(required = false, defaultValue = "0") String status,
        @RequestParam(required = false, defaultValue = "0") String urgency,
        @RequestParam(required = false, defaultValue = "0") String workgroup,
        @RequestParam(required = false, defaultValue = "0") String executor,
        @RequestParam(required = false, defaultValue = "0") String radiofilter,
        @RequestParam(required = false, defaultValue = "0") String finished,
        @AuthenticationPrincipal User user
        )
    {
        Iterable<Task> tasks = taskRepo.findAll();
        List<Task> warTasks = new ArrayList<>();
        List<Task> alarmTasks = new ArrayList<>();

        for (Task task : tasks)
        {
            try {
                Date taskDate = dateFormat.parse(task.getExecDate());
                long milliseconds = new Date().getTime() - taskDate.getTime();
                int days = (int) (milliseconds / (24 * 60 * 60 * 1000));
                String chrt = task.getChronos().toUpperCase();
                String str = "Срок исполнения задачи истёк".toUpperCase();
                boolean chr = chrt.contains(str);
                if (days >= 0 && !task.getChronos().toUpperCase().contains("Срок исполнения задачи истёк".toUpperCase()))
                {
                   alarmTasks.add(task);
                   task.setChronos(task.getChronos() + addDateFormat.format(new Date()) + " Срок исполнения задачи истёк. Задача возвращена на распределение.\n");
                   task.setStatus(statusRepo.findByName("Возвращено на распределение"));
                   //task.setUrgency(urgencyRepo.findByName("Важно"));
                   task.setWorkGroup(workGroupRepo.findByName("Не назначена"));

                }
                if (days == -3 && !task.getChronos().toUpperCase().contains("Срок исполения задачи истекает".toUpperCase()) && !task.getChronos().toUpperCase().contains("срок исполения задачи истекает чере 3 дня".toUpperCase()))
                {
                    warTasks.add(task);
                    task.setChronos(task.getChronos() + addDateFormat.format(new Date()) + " Срок исполения задачи истекает через 3 дня.\n");
                }



            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (!warTasks.isEmpty())
        {
            for (Task task : warTasks)
            {
                List<String> recList = new ArrayList<>();
                if (task.getExecutor() != null && task.getExecutor().getEmail() != null && !task.getExecutor().getEmail().equalsIgnoreCase(""))
                {
                    if (!recList.contains(task.getExecutor().getEmail())) {
                        recList.add(task.getExecutor().getEmail());
                    }
                }

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

                    User groupBoss = gb.getUser();
                    if (!recList.contains(groupBoss.getEmail())) {
                        recList.add(groupBoss.getEmail());
                    }
                }

                Mail mail = new Mail();
                mail.setRecepientList(recList);
                mail.setTheme("Cрок исполения задачи #" + task.getId() + " скоро истекает");
                mail.setText("Добрый день!\n" +
                        "Срок исполнения задачи #" + task.getId() + " истекает через 3 дня.\n" +
                       // "Задача: http://10.161.193.164:8080/" + urlprefixPath + "/showonetaskforuser?tid=" + task.getId());
                       "Задача: http://" + serverAdress + ":" + serverPort + urlprefixPath + "/showonetaskforuser?tid=" + task.getId());


                sendEmail(mail, smtpserver);
                //mailSending(mail);


            }
        }

        if (!alarmTasks.isEmpty())
        {
            for (Task task : alarmTasks)
            {
                List<String> recList = new ArrayList<>();
                if (task.getExecutor() != null && task.getExecutor().getEmail() != null && !task.getExecutor().getEmail().equalsIgnoreCase(""))
                {
                    if (!recList.contains(task.getExecutor().getEmail())) {
                        recList.add(task.getExecutor().getEmail());
                    }
                }

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
                } catch(NullPointerException e)
                {
                    e.printStackTrace();
                }
                }
                Mail mail = new Mail();
                mail.setRecepientList(recList);
                mail.setTheme("Cрок исполения задачи #" + task.getId() + " истёк");
                mail.setText("Добрый день!\n" +
                        "Срок исполнения задачи #" + task.getId() + " истёк.\n" +
                        "Задача возвращена на распределение.\n" +
                        // "Задача: http://10.161.193.164:8080/" + urlprefixPath + "/showonetaskforuser?tid=" + task.getId());
                        "Задача: http://" + serverAdress + ":" + serverPort + urlprefixPath + "/showonetaskforuser?tid=" + task.getId());



                sendEmail(mail, smtpserver);
                //mailSending(mail);


            }
        }



        Iterable<Urgency> urgencys;
        Iterable<Status> statuses;
        Iterable<WorkGroup> workgroups;
        Iterable<User> executors;

        statuses = statusRepo.findAll();
        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();
        executors = userRepo.findAll();

        model.put("radiofilterset", radiofilter.equals("0") ? "statusfilter" : radiofilter);
        model.put("finished", finished);
        model.put("statuses", statuses);
        model.put("statusset", status);
        model.put("urgencys", urgencys);
        model.put("urgencyset", urgency);
        model.put("workgroups", workgroups);
        model.put("workgroupset", workgroup);
        model.put("executors", executors);
        model.put("executorset", executor);
        model.put("urlprefixPath", urlprefixPath);
        model.put("tasks", tasks);
        return "showtask";
    }

    @PostMapping("/showtask")
    public String showfiltertask(
            Map<String, Object> model,
            @RequestParam(required = false, defaultValue = "0") String radiofilter,
            @RequestParam(required = false, defaultValue = "0") String status,
            @RequestParam(required = false, defaultValue = "0") String urgency,
            @RequestParam(required = false, defaultValue = "0") String workgroup,
            @RequestParam(required = false, defaultValue = "0") String executor,
            @RequestParam(required = false, defaultValue = "0") String finished,
            @AuthenticationPrincipal User user
    )
    {

        Iterable<Task> tasks = null;
        switch (radiofilter)
        {
            case "statusfilter":

                    tasks = !status.equals("Статус") && !status.equals("Все") ? taskRepo.findByStatusName(status) : taskRepo.findAll();

            break;

            case "urgencyfilter":

                tasks = finished.equals("on") ? !urgency.equals("Важность") && !urgency.equals("Все") ? taskRepo.findByUrgencyNameAndStatusNameNot(urgency, "Выполнено") : taskRepo.findByStatusNameNot("Выполнено") : !urgency.equals("Важность") && !urgency.equals("Все") ? taskRepo.findByUrgencyName(urgency) : taskRepo.findAll();

                break;

            case "workgroupfilter":
                tasks = finished.equals("on") ? !workgroup.equals("РГ") && !workgroup.equals("Все") ? taskRepo.findByworkGroupNameAndStatusNameNot(workgroup, "Выполнено") : taskRepo.findByStatusNameNot("Выполнено") : !workgroup.equals("РГ") && !workgroup.equals("Все") ? taskRepo.findByworkGroupName(workgroup) : taskRepo.findAll();

                break;

            case "executorfilter":
                tasks = finished.equals("on") ? !executor.equals("Исполнитель") && !executor.equals("Все") ? taskRepo.findByExecutorAndStatusNameNot(userRepo.findByFio(executor), "Выполнено") : taskRepo.findByStatusNameNot("Выполнено") : !executor.equals("Исполнитель") && !executor.equals("Все") ? taskRepo.findByExecutor(userRepo.findByFio(executor)) : taskRepo.findAll();

                break;
        }
        //Iterable<Task> tasks = taskRepo.findByStatus(statusRepo.findByName(status));



        Iterable<Urgency> urgencys;
        Iterable<Status> statuses;
        Iterable<WorkGroup> workgroups;
        Iterable<User> executors;

        statuses = statusRepo.findAll();
        urgencys = urgencyRepo.findAll();
        workgroups = workGroupRepo.findAll();
        executors = userRepo.findAll();

        model.put("statuses", statuses);
        model.put("statusset", status);
        model.put("urgencys", urgencys);
        model.put("urgencyset", urgency);
        model.put("workgroups", workgroups);
        model.put("workgroupset", workgroup);
        model.put("executors", executors);
        model.put("executorset", executor);
        model.put("urlprefixPath", urlprefixPath);
        model.put("radiofilterset", radiofilter);
        model.put("finished", finished);
        model.put("tasks", tasks);
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

        String taskFilePath;
        String taskFileName;
        String oldFileName;
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

        String selectedurgency = editableurgency;
        String selectedworkgroup = editableworkgroup;
        String selecteddescription = editabledescription;
        String selectedexecdate = editableexecdate;



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

    private static class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(username, password);
        }
    }


    static void sendEmail(Mail mail, String smtpserver) {

        try {
        creds = credRead();
        username = creds[0].replaceAll("\n", "").replaceAll("\r", "");
        password = creds[1].replaceAll("\n", "").replaceAll("\r", "");

        mailSender.setHost(smtpserver);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        /*
        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtpserver);
        props.put("mail.smtp.auth", "true");
        */

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("sep@rosatom.ru");
        for (String rec : mail.getRecepientList())
        {
            msg.setTo(rec);
        }

        msg.setSubject(mail.getTheme());
        msg.setText(mail.getText());



            mailSender.send(msg);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //javaMailSender.send(msg);

    }

    static void mailSending(Mail mail) {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        SimpleDateFormat dateFormatBody = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat subFormatBody = new SimpleDateFormat("ddMMyyyy");

        //String dateBody = dateFormatBody.format(new Date());
        String dateBody = dateFormatBody.format(calendar.getTime());
        //String dateSub = subFormatBody.format(new Date());
        String dateSub = subFormatBody.format(calendar.getTime());


        creds = credRead();
        username = creds[0].replaceAll("\n", "").replaceAll("\r", "");
        password = creds[1].replaceAll("\n", "").replaceAll("\r", "");


        try {

            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", SMTP_HOST_NAME);
            props.put("mail.smtp.auth", "true");

            Authenticator auth = new SMTPAuthenticator();
            Session mailSession = Session.getDefaultInstance(props, auth);

            Transport transport = mailSession.getTransport();

            transport.connect();
            MimeMessage message = null;
            message = new MimeMessage(mailSession);

            message.setSubject(mail.getTheme());

            message.setFrom(new InternetAddress("sep@rosatom.ru"));

            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("aamartynyuk@greenatom.ru"));

            for (String recepient : mail.getRecepientList())
            {
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(recepient));
            }

            MimeBodyPart textBodyPart = new MimeBodyPart();


            Multipart multipart = new MimeMultipart();

            textBodyPart.setText(mail.getText());

            multipart.addBodyPart(textBodyPart);
            message.setContent(multipart);
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();

            //JOptionPane.showMessageDialog(null, "Файлы отправлены адресатам!");

        } catch (MessagingException e1) {
            //JOptionPane.showMessageDialog(null, "Ошибка отправки почты (возможно истек пароль к УЗ): " + e1.getMessage());
            e1.printStackTrace();
        } catch (Exception e) {
            //JOptionPane.showMessageDialog(null, "Ошибка отправки почты (возможно истек пароль к УЗ): " + e.getMessage());
            e.printStackTrace();
        }

    }

    private static String[] credRead()
    {
        String[]creds=new String[0];

        try(FileReader reader=new FileReader(CREDS_FILE_NAME))
        {
            char[]buf=new char[256];
            int c;
            while((c=reader.read(buf))>0){

                if(c < 256){
                    buf= Arrays.copyOf(buf,c);
                }
                creds=String.valueOf(buf).trim().replaceAll(" ","").split("\n");
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }


        return creds;
    }



}
