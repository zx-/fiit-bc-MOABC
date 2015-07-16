/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony;

import ResultParser.ResultParser;
import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.OBLF.LevyFlight;
import artificial_bee_colony.bee.OBLF.OppositionBasedOptimization;
import artificial_bee_colony.bee.evaluator.Evaluator;
import artificial_bee_colony.bee.mutator.Mutator;
import artificial_bee_colony.bee.randomSelector.RandomSelector;
import artificial_bee_colony.bee.randomSelector.RandomValuedSelection;
import artificial_bee_colony.bee.sorter.Sorter;
import artificial_bee_colony.postprocessor.SimplePostprocessor;
import artificial_bee_colony.postprocessor.StringMotifResult;
import dna_sequence.DNASequence;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import motif.Motif;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class MotifFindingSolver {
    
    static final Logger logger = Logger.getLogger( MotifFindingSolver.class.getName() );
    
    private Evaluator evaluator;
    private Sorter sorter;
    private Configuration cfg;
    private Mutator mutator;
    private RandomSelector selector = new RandomValuedSelection();
    
    public MotifFindingSolver(Evaluator e,Configuration config, Mutator m,Sorter s){
    
        evaluator = e;
        sorter = s;
        cfg = config;
        mutator = m;
        logger.setLevel(config.logLevel);
    
    }
    
    public StringMotifResult[] solve(DNASequence[] sequences){
        
        return solve(sequences, cfg);
    
    }
    
    private void iterate(DNASequence[] sequences, Bee[] colony){
    
        iterate(sequences, colony, cfg);
     
    }
    
    public StringMotifResult[] solve(DNASequence[] sequences, Configuration config){
    
        int i;
        Bee[] colony = new Bee[0];
        ArrayList<Bee> finalColony = new ArrayList<>();
        SimplePostprocessor proc = new SimplePostprocessor();
        
        // initiate bees
       
        for(i = 0; i<config.numberOfRepetitionsPerInput;i++){
            
            colony = Bee.createBeeArraySatisfyingConstraints(sequences, config, config.getColonySize(),evaluator);
            iterate(sequences,colony,config);
            for(Bee b:colony)
                finalColony.add(b);
            
            
        }
        
        
        return proc.processColony(finalColony,sorter);
        
//        ResultParser.parseFinalResults(
//        
//                proc.processColony(finalColony,sorter)
//                
//        );

        
    }

    private void iterate(DNASequence[] sequences, Bee[] colony, Configuration cfg){
        
        int i,j,k;
        Bee selectedBee,newBee;
        List<Bee> firstParetoFront;
        LevyFlight levyFlight = new LevyFlight(cfg,sorter,evaluator);
        OppositionBasedOptimization  obo = new OppositionBasedOptimization(sorter, evaluator,cfg);
        
        ResultParser.iterationSearchStart(cfg);
        
        evaluator.evaluate(colony,cfg);
        sorter.sortColony(colony);
        for(i = 0 ; i < cfg.maxIterations; i++){
        
            
            // WorkerPhase
            ResultParser.workerPhase();
            for( j = 0; j < cfg.workersSize; j++){
            
                k = 1;
                newBee = mutator.mutate(colony[j], colony);
                evaluator.evaluate(newBee, cfg);
                while(!newBee.satisfiesConstraints()){
                    
                    k++;
                    newBee = mutator.mutate(colony[j], colony);
                    evaluator.evaluate(newBee, cfg);
                
                }
                ResultParser.satisfyBeeCreationTries(k);
                
                if(!colony[j].satisfiesConstraints()){
                
                    colony[j] = newBee;
                
                } else if(sorter.isBetter(newBee,colony[j])){
                
                    colony[j] = newBee;
                
                }
            
            }
            
            // Onlookers
            ResultParser.onlookersPhase();
            for(j = cfg.workersSize; j < cfg.workersSize + cfg.onlookersSize; j++){
            
                selectedBee = selector.select(colony, cfg.workersSize);
                
                k=1;
                newBee = mutator.mutate(selectedBee, colony);
                evaluator.evaluate(newBee , cfg);
                while(!newBee.satisfiesConstraints()){
                    
                    k++;
                    newBee = mutator.mutate(selectedBee, colony);
                    evaluator.evaluate(newBee, cfg);
                
                }
                ResultParser.satisfyBeeCreationTries(k);
                
                if(!colony[j].satisfiesConstraints()){
                
                    colony[j] = newBee;
                
                } else if(sorter.isBetter(newBee,colony[j])){
                
                    colony[j] = newBee;
                
                }
            
            }
            //Scouts
            ResultParser.scountsPhase();
            j = 0;
            while(j < cfg.scoutsSize){
            
                newBee = Bee.createBee(sequences, cfg);
                evaluator.evaluate(newBee,cfg);
                
                if(newBee.satisfiesConstraints()){
                
                    colony[cfg.workersSize + cfg.onlookersSize + j] = newBee;
                    j++;
                
                }
            
            }
            
            // SORT COLONY
            firstParetoFront = sorter.sortColony(colony);
            ResultParser.paretoFrontSize(firstParetoFront.size());
            
            if(cfg.useLevy == 1){            

                ResultParser.levyPhase();            
                levyFlight.applyLevyFlight(firstParetoFront,colony);
            
            }
            
            if(cfg.useObom == 1){
            
                ResultParser.obPhase();   
                obo.applyOB(colony);
                
            }
            
            
            
            if(logger.isLoggable(Level.INFO)){
        
                logger.log(Level.INFO,"Iteration: "+i);
        
            }       
        
            ResultParser.parseColonyIteration(colony,i);
            
        }
        
//        ResultParser.parseFinalResults(colony);
        ResultParser.iterationSearchEnd();
        
//        for(i = 0 ; i < 10 ; i++){
//        
//            System.out.println(colony[i].toString());
//            System.out.println(Motif.createMotifFromPFM(colony[i].getPFM()).toString());
//        
//        }
//        
        
    }
    
    
}
