/*
 * Copyright 2015 Kyata.
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static kc_phd_cambridge.cellproliferation.FXMLMainWindowController.new_line;
import static kc_phd_cambridge.cellproliferation.FXMLMainWindowController.tab;


/**
 *
 * @author Kyata Chibalabala
 */
public class DataAnalysis 
{
  //private final List<Cell> cell_population;
  private final String organism;
  private final int sex;
  private final List<String> genome_data;
  private final int  haploid_number;
  private long total_number_of_bases_in_genome;
  private double fraction_of_genome_labelled;
  private int total_labelled_bases_in_genome, latest_generation, total_number_of_lineages;
  
  /**
   *
   * @param new_population the array list of cells in the final population. 
   * @param new_genome_data the string array of chromosome sizes required to
   * perform fractional DNA synthesis calculations.
   * @param new_organism the string representation of the organism.
   * @param new_sex the integer value of the sex of the organism.
   */
  public DataAnalysis(String file_name, List<String> new_genome_data, String new_organism, int new_sex, int new_latest_generation, int initial_population_size)
	{
    //this.cell_population = new_population;
    this.genome_data = new_genome_data;
    this.organism = new_organism;
    this.sex = new_sex;
    this.total_number_of_bases_in_genome = GenomeData.getGenomeSize(this.organism, this.sex);
    this.haploid_number = GenomeData.getHaploidNumber(this.organism);
    this.latest_generation = new_latest_generation;
    this.total_number_of_lineages = initial_population_size;
    
    int[][] expected_number_of_cells_in_each_generation = new int[latest_generation+1][1];
    String[][] generation_percentages = new String[total_number_of_lineages][latest_generation+1];
    
    System.out.println(total_number_of_lineages + " " + latest_generation);
    
    int cells_in_generation = 1;
    expected_number_of_cells_in_each_generation[0][0] = cells_in_generation;
    for(int count = 1; count < expected_number_of_cells_in_each_generation.length; count++)
    {
      cells_in_generation = cells_in_generation * 2;
      expected_number_of_cells_in_each_generation[count][0] = cells_in_generation;
    }
    for(int count = 0; count < expected_number_of_cells_in_each_generation.length; count++)
    {
      System.out.println("Generation: " + count + " Number of cells = " + expected_number_of_cells_in_each_generation[count][0]);
    }
    
    for(int count = 0; count < generation_percentages.length; count++)
    {
      for(int count2 = 0; count2 < generation_percentages[count].length; count2++)
      {
        System.out.println("dimension1 " + generation_percentages.length + "dim 2 " + generation_percentages[count2].length);
        System.out.println(count + " " + count2);
        generation_percentages[count][count2] = ("");    
      }
    }
    
    FileReader fr; 
    try 
    {
      fr = new FileReader(file_name);
      BufferedReader br = new BufferedReader(fr); 
      String s; 
      while((s = br.readLine()) != null) 
      { 
        String[] split_line = s.split(tab + tab);
        //while(split_line[0] !=)
        int cell_id, cell_generation, cell_lineage; 
        double percentage_labelled;
        cell_id = Integer.parseInt(split_line[0]);
        cell_generation = Integer.parseInt(split_line[1]);
        cell_lineage = Integer.parseInt(split_line[2]);
        percentage_labelled = Double.parseDouble(split_line[3]);
        
        System.out.println(cell_lineage +" " +cell_generation);
        String current = generation_percentages[cell_lineage][cell_generation];
        generation_percentages[cell_lineage][cell_generation] = current + percentage_labelled;
        
        System.out.println(generation_percentages[cell_lineage][cell_generation]); 
      } 
      fr.close();
    } catch (FileNotFoundException ex) 
    {
      Logger.getLogger(DataAnalysis.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) 
    {
      Logger.getLogger(DataAnalysis.class.getName()).log(Level.SEVERE, null, ex);
    }
     for(int count1 = 0; count1 < generation_percentages.length; count1++)
     {
       for(int count2 = 0; count2 < generation_percentages[count1].length; count1++)
       {
         System.out.println("" + count1 + " " + count2 + "Dim one length - 1 = " + (generation_percentages.length-1));
         System.out.println("Lineage: " + count1 +" Generation:" + count2 + " = " + generation_percentages[count1][count2]); 
       }
     }
    
    
    /*TODO
    1. Calculate B, the toal number of bases in the genome
    2. Calculated L, the number of bases in the genome thats are labelled
    3. Calculate F, the fraction of the genome that is labelled
    */
  } 
  
  private void getCellLabelDistribution(Cell new_cell)
  {
    double[][] chromosome_labelled_bases = new double[haploid_number][2];
    double[][][] genome = new_cell.getGenome();
    total_labelled_bases_in_genome = 0;
    //System.out.println("Cell " + new_cell.getId() + ": Generation " + new_cell.getGeneration());
    for(int chromosome_count = 0; chromosome_count < genome.length; chromosome_count++)
    {// For each homologous pair
      double chromo_labelled_bases = 0;
      for(int homologous_pair_count= 0; homologous_pair_count < genome[chromosome_count].length; homologous_pair_count++)
      {// For each chromosome in a homologous pair
        int chromosome_size;
        
        String[] split_chromosome_sizes = genome_data.get(chromosome_count).split(",");
        if(homologous_pair_count==0)
        {//Homologous chromosome one
          chromosome_size = Integer.parseInt(split_chromosome_sizes[0]);
        }else
        {//Homologous chromosome two
          chromosome_size = Integer.parseInt(split_chromosome_sizes[1]);
        }
        for(int dna_strand_count = 0; dna_strand_count < genome[chromosome_count][homologous_pair_count].length; dna_strand_count++)
        {// For each DNA strand in the chromosome 
          
          //System.out.println(genome[chromosome_count][homologous_pair_count][dna_strand_count]);
                  
          double bases_labelled_on_strand = genome[chromosome_count][homologous_pair_count][dna_strand_count];
          //System.out.println("Strand bases labelled " + bases_labelled_on_strand);
          total_labelled_bases_in_genome += bases_labelled_on_strand*(double)chromosome_size; 
          
          chromo_labelled_bases =(bases_labelled_on_strand*(double)chromosome_size);          
        }// For each DNA strand
        chromosome_labelled_bases[chromosome_count][homologous_pair_count] = chromo_labelled_bases;
      }// For each chromosome in a homologous pair
    }// For each homologous pair
    fraction_of_genome_labelled = (total_labelled_bases_in_genome/(double)total_number_of_bases_in_genome);
    new_cell.setFractionGenomeLabelled(fraction_of_genome_labelled);
  }// getCellLabelDistribution
  
  private void writeToFile(List<String> file_contents, String file_name)
  {
    try
    {
      try(FileWriter writer = new FileWriter("Data_Out.txt")) {
        for(String line : file_contents)
        {
          writer.append(line);
          writer.append(new_line);
        }
        //generate whatever data you want
        writer.flush();
        writer.close();
      }
    }
    catch(IOException e)
    {
    }
  }
}// DataAnalysis
