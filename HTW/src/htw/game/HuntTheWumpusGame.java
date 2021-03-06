package htw.game;

import htw.HtwMessageReceiver;
import htw.HuntTheWumpus;

import java.util.*;
import java.util.function.Predicate;

public class HuntTheWumpusGame implements HuntTheWumpus {
	private List<Connection> connections = new ArrayList<>();

	private Set<String> caverns = new HashSet<>();
	private String playerCavern = "NONE";
	private HtwMessageReceiver messageReceiver;
	private Set<String> batCaverns = new HashSet<>();
	private Set<String> pitCaverns = new HashSet<>();
	private String wumpusCavern = "NONE";
	private int quiver = 0;
	private Map<String, Integer> arrowsIn = new HashMap<>();
	private String elixirCavern = "NONE";
	private Items items = new Items();
	private int hitPoints;
	private String hunterName = "Hunter";
	private String wumpusName = "Wumpus";
	private String gameMode = "Standard";
	private String[] modes = { "Standard", "Co-Hunt", "test" };
	private boolean wumpus = false;

	public int getHitPoints() {
		return hitPoints;
	}

	public void setHitPoints(int hitPoints) {
		this.hitPoints = hitPoints;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public String getHunterName() {
		return hunterName;
	}

	public void setHunterName(String hunterName) {
		if (hunterName != null && hunterName.trim().length() != 0)
			this.hunterName = hunterName;
		else
			this.hunterName = "Hunter";
	}

	public String getWumpusName() {
		return wumpusName;
	}

	public void setWumpusName(String wumpusName) {
		if (wumpusName != null && wumpusName.trim().length() != 0)
			this.wumpusName = wumpusName;
		else
			this.wumpusName = "Wumpus";
	}

	public String getGameMode() {
		return gameMode;
	}

	public void setGameMode(String gameMode) {
		for (String s : modes)
			if (s.equalsIgnoreCase(gameMode))
				this.gameMode = s;
	}

	public boolean isWumpus() {
		return wumpus;
	}

	public void setWumpus(boolean wumpus) {
		this.wumpus = wumpus;
	}

	private void hit(int points) {
		hitPoints -= points;
		if (hitPoints <= 0) {
			System.out.println("You have died of your wounds.");
			System.exit(0);
		}
	}

	public HuntTheWumpusGame(HtwMessageReceiver receiver) {
		this.messageReceiver = receiver;
	}

	public void setPlayerCavern(String playerCavern) {
		this.playerCavern = playerCavern;
	}

	public String getPlayerCavern() {
		return playerCavern;
	}

	public void setElixirCavern(String elixirCavern) {
		this.elixirCavern = elixirCavern;
	}

	public String getElixirCavern() {
		return elixirCavern;
	}

	public void reportStatus() {
		reportAvailableDirections();
		if (!isWumpus()) {
			if (reportNearbyHunter(c -> batCaverns.contains(c.to)))
				messageReceiver.hearBats();
			if (reportNearbyHunter(c -> pitCaverns.contains(c.to)))
				messageReceiver.hearPit();
			if (reportNearbyHunter(c -> wumpusCavern.equals(c.to)))
				messageReceiver.smellWumpus();
			if (reportNearbyHunter(c -> elixirCavern.equals(c.to)))
				messageReceiver.hearElixir();
		} else {
			int closeness = reportNearbyWumpusOneSpace(c -> playerCavern.equals(c.to));
			if (closeness > 0)
				messageReceiver.smellHunter(closeness);
		}
	}

	private boolean reportNearbyHunter(Predicate<Connection> nearTest) {
		for (Connection c : connections)
			if (playerCavern.equals(c.from) && nearTest.test(c))
				return true;
		return false;
	}

	private int reportNearbyWumpusOneSpace(Predicate<Connection> nearTest) {
		List<Connection> perimeter = new ArrayList<Connection>();
		for (Connection c : connections)
		{
			if (wumpusCavern.equals(c.from)) {
				perimeter.add(c);
				if (nearTest.test(c))
					return 1;
			}
		} // you want to check all the immediate spaces before checking any further
		for (Connection p : perimeter) {
				if (reportNearbyWumpusTwoSpaces(p.to,nearTest))
					return 2;
		}
		return 0;
	}

	private boolean reportNearbyWumpusTwoSpaces(String perimeterPoint,Predicate<Connection> nearTest) {
		for (Connection c : connections)
		{
			if (perimeterPoint.equals(c.from) && nearTest.test(c))
					return true;
		} 
		return false;
	}

	private void reportAvailableDirections() {
		String thisCavern = wumpus ? wumpusCavern : playerCavern;
		for (Connection c : connections) {
			if (thisCavern.equals(c.from))
				messageReceiver.passage(c.direction);
		}
	}

	public void addBatCavern(String cavern) {
		batCaverns.add(cavern);
	}

	public void addPitCavern(String cavern) {
		pitCaverns.add(cavern);
	}

	public void setWumpusCavern(String wumpusCavern) {
		this.wumpusCavern = wumpusCavern;
	}

	public String getWumpusCavern() {
		return wumpusCavern;
	}

	protected void moveWumpus() {
		List<String> wumpusChoices = new ArrayList<>();
		for (Connection c : connections)
			if (wumpusCavern.equals(c.from))
				wumpusChoices.add(c.to);
		wumpusChoices.add(wumpusCavern);

		int nChoices = wumpusChoices.size();
		int choice = (int) (Math.random() * nChoices);
		wumpusCavern = wumpusChoices.get(choice);
	}
	protected boolean moveWumpus(Direction direction) {
		String destination = findDestination(wumpusCavern, direction);
		if (destination != null) {
			wumpusCavern = destination;
			return true;
		}
		return false;
	}

	private void randomlyTransportPlayer() {
		Set<String> transportChoices = new HashSet<>(caverns);
		transportChoices.remove(playerCavern);
		int nChoices = transportChoices.size();
		int choice = (int) (Math.random() * nChoices);
		String[] choices = new String[nChoices];
		playerCavern = transportChoices.toArray(choices)[choice];
	}

	public void setQuiver(int arrows) {
		this.quiver = arrows;
	}

	public int getQuiver() {
		return quiver;
	}

	public Integer getArrowsInCavern(String cavern) {
		return zeroIfNull(arrowsIn.get(cavern));
	}

	private int zeroIfNull(Integer integer) {
		if (integer == null)
			return 0;
		else
			return integer.intValue();
	}

	private class Connection {
		String from;
		String to;
		Direction direction;

		public Connection(String from, String to, Direction direction) {
			this.from = from;
			this.to = to;
			this.direction = direction;
		}
	}

	public void connectCavern(String from, String to, Direction direction) {
		connections.add(new Connection(from, to, direction));
		caverns.add(from);
		caverns.add(to);
	}

	public String findDestination(String cavern, Direction direction) {
		for (Connection c : connections)
			if (c.from.equals(cavern) && c.direction == direction)
				return c.to;
		return null;
	}

	public Command makeRestCommand() {
		return new RestCommand();
	}

	public Command makeShootCommand(Direction direction) {
		return new ShootCommand(direction);
	}

	public Command makeMoveCommand(Direction direction) {
		return new MoveCommand(direction);
	}

	public Command makeHealCommand() {
		return new HealCommand();
	}

	public abstract class GameCommand implements Command {
		public void execute() {
			processCommand();
			if (!gameMode.equals("Co-Hunt")) {
				moveWumpus();
				checkWumpusMovedToPlayer();
				reportStatus();
			}

		}

		protected void checkWumpusMovedToPlayer() {
			if (playerCavern.equals(wumpusCavern)) {
				messageReceiver.wumpusMovesToPlayer();
				messageReceiver.end();
			}
		}

		protected abstract void processCommand();

	}

	private class RestCommand extends GameCommand {
		public void processCommand() {
		}
	}

	private class HealCommand extends GameCommand {
		public void processCommand() {
			if (items.hasElixir()) {
				if (hitPoints != 10)
					healPlayer();
				else
					wastedElixir();
				items.setElixir(false);
			} else
				messageReceiver.noElixir();
		}

		private void wastedElixir() {
			items.setElixir(false);
			messageReceiver.wastedElixir();
		}

		private void healPlayer() {
			setHitPoints(10);
			items.setElixir(false);
			messageReceiver.playerHealed();
		}
	}

	private class ShootCommand extends GameCommand {
		private Direction direction;

		public ShootCommand(Direction direction) {
			this.direction = direction;
		}

		public void processCommand() {
			if (quiver == 0)
				messageReceiver.noArrows();
			else {
				messageReceiver.arrowShot();
				quiver--;
				ArrowTracker arrowTracker = new ArrowTracker(playerCavern)
						.trackArrow(direction);
				if (arrowTracker.arrowHitSomething())
					return;
				incrementArrowsInCavern(arrowTracker.getArrowCavern());
			}
		}

		private void incrementArrowsInCavern(String arrowCavern) {
			int arrows = getArrowsInCavern(arrowCavern);
			arrowsIn.put(arrowCavern, arrows + 1);
		}

		private class ArrowTracker {
			private boolean hitSomething = false;
			private String arrowCavern;

			public ArrowTracker(String startingCavern) {
				this.arrowCavern = startingCavern;
			}

			boolean arrowHitSomething() {
				return hitSomething;
			}

			public String getArrowCavern() {
				return arrowCavern;
			}

			public ArrowTracker trackArrow(Direction direction) {
				String nextCavern;
				for (int count = 0; (nextCavern = nextCavern(arrowCavern,
						direction)) != null; count++) {
					arrowCavern = nextCavern;
					if (shotSelfInBack())
						return this;
					if (shotWumpus())
						return this;
					if (count > 100)
						return this;
				}
				if (arrowCavern.equals(playerCavern)) {
					messageReceiver.playerShootsWall();
					hit(3);
				}
				return this;
			}

			private boolean shotWumpus() {
				if (arrowCavern.equals(wumpusCavern)) {
					messageReceiver.playerKillsWumpus();
					messageReceiver.fireworks();
					messageReceiver.end();
					hitSomething = true;
					return true;
				}
				return false;
			}

			private boolean shotSelfInBack() {
				if (arrowCavern.equals(playerCavern)) {
					messageReceiver.playerShootsSelfInBack();
					hit(3);
					hitSomething = true;
					return true;
				}
				return false;
			}

			private String nextCavern(String cavern, Direction direction) {
				for (Connection c : connections)
					if (cavern.equals(c.from) && direction.equals(c.direction))
						return c.to;
				return null;
			}
		}
	}

	private class MoveCommand extends GameCommand {
		private Direction direction;

		public MoveCommand(Direction direction) {
			this.direction = direction;
		}

		public void processCommand() {
			if (wumpus) {
				if (moveWumpus(direction))
					checkForHunter();
				else
					messageReceiver.noPassage();
			}

			if (!wumpus) {
				if (movePlayer(direction)) {
					checkForWumpus();
					checkForPit();
					checkForBats();
					checkForArrows();
					checkForElixir();
				} else
					messageReceiver.noPassage();
			}
		}

		private void checkForWumpus() {
			if (wumpusCavern.equals(playerCavern)) {
				messageReceiver.playerMovesToWumpus();
				messageReceiver.end();
			}
		}

		private void checkForHunter() {
			if (wumpusCavern.equals(playerCavern)) {
				messageReceiver.wumpusFoundHunter();
				messageReceiver.end();
			}
		}

		private void checkForBats() {
			if (batCaverns.contains(playerCavern)) {
				messageReceiver.batsTransport();
				randomlyTransportPlayer();
			}
		}

		public boolean movePlayer(Direction direction) {
			String destination = findDestination(playerCavern, direction);
			if (destination != null) {
				playerCavern = destination;
				return true;
			}
			return false;
		}

		public boolean moveWumpus(Direction direction) {
			String destination = findDestination(wumpusCavern, direction);
			if (destination != null) {
				wumpusCavern = destination;
				return true;
			}
			return false;
		}

		private void checkForPit() {
			if (pitCaverns.contains(playerCavern)) {
				messageReceiver.fellInPit();
				hit(4);
			}
		}

		private void checkForArrows() {
			Integer arrowsFound = getArrowsInCavern(playerCavern);
			if (arrowsFound > 0)
				messageReceiver.arrowsFound(arrowsFound);
			quiver += arrowsFound;
			arrowsIn.put(playerCavern, 0);
		}

		private void checkForElixir() {
			if (elixirCavern.equals(playerCavern)) {
				items.setElixir(true);
				elixirCavern = "NONE";
				messageReceiver.elixirFound();
			}

		}
	}
}
