package ucproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ucproject.domain.Role;
import ucproject.domain.User;
import ucproject.repos.UserRepo;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration(Map<String, Object> model)
    {
       System.out.println(urlprefixPath);
       model.put("urlprefixPath", urlprefixPath);

        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user,
                          Map<String, Object> model)
    {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "registration";
        }


        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        model.put("urlprefixPath", urlprefixPath);
        return "redirect:/login";
    }
}
