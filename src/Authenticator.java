import java.io.File;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import org.json.simple.JSONObject;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;


public class Authenticator {
    FileAccess file = new FileAccess();
    JSONObject userDatabase = new JSONObject();
    public boolean AuthenticateUser(String userName, String inputPass) throws Exception {
        UserInfo inputUser = file.FetchUser(userName);
        if (inputUser == null) {
            return false;
        } else {
            String salt = inputUser.salt;
            String calculatedHash = getEncryptedPassword(inputPass, salt);
            if (calculatedHash.equals(inputUser.passwordHash)) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void CreateUser(String username, String passWord) throws Exception {
        String salt = getNewSalt();
        String encryptedPassword = getEncryptedPassword(passWord, salt);
        file.StoreLogin(username, encryptedPassword, salt);
    }

    public String getEncryptedPassword(String password, String salt) throws Exception {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160; // for SHA1
        int iterations = 20000; // NIST specifies 10000

        byte[] saltBytes = Base64.getDecoder().decode(salt);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        byte[] encBytes = f.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(encBytes);
    }

    // Returns base64 encoded salt
    public String getNewSalt() throws Exception {
        // Don't use Random!
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // NIST recommends minimum 4 bytes. We use 8.
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

}
