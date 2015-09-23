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
  private static boolean successfull_genome_import = false;
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
    
    if(importGenomeData(genome_data_file))
    {
      successfull_genome_import = true;
    }else// Import was unsuccessful
    {
      successfull_genome_import = false;
    }
	}// Constructor

  private static boolean importGenomeData(File received_genome_data_file)
  {
    
    return true;
    // TODO: Perform import of ALL data and return true if successful or false if not
  }// importGenomeData
  
  // TODO: Access method for organism and sex specific chromosome sizes

  boolean getImportStatus() 
  {
    return successfull_genome_import;
  }
}// GenomeData
