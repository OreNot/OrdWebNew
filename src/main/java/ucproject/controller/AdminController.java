package ucproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ucproject.domain.*;
import ucproject.repos.FioRepo;
import ucproject.repos.GroupManagerRepo;
import ucproject.repos.UserRepo;
import ucproject.repos.WorkGroupRepo;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    WorkGroupRepo workGroupRepo;

    @Autowired
    FioRepo fioRepo;

    @Autowired
    GroupManagerRepo groupManagerRepo;

    @Value("${urlprefix}")
    private String urlprefixPath;

    @Value("${manupload.path}")
    private String manUpPath;

    @GetMapping("/usersettings")
    public String showusers(Map<String, Object> model) {

        Iterable<User> userlist = userRepo.findAll();
        Iterable<WorkGroup> workgroupslist = workGroupRepo.findAll();


        model.put("userlist", userlist);
        model.put("workgroupslist", workgroupslist);
        model.put("urlprefixPath", urlprefixPath);

        return "usersettings";

    }

    @PostMapping("/usersettings")
    public String editusers(
            @RequestParam(required = false, defaultValue = "0") String username,
            @RequestParam(required = false, defaultValue = "0") String fio,
            @RequestParam(required = false, defaultValue = "0") String userrole,
            @RequestParam(required = false, defaultValue = "0") String workgroup,
            Map<String, Object> model)
    {


        User editableUser = userRepo.findByUsername(username);
        Fio newFio;

        Set<Role> roles = editableUser.getRoles();

        if(!fio.equals("0")) {
            if (fioRepo.findByFio(fio.trim()) == null || fioRepo.findByFioIgnoreCase(fio.trim()).equals("")) {
                newFio = new Fio(fio.trim());
                fioRepo.save(newFio);

            } else {
                newFio = fioRepo.findByFioIgnoreCase(fio.trim());
            }


        editableUser.setFio(newFio);
        }

        if (!roles.contains(userrole))
        {
            switch (userrole)
            {
                case "OPERATOR" :
                {
                    roles.add(Role.OPERATOR);
                    break;
                }

                case "GROUPBOSS" :
                {
                    roles.add(Role.GROUPBOSS);
                    break;
                }

                case "MANAGER" :
                {
                    roles.add(Role.MANAGER);
                    break;
                }

                case "SUPERBOSS" :
                {
                    roles.add(Role.SUPERBOSS);
                    break;
                }
            }
            editableUser.setRoles(roles);
        }

        if (!workgroup.equals("NONE"))
        {
            GroupManager g = groupManagerRepo.findByUser(editableUser);
            if (groupManagerRepo.findByUser(editableUser) == null)
            {
                GroupManager groupManager = new GroupManager(editableUser, workGroupRepo.findByName(workgroup));
                groupManagerRepo.save(groupManager);
            }
            if (!groupManagerRepo.findByUser(editableUser).getWorkGroup().getName().equals(workGroupRepo.findByName(workgroup).getName()))
            {
                GroupManager groupManager = new GroupManager(editableUser, workGroupRepo.findByName(workgroup));
                groupManagerRepo.save(groupManager);
            }

        }

        userRepo.save(editableUser);

        Iterable<User> userlist = userRepo.findAll();
        Iterable<WorkGroup> workgroupslist = workGroupRepo.findAll();


        model.put("userlist", userlist);
        model.put("error", "Пользователь изменен");
        model.put("workgroupslist", workgroupslist);
        model.put("urlprefixPath", urlprefixPath);

        return "usersettings";

    }


}
