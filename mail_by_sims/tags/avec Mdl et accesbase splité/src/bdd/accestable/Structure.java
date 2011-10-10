package bdd.accestable;

public interface Structure {

	String getNomChamp();

	/**
	 * @return the typeChamp
	 */
	Class<?> getTypeChamp();

	/**
	 * @return the tailleChamp
	 */
	Integer getTailleChamp();

}
