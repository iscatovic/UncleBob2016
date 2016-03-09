//
//package htw.console;
//
//import htw.HtwMessageReceiver;
//import htw.HuntTheWumpus;
//import htw.HuntTheWumpus.Direction;
//import htw.factory.HtwFactory;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.ArrayList;
//import java.util.List;
//
//import static htw.HuntTheWumpus.Direction.*;
//
//public class MainXX implements HtwMessageReceiver {
//  private static ConsoleLogic console = new ConsoleLogic();
//  private static final List<String> caverns = new ArrayList<>();
//
//  public static void main(String[] args) throws IOException {
//	
//    HuntTheWumpus game = HtwFactory.makeGame("htw.game.HuntTheWumpusGame", new Main());
//    ConsoleLogic.setGame(game);
//    ConsoleLogic.setHitPoints(10);
//    createMap(game);
//    ConsoleLogic.initFirstRound();
// 
//    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//    
//    while (true) {
//      System.out.println(ConsoleLogic.getGame().getPlayerCavern());
//      System.out.println(console.getStatus());
//      System.out.println(">");
//      String command = br.readLine();
//      console.performTurn(command);
//    }
//  }
//
//  private static void createMap(HuntTheWumpus game) {
//    int ncaverns = (int) (Math.random() * 30.0 + 10.0);
//    while (ncaverns-- > 0)
//      caverns.add(ConsoleLogic.makeName());
//
//    for (String cavern : caverns) {
//      maybeConnectCavern(cavern, NORTH);
//      maybeConnectCavern(cavern, SOUTH);
//      maybeConnectCavern(cavern, EAST);
//      maybeConnectCavern(cavern, WEST);
//    }
//
//    String playerCavern = anyCavern();
//    String elixirCavern = anyCavern();
//    game.setPlayerCavern(playerCavern);
//    game.setWumpusCavern(anyOther(playerCavern));
//    
//    game.setElixirCavern(anyOther(playerCavern));
//    
//    game.addBatCavern(anyOther(playerCavern, elixirCavern));
//    game.addBatCavern(anyOther(playerCavern, elixirCavern));
//    game.addBatCavern(anyOther(playerCavern, elixirCavern));
//
//    game.addPitCavern(anyOther(playerCavern));
//    game.addPitCavern(anyOther(playerCavern));
//    game.addPitCavern(anyOther(playerCavern));
//
//    game.setQuiver(5);
//  }
//
//  private static void maybeConnectCavern(String cavern, Direction direction) {
//    if (Math.random() > .2) {
//      String other = anyOther(cavern);
//      connectIfAvailable(cavern, direction, other);
//      connectIfAvailable(other, direction.opposite(), cavern);
//    }
//  }
//
//  private static void connectIfAvailable(String from, Direction direction, String to) {
//    ConsoleLogic.connectIfAvailable(from, direction, to);
//  }
//
//  private static String anyOther(String cavern) {
//    String otherCavern = cavern;
//    while (cavern.equals(otherCavern)) {
//      otherCavern = anyCavern();
//    }
//    return otherCavern;
//  }
//  
//  private static String anyOther(String cavern1, String cavern2) {
//	  String otherCavern = cavern1;
//	  while (cavern1.equals(otherCavern)||cavern2.equals(otherCavern)) {
//		  otherCavern = anyCavern();
//	  }
//	  return otherCavern;
//  }
//
//  private static String anyCavern() {
//    return caverns.get((int) (Math.random() * caverns.size()));
//  }
//
//  public void noPassage() {
//    System.out.println("No Passage.");
//  }
//
//  public void hearBats() {
//    System.out.println("You hear chirping.");
//  }
//
//  public void hearPit() {
//	    System.out.println("You hear wind.");
//	  }
//  
//  public void hearElixir() {
//		System.out.println("You hear a bubbling potion.");
//	}
//
//  public void smellWumpus() {
//    System.out.println("There is a terrible smell.");
//  }
//
//  public void passage(Direction direction) {
//    System.out.println("You can go " + direction.name());
//  }
//
//  public void noArrows() {
//    System.out.println("You have no arrows.");
//  }
//
//  public void arrowShot() {
//    System.out.println("Thwang!");
//  }
//
//  public void playerShootsSelfInBack() {
//    System.out.println("Ow!  You shot yourself in the back.");
//    console.playerShootsSelfInBack();
//  }
//
//  public void playerKillsWumpus() {
//    System.out.println("You killed the Wumpus.");
//    console.playerKillsWumpus();
//  }
//
//  public void playerShootsWall() {
//    System.out.println("You shot the wall and the ricochet hurt you.");
//    console.playerShootsWall();
//  }
//
//  public void arrowsFound(Integer arrowsFound) {
//    System.out.println("You found " + arrowsFound + " arrow" + (arrowsFound == 1 ? "" : "s") + ".");
//  }
//
//  public void fellInPit() {
//    System.out.println("You fell in a pit and hurt yourself.");
//    console.fellInPit();
//  }
//
//  public void playerMovesToWumpus() {
//    System.out.println("You walked into the waiting arms of the Wumpus.");
//    console.playerMovesToWumpus();
//  }
//
//  public void wumpusMovesToPlayer() {
//    System.out.println("The Wumpus has found you.");
//    console.playerMovesToWumpus();
//  }
//
//  public void batsTransport() {
//    System.out.println("Some bats carried you away.");
//  }
//  
//  public void noElixir() {
//	  System.out.println("You have no elixir.");
//  }
//
//  public void playerHealed(String type, int amount) {
//	  console.playerHealed(type, amount);
//	  System.out.println("You used the elixir to heal yourself.");	
//  }
//
//  public void elixirFound() {
//	  System.out.println("You found a healing elixir.");
//  }
//}
//
//
