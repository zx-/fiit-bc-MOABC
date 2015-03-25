/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna_generator;

import dna_sequence.DNASequence;
import motif.Nucleotide;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public interface DNAGenerator {
    
    /**
     *
     * @param length
     * @return
     */
    public DNASequence generateDNA(int length);
    
}
