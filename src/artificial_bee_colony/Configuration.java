/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony;

import java.util.logging.Level;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class Configuration {
    
    public int motifMaxLength = 64; // 64
    public int motifMinLength = 7;
    public int maxIterations = 2000;
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
