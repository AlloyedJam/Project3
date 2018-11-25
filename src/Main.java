import java.util.ArrayList;
import java.util.Random;

public class Main {
	// don't restric these variables
	static final int RES_X = 1024;
	static final int RES_Y = 768;
	static Player player;
	static Platform test;
	static ArrayList<Platform> platforms = new ArrayList();
	static ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	static Random rand = new Random();
	static Background background;
	static int difficulty;
	static Enemy eTest;

	static final int MINUMUM_CHANCE = 85;

	// All of the initialization goes here
	public static void setup() {
		EZ.initialize(RES_X, RES_Y);
		// Background background = new Background("biggrid.jpg", RES_X, RES_Y);
		player = new Player("jumping_left.png", "jumping_right.png", RES_X / 2, RES_Y / 2 - 300);

		// test = new Platform("platform.png", player,RES_X/2,RES_Y/2 + 200);

		for (int i = 100; i < 600; i = i + 100) {
			Platform platform = new Platform("platform2.png", player, rand.nextInt(RES_Y - 30) + 30, i);
			platforms.add(platform);
		}

//		eTest = new Enemy("enemyPH.png",platforms,true);

		// variables for lowest platform calculation
		int lowestY = 0;
		int lowestX = RES_X;

		// set the lowest y coordinate of the platform
		for (Platform i : platforms) {
			if (i.getPosY() > lowestY) {
				lowestY = i.getPosY();
				lowestX = i.getPosX();
			}
		}

		// spawn player on top of the lowest platform platform
		player.setPosition(lowestX, lowestY - (player.getLegsLength() + 20));

		background = new Background("biggrid.jpg", RES_X, RES_Y);

		// addPlatform(10, 15);
		// addPlatform(20, 25);
		// addPlatform(50, 75);

		// difficulty for how far the platform can be

		difficulty = 40;
		addPlatform(difficulty);

	}

	// all the update goes here
	public static void update() {
		player.update();
		for (Platform i : platforms) {
			i.update();
		}

		//testOnScreen
		
//		eTest.update(player);
		
		/* for (int i = 0; i < platforms.size(); i++)
		{
			boolean check = testOnScreenBool(platforms.get(i));
			System.out.print( index + "is " + i.getOnScreenStatus() + " ");
		} */
		
		//test.update();
		background.scroll();
		for (Platform i : platforms)
			i.scroll(1);
		
		//System.out.println(eTest.animFrame);
		
		for (int i = 0; i < platforms.size(); i++)
		{
			boolean check = testOnScreenBool(platforms.get(i));
			if (check == false)
			{
				removePlatform(i);
				addPlatform(difficulty);

			}
		}

		if (enemies.size() > 0) {
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).update(player);
				if (enemies.get(i).getBelowScreenStatus()) {
					enemies.get(i).remove();
					enemies.remove(i);
				}
			}
		}
		System.out.println(enemies.size());
	}
	//for testing how many platform are on screen
	public static boolean testOnScreenBool(Platform p) {
		/* int index = 0;
		for (Platform i : platforms) {
			System.out.print( index + "is " + i.getOnScreen() + " ");
			System.out.println("");
			index++;
		}
		
		index = 0; */

		if (p.getPosY() > RES_Y)
			return false;
		else
			return true;
	}

	public static void testOnScreen() {
		int index = 0;
		for (Platform i : platforms) {
			System.out.print( index + "is " + i.getOnScreenStatus() + " ");
			System.out.println("");
			index++;
		}
	}
	
	public static void addPlatform(int difficulty)
	/*
	 * int lowerBound: sets the lowest amount from the top of the screen a platform
	 * can appear int upperBound: sets the highest amount from the top of the screen
	 * a platform can appear
	 */
	{
		// int offset = upperBound - lowerBound; // distance between the two
		int y = rand.nextInt(difficulty); // will generate a number between zero and the maximum offset
		int x = rand.nextInt(RES_X); // generate a random new platform
		Platform platform = new Platform("platform2.png", player, x, 0 - y);
		platforms.add(platform);
		System.out.println(platform.getPosX());
		System.out.println(platform.getPosY());
		spawn(platform);
	}

	public static void removePlatform(int i)
	/*
	 * int i: the index in the platforms array the platform to be closed is located
	 * at
	 */
	{
		platforms.get(i).close();
		platforms.remove(i);
	}

	public static void spawn(Platform platform) {
		if (rand.nextInt(99) + 1 > MINUMUM_CHANCE) {
			Enemy temp = new Enemy("enemyPH.png", RES_X + 30, RES_Y + 30, platforms, true);
			temp.spawnOnPlatform(platform);
			enemies.add(temp);
			System.out.println("Spawned");
		}
	}

	// Main part of the program
	public static void main(String[] args) {
		setup();

		while (true) {
			update();

			// Do not change the refresh
			EZ.refreshScreen();
		}

	}
}
