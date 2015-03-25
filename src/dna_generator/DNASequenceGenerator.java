/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna_generator;

import dna_sequence.DNASequence;
import java.util.Random;
import motif.Motif;
import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class DNASequenceGenerator {
    
    public static DNASequence[] GenerateSequences(Motif m, int length, int num){    
        
        DNASequence[] aSequences = new DNASequence[num];
        DNAGenerator gDNA = new DNASequenceUniformGenerator();
        MotifSequenceGenerator gMotif = new MotifSequenceFromMotifGenerator(m);
                
        for(int i=0; i<num; i++){
        
            aSequences[i] = generateSequence(gDNA, gMotif, length);
        
        }
                   
        return aSequences;
    
    }
    
    private static DNASequence generateSequence(DNAGenerator gDNA,MotifSequenceGenerator gMotif,int length){
    
        DNASequence seq = gDNA.generateDNA(length);
        DNASequence motif = gMotif.generateMotifSequence();
        
        if( seq.getLength()> motif.getLength()){
        
            Random randomGenerator = new Random();
            int randPosition = randomGenerator.nextInt(seq.getLength() - motif.getLength() + 1);
            
            for(int i = randPosition; i-randPosition<motif.getLength(); i++)
                seq.setNucleotide(motif.nucleotideAt(i-randPosition), i);
            
            seq.setMotifAt(randPosition);
            seq.setMotifLength(motif.getLength());
            
            return seq;
        
        }
        
        if(seq.getLength() ==  motif.getLength()){
        
            return motif;
        
        }
        
        return null;
    
    }
    
    
    
}
