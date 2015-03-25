/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.randomSelector;

import artificial_bee_colony.bee.Bee;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public interface RandomSelector {
    
    public Bee select(Bee[] colony,int limit);
    
}
