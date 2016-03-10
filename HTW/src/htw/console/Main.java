package htw.console;

import htw.HtwMessageReceiver;
import htw.HuntTheWumpus;
import htw.HuntTheWumpus.Direction;
import htw.factory.HtwFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import static htw.HuntTheWumpus.Direction.*;

public class Main implements HtwMessageReceiver {
	private static HuntTheWumpus game;
	private static final List<String> caverns = new ArrayList<>();
	private static final String[] environments = new String[] { "bright",
			"humid", "dry", "creepy", "ugly", "foggy", "hot", "cold", "drafty",
			"dreadful" };

	private static final String[] shapes = new String[] { "round", "square",
			"oval", "irregular", "long", "craggy", "rough", "tall", "narrow" };

	private static final String[] cavernTypes = new String[] { "cavern",
			"room", "chamber", "catacomb", "crevasse", "cell", "tunnel",
			"passageway", "hall", "expanse" };

	private static final String[] adornments = new String[] {
			"smelling of sulphur", "with engravings on the walls",
			"with a bumpy floor", "", "littered with garbage",
			"spattered with guano", "with piles of Wumpus droppings",
			"with bones scattered around", "with a corpse on the floor",
			"that seems to vibrate", "that feels stuffy",
			"that fills you with dread" };

	public static void main(String[] args) throws IOException {
		game = HtwFactory.makeGame("htw.game.HuntTheWumpusGame", new Main());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		gameSetup();
		boolean playGame = true;

		if ("Standard".equals(game.getGameMode())) {
			createMap();
			game.makeRestCommand().execute();
			while (playGame) {
				playGame = performHunterTurn(br);
			}
		}
		else if("test".equalsIgnoreCase(game.getGameMode())) {
			createTestMap();
			game.makeRestCommand().execute();
			while (playGame) {
				playGame = performHunterTurn(br);
			}
		}
		else if ("Co-Hunt".equals(game.getGameMode())) {
			createMap();
			System.out.println("Enter the Hunter's Name");
			System.out.println(">");
			game.setHunterName(br.readLine());
			System.out.println();
			System.out.println("Enter the Wumpus Name");
			System.out.println(">");
			game.setWumpusName(br.readLine());
			
			game.makeRestCommand().execute();
			while (playGame) {
				System.out.println();
				System.out.println(game.getHunterName().toUpperCase() + ", IT IS YOUR TURN TO HUNT.");
				System.out.println();
				playGame = performHunterTurn(br);
				
				System.out.println();
				System.out.println(game.getWumpusName().toUpperCase() + ", IT IS YOUR TURN TO HUNT.");
				System.out.println();
				playGame = performWumpusTurn(br);
			}
		}
	}

	private static boolean performWumpusTurn(BufferedReader br) throws IOException {
		System.out.println(game.getPlayerCavern());
		HuntTheWumpus.Command c = game.makeRestCommand();
		System.out.println(">");
		String command = br.readLine();
		if (command.equalsIgnoreCase("e"))
			c = game.makeMoveCommand(EAST);
		else if (command.equalsIgnoreCase("w"))
			c = game.makeMoveCommand(WEST);
		else if (command.equalsIgnoreCase("n"))
			c = game.makeMoveCommand(NORTH);
		else if (command.equalsIgnoreCase("s"))
			c = game.makeMoveCommand(SOUTH);
		else if (command.equalsIgnoreCase("r"))
			c = game.makeRestCommand();
		else if (command.equalsIgnoreCase("q"))
			return false;
		c.execute();
		return true;
	}

	private static boolean performHunterTurn(BufferedReader br) throws IOException {
		System.out.println(game.getPlayerCavern());
		System.out.println("Health: " + game.getHitPoints() + " / arrows: "
				+ game.getQuiver() + " / elixir: "
				+ (game.getItems().hasElixir() ? 1 : 0));
		HuntTheWumpus.Command c = game.makeRestCommand();
		System.out.println(">");
		String command = br.readLine();
		if (command.equalsIgnoreCase("e"))
			c = game.makeMoveCommand(EAST);
		else if (command.equalsIgnoreCase("w"))
			c = game.makeMoveCommand(WEST);
		else if (command.equalsIgnoreCase("n"))
			c = game.makeMoveCommand(NORTH);
		else if (command.equalsIgnoreCase("s"))
			c = game.makeMoveCommand(SOUTH);
		else if (command.equalsIgnoreCase("r"))
			c = game.makeRestCommand();
		else if (command.equalsIgnoreCase("sw"))
			c = game.makeShootCommand(WEST);
		else if (command.equalsIgnoreCase("se"))
			c = game.makeShootCommand(EAST);
		else if (command.equalsIgnoreCase("sn"))
			c = game.makeShootCommand(NORTH);
		else if (command.equalsIgnoreCase("ss"))
			c = game.makeShootCommand(SOUTH);
		else if (command.equalsIgnoreCase("h"))
			c = game.makeHealCommand();
		else if (command.equalsIgnoreCase("q"))
			return false;
		c.execute();
		return true;
	}

	private static void gameSetup() throws IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(
				System.in));
		System.out.println("~HUNT THE WUMPUS~");
		System.out.println();
		System.out.println("Please select game mode : Standard or Co-Hunt");
		System.out.println(">");
		game.setGameMode(input.readLine());
		System.out.println("Game mode chosen : " + game.getGameMode());
		System.out.println();
	}

	private static void createMap() {
		int ncaverns = (int) (Math.random() * 30.0 + 10.0);
		while (ncaverns-- > 0)
			caverns.add(makeName());

		for (String cavern : caverns) {
			maybeConnectCavern(cavern, NORTH);
			maybeConnectCavern(cavern, SOUTH);
			maybeConnectCavern(cavern, EAST);
			maybeConnectCavern(cavern, WEST);
		}

		String playerCavern = anyCavern();
		String elixirCavern = anyCavern();
		game.setPlayerCavern(playerCavern);
		game.setWumpusCavern(anyOther(playerCavern));

		game.setElixirCavern(anyOther(playerCavern));

		game.addBatCavern(anyOther(playerCavern, elixirCavern));
		game.addBatCavern(anyOther(playerCavern, elixirCavern));
		game.addBatCavern(anyOther(playerCavern, elixirCavern));

		game.addPitCavern(anyOther(playerCavern));
		game.addPitCavern(anyOther(playerCavern));
		game.addPitCavern(anyOther(playerCavern));

		game.setQuiver(5);
		game.setHitPoints(10);
	}
	
	private static void createTestMap() {
		String fr = "FarRight";
		String fl = "FarLeft";
		String l = "Left";
		String m = "Middle";
		String r = "Right";
		String t = "Top";
		String a = "Above";
		String b = "Below";
		String bot = "Bottom";
		String [] cavernName = {fr,fl,l,m,r,t,a,b,bot};
		for(String i:cavernName) {
			caverns.add(i);
		}
		game.setPlayerCavern(m);
		game.setWumpusCavern(t);
		game.setElixirCavern(fr);
		game.addBatCavern(fl);
		game.addPitCavern(bot);
		
		game.connectCavern(fl, l, Direction.EAST);
		game.connectCavern(l, fl,Direction.WEST);
		game.connectCavern(l,m ,Direction.EAST);
		game.connectCavern(m,l ,Direction.WEST);
		game.connectCavern(m, r,Direction.EAST);
		game.connectCavern(r,m ,Direction.WEST);
		game.connectCavern(r,fr ,Direction.EAST);
		game.connectCavern(fr,r ,Direction.WEST);
		game.connectCavern(t, a,Direction.SOUTH);
		game.connectCavern(a, t,Direction.NORTH);
		game.connectCavern(a,m ,Direction.SOUTH);
		game.connectCavern(m, a,Direction.NORTH);
		game.connectCavern(m,b ,Direction.SOUTH);
		game.connectCavern(b, m,Direction.NORTH);
		game.connectCavern(bot, b, Direction.NORTH);
		game.connectCavern(b,bot ,Direction.SOUTH);
		
		game.setQuiver(5);
		game.setHitPoints(10);
		
		
		
	}

	private static String makeName() {

		return "A " + chooseName(environments) + " " + chooseName(shapes) + " "
				+ chooseName(cavernTypes) + " " + chooseName(adornments);
	}

	private static String chooseName(String[] names) {
		int n = names.length;
		int choice = (int) (Math.random() * (double) n);
		return names[choice];
	}

	private static void maybeConnectCavern(String cavern, Direction direction) {
		if (Math.random() > .2) {
			String other = anyOther(cavern);
			connectIfAvailable(cavern, direction, other);
			connectIfAvailable(other, direction.opposite(), cavern);
		}
	}

	private static void connectIfAvailable(String from, Direction direction,
			String to) {
		if (game.findDestination(from, direction) == null) {
			game.connectCavern(from, to, direction);
		}
	}

	private static String anyOther(String cavern) {
		String otherCavern = cavern;
		while (cavern.equals(otherCavern)) {
			otherCavern = anyCavern();
		}
		return otherCavern;
	}

	private static String anyOther(String cavern1, String cavern2) {
		String otherCavern = cavern1;
		while (cavern1.equals(otherCavern) || cavern2.equals(otherCavern)) {
			otherCavern = anyCavern();
		}
		return otherCavern;
	}

	private static String anyCavern() {
		return caverns.get((int) (Math.random() * caverns.size()));
	}

	public void noPassage() {
		System.out.println("No Passage.");
	}

	public void hearBats() {
		System.out.println("You hear chirping.");
	}

	public void hearPit() {
		System.out.println("You hear wind.");
	}

	public void hearElixir() {
		System.out.println("You hear a bubbling potion.");
	}

	public void smellWumpus() {
		System.out.println("There is a terrible smell.");
	}

	public void passage(Direction direction) {
		System.out.println("You can go " + direction.name());
	}

	public void noArrows() {
		System.out.println("You have no arrows.");
	}

	public void arrowShot() {
		System.out.println("Thwang!");
	}

	public void playerShootsSelfInBack() {
		System.out.println("Ow!  You shot yourself in the back.");
	}

	public void playerKillsWumpus() {
		System.out.println("You killed the Wumpus.");
	}

	public void playerShootsWall() {
		System.out.println("You shot the wall and the ricochet hurt you.");
	}

	public void arrowsFound(Integer arrowsFound) {
		System.out.println("You found " + arrowsFound + " arrow"
				+ (arrowsFound == 1 ? "" : "s") + ".");
	}

	public void fellInPit() {
		System.out.println("You fell in a pit and hurt yourself.");
	}

	public void playerMovesToWumpus() {
		System.out.println("You walked into the waiting arms of the Wumpus.");
	}

	public void wumpusMovesToPlayer() {
		System.out.println("The Wumpus has found you.");
	}

	public void batsTransport() {
		System.out.println("Some bats carried you away.");
	}

	public void slowDeath() {
		System.out.println("You have died of your wounds.");
	}

	public void noElixir() {
		System.out.println("You have no elixir.");
	}

	public void playerHealed() {
		System.out.println("You used the elixir to heal yourself.");
	}

	public void elixirFound() {
		System.out.println("You found a healing elixir.");
	}

	public void fireworks() {
		System.out.println("Hooray.");

		JFrame frame = new JFrame();
		ImageIcon icon = new ImageIcon("./img/fireworks.jpg");
		JLabel label = new JLabel(icon);
		frame.add(label);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	public void end() {
		System.exit(0);
	}

	@Override
	public void wastedElixir() {
		System.out.println("You wasted the elixir :(!");
	}

	@Override
	public void smellHunter(int closeness) {
		System.out.println(game.getWumpusName() + " smells the hunter, " + closeness + " spaces away.");
		
	}
}
