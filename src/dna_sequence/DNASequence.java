/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna_sequence;

import com.google.gson.Gson;
import motif.Nucleotide;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class DNASequence {
    
    private Nucleotide[] seq;
    private int motifAt = -1;
    private int motifAtFound = -1;
    private int motifLength = -1;
    private String name;    
    private int number;
    
    public DNASequence(){}
    
    public DNASequence(Nucleotide[] s){
    
        seq = new Nucleotide[s.length];
        System.arraycopy(s, 0, seq, 0, s.length);
    
    }
    
    public static DNASequence createFromJson(String json){
    
        Gson gson = new Gson();
        DNASequence seq = gson.fromJson(json, DNASequence.class);
        
        return seq;
        
    }
    
    public static DNASequence createFromString(String nucleotides){
    
        DNASequence s = new DNASequence();
        s.seq = new Nucleotide[nucleotides.length()];
        for(int i = 0 ; i< nucleotides.length();i++){
        
            s.seq[i] = Nucleotide.valueOf(Character.toString(nucleotides.charAt(i)));
        
        }
        
        return s;
    
    }
    
    public int getLength(){
    
        return seq.length;
    
    }
    
    public Nucleotide nucleotideAt(int i){
    
        if(i >= 0 && i < this.getLength()){
        
            return seq[i];
        
        }
        
        return null;
    
    }
    
    public void setNucleotide(Nucleotide n, int position){
    
        seq[position] = n;
    
    }
    
    public String toString(){
    
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i<getLength(); i++){
            
            if(motifAt >= 0 && motifAt == i ){
            
                sb.append('|');
            
            }
                        
            sb.append(nucleotideAt(i));
            
            if(motifLength >= 0 && motifAt+motifLength - 1 == i){
            
               sb.append('|'); 
            
            }
                
            
        
        }
        
        return sb.toString();
    
    }

    /**
     * @return the motifAt
     */
    public int getMotifAt() {
        return motifAt;
    }

    /**
     * @param motifAt the motifAt to set
     */
    public void setMotifAt(int motifAt) {
        this.motifAt = motifAt;
    }

    /**
     * @return the motifAtFound
     */
    public int getMotifAtFound() {
        return motifAtFound;
    }

    /**
     * @param motifAtFound the motifAtFound to set
     */
    public void setMotifAtFound(int motifAtFound) {
        this.motifAtFound = motifAtFound;
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
    
    public String toJSON(){
        
        Gson gson = new Gson();
        return gson.toJson(this);
    
    }

    /**
     * @return the sequence array of Nucleotide
     */
    public Nucleotide[] getSequenceArray() {
        return seq;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }
   
    
}
