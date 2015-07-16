/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.MotifFindingSolver;
import artificial_bee_colony.bee.evaluator.Evaluator;
import artificial_bee_colony.bee.evaluator.GonzalezAlvarezEvaluator;
import artificial_bee_colony.bee.mutator.BasicMutator;
import artificial_bee_colony.bee.sorter.SimilarityPrioritySorter;
import artificial_bee_colony.bee.sorter.SingleCrowdingDistanceSorter;
import artificial_bee_colony.bee.sorter.Sorter;
import dna_sequence.DNASequence;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ParallelTester {
    
    
    public static final int REPETITIONS = 2;
    
    public static void main(String args[]) throws InterruptedException, ExecutionException{
    

        
    
    }
    
    
    public void test(ArrayList<DNASequence[]> sequenceList,ArrayList<Configuration> configList) throws InterruptedException, ExecutionException{
    
        List<List<ParallelTest>> testsListsPerRep = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Date timestamp = new Date(calendar.getTime().getTime());
        
        
        
        configList.forEach((c)->{  
            
            for (int i = 0; i < REPETITIONS; i++) {
                
                List<ParallelTest> tests = new ArrayList<>();
                
                for (DNASequence[] seqArray : sequenceList) {

                    if (seqArray.length > 1) {

                        Evaluator e = new GonzalezAlvarezEvaluator();
                        Sorter s = c.useSimilarityPrioritySorter == 1
                                ? new SimilarityPrioritySorter() : new Sorter();

                        s.logger.setLevel(c.logLevel);

                        if (c.useSingleCrowdingDistance == 1) {
                            s.setCDSorter(new SingleCrowdingDistanceSorter());
                        }

                        MotifFindingSolver solver
                                = new MotifFindingSolver(e, c, new BasicMutator(e), s);

                        tests.add(
                                new ParallelTest(seqArray, i, c, solver)
                        );

                    }

                };
                
                testsListsPerRep.add(tests);
                
            }
        });
            
                
        List<List<CompletableFuture<ParallelTest>>> listOfRepFutures = new ArrayList<>();
        
        System.out.println("Zacinam");    
        for(List<ParallelTest> tests:testsListsPerRep){
        
            List<CompletableFuture<ParallelTest>> futures = new ArrayList<>();       
        
        
            tests.stream().forEach((t) -> {
                futures.add(                   
                    CompletableFuture.supplyAsync( ()->{ return t.test();} )                    
                );
            });
            
            listOfRepFutures.add(futures);
        
        }        
        
        List<CompletableFuture<Void>> endFutures = new ArrayList<>();
        for(List<CompletableFuture<ParallelTest>> futures:listOfRepFutures){
        
            System.out.println("Zacinam cakat");
            CompletableFuture f = CompletableFuture.allOf(futures.toArray(
                new CompletableFuture[0]
            ));

            endFutures.add(f.thenApplyAsync((v)->{
            
                ParallelTester.this.parseFutures(futures);
                return null;
                
            }));
            
        }
        
        endFutures.forEach((f)->{ 
            try {
                f.get();
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        System.out.println("KOncim");
    
    }   

    private void parseFutures(List<CompletableFuture<ParallelTest>> futures) {
        
        List<ParallelTest> tests = new ArrayList<>();
        
        futures.forEach((f)->{ 
            try {
                tests.add(f.get());
            } catch (InterruptedException ex) {
                Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ExecutionException ex) {
                Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
            }
});
        
        StringBuilder sb = new StringBuilder();
        
        for(ParallelTest t:tests){
        
            sb.append(">dataset\n");//appends the string to the file
            sb.append(t.getSequenceName()).append("\n");//appends the string to the file
            sb.append(">instances\n");
            for(String s:t.getResultSet()){
            
                sb.append(s);
                sb.append("\n");
                
            }
        
        }
        
//        System.out.println(String.format(
//                "RESULTS FROM CFG : %s  run number : %d \n", 
//                tests.get(0).getCfgName(),
//                tests.get(0).getRunNumber())
//        );
        System.out.println(sb.toString());
     
    }
    
    
    
}
