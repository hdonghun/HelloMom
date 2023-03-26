package hello.mom.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;
    @Column(nullable = false)
    private String email;
    @JsonIgnore
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;

//    @Column(nullable = false)
//    private Authority role;
//    @Column
//    private Long kakaoId;
//    @OneToMany(mappedBy = "member")
//    private List<Dog> dogs;

    // 기본 생성자
    public Member() {
    }

    @Builder
    public Member(Long memberNo, String email, String password, String nickname) {
        this.memberNo = memberNo;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
//        this.role = role;
//        this.kakaoId = kakaoId;
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void update(String nick) {
        this.nickname = nickname;
    }


}