/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.mutator;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public interface Mutator {
    
    public Bee mutate(Bee b, Bee[] colony);
    public void setConfiguration(Configuration c);
    
}
