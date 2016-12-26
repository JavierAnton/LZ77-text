/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package createsequence;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier
 */
public class CreateSequence {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuffer sequency = new StringBuffer();
        HashMap<String, Double> items = new HashMap<>();
        
        items.put("D", 0.3);
        items.put("K", 0.2);
        items.put("Q", 0.2);
        items.put("J", 0.15);
        items.put("10", 0.10);
        items.put("9", 0.05);
        
        // TODO code application logic here
        for (int i=0; i<10000000; i++){
            String s = randomChoice(items);
            sequency.append(s);
        }
        System.out.println(sequency);
        PrintWriter writer;
        try {
            writer = new PrintWriter("huffman.txt", "UTF-8");
            writer.println(sequency);
            writer.close();       
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CreateSequence.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(CreateSequence.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
    //Generates the random item
    public static String randomChoice(HashMap<String, Double> items){
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for(String sKey : items.keySet()) {
            cumulativeProbability += items.get(sKey);
            //System.out.println(p + " <=? " + cumulativeProbability + " FREQ:"+items.get(sKey));
            if (p <= cumulativeProbability) {
                return sKey;
            }
        }
        return null;
    }
}

