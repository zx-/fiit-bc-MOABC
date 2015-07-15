/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.OBLF;

import ResultParser.ResultParser;
import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.evaluator.Evaluator;
import artificial_bee_colony.bee.sorter.Sorter;
import dna_sequence.DNASequence;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.special.Gamma;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class LevyFlight {
    
    private final Configuration cfg;
    private final Sorter sorter;
    
    private NormalDistribution uDist;
    private NormalDistribution vDist;
    
    private final Evaluator evaluator;
    
    public LevyFlight(Configuration cfg,Sorter s, Evaluator e){
    
        this.cfg = cfg;
        this.sorter = s;
        this.evaluator = e;
        createDistributions();
        
    }
    
    public void applyLevyFlight(List<Bee> firstParetoFront,Bee[] colony){
    
        for(int i = 0; i < firstParetoFront.size() && i < 40; i++){                 
            
            colony[i] = applyLevyFlight(colony[i], colony);
                    
        }
    
    }
    
    public Bee applyLevyFlight(Bee mutatingBee, Bee[] colony){
    
        double stepLength;
        Bee newBee; 
        
        for(int i = 0; i< cfg.OBLFepsilon; i++){
        
            stepLength = computeStepLength(i);
            ResultParser.levyStepLength(stepLength);
            
            newBee = generateNewBee(mutatingBee,colony,stepLength);
            evaluator.evaluate(newBee);
            
            while(!newBee.satisfiesConstraints()){
            
                newBee = generateNewBee(mutatingBee,colony,stepLength);
                evaluator.evaluate(newBee);
            
            }
            

            if(sorter.isBetter(newBee,mutatingBee)){
            
                mutatingBee = newBee;
                ResultParser.levyImprove(true);
            
            } else {
            
                ResultParser.levyImprove(false);
                
            }
        
        }
        
        return mutatingBee;
    
    }

    /**
     * Computes step_length(t) = 0.001 x s(t) 
     * where s(t) is from levys
     * doesnt include SLC component
     * 
     * @param i
     * @return 
     */
    private double computeStepLength(int i) {
    
        double s = 1;
        
        double u = uDist.sample();
        double v = vDist.sample();
        
        v = Math.abs(v);
        v = Math.pow(v, 1.0/cfg.OBLFbeta);
        
        s = u/v;        
        s = s*cfg.OBLFstepMultiplier;
        
        if(Math.abs(s) > 300){
        
            s = 0;
        
        }
        
        
        return s;
    
    }

    private Bee generateNewBee(Bee mutatingBee, Bee[] colony, double stepLength) {
        
        Bee randomBee;
        Random r = new Random();
        int pos;
    
                
        pos = r.nextInt(colony.length); 
        randomBee = colony[pos];
        
        if( mutatingBee == randomBee && colony.length > 1){

            randomBee = colony[(pos+r.nextInt(colony.length-1)+1) % colony.length];

        } 
        
        int i;
        int motifLength = mutatingBee.getMotifLength();
        boolean isSolution = false;
        DNASequence[] seqs = mutatingBee.getSequences();
        int[] newPositions = new int[seqs.length];
        int[] mutatingBeePositions = mutatingBee.getSequenceMotifPositions();
        int[] randomBeePositions = randomBee.getSequenceMotifPositions();
        
        int genCount = 0;
        while(!isSolution){
        
            if( genCount++ > cfg.maxGenerationTries)
                return mutatingBee;
            
            isSolution = true;
            
            if(r.nextDouble() > cfg.perturbationRate){
            
               // motifLength = (int) (mutatingBee.getMotifLength() 
                 //   + r.nextDouble()*stepLength*(mutatingBee.getMotifLength() - randomBee.getMotifLength()));
                // new = prev + step*U(0,1)*SLC
                // SLC = prev - random
                // step = 0,001*s(t) - s from levys
                motifLength = (int) ( mutatingBee.getMotifLength() + stepLength*r.nextDouble()); 
                
            } else {
            
                motifLength = mutatingBee.getMotifLength();
            
            }
            
            if( motifLength < cfg.motifMinLength || motifLength > cfg.motifMaxLength ){
            
                isSolution = false;
                continue;                      
            
            }
            
            for( i = 0; i < newPositions.length; i++ ){
            
                if(r.nextDouble() > cfg.perturbationRate){
                
//                    newPositions[i] = (int) ( mutatingBeePositions[i] 
//                        + r.nextDouble()*stepLength*( mutatingBeePositions[i] - randomBeePositions[i] ));
//                
                    newPositions[i] = (int) ( mutatingBeePositions[i] 
                        + r.nextDouble()*stepLength);
                
                
                    if( newPositions[i] < 0 
                        || newPositions[i] + motifLength > seqs[i].getLength() )
                    {
                
                        isSolution = false;
                        break;
                
                    }
                    
                } else {
                
                    newPositions[i] = mutatingBeePositions[i];
                    if( newPositions[i] < 0 
                        || newPositions[i] + motifLength > seqs[i].getLength() )
                    {
                
                        isSolution = false;
                        break;
                
                    }
                    
                }
                
            
            }  
        
        }  
        
        
        
        return Bee.createBeeFromSolution(motifLength, newPositions, seqs, cfg);
        
    }

    private void createDistributions() {
   
        double du;
        
        double gamma = Gamma.gamma(1 + cfg.OBLFbeta);
        
        du = gamma*Math.sin(Math.PI*cfg.OBLFbeta/2);
        
        gamma = Gamma.gamma((1 + cfg.OBLFbeta)/2);
        du = du / (cfg.OBLFbeta * gamma * Math.pow(2, (cfg.OBLFbeta-1)/2));
        
        du = Math.pow(du,1/cfg.OBLFbeta);
        
        
        vDist = new NormalDistribution(0, 1);
        uDist = new NormalDistribution(0,du);
    
    }
    
}
