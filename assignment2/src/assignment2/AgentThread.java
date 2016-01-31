package assignment2;

import java.util.Random;

public class AgentThread implements Runnable {
	
	Table table;
	Random rand;
	
	public AgentThread() {
		table = Table.getInstance();
		rand = new Random();
	}
	
	@Override
	public void run() {
		while(table.getSandwiches() < Table.MAX_SANDWICHES) {
			supplyRandomIngredients();
			System.out.println("Agent: Supplied Ingredients");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void supplyRandomIngredients() {
		int first = rand.nextInt(3);
		int second = (rand.nextInt(2) + first + 1) % 3;

		table.placeIngredient(Ingredient.values()[first]);
		table.placeIngredient(Ingredient.values()[second]);
	}

}
