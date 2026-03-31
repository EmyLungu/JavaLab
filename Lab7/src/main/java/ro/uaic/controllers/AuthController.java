package ro.uaic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ro.uaic.jwt.JwtUtils;

/**
 * AuthController
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public String login(@RequestParam String username) {
        String token = jwtUtils.generateToken(username);
        return token;
    }
}
