/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.evaluator.Evaluator;
import dna_sequence.DNASequence;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import motif.Nucleotide;
import org.w3c.dom.ranges.RangeException;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class Bee {
    
    private int[] sequenceMotifPositions;
    private int motifLength;
    private DNASequence[] sequences;
    private Configuration config;
    private boolean satisfiesConstraints;
    private final HashMap<Integer,Double> objectives;
    private double[][] PFM;
            
    private Bee(DNASequence[] sequences, Configuration config){
        
        this.objectives = new HashMap<>();    
        sequenceMotifPositions = new int[sequences.length];
        this.sequences = sequences;
        this.config = config;
    
    };
    
    public static Bee createBee(DNASequence[] sequences, Configuration config) {
        
        Bee b = new Bee(sequences,config);
        b.randomizeBee();
        return b;
   
    }
    
    public static Bee[] createBeeArray(DNASequence[] sequences, Configuration config,int size){
    
        Bee[] aBee = new Bee[size];
        
        for(int i = 0; i < size; i++ )
            aBee[i] = Bee.createBee(sequences, config);
        
        return aBee;
    
    }
    
    public static Bee[] createBeeArraySatisfyingConstraints(DNASequence[] sequences, Configuration config,int size,Evaluator e){
    
        Bee[] aBee = new Bee[size];
        int i = 0;
        
        while( i < size){
        
            aBee[i] = Bee.createBee(sequences, config);
            e.evaluate(aBee[i]);
            if(aBee[i].satisfiesConstraints()){
            
                i++;
            
            }         
        
        }
            
        
        return aBee;
    
    }
    
    public static Bee createBeeFromSolution(int MotifLength,int[] positions,DNASequence[] sequences, Configuration config){
    
        Bee b = new Bee(sequences,config);
        
        b.setMotifLength(MotifLength);
        b.sequenceMotifPositions = positions;
        
        return b;
    
    }
    
    public void randomizeBee(){
        
        Random rand = new Random();        
        setMotifLength(config.motifMinLength 
                + rand.nextInt(config.motifMaxLength - config.motifMinLength + 1));
        
        if(getMotifLength() > getSequences()[0].getLength() )
            setMotifLength(getSequences()[0].getLength());
        
        for(int i = 0; i < getSequences().length; i++){
        
            sequenceMotifPositions[i] = 
                    getSequences()[i].getLength() >= getMotifLength()?
                    rand.nextInt(getSequences()[i].getLength() - getMotifLength() + 1):0;
        
        }
        
    
    }

    /**
     * @return the motifLength
     */
    public int getMotifLength() {
        return motifLength;
    }

    /**
     * @param motifLength the motifLength to set
     */
    public void setMotifLength(int motifLength) {
        this.motifLength = motifLength;
    }
    /**
     * Returns nucleotide on position from candidate solution
     * from concrete sequence.
     * 
     * @param positionNumber position in candidate motif
     * @param sequenceNumber number of sequence
     * @return 
     */
    public Nucleotide getNucleotideFromSolution(int positionNumber, int sequenceNumber){
    
        if(positionNumber >= getMotifLength() && sequenceNumber >= getNumberOfSequences())
            throw new ArrayIndexOutOfBoundsException();
        
        return getSequences()[sequenceNumber]
                .nucleotideAt(getSequenceMotifPositions()[sequenceNumber] + positionNumber
                );
        
        
    }

    public int getNumberOfSequences() {
     
        return getSequences().length;
    
    }
    
    public Nucleotide[][] getSolutionSequences(){
    
        Nucleotide[][] solutions = new Nucleotide[getNumberOfSequences()][getMotifLength()];
        
        for(int i = 0; i < getNumberOfSequences(); i++){
        
            System.arraycopy(getSequences()[i].getSequenceArray(), //src 
                    getSequenceMotifPositions()[i], //srcPos
                    solutions[i], //dest
                    0, //destPost 
                    getMotifLength() //length
            );
        
        }
        
        return solutions;
    
    }

    /**
     * @return the satisfiesConstraints
     */
    public boolean satisfiesConstraints() {
        return satisfiesConstraints;
    }

    /**
     * @param satisfiesConstraints the satisfiesConstraints to set
     */
    public void setSatisfiesConstraints(boolean satisfiesConstraints) {
        this.satisfiesConstraints = satisfiesConstraints;
    }
    
    public void setObjective(int objNumber, double value){
    
        this.objectives.put(objNumber, value);
    
    }
    
    public double getObjectiveMotifLength(boolean normalise){
    
        return getObjective(0, normalise);
    
    }
    
    public double getObjectiveSupport(boolean normalise){
    
        return getObjective(1, normalise);
    
    }
    
    public double getObjectiveSimilarity(boolean normalise){
    
        return getObjective(2, normalise);
    
    }
    
    public double getObjective(int objNumber, boolean normalise){
    
        double norm = 1;
        if(normalise){
        
            switch(objNumber){
            
                case 0: norm = config.motifMaxLength;
                        break;
                    
                case 1: norm = this.getNumberOfSequences();
                        break;
                    
                case 2: norm = 1;
                        break;
                    
            }
        
        }            
    
        return this.objectives.get(objNumber)/norm;
        
    }
    
    public int getNumOfSetObjectives(){
    
        return this.objectives.size();
    
    }
    
    public void setMultipleObjectives(double ... values){
    
        for(int i = 0; i < values.length; i++ )
            this.objectives.put(i, values[i]);
    
    }

    /**
     * @return the sequences
     */
    public DNASequence[] getSequences() {
        return sequences;
    }

    /**
     * @return the sequenceMotifPositions
     */
    public int[] getSequenceMotifPositions() {
        return sequenceMotifPositions;
    }
    
    public String toString(){
    
        StringBuilder sb = new StringBuilder();
        
        sb.append("###################\n");
        sb.append(Arrays.toString(sequenceMotifPositions));
        sb.append("\n");
        for(int i = 0; i <sequenceMotifPositions.length; i++){
        
            sb.append( sequenceMotifPositions[i] - sequences[i].getLength());
            sb.append(", ");
            
        }
        sb.append("\n");
        sb.append(objectives.toString());
        sb.append("\n");
        sb.append( this.getObjectiveMotifLength(true)+", ");
        sb.append( this.getObjectiveSupport(true)+", ");
        sb.append( this.getObjectiveSimilarity(true)+"\n");    
        sb.append("###################\n");
                
        return sb.toString();
    
    }
    
    public String getDNADatasetName(){
    
        return this.sequences[0].getName();
    
    }

    /**
     * @return the pfm
     */
    public double[][] getPFM() {
        return PFM;
    }

    /**
     * @param pfm the pfm to set
     */
    public void setPFM(double[][] pfm) {
        this.PFM = pfm;
    }
    
}
