package ucproject.service;

import antlr.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ucproject.domain.Role;
import ucproject.domain.User;
import ucproject.repos.UserRepo;

import java.util.Collections;

@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepo userRepo;

    @Bean
    public PasswordEncoder getPasswordEncoder()
    {
        return new BCryptPasswordEncoder(8);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user)
    {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null)
        {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setFio(null);
        user.setPassword(user.getPassword());
        userRepo.save(user);

        return true;
    }

    public void updateProfile(User user, String password) {

        if (!password.equals(""))
        {
            user.setPassword(password);
        }
        userRepo.save(user);
    }
}
