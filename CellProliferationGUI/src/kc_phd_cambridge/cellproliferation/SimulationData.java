/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kc_phd_cambridge.cellproliferation;

/**
 *
 * @author Kyata Chibalabala
 */
public class SimulationData 
{
  // Instance variables
  private String organism, sex;
  private int initial_population_size, simulation_duration, time_interval;
  
  // Constructor
  public SimulationData(String new_org, String new_sex, int new_init_pop_size, int new_sim_dur, int new_interval)
  {  
    this.organism = new_org;
    this.sex = new_sex;
    this.initial_population_size = new_init_pop_size;
    this.simulation_duration = new_sim_dur;
    this.time_interval = new_interval;    
  }// Constructor
  
}
