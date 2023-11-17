import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

//Desgining MOT  
class Tuple {

    String mnemonic, bin_opcode, type;
    int length;

    Tuple() {
    }

    Tuple(String s1, String s2, String s3, String s4) {
        mnemonic = s1;
        bin_opcode = s2;
        length = Integer.parseInt(s3);
        type = s4;
    }
}
//Desgining ST

class SymTuple {

    String symbol, ra;
    int value, length;

    SymTuple(String s1, int i1, int i2, String s2) {
        symbol = s1;
        value = i1;
        length = i2;
        ra = s2;
    }
}
//Designing Literal

class LitTuple {

    String literal, ra;
    int value, length;

    LitTuple() {
    }

    LitTuple(String s1, int i1, int i2, String s2) {
        literal = s1;
        value = i1;
        length = i2;
        ra = s2;
    }
}
//Actual Code

public class AssemblerPass1 {

    static int lc;
    static List<Tuple> mot; 
    static List<String> pot; 
    static List<SymTuple> symtable;  
    static List<LitTuple> littable; 
    static List<Integer> lclist;
    static Map<Integer, Integer> basetable; 
    static PrintWriter out_pass2; 
    static PrintWriter out_pass1; 
    static int line_no;

    public static void main(String[] args) throws Exception {
        initializeTables(); 
        System.out.println("====== PASS 1 ======\n");
        pass1(); //Run Pass 1

        
        PrintWriter lclistWriter = new PrintWriter(new FileWriter("/home/student/Desktop/T1954235/output file/lclist.txt"), true); //generate ST
        for (int i = 0; i < lclist.size(); i++) {
            lclistWriter.println(lclist.get(i));
        }
        lclistWriter.close();
    }

    static void initializeTables() throws Exception {
        symtable = new LinkedList<>();
        littable = new LinkedList<>();
        lclist = new ArrayList<>();
        basetable = new HashMap<>();
        mot = new LinkedList<>();
        pot = new LinkedList<>();
        String s;
        BufferedReader br;
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/student/Desktop/T1954235/input file/mot.txt")));//reading MOT
        while ((s = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(s, " ", false); //convert line into tokens
            mot.add(new Tuple(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken())); //adding token into list
        }
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/home/student/Desktop/T1954235/input file/pot.txt")));//reading POT
        while ((s = br.readLine()) != null) {
            pot.add(s);
        }
        Collections.sort(pot); 
    }
    //Pass 1 Srarts here

    static void pass1() throws Exception {
        //Read Input file
        BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream("/home/student/Desktop/T1954235/input file/input.txt")));
        out_pass1 = new PrintWriter(new FileWriter("/home/student/Desktop/T1954235/output file/output_pass1.txt"), true); //writing to Output file pass1
        PrintWriter out_symtable = new PrintWriter(new FileWriter("/home/student/Desktop/T1954235/output file/out_symtable.txt"), true); //generate ST
        PrintWriter out_littable = new PrintWriter(new FileWriter("/home/student/Desktop/T1954235/output file/out_littable.txt"), true); //generate LT
        String s;
        while ((s = input.readLine()) != null) { 
            StringTokenizer st = new StringTokenizer(s, " ", false);
            String s_arr[] = new String[st.countTokens()]; 
            for (int i = 0; i < s_arr.length; i++) {
                s_arr[i] = st.nextToken(); 
            }
            if (searchPot1(s_arr) == false) { 
                searchMot1(s_arr); 
                out_pass1.println(s); 
            }
            lclist.add(lc); 
        }
        int j; 
        String output = new String(); 
        System.out.println("Symbol Table:");
        System.out.println("Symbol    Value  Length   R/A");
        for (SymTuple i : symtable) { 
            output = i.symbol; 
            for (j = i.symbol.length(); j < 10; j++) { 
                output += " ";
            }
            output += i.value;
            for (j = new Integer(i.value).toString().length(); j < 7; j++) {
                output += " ";
            }
            output += i.length + "        " + i.ra; 
            System.out.println(output);
            out_symtable.println(output);
        }
        System.out.println("\nLiteral Table:");
        System.out.println("Literal   Value  Length   R/A");
        for (LitTuple i : littable) { 
            output = i.literal;
            for (j = i.literal.length(); j < 10; j++) {
                output += " ";
            }
            output += i.value;
            for (j = new Integer(i.value).toString().length(); j < 7; j++) {
                output += " ";
            }
            output += i.length + "        " + i.ra;
            System.out.println(output);
            out_littable.println(output);
        }
    }

    static boolean searchPot1(String[] s) {
        int i = 0; 
        int l = 0; 
        int potval = 0; 

        if (s.length == 3) {
            i = 1;
        }
        s = tokenizeOperands(s); 

        if (s[i].equalsIgnoreCase("DS") || s[i].equalsIgnoreCase("DC")) {
            potval = 1; //if DC or DS
        }
        if (s[i].equalsIgnoreCase("EQU")) {
            potval = 2; //if EQU
        }
        if (s[i].equalsIgnoreCase("START")) {
            potval = 3; //if START
        }
        if (s[i].equalsIgnoreCase("LTORG")) {
            potval = 4; //if LTORG
        }
        if (s[i].equalsIgnoreCase("END")) {
            potval = 5; //if END
        }

        switch (potval) { 
            case 1:
                
                String x = s[i + 1]; 
                int index = x.indexOf("F"); 
                if (i == 1) {
                    symtable.add(new SymTuple(s[0], lc, 4, "R"));
                }
                if (index != 0) {
                    // Ends with F 
                    l = Integer.parseInt(x.substring(0, x.length() - 1));
                    l *= 4;
                } else {
                    // Starts with F
                    for (int j = i + 1; j < s.length; j++) {
                        l += 4;
                    }
                }
                lc += l; //update LC
                return true;

            case 2:
                
                if (!s[2].equals("*")) { 
                    symtable.add(new SymTuple(s[0], Integer.parseInt(s[2]), 1, "A"));
                } else {
                    symtable.add(new SymTuple(s[0], lc, 1, "R"));
                }
                return true;

            case 3:
                
                symtable.add(new SymTuple(s[0], Integer.parseInt(s[2]), 1, "R"));  
                return true;

            case 4:
                
                ltorg(false); 
                return true;

            case 5:
               
                ltorg(true); 
                return true;
        }
        return false;
    }

    static void searchMot1(String[] s) {
        Tuple t = new Tuple(); //MOT object
        int i = 0;
        if (s.length == 3) { 
            i = 1; 
        }
        s = tokenizeOperands(s); 
        for (int j = i + 1; j < s.length; j++) {
            if (s[j].startsWith("=")) { //check if literal
                littable.add(new LitTuple(s[j].substring(1, s[j].length()), -1, 4, "R")); 
            }
        }
        if ((i == 1) && (!s[0].equalsIgnoreCase("END"))) { 
            symtable.add(new SymTuple(s[0], lc, 4, "R")); 
        }
        for (Tuple x : mot) { //traverse all MOTs
            if (s[i].equals(x.mnemonic)) { 
                t = x; 
                break;
            }
        }
        lc += t.length; 
    }

    static String[] tokenizeOperands(String[] s) {
        List<String> temp = new LinkedList<>(); 
        for (int j = 0; j < s.length - 1; j++) { 
            temp.add(s[j]);
        }
        StringTokenizer st = new StringTokenizer(s[s.length - 1], " ,", false); 
        while (st.hasMoreTokens()) {
            temp.add(st.nextToken()); 
        }
        s = temp.toArray(new String[0]); 
        return s;
    }

    static void ltorg(boolean isEnd) { 
        Iterator<LitTuple> itr = littable.iterator();  
        LitTuple lt = new LitTuple(); 
        boolean isBroken = false; 
        while (itr.hasNext()) { 
            lt = itr.next(); 
            if (lt.value == -1) {
                isBroken = true;
                break;
            }
        }
        if (!isBroken) { //if LTORG occurs
            return;
        }
        if (!isEnd) { //if not END
            while (lc % 8 != 0) {
                lc++; //reach up to END statement
            }
        }
        lt.value = lc;
        lc += 4;
        while (itr.hasNext()) {
            lt = itr.next(); 
            lt.value = lc; 
            lc += 4; 
        }
    }
}
