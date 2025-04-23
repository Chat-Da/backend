package site.chatda.global.jwt.userdetails;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.chatda.domain.member.entity.Member;
import site.chatda.domain.member.entity.Student;
import site.chatda.domain.member.entity.Teacher;
import site.chatda.domain.member.repository.MemberRepository;
import site.chatda.global.exception.CustomException;

import java.util.Collections;

import static site.chatda.global.statuscode.ErrorCode.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String uuid) throws UsernameNotFoundException {

        Member member = memberRepository.findMemberByUuid(uuid)
                .orElseThrow(() -> new CustomException(NOT_FOUND));

        UserDetails userDetails = null;

        switch (member.getRole()) {
            case ROLE_STUDENT -> {
                Student student = memberRepository.findStudentByMemberId(member.getId())
                        .orElseThrow(() -> new CustomException(NOT_FOUND));

                userDetails = new StudentDetails(student, Collections.singletonList(member.getRole()));
            }
            case ROLE_TEACHER -> {
                Teacher teacher = memberRepository.findTeacherByMemberId(member.getId())
                        .orElseThrow(() -> new CustomException(NOT_FOUND));

                userDetails = new TeacherDetails(teacher, Collections.singletonList(member.getRole()));
            }
        }

        return userDetails;
    }
}
