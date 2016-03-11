package htw.fixtures;

import htw.HuntTheWumpus;
import htw.HuntTheWumpus.Direction;
import htw.HuntTheWumpus.Items;

import java.util.Map;

import static htw.fixtures.TestContext.game;

public class HtwFixture {
	public boolean ConnectCavernToGoing(String c1, String c2, String dir) {
		game.connectCavern(c1, c2, toDirection(dir));
		return true;
	}

	public boolean putPlayerInCavern(String c) {
		game.setPlayerCavern(c);
		return true;
	}

	public boolean movePlayer(String dir) {
		game.makeMoveCommand(toDirection(dir)).execute();
		return true;
	}

	public boolean moveWumpus(String dir) {
		game.setWumpus(true);
		game.setGameMode("co-hunt");
		game.moveWumpus(dir);
		return true;
	}

	public String getPlayerCavern() {
		return game.getPlayerCavern();
	}

	public boolean putWumpusInCavern(String c) {
		game.setWumpusCavern(c);
		return true;
	}

	public String getWumpusCavern() {
		return game.getWumpusCavern();
	}

	public boolean freezeWumpus() {
		game.freezeWumpus();
		return true;
	}

	public boolean MessageIdWasGiven(String message) {
		return (TestContext.messages.contains(message));
	}

	public boolean MessageIdWasNotGiven(String message) {
		return !MessageIdWasGiven(message);
	}

	public boolean clearMessages() {
		TestContext.messages.clear();
		return true;
	}

	public boolean setCavernAsPit(String cavern) {
		game.addPitCavern(cavern);
		return true;
	}

	public boolean setCavernAsBats(String cavern) {
		game.addBatCavern(cavern);
		return true;
	}

	public boolean setCavernAsElixir(String cavern) {
		game.setElixirCavern(cavern);
		return true;
	}

	public boolean rest() {
		game.makeRestCommand().execute();
		return true;
	}

	public boolean RestTimesWithWumpusInEachTime(int times, String cavern) {
		TestContext.wumpusCaverns.clear();
		for (int i = 0; i < times; i++) {
			putWumpusInCavern(cavern);
			game.makeRestCommand().execute();
			incrementCounter(TestContext.wumpusCaverns, game.getWumpusCavern());
		}
		return true;
	}

	private int zeroIfNull(Integer integer) {
		return integer == null ? 0 : integer;
	}

	public boolean MovePlayerTimesWithPlayerInEachTime(int times, String direction, String startingCavern) {
		TestContext.batTransportCaverns.clear();
		for (int i = 0; i < times; i++) {
			putPlayerInCavern(startingCavern);
			game.makeMoveCommand(toDirection(direction)).execute();
			incrementCounter(TestContext.batTransportCaverns, game.getPlayerCavern());
		}
		return true;
	}

	private void incrementCounter(Map<String, Integer> counterMap, String cavern) {
		counterMap.put(cavern, zeroIfNull(counterMap.get(cavern)) + 1);
	}

	public boolean restUntilKilled() {
		while (!getPlayerCavern().equals(getWumpusCavern()))
			game.makeRestCommand().execute();

		return true;
	}

	public boolean setArrowsInQuiverTo(int arrows) {
		game.setQuiver(arrows);
		return true;
	}

	public boolean shootArrow(String direction) {
		game.makeShootCommand(toDirection(direction)).execute();
		return true;
	}

	public boolean drinkElixir() {
		game.makeHealCommand().execute();
		return true;
	}

	public boolean setElixirTo(boolean hasElixir) {
		Items items = new Items();
		items.setElixir(hasElixir);
		game.setItems(items);
		return true;
	}

	private HuntTheWumpus.Direction toDirection(String direction) {
		return HuntTheWumpus.Direction.valueOf(direction.toUpperCase());
	}

	public int arrowsInQuiver() {
		return game.getQuiver();
	}

	public boolean playerHasElixir() {
		return game.getItems().hasElixir();
	}

	public boolean cavernHasElixir(String cavern) {
		return cavern.equalsIgnoreCase(game.getElixirCavern());
	}

	public int arrowsInCavern(String cavern) {
		return game.getArrowsInCavern(cavern);
	}

	public boolean setHitPointsTo(int hitpoints) {
		game.setHitPoints(hitpoints);
		return true;
	}

	public int hitpointsRemaining() {
		return game.getHitPoints();
	}

	public String getHunterName() {
		return game.getHunterName();
	}

	public String getWumpusName() {
		return game.getWumpusName();
	}

	public void setHunterName(String name) {
		game.setHunterName(name);
	}

	public void setWumpusName(String name) {
		game.setWumpusName(name);
	}
}
