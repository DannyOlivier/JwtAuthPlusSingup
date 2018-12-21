package com.example.demo.web;

import com.example.demo.domain.ActivationCode;
import com.example.demo.domain.User;
import com.example.demo.repository.ActivationCodeRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ActivationCodeRepository activationCodeRepository;

    @PostMapping("/signIn")
    public ResponseEntity signIn(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = jwtTokenProvider.createToken(username, this.users.findById(username).orElseThrow(() -> new UsernameNotFoundException("Username " + username + "not found")).getRoles());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("token", token);
            return ResponseEntity.ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

    @PostMapping("/signUp")
    public ResponseEntity signUp(@RequestBody User data) {
        Map<String, String> result = new HashMap<>();

        if(this.users.findById(data.getUsername()).isPresent()){
            result.put("Error","The User Name is already taken");
            return ResponseEntity.badRequest().body(result);
        }

        User user = this.users.save(User.builder()
                .username(data.getUsername())
                .password(this.passwordEncoder.encode(data.getPassword()))
                .roles(data.getRoles())
                .enabled(false)
                .build());

        ActivationCode activationCode =  new ActivationCode(UUID.randomUUID().toString(), user.getUsername());

        this.activationCodeRepository.save(activationCode);

        result.put("userCreatedSuccessfully", "true");
        result.put("activationCode", activationCode.getActivationCode());
        return ResponseEntity.ok(result);
    }
}
