import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class FileAccess {

    void StoreLogin(String userName, String encryptedPassword, String salt) throws Exception {
        UserInfo user = new UserInfo(encryptedPassword, userName, salt);
        JSONParser parser = new JSONParser();
        JSONObject item = new JSONObject();
        item.put("passwordHash", user.passwordHash);
        item.put("salt", user.salt);
        JSONObject currentData = new JSONObject();

        try {
            FileReader reader = new FileReader("users.json");
            currentData = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            ;
        }
        currentData.put(user.username, item);

        try {
            FileWriter file = new FileWriter("users.json");
            file.write(currentData.toJSONString());
            file.close();
        } catch (Exception e) {
            ;
        }
    }
    UserInfo FetchUser(String userName) throws IOException, ParseException {
        try {
            FileReader reader = new FileReader("users.json");
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject current = (JSONObject) jsonObject.get(userName);
            if (current == null) {
                return (null);
            }
            return(new UserInfo(current.get("passwordHash").toString(), userName, current.get("salt").toString()));
        } catch (Exception e) {
            return (null); //File doesnt exist, return null.
        }
    }

}
