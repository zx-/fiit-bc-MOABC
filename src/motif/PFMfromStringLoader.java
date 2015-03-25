/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motif;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class PFMfromStringLoader implements PFMLoader{
    
    private final String sPFM;
    private double[][] positionFrequencyMatrix = null;
    
    public PFMfromStringLoader(String s){
    
        this.sPFM = s;
    
    }

    @Override
    public void loadPFM() throws motif.PFMNotLoadedException {
        
        if(positionFrequencyMatrix!=null)
            return;
        
        Scanner s = new Scanner(sPFM);
        Scanner sLine;
        String l;
        List<String> tokens = new LinkedList<>();
        ListIterator tokensIterator;
        int size,PFMSize = 0;
        
        
        for(int i = 0; i<4; i++){
        
            try{
                
                l = s.nextLine();
                sLine = new Scanner(l);
                
                tokens.clear();
                while(sLine.hasNext()){
                
                    tokens.add(sLine.next());
                
                }
                // ignore first Letter
                if(Character.isLetter(tokens.get(0).charAt(0))){
                
                    tokens.remove(0);
                
                }
                
                if(positionFrequencyMatrix == null){
                
                    positionFrequencyMatrix = new double[4][tokens.size()];
                    PFMSize = tokens.size();
                
                }
                
                size = tokens.size();
                
                if(size != PFMSize){
                
                    throw new motif.PFMNotLoadedException("Wrong line size at: "+i);
                
                }
                
                tokensIterator = tokens.listIterator();
                while(tokensIterator.hasNext()){
                
                    positionFrequencyMatrix[i][tokensIterator.nextIndex()] = 
                            Double.parseDouble((String)tokensIterator.next());
                
                }
                
                
                
            
            } catch(Exception e){
            
                throw new motif.PFMNotLoadedException(e);
            
            }
                    
        }
        
        s.close();
        
    }

    @Override
    public double[][] getPFM() throws motif.PFMNotLoadedException {
        
        if(positionFrequencyMatrix == null){
        
            throw new motif.PFMNotLoadedException("PFM not loaded");
        
        }
        
        return positionFrequencyMatrix;            
        
    }
    
    
}
