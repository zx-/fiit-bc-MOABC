/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.evaluator;

import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public interface Evaluator {
    
    public void evaluate(Bee b, Configuration c);
    public void evaluate(Bee[] b, Configuration c);
    public void evaluate(Bee b);
    public void evaluate(Bee[] b);

    public void setConfiguration(Configuration c);
    
}
