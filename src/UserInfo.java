public class UserInfo {
    String passwordHash;
    String username;
    String salt;
    UserInfo(String PasswordHash, String Username, String Salt) {
        passwordHash = PasswordHash;
        username = Username;
        salt = Salt;
    }
}