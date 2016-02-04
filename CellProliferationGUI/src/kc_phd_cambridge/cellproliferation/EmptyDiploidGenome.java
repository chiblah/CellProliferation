/*
 * Copyright 2016 Kyata.
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
 * Representation of a diploid genome.
 * @author Kyata
 */
public class EmptyDiploidGenome 
{
  double[][][] diploid_genome;// The three dimensional array to store a genome
  /**
   * Constructor.
   * 
   * @param haploid_number the integer value of target organism's haploid number
   */
  public EmptyDiploidGenome(int haploid_number) 
  {
    this.diploid_genome = new double[haploid_number][2][2];
  }
  
  public double[][][] getGenome()
  {
    return this.diploid_genome;
    
  }
}
