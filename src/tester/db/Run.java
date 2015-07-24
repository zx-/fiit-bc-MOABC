/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tester.db;

/**
 *
 * @author Róbert Cuprik <robertcuprik at hotmail.com>
 */
public class Run {
    
    private int id;
    private int configuration;

    
    public Run(int id,int cfgid){
    
        this.id = id;
        this.configuration = cfgid;
    
    }
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the configuration
     */
    public int getConfiguration() {
        return configuration;
    }

    /**
     * @param configuration the configuration to set
     */
    public void setConfiguration(int configuration) {
        this.configuration = configuration;
    }
    
    public String toString(){
    
        return "Run #"+id+" config #"+configuration;
    
    }
    
}
