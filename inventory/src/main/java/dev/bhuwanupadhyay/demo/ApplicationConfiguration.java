package dev.bhuwanupadhyay.demo;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.utils.Constants;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableConfigurationProperties
public class ApplicationConfiguration {

  private final ApplicationProperties properties;
  private final SpringDocConfigProperties springDocConfigProperties;

  public ApplicationConfiguration(ApplicationProperties properties,
      SpringDocConfigProperties springDocConfigProperties) {
    this.properties = properties;
    this.springDocConfigProperties = springDocConfigProperties;
  }

  @Bean
  public OpenAPI applicationOpenAPI() {

    Info info = new Info().title(properties.getSpringdoc().title())
        .description(properties.getSpringdoc().description())
        .version(properties.getSpringdoc().version()).license(
            new License().name(properties.getSpringdoc().license().name())
                .url(properties.getSpringdoc().license().url()));

    ExternalDocumentation externalDocs = new ExternalDocumentation().description(
            properties.getSpringdoc().externalDoc().description())
        .url(properties.getSpringdoc().externalDoc().url());

    return new OpenAPI().info(info).externalDocs(externalDocs);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    String apiDocsPath = springDocConfigProperties.getApiDocs().getPath() + "/**";
    String swaggerUiPath = Constants.SWAGGER_UI_PREFIX + "/**";
    String swaggerUiHtmlPath = Constants.DEFAULT_SWAGGER_UI_PATH;

    String[] ignoredMatchers = new String[]{apiDocsPath, swaggerUiPath, swaggerUiHtmlPath};

    http.authorizeHttpRequests(
            (requests) -> requests.requestMatchers(ignoredMatchers).permitAll().anyRequest()
                .authenticated()).formLogin((form) -> form.loginPage("/login").permitAll())
        .logout(LogoutConfigurer::permitAll);

    return http.build();
  }

}