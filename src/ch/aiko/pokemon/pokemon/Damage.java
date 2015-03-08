package ch.aiko.pokemon.pokemon;

import ch.aiko.pokemon.language.Language;

public enum Damage {
	
	NO_DAMAGE(0F), FOURTH_DAMAGE(0.25F), HALF_DAMAGE(0.5F), NORMAL_DAMAGE(1.0F), DOUBLE_DAMAGE(2.0F), QUAD_DAMAGE(4.0F);
	
	private float multiplier;
	
	private Damage(float multiplier) {
		this.multiplier = multiplier;
	}
	
	public float getDamage(int oldDamage) {
		return oldDamage * multiplier;
	}
	
	//PFLANZE --> PLFANZE/GIFT  -->  1/4
	//FEUER --> PLFANZE/GIFT  -->  2
	
	public static Damage getDamage(Type type1, Type type21, Type type22) {
		if(type22 == null || type22 == Type.NULL) type22 = type21;
		
		if(!type1.doesDamageAgainst(type21) || !type1.doesDamageAgainst(type22)) return Damage.NO_DAMAGE;
		
		if(type1.isGoodAgainst(type21) && type1.isBadAgainst(type22)) return Damage.NORMAL_DAMAGE;
		if(type1.isGoodAgainst(type22) && type1.isBadAgainst(type21)) return Damage.NORMAL_DAMAGE;
		
		if(type1.isGoodAgainst(type21) || type1.isGoodAgainst(type22)) return Damage.DOUBLE_DAMAGE;
		if(type1.isGoodAgainst(type21) && type1.isGoodAgainst(type22)) return Damage.QUAD_DAMAGE;
		
		if(type1.isBadAgainst(type21) || type1.isBadAgainst(type22)) return Damage.HALF_DAMAGE;
		if(type1.isBadAgainst(type21) && type1.isBadAgainst(type22)) return Damage.FOURTH_DAMAGE;
				
		return Damage.NORMAL_DAMAGE;
	}
	
	public String getName() {
		return Language.current.getValue(name());
	}

}
