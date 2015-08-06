/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester.db;

import artificial_bee_colony.Configuration;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class PgDAO {
    
    private org.postgresql.ds.PGPoolingDataSource ds;
    
    public PgDAO() throws FileNotFoundException, IOException, SQLException{
        
        Properties props = new Properties();
        FileInputStream fis = null;

        TimeZone timeZone = TimeZone.getTimeZone("GMT+1"); // e.g. "Europe/Rome"
        TimeZone.setDefault(timeZone);

        fis = new FileInputStream("src\\tester\\dbprops.properties");
        props.load(fis);
        ds = new org.postgresql.ds.PGPoolingDataSource();
        ds.setUrl(props.getProperty("PG_DB_URL"));
        ds.setUser(props.getProperty("PG_DB_USERNAME"));
        ds.setPassword(props.getProperty("PG_DB_PASSWORD"));

            //DATA_SOURCE = null;            
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        con = ds.getConnection();
        con.close();

    }
    
    public void insertDNAMotifs(String motifs, int runId){
    
        try {
            Connection con = null;
            con = ds.getConnection();
            
            Statement stmt = con.createStatement();
            stmt.execute(String.format(
                    "INSERT INTO motifs( run, motifs ) VALUES ( '%d', '%s' ) ",
                    runId, motifs));
            
            con.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(PgDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }
        
    
    
    public List<Configuration> getAllConfigurations(){
    
        List<Configuration> configs = new ArrayList<>();
        
        try {
            
            
            Connection con = null;
            Statement stmt = null;
            ResultSet rs = null;
            
            con = ds.getConnection();
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery("select * from configuration");
            
            while(rs.next()){
            
                Configuration c = new Configuration();
                c.id = rs.getInt("id");
                c.name = rs.getString("name");
                c.numberOfRepetitionsPerInput = 
                        rs.getInt("numberOfRepetitionsPerInput");
                c.motifMaxLength = rs.getInt("motifMaxLength");
                c.motifMinLength = rs.getInt("motifMinLength");
                c.maxIterations = rs.getInt("maxIterations");
                c.onlookersSize = rs.getInt("onlookersSize");
                c.workersSize = rs.getInt("workersSize");
                c.scoutsSize = rs.getInt("scoutsSize");
                c.supportTreshold = rs.getDouble("supportTreshold");
                c.motifMinComplexity = rs.getDouble("motifMinComplexity");
                c.OBLFepsilon = rs.getInt("OBLFepsilon");
                c.OBLFbeta = rs.getDouble("OBLFbeta");
                c.jumpRate = rs.getDouble("jumpRate");
                c.perturbationRate = rs.getDouble("perturbationRate");
                c.stepC = rs.getDouble("stepC");
                c.OBLFstepMultiplier = rs.getDouble("OBLFstepMultiplier");
                c.useLevy = rs.getBoolean("useLevy")?1:0;
                c.useObom = rs.getBoolean("useObom")?1:0;
                c.useSingleCrowdingDistance 
                        = rs.getBoolean("useSingleCrowdingDistance")?1:0;
                c.useSimilarityPrioritySorter 
                        = rs.getBoolean("useSimilarityPrioritySorter")?1:0;
                
                c.useEntropy = rs.getBoolean("useEntropy");
                
                configs.add(c);
            
            }
            
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PgDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return configs;
    
    } 

    public int createNewRun(Configuration c) {
  
        try {
            Connection con = null;
            con = ds.getConnection();
            
            Statement stmt = con.createStatement();
            stmt.execute(String.format(
                    "INSERT INTO run( configuration ) VALUES ( %d ) ",
                    c.id),
                    Statement.RETURN_GENERATED_KEYS);
            ResultSet keyset = stmt.getGeneratedKeys();
            
            keyset.next();
            
            con.close();
            
            return keyset.getInt("id");
            
        } catch (SQLException ex) {
            Logger.getLogger(PgDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;
    
    }

    public void insertParsedResults(ArrayList<HashMap<String, String>> parsedResults, int runId, int repetition) {
  
        try {
            
            Connection con = ds.getConnection();
            
            StringBuilder sb = new StringBuilder();
            
            
            Set<String> keys = parsedResults.get(0).keySet();
            ArrayList<String> keysArray = new ArrayList<>(keys);
            
            
            sb.append("INSERT INTO result");
            sb.append("( run, repetition, ");
            
            for(int i = 0; i< keysArray.size() ; i++){
            
                sb.append('"'+keysArray.get(i)+'"');
                if( i != keysArray.size()-1){                    
                    
                    sb.append(",");
                    
                } 
            
            }            
            
            sb.append(") VALUES ");
            
            System.out.println(sb.toString());
            
            for( HashMap<String, String> res:parsedResults ){
            
                sb.append("( "+runId+", "+repetition+", ");
                
                for(int i = 0; i< keysArray.size() ; i++){
            
                    sb.append("'"+res.get(keysArray.get(i))+"'");
                    
                    if( i != keysArray.size()-1){                    

                        sb.append(", ");

                    } 
            
                } 
                
                
                sb.append("),");
                                
            
            }
            
            sb.deleteCharAt(sb.length()-1);
            
            System.out.println(sb.toString());
            
            
            
            Statement stmt = con.createStatement();
            stmt.execute(sb.toString());
            
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PgDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    public List<Run> getRunListSwing() {
        
        List<Run> runs = new ArrayList<>();
        
        try {
            
            
            Connection con = null;
            Statement stmt = null;
            ResultSet rs = null;
            
            con = ds.getConnection();
            
            stmt = con.createStatement();
            
            rs = stmt.executeQuery(
                    "select run.id, run.configuration, configuration.name from run join configuration on run.configuration=configuration.id");
            
            while(rs.next()){
                
                runs.add(
                        new Run(
                                rs.getInt("id"),
                                rs.getInt("configuration"),
                                rs.getString("name")
                        )                
                );
            
            }
            
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PgDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return runs;
        
    }
    
    public List<List<String>> getResultsByRun(Run r, boolean totals){
    
        List<List<String>> resultTable = new ArrayList<>();
        
        try {
            
            
            Connection con = null;
            Statement stmt = null;
            ResultSet rs = null;
            
            con = ds.getConnection();
            
            stmt = con.createStatement();
            String query = "select * from result where run = "+r.getId();
            
            query = totals?query+" AND \"Data set\" = 'Total'":query;
            
            System.out.println(query);
            rs = stmt.executeQuery(query);
            
            ResultSetMetaData rsMetadata = rs.getMetaData();
            int columnCount = rsMetadata.getColumnCount();
            
            ArrayList<String> res = new ArrayList<>();
            
            for(int i = 1; i<= columnCount; i++){
            
                res.add(rsMetadata.getColumnName(i));
            
            }
            
            resultTable.add(res);
            
            while( rs.next() ){
                
                res = new ArrayList<>();
            
                for(int i = 1; i<= columnCount; i++){
            
                    res.add(rs.getString(i));
            
                }
                resultTable.add(res);
            
            }
          
            con.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(PgDAO.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return resultTable;
    
    }
    
}
