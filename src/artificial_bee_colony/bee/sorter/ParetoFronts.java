/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.sorter;

import artificial_bee_colony.bee.Bee;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class ParetoFronts {
    
    HashMap<Integer,ArrayList<Sortee>> paretoFronts = new HashMap<>();
    
        
    protected void resetParetoFronts() {
    
        for( ArrayList<Sortee> a : paretoFronts.values()){
        
            a.clear();
        
        }
        
    }
    
    
    protected Iterable<Sortee> getIterableFromParetoFront(int i) {
    
        return paretoFronts.get(i);
    
    }
    
    protected boolean isParetoFrontEmpty(int i){
    
        return !paretoFronts.containsKey(i) || paretoFronts.get(i).isEmpty();
    
    }
    
    protected void addToParetoFront(Sortee s, int rank){
    
        if(paretoFronts.containsKey(rank)){
        
            paretoFronts.get(rank).add(s);
        
        } else {
        
            ArrayList<Sortee> a = new ArrayList<>();
            a.add(s);
            paretoFronts.put(rank, a);
        
        }
    
    }
    
    protected List<Sortee> getParetoFront(int i){
    
        return paretoFronts.get(i);
    
    }

    protected void fill(Bee[] colony) {
        
        int i = 0;
        for( ArrayList<Sortee> a : paretoFronts.values()){
        
            for( Sortee s: a){
            
                colony[i++] = s.bee;
            
            }
        
        }
        
    }
    
    public String toString(){
    
        StringBuilder sb = new StringBuilder();
        
        int j = 0 ,i = 0,k;
        for( ArrayList<Sortee> a : paretoFronts.values()){
            
            sb.append("Front rank: "+j+"\n");
            j++;
            i=0;
            
            for( Sortee s: a){
            
                sb.append("Sortee:\n");
                sb.append(String.format("Pos: %d rank: %d dist %2.3f \n",
                        i,s.rank,s.crowdingDistance));
                
                for( k = 0 ; k < s.bee.getNumOfSetObjectives(); k++){
                
                    sb.append(String.format("Obj %d: %2.3f ",
                            k,s.bee.getObjective(k,false)));
                
                }
                sb.append("\n");
                
                i++;
            
            }
            
            sb.append("\n");
            
        }
        
        return sb.toString();
    
    }
    
}
