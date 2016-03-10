package htw.unittests;

import org.junit.Assert;
import org.junit.Test;

import htw.HuntTheWumpus;
import htw.console.Main;
import htw.factory.HtwFactory;
import htw.fixtures.TestContext;
import htw.game.HuntTheWumpusGame;

public class GameModeTest extends TestContext{

	private HuntTheWumpus game =  HtwFactory.makeGame("htw.game.HuntTheWumpusGame", this);	
	
	@Test
	public void characterNamesTest() {
		
		Assert.assertTrue("Hunter".equals(game.getHunterName()));
		Assert.assertTrue("Wumpus".equals(game.getWumpusName()));
		
		game.setHunterName("Bob");
		Assert.assertTrue("Bob".equals(game.getHunterName()));
		Assert.assertTrue("Wumpus".equals(game.getWumpusName()));
		
		game.setWumpusName("WumpWump");
		Assert.assertTrue("WumpWump".equals(game.getWumpusName()));
		
	}
	@Test
	public void gameModeTest() 
	{
		Assert.assertTrue("Standard".equals(game.getGameMode()));
		game.setGameMode("Co-Hunt");
		Assert.assertTrue("Co-Hunt".equals(game.getGameMode()));
		
	}
	
	
}
