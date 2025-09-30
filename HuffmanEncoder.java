import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class HuffmanEncoder {

    private String[] values;

    public HuffmanEncoder(String codeFile) throws IOException {
        values = new String[128];
        BufferedReader br = new BufferedReader(new FileReader(codeFile));
        int ctr = 0;
        while (br.ready()) {
            values[ctr] = br.readLine();
            ctr += 1;
        }

    }

    public String encodeChar(char c) {
        return values[(int) c];
    }

    public static int convertFromBinary(String code) {
        return 128 * Integer.parseInt(Character.toString(code.charAt(0)))
                + 64 * Integer.parseInt(Character.toString(code.charAt(1)))
                + 32 * Integer.parseInt(Character.toString(code.charAt(2)))
                + 16 * Integer.parseInt(Character.toString(code.charAt(3)))
                + 8 * Integer.parseInt(Character.toString(code.charAt(4)))
                + 4 * Integer.parseInt(Character.toString(code.charAt(5)))
                + 2 * Integer.parseInt(Character.toString(code.charAt(6)))
                + 1 * Integer.parseInt(Character.toString(code.charAt(7)));
    }

    public void encodeLong(String fileToCompress, String encodedFile) throws IOException {
        try {
            StringBuilder str = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(fileToCompress));
            while (br.ready()) {
                str.append(encodeChar((char) br.read()));
            }
            br.close();
            str.append(encodeChar((char) 26));
            int len = str.length() % 8;
            len = 8 - len;
            for (int i = 0; i < len; i++) {
                str.append("0");
            }
            PrintWriter w = new PrintWriter(encodedFile);
            w.write(str.toString());
            w.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }

    public void encodeFile(String fileToCompress) throws IOException {
        String newFileName = fileToCompress + ".huf";
        try {
            StringBuilder str = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(fileToCompress));
            while (br.ready()) {
                str.append(encodeChar((char) br.read()));
            }
            br.close();
            str.append(encodeChar((char) 26));
            int len = str.length() % 8;
            len = 8 - len;
            for (int i = 0; i < len; i++) {
                str.append("0");
            }
            StringBuilder newStr = new StringBuilder();
            for (int i = 0; i < str.length() / 8; i++) {
                newStr.append((char) convertFromBinary((str.substring(i * 8, i * 8 + 8))));
            }
            PrintWriter w = new PrintWriter(newFileName);
            w.write(newStr.toString());
            w.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }


}
