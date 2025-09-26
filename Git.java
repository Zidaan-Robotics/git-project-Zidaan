import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Git {

    public static File git = new File("git");
    public static File objects = new File(git.getPath() + "/objects");
    public static File index = new File(git.getPath() + "/index");
    public static File HEAD = new File(git.getPath() + "/HEAD");


    public static void generateFiles() throws IOException{
        boolean created = true;
        if (!git.isDirectory()) {
            git.mkdir();
            created = false;
        }


        if (!objects.isDirectory()) {
            objects.mkdir();
            created = false;
        }


        if (!index.isFile()) {
            index.createNewFile();
            created = false;
        }
        if (!HEAD.isFile()) {
            HEAD.createNewFile();
            created = false;
        }
        if (!created) {
            System.out.println("Git Repository Created");
        } else {
            System.out.println("Git Repository Already Exists");
        }
    }

    public static void deleteFiles() {
        index.delete();
        HEAD.delete();
        File[] objFiles = objects.listFiles();
        for (File f : objFiles) {
            f.delete();
        }
        objects.delete();
        objFiles = git.listFiles();
        for (File f : objFiles) {
            f.delete();
        }
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

    public static void createBlob(File file) throws IOException{
        String hash = sha1Hash(file);
        File blob = new File(objects.getPath() + "/" + hash);
        blob.createNewFile();
        FileWriter writer = new FileWriter(blob);
        writer.write(hash);
        writer.close();


    }

}
