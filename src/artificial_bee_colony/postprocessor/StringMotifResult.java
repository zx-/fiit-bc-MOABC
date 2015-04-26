/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.postprocessor;

import artificial_bee_colony.bee.Bee;
import motif.Nucleotide;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class StringMotifResult {
    
    public int position;
    public int sequenceNumber;
    public String motif;
    public Bee bee;
    
    public static StringMotifResult getFromBee(Bee bee){
    
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
        
        return new StringMotifResult(position,seqNum,motif,bee);
        
    }

    public StringMotifResult(){}
    
    public StringMotifResult(int position,int sequenceNumber,String motif,Bee b){
    
        this.position = position;
        this. sequenceNumber = sequenceNumber;
        this.motif = motif;
        this.bee = b;
    
    }
    
    /**
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * @return the sequenceNumber
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return the motif
     */
    public String getMotif() {
        return motif;
    }

    /**
     * @param motif the motif to set
     */
    public void setMotif(String motif) {
        this.motif = motif;
    }

    /**
     * @return the bee
     */
    public Bee getBee() {
        return bee;
    }

    /**
     * @param bee the bee to set
     */
    public void setBee(Bee bee) {
        this.bee = bee;
    }
 
    @Override
    public String toString(){
    
        return sequenceNumber+", "+position+", "+motif;
    
    }
}
