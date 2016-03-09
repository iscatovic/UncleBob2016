//package htw.console;
//
//import htw.HuntTheWumpus;
//import htw.HuntTheWumpus.Direction;
//import static htw.HuntTheWumpus.Direction.*;
//
//public class ConsoleLogic {
//
//	private static HuntTheWumpus game;
//	private static int hitPoints;
//
//	public static void initFirstRound() {
//		game.makeRestCommand().execute();
//	}
//
//	public void performTurn(String command) {
//		HuntTheWumpus.Command c = game.makeRestCommand();
//		if (command.equalsIgnoreCase("e"))
//			c = game.makeMoveCommand(EAST);
//		else if (command.equalsIgnoreCase("w"))
//			c = game.makeMoveCommand(WEST);
//		else if (command.equalsIgnoreCase("n"))
//			c = game.makeMoveCommand(NORTH);
//		else if (command.equalsIgnoreCase("s"))
//			c = game.makeMoveCommand(SOUTH);
//		else if (command.equalsIgnoreCase("r"))
//			c = game.makeRestCommand();
//		else if (command.equalsIgnoreCase("sw"))
//			c = game.makeShootCommand(WEST);
//		else if (command.equalsIgnoreCase("se"))
//			c = game.makeShootCommand(EAST);
//		else if (command.equalsIgnoreCase("sn"))
//			c = game.makeShootCommand(NORTH);
//		else if (command.equalsIgnoreCase("ss"))
//			c = game.makeShootCommand(SOUTH);
//		else if (command.equalsIgnoreCase("h"))
//			c = game.makeHealCommand();
//		else if (command.equalsIgnoreCase("q"))
//			return;
//		c.execute();
//	}
//
//	public static HuntTheWumpus getGame() {
//		return game;
//	}
//
//	public static void setGame(HuntTheWumpus game) {
//		ConsoleLogic.game = game;
//	}
//
//	public static int getHitPoints() {
//		return hitPoints;
//	}
//
//	public static void setHitPoints(int hitPoints) {
//		ConsoleLogic.hitPoints = hitPoints;
//	}
//
//	public static void connectIfAvailable(String from, Direction direction,
//			String to) {
//		if (game.findDestination(from, direction) == null) {
//			game.connectCavern(from, to, direction);
//		}
//	}
//
//	public void passage(Direction direction) {
//	}
//
//	public void playerShootsSelfInBack() {
//		hit(3);
//	}
//
//	public void playerKillsWumpus() {
//		System.exit(0);
//	}
//
//	public void playerShootsWall() {
//		hit(3);
//	}
//
//	public void fellInPit() {
//		hit(4);
//	}
//
//	public void playerMovesToWumpus() {
//		System.exit(0);
//	}
//
//	public void wumpusMovesToPlayer() {
//		System.exit(0);
//	}
//
//	private void hit(int points) {
//		hitPoints -= points;
//		if (hitPoints <= 0) {
//			System.out.println("You have died of your wounds.");
//			System.exit(0);
//		}
//	}
//
//	public void playerHealed(String type, int amount) {
//		if ("full".equalsIgnoreCase(type))
//			hitPoints = amount;
//		else
//			hitPoints += amount;
//	}
//
//	public String getStatus() {
//		return "Health: " + hitPoints + " / arrows: " + game.getQuiver()
//				+ " / elixir: " + (game.getItems().hasElixir() ? 1 : 0);
//	}
//	
//	
//	  public static String makeName() {
//
//		    return "A " + chooseName(environments) + " " + chooseName(shapes) + " " +
//		      chooseName(cavernTypes) + " " + chooseName(adornments);
//		  }
//
//		  private static String chooseName(String[] names) {
//		    int n = names.length;
//		    int choice = (int)(Math.random() * (double) n);
//		    return names[choice];
//		  }
//		  
//		  private static final String[] environments = new String[]{
//			    "bright",
//			    "humid",
//			    "dry",
//			    "creepy",
//			    "ugly",
//			    "foggy",
//			    "hot",
//			    "cold",
//			    "drafty",
//			    "dreadful"
//			  };
//
//			  private static final String[] shapes = new String[] {
//			    "round",
//			    "square",
//			    "oval",
//			    "irregular",
//			    "long",
//			    "craggy",
//			    "rough",
//			    "tall",
//			    "narrow"
//			  };
//
//			  private static final String[] cavernTypes = new String[] {
//			    "cavern",
//			    "room",
//			    "chamber",
//			    "catacomb",
//			    "crevasse",
//			    "cell",
//			    "tunnel",
//			    "passageway",
//			    "hall",
//			    "expanse"
//			  };
//
//			  private static final String[] adornments = new String[] {
//			   "smelling of sulphur",
//			    "with engravings on the walls",
//			    "with a bumpy floor",
//			    "",
//			    "littered with garbage",
//			    "spattered with guano",
//			    "with piles of Wumpus droppings",
//			    "with bones scattered around",
//			    "with a corpse on the floor",
//			    "that seems to vibrate",
//			    "that feels stuffy",
//			    "that fills you with dread"
//			  };
//}
