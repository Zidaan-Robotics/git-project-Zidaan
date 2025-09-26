import java.io.File;
import java.io.IOException;

public class Tester {
    public static void main(String[] args) {
        Git.generateFiles();
        Git.deleteFiles();
        Git.generateFiles();
        Git.generateFiles();
        Git.deleteFiles();
        Git.generateFiles();
        try {
            File f = new File("testfile.txt");
            Git.createBlob(f);
        } catch (IOException e) {
            System.out.println("somebody sold fr");
        }

    }
}
