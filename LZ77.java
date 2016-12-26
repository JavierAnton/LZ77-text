/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lz77;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier
 */
public class LZ77 {
    public static int SLIDER_SIZE = 2048;
    public static int ENTRY_SIZE = 124;    
    
    private static StringBuffer encode(){
        StringBuffer encoded = new StringBuffer(); //The whole file coded
        StringBuffer binary_encoded = new StringBuffer();
        File file = new File("binary.txt");
        BufferedReader reader;
        String slider;
        String entry;
        
        /*See if file is valid*/
        if (!file.exists()){
            System.err.println("File does not exists");
        }
        
        /*Test if SLIDER_SIZE is bigger than ENTRY_SIZE */
        if (ENTRY_SIZE > SLIDER_SIZE){
            int aux = SLIDER_SIZE;
            SLIDER_SIZE = ENTRY_SIZE;
            ENTRY_SIZE = aux;
        }if( (SLIDER_SIZE%2 != 0) || (ENTRY_SIZE%2 != 0)){
            SLIDER_SIZE = 8;
            ENTRY_SIZE = 4;
        }
        
        int Mdes = (int) (Math.log(SLIDER_SIZE)/Math.log(2));
        int Ment = (int) (Math.log(ENTRY_SIZE)/Math.log(2));
                
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = reader.readLine(); 
            int size = text.length();
            
            char nextSimbol;
            String match;
            int matchIndx;
            int currIndx;
            int matchLenght;
            String strIndx;
            String strLenght;
            boolean stop;
            
            encoded.append(text.substring(0, SLIDER_SIZE));//From x to y-1)
            binary_encoded.append(text.substring(0, SLIDER_SIZE));
            int pos = 0;
            // While there are characters
            while ( pos <= size-SLIDER_SIZE-ENTRY_SIZE ){                
                slider = text.substring(pos, pos+SLIDER_SIZE);//From x to y-1
                entry = text.substring(pos+SLIDER_SIZE, pos+SLIDER_SIZE+ENTRY_SIZE); 
                
                //System.out.println("Slider :"+ slider);
                //System.out.println("Entrada :"+ entry);
                
                /*Restart values*/ 
                match = "";
                matchIndx = 0;
                matchLenght = 0;
                stop = false;
                /*Finds the match*/
                while (matchLenght<ENTRY_SIZE && !stop){
                    nextSimbol = entry.charAt(matchLenght);
                    currIndx = slider.indexOf(match + nextSimbol);
                    
                    //System.out.println(currIndx);
                    if ( currIndx == -1 ){
                        stop = true;
                    }else{
                        match = match + nextSimbol;
                        matchIndx = currIndx;
                        matchLenght++;
                    }
                }
                if (matchLenght != 0){
                    //Save in alpha-numeric format
                    match = "("+matchLenght+","+(SLIDER_SIZE-matchIndx)+")";
                    encoded.append(match);
                    
                    //Save in binary format
                    strLenght = Integer.toBinaryString(matchLenght);
                    strIndx = Integer.toBinaryString(SLIDER_SIZE-matchIndx);
                    while(strLenght.length() < Ment){
                        strLenght = "0"+strLenght;
                    }
                    while(strIndx.length() < Mdes){
                        strIndx = "0"+strIndx;
                    }
                    /*If the string is to long*/
                    strLenght = strLenght.substring(strLenght.length() - Ment);
                    strIndx = strIndx.substring(strIndx.length() - Mdes);
                    /*binary encode*/
                    binary_encoded.append(strLenght+strIndx);
                    
                    pos = pos + matchLenght;
                }else{
                    //Save in alpha-numeric format
                    encoded.append(entry.charAt(0));
                    
                    /*Save like a fixed error
                     *2bits at distance 1
                     *To decode, find the bit that not occurs in SLIDER
                     */
                    String aux = ""; 
                    if(Ment==1){
                        aux = "0"; //2 bits
                    }else{
                        aux = "10";
                        while(aux.length() < Mdes){
                            aux = "0"+aux;
                        }
                    }
                    binary_encoded.append(aux);
                    aux = "1"; //means the maximun distance
                    while(aux.length() < Mdes){
                        aux = "0"+aux;
                    }
                    binary_encoded.append(aux);
                    
                    pos++;
                }
            }
            
            /*The last bits*/
            int trash = size - SLIDER_SIZE - pos;
            if (trash > 0){
                encoded.append(text.substring(size-trash));
                
                binary_encoded.append(text.substring(size-trash,size));
            }
            
            //System.out.println("String: "+ encoded);
            System.out.println("Binary: "+ binary_encoded);
            
            System.out.println("Original size: "+ size);
            System.out.println("Binary size: "+ binary_encoded.length());
            System.out.println("Compress factor:" + (size/(float)binary_encoded.length()) );  
            
            
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return binary_encoded;
    }
    
    
    private static StringBuffer encode_text(StringBuffer text){
        StringBuffer encoded = new StringBuffer(); //The whole file coded
        StringBuffer binary_encoded = new StringBuffer();
        String slider;
        String entry;
        
        /*Test if SLIDER_SIZE is bigger than ENTRY_SIZE */
        if (ENTRY_SIZE > SLIDER_SIZE){
            int aux = SLIDER_SIZE;
            SLIDER_SIZE = ENTRY_SIZE;
            ENTRY_SIZE = aux;
        }if( (SLIDER_SIZE%2 != 0) || (ENTRY_SIZE%2 != 0)){
            SLIDER_SIZE = 8;
            ENTRY_SIZE = 4;
        }
        
        int Mdes = (int) (Math.log(SLIDER_SIZE)/Math.log(2));
        int Ment = (int) (Math.log(ENTRY_SIZE)/Math.log(2));
                
        
        int size = text.length();

        char nextSimbol;
        String match;
        int matchIndx;
        int currIndx;
        int matchLenght;
        String strIndx;
        String strLenght;
        boolean stop;

        encoded.append(text.substring(0, SLIDER_SIZE));//From x to y-1)
        binary_encoded.append(text.substring(0, SLIDER_SIZE));
        int pos = 0;
        // While there are characters
        while ( pos <= size-SLIDER_SIZE-ENTRY_SIZE ){                
            slider = text.substring(pos, pos+SLIDER_SIZE);//From x to y-1
            entry = text.substring(pos+SLIDER_SIZE, pos+SLIDER_SIZE+ENTRY_SIZE); 

            //System.out.println("Slider :"+ slider);
            //System.out.println("Entrada :"+ entry);

            /*Restart values*/ 
            match = "";
            matchIndx = 0;
            matchLenght = 0;
            stop = false;
            /*Finds the match*/
            while (matchLenght<ENTRY_SIZE && !stop){
                nextSimbol = entry.charAt(matchLenght);
                currIndx = slider.indexOf(match + nextSimbol);

                //System.out.println(currIndx);
                if ( currIndx == -1 ){
                    stop = true;
                }else{
                    match = match + nextSimbol;
                    matchIndx= currIndx;
                    matchLenght++;
                }
            }
            if (matchLenght != 0){
                //Save in alpha-numeric format
                match = "("+matchLenght+","+(SLIDER_SIZE-matchIndx)+")";
                encoded.append(match);

                //Save in binary format
                strLenght = Integer.toBinaryString(matchLenght);
                strIndx = Integer.toBinaryString(SLIDER_SIZE-matchIndx);
                while(strLenght.length() < Ment){
                    strLenght = "0"+strLenght;
                }
                while(strIndx.length() < Mdes){
                    strIndx = "0"+strIndx;
                }
                /*If the string is to long*/
                strLenght = strLenght.substring(strLenght.length() - Ment);
                strIndx = strIndx.substring(strIndx.length() - Mdes);
                /*binary encode*/
                binary_encoded.append(strLenght+strIndx);

                pos = pos + matchLenght;
            }else{
                //Save in alpha-numeric format
                encoded.append(entry.charAt(0));

                /*Save like a fixed error
                 *2bits at distance 1
                 *To decode, find the bit that not occurs in SLIDER
                 */
                String aux = ""; 
                if(Ment==1){
                    aux = "0"; //2 bits
                }else{
                    aux = "10";
                    while(aux.length() < Mdes){
                        aux = "0"+aux;
                    }
                }
                binary_encoded.append(aux);
                aux = "1"; //means the maximun distance
                while(aux.length() < Mdes){
                    aux = "0"+aux;
                }
                binary_encoded.append(aux);

                pos++;
            }
        }

        /*The last bits*/
        int trash = size - SLIDER_SIZE - pos;
        if (trash > 0){
            encoded.append(text.substring(size-trash));

            binary_encoded.append(text.substring(size-trash,size));
        }

        //System.out.println("String: "+ encoded);
        System.out.println("Binary: "+ binary_encoded);

        System.out.println("Original size: "+ size);
        System.out.println("Binary size: "+ binary_encoded.length());
        System.out.println("Compress factor:" + (size/(float)binary_encoded.length()) );

        return binary_encoded;
    }
    
    
    
    
    /**
     * Decodes the file entry
     */
    private static void decode(){
        StringBuffer binary_decoded = new StringBuffer();
        File file = new File("binary_encoded.txt");
        BufferedReader reader;
        String slider;
        
        /*See if file is valid*/
        if (!file.exists()){
            System.err.println("File does not exists");
        }
        
        /*Test if SLIDER_SIZE is bigger than ENTRY_SIZE */
        if (ENTRY_SIZE > SLIDER_SIZE){
            int aux = SLIDER_SIZE;
            SLIDER_SIZE = ENTRY_SIZE;
            ENTRY_SIZE = aux;
        }if( (SLIDER_SIZE%2 != 0) || (ENTRY_SIZE%2 != 0)){
            SLIDER_SIZE = 8;
            ENTRY_SIZE = 4;
        }
        
        int Mdes = (int) (Math.log(SLIDER_SIZE)/Math.log(2));
        int Ment = (int) (Math.log(ENTRY_SIZE)/Math.log(2));
                
        try {
            reader = new BufferedReader(new FileReader(file));
            String text = reader.readLine(); 
            int size = text.length();
            
            String match = null;
            int matchIndx;
            int matchLenght;
            String strIndx;
            String strLenght;
            int checkMatchIndx;
            
            binary_decoded.append(text.substring(0, SLIDER_SIZE));
            int pos = SLIDER_SIZE;
            int sld_pos = 0;
            // While there are characters 
            while ( pos <= size-Mdes-Ment){                
                slider = binary_decoded.substring(sld_pos, sld_pos+SLIDER_SIZE);//From x to y-1
                //System.out.println("Slider :"+ slider);
                
                /*Takes the next match size and index*/
                strLenght = text.substring(pos, pos+Ment);
                strIndx = text.substring(pos+Ment, pos+Ment+Mdes);
                //System.out.println("SLD_P: "+ sld_pos+"/ S-I: "+strLenght+" "+strIndx);

                matchLenght = Integer.parseInt( strLenght, 2);
                matchIndx = Integer.parseInt( strIndx, 2);
                matchIndx = SLIDER_SIZE-matchIndx;
                //Cases of last number
                if (matchLenght == 0){
                    matchLenght = ENTRY_SIZE;
                }
                if (matchIndx == SLIDER_SIZE){
                    matchIndx = 0;
                }
                //ERROR, we have a single no-match bit
                //So it wasn't a match
                //System.out.println(slider+" "+matchLenght +" "+matchIndx );
                if(matchLenght==2 && matchIndx==SLIDER_SIZE-1){
                    //System.out.println(slider + " "+matchLenght +" "+matchIndx );
                    checkMatchIndx = slider.indexOf("0");
                    if (checkMatchIndx == -1){
                        binary_decoded.append("0");
                        sld_pos ++;
                    }else{
                        binary_decoded.append("1");
                        sld_pos ++;
                    }
                    pos += Mdes+Ment;
                }else{
                    try{
                        match = slider.substring( matchIndx, matchIndx+matchLenght );
                        binary_decoded.append(match);
                        sld_pos += matchLenght;
                        pos += Mdes+Ment;
                    }catch(StringIndexOutOfBoundsException ex){
                        match = slider.substring( matchIndx);
                        //System.out.println(match+" "+ matchIndx+" "+sld_pos+" "+pos);
                        //sld_pos += matchLenght;
                        pos += 1;
                    }
                }
                
            }
            /*The last bits*/
            int trash = size-pos;
            if (trash > 0){
                //System.out.println(text.substring(size-trash));
                binary_decoded.append(text.substring(size-trash));
            }
            
            
            System.out.println("Decode: "+ binary_decoded);
            //System.out.println("Decode size: "+binary_decoded.length());
        }catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private static void decode_text(StringBuffer text){
        StringBuffer binary_decoded = new StringBuffer();
        String slider;
        
        /*Test if SLIDER_SIZE is bigger than ENTRY_SIZE */
        if (ENTRY_SIZE > SLIDER_SIZE){
            int aux = SLIDER_SIZE;
            SLIDER_SIZE = ENTRY_SIZE;
            ENTRY_SIZE = aux;
        }if( (SLIDER_SIZE%2 != 0) || (ENTRY_SIZE%2 != 0)){
            SLIDER_SIZE = 8;
            ENTRY_SIZE = 4;
        }
        
        int Mdes = (int) (Math.log(SLIDER_SIZE)/Math.log(2));
        int Ment = (int) (Math.log(ENTRY_SIZE)/Math.log(2));
        
        int size = text.length();

        char nextSimbol;
        String match = null;
        int matchIndx;
        int matchLenght;
        String strIndx;
        String strLenght;
        int checkMatchIndx;


        binary_decoded.append(text.substring(0, SLIDER_SIZE));
        int pos = SLIDER_SIZE;
        int sld_pos = 0;
        // While there are characters 
        while ( pos <= size-Mdes-Ment){                
            slider = binary_decoded.substring(sld_pos, sld_pos+SLIDER_SIZE);//From x to y-1
            //System.out.println("Slider :"+ slider);

            /*Takes the next match size and index*/
            strLenght = text.substring(pos, pos+Ment);
            strIndx = text.substring(pos+Ment, pos+Ment+Mdes);
            //System.out.println("SLD_P: "+ sld_pos+"/ S-I: "+strLenght+" "+strIndx);

            matchLenght = Integer.parseInt( strLenght, 2);
            matchIndx = Integer.parseInt( strIndx, 2);
            matchIndx = SLIDER_SIZE-matchIndx;
            //Cases of last number
            if (matchLenght == 0){
                matchLenght = ENTRY_SIZE;
            }
            if (matchIndx == SLIDER_SIZE){
                matchIndx = 0;
            }
            //ERROR, we have a single no-match bit
            //So it wasn't a match
            //System.out.println(slider+" "+matchLenght +" "+matchIndx );
            if(matchLenght==2 && matchIndx==SLIDER_SIZE-1 ){
                //System.out.println(slider + " "+matchLenght +" "+matchIndx );
                checkMatchIndx = slider.indexOf("0");
                if (checkMatchIndx == -1){
                    binary_decoded.append("0");
                    sld_pos ++;
                }else{
                    binary_decoded.append("1");
                    sld_pos ++;
                }
            }else{
                try{
                    match = slider.substring( matchIndx, matchIndx+matchLenght );
                    binary_decoded.append(match);
                    sld_pos += matchLenght;
                    pos += Mdes+Ment;
                }catch(StringIndexOutOfBoundsException ex){
                    match = slider.substring( matchIndx);
                    //System.out.println(match+" "+ matchIndx+" "+sld_pos+" "+pos);
                    //sld_pos += matchLenght;
                    pos += 1;
                }
            }
        }
        /*The last bits*/
        int trash = size-pos;
        if (trash > 0){
            binary_decoded.append(text.substring(size-trash));
        }


        System.out.println("Decode: "+ binary_decoded);
        //System.out.println("Decode size: "+binary_decoded.length());
    }
    
    public static void saveToFile(StringBuffer encoded){
        /*SAVE ENCODING IN A FILE*/
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("binary_encoded.txt", "UTF-8");
            writer.println(encoded);
            writer.close();  
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LZ77.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LZ77.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        StringBuffer text = txtReader.cargarTxt("hamlet_short.txt");
        StringBuffer encoded = null;
        
        double t1 = System.nanoTime();
        encoded = encode_text(text);
        //encode();
        double t2 = System.nanoTime();
        saveToFile(encoded);
        
        double t3 = System.nanoTime();
        decode();
        double t4 = System.nanoTime();
        
        double encode_time = ((t2-t1)/Math.pow(10, 6)); //ms
        double decode_time = ((t4-t3)/Math.pow(10, 6)); //ms
        System.out.println("Code time :"+ encode_time+"ms");
        System.out.println("Decode time :"+ decode_time+"ms");
        
        
    }
        
}
