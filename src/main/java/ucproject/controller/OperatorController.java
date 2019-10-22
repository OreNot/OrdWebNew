package ucproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ucproject.UserCatalog;
import ucproject.UserFile;
import ucproject.repos.UrgencyRepo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
                logging("Файл \"" + resultFileName + "\" добавлен в архив");
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

        try(PrintWriter output = new PrintWriter(new FileWriter(logPath,true)))
        {
            output.printf("%s\r\n", dateFormat.format(new Date()) + text);
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
}
