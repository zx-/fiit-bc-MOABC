/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.OBLF;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.evaluator.Evaluator;
import artificial_bee_colony.bee.sorter.Sorter;
import dna_sequence.DNASequence;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class OppositionBasedOptimization {
    
    private Sorter sorter;
    private Evaluator evaluator;
    private Configuration cfg;
    
    public OppositionBasedOptimization(Sorter s,Evaluator e,Configuration cfg){
        
        sorter = s;
        evaluator = e;
        this.cfg = cfg;
        
    
    }
    
    public void applyOB(Bee[] colony){
    
        Random r = new Random();
        ArrayList<Bee> newBees = new ArrayList<>();
        Bee newB;

        int dimensions = colony[0].getNumberOfSequences() + 1;

        int newpositions[] = new int[colony[0].getNumberOfSequences()];
        DNASequence[] sequences = colony[0].getSequences();
        int minimums[] = new int[dimensions];
        int maximums[] = new int[dimensions];
        int i, motifLength;
        int pos[];
        boolean isValid = true;

        if (r.nextDouble() < cfg.jumpRate) {
            
            generateBorders(colony, minimums, maximums);

            for (Bee curB : colony) {

                pos = curB.getSequenceMotifPositions();
                motifLength = minimums[0] + maximums[0] - curB.getMotifLength();
                isValid = true;

                for (i = 0; i < curB.getNumberOfSequences(); i++) {

                    newpositions[i] = minimums[i + 1] + maximums[i + 1] - pos[i];
                    if( newpositions[i] + motifLength > sequences[i].getLength() ){
                    
                        isValid = false;
                        break;
                    
                    }
                    
                }
                
                if(isValid){                    

                    newB = Bee.createBeeFromSolution(
                            motifLength,
                            Arrays.copyOf(newpositions, newpositions.length),
                            curB.getSequences(),
                            cfg
                    );

                    evaluator.evaluate(newB);
                    if (newB.satisfiesConstraints()) {

                        newBees.add(newB);

                    }
                
                }

            }
            
            Bee newColony[] = new Bee[newBees.size() + colony.length];
            for(i = 0; i < colony.length; i++)
                newColony[i] = colony[i];

            for(i = 0; i < newBees.size(); i++)
                newColony[colony.length + i] = newBees.get(i);

            sorter.sortColony(newColony);
            System.arraycopy(newColony, 0, colony, 0, colony.length);

        }
    
    };

    private void generateBorders(Bee[] colony, int[] minimums, int[] maximums) {
    
        int i;
        int pos[];
        
        Arrays.fill(maximums, 0);
        Arrays.fill(minimums, Integer.MAX_VALUE);
        
        for(Bee b:colony){
        
            if(b.getMotifLength() > maximums[0])
                maximums[0] = b.getMotifLength();
            
            if(b.getMotifLength() < minimums[0])
                minimums[0] = b.getMotifLength();
            
            pos = b.getSequenceMotifPositions();
            
            for(i = 0; i<b.getNumberOfSequences(); i++){
            
                if(pos[i] > maximums[i+1])
                    maximums[i+1] = pos[i];
                
                if(pos[i] < minimums[i+1])
                    minimums[i+1] = pos[i];                          
            
            }
        
        }
    
    }
    
    
}
