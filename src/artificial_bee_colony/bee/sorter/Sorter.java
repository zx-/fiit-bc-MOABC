/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.sorter;

import ResultParser.ResultParser;
import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class Sorter {
    
    public static final Logger logger = Logger.getLogger( Sorter.class.getName() );
    Sortee[] sortees = null;
    ParetoFronts paretoFronts = new ParetoFronts();
    protected SingleCrowdingDistanceSorter crowdingDistanceSorter = null;
    
    
   
    public List<Bee> sortColony(Bee[] colony){
    
        int i,j,k;        
        
        // INITIALIZATION
        if( sortees == null || sortees.length < colony.length){
        
            sortees = new Sortee[colony.length];
            for( i = 0; i < sortees.length; i++)
                sortees[i] = new Sortee();
        
        } else {
        
            for( i = 0; i < sortees.length; i++)
                resetSortee(sortees[i]);
        
        }
        paretoFronts.resetParetoFronts();
        
        for( i = 0; i < colony.length; i++)
            sortees[i].bee = colony[i];
            
        // COMPARE SOLUTIONS & CREATE DOMINATION LISTS
        for( i = 0; i < colony.length; i++ ){
        
            for( j = 0; j < colony.length; j++){
            
                if( i != j ){
                
                    if(isDominating(sortees[i],sortees[j])){
                    
                        sortees[i].dominated.add(sortees[j]);
                    
                    } else if(isDominating(sortees[j],sortees[i])){
                    
                        sortees[i].dominatedByCount++;
                    
                    }
                
                }
            
            }
            
            // NON DOMINATED SOLUTION
            if(sortees[i].dominatedByCount == 0){
            
                sortees[i].rank = 0;
                paretoFronts.addToParetoFront(sortees[i], 0);
            
            }
        
        }
        
        //CREATE PARETO FRONTS
        
        createParetoFronts();

        
        if(logger.isLoggable(Level.FINE)){
        
            logger.log(Level.FINE,"Before sort");
            logger.log(Level.FINE,paretoFronts.toString());
        
        }       
        
        // SORT PARETO FRONTS BY CROWDING DISTANCE
        
        if(crowdingDistanceSorter == null){
            
            sortParetoFronts(colony);
        
        } else {
        
            crowdingDistanceSorter.sortParetoFronts(paretoFronts, colony);
        
        }
        
        
        
        
        paretoFronts.fill(colony);
        if(logger.isLoggable(Level.INFO)){
        
            logger.log(Level.INFO,"After sort");
            logger.log(Level.INFO,paretoFronts.toString());
        
        } 
        
        ResultParser.paretoFrontsInSort(paretoFronts);
        
        return getBeeListFromSorteeList(paretoFronts.getParetoFront(0));
    
    }      
    
    protected void createParetoFronts() {
        int  i = 0;
        while(!paretoFronts.isParetoFrontEmpty(i)){
        
            // s dominates b
            for( Sortee s: paretoFronts.getIterableFromParetoFront(i)){
            
                for( Sortee b: s.dominated ){
                
                    b.dominatedByCount--;
                    if(b.dominatedByCount == 0){
                    
                        b.rank = s.rank + 1;
                        paretoFronts.addToParetoFront(b, b.rank);
                    
                    }
                
                }
            
            }
            i++;
        
        }
    }

    
    protected boolean isDominating(Sortee a, Sortee b) {
        
        int pom = 0;
        
        for( int i = 0; i < a.bee.getNumOfSetObjectives(); i++){
        
            if(a.bee.getObjective(i,false) < b.bee.getObjective(i,false)){
            
                return false;
            
            } else if (a.bee.getObjective(i,false) > b.bee.getObjective(i,false)) {
            
                pom++;
            
            }
        
        }
        
        return pom > 0;
    
    }
    
    protected boolean isDominating(Bee a, Bee b) {
        
        int pom = 0;
        
        for( int i = 0; i < a.getNumOfSetObjectives(); i++){
        
            if(a.getObjective(i,false) < b.getObjective(i,false)){
            
                return false;
            
            } else if (a.getObjective(i,false) > b.getObjective(i,false)) {
            
                pom++;
            
            }
        
        }
        
        return pom > 0;
    
    }
        
    protected void resetSortee(Sortee s){
    
        s.bee = null;
        s.rank = 0;
        s.crowdingDistance = 0;
        s.dominated.clear();
        s.dominatedByCount = 0;
    
    }

    public boolean isBetter(Bee a, Bee b) {
        
                
        if(isDominating(a, b)){
            
            ResultParser.beeComparison(true);
            return true;
            
        }
        
        if(isDominating(b, a)){
        
            ResultParser.beeComparison(true);
            return false;
        
        }
      
        double s = 0;
        
        for(int i = 0; i < a.getNumOfSetObjectives(); i++){
        
            s += a.getObjective(i, true) - b.getObjective(i, true);
        
        }
        
        ResultParser.beeComparison(false);
        return s>0;
  
    }

    protected List<Bee> getBeeListFromSorteeList(List<Sortee> paretoFront) {
    
        List<Bee> l = new ArrayList<>();
        for(Sortee s:paretoFront)
            l.add(s.bee);
        
        return l;
    
    }

    protected void sortParetoFronts(Bee[] colony) {
        
        int j = 0, 
            i = 0,
            k = 0;
                
        while(!paretoFronts.isParetoFrontEmpty(i)){
        
            List<Sortee> c = paretoFronts.getParetoFront(i);
            for( j = 0; j < colony[0].getNumOfSetObjectives(); j++ ){
            
                Collections.sort(c, Sortee.getComparatorByObjective(j));
                
                c.get(0).crowdingDistance+=3;
                c.get(c.size() - 1).crowdingDistance+=3;
                for( k = 1; k < c.size() - 1 ; k++){
                
                    c.get(k).crowdingDistance += 
                            c.get(k+1).bee.getObjective(j,true) 
                            - c.get(k-1).bee.getObjective(j,true);
                
                }
            
            }
            
            Collections.sort(c);
            i++;
        
        }
    }

    /**
     * @return the sorter
     */
    public SingleCrowdingDistanceSorter getCDSorter() {
        return crowdingDistanceSorter;
    }

    /**
     * @param sorter the sorter to set
     */
    public void setCDSorter(SingleCrowdingDistanceSorter sorter) {
        this.crowdingDistanceSorter = sorter;
    }

    public List<Bee> getSortedListByObjective(Bee colony[],int objective, int limit,boolean ascending){
    
        List<Bee> list = new ArrayList<>();
        List<Sortee> listSortee = new ArrayList<>();
        
        for(int i = 0; i < limit && i < colony.length; i++){
        
            listSortee.add(new Sortee(colony[i]));
        
        }
        
        Collections.sort(listSortee, Sortee.getComparatorByObjective(objective));
        
        if(ascending){
        
            for(int i = 0; i < listSortee.size(); i++)
                list.add(listSortee.get(i).bee);
        
        } else {
        
            for(int i = listSortee.size() -1; i>=0; i--)
                list.add(listSortee.get(i).bee);
        
        }
        
        
        return list;
    
    }
    
    
}
