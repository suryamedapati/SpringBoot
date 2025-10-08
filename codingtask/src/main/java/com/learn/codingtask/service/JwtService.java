
package com.learn.codingtask.service;

import com.learn.codingtask.dto.EmployeeResponseDTO;
import com.learn.codingtask.exception.CustomExceptions;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String SECRET_KEY; // stored in application.properties

     //Generate token with custom claims
    public String generateToken(EmployeeResponseDTO employee) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", employee.getUserName());
        claims.put("isActive",employee.isActive());
        claims.put("role",employee.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(employee.getUserName()) // sub = userName
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5 min expiry
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

//    // Validate token and extract policyNumber (subject)
    public String validateTokenAndGetUserName(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // userName
        } catch (Exception e) {
            return null; // invalid/expired
        }    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserRole(String token) {
        return (String) extractClaim(token, claims -> claims.get("role"));
    }


    public void checkAdmin(String token) {
        String role = extractUserRole(token);
        if (!"Admin".equalsIgnoreCase(role)) {
            throw new CustomExceptions.UnauthorizedException("Admins only");
        }
    }

    public void checkUserOrAdminForPolicy(String token, String userName) {
        String role = extractUserRole(token);
        String employeeUserName = extractUsername(token); // subject = policyNumber

        if ("Admin".equalsIgnoreCase(role)) {
            return; // Admin can access anything
        }

        if ("User".equalsIgnoreCase(role)) {
            if (!userName.equals(employeeUserName)) {
                throw new CustomExceptions.UnauthorizedException("Access denied! You can only view your own details.");
            }
            return;
        }
        throw new CustomExceptions.UnauthorizedException("Invalid role");
    }
public boolean checkUser(String token, String userName){
    String employeeUserName = extractUsername(token);
        if (!userName.equals(employeeUserName)) {
            throw new CustomExceptions.UnauthorizedException("Access denied! You can only apply leave for yourself.");
        }
        return true;

}
}

