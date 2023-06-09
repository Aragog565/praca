package sklep.config.security;


import sklep.repository.UserRepository;
import sklep.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final String SECRET = "FADR5257234GSDFADRQEYUIOPUMZs523578FAFYGyteqq1358jkncbjoyu8967jdcar$#FDAd344314";

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 5; // ms

    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Autowired
    public JwtTokenProvider(CustomUserDetailsService userDetailsService, UserRepository userRepository){
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public String createToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        Long id = this.userRepository.findByEmail(username).getId();
        return Jwts.builder().setClaims(claims)
                .setSubject(String.valueOf(id))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public Authentication getAuthentication(String token){
        String id = getSubjectFromToken(token);
        if(id == null || isTokenExpired(token)) return null;
        UserDetails userDetails = userDetailsService.loadUserById(Long.valueOf(id));
        if(userDetails == null){
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());

    }

    public Date getExpirationDateFromToken(String token){
        return getAllClaimsFromToken(token).getExpiration();
    }

    public String getSubjectFromToken(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parser().setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }
}
