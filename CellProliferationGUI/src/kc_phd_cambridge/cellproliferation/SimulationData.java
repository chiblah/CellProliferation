/*
 * Copyright 2015 Kyata Chibalabala.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package kc_phd_cambridge.cellproliferation;

/**
 * Stores input parameters used to create and run a new Simulation.
 * Stores parameters provided by the user and is passed to the Simulation
 * object's constructor upon initiation of said object.
 * 
 * @see kc_phd_cambridge.cellproliferation.Simulation
 * @author Kyata Chibalabala
 */
public class SimulationData 
{
  // Instance variables
  final String organism;
  final int sex, initial_population_size, simulation_duration, time_interval;
  
  // Constructor
  public SimulationData(String new_org, int new_sex, int new_init_pop_size, int new_sim_dur, int new_interval)
  {  
    this.organism = new_org;
    this.sex = new_sex;
    this.initial_population_size = new_init_pop_size;
    this.simulation_duration = new_sim_dur;
    this.time_interval = new_interval;    
  }// Constructor
  
  // Helper methods
  @Override
  public String toString()
  {
    return "" + this.organism + " " + this.sex + " " + this.initial_population_size + " " + this.simulation_duration + " "+ this.time_interval; 
  }
}
