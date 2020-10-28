package app.security.authenticator;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticatorConfig {

    private final CredentialsAuthenticator credentialRepository;

    public AuthenticatorConfig(CredentialsAuthenticator credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    @Bean
    public GoogleAuthenticator googleAuthenticator() {
        GoogleAuthenticator googleAuthenticator = new GoogleAuthenticator();
        googleAuthenticator.setCredentialRepository(credentialRepository);
        return googleAuthenticator;
    }
}
