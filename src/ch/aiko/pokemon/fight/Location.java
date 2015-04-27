package ch.aiko.pokemon.fight;

public enum Location {

	GRASS("grass_day", "grass_afternoon", "grass_night", "grass_day", "grass_afternoon", "grass_night"),
	FOREST("grass_day", "grass_afternoon", "grass_night", "forest_day", "forest_afternoon", "forest_night"),
	MOUNTAIN("feild_day", "feild_afternoon", "feild_night", "mountain_day", "mountain_afternoon", "mountain_night"),
	OCEAN("water_day", "water_afternoon", "water_night", "water_day", "water_afternoon", "water_night"),
	CAVES("cave", "cave", "cave", "cave_day", "cave_day", "cave_night"),
	SAND("sand_day", "sand_afternoon", "sand_night", "mountain_day", "mountain_afternoon", "mountain_night"),
	SNOW("snow_day", "snow_afternoon", "snow_night", "snow_day", "snow_afternoon", "snow_night"),
	INDOOR("indoor", "indoor", "indoor", "indoor", "indoor", "indoor"),
	FEILD("feild_day", "feild_afternoon", "feild_night", "grass_day", "grass_afternoon", "grass_night");

	private String ground_day, ground_afternoon, ground_night, background_day, background_afternoon, background_night;

	private Location(String ground_day, String ground_afternoon, String ground_night, String background_day, String background_afternoon, String background_night) {
		this.ground_day = ground_day;
		this.ground_afternoon = ground_afternoon;
		this.ground_night = ground_night;

		this.background_day = background_day;
		this.background_afternoon = background_afternoon;
		this.background_night = background_night;
	}

	public String getGroundPath(Time t) {
		return "/ch/aiko/pokemon/textures/fight_ground/" + getString(0, t) + ".png";
	}
	
	public String getBackGroundPath(Time t) {
		return "/ch/aiko/pokemon/textures/fight_background/" + getString(1, t) + ".png";
	}

	/**
	 * 
	 * 
	 * @param type
	 *            The Type of the value (0 = ground, 1 = background)
	 * @param t
	 *            The Time of the texture-value
	 * @return The name of the file without png or a path
	 */
	private String getString(int type, Time t) {
		if (type == 0) {
			switch (t) {
				case DAY:
					return ground_day;
				case AFTERNOON:
					return ground_afternoon;
				case NIGHT:
					return ground_night;
				default:
					return ground_day;
			}
		} else if (type == 1) {
			switch (t) {
				case DAY:
					return background_day;
				case AFTERNOON:
					return background_afternoon;
				case NIGHT:
					return background_night;
				default:
					return background_day;
			}
		} else {
			System.err.println("What are you trying to get ? Read the Javadoc! Only 0 & 1 allowed");
			return "";
		}
	}

}
