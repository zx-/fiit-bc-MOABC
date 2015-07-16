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
import artificial_bee_colony.bee.sorter.SimilarityPrioritySorter;
import artificial_bee_colony.bee.sorter.SingleCrowdingDistanceSorter;
import artificial_bee_colony.bee.sorter.Sorter;
import dna_generator.DNASequenceGenerator;
import dna_sequence.DNASequence;
import dna_sequence.DNASequenceLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import motif.Motif;
import motif.PFMLoader;
import motif.PFMNotLoadedException;
import motif.PFMfromStringLoader;
import tester.ParallelTester;
import tester.Tester;

/**
 *
 * @author 
 */
public class BC_DNA {

    private ArrayList<DNASequence[]> sequenceList = new ArrayList<>();
    private ArrayList<Configuration> configList = new ArrayList<>();
    private final MainForm frame;
    
    public BC_DNA(MainForm frame){
    
        this.frame = frame;
    
    }
    
    public void run(Boolean deepResults, Boolean usePriority,Boolean useSingleCD){
    
        Evaluator e = new GonzalezAlvarezEvaluator();
        Sorter s = usePriority?new SimilarityPrioritySorter():new Sorter();
        if(useSingleCD){
        
            s.setCDSorter(new SingleCrowdingDistanceSorter());
        
        }
        ResultParser.isDeepResultTest = deepResults;
//        double i = 1;
//        
//        for(DNASequence[] sequence: sequenceList){
//        
//            if(sequence.length > 1){
//            
//                Tester tester = new Tester
//                .TesterBuilder(sequence[0].getName())
//                    .evaluator(e)
//                    .sorter(s)
//                    .mutator(new BasicMutator(e))
//                    .dnaSequences(sequence)
//                    .configurations(configList.toArray(new Configuration[0]))
//                    .build();
//
//                tester.test();
//
//                ResultParser.printResults();   
//                
//            }         
//            
//            frame.printPercentage((i++/sequenceList.size()*100));
//            
//        }
//    
//        ResultParser.searchRunEnd();
        try {
            new ParallelTester().test(sequenceList, configList);
        } catch (InterruptedException ex) {
            Logger.getLogger(BC_DNA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(BC_DNA.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        frame.endSearch();
        
    }
    
    /**
     * @param args the command line arguments
     * @throws motif.PFMNotLoadedException
     */
//    public static void main(String[] args) throws PFMNotLoadedException, FileNotFoundException, IOException {
//        
//        
//        Configuration c = new Configuration();
//        Configuration[] configs = new Configuration[3];
//       // configs[0] = Configuration.getFromJsonFile(new File("assets\\configs\\def.json"));
//        configs[0] = Configuration.getFromJsonFile(new File("assets\\configs\\def3.json"));
//        configs[1] = Configuration.getFromJsonFile(new File("assets\\configs\\200b500it.json"));
//        configs[2] = Configuration.getFromJsonFile(new File("assets\\configs\\200b1000it.json"));
//        
//        
//        ArrayList<DNASequence[]> seqList = openSequences();
//       // ArrayList<DNASequence[]> seqList =new ArrayList<>();
//        
//        DNASequence[] seq = DNASequenceLoader.loadFromFile("assets\\sequences\\real\\hm20r.fasta");
//        //seqList.add(seq);
//          
////        for(int i= 0; i<size;i++)
////            System.out.println(seq[i].toJSON());
//        Evaluator e = new GonzalezAlvarezEvaluator(c);
//        Sorter s = new Sorter();
//        //Sorter s = new SimilarityPrioritySorter();
//        
//        for(DNASequence[] sequence: seqList){
//        
//            if(sequence.length > 1){
//            
//                Tester tester = new Tester
//                .TesterBuilder(sequence[0].getName())
//                    .evaluator(e)
//                    .sorter(s)
//                    .mutator(new BasicMutator(e))
//                    .dnaSequences(sequence)
//                    .configurations(configs)
//                    .build();
//
//                tester.test();
//
//                ResultParser.printResults();   
//                
//            }
//            
//        }
//        
//        
//        
//    }
//    
    public static ArrayList<DNASequence[]> openSequences(){
    
        File defDir = new File("assets\\sequences\\");
        Path dir = defDir.toPath();
        File f;
        
        ArrayList<DNASequence[]> seqList = new ArrayList<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file: stream) {

               f = file.toFile();
               if(f.isDirectory()){

                   DirectoryStream<Path> stream2 = Files.newDirectoryStream(f.toPath());

                   for(Path seq:stream2 ){

                       System.out.println(seq.toString());
                       seqList.add(DNASequenceLoader.loadFromFile(seq.toFile()));

                   }               

               }

            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }
        
        return seqList;
    }    

    public int loadSequencesFromFiles(File files[],boolean reset){
    
        FileFilter fastaFilter = new FileNameExtensionFilter("fasta","fasta");
        if(reset) sequenceList.clear();
        
        for(File f:files){
        
            if(f.isDirectory()){
            
                loadSequencesFromFiles(f.listFiles(),false);
            
            } else {
            
                if(fastaFilter.accept(f)){
                
                    try{
                        
                        DNASequence[] seq = DNASequenceLoader.loadFromFile(f);
                        sequenceList.add(seq);
                    
                    } catch ( Exception e){
                    
                    
                    }
                
                }
            
            }
            
        }
    
        return sequenceList.size();
        
    }
    
    public int loadConfigurationsFromFiles(File files[],boolean reset){
        
        FileFilter jsonFilter = new FileNameExtensionFilter("json","json");
        if(reset) configList.clear();
        
        for(File f:files){
        
            if(f.isDirectory()){
            
                loadConfigurationsFromFiles(f.listFiles(),false);
            
            } else {
            
                if(jsonFilter.accept(f)){
                
                    try{
                        
                        Configuration c = Configuration.getFromJsonFile(f);
                        configList.add(c);
                    
                    } catch ( Exception e){
                    
                    
                    }
                
                }
            
            }
            
        }
    
        return configList.size();
    }    
    
}
