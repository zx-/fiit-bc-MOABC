/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class Configuration {
    
    public static Configuration getFromJson(String s){
    
        Gson gson = new Gson();
        Configuration c = gson.fromJson(s, Configuration.class);
        
        return c;
    
    }
    public static Configuration getFromJsonFile(File f) throws IOException{
    
        return getFromJson(new String(Files.readAllBytes(Paths.get(f.getPath()))));
    
    }
    
    public String createJson(){
    
        Gson gson = new Gson();
        return gson.toJson(this, Configuration.class);
    
    }
    
    public String name = "Default Config";
    public String currentTestName = "";
    public int numberOfRepetitionsPerInput = 1;
    
    public int motifMaxLength = 64; // 64
    public int motifMinLength = 7;
    public int maxIterations = 1000;
    public int onlookersSize = 10;//100
    public int workersSize = 10;//100
    public int scoutsSize = 1;//20
    public double supportTreshold = 0.5;
    public double motifMinComplexity = 0.5;
    
    public int numberOfSequentions = 12;
    public int lengthOfSequence = 200;
    public Level logLevel = Level.OFF;
    
    public int OBLFepsilon = 15;
    public double OBLFbeta = 2;
    
    public double jumpRate = 0.1;
    public double perturbationRate = 0.2;
    
    public double stepC = 1.5;
    public double OBLFstepMultiplier = 2;
    
    public String getCurrentResultName(){
    
        return this.name+"-"+this.currentTestName;
    
    }

    public int getMinSupport(int numOfSequences) {
     
        if( numOfSequences < 5 )
            return 2;
        
        return 3;
        
    }


    /**
     * @return the colonySize
     */
    public int getColonySize() {
        return workersSize + scoutsSize + onlookersSize;
    }
    

    
}
