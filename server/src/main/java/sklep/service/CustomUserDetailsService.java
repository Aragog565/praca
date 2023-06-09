package sklep.service;

import sklep.entity.User;
import sklep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sklep.service.exception.EntityNotFoundException;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements AppUserDetailsService{

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String login) {

        return this.userRepository.findByEmail(login);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long id) {

        return this.userRepository.findById(id).orElse(null);
    }
}
