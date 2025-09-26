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
            System.out.println(Git.sha1Hash(f));
        } catch (IOException e) {
            System.out.println("somebody sold fr");
        }

    }
}
