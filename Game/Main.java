package Game;

/**
 * Rampage created by Sawyer Porter for Mr.Yee's class of 2025
 * */

public class Main {			
	public static void main(String args[]) throws InterruptedException {
		App app;
		do {
			app = new App("Rampage", 1400, 850, 60);
			
			int ec = app.run();
			if(ec != 0) {
				System.out.println("App ran unsuccessfully: " + ec);
			}
		} while(app.shouldRecreate());
	}
}