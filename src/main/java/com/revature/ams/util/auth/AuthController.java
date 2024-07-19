package com.revature.ams.util.auth;

import com.revature.ams.Member.Member;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

import javax.security.sasl.AuthenticationException;
import java.lang.management.MemoryManagerMXBean;
import java.util.Scanner;

public class AuthController implements Controller {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void registerPaths(Javalin app) {
        app.post("/login", this::postLogin);
        app.get("/user-info", this::getRedirect);
    }

    private void getRedirect(Context ctx){
        ctx.redirect("https://i.pinimg.com/736x/6a/6d/11/6a6d1124cf69e5588588bc7e397598f6.jpg");
    }

    private void postLogin(Context ctx){
        String email = ctx.queryParam("email");
        String password = ctx.queryParam("password");

        try {
            Member member = authService.login(email, password);
            ctx.header("memberId", String.valueOf(member.getMemberId()));
            ctx.header("memberType", member.getType().name());
            ctx.status(200);
        } catch (AuthenticationException e) {
            ctx.status(HttpStatus.UNAUTHORIZED);
        }
    }
}
