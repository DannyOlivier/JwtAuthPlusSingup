package com.example.demo.web;

import com.example.demo.domain.ActivationCode;
import com.example.demo.domain.User;
import com.example.demo.repository.ActivationCodeRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/activate")
public class ActivateUserController {

    @Autowired
    UserRepository users;

    @Autowired
    ActivationCodeRepository activationCodeRepository;

    @PostMapping
    public ResponseEntity activate(@RequestParam String activationCode) {
        Map<String, String> result = new HashMap<>();

        Optional<ActivationCode> activationCodeOptional = this.activationCodeRepository.findById(activationCode);
        if(activationCodeOptional.isPresent()){
            User user = this.users.findById(activationCodeOptional.get().getUserName()).get();
            user.enable();
            this.users.save(user);
            result.put("userActive","true");
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.badRequest().body(result);
        }
    }
}
