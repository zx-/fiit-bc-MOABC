/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.postprocessor;

import artificial_bee_colony.bee.Bee;
import artificial_bee_colony.bee.sorter.Sorter;
import java.util.ArrayList;
import java.util.List;

/**                                         
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class SimplePostprocessor {
    
    public StringMotifResult[] processColony(ArrayList<Bee> colony,Sorter sorter){
    
        Bee aColony[] = colony.toArray(new Bee[0]);
        
        List<Bee> firstPareto = sorter.sortColony(aColony);
        
        
        ArrayList<StringMotifResult> list = new ArrayList<>();
        
        int i = 0;
        
        for(int j= 0; j<colony.size(); j++){
            
            Bee b = colony.get(j);
        
            // similarity/
            // support
            
            // complexitY?
            
            if(b.getComplexity()> 0.75 && b.getObjectiveSupport(true) > 0.65 && b.getObjectiveSimilarity(true) > 0.65){
            
                list.add(StringMotifResult.getFromBee(b));
            
            }
            
            if(i++ > 15)
                break;
            
        
        }
        
        
        return list.toArray(new StringMotifResult[0]);
        
    
    }

    
}
