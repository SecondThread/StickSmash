package game.scenes;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import game.Game;
import graphics.Camera;
import graphics.DrawLogEntry;
import input.Input;
import input.Keyboard;
import input.RawInputType;
import input.networking.Client;

public class MultiplayerScene extends Scene {

	private Client client;
	
	public void init() {
		String response=JOptionPane.showInputDialog("Host IP: ");
		String connectTo=response;
		RawInputType myInput=Keyboard.getInstance(true);
		client=new Client(connectTo, myInput);
		Game.force120=false;
	}

	public Scene update() {
		if (client.hasCrashed())
			return new TitleScene(new Input(Keyboard.getInstance(true)));
		return this;
	}

	public BufferedImage render() {
		Camera cam=Camera.getInstance();
		cam.preRender();
		
		ArrayList<DrawLogEntry> drawLog=client.getRecievedDrawLog();

		if (drawLog!=null) {
			for (DrawLogEntry entry:drawLog)
				cam.draw(entry);
		}
		
		return cam.postRender();
	}

}
