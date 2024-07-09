package com.revature.ams.Member;

/**
 * Member model holds all information pertaining to our pilots, admins & passengers for usage of our application
 */
public class Member {
    private int memberId;
    private String firstName;
    private String lastName;
    private String email;
    private MemberType memberType;
    private String password;

    /**
     * ENUM for specific use on CONSTANT values that are acceptable for our MemberType
     */
    public enum MemberType {
        PILOT, ADMIN, PASSENGER
    }

    public Member(){}

    public Member(int id, String firstName, String lastName, String email, MemberType memberType, String password) {
        this.memberId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.memberType = memberType;
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

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
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

    /**
     * Overridden toString method excludes password for security purposes
     * @return
     */
    @Override
    public String toString() {
        return "Member{" +
                "id=" + memberId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", type=" + memberType +
                '}';
    }


}
