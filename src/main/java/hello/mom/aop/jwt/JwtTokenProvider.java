package hello.mom.aop.jwt;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret}")
    private String secretKey;

    private static final long TOKEN_VALID_TIME = 1000L * 60 * 1440;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String userPk, String nick, Long memberid) {
        Claims claims = Jwts.claims().setSubject(userPk);

        Map<String, Object> claimsMap = new HashMap<String, Object>();
        claimsMap.put("username", userPk);
        claimsMap.put("nick", nick);
        claimsMap.put("memberid", memberid);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claimsMap)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("username", String.class);
    }

    public String getNick(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("nick", String.class);
    }

    public Long getMemberid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("memberid", Long.class);
    }

    public static String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }


    public final String HEADER_PREFIX = "Bearer ";


    public String extract(String header) {
        if (header == null || header.equals("") || header.length() < HEADER_PREFIX.length()) {
            throw new NoSuchElementException("올바른 JWT 정보가 아닙니다.");
        }

        return header.substring(
                HEADER_PREFIX.length(),
                header.length()
        );
    }

}