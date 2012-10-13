package english.longman.comm3000.a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class MostFrequentSpokenVerb {
    public MostFrequentSpokenVerb() throws IOException {
        String path = this.getClass().getResource("comm3000.txt").getFile();
        
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line, word, pos, type;
        int num=0;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            word = st.nextToken();
            pos = st.nextToken();
            type = st.nextToken();
                        
            if (pos.equals("v") && (type.startsWith("S1"))) {
                System.out.println(line);
                num++;
            }
        }
        System.out.println(num);
    }
        
    public static void main(String[] args) throws IOException {
        new MostFrequentSpokenVerb();
    }
}