/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna_generator;

import dna_sequence.DNASequence;
import motif.Nucleotide;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class DNASequenceUniformGenerator implements DNAGenerator{
    
    private Nucleotide[] n = Nucleotide.values();
    private UniformIntegerDistribution dist 
                = new UniformIntegerDistribution(0,n.length-1); 

    @Override
    public DNASequence generateDNA(int length) {
        
        Nucleotide[] seq = new Nucleotide[length];        
                
        for(int i=0;i<length;i++){
        
            seq[i] = n[dist.sample()];
        
        }
        
        return new DNASequence(seq);
        
        
    }
    
    
}
