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

import java.util.List;


/**
 *
 * @author Kyata Chibalabala
 */
public class DataAnalysis 
{
  private final List<Cell> cell_population;
  private final String organism;
  private final int sex;
  List<String> genome_data;
  private int total_number_of_bases_in_genome, total_labelled_bases_in_genome, fraction_of_genome_labelled;
  /**
   *
   * @param new_population the array list of cells in the final population. 
   * @param new_genome_data the string array of chromosome sizes required to
   * perform fractional DNA synthesis calculations.
   * @param new_organism the string representation of the organism.
   * @param new_sex th integer value of the sex of the organism.
   */
  public DataAnalysis(List<Cell> new_population, List<String> new_genome_data, String new_organism, int new_sex)
	{
    this.cell_population = new_population;
    this.genome_data = new_genome_data;
    this.organism = new_organism;
    this.sex = new_sex;
    
    total_number_of_bases_in_genome = GenomeData.getGenomeSize(this.organism, this.sex);
    int haploid_number = GenomeData.getHaploidNumber(this.organism);
    cell_population.stream().forEach((this_cell) -> 
    {
      //this_cell.getLabelDistribution();
      double[][][] genome = this_cell.getGenome();
      double total_labelled_bases = 0;
      for(int chromosome_count = 0; chromosome_count < genome.length; chromosome_count++)
      {
        for(int homologous_pair_count= 0; homologous_pair_count < genome[chromosome_count].length; homologous_pair_count++)
        {
          int chromosome_size = 0;
          String[] split_chromosome_sizes = genome_data.get(chromosome_count).split(",");
          if(homologous_pair_count==0)
          {//Homologous chromosome one
            chromosome_size = Integer.parseInt(split_chromosome_sizes[0]);
          }else
          {//Homologous chromosome two
            chromosome_size = Integer.parseInt(split_chromosome_sizes[1]);
          }
          for(int dna_strand_count = 0; dna_strand_count < genome[chromosome_count][homologous_pair_count].length; dna_strand_count++)
          {
            
            total_labelled_bases += genome[chromosome_count][homologous_pair_count][dna_strand_count]*chromosome_size;
            
            //System.out.println(""+total_labelled_bases);
            System.out.println(""+chromosome_size);
            //System.out.println("Chromosome " + Integer.toString(chromosome_count+1) + "; Homologous Pair " + Integer.toString(homologous_pair_count+1) + "; Strand " + Integer.  toString(dna_strand_count+1) + " - Label status = " + Double.toString(this.genome[chromosome_count][homologous_pair_count][dna_strand_count]));
          }
          //total_labelled_bases = 0;
        }
      }
      
      double fraction_labelled = total_labelled_bases/total_number_of_bases_in_genome;
      //DO SOMETHING WITH EACH CELL
      System.out.println("Cell: " + this_cell.getId() + ". Fraction labelled: " + fraction_labelled);
      System.out.println("Total labelled bases " + total_labelled_bases + "   Total bases in genome: " + total_number_of_bases_in_genome);
    });// For each cell in the final population 
    /*TODO
    1. Calculate B, the toal number of bases in the genome
    2. Calculated L, the number of bases in the genome thats are labelled
    3. Calculate F, the fraction of the genome that is labelled
    */
  } 
}// DataAnalysis
