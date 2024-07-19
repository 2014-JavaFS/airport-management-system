package com.revature.ams.Member;

import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.interfaces.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.Objects;

public class MemberController implements Controller {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }


    @Override
    public void registerPaths(Javalin app) {
        app.post("/members", this::postNewMember);
        app.get("/members/{memberId}", this::getMemberById);
        app.put("/members", this::putUpdateMember);
        app.delete("/members", this::deleteMember);
    }


    private void postNewMember(Context ctx) {
        Member newMember = ctx.bodyAsClass(Member.class);
        newMember.setType("PASSENGER");
        Member member = memberService.create(newMember);

        ctx.status(201);
        ctx.json(member);
    }


    private void getMemberById(Context ctx) {
        int memberId = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("memberId")));

        try {
            Member foundMember = memberService.findById(memberId);
            ctx.json(foundMember);
        } catch (DataNotFoundException e) {
            ctx.status(404);
            ctx.result(e.getMessage());
        }

    }

    private void deleteMember(Context ctx) {
        int memberId = loggedInCheck(ctx);
        if (memberId == -1) return;

        Member toDelete = new Member();
        toDelete.setMemberId(memberId);

        if (!memberService.delete(toDelete)) {
            ctx.status(400);
        } else {
            ctx.status(204);
        }
    }


    private void putUpdateMember(Context ctx) {
        int memberId = loggedInCheck(ctx);
        if (memberId == -1) return;

        Member updatedMember = ctx.bodyAsClass(Member.class);

        if (updatedMember.getMemberId() == memberId) {
            memberService.update(updatedMember);
            ctx.status(202);
            ctx.result("Member has been updated.");
        } else {
            ctx.status(403);
            ctx.result("You are not logged in as the member you're trying to update. Please try again");
        }
    }

    private int loggedInCheck(Context ctx) {
        String headerMemberId = ctx.header("memberId");
        if (headerMemberId == null) {
            ctx.status(400);
            ctx.result("You are not logged in.");
            return -1;
        }
        return Integer.parseInt(headerMemberId);
    }

}


