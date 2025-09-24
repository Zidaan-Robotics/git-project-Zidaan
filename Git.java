import java.io.File;

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

}
