package htw.fixtures;

import htw.HtwMessageReceiver;
import htw.game.HuntTheWumpusGame;

public class TestableHuntTheWumpus extends HuntTheWumpusGame {
  private boolean wumpusFrozen = false;

  protected void moveWumpus() {
    if (!wumpusFrozen)
      super.moveWumpus();
  }

  public TestableHuntTheWumpus(HtwMessageReceiver receiver) {
    super(receiver);
  }

  public void freezeWumpus() {
    wumpusFrozen = true;
  }
  protected boolean moveWumpus(String dir) {
	  Direction direction = null;
	  if ("east".equalsIgnoreCase(dir))
		  direction = Direction.EAST;
	  if ("west".equalsIgnoreCase(dir))
		  direction = Direction.WEST;
	  if ("north".equalsIgnoreCase(dir))
		  direction = Direction.NORTH;
	  if ("south".equalsIgnoreCase(dir))
		  direction = Direction.SOUTH;
	  
	  return super.moveWumpus(direction);
  }

}
