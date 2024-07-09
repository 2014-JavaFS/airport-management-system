package com.revature.ams.Member;

// TODO: REVIEW ME

import com.revature.ams.util.exceptions.DataNotFoundException;

import java.util.Scanner;

/**
 * Used to handle the content for a request by a user and return any pertinent information back. Initialization requires
 * a Scanner & MemberService to be injected in.
 */
public class MemberController {
    private final Scanner scanner;
    private final MemberService memberService;
    private int count = 0;

    public MemberController(Scanner scanner, MemberService memberService) {
        this.scanner = scanner;
        this.memberService = memberService;
    }

    /**
     * Prompts the user for information to be registered to our database. Information is then passed down to the memberService
     * to be validated prior to persistence in database.
     */
    public void register(){
        System.out.println("Please enter first name: ");
        String firstName = scanner.next();

        System.out.println("Please enter last name: ");
        String lastName = scanner.next();

        System.out.println("Please enter email: ");
        String email = scanner.next();

        Member.MemberType memberType = Member.MemberType.valueOf("PASSENGER");

        System.out.println("Enter password: ");
        String password = scanner.next();

        Member newMember = new Member(count,firstName, lastName, email, memberType, password);
        memberService.create(newMember);
        count++;
    }

    /**
     * Currently establishes a mean to update a single attribute of the class for persistence in our database. Prompts the
     * user for information about the MemberId they wish to update. Proceeds to search that information to assure its'
     * existence in the database. Once confirmed it moves on to ask the single attribute to update for our Member. Performs
     * update after all new information has been gathered & will handle any exceptions thrown by the memberService update method.
     */
    public void update(){
        System.out.println("Enter the ID of the member to update: ");
        int memberId = scanner.nextInt();

        Member memberToUpdate = memberService.findById(memberId);

        if (memberToUpdate == null) {
            System.out.println("Member with ID " + memberId + " not found.");
            return;
        }

        System.out.println("What information do you want to update?");
        System.out.println("1. First Name");
        System.out.println("2. Last Name");
        System.out.println("3. Email");
        System.out.println("4. Password");
        System.out.println("5. Cancel");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Enter new first name: ");
                String newFirstName = scanner.next();
                memberToUpdate.setFirstName(newFirstName);
                break;
            case 2:
                System.out.println("Enter new last name: ");
                String newLastName = scanner.next();
                memberToUpdate.setLastName(newLastName);
                break;
            case 3:
                System.out.println("Enter new email: ");
                String newEmail = scanner.next();
                memberToUpdate.setEmail(newEmail);
                break;
            case 4:
                System.out.println("Enter new password: ");
                String newPassword = scanner.next();
                memberToUpdate.setPassword(newPassword); // Remember to implement secure password storage
                break;
            case 5:
                System.out.println("Update cancelled.");
                return;
            default:
                System.out.println("Invalid choice.");
        }

        try {
            memberService.update(memberToUpdate); // Call update method in MemberService
            System.out.println("Member information updated successfully!");
        } catch (DataNotFoundException e) {
            System.out.println(e.getMessage()); // Does this ever change? Will change with logging
        }

    }

}
