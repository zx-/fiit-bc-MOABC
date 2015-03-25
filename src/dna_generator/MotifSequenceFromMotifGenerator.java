/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna_generator;

import dna_sequence.DNASequence;
import motif.Motif;
import motif.Nucleotide;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class MotifSequenceFromMotifGenerator implements MotifSequenceGenerator{
    
    private double[][] pfm;
    private double[] probabilities = new double[4];
    private int[] nums = new int[]{0,1,2,3};
    private Nucleotide[] n = Nucleotide.values(); 
    private Motif motif;
    
    public MotifSequenceFromMotifGenerator(Motif m){
    
        pfm = m.getPFM();
        motif = m;
    
    }

    @Override
    public DNASequence generateMotifSequence() {
        
        EnumeratedIntegerDistribution distribution;
        Nucleotide[] seq = new Nucleotide[motif.getLength()];
        
        for(int i = 0; i<motif.getLength();i++){
        
            for(int j = 0; j<4;j++ ){
            
                probabilities[j] = pfm[j][i];                        
            
            }
            
            distribution 
                = new EnumeratedIntegerDistribution(nums,probabilities);
            
            seq[i] = n[distribution.sample()];
        
        }       
        DNASequence dnaSequence = new DNASequence(seq);
        dnaSequence.setMotifAt(0);
        dnaSequence.setMotifLength(seq.length);
        return dnaSequence;
    }
    
}
