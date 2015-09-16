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

/**
 * Class for a Cell object with variables for the various properties of each 
 * cell and methods to gain access or change the values of select variables.
 * 
 * @author Kyata Chibalabala
 */
public class Cell 
{
  // Instance variables
	private int cell_id; // A unique identifier for each cell
	private int cell_gen; // Track the generation the cell belongs to
	private double last_div; // The last time this cell completed M-phase. Initially set to the timepoint it was created
	private boolean can_divide; // Indicates the state of the cell, true if cell is at G2 and can divide
  private int[][][] genome;
  private int generation_number;
	
    // Constructor
	public Cell(int new_id, int new_gen, double provided_last_div, boolean division_status, int[][][] provided_genome)
	{
		this.cell_id = new_id;
		this.cell_gen = new_gen;
		this.last_div = provided_last_div;
		this.can_divide = division_status;
    this.genome = provided_genome;
		
	}// Constructor
	
	// Class methods
	
	/**
   * Allows the division status of this cell to be changed to true or false
   * True = cell can divide, False = Cell can not divide
   *
   * @param new_status the new division status of this cell.
   */
	public void changeDivisionStatus(boolean new_status)
	{
    this.can_divide = new_status;
	}// updateDivisionStatus
	
	/**
   * Provides access to the current division status of this cell
   *
   * @return the division status of this cell
   */
	public boolean getDivisionStatus()
	{
    return this.can_divide;
	}// getDivisionStatus
	
  /**
   * Provides access to the unique ID of this cell
   *
   * @return the cellId of this cell object
   */
  public int getId()
  {
    return this.cell_id;
  }// getId
    
  /**
   * Provides access to the generation number of this cell
   *
   * @return the generation of cells that this cell belongs to
   */
	public int getGeneration()
	{
		return this.cell_gen;
	}// getGen
	
	/**
   * Enables changing of the generation number of this cell 
   *
   * @param new_generation the generation of cells that this cell now belongs to
   */
	public void setGeneration(int new_generation)
	{
		this.cell_gen = new_generation;
	}// setGen
    
  /**
   * Provides access to the the diploid genome of this cell
   *
   * @return  the genome of this cell
   */
  public int[][][] getGenome()
  {
      return this.genome;
  }// getGenome
    
  /**
   * Enables a new diploid genome to be set for this cell
   *
   * @param new_genome the new genome object that this cell will now carry
   */
  public void setGenome(int[][][] new_genome)
  {
    this.genome = new_genome;
  }// setGenome
    
	
	/**
   * Returns a string of key information about this cell
   *
   * @return newStr a String representation of key information about this cell object
   */
	public String toString()
	{
    String newStr = "Cell ID = " + this.cell_id + "; Generation = " + this.cell_gen; // + "; Last division timepoint = "+ this.last_div + " Division status = " + can_divide;
		return newStr;
	}// toString
} // Class Cell
