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
public class Motif {
    
    private final double[][] possitionFrequencyMatrix;
    private final int length;
    
    private Motif(int l){
    
        this.length = l;
        this.possitionFrequencyMatrix = new double[4][this.length];
    
    }
        
    public static Motif createMotifFromPFM(double[][] pfm){
    
        Motif m = new Motif(pfm[0].length);
        
        m.setPFM(pfm);
        
        return m;
    
    }
    
    public static Motif createMotifFromPFMLoader(PFMLoader l) throws PFMNotLoadedException {
        
        l.loadPFM();
        return createMotifFromPFM(l.getPFM());
    
    }
    
    private void setPFM(double[][] pfm){
    
        for(int i = 0; i<4; i++){
            
            System.arraycopy(pfm[i], 0, this.possitionFrequencyMatrix[i], 0, pfm[0].length);
            
        }
    
    }
    
    public String toString(){        

        if(possitionFrequencyMatrix == null){
        
            return "Empty Matrix";
        
        }
        
        int precision = 3;
        StringBuilder sb = new StringBuilder(4+4*possitionFrequencyMatrix[0].length*(precision+2+1)+3);
        
        for(Nucleotide n:Nucleotide.values()){
        
            sb.append(n);
            for(int j = 0; j<length; j++)
                sb.append(" ").append(String.format("%."+precision+"f", possitionFrequencyMatrix[n.ordinal()][j]));
            
            sb.append(System.getProperty("line.separator"));
        
        }
        
        return sb.toString();
    
    }
    
    public double[][] getPFM(){

        return possitionFrequencyMatrix;
    
    }

    public int getLength() {
        
        return this.length;
    
    }
    
    
}
