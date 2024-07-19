package com.revature.ams.Member;

import com.revature.ams.util.exceptions.DataNotFoundException;
import com.revature.ams.util.interfaces.Serviceable;

import java.util.List;

public class MemberService implements Serviceable<Member> {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Member> findAll() {
        return null;
    }

    @Override
    public Member findById(int memberId) {
        return memberRepository.findById(memberId);
    }

    @Override
    public Member create(Member newMember) {
        return memberRepository.create(newMember);
    }

    public boolean delete(Member removedMemeber) {
        return memberRepository.delete(removedMemeber);
    }

    /**
     * Searches the database for information where the email & password provided must be equal to a row within
     * our database.
     *
     * @param email - String
     * @param password - String
     * @return - Member object, if no member found it will return null
     */
    public Member findByEmailAndPassword(String email, String password){
        return memberRepository.findByEmailAndPassword(email, password);
    }

    /**
     * Update method takes in a Member Object with the updated information. Searches the list for a matching
     * memberId. Once found it replaces the object at the index with matching ids with the updated Member.
     * IF not memberId is matched, we throw an exception.
     *
     * @param updatedMember - Is an existing Member with updated information based on their request
     * @throws DataNotFoundException - MemberId provided doesn't match with anything in the database
     */
    public boolean update(Member updatedMember) {
        return memberRepository.update(updatedMember);
    }

}
