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
import ucproject.domain.Urgency;
import ucproject.repos.UrgencyRepo;

import javax.swing.*;
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

public class MainController {

    @Value("${upload.path}")
    private String uploadPath;

    @Value("${logfile}")
    private String logPath;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Autowired
    UrgencyRepo urgencyRepo;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss ");

    @GetMapping("/")
    public String main(Map<String, Object> model) {

         model.put("urlprefixPath", urlprefixPath);
        return "main";
    }


}