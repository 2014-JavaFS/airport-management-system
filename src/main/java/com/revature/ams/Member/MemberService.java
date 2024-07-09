package com.revature.ams.Member;

import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.interfaces.Serviceable;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to validate all information that was provided by the controller based on the business logic of our application
 * prior to its persistence to the database. List acting as temporary ephemeral database, PLEASE UPDATE WITH ADDITION OF
 * DAO!
 */
public class MemberService implements Serviceable<Member> {
    private List<Member> memberList = new ArrayList<>();

    /**
     * No implementation as this is a feature we don't want, but is required by Serviceable.
     *
     * @return null
     */
    @Override
    public Member[] findAll() {
        return null;
    }

    /**
     * Iterates through our database to find the Member object with the associated memberId.
     *
     * @param memberId - int value reflective of our database value
     * @return - Member object if found in database, otherwise null
     */
    @Override
    public Member findById(int memberId) {
        for (Member member : memberList) {
            if (member.getMemberId() == memberId) {
                return member;
            }
        }
        return null;
    }

    /**
     * Simply adds a newly created member to the database, no validation at this point.
     *
     * @param newMember - newly created member from the controller layer
     * @return - the newMember provided in the param
     */
    @Override
    public Member create(Member newMember) {
        memberList.add(newMember);
        return newMember;
    }

    /**
     * Searches the database for information where the email & password provided must be equal to a row within
     * our database. Only utilized by the AuthService
     *
     * @param email - String
     * @param password - String
     * @return - Member object, if no member found it will return null
     */
    public Member findByEmailAndPassword(String email, String password){
        for (Member member : memberList) {
            if (member.getEmail().equals(email) && member.getPassword().equals(password)) {
                return member;
            }
        }
        return null;
    }

    /**
     * Update method takes in a Member Object with the updated information. Searches the list for a matching
     * memberId. Once found it replaces the object at the index with matching ids with the updated Member.
     * IF not memberId is matched, we throw an exception.
     *
     * @param updatedMember - Is an existing Member with updated information based on their request
     * @throws DataNotFoundException - MemberId provided doesn't match with anything in the database
     */
    public void update(Member updatedMember) {
        for (int i = 0; i < memberList.size(); i++) {
            if (memberList.get(i).getMemberId() == updatedMember.getMemberId()) {
                memberList.set(i, updatedMember);
                return;
            }
        }

        throw new DataNotFoundException("Member with ID provided not within Database");

    }

}
