package ch.aiko.pokemon.entity.trainer;

import java.util.ArrayList;

public class TrainerUtil {

	public static ArrayList<Trainer> allTrainers = new ArrayList<Trainer>();

	public static void registerTrainer(Trainer t, int id) {
		while (allTrainers.size() <= id)
			allTrainers.add(null);
		allTrainers.set(id, t);
	}

	public static int registerTrainer(Trainer t) {
		if (!allTrainers.contains(t)) allTrainers.add(t);
		return allTrainers.size();
	}

	public static int getIDOf(Trainer t) {
		return allTrainers.indexOf(t);
	}

}
