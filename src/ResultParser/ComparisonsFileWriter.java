/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultParser;

import artificial_bee_colony.Configuration;
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
public class ComparisonsFileWriter {
    
    public void write(ArrayList<Integer> comparisons,Configuration c){
    
        BufferedWriter writer;
        try {
            
            writer = new BufferedWriter( new FileWriter("results//comparisons-"+c.name));
            writer.write("t,var,count\n");
            Iterator<Integer> it = comparisons.iterator();
            int i = 0;
            while(it.hasNext()){
            
                for(SearchPhase s:SearchPhase.values()){
                
                    writer.write(i+","+s.toString()+"_dom,"+it.next()+"\n");
                
                }
                
                for(SearchPhase s:SearchPhase.values()){
                
                    writer.write(i+","+s.toString()+"_non_dom,"+it.next()+"\n");
                
                }
                
                
            
                i++;
            }
            
            writer.close();
            
        } catch (IOException ex) {
            Logger.getLogger(ComparisonsFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
