package Application;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Spravene podla navodu na stranke:
// https://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/


public class Password_hash {

    public Password_hash() {

    }

    public String getSecurePassword(String user_passw) {
        return getSecurePassw(user_passw);
    }

    private static String getSecurePassw(String passwordToHash) {
        String generatedPassword = null;

        try {

            MessageDigest md = MessageDigest.getInstance("MD5"); // Create MessageDigest instance for MD5

            byte[] bytes = md.digest(passwordToHash.getBytes()); //Get the hash's bytes

            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));

            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

}


