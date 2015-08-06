/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.evaluator;

import artificial_bee_colony.bee.Bee;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class Entropy {
    
    public static double compute(Bee b){
    
        double entropy = 0;
    
        double[][] pfm = b.getPFM();
        
        for( int i = 0; i < pfm[0].length; i++ ){
        
            double en = 0;
            for( int n = 0; n < pfm.length; n++ ){
            
                if( pfm[n][i] != 0 )
                    en += pfm[n][i] *  FastMath.log(2, pfm[n][i]);
            
            }
            en *= -1;
            en = 2 - en;
            en /= 2;
            
            entropy += en;
        
        }
        
        entropy /= pfm[0].length;
        
        
        return entropy;
        
    }
    
}
