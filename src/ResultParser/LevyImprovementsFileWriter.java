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
public class LevyImprovementsFileWriter {

    public void write(ArrayList<Integer> levy,Configuration c){
    
        BufferedWriter writer;
        try {
            
            writer = new BufferedWriter( new FileWriter("results//levys//levys-"+c.getCurrentResultName()));
            writer.write("Improved,NotImproved\n");
            Iterator<Integer> it = levy.iterator();
            int i = 0;
            while(it.hasNext()){
            
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
