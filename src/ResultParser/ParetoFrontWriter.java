/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultParser;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ParetoFrontWriter {
    
    public void write(ArrayList<Double> fitnesses,ArrayList<Integer> sizes,int size){
    
        BufferedWriter writer;
        try {
            
            writer = new BufferedWriter( new FileWriter("results//paretoFrontSize-1"));
            writer.write("size\n");
            Iterator<Integer> it = sizes.iterator();
            while(it.hasNext()){
            
                writer.write(it.next()+"\n");
            
            }
            
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ComparisonsFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            
            writer = new BufferedWriter( new FileWriter("results//fitnesses-1"));
            writer.write("iteration,length,support,similarity\n");
            Iterator<Double> it = fitnesses.iterator();
            int i = 0;
            while(it.hasNext()){
            
                writer.write((int)(i/size)+",");
                writer.write(it.next()+",");
                writer.write(it.next()+",");
                writer.write(it.next()+"\n");
            
                i++;
                
            }
            
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ComparisonsFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
