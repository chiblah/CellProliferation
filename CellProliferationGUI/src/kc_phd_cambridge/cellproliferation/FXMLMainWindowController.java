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
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;


/**
 * Main class responsible for drawing and controlling the user interface,
 * receiving input data from the user and creating threads on which separate
 * instances of Simulations are run.
 * 
 * Takes validated user input(s) and stores them in an ArrayList as a set  
 * containing unique SimulationData objects. For each SimulatedData object in 
 * the ArrayList, a new thread is created and a Simulation evaluated.
 * 
 * @see Simulation
 * @see SimulationData
 * @author Kyata Chibalabala
 */
public class FXMLMainWindowController  
{ 
  // FXML variables for GUI objects
  @FXML
  private Button addSimulationButton, beginSimulationButton;
  @FXML
  private RadioButton sexRadioButtonF, sexRadioButtonM; // Sex radio buttons
  @FXML
  private RadioButton organismRadioButtonHum, organismRadioButtonMou, organismRadioButtonTest; // Organism radio buttons
  @FXML
  private TextField initPopSizeField, simDurationField, timeIntervalField;
  @FXML
  private ToggleGroup organismToggleGroup, sexToggleGroup; // Toggle groups to make mutually exclusive selevtions for organism and sex
  @FXML
  private TextArea outputTextArea;
  private final int FEMALE = 1, MALE = 2; // "enum" for sex
  private boolean organism_input_valid = false, sex_input_valid = false, init_pop_input_valid = false, sim_dur_input_valid = false, interval_input_valid = false;
         
  
  // An array list to store the input data for each unique simulation
  List<SimulationData> input_data_for_simulations = new ArrayList<>();
  
  /** 
   * Verifies input data when the user decides to create a SimulationData object
   * by activating the ****????? button.
   * 
   * Each unique input data field is verified and a SimulationData object is
   * created and stored if and only if all input fields contain valid data.
   * 
   * @param temp_organism the organism provided by the user. Empty string if none received (invalid input).
   * @param temp_sex the sex provided by the user. 0 if none received, 1 = Female, 2 = Male (invalid input).
   * @param temp_initial_population_size the initial population size provided by the user. 0 if none received (invalid input).
   * @param temp_simulation_duration the duration of the simulation as provided by the user. 0 if none received (invalid input).
   * @param temp_time_interval the time interval provided by the user. 0 if none received (invalid input).
   */
  @FXML
  private void handleAddSimulationButtonEvent(ActionEvent event) 
  {
    // Temp variables to store input data as validation is completed. When all
    // field are validated, the temp values are then used to create a SimData
    // object which is then added to the list of input datasets.
    String temp_organism = "";
    int temp_sex = 0, temp_initial_population_size = 0, temp_simulation_duration = 0, temp_time_interval = 0;


    // TODO
    // 1. Perform validation on each data entry field

    // Extract organism information froim user input
    if(organismRadioButtonHum.isSelected())
    {
      temp_organism = "Homo sapiens";
      organism_input_valid = true;
    }
    else if(organismRadioButtonMou.isSelected())
    {
      temp_organism = "Mus musculus";
      organism_input_valid = true;
    }
    else if(organismRadioButtonTest.isSelected())
    {
      temp_organism = "Test test";
      organism_input_valid = true;
    }
    else
    {
      organism_input_valid = false;
      // TODO 
      // Warn the user that they have not selected an organism! (Or set a default)
      System.out.println("Unable to proceed, no organism selected!!!");
    }

    // Extract sex information from user input
    if(sexRadioButtonF.isSelected())
    {
      temp_sex = FEMALE;
      sex_input_valid = true;
    }
    else if(sexRadioButtonM.isSelected())
    {
      temp_sex = MALE;
      sex_input_valid = true;
    }
    else
    {
      sex_input_valid = false;
      // TODO => GUI error message
      // Warn the user that they have not selected a sex! (Or set a default)
      System.out.println("Unable to proceed, no sex selected!!!");
    }

    // Extract population size from user input
    if(initPopSizeField.getText() != null && ! initPopSizeField.getText().trim().isEmpty()) 
    {
      temp_initial_population_size = Integer.parseInt(initPopSizeField.getText());
      init_pop_input_valid = true;
    }else
    {
      init_pop_input_valid = false;
      // TODO => GUI error message
      // Warn the user that they have not entered a population size
      System.out.println("Unable to proceed, no population size provided");
    }

    // Extract simulation duration from user input
    if(simDurationField.getText() != null && ! simDurationField.getText().trim().isEmpty()) 
    {
      temp_simulation_duration = Integer.parseInt(simDurationField.getText());
      sim_dur_input_valid = true;
    }else
    {
      sim_dur_input_valid = false;
      // TODO => GUI error message
      // Warn the user that they have not entered a value for simulation duration
      System.out.println("Unable to proceed, no simulation duration provided");
    }

    // Extract time interval from user input
    if(timeIntervalField.getText() != null && ! timeIntervalField.getText().trim().isEmpty()) 
    {
      temp_time_interval = Integer.parseInt(timeIntervalField.getText());
      interval_input_valid = true;
    }else
    {
      interval_input_valid = false;
      // TODO => GUI error message
      // Warn the user that they have not entered a value for the time interval
      System.out.println("Unable to proceed, no time interval provided");
    }

    // Create a SimulationData object from the temp values and add it to the 
    // list of input datasets. However, only do this if all inputes are valid
    if(organism_input_valid && sex_input_valid && init_pop_input_valid && sim_dur_input_valid && interval_input_valid)
    {
      input_data_for_simulations.add(new SimulationData(temp_organism, temp_sex, temp_initial_population_size, temp_simulation_duration, temp_time_interval));
      System.out.println("Success! Dataset has been added to the list of simulations to run");
    }
    else
      System.out.println("Sorry, this dataset was not added as there exists =< 1 incorrect inputs");
// 3. Based on which option has been selected, wipe input field
  }

  
  /** 
   * Creates threads on which Simulations are performed from user input data.
   * 
   * Goes through the list of SimulationData objects and for each object, passes
   * the SimulationData object to a Simulation object's constructor which then
   * initiates and performs the simulation.
   * 
   * @param input_data_for_simulations the list of SimulationData objects stored in an ArrayList
   */
  @FXML
  private void handleBeginSimulationEvent(ActionEvent event) 
  {
    // TODO
    // 1. Present a confirmation prompt for the user
    // 2. For each unique row in the input data array, create a new thread
    //    to run a simulation object.
    // Go through each element in the input_data_for_simulations array and create
    // a
    for (SimulationData current_simulation_input_dataset : input_data_for_simulations) 
    {  
      Thread thread = new Thread(new Simulation(current_simulation_input_dataset));
      thread.start();
      //System.out.println(current_simulation_input_dataset.toString());
    }

  }
    
}
