package com.provectus.kafka.ui.config.auth;

import com.provectus.kafka.ui.util.WebClientConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.oidc.authentication.OidcAuthorizationCodeReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoderFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@ConditionalOnProperty(value = "auth.type", havingValue = "OAUTH2")
public class CustomOidcAuthenticationManagerBuilder {
  private WebClient webClient = new WebClientConfigurator().build();
  // Reuse customOidcUserService bean from `OAuthSecurityConfig`
  private ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;
  @Value("${auth.oauth2.client.okta.jwk-set-uri}")
  private String jwkUri;

  public CustomOidcAuthenticationManagerBuilder(ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService) {
    this.oidcUserService = oidcUserService;
  }

  public OidcAuthorizationCodeReactiveAuthenticationManager build() {
    // Use WebClient that respects proxy settings to get token
    WebClientReactiveAuthorizationCodeTokenResponseClient client =
        new WebClientReactiveAuthorizationCodeTokenResponseClient();
    client.setWebClient(webClient);

    // Use WebClient that respects proxy settings to get JWT
    ReactiveJwtDecoderFactory<ClientRegistration> idTokenDecoderFactory =
        (clientRegistration) -> NimbusReactiveJwtDecoder.withJwkSetUri(jwkUri)
            .webClient(webClient)
            .build();

    OidcAuthorizationCodeReactiveAuthenticationManager manager =
        new OidcAuthorizationCodeReactiveAuthenticationManager(client, oidcUserService);
    manager.setJwtDecoderFactory(idTokenDecoderFactory);

    return manager;
  }
}
