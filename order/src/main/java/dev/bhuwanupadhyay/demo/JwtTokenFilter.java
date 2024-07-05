package dev.bhuwanupadhyay.demo;

import static org.apache.commons.lang3.StringUtils.isEmpty;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


public class JwtTokenFilter extends OncePerRequestFilter {

  private final RequestMatcher[] ignoredRequestMatchers;

  public JwtTokenFilter(RequestMatcher[] ignoredRequestMatchers) {
    this.ignoredRequestMatchers = ignoredRequestMatchers;
  }


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws ServletException, IOException {
    // Get authorization header and validate
    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (isEmpty(header) || !header.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    // Get jwt token and validate
    final String token = header.split(" ")[1].trim();
    if (!JwtTokenUtil.validateToken(token)) {
      chain.doFilter(request, response);
      return;
    }

    String email = JwtTokenUtil.getUserEmailFromToken(token);

    // Set user identity on the spring security context
    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
        email, null, Collections.emptyList());

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return new OrRequestMatcher(ignoredRequestMatchers).matches(request);
  }

  public static class JwtTokenUtil {

    public static String getUserEmailFromToken(String token) {
      return getClaimFromToken(token, "email", String.class);
    }

    public static String getUsernameFromToken(String token) {
      return getClaimFromToken(token, "preferred_username", String.class);
    }

    public static boolean validateToken(String token) {
      return (!isEmpty(getUsernameFromToken(token)) && !isEmpty(getUserEmailFromToken(token))
          && !isTokenExpired(token));
    }

    private static <T> T getClaimFromToken(String token, String claimKey, Class<?> type) {
      Map<String, Object> claims = getClaimObjectFromToken(token, JWTClaimsSet::getClaims);
      Object o = claims.get(claimKey);
      if (type.isInstance(o)) {
        return (T) o;
      }
      throw new InvalidTokenAuthenticationException(
          "Key " + claimKey + " does not match with expected return type " + type.getSimpleName());
    }

    private static <T> T getClaimObjectFromToken(String token,
        Function<JWTClaimsSet, T> claimsResolver) {
      final JWTClaimsSet claims = getAllClaimsFromToken(token);
      return claimsResolver.apply(claims);
    }

    private static JWTClaimsSet getAllClaimsFromToken(String token) {
      try {
        return SignedJWT.parse(token).getJWTClaimsSet();
      } catch (ParseException e) {
        throw new InvalidTokenAuthenticationException(
            "Invalid bearer token in authorization header!", e);
      }
    }

    private static Boolean isTokenExpired(String token) {
      final Date expiration = getExpirationDateFromToken(token);
      return expiration.before(new Date());
    }

    private static Date getExpirationDateFromToken(String token) {
      return getClaimObjectFromToken(token, JWTClaimsSet::getExpirationTime);
    }

    public static class InvalidTokenAuthenticationException extends AuthenticationException {

      public InvalidTokenAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
      }

      public InvalidTokenAuthenticationException(String msg) {
        super(msg);
      }

    }

  }

}