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
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
class ParallelTest {
    
    private final DNASequence[] sequences;
    private final int repetition;
    private final Configuration cfg;
    private MotifFindingSolver solver;
    private final HashSet<String> resultSet  = new HashSet<>();
    private final int runId;
    
    public ParallelTest(DNASequence[] sequences, int runNumber,
            Configuration cfg, MotifFindingSolver solver, int runId){
    
        this.sequences = sequences;
        this.repetition = runNumber;
        this.cfg = cfg;
        this.solver = solver;
        this.runId = runId;
    
    }
    
    public ParallelTest test() {
    
        final Thread currentThread = Thread.currentThread();
        final String oldName = currentThread.getName();
        
        currentThread.setName(oldName+": "+getCfgName()+" -> "+sequences[0].getName()+" run: "+getRunRepetition());
        
        try {
            
            System.out.println(currentThread.getName()+"\n"
            +"started search");


            StringMotifResult results[] = solver.solve(sequences);
            solver = null;

            for(StringMotifResult r:results){

                getResultSet().add(r.toString());

            }        
        
            System.out.println(currentThread.getName()+"\n"
            +"finished search");
        
        } catch (Exception e) {
        
            String msg = "Logged while solving in thread:"
                    +"\n "+currentThread.getName();
            
            Logger.getLogger(ParallelTest.class.getName()).log(Level.SEVERE, msg, e);
        
        } finally {        
            
            currentThread.setName(oldName);
        
        }
        
        
        return this;        
    
    }

    public String getSequenceName(){
    
        return sequences[0].getName();
    
    }
    
    /**
     * @return the runNumber
     */
    public int getRunRepetition() {
        return repetition;
    }

    /**
     * @return the cfgName
     */
    public String getCfgName() {
        return this.getCfg().name;
    }

    /**
     * @return the resultSet
     */
    public HashSet<String> getResultSet() {
        return resultSet;
    }

    /**
     * @return the cfg
     */
    public Configuration getCfg() {
        return cfg;
    }

    /**
     * @return the runId
     */
    public int getRunId() {
        return runId;
    }
    
}
