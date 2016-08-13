package ch.aiko.pokemon.server;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import ch.aiko.as.ASDataBase;
import ch.aiko.as.ASObject;
import ch.aiko.engine.command.BasicCommand;
import ch.aiko.engine.graphics.Screen;
import ch.aiko.util.FileUtil;

public class UpdateHandler {

	public ArrayList<ServerPlayer> p = new ArrayList<ServerPlayer>();

	public void setServerPlayerLevel(String uuid, String level) {
		for (ServerPlayer player : p)
			if (player.uuid.equals(uuid)) player.currentLevel = level;
	}

	public void setServerPlayerPos(String uuid, int x, int y, int dir) {
		for (ServerPlayer player : p)
			if (player.uuid.equals(uuid)) {
				player.x = x;
				player.y = y;
				player.dir = dir;
			}
	}

	public ServerPlayer getServerPlayer(String uuid) {
		for (ServerPlayer player : p)
			if (player.uuid.equals(uuid)) return player;
		return null;
	}

	public void addServerPlayer(ServerPlayer player) {
		p.add(player);
	}

	private Screen screen = new Screen(960, 540) {
		private static final long serialVersionUID = 1L;

		public Screen startThreads() {
			ScheduledThreadPoolExecutor exe = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(3);
			update = exe.scheduleAtFixedRate(() -> preUpdate(), 0, 1000000000 / 60, TimeUnit.NANOSECONDS);
			render = exe.scheduleAtFixedRate(() -> saveServerPlayers(), 0, 10, TimeUnit.SECONDS);
			disp = exe.scheduleAtFixedRate(() -> {
				lastUPS = ups;
				ups = 0;
			} , 0, 1, TimeUnit.SECONDS);
			return this;
		}

		public void stopThreads() {
			saveServerPlayers();
			update.cancel(false);
			disp.cancel(true);
			PokemonServer.out.println("Done");
		};
	};

	private void saveServerPlayers() {
		ASDataBase base = new ASDataBase("ServerPlayerData");
		for (ServerPlayer player : p) {
			player.reload();
			base.addObject(player);
		}
		base.saveToFile(FileUtil.getRunningJar().getParent() + "/players.bin");
	}

	private void registerCommands() {
		new BasicCommand("ups", "ups", 0, (String[] args, Screen sender) -> {
			PokemonServer.out.println("" + sender.lastUPS);
			return true;
		});

		new BasicCommand("save", "save", 1, (String[] args, Screen sender) -> {
			saveServerPlayers();
			return true;
		});
	}

	private void loadServerPlayers() {
		// ASDataBase base = new ASDataBase("ServerData");
		ASDataBase base = ASDataBase.createFromFile(FileUtil.getRunningJar().getParent() + "/players.bin");
		if (base == null) base = new ASDataBase("ServerPlayerData");

		for (int i = 0; i < base.objectCount; i++) {
			ASObject obj = base.objects.get(i);
			if (obj != null && obj.getName().equals("ServerPlayer")) {
				p.add(new ServerPlayer(obj));
				PokemonServer.out.println("Found ServerPlayer:" + p.get(p.size() - 1).uuid);
			}
		}
	}

	public UpdateHandler() {
		registerCommands();
		loadServerPlayers();
		screen.ps = PokemonServer.out;
		screen.startThreads();
		screen.startCommandLineReader();
	}

}
