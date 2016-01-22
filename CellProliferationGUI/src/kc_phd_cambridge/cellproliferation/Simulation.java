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

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import static kc_phd_cambridge.cellproliferation.FXMLMainWindowController.new_line;
import static kc_phd_cambridge.cellproliferation.FXMLMainWindowController.tab;

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
  
  // The ArrayList to store the growing cell population
  private CopyOnWriteArrayList<Cell> cell_population = new CopyOnWriteArrayList<>(); 
  
  // The ArrayList to store the appropriate subset of genome data
  List<String> genome_data_subset = new ArrayList<>();
  double[][][] blank_genome;
  
  // Instance variables passed to constructor
  private final SimulationData input_parameters;
  
  // Local input parameters to be read from the SimulationData object receieved by the constructor
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
   blank_genome = newEmptyDiploidGenome();
  }
  
  @Override
  public void run()
  {
    //Get the chromosome sizes for this simulation according to input parameters.
    genome_data_subset = GenomeData.getGenomeData(organism, sex);
    
    //Perform the simulation
    List<Cell> final_population = runSimulation();
    
    //Create a DataAnalysis object for this simulation
    //new DataAnalysis(final_population, genome_data_subset, organism,sex);
    
    genome_data_subset.stream().forEach((String line) -> 
    {
      //System.out.println("Genome data" + line);
    });// For each cell in the final population 
  }
  
  //*** Helper methods ***//
  
  /**
   * Track population dynamics of a dividing cell population.
   *
   * 
   */
  private List<Cell> runSimulation()
  {
    //Initialise a population to be used at the start of the simulation
    cell_population = initiatePopulation(initial_population_size);
    System.out.println("Running =>" + input_parameters.toString());

    // Track the size of the current population before more cells are addded
    int index_of_final_cell_to_evaluate = cell_population.size()-1;
    Cell final_cell_to_evaluate = cell_population.get(cell_population.size()-1);
    

    try
    {
      String output_file_name = input_parameters.toString();
      try(FileWriter output_writer = new FileWriter(output_file_name)) 
      {
        for(int current_time = 0; current_time < simulation_duration; current_time+=time_interval)
        {// at each time interval - evaluate and track cell population dynamics
          System.out.println(current_time + " <--population size at beginning = " + cell_population.size());
          Iterator<Cell> current_population = cell_population.iterator();
          
          int mother_cell_index_tracker = 0;
          while (current_population.hasNext()) 
          {// for each cell in the current population
            Cell mother_cell = current_population.next();
            if(mother_cell.getDivisionStatus())
            {// current cell can divide
              
              //Produce a random number, R, between 0 and 1. Divide if division threshold attained
              double number_between_zero_and_one = randomDouble();
              if(number_between_zero_and_one < 0.2) //The cell can divide
              {// If division threshold attained
                
                // Mother cell becomes daughter cell one
                int next_generation = mother_cell.getGeneration() + 1; 
                mother_cell.setGeneration(next_generation);// Increase the cell's generation number - making it daughter cell one
                Cell daughter_cell_one = new Cell(mother_cell); // Perform a deep copy of the mother cell
                
      
                // Create a new cell object which will become daughter cell 2, with a blank diploid genome, 
                // of same generation and cell lineage as daughter cell one
                int id_of_newly_created_cell = id_of_last_created_cell + 1;
                int lineage_of_newly_created_cell = daughter_cell_one.getLineageId();
                Cell new_cell = new  Cell(id_of_newly_created_cell, lineage_of_newly_created_cell, next_generation, -1, CAN_DIVIDE, blank_genome);
                cell_population.add(new_cell);
                
                Cell daughter_cell_two = new Cell(new_cell);// Perform a deep copy of the new cell
                
                //Track the array indices of the two daughter cells
                int index_of_daughter_cell_one, index_of_daughter_cell_two;
                index_of_daughter_cell_one = mother_cell_index_tracker;
                index_of_daughter_cell_two = cell_population.size()-1;
                
                //Mark unused cell variables for garbage collection
                mother_cell = null; new_cell = null;
                
                // Keep track the latest generation of cells and the id of the last created cell
                id_of_last_created_cell++;
                if(daughter_cell_one.getGeneration() > newest_generation)
                {newest_generation++;}
                
                
                // Perform S-phase for daughter cells
                System.out.println("Main copies before S-Phase" + new_line + cell_population.get(index_of_daughter_cell_one).toString());
                System.out.println(cell_population.get(index_of_daughter_cell_two).toString());
                performSPhase(daughter_cell_one, daughter_cell_two);
                cell_population.set(index_of_daughter_cell_one, daughter_cell_one);
                cell_population.set(index_of_daughter_cell_two, daughter_cell_one);
                System.out.println("Second copies after S-Phase" + new_line + daughter_cell_one.toString());
                System.out.println(daughter_cell_two.toString());
                System.out.println("Main copies after S-Phase" + new_line + cell_population.get(index_of_daughter_cell_one).toString());
                System.out.println(cell_population.get(index_of_daughter_cell_two).toString());
                
              }// If simulation threshold attained
            }// current cell can divide
            else
            {
              // current cell cannot divide, do nothing
            }// if current cell can divide - end of else

            //perform(current_cell); 
            //System.out.println(current_cell.toString());
            mother_cell_index_tracker++;
          }// for each cell in the current population

          
          System.out.println(current_time + " <--population size at end of timepoint = " + cell_population.size());
        }// at each time interval
      }// try(FileWriter writer
    }// try block
    catch(IOException e)
    {
    } 
    return cell_population; 
  }// runSimulation
  
  private void performSPhase(Cell cell_one, Cell cell_two)
  {
    cell_one.setFractionGenomeLabelled(STRAND_FULLY_LABELLED);
    cell_two.setFractionGenomeLabelled(200.0);
  }
  
  /**
   * Models DNA synthesis during S-Phase, takes the genome from a mother cell and
   * produces two genomes for two daughter cells; also handles stochastic 
   * chromosome segregation into daughter cell one or two.
   * 
   * @param cell_one the integer value for the index of the mother cell which will become daughter cell one 
   * @param cell_two the integer value for the index of daughter cell two 
   */
  private void mitosis(int cell_one, int cell_two)
  {
    
  }// mitosis
  
  public void calculateCellFractionLabelled(int cell_index)
  {
    double[][] chromosome_labelled_bases = new double[haploid_number][2];
    long total_labelled_bases_in_genome, total_number_of_bases_in_genome;
      
    total_labelled_bases_in_genome = 0;
    total_number_of_bases_in_genome = GenomeData.getGenomeSize(organism, sex);

    for(int chromosome_count = 0; chromosome_count < cell_population.get(cell_index).getGenome().length; chromosome_count++)
    {// For each homologous pair
      double chromo_labelled_bases;
      for(int homologous_pair_count= 0; homologous_pair_count < cell_population.get(cell_index).getGenome().length; homologous_pair_count++)
      {// For each chromosome in a homologous pair
        int chromosome_size;
        String[] split_chromosome_sizes = genome_data_subset.get(chromosome_count).split(",");
        if(homologous_pair_count==0)
        {//Homologous chromosome one
          chromosome_size = Integer.parseInt(split_chromosome_sizes[0]);
        }else
        {//Homologous chromosome two
          chromosome_size = Integer.parseInt(split_chromosome_sizes[1]);
        }
        for(int dna_strand_count = 0; dna_strand_count < cell_population.get(cell_index).getGenome()[chromosome_count][homologous_pair_count].length; dna_strand_count++)
        {// For each DNA strand in the chromosome 
          double bases_labelled_on_strand = cell_population.get(cell_index).getGenome()[chromosome_count][homologous_pair_count][dna_strand_count];
          total_labelled_bases_in_genome += bases_labelled_on_strand*(double)chromosome_size; 
          chromo_labelled_bases =(bases_labelled_on_strand*(double)chromosome_size);    
          chromosome_labelled_bases[chromosome_count][homologous_pair_count] = chromo_labelled_bases;
        }// For each DNA strand
      }// For each chromosome in a homologous pair
    }// For each homologous pair
    cell_population.get(cell_index).setFractionGenomeLabelled(total_labelled_bases_in_genome/(double)total_number_of_bases_in_genome);
  }// calculateCellFractionLabelled
  
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
  private CopyOnWriteArrayList<Cell> initiatePopulation(int required_population_size)
  {
    CopyOnWriteArrayList<Cell> population = new CopyOnWriteArrayList<>(); //Define an arraylist to hold the initial population of cells
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
      
      // Create a new Cell object, cell IDs ranging from 0 to population size, cell
      // lineage IDs are also identical to the cell IDs of each cell in the initial
      // population, all generation 0 with an unlabelled diploid genome
      population.add(new  Cell(cell_id, cell_id, cell_generation, last_div, CAN_DIVIDE, diploid_genome)); 
      id_of_last_created_cell = cell_id; // Track the id of the last created cell
    }// for
    this.newest_generation++;
    return population;
  }// initiate_first_population()
 
  //*** Access methods ***//
}
