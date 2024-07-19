package com.revature.ams.Member;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Member {
    private int memberId;
    private String firstName;
    private String lastName;
    private String email;
    private MemberType type;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public enum MemberType {
        PILOT, ADMIN, PASSENGER
    }

    public Member(){}

    public Member(int id, String firstName, String lastName, String email, MemberType type, String password) {
        this.memberId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.type = type;
        this.password = password;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public MemberType getType() {
        return type;
    }

    public void setType(String type) {
        this.type = MemberType.valueOf(type);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + memberId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", type=" + type +
                '}';
    }


}
