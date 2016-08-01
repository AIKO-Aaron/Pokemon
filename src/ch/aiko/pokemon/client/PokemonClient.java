package ch.aiko.pokemon.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import ch.aiko.pokemon.Pokemon;

public class PokemonClient {

	public static final int PORT = 4732;

	public Thread receiver;
	public boolean running;
	public String address;
	public String uuid;
	public String pathToLevel;
	public int x, y, dir;
	public Socket socket;
	public boolean synchrone = false, lvl;

	public PokemonClient(String connectTo, String uuid) {
		address = connectTo;
		receiver = new Thread(() -> receive());
		try {
			socket = new Socket(InetAddress.getByName(connectTo), PORT);
		} catch (Throwable e) {
			e.printStackTrace(Pokemon.out);
		}
		receiver.start();
		if (uuid != null && !uuid.equalsIgnoreCase(connectTo) && uuid.contains("-")) sendText("/c/" + uuid);
		else sendText("/ruuid/");
	}

	public void sendText(String text) {
		try {
			while (!socket.isConnected());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			writer.write(text + "\n");
			writer.flush();
		} catch (Throwable e) {
			e.printStackTrace(Pokemon.out);
		}
	}

	private void receive() {
		running = true;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (running) {
				String received = reader.readLine();
				if (received == null) continue;
				System.out.println(received);
				perform(received.trim(), socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void perform(String received, Socket s) {
		if (received.startsWith("/guuid/")) {
			uuid = received.substring(7).trim();
			Pokemon.serverUUIDs.addValue(address, uuid);
			Pokemon.serverUUIDs.saveFile();
			System.out.println("Saved uuids....");
		} else if (received.startsWith("/lvl/")) {
			pathToLevel = received.substring(5);
			lvl = true;
		} else if (received.startsWith("/pos/")) {
			x = Integer.parseInt(received.substring(5).split("/")[0]);
			y = Integer.parseInt(received.substring(5).split("/")[1]);
			dir = Integer.parseInt(received.substring(5).split("/")[2]);
			synchrone = true;
		}
	}

	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace(Pokemon.out);
		}
	}

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

	public void synchronize() {
		synchrone = lvl = false;
		sendText("/rpos/");
		sendText("/rlvl/");
		while (!synchrone || !lvl)
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
}
