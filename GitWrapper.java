
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class GitWrapper {

    /**
     * Initializes a new Git repository. This method creates the necessary directory structure and
     * initial files (index, HEAD) required for a Git repository.
     */
    public void init() {
        // to-do: implement functionality here
        try {
            Git.generateFiles();
            Git.deleteFiles();
            Git.generateFiles();
        } catch (IOException e) {
            System.out.println("throwing fr");
        }

    };

    /**
     * Stages a file for the next commit. This method adds a file to the index file. If the file
     * does not exist, it throws an IOException. If the file is a directory, it throws an
     * IOException. If the file is already in the index, it does nothing. If the file is
     * successfully staged, it creates a blob for the file.
     * 
     * @param filePath The path to the file to be staged.
     */
    public void add(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.isFile()) {
            throw new IOException(filePath + " is not a file");
        }
        if (file.isDirectory()) {
            throw new IOException(filePath + " is a directory");
        }

        Git.createBlob(file);
    };

    /**
     * Creates a commit with the given author and message. It should capture the current state of
     * the repository by building trees based on the index file, writing the tree to the objects
     * directory, writing the commit to the objects directory, updating the HEAD file, and returning
     * the commit hash.
     * 
     * The commit should be formatted as follows: tree: <tree_sha> parent: <parent_sha> author:
     * <author> date: <date> summary: <summary>
     *
     * @param author The name of the author making the commit.
     * @param message The commit message describing the changes.
     * @return The SHA1 hash of the new commit.
     */
    public String commit(String author, String message) {
        try {
            return Git.makeCommit(author, message);
        } catch (IOException e) {
            System.out.println("throwing fr");
            return null;
        }

    }


    /**
     * EXTRA CREDIT: Checks out a specific commit given its hash. This method should read the HEAD
     * file to determine the "checked out" commit. Then it should update the working directory to
     * match the state of the repository at that commit by tracing through the root tree and all its
     * children.
     *
     * @param commitHash The SHA1 hash of the commit to check out.
     */
    public void checkout(String commitHash) throws IOException {
        File commit = new File("git/objects/" + commitHash);
        StringBuilder commitContents = new StringBuilder();
        FileReader commitReader = new FileReader(commit);
        while (commitReader.ready()) {
            commitContents.append((char) commitReader.read());
        }
        commitReader.close();
        File tree = new File("git/objects/" + commitContents.substring(6, 46));
        checkOutLine(readFile(tree.getPath()));

    };

    private void checkOutLine(String line) throws IOException {
        if (getTypeOfLine(line).equals("blob")) {
            File file = new File(getPathOfLine(line));
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(readFile(getPathOfLine(line)));
        } else {
            File directory = new File(getPathOfLine(line));
            directory.mkdir();
            BufferedReader r =
                    new BufferedReader(new FileReader("git/objects/" + getHashOfLine(line)));
            while (r.ready()) {
                checkOutLine(r.readLine());
            }
            r.close();
        }
    }

    private String getPathOfLine(String line) {
        return line.substring(46);
    }

    private String getHashOfLine(String line) {
        return line.substring(5, 45);
    }

    private String getTypeOfLine(String line) {
        return line.substring(0, 4);
    }

    private String readFile(String s) throws IOException {
        File f = new File(s);
        FileReader reader = new FileReader(f);
        StringBuilder returnString = new StringBuilder();
        while (reader.ready()) {
            returnString.append((char) reader.read());
        }
        return returnString.toString();
    }
}
