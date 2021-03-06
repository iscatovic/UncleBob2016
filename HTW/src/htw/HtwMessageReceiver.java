package htw;

public interface HtwMessageReceiver {
	void noPassage();

	void hearBats();

	void hearPit();

	void smellWumpus();

	void passage(HuntTheWumpus.Direction direction);

	void noArrows();

	void arrowShot();

	void playerShootsSelfInBack();

	void playerKillsWumpus();

	void playerShootsWall();

	void arrowsFound(Integer arrowsFound);

	void fellInPit();

	void playerMovesToWumpus();

	void wumpusMovesToPlayer();

	void batsTransport();

	void playerHealed();

	void noElixir();

	void hearElixir();

	void elixirFound();

	void slowDeath();

	void fireworks();

	void end();

	void wastedElixir();

	void smellHunter(int closeness);

	void wumpusFoundHunter();
}
