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
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import tester.db.PgDAO;
import tester.httpResults.ResultSubmitterHTTP;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ParallelTester {
    
    
    public static final int REPETITIONS = 5;

    private final PgDAO pgDAO; 
    private final ResultSubmitterHTTP submitter;
    
    public static void main(String args[]) throws IOException, SQLException{
    
        PgDAO p = new PgDAO();
        List<Configuration> configs = p.getAllConfigurations();
        
        int i = p.createNewRun(configs.get(0));
    
    }
    
    public ParallelTester(PgDAO pgDAO){
    
        this.pgDAO = pgDAO;
        submitter = new ResultSubmitterHTTP();
      
    }    
    
    public void test(List<DNASequence[]> sequenceList,List<Configuration> configList) throws InterruptedException, ExecutionException{
    
        List<List<ParallelTest>> testsListsPerRep = new ArrayList<>();        
        
        configList.forEach((c)->{  
            
            
            int runId = pgDAO.createNewRun(c);
            
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
                                new ParallelTest(seqArray, i, c, solver, runId)
                        );

                    }

                }
                
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
        
        futures.forEach(
            (f)->{ 
                try {
                    tests.add(f.get());
                } catch (InterruptedException ex) {
                    Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ExecutionException ex) {
                    Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        );
        
        String results = getResultStringFromTests(tests);
      
        pgDAO.insertDNAMotifs(results, tests.get(0).getRunId());
         
        try {
            
            InputStream stream = new ByteArrayInputStream(
                    results.getBytes(StandardCharsets.UTF_8));
            String response = 
                    submitter.submitResultFile(stream, "fiit-moabc"+tests.get(0).getRunId());
        
            ArrayList<HashMap<String, String>> parsedResults 
                    = submitter.getDocumentResults(response);
            
            pgDAO.insertParsedResults(parsedResults,tests.get(0).getRunId(),tests.get(0).getRunRepetition());
        
        } catch (IOException ex) {
            Logger.getLogger(ParallelTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        
     
    }
    
    
    private String printTestInfo(ParallelTest t){
    
        return String.format(
                "RESULTS FROM CFG : %s  run number : %d \n", 
                t.getCfgName(),
                t.getRunRepetition());
    
    }
    
    private String getResultStringFromTests(List<ParallelTest> tests){
    
        return getResultStringBuilderFromTests(tests).toString();
    
    }
    
    private StringBuilder getResultStringBuilderFromTests(List<ParallelTest> tests){
    
        StringBuilder sb = new StringBuilder();
        
        for(ParallelTest t:tests){
        
            if(!t.getResultSet().isEmpty()){
                
                sb.append(">dataset\n");
                sb.append(t.getSequenceName()).append("\n");
                sb.append(">instances\n");
                for(String s:t.getResultSet()){

                    sb.append(s);
                    sb.append("\n");

                }
                
            }
        }
        
        return sb;
    
    }
    
    
}
