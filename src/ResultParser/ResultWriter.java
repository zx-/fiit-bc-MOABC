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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ResultWriter {

    private HashMap<String,FileWriter> writers = new HashMap<>();
    private HashMap<String,HashSet<String>> resultsByConf = new HashMap<>();
    
    void writeNewDataSet(Configuration currentConfiguration, String dnaDatasetName) {
    
        try {
            
            FileWriter fw;
            
            if(writers.containsKey(currentConfiguration.name)){
                
                fw = writers.get(currentConfiguration.name);
        
            } else {
            
                fw = new FileWriter("results//assesmentResults//"+currentConfiguration.name,true);
                writers.put(currentConfiguration.name, fw);
            
            }            
             //the true will append the new data
            fw.write(">dataset\n");//appends the string to the file
            fw.write(dnaDatasetName+"\n");//appends the string to the file
            fw.write(">instances\n");
        }
        catch(IOException e){

            System.err.println("IOException: " + e.getMessage());
            
        }
        
    }

    void writeInstance(Configuration currentConfiguration, String result) {
        
        try {
            
            FileWriter fw;
            
            if(writers.containsKey(currentConfiguration.name)){
                
                fw = writers.get(currentConfiguration.name);
        
            } else {
            
                fw = new FileWriter(currentConfiguration.name,true);
                writers.put(currentConfiguration.name, fw);
            
            }            
            fw.write(result+"\n");
        }
        catch(IOException ioe){

            System.err.println("IOException: " + ioe.getMessage());
        }
        
    }
    
    void addInstance(Configuration currentConfiguration, String result){
    
        HashSet<String> set;
        if(resultsByConf.containsKey(currentConfiguration.name)){
        
            set = resultsByConf.get(currentConfiguration.name);
        
        } else {
        
            set = new HashSet<>();
            resultsByConf.put(currentConfiguration.name, set);
        
        }
        
        set.add(result);
    
    }
    
    void writeAddedInstances(Configuration currentConfiguration){
    
        HashSet<String> set = resultsByConf.get(currentConfiguration.name);
        
        if(set != null){
        
            for(String s:set){
                
                writeInstance(currentConfiguration, s);
            
            }
        
        }
    
    }

    void close(Configuration currentConfiguration) {
    
        if(writers.containsKey(currentConfiguration.name)){
                
            FileWriter fw = writers.get(currentConfiguration.name);
            writers.remove(currentConfiguration.name);
            try {
                fw.close();
            } catch (IOException ex) {
                System.err.println("IOException: " + ex.getMessage());
            }
        
        }
        
        resultsByConf.clear();
    
    }
    
}
