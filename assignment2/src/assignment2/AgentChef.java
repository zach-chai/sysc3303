package assignment2;

public class AgentChef {
	
	static final int CHEF_THREADS = 3;

	public static void main(String[] args) {
		(new Thread(new AgentThread())).start();

		
		for(int i = 0; i < CHEF_THREADS; ++i) {
			(new Thread(
					new ChefThread(Ingredient.values()[i]))).start();
		}
	}

}
