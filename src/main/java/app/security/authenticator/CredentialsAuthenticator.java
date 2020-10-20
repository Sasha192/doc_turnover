package app.security.authenticator;

import com.warrenstrange.googleauth.ICredentialRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CredentialsAuthenticator implements ICredentialRepository {
    private final Map<String, UserTotp> usersKeys = new HashMap<String, UserTotp>();

    @Override
    public String getSecretKey(String userName) {
        return usersKeys.get(userName).getSecretKey();
    }

    @Override
    public void saveUserCredentials(String userName,
                                    String secretKey,
                                    int validationCode,
                                    List<Integer> scratchCodes) {
        usersKeys.put(userName, new UserTotp(userName, secretKey, validationCode, scratchCodes));
    }

    public UserTotp getUser(String username) {
        return usersKeys.get(username);
    }

    class UserTotp {
        private String username;
        private String secretKey;
        private int validationCode;
        private List<Integer> scratchCodes;

        public UserTotp() {
        }

        public UserTotp(String username,
                        String secretKey,
                        int validationCode,
                        List<Integer> scratchCodes) {
            this.username = username;
            this.secretKey = secretKey;
            this.validationCode = validationCode;
            this.scratchCodes = scratchCodes;
        }

        public String getUsername() {
            return username;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public int getValidationCode() {
            return validationCode;
        }

        public List<Integer> getScratchCodes() {
            return scratchCodes;
        }
    }
}
