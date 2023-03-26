package hello.mom.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hello.mom.member.util.Authority;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY) //엔티티 클래스의 primary key에 대한 값을 자동으로 생성해주는 방식을 지정합니다. 이 때, @GeneratedValue 어노테이션은 해당 필드가 자동 생성되어야 함을 나타내기 위해 @Id 어노테이션과 함께 사용됩니다.
    private Long memberNo;
    @Column(nullable = false)
    private String email;
    @JsonIgnore // JSON 데이터에서 password 필드는 제외되어 출력됩니다. 즉, JSON 직렬화 과정에서 해당 필드가 무시됩니다.
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nick;

    @Column(nullable = false)
    private Authority role;

//    @Column
//    private Long kakaoId;
//    @OneToMany(mappedBy = "member")
//    private List<Dog> dogs;

    // 기본 생성자
    public Member() {
    }

    @Builder
    public Member(Long memberNo, String email, String password, String nick, Authority role) {
        this.memberNo = memberNo;
        this.email = email;
        this.password = password;
        this.nick = nick;
        this.role = role;
//        this.kakaoId = kakaoId;
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void update(String nick) {
        this.nick = nick;
    }


}