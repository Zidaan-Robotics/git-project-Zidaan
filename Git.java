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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterOutputStream;

public class Git {

    public static File git = new File("git");
    public static File objects = new File(git.getPath() + "/objects");
    public static File index = new File(git.getPath() + "/index");
    // gonna use FileName, blob hash
    private static HashMap<String, String> indexMap = new HashMap<String, String>();
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
        if (readFile(index).contains(hash + " " + relativePath(file))) {
            return;
        }
        File blob = new File(objects.getPath() + "/" + hash);
        blob.createNewFile();
        FileWriter writer = new FileWriter(blob);
        writer.write(readFile(file));
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

    private static String relativePath(File f) {
        // return f.getAbsolutePath().substring(f.getAbsolutePath().indexOf("git-project"));
        return f.getPath();
    }

    public static void updateIndex(File file, String hash) throws IOException {
        indexMap.put(relativePath(file), hash);
        FileWriter writer = new FileWriter(index);
        int ctr = 0;
        for (Map.Entry<String, String> entry : indexMap.entrySet()) {
            if (new File(entry.getKey()).isFile()) {
                if (ctr == 0) {
                    writer.write((entry.getValue() + " " + entry.getKey()));
                    ctr++;
                } else {
                    writer.write(("\n" + entry.getValue() + " " + entry.getKey()));
                }
            }
        }
        writer.close();

    }

    public static String createTree(File dir) throws IOException {
        // fix all paths to not include git-proejct
        StringBuilder str = new StringBuilder();
        File[] files = dir.listFiles();
        for (File f : files) {
            if (f.equals(files[0])) {
                if (f.isFile()) {
                    createBlob(f);

                    str.append("blob ").append(indexMap.get(relativePath(f))).append(" ")
                            .append(relativePath(f));
                } else if (f.isDirectory()) {
                    createTree(f);
                    str.append("tree ").append(indexMap.get(relativePath(f))).append(" ")
                            .append(relativePath(f));
                }
            } else {
                if (f.isFile()) {
                    createBlob(f);
                    str.append("\nblob ").append(indexMap.get(relativePath(f))).append(" ")
                            .append(relativePath(f));
                } else if (f.isDirectory()) {
                    createTree(f);
                    str.append("\ntree ").append(indexMap.get(relativePath(f))).append(" ")
                            .append(relativePath(f));
                }
            }
        }
        String hash = sha1Hash(str.toString());
        File tree = new File(objects.getPath() + "/" + hash);
        FileWriter treeWriter = new FileWriter(tree);
        treeWriter.write(str.toString());
        indexMap.put(relativePath(dir), sha1Hash(str.toString()));
        treeWriter.close();
        return hash;

    }

    private static String subPath(String path) {
        return path.substring(0, path.lastIndexOf("\\"));
    }

    private static int slashes(String path) {
        int ctr = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == ('\\')) {
                ctr++;
            }
        }
        return ctr;
    }

    public static String treeFromIndex() throws IOException {
        String indexString = readFile(index);
        String[] workingLines = indexString.split("\n");
        ArrayList<String> workingList = new ArrayList<String>(Arrays.asList(workingLines));
        for (int i = 0; i < workingList.size(); i++) {
            workingList.set(i,
                    workingList.get(i).substring(41) + " " + workingList.get(i).substring(0, 40));
        }
        // Set<String> hashKeys = indexMap.keySet();
        // for (String i : hashKeys) {
        // workingList.add(i);
        // }
        Collections.sort(workingList);
        // for (int i = 0; i < workingList.size(); i++) {
        // System.out.println(workingList.get(i));
        // }
        if (workingList.size() == 1) {
            createTree(new File(subPath(workingList.get(0))));
            String[] splitStr = workingList.get(0).split(" ");
            String finalThing = "blob " + indexMap.get(splitStr[0]) + " " + splitStr[0];
            String finalHash = sha1Hash(finalThing);
            File finalFile = new File("git/objects/" + finalHash);
            finalFile.createNewFile();
            FileWriter w = new FileWriter(finalFile);

            w.write("blob " + indexMap.get(splitStr[0]) + " " + splitStr[0]);
            w.close();
            return finalHash;
        }

        while (workingList.size() > 1) {
            int maxSlashes = 0;
            for (int i = 0; i < workingList.size(); i++) {
                int currentSlashes = slashes(workingList.get(i));
                if (currentSlashes > maxSlashes) {
                    maxSlashes = currentSlashes;
                }
            }
            for (int i = 0; i < workingList.size(); i++) {
                ArrayList<String> runningTotal = new ArrayList<String>();
                int currentSlashes = slashes(workingList.get(i));
                if (currentSlashes == maxSlashes) {
                    if (runningTotal.isEmpty()) {
                        runningTotal.add(workingList.remove(i));
                    } else {
                        if (subPath(runningTotal.get(0)).equals(subPath(workingList.get(i)))) {
                            runningTotal.add(workingList.remove(i));
                        }
                    }
                } else {
                    if (!runningTotal.isEmpty()) {
                        workingList.add(subPath(runningTotal.get(0)));
                        createTree(new File(subPath(runningTotal.get(0))));
                        runningTotal = new ArrayList<String>();
                        Collections.sort(workingList);
                        i--;
                    }
                }
                if (i == workingList.size() - 1) {
                    if (!runningTotal.isEmpty()) {
                        workingList.add(subPath(runningTotal.get(0)));
                        createTree(new File(subPath(runningTotal.get(0))));
                        Collections.sort(workingList);
                    }
                }
            }
        }
        String finalThing = "tree " + indexMap.get(workingList.get(0)) + " " + workingList.get(0);
        String finalHash = sha1Hash(finalThing);
        File finalFile = new File("git/objects/" + finalHash);
        finalFile.createNewFile();
        FileWriter w = new FileWriter(finalFile);
        w.write("tree " + indexMap.get(workingList.get(0)) + " " + workingList.get(0));
        w.close();

        return finalHash;


    }

    public static String makeCommit(String author, String message) throws IOException {
        StringBuilder commitString = new StringBuilder();
        // tree
        commitString.append("tree: ");
        commitString.append(treeFromIndex());
        commitString.append("\n");

        // parent
        try {
            StringBuilder head = new StringBuilder();
            FileReader r = new FileReader("git/HEAD");
            while (r.ready()) {
                head.append((char) r.read());
            }
            r.close();
            if (!head.isEmpty()) {
                commitString.append("parent: ");
                commitString.append(head);
                commitString.append("\n");
            }
        } catch (IOException e) {
            System.out.println("aight bro just run the initializeRepo() already");
        }

        // author
        commitString.append("author: ");
        commitString.append(author);
        commitString.append("\n");

        // date
        Date date = new Date();
        commitString.append("date: ");
        commitString.append(date);
        commitString.append("\n");

        // message
        commitString.append("message: ");
        commitString.append(message);

        String hash = sha1Hash(commitString.toString());
        File commitObj = new File("git/objects/" + hash);
        try {
            commitObj.createNewFile();
            FileWriter headWriter = new FileWriter("git/HEAD");
            headWriter.write(hash);
            headWriter.close();
            FileWriter commitWriter = new FileWriter(commitObj);
            commitWriter.write(commitString.toString());
            commitWriter.close();
            return hash;
        } catch (IOException e) {
            System.out.println("idk man it's joever");
        }

        return null;
    }



}
