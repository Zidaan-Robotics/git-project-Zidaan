import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class HuffmanDecoder {

    private HashMap<String, Character> values;

    public HuffmanDecoder(String codeFile) throws IOException {
        values = new HashMap<String, Character>();
        BufferedReader br = new BufferedReader(new FileReader(codeFile));
        int ctr = 0;
        while (br.ready()) {
            values.put(br.readLine(), (char) ctr);
            ctr += 1;
        }

    }

    public boolean isCode(String binary) {
        return values.containsKey(binary);
    }

    public char decodeChar(String binary) {
        return values.get(binary);
    }

    public void decodeLong(String encodedFile, String decodedFile) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(encodedFile));
        StringBuilder str = new StringBuilder();
        StringBuilder returnString = new StringBuilder();
        boolean end = false;
        while (br.ready() && !end) {
            if (!end) {
                str.append((char) br.read());
                if (isCode(str.toString())) {
                    returnString.append(decodeChar(str.toString()));
                    if (returnString.charAt(returnString.length() - 1) == (char) 26) {
                        returnString = new StringBuilder(
                                returnString.substring(0, returnString.length() - 1));
                        end = true;
                    }
                    str = new StringBuilder();
                }
            }

        }
        br.close();
        PrintWriter writer = new PrintWriter(decodedFile);
        writer.write(returnString.toString());
        writer.close();
    }

    public void decodeFile(String encodedFile) throws IOException {
        String decodedFile = "";
        if (encodedFile.substring(encodedFile.length() - 4).equals(".huf")) {
            decodedFile = encodedFile.substring(0, encodedFile.length() - 4);
        } else {
            throw new IllegalArgumentException();
        }
        BufferedReader br = new BufferedReader(new FileReader(encodedFile));
        StringBuilder str = new StringBuilder();
        StringBuilder returnString = new StringBuilder();
        StringBuilder binaryString = new StringBuilder();
        boolean end = false;
        while (br.ready()) {
            char c = (char) br.read();
            String binary = Integer.toBinaryString(c);
            while (binary.length() < 8) {
                binary = "0" + binary;
            }
            binaryString.append(binary);
        }
        for (int i = 0; i < binaryString.length(); i++) {
            if (!end) {
                str.append(binaryString.charAt(i));
                if (isCode(str.toString())) {
                    returnString.append(decodeChar(str.toString()));
                    if (returnString.charAt(returnString.length() - 1) == (char) 26) {
                        returnString = new StringBuilder(
                                returnString.substring(0, returnString.length() - 1));
                        end = true;
                    }
                    str = new StringBuilder();
                }
            }

        }
        br.close();
        PrintWriter writer = new PrintWriter(decodedFile);
        writer.write(returnString.toString());
        writer.close();
    }


}
