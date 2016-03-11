package htw.unittests;

import static htw.HuntTheWumpus.Direction.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import htw.HuntTheWumpus;
import htw.HuntTheWumpus.Direction;
import htw.factory.HtwFactory;
import htw.fixtures.TestContext;

public class WumpusControlTest extends TestContext{

	private HuntTheWumpus game;
	private static final List<String> testCaverns = new ArrayList<>();
	
	@Before
	public void setup() 
	{
		game = HtwFactory.makeGame("htw.game.HuntTheWumpusGame", this);
		game.setGameMode("co-hunt");
		game.setWumpus(true);
		createTestMap();
		messages.clear();
	}
	
	@Test
	public void testMoveWumpus()
	{
		Assert.assertTrue("top".equalsIgnoreCase(game.getWumpusCavern()));
		HuntTheWumpus.Command c;
		
		c = game.makeMoveCommand(SOUTH);
		c.execute();
		Assert.assertTrue("above".equalsIgnoreCase(game.getWumpusCavern()));
		
		c = game.makeMoveCommand(NORTH);
		c.execute();
		Assert.assertTrue("top".equalsIgnoreCase(game.getWumpusCavern()));
		
		c = game.makeMoveCommand(SOUTH);
		c.execute();
		Assert.assertTrue("above".equalsIgnoreCase(game.getWumpusCavern()));
		
		
		c = game.makeMoveCommand(SOUTH);
		c.execute();
		Assert.assertTrue("middle".equalsIgnoreCase(game.getWumpusCavern()));
		
		
		c = game.makeMoveCommand(WEST);
		c.execute();
		Assert.assertTrue("left".equalsIgnoreCase(game.getWumpusCavern()));
		
		
		c = game.makeMoveCommand(EAST);
		c.execute();
		Assert.assertTrue("middle".equalsIgnoreCase(game.getWumpusCavern()));
		
		c = game.makeMoveCommand(EAST);
		c.execute();
		Assert.assertTrue("right".equalsIgnoreCase(game.getWumpusCavern()));
		
		
	}
	
	@Test
	public void testWumpusRest()
	{
		Assert.assertTrue("top".equalsIgnoreCase(game.getWumpusCavern()));
		
		HuntTheWumpus.Command c = game.makeRestCommand();
		c.execute();
		Assert.assertTrue("top".equalsIgnoreCase(game.getWumpusCavern()));
		
	}
	
	@Test
	public void testWumpusAgainstBats()
	{
		game.setWumpusCavern("Right");
		Assert.assertTrue("right".equalsIgnoreCase(game.getWumpusCavern()));
		
		HuntTheWumpus.Command c = game.makeMoveCommand(EAST);
		c.execute();
		Assert.assertTrue("farRight".equalsIgnoreCase(game.getWumpusCavern()));
		
		Assert.assertTrue(messages.size() == 0);
	}
	
	@Test
	public void testWumpusAgainstPit()
	{
		game.setWumpusCavern("Middle");
		Assert.assertTrue("middle".equalsIgnoreCase(game.getWumpusCavern()));
		
		HuntTheWumpus.Command c = game.makeMoveCommand(SOUTH);
		c.execute();
		Assert.assertTrue("below".equalsIgnoreCase(game.getWumpusCavern()));
		
		Assert.assertTrue(messages.size() == 0);
	}
	
	@Test
	public void testWumpusAgainstElixir()
	{
		game.setWumpusCavern("Left");
		Assert.assertTrue("left".equalsIgnoreCase(game.getWumpusCavern()));
		
		HuntTheWumpus.Command c = game.makeMoveCommand(WEST);
		c.execute();
		Assert.assertTrue("farleft".equalsIgnoreCase(game.getWumpusCavern()));
		
		Assert.assertTrue(messages.size() == 0);
	}
	
	@Test
	public void testWumpusStatusAndSmellsPlayer()
	{
		game.setPlayerCavern("Bottom");
		Assert.assertTrue("bottom".equalsIgnoreCase(game.getPlayerCavern()));
		Assert.assertTrue("top".equalsIgnoreCase(game.getWumpusCavern()));
		
		HuntTheWumpus.Command c = game.makeMoveCommand(SOUTH);
		
		c.execute();
		Assert.assertTrue("above".equalsIgnoreCase(game.getWumpusCavern()));
		game.reportStatus();
		Assert.assertTrue(messages.remove("NORTH_PASSAGE"));
		Assert.assertTrue(messages.remove("SOUTH_PASSAGE"));
		Assert.assertTrue(messages.size() == 0);
		
		c.execute();
		Assert.assertTrue("middle".equalsIgnoreCase(game.getWumpusCavern()));
		game.reportStatus();
		Assert.assertTrue(messages.remove("NORTH_PASSAGE"));
		Assert.assertTrue(messages.remove("WEST_PASSAGE"));
		Assert.assertTrue(messages.remove("EAST_PASSAGE"));
		Assert.assertTrue(messages.remove("SOUTH_PASSAGE"));
		Assert.assertTrue(messages.remove("SMELLS_HUNTER_AT_2"));
		Assert.assertTrue(messages.size() == 0);

		c.execute();
		Assert.assertTrue("below".equalsIgnoreCase(game.getWumpusCavern()));
		game.reportStatus();
		Assert.assertTrue(messages.remove("NORTH_PASSAGE"));
		Assert.assertTrue(messages.remove("SOUTH_PASSAGE"));
		Assert.assertTrue(messages.remove("SMELLS_HUNTER_AT_1"));
		Assert.assertTrue(messages.size() == 0);

	}
	
	@Test
	public void testWumpusFindsPlayer()
	{
		game.setWumpusCavern("Below");
		game.setPlayerCavern("Bottom");
		Assert.assertTrue("below".equalsIgnoreCase(game.getWumpusCavern()));
		Assert.assertTrue("bottom".equalsIgnoreCase(game.getPlayerCavern()));
		
		HuntTheWumpus.Command c = game.makeMoveCommand(SOUTH);

		c.execute();
		Assert.assertTrue(messages.remove("WUMPUS_FOUND_HUNTER"));
		Assert.assertTrue(messages.remove("GAME_OVER"));
		Assert.assertTrue(messages.size()==0);
	}

	private void createTestMap() {
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
			testCaverns.add(i);
		}
		game.setWumpusCavern(t);
		game.setElixirCavern(fl);
		game.addBatCavern(fr);
		game.addPitCavern(b);
		
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
	
	
}
