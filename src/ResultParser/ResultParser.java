/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultParser;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.sorter.ParetoFronts;
import artificial_bee_colony.bee.sorter.Sorter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ResultParser {

    
    private static int beesCheckSize = 20;
    
    private static int comparisonCounts[] = new int[SearchPhase.values().length*2]; 
    private static int levyCounts[] = new int[2];
    private static int currentParetoFrontSize = 0;
    public static Configuration currentConfiguration;
    
    private static ArrayList<Integer> comparisons = new ArrayList<>();
    private static ArrayList<Integer> improvementsLevy = new ArrayList<>();
    private static ArrayList<Integer> paretoFrontSize = new ArrayList<>();
    private static ArrayList<Double> colonyFitnesses = new ArrayList<>();
    
    private static ComparisonsFileWriter comparisonWriter = new ComparisonsFileWriter();
    private static LevyImprovementsFileWriter levyImprovementsWriter = new LevyImprovementsFileWriter();
    private static ParetoFrontWriter paretoFrontWriter = new ParetoFrontWriter();
    private static ResultWriter resultWriter = new ResultWriter();
    
    private static SearchPhase beeSearchPhase = SearchPhase.WORKER_PHASE;
    public static Boolean isDeepResultTest = true;
    public static Sorter beeSorter = null;
    
    public static void reset(){

        comparisonCounts = new int[SearchPhase.values().length * 2];
        levyCounts = new int[2];
        currentParetoFrontSize = 0;
        comparisons = new ArrayList<>();
        improvementsLevy = new ArrayList<>();
        paretoFrontSize = new ArrayList<>();
        colonyFitnesses = new ArrayList<>();
        comparisonWriter = new ComparisonsFileWriter();
        levyImprovementsWriter = new LevyImprovementsFileWriter();
        paretoFrontWriter = new ParetoFrontWriter();
        resultWriter = new ResultWriter();
  
    }
    
    public static void levyStepLength(double stepLength) {
  
        if(!isDeepResultTest)
            return;
        
        //System.out.println("Levy step: "+stepLength);
    
    }

    public static void levyImprove(boolean b) {
        
        if(!isDeepResultTest)
            return;
  
        levyCounts[b?0:1]++;
    
    }

    
    public static void parseFinalResults(Bee[] colony) {
        
        resultWriter
            .writeNewDataSet(currentConfiguration,colony[0].getDNADatasetName());
        
        if(beeSorter == null ) {
        
            for(int i=0; i< currentConfiguration.workersSize && i < 15; i++){

                resultWriter.addInstance(
                        currentConfiguration,
                        BeeResultParser.parseBeeMotif(colony[i])
                        );

            }
        
        } else {
        
            int i = 0;
            for(Bee b: beeSorter.getSortedListByObjective(colony, 2, currentConfiguration.workersSize, false)){
            
                resultWriter.addInstance(
                        currentConfiguration,
                        BeeResultParser.parseBeeMotif(b)
                        );
                
                if(++i > 15)
                    break;
            
            }
        
        }
        
        resultWriter.writeAddedInstances(currentConfiguration);
        
    }

    public static void parseColonyIteration(Bee[] colony, int iteration) {
    
        if(!isDeepResultTest)
            return;
        
        System.out.println("RP:  i: "+iteration);
        // Comparisons
        for(int i: comparisonCounts)
            comparisons.add(i);
        
        //Levy
        for(int i: levyCounts)
            improvementsLevy.add(i);
        
        paretoFrontSize.add(currentParetoFrontSize);
        
        
        currentParetoFrontSize = 0;
        Arrays.fill(comparisonCounts,0);
        Arrays.fill(levyCounts,0);
        
        // FITNESSES
        
        parseFitnesses(colony);
        
    }

    public static void satisfyBeeCreationTries(int k) {
    }

    public static void addSatisfiesConstraintBee(boolean b) {
    }

    public static void selectedBeeInSelector(int index, int limit) {
    }

    public static void paretoFrontsInSort(ParetoFronts paretoFronts) {
    }

    public static void iterationSearchEnd() {
        
        if(isDeepResultTest){
        
           comparisonWriter.write(comparisons,currentConfiguration);
           levyImprovementsWriter.write(improvementsLevy,currentConfiguration);
           paretoFrontWriter.write(colonyFitnesses,paretoFrontSize,beesCheckSize,currentConfiguration);
             
        }
        
    }
    
    public static void searchRunEnd(){
    
        resultWriter.writeToFiles();
        resultWriter.reset();
    
    }

    public static void iterationSearchStart(Configuration cfg) {
        
        
        
        if(isDeepResultTest){
        
            Arrays.fill(comparisonCounts,0);
            comparisons.clear();

            Arrays.fill(levyCounts,0);
            improvementsLevy.clear();
            colonyFitnesses.clear();
            paretoFrontSize.clear();
            currentParetoFrontSize = 0;
        
        }
        
    }

    public static void beeComparison(boolean dominated) {
        
        if(!isDeepResultTest)
            return;
        
        int index = beeSearchPhase.ordinal();
        index+=dominated?0:SearchPhase.values().length;
        
        comparisonCounts[index]++;
        
    }
    
    public static void printResults(){
        
  
        
    }

    public static void workerPhase() {
   
        beeSearchPhase = SearchPhase.WORKER_PHASE;
    
    }

    public static void onlookersPhase() {
   
        beeSearchPhase = SearchPhase.ONLOOKERS_PHASE;
    
    }
    
    public static void levyPhase() {

        beeSearchPhase = SearchPhase.LEVY_PHASE;
        
    }

    public static void scountsPhase() {
   
        beeSearchPhase = SearchPhase.SCOUNT_PHASE;
        
    }
    
    public static void obPhase() {
        
        beeSearchPhase = SearchPhase.OB_PHASE;
    
    }

    public static void paretoFrontSize(int size) {
        
        currentParetoFrontSize = size;
        System.out.println("Pareto size: "+size);
        
    }
        
    private static void parseFitnesses(Bee[] colony) {
   
        if(!isDeepResultTest)
            return;
        
        Bee b;
        for( int i = 0; i < colony.length && i < beesCheckSize; i++ ){
        
            b = colony[i];
            
            for( int j = 0; j < b.getNumOfSetObjectives(); j++){
            
                colonyFitnesses.add( b.getObjective(j, false) );
            
            }
        
        }
    
    }

    /**
     * @param aCurrentConfiguration the currentConfiguration to set
     */
    public static void setCurrentConfiguration(Configuration aCurrentConfiguration) {
        currentConfiguration = aCurrentConfiguration;
    }
    
}
