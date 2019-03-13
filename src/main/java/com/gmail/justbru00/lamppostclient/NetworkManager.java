package com.gmail.justbru00.lamppostclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NetworkManager {

	private static Socket socket;
	private static BufferedReader in;
	private static PrintWriter out;
	private static boolean connected = true;

	/**
	 * Using port 2019 This method will attempt to keep the connection alive forever
	 * 
	 * @param serverIP
	 */
	public static void connectToServer(String serverIP) {
		// Open the socket
		while (Reference.RUNNING) {
			// Try connecting again if we lose connection every 2 seconds
			try {
				socket = new Socket(serverIP, 2019);
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("FAILED TO CONNECT!!!");
			}

			if (!socket.isClosed()) {
				connected = true;
				while (connected) {
					// Attempt to send message
					out.println("WHAT_DO_YOU_WANT_FROM_ME?");

					String response = "";
					try {
						response = in.readLine();
						if (response == null || response.equals("")) {
							System.out.println("RECEIVED NOTHING FROM SERVER");
						}
					} catch (IOException e) {
						System.out.println("AN ERROR OCCUCRED");
						connected = false;
						break;
					}
					
					System.out.println(response);
					
					if (response.equalsIgnoreCase("LAMPON")) {
						Reference.LAMPSTATE = true;
					} else if (response.equalsIgnoreCase("LAMPOFF")) {
						Reference.LAMPSTATE = false;
					}
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			System.out.println("SOCKET WAS CLOSED TRYING TO CONNECT AGAIN IN 2 SECONDS");

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
