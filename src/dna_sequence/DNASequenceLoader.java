/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dna_sequence;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class DNASequenceLoader {
    
    public static DNASequence[] loadFromFile(File f) throws FileNotFoundException{
    
        ArrayList<DNASequence> list = new ArrayList<>();
        Scanner sc = new Scanner(f);
        DNASequence seq;
        int i = 0;
        
        while(sc.hasNext()){
        
            sc.next(); // Seqnumber
            seq = DNASequence.createFromString(sc.next());
            seq.setName(f.getName());
            seq.setNumber(i++);
            list.add(seq);
        
        }
        
        
        return list.toArray(new DNASequence[list.size()]);
    
    }
    
    public static DNASequence[] loadFromFile(String filePath) throws FileNotFoundException{
    
        
        File f = new File(filePath);
        return loadFromFile(f);
    
    
    }
    
}
