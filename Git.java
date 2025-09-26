import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {

    public static void generateFiles() {
        boolean created = true;
        File git = new File("git");
        if (!git.isDirectory()) {
            git.mkdir();
            created = false;
        }


        File objects = new File(git.getPath() + "/objects");
        if (!objects.isDirectory()) {
            objects.mkdir();
            created = false;
        }

        File index = new File(git.getPath() + "/index");
        File HEAD = new File(git.getPath() + "/HEAD");
        if (!index.isFile()) {
            index.mkdir();
            created = false;
        }
        if (!HEAD.isFile()) {
            HEAD.mkdir();
            created = false;
        }
        if (!created) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
        }
    }

    public static void deleteFiles() {
        File git = new File("git");
        File objects = new File(git.getPath() + "/objects");
        File index = new File(git.getPath() + "/index");
        File HEAD = new File(git.getPath() + "/HEAD");
        index.delete();
        HEAD.delete();
        objects.delete();
        git.delete();
        System.out.println("Files have been deleted");

    }

    public static String sha1Hash(File file) throws IOException {
        // from geeksforgeeks
        try {
            StringBuilder s = new StringBuilder();
            if (file.exists()) {
                FileReader f = new FileReader(file);

                while (f.ready()) {
                    s.append((char) f.read());
                }
                f.close();

            }
            String input = s.toString();


            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 40 digits long
            while (hashtext.length() < 40) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
