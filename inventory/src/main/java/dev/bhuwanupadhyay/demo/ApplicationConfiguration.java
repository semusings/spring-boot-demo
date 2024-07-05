package dev.bhuwanupadhyay.demo;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.utils.Constants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableConfigurationProperties
public class ApplicationConfiguration {

  @Bean
  public OpenAPI applicationOpenAPI(ApplicationProperties properties) {
    //@formatter:on
    Info info = new Info()
        .title(properties.getSpringdoc().title())
        .description(properties.getSpringdoc().description())
        .version(properties.getSpringdoc().version())
        .license(
            new License()
                .name(properties.getSpringdoc().license().name())
                .url(properties.getSpringdoc().license().url())
        );

    ExternalDocumentation externalDocs = new ExternalDocumentation()
        .description(properties.getSpringdoc().externalDoc().description())
        .url(properties.getSpringdoc().externalDoc().url());

    return new OpenAPI().info(info).externalDocs(externalDocs);
    //@formatter:off
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      JwtTokenFilter jwtTokenFilter,
      @Qualifier("ignoredRequestMatchers") RequestMatcher[] ignoredRequestMatchers
  ) throws Exception {
    //@formatter:on
    return http
        // Enable CORS and disable CSRF
        .cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)

        // Set session management to stateless
        .sessionManagement(configurer -> {
          configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        })

        // Set unauthorized requests exception handler
        .exceptionHandling(configurer -> {
          configurer.authenticationEntryPoint((request, response, authException) -> {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
          });
        })

        // Set permissions on endpoints
        .authorizeHttpRequests(registry -> {
          registry
              // Our public endpoints
              .requestMatchers(ignoredRequestMatchers).permitAll()
              // Our private endpoints
              .requestMatchers("/api/**").authenticated()
              // for role-based - registry.requestMatchers("/api/**").hasRole(Role.SYSTEM_ADMIN);
              .anyRequest().authenticated();
        })

        // Add JWT token filter
        .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

        // build
        .build();
    //@formatter:off
  }

  @Bean
  public JwtTokenFilter jwtTokenFilter(@Qualifier("ignoredRequestMatchers") RequestMatcher[] ignoredRequestMatchers) {
    return new JwtTokenFilter(ignoredRequestMatchers);
  }

  @Bean
  @Qualifier("ignoredRequestMatchers")
  public RequestMatcher[] ignoredRequestMatchers(
      SpringDocConfigProperties springDocConfigProperties,
      WebEndpointProperties webEndpointProperties) {
    List<RequestMatcher> requestMatchers = new ArrayList<>();

    String swaggerUiPath = Constants.SWAGGER_UI_PREFIX + "/**";
    requestMatchers.add(new AntPathRequestMatcher(swaggerUiPath));

    String swaggerUiHtmlPath = Constants.DEFAULT_SWAGGER_UI_PATH;
    requestMatchers.add(new AntPathRequestMatcher(swaggerUiHtmlPath));

    String apiDocsPath = springDocConfigProperties.getApiDocs().getPath() + "/**";
    requestMatchers.add(new AntPathRequestMatcher(apiDocsPath, HttpMethod.GET.name()));

    String managementEndpointsPath = webEndpointProperties.getBasePath() + "/**";
    requestMatchers.add(new AntPathRequestMatcher(managementEndpointsPath, HttpMethod.GET.name()));

    return requestMatchers.toArray(new RequestMatcher[0]);
  }

}