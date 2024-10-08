package com.revature.ams.Member;

import com.revature.ams.util.exceptions.DataNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/members")
public class MemberController{
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    private ResponseEntity<Member> postNewMember(@Valid @RequestBody Member member) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(memberService.create(member));
    }

    @GetMapping("/{memberId}")
    private ResponseEntity<Member> getMemberById(@PathVariable int memberId) {
        return ResponseEntity.ok(memberService.findById(memberId));
    }

    @DeleteMapping
    private ResponseEntity<Void> deleteMember(@Valid @RequestBody Member member) {
        memberService.delete(member);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    private ResponseEntity<Void> putUpdateMember(@Valid @RequestBody Member member) {
        memberService.update(member);
        return ResponseEntity.noContent().build();
    }

    private int loggedInCheck() {
      return -1;
    }


}


