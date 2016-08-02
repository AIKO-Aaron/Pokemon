package ch.aiko.pokemon.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.player.OtherPlayer;

public class PokemonClient {

	public static final int PORT = 4732;

	public ArrayList<OtherPlayer> players = new ArrayList<OtherPlayer>();
	public Thread receiver, sender;
	public boolean running, sending;
	public String address;
	public String uuid;
	public String pathToLevel;
	public int x, y, dir;
	public Socket socket;
	public boolean synchrone = false, lvl = false;
	private String textToSend = "";

	private boolean receivingPlayers = false;

	public PokemonClient(String connectTo, String uuid) {
		address = connectTo;
		receiver = new Thread(() -> receive());
		sender = new Thread(() -> send());
		try {
			socket = new Socket(InetAddress.getByName(connectTo), PORT);
		} catch (Throwable e) {
			e.printStackTrace(Pokemon.out);
		}
		receiver.start();
		sender.start();
		if (uuid != null && !uuid.equalsIgnoreCase(connectTo) && uuid.contains("-")) sendText("/c/" + uuid);
		else sendText("/ruuid/");
	}

	public void sendText(String text) {
		textToSend = text;
	}

	public void send() {
		sending = true;
		while (sending) {
			try {
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				while (!socket.isConnected()) {
					Thread.sleep(10);
					System.out.println("Waiting for socket...");
				}
				writer.write(textToSend + "\n");
				writer.flush();
				textToSend = "";
			} catch (Throwable e) {
				e.printStackTrace(Pokemon.out);
			}
		}
	}

	private void receive() {
		running = true;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (running) {
				String received = reader.readLine();
				if (received == null) continue;
				perform(received.trim(), socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void perform(String received, Socket s) {
		if (received.equals("/pend/")) receivingPlayers = false;
		else if (received.startsWith("/guuid/")) {
			uuid = received.substring(7).trim();
			Pokemon.serverUUIDs.addValue(address, uuid);
			Pokemon.serverUUIDs.saveFile();
		} else if (received.startsWith("/lvl/")) {
			pathToLevel = received.substring(5);
			lvl = true;
		} else if (received.startsWith("/pos/")) {
			x = Integer.parseInt(received.substring(5).split("/")[0]);
			y = Integer.parseInt(received.substring(5).split("/")[1]);
			dir = Integer.parseInt(received.substring(5).split("/")[2]);
			synchrone = true;
		} else if (received.startsWith("/padd/") || receivingPlayers) {
			try {
				OtherPlayer otpl = new OtherPlayer(received.startsWith("/padd/") ? received.substring(6) : received);
				System.out.println("Adding Player: " + otpl.uuid);
				if (Pokemon.pokemon != null && Pokemon.pokemon.handler != null && Pokemon.pokemon.handler.level != null) Pokemon.pokemon.handler.level.addEntity(otpl);
				players.add(otpl);
			} catch (Throwable t) {
				t.printStackTrace();
			}

		} else if (received.startsWith("/prem/")) {
			String uuid = received.substring(6);
			for (int i = 0; i < players.size(); i++) {
				OtherPlayer p = players.get(i);
				if (p.uuid.equals(uuid)) {
					players.remove(i);
					Pokemon.pokemon.handler.level.removeEntity(p);
				}
			}
		} else if (received.equals("/pset/")) {
			players.clear();
			receivingPlayers = true;
		} else if (received.startsWith("/pupd/")) {
			OtherPlayer otpl = new OtherPlayer(received.substring(6));
			int i = players.size();
			for (int j = 0; j < players.size(); j++) {
				if (players.get(j) != null && players.get(j).uuid != null && players.get(j).uuid.equals(otpl.uuid)) {
					i = j;
					break;
				}
			}
			if(i >= players.size()) return;
			OtherPlayer op = players.get(i);
			op.setX(otpl.getX()).setY(otpl.getY()).setUUID(otpl.uuid).setDirection(otpl.getDirection());
		}
	}

	/**
	 * public void close() { try { socket.close(); } catch (IOException e) { e.printStackTrace(Pokemon.out); } }
	 */

	/***/
	public String getLevel() {
		lvl = false;
		sendText("/rlvl/");
		while (!lvl)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		return pathToLevel;
	}

	public void waitFor() {
		while (!synchrone || !lvl) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
