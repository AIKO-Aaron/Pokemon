package ch.aiko.pokemon.attacks;

public class Attack {

	public String attackName;
	public int attackDamage, backFire;
	public Type type = Type.NORMAL;
	public AttackType at;
	public int defAP, maxAP;
	public int accuracy = -1;

	public static void init() {
		new Attack("Verzweifler", AttackType.PHYSICAL, 1, 0, -1, 50, 25, Type.NORMAL);
		new Attack("Tackle", AttackType.PHYSICAL, 35, 56, 95, 35, 0, Type.NORMAL);
		new Attack("Flammenwurf", AttackType.SPECIAL, 15, 24, 100, 95, 0, Type.FIRE);
		new Attack("Glut", AttackType.SPECIAL, 25, 40, 100, 40, 0, Type.FIRE);
	}

	public Attack(String name, AttackType at, int defAP, int maxAP, int acc, int dam, int back, Type type) {
		this.at = at;
		this.defAP = defAP;
		this.maxAP = maxAP;
		accuracy = acc;
		attackDamage = dam;
		backFire = back;
		this.type = type;
		String[] np = name.split(" ");
		attackName = "";
		for (String n : np)
			if (n != null) attackName += n.substring(0, 1).toUpperCase() + n.substring(1).toLowerCase() + (!name.endsWith(n) ? " " : "");
		AttackUtil.registerAttack(this);
	}

	public Attack setAccuracy(int acc) {
		accuracy = acc;
		return this;
	}

	public Attack setDamage(int d) {
		this.attackDamage = d;
		return this;
	}

	public Attack setBackFire(int d) {
		backFire = d;
		return this;
	}

	public Attack setType(Type t) {
		type = t;
		return this;
	}

}
