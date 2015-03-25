/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.randomSelector;

import ResultParser.ResultParser;
import artificial_bee_colony.bee.Bee;
import java.util.Random;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class RandomValuedSelection implements RandomSelector {

    Random r = new Random();
    @Override
    //https://www.fiitkar.sk/forum/viewtopic.php?f=111&t=2120&start=50
    public Bee select(Bee[] colony, int limit) {
        
        int sum = (1+limit)/2*limit;
        int num = r.nextInt(sum);
        int index = (int)Math.round( (-1 + Math.sqrt( 1 + 8*num ))/2 );
        ResultParser.selectedBeeInSelector(index,limit);
        
        return colony[index];
        
    }
    
}
