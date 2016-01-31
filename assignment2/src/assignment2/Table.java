package assignment2;

import java.util.ArrayList;

public class Table {

	static final int MAX_SANDWICHES = 20;
	private static Table instance;
	private ArrayList<Ingredient> ingredients;
	static final int MAX_SIZE = 2;
	private int sandwiches = 0;

	private Table() {
		this.ingredients = new ArrayList<>(MAX_SIZE);
	}

	public static Table getInstance() {
		if (instance == null) {
			instance = new Table();
		}
		return instance;
	}

	public synchronized void placeIngredient(Ingredient newIngredient) {
		while (ingredients.size() >= MAX_SIZE && !finished()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!finished()) {
			ingredients.add(newIngredient);
		}

		notifyAll();
	}

	public synchronized void useIngredients(Ingredient lastIngredient) {
		while((ingredients.size() < MAX_SIZE || !canUseIngredients(lastIngredient)) && !finished()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (!finished()) {
			ingredients.clear();
			System.out.println("Chef: used " + lastIngredient + " to make and eat sandwich");
			incrementSandwiches();
		}
		notifyAll();
	}

	private boolean canUseIngredients(Ingredient lastIngredient) {
		return !ingredients.contains(lastIngredient);
	}

	public ArrayList<Ingredient> getIngredients() {
		return ingredients;
	}

	public synchronized void incrementSandwiches() {
		++sandwiches;
	}

	public boolean finished() {
		return getSandwiches() >= MAX_SANDWICHES;
	}

	public int getSandwiches() {
		return sandwiches;
	}

}
