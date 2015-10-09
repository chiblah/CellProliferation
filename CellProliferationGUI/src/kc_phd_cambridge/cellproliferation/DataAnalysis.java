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
    
    cell_population.stream().forEach((this_cell) -> 
    {
      //DO SOMETHING WITH EACH CELL
    });// For each cell in the final population 
    /*TODO
    1. Calculate B, the toal number of bases in the genome
    2. Calculated L, the number of bases in the genome thats are labelled
    3. Calculate F, the fraction of the genome that is labelled
    */
  } 
  
  
}// DataAnalysis
