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

import java.io.*;
import java.lang.Math.*;
import java.util.*;

/**
 *
 * @author Kyata Chibalabala
 */
public class Simulation implements Runnable
{
  // Instance variables
  private static final int FEMALE = 1, MALE = 2; // "enums"
  private static final double STRAND_FULLY_LABELLED = 1.0, STRAND_UNLABELLED = 0.0;
  private static final boolean CAN_DIVIDE = true, CANT_DIVIDE = false;
  
  private static int current_timepoint = 0; // Track the current time in this simulation, initialise at 0.
	private static int newest_generation = -1; // Keep track of the most recent generation of cells
	private static int id_of_last_created_cell = -1; // The cell ID of the last cell that was created. Tracked to set the ID of the next cell to be created. 
  private static int haploid_number = 2; // TODO: Set this value dynamically!!!!!!**********
  
  // Instance variables passed to constructor
  private final SimulationData input_parameters;
  
  // Local input parameters to be read from the SimulationData object passed to the constructor
  String organism;
  int sex;
  int initial_population_size;
  int simulation_duration;
  int time_interval;
   
  // Constructor receives a SimulationData object
  public Simulation(SimulationData new_input_parameters) 
  {
    this.input_parameters = new_input_parameters;
    
   // Set local input parameters by reading from the SimulationData object passed to the constructor
   organism = input_parameters.getOrganism();
   sex = input_parameters.getSex();
   initial_population_size = input_parameters.getInitialPopulationSize();
   simulation_duration = input_parameters.getSimulationDuration();
   time_interval = input_parameters.getTimeInterval();
  }
  @Override
  public void run()
  {
    //Initialise a population to be used at the start of the simulation
    List<Cell> cell_population = initiatePopulation(initial_population_size); 

    System.out.println("MyRunnable running =>" + input_parameters.toString() + " Population size = " + cell_population.size());
  }
  
  //*** Helper methods ***//
  
  /**
   *
   */
    private static double randomDouble()
    {
      Random rand = new Random();
      return rand.nextDouble();
    }// randomDouble
  
    /**
     * Creates and returns an empty three dimensional array to store a diploid genome.
     */
    private static double[][][] newEmptyDiploidGenome()
    {
      double[][][] diploid_genome = new double[haploid_number][2][2]; // The karyotype of the cell, 3 dimensional array to store discrete values for each  chromosome -> each homologous pair of chromosomes -> 2 complementary DNA strands
      return diploid_genome;
    }// newEmptydiploidGenome()
    
    /**
     * Creates and returns an array list of the initial population of cells.
     * The total number of cells in the population is determined by the integer 'population_size'
     */
    private static List<Cell> initiatePopulation(int required_population_size)
    {
      List<Cell> population = new ArrayList<>(); //Define an arraylist to hold the initial population of cells
      double[][][] diploid_genome = newEmptyDiploidGenome();
      int cell_generation = newest_generation + 1, last_div = -1;
        
    // Fill the empty genome with unlabelled DNA strands
    for(double[][] diploid_genome1 : diploid_genome) 
    {
      for(double[] diploid_genome11 : diploid_genome1) 
      {
        for(int dna_strand_count = 0; dna_strand_count < diploid_genome11.length; dna_strand_count++) 
        {
          diploid_genome11[dna_strand_count] = STRAND_UNLABELLED;
        }
      }
    }
        
    // Create the starting population of cells, setting all cells to generation 0
    for (int counter = 0; counter < required_population_size; counter++)
    {
      int cell_id = counter;
      population.add(new  Cell(cell_id, cell_generation, last_div, CAN_DIVIDE, diploid_genome)); // Create a new Cell object with the following values.
      id_of_last_created_cell = cell_id; // Track the id of the last created cell
    }// for
    newest_generation++;
    return population;
  }// initiate_first_population()
    
  //*** Access methods ***//
}
