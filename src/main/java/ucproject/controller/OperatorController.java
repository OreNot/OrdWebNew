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
import ucproject.UserCatalog;
import ucproject.UserFile;
import ucproject.domain.User;
import ucproject.repos.UrgencyRepo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Controller
@PreAuthorize("hasAuthority('OPERATOR')")
public class OperatorController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${logfile}")
    private String logPath;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Autowired
    UrgencyRepo urgencyRepo;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");
    SimpleDateFormat dayDateFormat = new SimpleDateFormat("dd.MM.yyyy");



    @GetMapping("/addtoarchive")
    public String addtoarchive(Map<String, Object> model) {
        if (!Files.exists(Paths.get(logPath)))
        {
            try {
                Files.createFile(Paths.get(logPath));

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        model.put("error", "");
       // model.put("urlprefixPath", urlprefixPath);
        return "addtoarchive";
    }

    @PostMapping("/addtoarchive")
    public String addtoarchive(
            @RequestParam(required = false, defaultValue = "0") String fio,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false, defaultValue = "0") String organization,
            @RequestParam(required = false, defaultValue = "0") String catnum,
            @AuthenticationPrincipal User user,
            Map<String, Object> model) {



        if (file != null && !file.getOriginalFilename().equals("") && !fio.equals("0") && !organization.equals("0") && !catnum.equals("0")) {

            String directoryName = uploadPath + "\\" + fio;
            fio = fio.trim();
            organization = organization.trim();
            catnum = catnum.trim();

            if (!Files.exists(Paths.get(directoryName))) {
                try {
                    Files.createDirectory(Paths.get(directoryName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            File fioDir = new File(uploadPath + "/" + fio.trim());
            if (!fioDir.exists()) {
                fioDir.mkdir();
                logging("Каталог \"" + fio.trim() + "\" создан");
            }

            //String uuidFile = UUID.randomUUID().toString();
            String dateForFileName = new SimpleDateFormat("dd.MM.yyyy_HH_mm_ss").format(new Date());
            String resultFileName = dateForFileName + "_" + fio.split(" ")[0] + "_" + organization.trim().replaceAll("\"", "").replaceAll("\\\\\\\\", "-").replaceAll("/", "-").replaceAll(":", "-") + "_" + catnum + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());

            File destFile = new File(fioDir + "/" + resultFileName);
            try {
                file.transferTo(destFile);

                logging("Файл \"" + resultFileName + "\" добавлен в архив пользователем " + user.getUsername());
                model.put("error", "Документы добавлены");
            } catch (IOException e) {
                logging("Ошибка добавления файла \"" + resultFileName + "\" в архив");
                e.printStackTrace();

            }
        }
        else {

            model.put("fio", fio.equals("0")?"":fio);
            model.put("organization", organization.equals("0")?"":organization.replaceAll("\"", ""));
            model.put("catnum", catnum.equals("0")?"":catnum);
            model.put("error", "*Необходимо заполнить все поля");
        }

        //model.put("urlprefixPath", urlprefixPath);
        return "addtoarchive";
    }

    @GetMapping("/showarchive")
    public String showarchive(
            @RequestParam(required = false, defaultValue = "0") String filterByFio,
            @RequestParam(required = false, defaultValue = "0") String filterByOrg,
            @RequestParam(required = false, defaultValue = "0") String radiofilter,
            Map<String, Object> model) {

        List<UserCatalog> userList = new ArrayList<>();
        File dir = new File(uploadPath);

        if (filterByFio.equals("0") && filterByOrg.equals("0"))
        {

            int count = 0;
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {

                    UserCatalog catalog = new UserCatalog();
                    List<UserFile> fileList = new ArrayList<>();
                    catalog.setFio(file.getName());

                    for (File f : file.listFiles()) {
                        if (!f.isDirectory()) {
                            String date = f.getName().substring(0, 16);
                            String orgName = f.getName().substring(16, f.getName().lastIndexOf("."));
                            orgName = orgName.substring(0, orgName.lastIndexOf("_"));
                            orgName = orgName.substring(orgName.lastIndexOf("_", orgName.length())).replaceAll("_", "");
                            fileList.add(new UserFile(date + " " +
                                    orgName, f.getAbsolutePath()
                                    .replaceAll("C:\\\\OrdStorage\\\\", "")
                                    .replaceAll("\\\\", "/")));
                            count++;
                        }
                    }
                    catalog.setUserFiles(fileList);
                    userList.add(catalog);
                    model.put("count", count);

                }
            }
        }

        switch (radiofilter)
        {
            case "fiofilter":
            {
                if (!filterByFio.equals("0")) {
                    int count = 0;
                    for (File file : dir.listFiles()) {
                        if (file.isDirectory()) {
                            if (file.getName().toUpperCase().contains(filterByFio.trim().toUpperCase()))
                            {

                                UserCatalog catalog = new UserCatalog();
                                List<UserFile> fileList = new ArrayList<>();
                                catalog.setFio(file.getName());

                                for (File f : file.listFiles()) {
                                    if (!f.isDirectory()) {
                                        count++;
                                        String date = f.getName().substring(0, 16);
                                        String orgName = f.getName().substring(16, f.getName().lastIndexOf("."));
                                        orgName = orgName.substring(0, orgName.lastIndexOf("_"));
                                        orgName = orgName.substring(orgName.lastIndexOf("_", orgName.length())).replaceAll("_", "");
                                        fileList.add(new UserFile(date + " " +
                                                orgName, f.getAbsolutePath()
                                                .replaceAll("C:\\\\OrdStorage\\\\", "")
                                                //.replaceAll("\\\\\\\\gren-wd-000318\\\\OrdStorage/", "")
                                                .replaceAll("\\\\", "/")));
                                    }
                                }
                                catalog.setUserFiles(fileList);
                                userList.add(catalog);
                                model.put("count", count);
                            }
                        }
                    }
                }



            }
            break;
            case "orgfilter":
            {
                if (!filterByOrg.equals("0")) {
                    int count = 0;
                    for (File directory : dir.listFiles()) {
                        UserCatalog catalog = new UserCatalog();
                        List<UserFile> fileList = new ArrayList<>();
                        if (directory.isDirectory()) {
                            for (File file : directory.listFiles())
                            {
                                if (file.getName().toUpperCase().contains(filterByOrg.trim().toUpperCase()))
                                {
                                    count++;
                                    String fio = file.getParent();
                                    fio = fio.substring(fio.lastIndexOf("\\") + 1, fio.length());
                                    catalog.setFio(fio);

                                    String date = file.getName().substring(0, 16);
                                    String orgName = file.getName().substring(16, file.getName().lastIndexOf("."));
                                    orgName = orgName.substring(0, orgName.lastIndexOf("_"));
                                    orgName = orgName.substring(orgName.lastIndexOf("_", orgName.length())).replaceAll("_", "");
                                    fileList.add(new UserFile(date + " " +
                                            orgName, file.getAbsolutePath()
                                            .replaceAll("C:\\\\OrdStorage\\\\", "")
                                            .replaceAll("\\\\", "/")));

                                }


                            }
                        }

                        catalog.setUserFiles(fileList);
                        if (catalog.getFio() != null) {
                            userList.add(catalog);
                            model.put("count", count);
                        }
                    }
                }
            }
            break;
        }



        model.put("userlist", userList);
        model.put("error", "");
        model.put("urlprefixPath", urlprefixPath);
        model.put("uploadPath", uploadPath);
        return "showarchive";
    }

    private void logging(String text)
    {

       //try(PrintWriter output = new PrintWriter(new FileWriter(logPath,true)))
       try(Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logPath, true), "UTF-8")))
       //try(PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logPath), StandardCharsets.UTF_8), true))
       {
            writer.append(dateFormat.format(new Date()) + text + "\r\n");
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
    @GetMapping("/archivelog")
    public String showarchivelog(Map<String, Object> model) {

        String error;
        Set<String> log = logFileReading(logPath);

        if (log.contains("Log file read error")) {
            model.put("error", log);
        } else {
            int countToday = 0;
            int countYersterday = 0;
            int monthCounter = 0;
            for (String logStr : log)
            {
                try {
                    Date logDate = dayDateFormat.parse(logStr.substring(0, 10));
                    if (logStr.substring(logStr.indexOf(".") + 1, 10).equals(dayDateFormat.format(new Date()).substring(dayDateFormat.format(new Date()).indexOf(".") + 1, 10)))
                    {
                        monthCounter++;
                    }
                     switch (getDateDifferent(logDate)) {
                         case 1:
                             countYersterday++;
                             break;

                         case 0:
                             countToday++;
                     }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                catch (StringIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
            }
            model.put("countToday",countToday);
            model.put("countYersterday",countYersterday);
            model.put("monthCounter",monthCounter);
            model.put("log", logFileReading(logPath));
        }

        model.put("urlprefixPath", urlprefixPath);

        return "archivelog";

    }

    private int getDateDifferent(Date date)
    {
        long milliseconds = new Date().getTime() - date.getTime();
        int days = (int) (milliseconds / (24 * 60 * 60 * 1000));

        return days;
    }

    private Set<String> logFileReading(String logFilePath)
    {
        Set<String> sb = new HashSet<>();
        Path path = Paths.get(logFilePath);
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(s -> sb.add(s));
        } catch (IOException ex) {
            sb.add("Log file read error");
            return sb;
        }

        List list = new ArrayList(sb);
        Collections.sort(list, Collections.reverseOrder());
        sb.clear();
        Set<String> reverceSb =  new LinkedHashSet(list);
        return reverceSb;
    }
}
