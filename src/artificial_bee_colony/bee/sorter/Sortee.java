/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.sorter;

import artificial_bee_colony.bee.Bee;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
class Sortee implements Comparable<Sortee>{
    
    protected Bee bee;
    protected int rank = 0;
    protected double crowdingDistance = 0;
    protected int dominatedByCount = 0;
    protected List<Sortee> dominated = new ArrayList<>();
    private static HashMap<Integer,SorteeObjectiveComparator> comparators = new HashMap<>();
        
    protected static Comparator<Sortee> getComparatorByObjective(int objNumber){
        
        if(comparators.containsKey(objNumber))
            return comparators.get(objNumber);
        
        SorteeObjectiveComparator c = new SorteeObjectiveComparator(objNumber);
        comparators.put(objNumber, c);
        
        return c;
    
    } 

    @Override
    public int compareTo(Sortee o) {
        
        if(this.crowdingDistance < o.crowdingDistance)
            return 1;
        
        if(this.crowdingDistance > o.crowdingDistance)
            return -1;
                
        return 0;
        
    }
    
    
        
}
