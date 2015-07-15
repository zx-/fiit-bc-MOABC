/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.mutator;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.evaluator.Evaluator;
import dna_sequence.DNASequence;
import java.util.Random;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class BasicMutator implements Mutator {

    private Configuration cfg;
    private final Evaluator e;
    
    public BasicMutator(Evaluator e){
    
        cfg = new Configuration();
        this.e = e;
    
    }
    
    @Override
    public void setConfiguration(Configuration c){
    
        this.cfg = c;
    
    }

    
    @Override
    public Bee mutate(Bee b, Bee[] colony) {
  
        Bee randomBee;
        Random r = new Random();
        int pos = r.nextInt(colony.length);
        
        randomBee = colony[pos];
        
        if( b == randomBee && colony.length > 1){
        
//            randomBee = colony[(pos+1)%colony.length]; // new random maybe
            randomBee = colony[(pos+r.nextInt(colony.length-1)+1) % colony.length];
            
        }
        
        boolean isSolution = false;
        double step;
        DNASequence[] seqs = b.getSequences();
        int motifLength = 0,i;
        int[] newPositions = new int[seqs.length];
        int[] bPositions = b.getSequenceMotifPositions();
        int[] randomBeePositions = randomBee.getSequenceMotifPositions();
        
        int genCount = 0;
        while(!isSolution){
        
            if( genCount++ > cfg.maxGenerationTries)
                return b;
        
            step = r.nextDouble()*2 - 1;
            isSolution = true;
            
            motifLength = (int) (b.getMotifLength() 
                    + step*(b.getMotifLength() - randomBee.getMotifLength()));
            
            if( motifLength < cfg.motifMinLength || motifLength > cfg.motifMaxLength ){
            
                isSolution = false;
                continue;                      
            
            }
            
            for( i = 0; i < newPositions.length; i++ ){
                
                step = r.nextDouble()*2 - 1;
            
                newPositions[i] = (int) ( bPositions[i] 
                        + step*( bPositions[i] - randomBeePositions[i] ));
                
                if( newPositions[i] < 0 
                        || newPositions[i] + motifLength > seqs[i].getLength() )
                {
                
                    isSolution = false;
                    break;
                
                }
            
            }  
        
        }               
        
        return Bee.createBeeFromSolution(motifLength, newPositions, seqs, cfg);
    
    }
    
}
