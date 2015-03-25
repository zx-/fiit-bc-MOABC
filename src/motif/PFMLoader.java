/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motif;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public interface PFMLoader {
    
    public void loadPFM() throws motif.PFMNotLoadedException;
    public double[][] getPFM()throws motif.PFMNotLoadedException;
    
}
