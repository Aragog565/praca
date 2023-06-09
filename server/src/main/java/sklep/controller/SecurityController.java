package sklep.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import sklep.config.security.JwtTokenProvider;
import sklep.service.UserService;
import sklep.service.dto.LoginDTO;
import sklep.service.dto.Create.RegisterDTO;
import sklep.service.dto.TokenDTO;
import sklep.service.dto.UserDTO;
import sklep.service.exception.EntityNotFoundException;

import java.net.URI;
import java.net.URISyntaxException;

@CrossOrigin
@RestController
@RequestMapping(path="/api/auth")
public class SecurityController {

    private static final Logger log = LoggerFactory.getLogger(SecurityController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @Autowired
    public SecurityController(AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider,
                              UserService userService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<Void> register(
            @RequestBody @Validated RegisterDTO registerDTO
    ) throws URISyntaxException {
        log.debug("Request to save: user {}", registerDTO);

        userService.save(registerDTO);

        return ResponseEntity.created(new URI("")).build();
    }

    @PostMapping("login")
    public ResponseEntity<TokenDTO> login(
            @RequestBody @Validated LoginDTO loginDTO
    ) {
        log.debug("Request to create: new token {}", loginDTO);

        try {
            String email = loginDTO.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, loginDTO.getPassword()));

            String token = jwtTokenProvider.createToken(email);

            return ResponseEntity.ok(new TokenDTO(token));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid login/password supplied");
        }
    }

    @GetMapping("user")
    public ResponseEntity<UserDTO> getAuthUser(
        Authentication authentication
    ){
        log.debug("Request to get: authenticated user {}", authentication);

        if (authentication == null) {
            throw new EntityNotFoundException("trash--getAuthUser--SecurityController");
        }

        UserDTO userDTO = userService.getUserByEmail(authentication.getName());

        return ResponseEntity.ok(userDTO);
    }

}
