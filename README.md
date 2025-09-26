# git-project-Zidaan

(GP 2.1)
wassup guys
Files java class is used
Makes a method:
    first, we create a git folder
    then we create an objects folder
    then we create an index file inside the git folder
    then we create a HEAD file inside the git folder

Makes another method:
    deletes the 4 aforementioned files/directories

Both methods are tested in a new Tester.java file

(GP 2.2)
Added a sha1 hash method, which takes in a file and returns the sha1 hash of its contents.
I added a testfile.txt to test this with.

(GP 2.3)
Added a createblob method, which creats a file with contents and name equal to the hash code of the file passed into the method.
Cleaned up the tester:
    added the clearMakeFiles method which clears and makes the git, head, index, and objects folders
    added a makeBlobs method which makes blobs given Files
    added a checkBlobs method which checks if blobs for files exists
    added a createTestFiles method which creates testFiles and writes random content to them
    added a deleteTestFiles which deletes the testFiles.
Fixed a bug in 2.1 where index and HEAD were being created as folders, and fixed the delete files method in Git.java.

