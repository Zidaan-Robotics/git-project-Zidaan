import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GitWrapperTester {
    public static void main(String[] args) {
        GitWrapper github = new GitWrapper();
        github.init();
        File d1 = new File("FileFolder");
        File d2 = new File("FileFolder/subfolder");
        File t1 = new File("FileFolder/textfile1.txt");
        File t2 = new File("FileFolder/finalfile.txt");
        File t3 = new File("FileFolder/subfolder/textfile2.txt");
        File t4 = new File("FileFolder/subfolder/textfile3.txt");
        d1.mkdir();
        d2.mkdir();
        try {
            t1.createNewFile();
            t2.createNewFile();
            t3.createNewFile();
            t4.createNewFile();

            safelyPopulateFile(t4, "file4");
            safelyPopulateFile(t3, "file3");
            safelyPopulateFile(t2, "file2");
            safelyPopulateFile(t1, "file1");
        } catch (Exception e) {

        }



        try {
            github.add(t1.getPath());
            github.commit("Zidaan", "test 1");
            github.add(t2.getPath());
            github.commit("Zidaan", "test 2");
            github.add(t3.getPath());
            github.commit("Zidaan", "test 3");
            github.add(t4.getPath());
            github.commit("Zidaan", "test 4");

            // t1.delete();
            // t2.delete();
            // t3.delete();
            // t4.delete();
            // File dir = new File("FileFolder");
            // dir.delete();
            // dir = new File("FileFolder/subfolder");
            // dir.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static void safelyPopulateFile(File f, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
