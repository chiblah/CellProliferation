/*
 * Copyright 2015 Kyata Chibalabala .
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kyata Chibalabala
 */
public class GenomeData 
{
  // Class variables
  private static File genome_data_file;
  private static List<String> genome_data; 
  
  /**
  * Constructs a GenomeData object to store all Genome Data from a selected file
  *
  * @param new_genome_data_file The initial Genome Data file upon construction 
  * of the object
  */
  public GenomeData(File new_genome_data_file)
	{
    genome_data = new ArrayList<>();
		genome_data_file = new_genome_data_file;
    importGenomeData(genome_data_file);
	}// Constructor

  public static void importGenomeData(File received_genome_data_file)
  {
    // Begin by changing the genome data file to the newly provided file.
    // This is redundant if the method is called from the constructor object but
    // is necessary if the user changes the initially selected Genome Data file
    // which necessitates rerunning of the import code
    genome_data_file = received_genome_data_file;
    
    // TODO: Perform import of ALL data
  }// importGenomeData
  
  // TODO: Access method for organism and sex specific chromosome sizes
}// GenomeData
