package english.longman.comm3000.a1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class Extractor {
    List<FreqWord> freqWords = new ArrayList<FreqWord>();
    
    public Extractor() throws IOException {
        /*
         * prior knowledge
         * 1) there are maximum 2 tokens forming a word
         * 2) there are 2 types of POS (part of speech) containing more 1 token: definite article, indefinite article
         * 
         */
        
        
        FreqWord freqWord;

        Set<String> posSet = new TreeSet<String>();
        posSet.add("adj");
        posSet.add("adv");
        posSet.add("auxiliary");
        posSet.add("conj");
        posSet.add("definite");
        posSet.add("determiner");
        posSet.add("indefinite");
        posSet.add("interjection");
        posSet.add("modal"); 
        posSet.add("n");
        posSet.add("number");
        posSet.add("predeterminer");
        posSet.add("prep");
        posSet.add("pron");
        posSet.add("v");

        Set<String> distinctWord = new TreeSet<String>();
        List<String> duplicatedWord = new ArrayList<String>();

        String path = this.getClass().getResource("comm3000.modified.txt").getFile();        
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line, token, word="", pos, register;
        
        List<String> tokens;
        List<String> poss;
        List<String> registers;        
        while ((line = br.readLine()) != null) {
            tokens = new ArrayList<String>();
            poss = new ArrayList<String>();
            registers = new ArrayList<String>();        
            
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                token = st.nextToken();
            
                if (token.endsWith(",")) {
                    token = token.substring(0, token.length()-1);
                }
                tokens.add(token);
            }
            
            
            int i=0;
            while (i<tokens.size()) {            
                //form word
                if (word.equals("")) {
                    if (!posSet.contains(tokens.get(i+1))) {
                        word = tokens.get(i) + " " + tokens.get(i+1);
                        i++;
                        i++;
                    }
                    else {
                        word = tokens.get(i);
                        i++;
                    }
                }
                
                //form pos
                if (tokens.get(i).equals("definite") || tokens.get(i).equals("indefinite")) {
                    pos = tokens.get(i) + " " + tokens.get(i+1);                    
                    poss.add(pos);
                    i++;
                    i++;
                }
                else {
                    if (posSet.contains(tokens.get(i))) {
                        pos = tokens.get(i);                    
                        poss.add(pos);
                        i++;
                    }
                    //form register
                    else {
                        register = tokens.get(i);       
                        registers.add(register);
                        i++;
                    }
                }
            }

            if (distinctWord.contains(word)) {
                duplicatedWord.add(word);
            }
            distinctWord.add(word);
            
            
            System.out.println("===== " + line + " =====");
            System.out.println(word);
            System.out.println(poss);
            System.out.println(registers);

            freqWord = new FreqWord();
            freqWord.setWord(word);
            freqWord.setPoss(poss);
            freqWord.setRegisters(registers);            
            freqWords.add(freqWord);            
            
            word="";            
        }
        
        System.out.println(duplicatedWord);
    }
        
    public List<FreqWord> getFreqWords() {
        return freqWords;
    }
    
    
    public static void main(String[] args) throws IOException {
        Extractor s = new Extractor();
    }
}
