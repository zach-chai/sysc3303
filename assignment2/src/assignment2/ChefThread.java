package assignment2;

import java.util.Random;

public class ChefThread implements Runnable {
	
	Table table;
	Random rand;
	Ingredient ingredient;
	
	public ChefThread(Ingredient i) {
		ingredient = i;
		table = Table.getInstance();
		rand = new Random();
	}

	@Override
	public void run() {
		while(table.getSandwiches() < Table.MAX_SANDWICHES) {
			makeEatSandwich();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void makeEatSandwich() {
		table.useIngredients(ingredient);
	}

}
