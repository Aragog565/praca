package sklep.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sklep.entity.Role;
import sklep.entity.User;
import sklep.repository.RoleRepository;
import sklep.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;

@Component
public class SetupRolesLoader
        implements
        ApplicationListener<ContextRefreshedEvent>
{

    boolean alreadySetup = false;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SetupRolesLoader(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;

        for(Constants.Role role : Constants.Role.values()){
            createRoleIfNotFound(role.name);
        }

        createAdminIfNotFound();

        alreadySetup = true;
    }

    @Transactional
    void createRoleIfNotFound(String name){
        Role role = roleRepository.findByName(name);
        if(role == null){
            role = new Role(name);
            roleRepository.save(role);
        }
    }

    @Transactional
    void createAdminIfNotFound(){
        User user = new User();
        user.setName("MainAdmin");
        user.setEmail("admin@gmail.com");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setSurname("TOMITO");
        user.setPhoto("TOMITO");
        Role adminRole = roleRepository.findByName(Constants.Role.ADMIN.name);
        if(adminRole != null){
            user.setRole(adminRole);
        }
        if(userRepository.findByName(user.getName()) == null) {
            userRepository.save(user);
        }
    }

}
