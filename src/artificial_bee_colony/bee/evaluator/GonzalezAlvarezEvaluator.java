/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package artificial_bee_colony.bee.evaluator;

import ResultParser.ResultParser;
import artificial_bee_colony.Configuration;
import artificial_bee_colony.bee.Bee;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import motif.Motif;
import motif.Nucleotide;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class GonzalezAlvarezEvaluator implements Evaluator{
    
    private Nucleotide[] nucleotides = Nucleotide.values();
    private double[] complexityMaxs;
    private Configuration cfg;
    
    public GonzalezAlvarezEvaluator(Configuration config){
    
        complexityMaxs = calculateComplexityMaximums(config);
        cfg = config;
    
    }
    
    public GonzalezAlvarezEvaluator(){
            
        cfg = new Configuration();
        complexityMaxs = calculateComplexityMaximums(cfg);
    
    }
    
    @Override
    public void setConfiguration(Configuration c){
    
        complexityMaxs = calculateComplexityMaximums(c);
        cfg = c;
    
    }

    @Override
    public void evaluate(Bee bee, Configuration config) {
        
        Nucleotide[][] solutionSeqs = bee.getSolutionSequences();
        Nucleotide[] consensus = getConsensus(solutionSeqs);
        double[][] pfm;
        int support,motifLenght;
        double similarity;
        double complexity;
        double entropy = 0;
        
        int treshold = (int) (config.supportTreshold * bee.getMotifLength());
        
        ArrayList<Nucleotide[]> aboveTreshSeqs 
                = getSeqsAboveThresh(solutionSeqs, consensus, treshold);
        support = aboveTreshSeqs.size();
        
        if(support > 0){  
        
            pfm = createPfm(aboveTreshSeqs);
            similarity = computeSimilarity(pfm);
            complexity = computeComplexity(
                getConsensus( aboveTreshSeqs.toArray(new Nucleotide[support][]))
            );
            Motif m = Motif.createMotifFromPFM(pfm);
        
//            System.out.print(m.toString());
//            System.out.println("Sup: "+support
//                    +" Sim: "+similarity
//                    +" Comp: "+complexity
//                    +" Length: "+m.getLength()
//                    +"\n");
            motifLenght = m.getLength();
            bee.setPFM(pfm);
            
            if( config.useEntropy ) 
                entropy = Entropy.compute(bee);
            
        } else {
        
            similarity = 0;
            complexity = 0;
            support = 0;
            motifLenght = 0;        
            entropy = 0;
        
        }      
        
        if( config.useEntropy ) {
        
            bee.setMultipleObjectives(motifLenght,support,similarity,entropy);
        
        } else {
        
            bee.setMultipleObjectives(motifLenght,support,similarity);
        
        }
        
        bee.setComplexity(complexity);
        
        if( motifLenght >= config.motifMinLength && motifLenght <= config.motifMaxLength 
            && complexity >= config.motifMinComplexity 
            && support >= config.getMinSupport(solutionSeqs.length))
        {
        
            bee.setSatisfiesConstraints(true);
            ResultParser.addSatisfiesConstraintBee(true);
        
        } else {
        
            bee.setSatisfiesConstraints(false);
            ResultParser.addSatisfiesConstraintBee(false);
        
        }
        
    }
    
    private Nucleotide[] getConsensus(Nucleotide[][] solutionSeqs){
    
        Nucleotide[] consensus = new Nucleotide[solutionSeqs[0].length];
        int[] hist = new int[nucleotides.length];
        int i,j;
        
        for( i = 0; i<consensus.length; i++ ){
        
            for( j = 0; j < nucleotides.length; j++)
                hist[j] = 0;
            
            for( j = 0; j < solutionSeqs.length; j++)
                hist[solutionSeqs[j][i].ordinal()]++;
            
            consensus[i] = maxFromHist(hist);
        
        }
        
        return consensus;
    
    }

    @Override
    public void evaluate(Bee[] bees, Configuration config) {
 
        for(Bee b:bees)
            evaluate(b,config);
    
    }

    private Nucleotide maxFromHist(int[] hist) {
        
        int max = 0;
        int pos = 0;
        
        for(int i = 0; i < hist.length; i++){
        
            if(hist[i] > max){
            
                max = hist[i];
                pos = i;
            
            }
        
        }
        
        return nucleotides[pos];
        
    }

    private ArrayList<Nucleotide[]> getSeqsAboveThresh(Nucleotide[][] solutionSeqs, Nucleotide[] consensus, int treshold) {
        
        ArrayList<Nucleotide[]> list = new ArrayList<>();
        int count,i,j;
        
        for( i = 0; i<solutionSeqs.length; i++){
        
            count = treshold;
            for( j = 0; j< consensus.length; j++){
            
                if(consensus[j] != solutionSeqs[i][j])
                    count--;
            
            }           
            
            if(count >=0)
                list.add(solutionSeqs[i]);
        
        }
        
        return list;
        
    }

    private double[][] createPfm(ArrayList<Nucleotide[]> seqs) {
        
        int i,seqCount = seqs.size();
        double[][] pfm = new double[nucleotides.length][seqs.get(0).length];
        
        for( i = 0; i < nucleotides.length; i++)
            Arrays.fill(pfm[i], 0);
        
        for( Nucleotide[] s:seqs ){
        
            for( i = 0; i < s.length; i++ ){
            
                pfm[s[i].ordinal()][i] += 1.0/seqCount;
            
            }
        
        }
        
        return pfm;
        
    }

    private double computeSimilarity(double[][] pfm) {
        
        double sim = 0;
        
        for(int i = 0; i < pfm[0].length; i++){
        
            sim += Math.max( 
                    Math.max( pfm[0][i], pfm[1][i]),
                    Math.max( pfm[2][i], pfm[3][i])
            );
        
        }
        
        sim /= pfm[0].length;
        
        return sim;
        
    }

    private double computeComplexity(Nucleotide[] seq) {
        
        double compl = 1;
        
        int[] hist = new int[nucleotides.length];
        Arrays.fill(hist,0);         
        for(Nucleotide n:seq)
            hist[n.ordinal()]++;
        
        for(int i:hist)
            compl *= CombinatoricsUtils.factorialDouble(i);
        
        compl = FastMath.log(
                4,
                CombinatoricsUtils.factorialDouble(seq.length)/compl
        );
        
        compl /= complexityMaxs[seq.length];
        
        return compl;
        
    }

    private double[] calculateComplexityMaximums(Configuration config) {
        
        int i,j;
        double[] maxs = new double[config.motifMaxLength + 1];
        int[] histogram = new int[nucleotides.length];
        
        Arrays.fill(histogram, 0);
        Arrays.fill(maxs, 1);
        
        for( i = 1 ; i < maxs.length; i++ ){
        
            histogram[ i % nucleotides.length ]++;
            
            for( j = 0; j < nucleotides.length; j++){
            
                maxs[i] *= CombinatoricsUtils.factorialDouble( histogram[j]);
            
            }
            
            maxs[i] = FastMath.log(
                    4,
                    CombinatoricsUtils.factorialDouble(i)/maxs[i]
            );
            
        
        }
        
        
        return maxs;
                
    }

    @Override
    public void evaluate(Bee b) {
   
        evaluate(b,cfg);
    
    }

    @Override
    public void evaluate(Bee[] b) {
   
        evaluate(b,cfg);
    
    }
    
}
