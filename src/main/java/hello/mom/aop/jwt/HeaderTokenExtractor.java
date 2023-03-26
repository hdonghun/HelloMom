package hello.mom.aop.jwt;


import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class HeaderTokenExtractor {

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