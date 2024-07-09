package com.revature.ams.util.auth;

import com.revature.ams.Member.Member;

import javax.security.sasl.AuthenticationException;
import java.util.Scanner;

/**
 * Used to handle the information from a user request to log them in. Initialization requires
 * a Scanner & AuthService to be injected in.
 */
public class AuthController {

    private final Scanner scanner;
    private final AuthService authService;

    public AuthController(Scanner scanner, AuthService authService) {
        this.scanner = scanner;
        this.authService = authService;
    }

    public Member login(Member memberLoggedIn){
        try {
            if(memberLoggedIn != null) throw new RuntimeException("Already logged in");
            System.out.println("Please enter email: ");
            String email = scanner.next();

            System.out.println("Enter password: ");
            String password = scanner.next();

            return authService.login(email, password);

        } catch (AuthenticationException | RuntimeException e) {
            System.out.println(e.getMessage());;
        }
        return memberLoggedIn;
    }

}
