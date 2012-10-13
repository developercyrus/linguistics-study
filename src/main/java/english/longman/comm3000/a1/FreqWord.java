package english.longman.comm3000.a1;

import java.util.List;

public class FreqWord {
    private String word;
    private List<String> poss;
    private List<String> registers;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<String> getPoss() {
        return poss;
    }

    public void setPoss(List<String> poss) {
        this.poss = poss;
    }

    public List<String> getRegisters() {
        return registers;
    }

    public void setRegisters(List<String> registers) {
        this.registers = registers;
    }
}