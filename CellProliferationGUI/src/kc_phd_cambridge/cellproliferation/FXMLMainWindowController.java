/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kc_phd_cambridge.cellproliferation;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;


/**
 * The controller for the main application window. This class is responsible for
 * dealing with the user interface and parsing data provided by the user, 
 * verification of input data, initiation of instances of simulation executives, 
 * dealing with exceptions thrown by other classes and errors resulting improper
 * usage of the code.
 * 
 * @author Kyata Chibalabala
 */
public class FXMLMainWindowController  
{ 
    @FXML
    private Button addSimulationButton;
    @FXML
    private Button beginSimulationButton;
    @FXML
    private RadioButton sexRadioButtonF;
    @FXML
    private RadioButton sexRadioButtonM;
    
    @FXML
    private RadioButton organismRadioButtonHum;
    @FXML
    private RadioButton organismRadioButtonMou;
    @FXML
    private RadioButton organismRadioButtonTest;
    
    final ToggleGroup organismGroup = new ToggleGroup();
    
    @FXML
    private TextField initPopSizeField;
    @FXML
    private TextField simDurationField;
    @FXML
    private TextField timeIntervalField;
    @FXML
    private ToggleGroup organismToggleGroup;
    @FXML
    private ToggleGroup sexToggleGroup;
    
    
    /* A two dimensional array to store simulation arguments. First dimension
     * (columns) stores ORGANISM, SEX, INITIAL_POPULATION_SIZE, 
     * SIMULATION_DURATION, TIME_INTERVAL. The second dimension (rows) 
     * store the input data for each unique simulation
     */
    String[][] simulation_arguments = new String[5][3];
    
    // Evaluate this code when a user decides to add simulation details to the
    // list of simulations
    @FXML
    private void handleAddSimulationButtonEvent(ActionEvent event) 
    {
        // TODO
        // 1. Perform validation on each data entry field
        // 2. Write input data to a row in the input data array
        // 3. Based on which option has been selected, wipe input field
    }
    
    // Evaluate this code when a user decides to run simulations from the data 
    // they have provided
    @FXML
    private void handleBeginSimulationEvent(ActionEvent event) 
    {
        // TODO
        // 1. Present a confirmation message for the user
        // 2. For each unique row in the input data array, create a new thread
        //    to run a simulation object.
    }
    
}
