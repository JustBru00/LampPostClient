package com.gmail.justbru00.lamppostclient;

import com.pi4j.io.gpio.RaspiPin;

/**
 * @author Justin Brubaker
 *
 */
public class LampPostClientMain {
	
	private static SimplePin lampRelay = new SimplePin(RaspiPin.GPIO_07, "Lamp Post Relay" , true);

	public static void main(String[] args) {
		System.out.println("Starting LampPostClient " + Reference.VERSION);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
	        public void run() {
	            try {	            	
	                Thread.sleep(200);
	                System.out.println("\nReceived shutdown request from system. (CTRL-C)");
	                
	                Reference.RUNNING = false;		                
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });

		System.out.println("Connecting to server...");
		Reference.lampName = args[1];
		NetworkManager.connectToServer(args[0]);				

		while (Reference.RUNNING) {		
				if (Reference.LAMPSTATE) {
					// LAMPON
					lampRelay.setState(SimplePinState.ON);
				} else {
					// LAMPOFF
					lampRelay.setState(SimplePinState.OFF);
				}
			
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
