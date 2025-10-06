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

(GP 2.3.2)
Tester:
    Updated to include new methods in the tester
    Added a returnTestFiles method to allow testing without recreating the test files
Git:
    Added Compression and Decompression methods
    Updates the hash method to take in a String instead, and using the new readFile method to read the File contents to a String
    Added a variable called compression, which determines whether or not to use compression.


(GP 2.4) 
Git:
    Added a String keeping track of the index contents. Added an updateIndex file that updates the index.

(GP 2.4.1)
Made a test case generating 50 files, they seem to match and the index seems to work.

(GP 2.4.2)
Already completed tasks in previous steps while testing by accident lol

(GP 3.1)
Replaced indexString with a Hashmap to make changing and removing files easy. Also adjusted the index file to add the word "blob" to the front, and changed the fileName to the relative directory. Also added a method to get the relative directory of a file.

(GP 3.2)
Added a createTree method to recursively create a tree given a directory name. Returns the hash of the tree. 