/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import ResultParser.ResultParser;
import artificial_bee_colony.Configuration;
import artificial_bee_colony.MotifFindingSolver;
import artificial_bee_colony.bee.evaluator.Evaluator;
import artificial_bee_colony.bee.mutator.Mutator;
import artificial_bee_colony.bee.sorter.Sorter;
import dna_sequence.DNASequence;

/**
 *
 * @author pc-dell
 */
public class Tester {
    
    private DNASequence[] seq;
    private Configuration[] configs;
    private Evaluator evaluator;
    private Mutator mutator;
    private Sorter sorter;
    private String testName;
    
    public static class TesterBuilder{
    
        private DNASequence[] seq;
        private Configuration[] configs;
        private Evaluator evaluator;
        private Mutator mutator;
        private Sorter sorter;
        private String testName;
        
        
        public TesterBuilder dnaSequences(DNASequence[] seq){
        
            this.seq = seq;
            return this;
            
        };
        
        public TesterBuilder configurations(Configuration[] configs){
        
            this.configs = configs;
            return this;
            
        };
        
        public TesterBuilder evaluator(Evaluator e){
        
            this.evaluator = e;
            return this;
        
        };
        
        public TesterBuilder mutator(Mutator m){
        
            this.mutator = m;
            return this;
        
        };
        
        public TesterBuilder sorter(Sorter s){
        
            this.sorter = s;
            return this;
        
        }
        
        public TesterBuilder(String s)        {
        
            this.testName = s;
            
        }
        
        
        public Tester build(){
        
            return new Tester(this);
        
        }
        
    
    }
    
    private Tester(TesterBuilder b){
    
        this.seq = b.seq;
        this.configs = b.configs;
        this.mutator = b.mutator;
        this.sorter = b.sorter;
        this.evaluator = b.evaluator;
        this.testName = b.testName;
        
    }
    
    public void test(){
    
        for(Configuration c:configs){
            
            
            Sorter.logger.setLevel(c.logLevel);
            System.out.println(seq[0].getName());
        
            ResultParser.setCurrentConfiguration(c);
            ResultParser.beeSorter = sorter;
            c.currentTestName = testName;
            evaluator.setConfiguration(c);
            mutator.setConfiguration(c);
            new MotifFindingSolver(
                   evaluator,
                   c,
                   mutator,
                   sorter)
                .solve(seq); 
        
        }
    
    }
    
}
