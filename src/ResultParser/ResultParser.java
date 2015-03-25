/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ResultParser;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.sorter.ParetoFronts;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ResultParser {

    
    private static int beesCheckSize = 20;
    
    private static final int comparisonCounts[] = new int[SearchPhase.values().length*2]; 
    private static final int levyCounts[] = new int[2];
    private static int currentParetoFrontSize = 0;
    public static Configuration currentConfiguration;
    
    private static ArrayList<Integer> comparisons = new ArrayList<>();
    private static ArrayList<Integer> improvementsLevy = new ArrayList<>();
    private static ArrayList<Integer> paretoFrontSize = new ArrayList<>();
    private static ArrayList<Double> colonyFitnesses = new ArrayList<>();
    private static ComparisonsFileWriter comparisonWriter = new ComparisonsFileWriter();
    private static LevyImprovementsFileWriter levyImprovementsWriter = new LevyImprovementsFileWriter();
    private static ParetoFrontWriter paretoFrontWriter = new ParetoFrontWriter();
    
    private static SearchPhase beeSearchPhase = SearchPhase.WORKER_PHASE;
    
    
    public static void levyStepLength(double stepLength) {
  
        
    
    }

    public static void levyImprove(boolean b) {
  
        levyCounts[b?0:1]++;
    
    }

    
    public static void parseFinalResults(Bee[] colony) {
    }

    public static void parseColonyIteration(Bee[] colony, int iteration) {
    
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
        
        comparisonWriter.write(comparisons,currentConfiguration);
        levyImprovementsWriter.write(improvementsLevy,currentConfiguration);
        paretoFrontWriter.write(colonyFitnesses,paretoFrontSize,beesCheckSize,currentConfiguration);
        
    }

    public static void iterationSearchStart(Configuration cfg) {
        
        Arrays.fill(comparisonCounts,0);
        comparisons.clear();
        
        Arrays.fill(levyCounts,0);
        improvementsLevy.clear();
        colonyFitnesses.clear();
        paretoFrontSize.clear();
        currentParetoFrontSize = 0;
        
    }

    public static void beeComparison(boolean dominated) {
        
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
        
    }
        
    private static void parseFitnesses(Bee[] colony) {
   
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
