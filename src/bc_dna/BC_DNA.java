/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc_dna;

import ResultParser.ResultParser;
import artificial_bee_colony.Configuration;
import artificial_bee_colony.MotifFindingSolver;
import artificial_bee_colony.bee.evaluator.Evaluator;
import artificial_bee_colony.bee.evaluator.GonzalezAlvarezEvaluator;
import artificial_bee_colony.bee.mutator.BasicMutator;
import dna_generator.DNASequenceGenerator;
import dna_sequence.DNASequence;
import dna_sequence.DNASequenceLoader;
import java.io.FileNotFoundException;
import motif.Motif;
import motif.PFMLoader;
import motif.PFMNotLoadedException;
import motif.PFMfromStringLoader;

/**
 *
 * @author 
 */
public class BC_DNA {

    /**
     * @param args the command line arguments
     * @throws motif.PFMNotLoadedException
     */
    public static void main(String[] args) throws PFMNotLoadedException, FileNotFoundException {
        
        
        Configuration c = new Configuration();
        
        DNASequence[] seq = DNASequenceLoader.loadFromFile("assets\\sequences\\real\\hm20r.fasta");
        
          
//        for(int i= 0; i<size;i++)
//            System.out.println(seq[i].toJSON());
        Evaluator e = new GonzalezAlvarezEvaluator(c);
        
        new MotifFindingSolver(e,c,new BasicMutator(e,c))
                .solve(seq);
        

        ResultParser.printResults();   
        
        
    }
    
}
