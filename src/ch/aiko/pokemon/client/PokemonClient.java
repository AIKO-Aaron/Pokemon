package ch.aiko.pokemon.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import ch.aiko.as.ASDataBase;
import ch.aiko.modloader.LoadedMod;
import ch.aiko.modloader.ModLoader;
import ch.aiko.pokemon.Pokemon;
import ch.aiko.pokemon.entity.player.OtherPlayer;
import ch.aiko.pokemon.pokemons.TeamPokemon;

public class PokemonClient {

	public static final int PORT = 4732;

	public ArrayList<OtherPlayer> players = new ArrayList<OtherPlayer>();
	public Thread receiver, sender;
	public boolean running, sending;
	public String address;
	public String uuid;
	public String pathToLevel;
	public int x, y, dir;
	public TeamPokemon[] team = new TeamPokemon[Pokemon.TeamSize];
	public Socket socket;
	public boolean synchrone = false, lvl = false, pos = false;
	private int waitingForMods = 0;
	private ArrayList<String> modNames = new ArrayList<String>();

	private ArrayList<String> texts = new ArrayList<String>();
	private int dataLength = 0;

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
		sender.start();
		receiver.start();
		if (uuid != null && !uuid.equalsIgnoreCase(connectTo) && uuid.contains("-")) sendText("/c/" + uuid);
		else sendText("/ruuid/");
	}

	public void sendText(String text) {
		texts.add(text);
	}

	public void send() {
		sending = true;
		while (sending) {
			try {
				if (socket == null) continue;
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				while (!socket.isConnected()) {
					Thread.sleep(100);
					System.out.println("Waiting for socket...");
				}
				if (texts.size() > 0) {
					writer.write(texts.get(0) + "\n");
					texts.remove(0);
				} else writer.write("\n");
				writer.flush();
			} catch (Throwable e) {
				if (!(e instanceof SocketException)) e.printStackTrace(Pokemon.out);
			}
		}
	}

	public void sendBytes(byte[] bytes) {
		if (socket == null) return;
		try {
			socket.getOutputStream().write(bytes, 0, bytes.length);
			socket.getOutputStream().write(0xA);
		} catch (Throwable e) {
			if (!(e instanceof SocketException)) e.printStackTrace(Pokemon.out);
		}
	}

	private void receive() {
		running = true;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (running) {
				if (dataLength > 0) {
					sendText("/rec/");
					byte[] buffer = new byte[dataLength];
					new DataInputStream(socket.getInputStream()).readFully(buffer, 0, buffer.length);
					ASDataBase pb = ASDataBase.createFromBytes(buffer);
					if (pb != null) {
						ServerPlayer p = new ServerPlayer(pb.getObject("Player"));
						team = p.team;
						x = p.x;
						y = p.y;
						dir = p.dir;
						pathToLevel = p.currentLevel;
						dataLength = 0;
						lvl = pos = true;
					}
				}
				String received = reader.readLine();
				if (received == null) continue;
				perform(received.trim(), socket);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void perform(String received, Socket s) {
		/**
		 * if (dataLength > 0) { dataLength -= received.getBytes().length; System.out.println(received + " received, " + dataLength + " bytes left"); data += received; if (dataLength <= 0 || received.equalsIgnoreCase("/EOPD/")) { for (byte b : received.getBytes()) System.out.print(Integer.toHexString(b & 0xFF) + ", "); System.out.println(); System.out.println(dataLength); ASDataBase pb = ASDataBase.createFromBytes(data.getBytes()); if (pb != null) { ServerPlayer p = new ServerPlayer(pb.getObject("Player")); System.out.println(p.currentLevel); lvl = pos = true; } } return; }
		 */
		if (waitingForMods > 0) {
			modNames.add(received);
			waitingForMods--;
			if (waitingForMods == 0) {
				for (LoadedMod lm : ModLoader.loadedMods) {
					String rec = lm.modInfoList.get("name") + "=" + lm.modInfoList.get("version");
					if (!modNames.contains(rec)) {
						Pokemon.out.err("Stopped loading unnecessary mod: " + rec.replace("=", ", version: "));
						ModLoader.loadedMods.remove(lm);
					} else modNames.remove(rec);
				}
				if (modNames.size() == 0) synchrone = true;
				else {
					Pokemon.out.err("Didn't find required mods: ");
					for (String t : modNames)
						Pokemon.out.err("\t" + t.replace("=", ", version: "));
					Pokemon.pokemon.handler.window.quit();
				}
			}
			return; // If mod name is /padd/X/Y/0/0000-0000-0000-0000-0000, we would add a player. Not what we want really
		}

		if (received.equals("/pend/")) receivingPlayers = false;
		else if (received.startsWith("/guuid/")) {
			uuid = received.substring(7).trim();
			Pokemon.serverUUIDs.addValue(address, uuid);
			Pokemon.serverUUIDs.saveFile();
		} /**
			 * else if (received.startsWith("/lvl/")) { pathToLevel = received.substring(5); lvl = true; }
			 */
		else if (received.startsWith("/mods/")) {
			waitingForMods = Integer.parseInt(received.substring(6));
			if (waitingForMods == 0) synchrone = true;
		} else if (received.startsWith("/padd/") || receivingPlayers) {
			OtherPlayer otpl = new OtherPlayer(received.startsWith("/padd/") ? received.substring(6) : received);
			addPlayer(otpl);
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
			if (i >= players.size()) {
				addPlayer(otpl);
				return;
			}
			OtherPlayer op = players.get(i);
			op.setWalking(op.lx != otpl.getX() || op.ly != otpl.getY());
			op.setX(otpl.getX()).setY(otpl.getY()).setUUID(otpl.uuid).setDirection(otpl.getDirection());
		} else if (received.startsWith("/SOPD/")) {
			dataLength = Integer.parseInt(received.substring(6));
		}
	}

	public void addPlayer(OtherPlayer otpl) {
		System.out.println("Adding Player: " + otpl.uuid);
		if (Pokemon.pokemon != null && Pokemon.pokemon.handler != null && Pokemon.pokemon.handler.level != null) otpl.add(Pokemon.pokemon.handler.level);// Pokemon.pokemon.handler.level.addEntity(otpl);
		players.add(otpl);
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

	public void waitFor() {
		while (!synchrone || !lvl || !pos) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void ifNotSet(String string) {
		if(texts.size() <= 0) texts.add(string);
	}
}
