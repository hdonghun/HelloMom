package hello.mom.member.dto.request;


import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpiresIn;


    @Builder
    public TokenDto(String grantType, String accessToken, Long accessTokenExpiresIn, String refreshToken) {
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    public TokenDto() {
    }

}