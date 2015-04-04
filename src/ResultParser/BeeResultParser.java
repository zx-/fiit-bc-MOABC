/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultParser;

import artificial_bee_colony.bee.Bee;
import java.util.Arrays;
import motif.Nucleotide;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class BeeResultParser {
    
    static String parseBeeMotif(Bee bee){
    
        String motif = "";
        int position;
        int seqNum;
        double score = 0;
        
        double currentMax = 0;
        int currentMaxCandidateIndex = 0;
    
        Nucleotide[][] solutions = bee.getSolutionSequences();
        double[][] pfm = bee.getPFM();
        
        int i = 0,j;
        for(Nucleotide[] candidateMotif:solutions){
        
            for(j = 0; j<candidateMotif.length; j++){
            
                score += pfm[candidateMotif[0].ordinal()][j];
            
            }
            
            if(score > currentMax){
            
                currentMax = score;
                currentMaxCandidateIndex = i;
            
            }
            
            score = 0;
            i++;
                
        }
        
        motif = "";
        for(Nucleotide n:solutions[currentMaxCandidateIndex]){
        
            motif+=n;
        
        }
        seqNum = currentMaxCandidateIndex;
        position = bee.getSequenceMotifPositions()[currentMaxCandidateIndex];
        position -= bee.getSequences()[currentMaxCandidateIndex].getLength();
        
        return seqNum+", "+position+", "+motif;
        
    }
    
}
