/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultParser;

import artificial_bee_colony.Configuration;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ResultWriter {

    private HashMap<String,StringBuilder> configStringBuilders = new HashMap<>();
    private HashMap<String,HashSet<String>> resultsByConf = new HashMap<>();
    
    void writeNewDataSet(Configuration currentConfiguration, String dnaDatasetName) {
    
        
        StringBuilder sb;

        if (configStringBuilders.containsKey(currentConfiguration.name)) {

            sb = configStringBuilders.get(currentConfiguration.name);

        } else {

            sb = new StringBuilder();
            configStringBuilders.put(currentConfiguration.name, sb);

        }
        //the true will append the new data
        sb.append(">dataset\n");//appends the string to the file
        sb.append(dnaDatasetName).append("\n");//appends the string to the file
        sb.append(">instances\n");
        
        
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
        
        if(set == null)
            return;
        
        StringBuilder sb = configStringBuilders.get(currentConfiguration.name);
        
       
        for(String s:set){
                
            sb.append(s+"\n");
            
        }
        
        set.clear();
    
    }
    void writeToFiles() {
        
        for(Entry<String,StringBuilder> entry:configStringBuilders.entrySet()){
        
            try {
                FileWriter fw = new FileWriter(
                        "results//assesmentResults//" + entry.getKey(),
                        false
                );
                
                fw.write(entry.getValue().toString());
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(ResultWriter.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        }
        
        
    }

    void reset() {
        
        configStringBuilders.clear();
        resultsByConf.clear();
        
    }

    
}
