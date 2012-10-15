package english.longman.comm3000.a1;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Selector {
    public Selector() throws ClassNotFoundException, SQLException, IOException {
        Extractor e = new Extractor();
        List<FreqWord> freqWords = e.getFreqWords();
        
        
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:src/main/resources/english/longman/comm3000/a1/db;", "SA", "");
        conn.createStatement().executeUpdate("drop table if exists word");
        conn.createStatement().executeUpdate("drop table if exists pos");
        conn.createStatement().executeUpdate("drop table if exists register");
        conn.createStatement().executeUpdate("create table word (id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, order_id INTEGER, head VARCHAR(45))");
        conn.createStatement().executeUpdate("create table pos (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, word_id INTEGER, type VARCHAR(45))");
        conn.createStatement().executeUpdate("create table register (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, word_id INTEGER, rank VARCHAR(45))");
        
        PreparedStatement ps0 = conn.prepareStatement("select MAX(order_id) from word where head = ?");
        PreparedStatement ps1 = conn.prepareStatement("insert into word (order_id, head) values (?, ?)");
        PreparedStatement ps2 = conn.prepareStatement("select MAX(id) from word"); 
        PreparedStatement ps3 = conn.prepareStatement("insert into pos (word_id, type) values (?, ?)");
        PreparedStatement ps4 = conn.prepareStatement("insert into register (word_id, rank) values (?, ?)");
        //PreparedStatement ps5 = conn.prepareStatement("select w.head, w.order_id, p.type, r.rank from word w inner join pos p on w.id = p.word_id inner join register r on w.id = r.word_id order by w.id");
        //PreparedStatement ps5 = conn.prepareStatement("select w.head, w.order_id, p.type, r.rank from word w inner join pos p on w.id = p.word_id inner join register r on w.id = r.word_id where p.type = 'v' and (r.rank = 'S1' or r.rank = 'W1') order by w.id");
        //PreparedStatement ps5 = conn.prepareStatement("select w.head, w.order_id, p.type, r.rank from word w inner join pos p on w.id = p.word_id inner join register r on w.id = r.word_id where p.type = 'v' order by w.id, r.rank");
        PreparedStatement ps5 = conn.prepareStatement("select w.head, w.order_id, p.type, r.rank from word w inner join pos p on w.id = p.word_id inner join register r on w.id = r.word_id where p.type = 'v' and w.head in (select head from word w inner join pos p on w.id = p.word_id where p.type = 'n') order by w.id, r.rank");
        
        int word_id, order_id;
        ResultSet rs0, rs2, rs5;

        for (FreqWord freqWord : freqWords) {
            word_id=0;
            order_id=0;
            
            ps0.setString(1, freqWord.getWord());
            rs0 = ps0.executeQuery();       
            while(rs0.next()){
                order_id = rs0.getInt(1);           
            }
            order_id++;
            
            ps1.setInt(1, order_id);
            ps1.setString(2, freqWord.getWord());
            ps1.executeUpdate();
            
            rs2 = ps2.executeQuery();       
            while(rs2.next()){
                word_id = rs2.getInt(1);           
            }

            List<String> poss = freqWord.getPoss();
            for (String pos : poss) {
                ps3.setInt(1, word_id);
                ps3.setString(2, pos);        
                ps3.executeUpdate();    
            }
            
            List<String> registers = freqWord.getRegisters();
            for (String register : registers) {
                ps4.setInt(1, word_id);
                ps4.setString(2, register);        
                ps4.executeUpdate();    
            }
        }
        
        rs5 = ps5.executeQuery();       
        while(rs5.next()){
            System.out.printf("%-18s %d %-18s %s %n", rs5.getString(1), rs5.getInt(2), rs5.getString(3), rs5.getString(4));
        }
            
        // persistent record to db.script
        conn.createStatement().execute("SHUTDOWN"); 
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        Selector s = new Selector();
    }
}