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
  private final List<String> genome_data;
  private final int total_number_of_bases_in_genome, haploid_number;
  private double fraction_of_genome_labelled;
  private int total_labelled_bases_in_genome;
  
  /**
   *
   * @param new_population the array list of cells in the final population. 
   * @param new_genome_data the string array of chromosome sizes required to
   * perform fractional DNA synthesis calculations.
   * @param new_organism the string representation of the organism.
   * @param new_sex the integer value of the sex of the organism.
   */
  public DataAnalysis(List<Cell> new_population, List<String> new_genome_data, String new_organism, int new_sex)
	{
    this.cell_population = new_population;
    this.genome_data = new_genome_data;
    this.organism = new_organism;
    this.sex = new_sex;
    this.total_number_of_bases_in_genome = GenomeData.getGenomeSize(this.organism, this.sex);
    this.haploid_number = GenomeData.getHaploidNumber(this.organism);
    
    cell_population.stream().forEach((this_cell) -> 
    {
      //DO SOMETHING WITH EACH CELL
      getCellLabelDistribution(this_cell);
    });// For each cell in the final population 
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
          double bases_labelled_on_strand = genome[chromosome_count][homologous_pair_count][dna_strand_count];
          System.out.println("Strand bases labelled " + bases_labelled_on_strand);
          total_labelled_bases_in_genome += bases_labelled_on_strand*(double)chromosome_size; 
          
          chromo_labelled_bases =(bases_labelled_on_strand*(double)chromosome_size);          
        }// For each DNA strand
        chromosome_labelled_bases[chromosome_count][homologous_pair_count] = chromo_labelled_bases;
      }// For each chromosome in a homologous pair
    }// For each homologous pair
    fraction_of_genome_labelled = (total_labelled_bases_in_genome/(double)total_number_of_bases_in_genome);
    new_cell.setFractionGenomeLabelled(fraction_of_genome_labelled);
    
    for(double[] homo_pair:chromosome_labelled_bases)
    {
      for(double chromo:homo_pair)
      {
        System.out.println("Chromosome frction labelled: " + chromo);
      }
    }
    
    System.out.println("Cell: " + new_cell.getId() + ". Fraction labelled: " + fraction_of_genome_labelled);
    System.out.println("Total labelled bases " + total_labelled_bases_in_genome + "   Total bases in genome: " + total_number_of_bases_in_genome);
  }// getCellLabelDistribution
}// DataAnalysis
