package ch.aiko.pokemon.basic;

import ch.aiko.modloader.GameEvent;
import ch.aiko.pokemon.entity.player.PlayerMenu;

public class PokemonEvents {
	
	public static final String PLAYER_MENU_OPEN_EVENT = "playeropenmenu";
	//TODO more events (for mods)

	public static class PlayerOpenMenuEvent extends GameEvent {
		public String eventName() {
			return PLAYER_MENU_OPEN_EVENT;
		}

		public PlayerMenu menu;

		public PlayerOpenMenuEvent(PlayerMenu m) {
			menu = m;
		}
	}

}
