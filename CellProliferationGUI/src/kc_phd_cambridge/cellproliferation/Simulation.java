/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kc_phd_cambridge.cellproliferation;

/**
 *
 * @author Kyata Chibalabala
 */
public class Simulation implements Runnable
{
  private SimulationData input_parameters;
  
  // Constructor receives a SimulationData object
  public Simulation(SimulationData new_inputs) 
  {
    this.input_parameters = new_inputs;
  }
    @Override
    public void run()
    {
       System.out.println("MyRunnable running =>" + input_parameters.toString() );
    }
}
