/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.MotifFindingSolver;
import artificial_bee_colony.postprocessor.StringMotifResult;
import dna_sequence.DNASequence;
import java.sql.Date;
import java.util.HashMap;
import java.util.HashSet;


/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
class ParallelTest {
    
    private final DNASequence[] sequences;
    private final int runNumber;
    private final Configuration cfg;
    private final MotifFindingSolver solver;
    private final HashSet<String> resultSet  = new HashSet<>();
    
    public ParallelTest(DNASequence[] sequences, int runNumber, Configuration cfg, MotifFindingSolver solver){
    
        this.sequences = sequences;
        this.runNumber = runNumber;
        this.cfg = cfg;
        this.solver = solver;
    
    }
    
    public ParallelTest test() {
    
        final Thread currentThread = Thread.currentThread();
        final String oldName = currentThread.getName();
        
        currentThread.setName(oldName+": "+getCfgName()+" -> "+sequences[0].getName()+" run: "+getRunNumber());
        
        System.out.println(currentThread.getName()+"\n"
        +"started search");
        
        
        StringMotifResult results[] = solver.solve(sequences);
        
        for(StringMotifResult r:results){
        
            getResultSet().add(r.toString());
        
        }
        
        
        System.out.println(currentThread.getName()+"\n"
        +"finished search");
        
        currentThread.setName(oldName);
        
        return this;        
    
    }

    public String getSequenceName(){
    
        return sequences[0].getName();
    
    }
    
    /**
     * @return the runNumber
     */
    public int getRunNumber() {
        return runNumber;
    }

    /**
     * @return the cfgName
     */
    public String getCfgName() {
        return this.cfg.name;
    }

    /**
     * @return the resultSet
     */
    public HashSet<String> getResultSet() {
        return resultSet;
    }
    
}
