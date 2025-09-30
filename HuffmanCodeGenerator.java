import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class HuffmanCodeGenerator {
    private HashMap<Character, Integer> map;
    private Tree tree;
    private String[] list;

    public HuffmanCodeGenerator(String frequencyfile) throws IOException {
        map = new HashMap<Character, Integer>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(frequencyfile));
            while (br.ready()) {
                char c = (char) br.read();
                add(c);
            }
            add((char) 26);
            br.close();


        } catch (FileNotFoundException e) {
            System.out.println("lo siento, file doesn't exist");
        }
        tree = Tree.makeTree(map);
        list = new String[128];
        Node root = tree.getRoot();
        recursiveNode(root.getRightChild(), "1");
        recursiveNode(root.getLeftChild(), "0");
    }

    public HashMap<Character, Integer> getMap() {
        return map;
    }

    private void add(char c) {
        Integer count = map.get(c);
        if (count == null) {
            map.put(c, 1);
        } else {
            map.replace(c, count + 1);
        }
    }

    public int getFrequency(char c) {
        Integer freq = map.get(c);
        if (freq == null) {
            return 0;
        }
        return freq;
    }

    public void makeCodeFile(String codeFile) {
        try {

            PrintWriter writer = new PrintWriter(codeFile);
            for (int i = 0; i < list.length; i++) {
                if (list[i] != null) {
                    writer.print(list[i].substring(0));
                }
                writer.println();
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println(e + " bad boy");
        }

    }

    public String getCode(char c) {
        return list[(int) c];

    }

    private void recursiveNode(Node node, String code) {
        if (node == null) {
            return;
        }
        if (node.getChar() != (char) 200) {
            list[(int) node.getChar()] = code;
        } else {
            recursiveNode(node.getRightChild(), code + "1");
            recursiveNode(node.getLeftChild(), code + "0");
        }

    }
}
