package input.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import graphics.Camera;
import graphics.DrawLogEntry;

public class ServerThread {
	
	private Socket socket;
	private Server mainServer;
	private RawNetworkInput rawNetworkInput;
	
	public ServerThread(Socket socket, Server mainServer) {
		System.out.println("Starting new server thread because someone connected...");
		this.socket=socket;
		this.mainServer=mainServer;
		rawNetworkInput=new RawNetworkInput();
		new Thread(null, null, "Server thread", 1<<18) {
			public void run() {
				runClient();
			}
		}.start();
	}
	
	public void runClient() {
		try {
			PrintWriter toClient=new PrintWriter(socket.getOutputStream());
			BufferedReader fromClient=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (!mainServer.shouldCloseFlag) {
				//TODO: read input from client
				String input=fromClient.readLine();
				rawNetworkInput.setValues(input);
				
				//TODO: send image to client
				String rawImage=compressDrawLog(Camera.getInstance().getLastDrawLog());
				toClient.println(rawImage);
				toClient.flush();
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			mainServer.onPlayerLeft();
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println("Could not close socket...");
				e.printStackTrace();
			}
		}
	}
	
	public RawNetworkInput getRawInput() {
		return rawNetworkInput;
	}
	
	public String compressDrawLog(ArrayList<DrawLogEntry> drawLog) {
		StringBuilder sb=new StringBuilder();
		for (DrawLogEntry e:drawLog) {
			sb.append(e.toString()+'#');
		}
		return sb.toString();
	}
	
}
