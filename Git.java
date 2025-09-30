import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;


public class Git {

    public static File git = new File("git");
    public static File objects = new File(git.getPath() + "/objects");
    public static File index = new File(git.getPath() + "/index");
    private static String indexString = "";
    public static File HEAD = new File(git.getPath() + "/HEAD");
    public static boolean compression = false;

    public static String readFile(File file) throws IOException {
        FileReader r = new FileReader(file);
        StringBuilder str = new StringBuilder();
        while (r.ready()) {
            str.append((char) r.read());
        }
        r.close();
        return str.toString();
    }


    public static void generateFiles() throws IOException {
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

    public static String sha1Hash(String input) throws IOException {
        // from geeksforgeeks

        try {
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

    public static void createBlob(File file) throws IOException {
        String hash;
        if (compression) {
            hash = sha1Hash(compressAndEncodeBase64(readFile(file)));
        } else {
            hash = sha1Hash(readFile(file));
        }
        File blob = new File(objects.getPath() + "/" + hash);
        blob.createNewFile();
        FileWriter writer = new FileWriter(blob);
        writer.write(hash);
        writer.close();
        updateIndex(file, hash);


    }

    // stackoverflow
    public static String compressAndEncodeBase64(String text) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (DeflaterOutputStream dos = new DeflaterOutputStream(os)) {
                dos.write(text.getBytes());
            }
            byte[] bytes = os.toByteArray();

            return new String(Base64.getEncoder().encode(bytes));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String decompressB64(String compressedAndEncodedText) {
        try {
            byte[] decodedText = Base64.getDecoder().decode(compressedAndEncodedText);

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try (OutputStream ios = new InflaterOutputStream(os)) {
                ios.write(decodedText);
            }
            byte[] decompressedBArray = os.toByteArray();
            return new String(decompressedBArray, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static void updateIndex(File file, String hash) throws IOException {
        FileWriter writer = new FileWriter(index);
        if (!indexString.equals("")) {
            indexString += "\n" + hash + " " + file.getName();
        } else {
            indexString += hash + " " + file.getName();
        }
        writer.write(indexString);
        writer.close();

    }



}
