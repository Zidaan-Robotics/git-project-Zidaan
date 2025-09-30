import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Tester {
    public static void main(String[] args) {
        try {
            Git.compression = false;
            clearMakeFiles();
            deleteTestFiles(50);
            File[] files = createTestFiles(50);
            // File[] files = returnTestFiles(3);
            makeBlobs(files);
            checkBlobs(files);
        } catch (IOException e) {
            System.out.println("somebody sold fr");
        }

    }

    public static void clearMakeFiles() throws IOException {
        Git.generateFiles();
        Git.deleteFiles();
        Git.generateFiles();
        Git.generateFiles();
        Git.deleteFiles();
        Git.generateFiles();
    }

    public static void makeBlobs(File... files) throws IOException {
        for (File file : files) {
            Git.createBlob(file);
        }
    }

    public static void checkBlobs(File... files) throws IOException {
        for (File file : files) {
            if (Git.compression) {
                if (!(new File(Git.objects + "/"
                        + Git.sha1Hash(Git.compressAndEncodeBase64(Git.readFile(file)))))
                                .exists()) {
                    System.out.println(file.getPath() + " DOES NOT HAVE A BLOB");
                }
            } else {
                if (!(new File(Git.objects + "/" + Git.sha1Hash(Git.readFile(file)))).exists()) {
                    System.out.println(file.getPath() + " DOES NOT HAVE A BLOB");
                }
            }

        }
    }

    public static File[] createTestFiles(int num) throws IOException {
        File[] returnList = new File[num];
        for (int i = 0; i < num; i++) {
            File f = new File("test" + i + "file.txt");
            f.createNewFile();
            StringBuilder txt = new StringBuilder();
            FileWriter w = new FileWriter(f);
            for (int j = 0; j < 1000; j++) {
                txt.append((char) ((int) Math.floor((Math.random() * 256))));
            }
            w.write(txt.toString());
            w.close();
            returnList[i] = f;
        }
        return returnList;

    }

    public static File[] returnTestFiles(int num) {
        File[] returnList = new File[num];
        for (int i = 0; i < num; i++) {
            returnList[i] = new File("test" + i + "file.txt");
        }
        return returnList;
    }

    public static void deleteTestFiles(int num) {
        for (int i = 0; i < num; i++) {
            File f = new File("test" + i + "file.txt");
            if (f.exists()) {
                f.delete();
            }
        }
    }

}
