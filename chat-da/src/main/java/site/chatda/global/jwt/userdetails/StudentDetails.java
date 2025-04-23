package site.chatda.global.jwt.userdetails;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import site.chatda.domain.member.entity.Student;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class StudentDetails implements UserDetails {

    private Student student;

    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return student.getMember().getUuid();
    }
}
