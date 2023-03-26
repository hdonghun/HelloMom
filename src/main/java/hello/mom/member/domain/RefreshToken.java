package hello.mom.member.domain;


import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class RefreshToken {
    @Id
    @Column(nullable = false)
    private Long refreshTokenNo;

    @JoinColumn(name = "memberNo", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String value;


    public RefreshToken() {

    }

    @Builder
    public RefreshToken(Long refreshTokenNo, Member member, String value) {
        this.refreshTokenNo = refreshTokenNo;
        this.member = member;
        this.value = value;
    }

    public void updateValue(String token) {
        this.value = token;
    }
}