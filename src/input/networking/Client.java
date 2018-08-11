package input.networking;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import graphics.DrawLogEntry;
import input.RawInputType;

public class Client {

	private String hostname;
	private RawInputType rawInput;
	private volatile boolean hasCrashed;
	private volatile ArrayList<DrawLogEntry> lastDrawLog;
	
	public Client(String hostname, RawInputType rawInput) {
		this.hostname=hostname;
		this.rawInput=rawInput;
		hasCrashed=false;
		new Thread(null, null, "Client thread", 1<<18) {
			public void run() {
				runClient();
			}
		}.start();
	}
	
	public void runClient() {
		Socket socket=null;
		try {
			System.out.println("Starting client...");
			System.out.println("Starting connection to "+hostname+" on port "+Server.portNumber+"...");
			socket=new Socket(hostname, Server.portNumber);
			PrintWriter toServer=new PrintWriter(socket.getOutputStream());
			BufferedReader fromServer=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {
				//TODO: send input to server
				String inputToSend=getInput();
				toServer.println(inputToSend);
				toServer.flush();
				
				//TODO: read image from server
				String compressed=fromServer.readLine();
				lastDrawLog=decompressDrawLog(compressed);
				
			}
		}
		catch(Exception e) {
			e.printStackTrace();
			hasCrashed=true;
		}
		finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean hasCrashed() {
		return hasCrashed;
	}
	
	public ArrayList<DrawLogEntry> getRecievedDrawLog() {
		return lastDrawLog;
	}
	
	private String getInput() {
		StringBuilder sb=new StringBuilder("");
		//jump, up, down, left, right, attack1, attack2, attackRecover, shield, grab
		sb.append(rawInput.jumpMovementDown()?1:0);
		sb.append(rawInput.upMovementDown()?1:0);
		sb.append(rawInput.downMovementDown()?1:0);
		sb.append(rawInput.leftMovementDown()?1:0);
		sb.append(rawInput.rightMovementDown()?1:0);
		sb.append(rawInput.attack1Down()?1:0);
		sb.append(rawInput.attack2Down()?1:0);
		sb.append(rawInput.attackRecoverDown()?1:0);
		sb.append(rawInput.shieldDown()?1:0);
		sb.append(rawInput.grabDown()?1:0);
		return sb.toString();
	}

	private ArrayList<DrawLogEntry> decompressDrawLog(String compressed) {
		String[] parts=compressed.split("#");
		ArrayList<DrawLogEntry> toReturn=new ArrayList<>();
		for (String s:parts) {
			if (s.isEmpty()) continue;
			toReturn.add(new DrawLogEntry(s));
		}
		return toReturn;
	}
	
}
