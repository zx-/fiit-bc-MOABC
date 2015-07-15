/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.sorter;

import artificial_bee_colony.bee.Bee;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class SingleCrowdingDistanceSorter {
    
    
    private ArrayList<Integer> sameFitnessCounts = new ArrayList<>();
    
    
    protected void sortParetoFronts(ParetoFronts paretoFronts, Bee[] colony) {
        
        int j = 0, 
            i = 0,
            k = 0,
            curCount = 0,
            l,m,it;
        
        
                
        while(!paretoFronts.isParetoFrontEmpty(i)){
        
            List<Sortee> c = paretoFronts.getParetoFront(i);
            for( j = 0; j < colony[0].getNumOfSetObjectives(); j++ ){
            
                Collections.sort(c, Sortee.getComparatorByObjective(j));
                sameFitnessCounts.clear();
                
                curCount = 0;
                for(it = 0; it < c.size(); it++){
                
                    
                    if( it!= 0 
                            && c.get(it).bee.getObjective(j,false) != c.get(it - 1).bee.getObjective(j,false)){
                    
                        sameFitnessCounts.add(curCount);
                        curCount=0;
                        
                    
                    } 
                    curCount++;
                    
                
                }
                
                sameFitnessCounts.add(curCount);
                
                // borders
                for(it = 0; it < sameFitnessCounts.get(0); it++){
                
                    c.get(it).crowdingDistance+=3;
                
                }
                
                if(sameFitnessCounts.size() != 1){
                    
                    for(it = 0; it < sameFitnessCounts.get(sameFitnessCounts.size()-1); it++){

                        c.get(c.size() - 1 - it).crowdingDistance+=3;

                    }
                    
                }
                
                //c.get(0).crowdingDistance+=3;
                //c.get(c.size() - 1).crowdingDistance+=3;
                
                if(sameFitnessCounts.size() > 2){
                    k = 1;
                    l = 0;
                    m =  sameFitnessCounts.get(k) + sameFitnessCounts.get(k-1);
                    while(k < sameFitnessCounts.size()-1 ){

                        for(it = l+1; it < m ; it++){

                            c.get(it).crowdingDistance += 
                                c.get(m).bee.getObjective(j,true) 
                                - c.get(l).bee.getObjective(j,true);

                        }
                        l += sameFitnessCounts.get(k);
                        k++;
                        m += sameFitnessCounts.get(k);


                    }
                }
            
            }
            
            Collections.sort(c);
            i++;
        
        }
    }
    
}
