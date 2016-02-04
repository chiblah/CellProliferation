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

import java.util.*;

/**
 *
 * @author Kyata Chibalabala
 */
public class Simulation implements Runnable
{
  // Instance variables
  public static final int FEMALE = 1, MALE = 2; // "enums"
  private static final double STRAND_FULLY_LABELLED = 1.0, STRAND_UNLABELLED = 0.0, GENOME_UNLABELLED = 0.0;
  private static final boolean CAN_DIVIDE = true, CANT_DIVIDE = false;
  
	private int newest_generation = -1; // Keep track of the most recent generation of cells
	private int id_of_last_created_cell = -1; // The cell ID of the last cell that was created. Tracked to set the ID of the next cell to be created. 
  private final int haploid_number; 
  
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
   haploid_number = input_parameters.getHaploidNumber();
  }
  
  @Override
  public void run()
  {
    //Get the chromosome sizes for this simulation based on input parameters.
    List<String> genome_data_subset = GenomeData.getGenomeData(organism, sex);
    
    //Perform the simulation
    List<Cell> final_population = runSimulation();
    final_population.stream().forEach((Cell this_cell) -> 
    {
      System.out.println(this_cell.toString());
    });// For each cell in the final population 
    
    //Create a DataAnalysis object for this simulation
    new DataAnalysis(final_population, genome_data_subset, organism,sex);
  }
  
  //*** Helper methods ***//
  
  /**
   *
   *
   * 
   */
  private List<Cell> runSimulation()
  {
    //Initialise a population to be used at the start of the simulation
    List<Cell> cell_population = initiatePopulation(initial_population_size);
    System.out.println("Running =>" + input_parameters.toString());
    
    int population_size = cell_population.size();
    double[][][] blank_genome = newEmptyDiploidGenome();
		List<String> genome_data_subset = GenomeData.getGenomeData(organism, sex);
    
    //Progress through time at selected intervals for a specified duration
		for(int current_timepoint = 0; current_timepoint < simulation_duration; current_timepoint += time_interval)
    {
      System.out.println(current_timepoint + " " + population_size);
      //Perform division of all cells that can divide as determined by a random number generator and the set division threshhold
      //As new cells are being added to the list, only go through the cells present in the population before new cells were added in this round i.e use the previous population size as max number of iterations.
      for(int current_element = 0; current_element < population_size; current_element++)
      {
        Cell current_cell = cell_population.get(current_element);
        boolean current_cell_can_divide = current_cell.getDivisionStatus();
        if(current_cell_can_divide)
        {

          //Produce a random number, R, between ***********????!!!!!!!!!
          double between_zero_and_one = randomDouble();
          if(between_zero_and_one < 0.2) //The cell can divide
          {
            //Remove one cell from the current generation and add two to the next
            //by changing the generation of the original cell to current_gen + 1 and
            //creating a new cell object also in generation current_gen + 1

            final int daughter_cell_one = current_element; //The index of the current cell which becomes daughter cell one

            // Change the orginal cell to daughter cell 1 by increasing its generation value by 1, from its current generation
            //printString("Generation of parent cell = " + daughter_cell_one.getGeneration());
            //printString("Cell ID of parent cell = " + daughter_cell_one.getId());
            int next_generation = current_cell.getGeneration() + 1;
            cell_population.get(daughter_cell_one).setGeneration(next_generation);


            //Create a new cell object which will become daughter cell 2, with a blank diploid genome, of same generation as the newly created daughter cell 1
            cell_population.add(new  Cell(id_of_last_created_cell + 1, next_generation, -1, CAN_DIVIDE, blank_genome, GENOME_UNLABELLED));
            id_of_last_created_cell++;
            //System.out.println("Population size after division = " + cell_population.size());
            final int daughter_cell_two = (cell_population.size() - 1); // The index of daughter cell two, the newly created cell

            mitosis(cell_population, daughter_cell_one, daughter_cell_two);

            /*********************************************************/
            // Track the latest generation of cells.
            if(current_cell.getGeneration() > newest_generation)
            {newest_generation++;}
            /*********************************************************/
            
          }// if(between_zeroANDone >= 0.5)
          else //Cell doesn't divided
          {
            //System.out.println("Didn't divide --->" + current_cell.toString());
          }
        } // if the cell can divide

        //printString(Integer.toString(cell_population.get(current_element).getGeneration()));

      } // for all cells in the main population
      population_size = cell_population.size(); // Update the population size after the round of division
      System.out.println(current_timepoint + " " + population_size);
      new DataAnalysis(cell_population, genome_data_subset, organism,sex);
    }// for
    System.out.println("Latest generation = " + (newest_generation));
    return cell_population;  
  }// runSimulation
  
  /**
   * Models DNA synthesis during S-Phase, takes the genome from a mother cell and
   * produces two genomes for two daughter cells; also handles stochastic 
   * chromosome segregation into daughter cell one or two.
   * 
   * @param cell_one the integer value for the index of the mother cell which will become daughter cell one 
   * @param cell_two the integer value for the index of daughter cell two 
   */
  private void mitosis(List<Cell> population, int cell_one, int cell_two)
  {
    double[][][] temp_genome_one = population.get(cell_one).getGenome(), temp_genome_two = newEmptyDiploidGenome();
    
    for (int homologous_pair_count = 0; homologous_pair_count < temp_genome_one.length; homologous_pair_count++) 
    {// foreach homologous pair of the genome
      for (int chromosome_count = 0; chromosome_count < temp_genome_one[homologous_pair_count].length; chromosome_count++) 
      {// for each chromosome in each homologous pair
        for (int dna_strand_count = 0; dna_strand_count < temp_genome_one[homologous_pair_count][chromosome_count].length; dna_strand_count++) 
        {// for each DNA strand in the chromosome
          
          /* For each dna strand in this chromosome
          New strands will be formed such that the new combinations of double
          stranded DNA will be OS-NS and NS-OS (OS=Original Strand,
          NS=New Strand). The logic to produce these combinations of double
          stranded DNA is handled here.
          */
          
          if(dna_strand_count == 1)
          {// Ignore the first strand, do this on the iteration for the second strand
            temp_genome_two[homologous_pair_count][chromosome_count][dna_strand_count] = temp_genome_one[homologous_pair_count][chromosome_count][dna_strand_count];
            temp_genome_two[homologous_pair_count][chromosome_count][dna_strand_count-1]=1.0;
            
            temp_genome_one[homologous_pair_count][chromosome_count][dna_strand_count]=1.0;
          }
          
          //System.out.println(Double.toString(mother_cell_genome[homologous_pair_count][chromosome_count][dna_strand_count]));
        }// for each DNA strand in the chromosome
        
        // Perform the logic to model stochastic distribution of each double 
        // stranded DNA complex into either temp_genome_one or two
        
        
        if(homologous_pair_count==1)
        {// Is this the second homologous chromosome?
          double new_zero_to_one = randomDouble();
          if(new_zero_to_one >= 0.5)
          {// swap the chromosome between genomes
            double[] temp_chromosome_one, temp_chromosome_two;

            // Set the values of the temp chromosomes to the current chromosomes 
            // being evaluated from each chromosome
            temp_chromosome_one = temp_genome_one[homologous_pair_count][chromosome_count];
            temp_chromosome_two = temp_genome_two[homologous_pair_count][chromosome_count];

            // PRINT CHROMOSOME CONTENTS BEFORE SWAPPING AND AFTER SWAPPING

            // Swap the chromosomes
            temp_genome_one[homologous_pair_count][chromosome_count] = temp_chromosome_two;
            temp_genome_two[homologous_pair_count][chromosome_count] = temp_chromosome_one;

          }else
          {//Don't swap chromosomes 
          }
          
 
        }// If second homologous chromosome
        
      }// for each chromosome in a homologous pair
    }// for each homologous pair
    
    /*
    Mitosis logic, including stochastic chromosome segregartion into daughter 
    cells is complete, now write the two temp genomes back to the cells
    */
    population.get(cell_one).setGenome(temp_genome_one);
    population.get(cell_two).setGenome(temp_genome_two);
  }// mitosis
  
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
    private double[][][] newEmptyDiploidGenome()
    {
      double[][][] diploid_genome = new double[haploid_number][2][2]; // The karyotype of the cell, 3 dimensional array to store discrete values for each  chromosome -> each homologous pair of chromosomes -> 2 complementary DNA strands
      return diploid_genome;
    }// newEmptydiploidGenome()
    
    /**
     * Creates and returns an array list of the initial population of cells.
     * The total number of cells in the population is determined by the integer 'population_size'
     */
    private List<Cell> initiatePopulation(int required_population_size)
    {
      List<Cell> population = new ArrayList<>(); //Define an arraylist to hold the initial population of cells
      double[][][] diploid_genome = newEmptyDiploidGenome();
      int cell_generation = this.newest_generation + 1, last_div = -1;
        
    // Fill the empty genome with unlabelled DNA strands
    for(double[][] homologous_pair : diploid_genome) 
    {// foreach homologous pair of the genome
      for(double[] chromosome : homologous_pair) 
      {// for each chromosome in each homologous pair
        for(int dna_strand_count = 0; dna_strand_count < chromosome.length; dna_strand_count++) 
        {// for each dna strand in this chromosome
          chromosome[dna_strand_count] = STRAND_UNLABELLED;
          //System.out.println(Double.toString(STRAND_UNLABELLED));
        }
      }
    }
        
    // Create the starting population of cells, setting all cells to generation 0
    for (int counter = 0; counter < required_population_size; counter++)
    {
      int cell_id = counter;
      population.add(new  Cell(cell_id, cell_generation, last_div, CAN_DIVIDE, diploid_genome, GENOME_UNLABELLED)); // Create a new Cell object with the following values.
      id_of_last_created_cell = cell_id; // Track the id of the last created cell
    }// for
    this.newest_generation++;
    return population;
  }// initiate_first_population()
    
  //*** Access methods ***//
}
