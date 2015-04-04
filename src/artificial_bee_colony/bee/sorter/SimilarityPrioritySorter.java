/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.sorter;

import ResultParser.ResultParser;
import artificial_bee_colony.bee.Bee;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class SimilarityPrioritySorter extends Sorter {
    
    
    @Override
    public boolean isBetter(Bee a, Bee b) {
        
                
        if(isDominating(a, b)){
            
            ResultParser.beeComparison(true);
            return true;
            
        }
        
        if(isDominating(b, a)){
        
            ResultParser.beeComparison(true);
            return false;
        
        }
        
        if(a.getObjective(1,false) >= b.getObjective(1, false) 
            && a.getObjective(2,false) > b.getObjective(2,false) ){
        
            return true;
        
        }
      
        double s = 0;
        
        for(int i = 0; i < a.getNumOfSetObjectives(); i++){
        
            s += a.getObjective(i, true) - b.getObjective(i, true);
        
        }
        
        ResultParser.beeComparison(false);
        return s>0;
  
    }
    
}
