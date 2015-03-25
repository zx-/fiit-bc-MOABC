/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.sorter;

import java.util.Comparator;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
class SorteeObjectiveComparator implements Comparator<Sortee>{
    
    private final int objNumber;
    
    protected SorteeObjectiveComparator(int objNumber){
    
        this.objNumber = objNumber;
    
    }

    @Override
    public int compare(Sortee o1, Sortee o2) {
   
        if(o1.bee.getObjective(objNumber,false) > o2.bee.getObjective(objNumber,false))
            return 1;
        
        if(o1.bee.getObjective(objNumber,false) < o2.bee.getObjective(objNumber,false))
            return -1;
                
        return 0;
        
    }
    
}
