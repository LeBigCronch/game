import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;
public class game {
  static int playerX = 0;
  static int playerY = 0;
  static int randint = 0;
  static int randint2 = 0;
  static int i = 0;
  static boolean mine = false;
  static boolean place = false;
  static int blocks = 0;
  static int enemyY = 0;
  static int enemyX = 0;
  static boolean enemyalive = true;
  static int health = 3;
  static String material = "@";
  static int depth = 0;
  static int teleports = 1;
  static boolean teleporting = false;
  static int bossX = 1;
  static int bossY = 1;
  static String bossmaterial = "\u001B[33m";
  static boolean bossfight = false;
  static int bosshealth = 0;
  static String [][] array = new String[30][30];

  public static void main (String[] args) throws Exception {  // main method to call things
    int hault = 0;
    enemyY = (int)(Math.random()*array.length -1);
    cavegen();
    render();
    System.out.println("type \'help\' to get a list of controls");
    while(true){
      if(health < 1){
        System.out.println("you deathed");
        while(true){
        }
      }
      if(bossfight){
        if(hault == 2){
          hault = 0;
          }
        if(hault == 0){
          bossBehavior();
        }
        hault++;
        if(bosshealth < 1){
          bossfight = false;
          bossmaterial = "\u001B[0m";
          renderboss();
          inputs();
          array[array.length/2][array.length/2] = "\u001B[34;1m"+ "0" + "\u001B[0m";
          array[array.length/2][(array.length/2)-1] = "\u001B[34;1m"+ "0" + "\u001B[0m";
          array[(array.length/2)-1][(array.length/2)-1] = "\u001B[34;1m"+ "0" + "\u001B[0m";
          array[(array.length/2)-1][(array.length/2)] = "\u001B[34;1m"+ "0" + "\u001B[0m";
          health++;
        }
      }
      enemybehavior();
      inputs();
      if(enemyalive == false){
        int chance = (int)(Math.random()*20);
        if(chance == 0){
          enemyY = (int)(Math.random()*array.length -1);
          spawnenemy();
          enemyalive = true;
        }
      }
    }
  }

  static void cavegen () throws Exception { // generates cave
    playerY = (int)(Math.random()*array.length); // number generated to pick a playerY for the player to go on
    int hault = 0; // for haulting the generation of ranges
    for(int d = 0; d < array.length; d++) { // modifying each array
      i = d;
      Arrays.fill(array[i], material);
      hault++;
      if(hault == 3) {          // generated ranges
        randint = (int)(Math.random()*array.length);
        randint2 = (int)(Math.random()*array.length);
        hault = 0;
      }
      if(randint2 > randint) {  // finding the larger of 2 range values
        range(randint+1, randint2+1);
      } else {
        range(randint2+1, randint+1);
      }
      if(playerY == i) { // determines where to put the player
        if(randint > randint2) {
          playerX = (int) ((Math.random() * (randint - randint2)) + randint2);
        } else {
          playerX = (int) ((Math.random() * (randint2 - randint)) + randint);
        }
        array[i][playerX] = "\u001B[34m"+ "P" + "\u001B[0m";
      }
    spawnenemy();
    }
  }

  static void range(int min, int max) { // replaces chunks of the array with " "
    for (int t = min; t < max; t++) {
      array[i][t] = " ";
      array[i][array.length /2] = " ";
    }
  }

  static void render(){ // displays the array
    System.out.print("\033[H\033[2J");  // clears the console
    System.out.flush();
    int number = 0;
    String x = " ";
    for(int e = 0; e < array.length; e++){
    if(teleporting){
      if(number > 9){
        x = "";
      }
      System.out.print(number + "" + x);
      number++;
    }
    System.out.println(Arrays.toString(array[e]));
    }
    if(teleporting){
      x = " ";
      System.out.print(" -,");
      number = 0;
    for(int i = 0; i < array.length; i++){
      if(number > 9){
        x = "";
      }
      System.out.print(number + "," + x);
      number++;
    }
    System.out.println("");
    }
    if(bossfight){
      System.out.println("boss health:" + " " + bosshealth);
    }
    System.out.println("health: " + "" + health + " ");
  }

  static void inputs() throws Exception{ // handles inputs
    Scanner input = new Scanner(System.in);
    Scanner input2 = new Scanner(System.in);
    String userin = input.nextLine();
    if(mine == true){
      Thread.sleep(300);
    }
    if("d".equals(userin) && playerX < array.length -1 && array[playerY][playerX +1] != "\u001B[31m" + "E" + "\u001B[0m" && array[playerY][playerX +1] != "."){//)] move in positive x
      if(array[playerY][playerX +1] != material || mine == true ){
        if(array[playerY][playerX+1] == material){
          blocks++;
        }
        if(array[playerY][playerX + 1] == "\u001B[35m" + "%" + "\u001B[0m"){
          teleports++;
        }
        if(array[playerY][playerX + 1] == "\u001B[34;1m"+ "0" + "\u001B[0m"){
          dungeonGen();
        }
        if(array[playerY][playerX + 1] == "\u001B[33m" + "#" + "\u001B[0m"){
          bossmaterial = "\u001B[33m";
          bossRoomGen();
        }
        if(array[playerY][playerX + 1] == "\u001b[38;5;198m" + "^" + "\u001B[0m"){
          health++;
        }
          array[playerY][playerX] = " ";
          array[playerY][playerX +1] = "\u001B[34m"+ "P" + "\u001B[0m";
          playerX++;
          render();
      }
    }
    if("a".equals(userin) && playerX > 0 && array[playerY][playerX -1] != "\u001B[31m" + "E" + "\u001B[0m" && array[playerY][playerX -1] != "."){ // move in negative x
      if(array[playerY][playerX -1] != material || mine == true){
        if(array[playerY][playerX -1] == material){
          blocks++;
        }
      if(array[playerY][playerX - 1]== "\u001B[35m" + "%" + "\u001B[0m"){
        teleports++;
      }
      if(array[playerY][playerX - 1] == "\u001B[34;1m"+ "0" + "\u001B[0m"){
        dungeonGen();
      }
      if(array[playerY][playerX - 1] == "\u001B[33m" + "#" + "\u001B[0m"){
        bossmaterial = "\u001B[33m";
        bossRoomGen();
      }
      if(array[playerY][playerX - 1] == "\u001b[38;5;198m" + "^" + "\u001B[0m"){
        health++;
      }
      if(array[playerY][playerX -1] == "\u001B[33m" + "&" + "\u001B[0m"){
        health--;
        playerY = (int)(Math.random()*array.length -2);
        playerX = (int)(Math.random()*array.length -2);
        playerY++;
        playerX++;
      }
        array[playerY][playerX] = " ";
        array[playerY][playerX -1] = "\u001B[34m"+ "P" + "\u001B[0m";
        playerX--;
        render();
      }
    }
    if("w".equals(userin) && playerY > 0 && array[playerY -1][playerX] != "\u001B[31m" + "E" + "\u001B[0m" && array[playerY -1][playerX] != ".") {// move in positive y
      if(array[playerY-1][playerX] != material || mine == true){
        if(array[playerY-1][playerX] == material){
          blocks++;
        }
      if(array[playerY -1][playerX] == "\u001B[35m" + "%" + "\u001B[0m"){
      teleports++;
      }
      if(array[playerY -1][playerX] == "\u001B[34;1m"+ "0" + "\u001B[0m"){
        dungeonGen();
      }
      if(array[playerY-1][playerX] == "\u001B[33m" + "#" + "\u001B[0m"){
        bossmaterial = "\u001B[33m";
        bossRoomGen();
      }
      if(array[playerY-1][playerX] == "\u001b[38;5;198m" + "^" + "\u001B[0m"){
        health++;
      }
      if(array[playerY-1][playerX] == "\u001B[33m" + "&" + "\u001B[0m"){
        health--;
        playerY = (int)(Math.random()*array.length -2);
        playerX = (int)(Math.random()*array.length -2);
        playerY++;
        playerX++;
      }
        array[playerY][playerX] = " ";
        array[playerY -1][playerX] = "\u001B[34m"+ "P" + "\u001B[0m";
        playerY--;
        render();
      }
    }
    if("s".equals(userin) && playerY < array.length -1 && array[playerY +1][playerX] != "\u001B[31m" + "E" + "\u001B[0m" && array[playerY +1][playerX] != "."){ // move in negative y
      if(array[playerY+1][playerX] != material || mine == true){
        if(array[playerY+1][playerX] == material){
          blocks++;
        }
        if(array[playerY +1][playerX] == "\u001B[35m" + "%" + "\u001B[0m"){
          teleports++;
        }
        if(array[playerY +1][playerX] == "\u001B[34;1m"+ "0" + "\u001B[0m"){
          dungeonGen();
        }
        if(array[playerY+1][playerX] == "\u001B[33m" + "#" + "\u001B[0m"){
          bossmaterial = "\u001B[33m";
          bossRoomGen();
        }
        if(array[playerY+1][playerX] == "\u001b[38;5;198m" + "^" + "\u001B[0m"){
          health++;
        }
        if(array[playerY+1][playerX] == "\u001B[33m" + "&" + "\u001B[0m"){
          health--;
          playerY = (int)(Math.random()*array.length -2);
          playerX = (int)(Math.random()*array.length -2);
          playerY++;
          playerX++;
        }
      if(playerY < array.length -1){
        array[playerY][playerX] = " ";
        array[playerY +1][playerX] = "\u001B[34m"+ "P" + "\u001B[0m";
        playerY++;
        render();
      }
    }
    if("s".equals(userin) && playerY == array.length -1){
      depth++;
      if(depth == 1){
        material = "*";
        cavegen();
      }
      if(depth == 2){
        material = "-";
        cavegen();
      }
      if(depth == 3){
        material = "@";
        dungeonGen();
      }
      spawnenemy();
      render();
      }
      render();
    }
    if("stats".equals(userin)){
      System.out.println("blocks:" + " " + blocks);
      System.out.println("teleports:" + " " + teleports);
    }
    if("mine".equals(userin)){
      mine = !mine;
      System.out.println("mining toggled");
    }
    if("place".equals(userin)){
      place = !place;
      System.out.println("placement toggled");
    }
    if("j".equals(userin)){
      if(place == false){
        fightL();
      }
      if(place == true && blocks > 0){
        array[playerY][playerX -1] = material;
        blocks--;
      render();
      }
    }
    if("l".equals(userin)){
      if(place == false){
        fightR();
      }
      if(place == true && blocks > 0){
        array[playerY][playerX +1] = material;
        blocks--;
        render();
      }
    }
    if("i".equals(userin)){
      if(place == false){
        fightU();
      }
      if(place == true && blocks > 0){
        array[playerY-1][playerX] = material;
        blocks--;
        render();
      }
    }
    if("k".equals(userin)){
      if(place == false){
        fightD();
      }
      if(place == true && blocks > 0){
        array[playerY+1][playerX] = material;
        blocks--;
        render();
      }
    }
    if("n".equals(userin)){
      dungeonGen();
      render();
    }
    if("teleport".equals(userin) || "tp".equals(userin)  && teleports > 0){
      int tempPlayerX = playerX;
      int tempPlayerY = playerY;
      try{
        teleporting = true;
        render();
        array[playerY][playerX] = " ";
        render();
        System.out.println("Enter coordinates (X first, then Y)");
        playerX = input2.nextInt();
        playerY = input2.nextInt();
        array[playerY][playerX] = "\u001B[34m"+ "P" + "\u001B[0m";
        teleporting = false;
        teleports--;
        render();
      }
      catch (ArrayIndexOutOfBoundsException e){
        playerX = tempPlayerX;
        playerY = tempPlayerY;
        teleporting = false;
        System.out.println("teleport out of bounds");
        inputs();
      }
      catch (InputMismatchException e){
        teleporting = false;
        inputs();
      }
    }
    if("help".equals(userin)){
      render();
      System.out.println("hit enter after every input (i know its annoying but its the only way i figured out how to do things)");
      System.out.println("WASD to move");
      System.out.println("I J K L to shoot by default");
      System.out.println("type \'place\' to toggle placement of blocks, I J K L to place a block in that direction");
      System.out.println("type \'mine\' to toggle mining (mining will slow you down)");
      System.out.println("type \'teleport\' or \'tp\' to bring up teleporting menu");
      System.out.println("type \'stats\' to get stats");
      System.out.println("if you're in the caves, keep going down to discover more");
      System.out.println("\u001B[31m" + "E" + "\u001B[0m" + " " + "is an enemy");
      System.out.println("\u001B[35m" + "%" + "\u001B[0m" + " " + "will give you another teleport");
      System.out.println("\u001B[34;1m" + "0" + "\u001B[0m" + " " + "will bring you into a new dungeon");
      System.out.println( "\u001B[33m" + "#" + "\u001B[0m" + " " + "will bring you to a bossfight");
      System.out.println("\u001b[38;5;198m" + "^" + "\u001B[0m" + " " + "will give you 1 more health");
    }
    if(blocks < 1){
      place = false;
    }
  }
  
  static void spawnenemy(){
    for(int sl = 0; sl < array.length; sl++){
      for(int si = 0; si < array.length; si++){
        if(array[sl][si]!= material && array[sl][si] != "."){
          enemyY = sl;
          enemyX = si;
          break;
        }
      }
    }
    array[enemyY][enemyX] = "\u001B[31m" + "E" + "\u001B[0m";
  }

  static void enemybehavior() throws InterruptedException{
    if(enemyalive == true){
      if(enemyX < array.length -1 && array[enemyY][enemyX +1] != material && enemyX < playerX && array[enemyY][enemyX +1] != "."){
        array[enemyY][enemyX +1] = "\u001B[31m" + "E" + "\u001B[0m";
        array[enemyY][enemyX] = " ";
        enemyX++;
        render();
      }
      if(enemyX != 0 && array[enemyY][enemyX -1] != material && enemyX > playerX && array[enemyY][enemyX +1] != "."){
        array[enemyY][enemyX -1] = "\u001B[31m" + "E" + "\u001B[0m";
        array[enemyY][enemyX] = " ";
        enemyX--;
      }
      if(enemyY < array.length -1 && array[enemyY +1][enemyX] != material && enemyY < playerY && array[enemyY][enemyX +1] != "."){
        array[enemyY+1][enemyX] = "\u001B[31m" + "E" + "\u001B[0m";
        array[enemyY][enemyX] = " ";
        enemyY++;
      }
      if(enemyY != 0 && array[enemyY -1][enemyX] != material && enemyY > playerY && array[enemyY][enemyX +1] != "."){
        array[enemyY-1][enemyX] = "\u001B[31m" + "E" + "\u001B[0m";
        array[enemyY][enemyX] = " ";
        enemyY--;
      }
      if(enemyY == playerY && enemyX == playerX){
        array[enemyY][enemyX] = "\u001B[34m"+ "P" + "\u001B[0m";
        enemyalive = false;
        health--;
        render();
      }
    }
  }

  static void fightR() throws InterruptedException{
    for(int e = playerX+1; e < array.length; e++){
      if(array[playerY][e] == "\u001B[31m" + "E" + "\u001B[0m"){
        enemyalive = false;
        array[playerY][e] = " ";
        render();
        break;
      }
      if(playerY == bossY && e == bossX){
        bosshealth--;
        array[playerY][e] = " ";
        render();
        break;
      }
      if(array[playerY][e] != material && array[playerY][e] != "."){
        array[playerY][e] = "*";
        render();
        Thread.sleep(25);
        array[playerY][e] = " ";
        render();
      }
      if(array[playerY][e] == material){
      break;
      }
    }
  }

  static void fightL() throws InterruptedException{
    for(int e = playerX-1; e > 0; e--){
      if(array[playerY][e] == "\u001B[31m" + "E" + "\u001B[0m"){
        enemyalive = false;
        array[playerY][e] = " ";
        render();
        break;
      }
      if(playerY == bossY && e == bossX){
        bosshealth--;
        array[playerY][e] = " ";
        render();
        break;
      }
      if(array[playerY][e] != material && array[playerY][e] != "."){
        array[playerY][e] = "*";
        render();
        Thread.sleep(25);
        array[playerY][e] = " ";
        render();
      }
      if(array[playerY][e] == material){
        break;
      }
    }
  }

  static void fightU() throws InterruptedException{
    for(int e = playerY-1; e > 0; e--){
      if(array[e][playerX] == "\u001B[31m" + "E" + "\u001B[0m"){
        enemyalive = false;
        array[e][playerX] = " ";
        render();
        break;
      }
      if(e == bossY && playerX == bossX){
        bosshealth--;
        array[playerY][e] = " ";
        render();
        break;
      }
      if(array[e][playerX] != material && array[e][playerX] != "."){
        array[e][playerX] = "*";
        render();
        Thread.sleep(25);
        array[e][playerX] = " ";
        render();
      }
      if(array[e][playerX] == material){
        break;
      }
    }
  }

  static void fightD() throws InterruptedException{
    for(int e = playerY+1; e < array.length; e++){
      if(array[e][playerX] == "\u001B[31m" + "E" + "\u001B[0m"){
        enemyalive = false;
        array[e][playerX] = " ";
        render();
        break;
      }
      if(e == bossY && playerX == bossX){
        bosshealth--;
        array[playerY][e] = " ";
        render();
        break;
      }
      if(array[e][playerX] != material && array[e][playerX] != "."){
        array[e][playerX] = "*";
        render();
        Thread.sleep(25);
        array[e][playerX] = " ";
        render();
      }
      if(array[e][playerX] == material){
        break;
      }
    }
  }

  static void dungeonGen() {
    for(int d = 0; d < array.length; d++){
      Arrays.fill(array[d], ".");
    }
    int repeat = 0;
    repeat = (int)(Math.random()*7);
    for(int d = 0; d < repeat +3; d++){
        int min = 0;
        int max = 0;
        int min1 = 0;
        int max1 = 0;
          int int1 = (int) (Math.random()*array.length-1);
        int int2 = (int) (Math.random()*array.length-1);
        int int3 = (int) (Math.random()*array.length-1);
        int int4 = (int) (Math.random()*array.length-1);
        if(int1 < int2){
            min = int1;
            max = int2 +2;
        } else {
            min = int2;
            max = int1 +2;
        }
        if(int3 < int4){
            min1 = int3;
            max1 = int4 +2;
        } else {
            min1 = int4;
            max1 = int3 +2;
        }
        for(int i = 0; i < array.length; i++){
            for(int e = min; e < max; e++ ){
                if(i > min1 && i < max1){
                    array[i][e] = "@";
                    array[i][(max+min) /2] = " ";
                    array[(max1+min1) /2][e] = " ";
                }
            }
            for(int e = min + 1; e < max -1 ; e++ ){
              if(i > min1 + 1 && i < max1 - 1){
                  array[i][e] = " ";
              }
            }
        }
    }
    itemGen();
    playerDungeonGen();
    spawnenemy();
    render();
  }

  static void itemGen(){
    boolean portalgen = false;
    while(portalgen == false){
      for(int i = 0; i < array.length; i++){
        for(int e = 0 + 1; e < array.length -1 ; e++ ){
          if(array[i][e] == " "){
            int key = (int)(Math.random()*200);
            if(key == 0) {
              array[i][e] = "\u001B[35m" + "%" + "\u001B[0m";
            }
            int portal = (int)(Math.random()*200);
            if(portal == 0) {
              portalgen = true;
              array[i][e] = "\u001B[34;1m" + "0" + "\u001B[0m";
            }
            int bossroom = (int)(Math.random()*500);
            if(bossroom == 0) {
              array[i][e] = "\u001B[33m" + "#" + "\u001B[0m";
            }
            int healthUp = (int)(Math.random()*300);
            if(healthUp == 0) {
              array[i][e] = "\u001b[38;5;198m" + "^" + "\u001B[0m";
            }
          }
        }
      }
    }
  }

  static void playerDungeonGen(){
    boolean generated = false;
    int i = 0;
    while(generated == false){
      i++;
      for(int e = 0; e < array.length; e++){
        if(array[i][e] == " "){
            array[i][e] = "\u001B[34m"+ "P" + "\u001B[0m";
            playerX = e;
            playerY = i;
            generated = true;
            break;
        }
      }
    }
  }

  static void bossRoomGen() throws InterruptedException{
    for(int d = 0; d < array.length; d++){
      Arrays.fill(array[d], "@");
    }
    for(int d = 1; d < array.length -1; d++){
      for(int e = 1; e < array.length -1; e++){
        array[d][e] = " ";
      }
    }
    for(int d = 0; d < 10; d++){
        int min = 0;
        int max = 0;
        int min1 = 0;
        int max1 = 0;
          int int1 = (int) (Math.random()*array.length-1);
        int int2 = (int) (Math.random()*array.length-1);
        int int3 = (int) (Math.random()*array.length-1);
        int int4 = (int) (Math.random()*array.length-1);
        if(int1 +3 < int2 -3){
            min = int1 + 3;
            max = int2 -3;
        } else {
            min = int2 + 3;
            max = int1 -3;
        }
        if(int3 +3 < int4 -3){
            min1 = int3 + 3;
            max1 = int4 -3;
        } else {
            min1 = int4 + 3;
            max1 = int3 -3;
        }
        for(int i = 0; i < array.length; i++){
            for(int e = min; e < max; e++ ){
                if(i > min1 && i < max1){
                    array[i][e] = "@";
                    array[i][(max+min) /2] = " ";
                    array[(max1+min1) /2][e] = " ";
                }
              }
            }
          }
      enemyalive = false;
      playerDungeonGen();
      spawnBoss();
      bossBehavior();
  }

  static void spawnBoss(){
    bosshealth = (int)(Math.random()*20);
    bosshealth = bosshealth+5;
    bossX = (int) ((Math.random() * (28 - 2)) + 2);
    bossY = (int) ((Math.random() * (28 - 2)) + 2);
    renderboss();
  }

  static void renderboss(){
    System.out.println(bossX + " " + bossY);
    for(int i = bossY+1; i > bossY-2; i--){
      for(int e = bossX-1; e < bossX+2; e++){
        if(playerY == i && playerX == e){
          health--;
          playerX = (int)(Math.random()*array.length-2);
          playerY = (int)(Math.random()*array.length-2);
          playerX++;
          playerY++;
        }
        array[i][e] = bossmaterial+ "&" + "\u001B[0m";
      }
    }
  array[bossY][bossX] = "\u001B[31m" + "0" + "\u001B[0m";
  }

  static void bossBehavior() throws InterruptedException{
    int attack = (int)(Math.random()*4);
    bossfight = true;
    if(bossX < array.length-2 && playerX > bossX){
      bossX++;
      array[bossY+1][bossX-2] = " ";
      array[bossY][bossX-2] = " ";
      array[bossY-1][bossX-2] = " ";
      if(attack == 0){
      bossAttackR();
      }
    }
    if(bossX > 1 && playerX < bossX){
      bossX--;
      array[bossY+1][bossX+2] = " ";
      array[bossY][bossX+2] = " ";
      array[bossY-1][bossX+2] = " ";
      if(attack == 0){
      bossAttackL();
      }
    }
    if(bossY > 1 && playerY < bossY){
      bossY--;
      array[bossY+2][bossX-1] = " ";
      array[bossY+2][bossX] = " ";
      array[bossY+2][bossX+1] = " ";
      if(attack == 0){
      bossAttackU();
      }
    }
    if(bossY < array.length-2 && playerY > bossY){
      bossY++;
      array[bossY-2][bossX-1] = " ";
      array[bossY-2][bossX] = " ";
      array[bossY-2][bossX+1] = " ";
      if(attack == 0){
      bossAttackD();
      }
    }
    renderboss();
  }

  static void bossAttackR() throws InterruptedException{
    for(int e = bossX+2; e < array.length-1 && e < bossX+15; e++){
      if(playerY == bossY && e == playerX){
        health--;
        array[bossY][e] = " ";
        render();
        break;
      }
      if(array[bossY][e] != material && array[bossY][e] != "."){
        array[bossY][e] = "*";
        render();
        Thread.sleep(25);
        array[bossY][e] = " ";
        render();
      }
      if(array[playerY][e] == material){
        break;
      }
    }
  }

 static void bossAttackL()throws InterruptedException{
  for(int e = bossX-2; e > 0 && e > bossX-15; e--){
    if(playerY == bossY && e == playerX){
      health--;
      array[bossY][e] = " ";
      render();
      break;
    }
    if(array[bossY][e] != material && array[bossY][e] != "."){
      array[bossY][e] = "*";
      render();
      Thread.sleep(25);
      array[bossY][e] = " ";
      render();
    }
    if(array[bossY][e] == material){
      break;
    }
  }
}

  static void bossAttackU() throws InterruptedException{
    for(int e = bossY-2; e > 0 && e > bossY-15; e--){
      if(e == playerY && playerX == bossX){
        health--;
        array[bossY][e] = " ";
        render();
        break;
      }
      if(array[e][bossX] != material && array[e][bossX] != "."){
        array[e][bossX] = "*";
        render();
        Thread.sleep(25);
        array[e][bossX] = " ";
        render();
      }
      if(array[e][bossX] == material){
        break;
      }
    }
  }

  static void bossAttackD() throws InterruptedException{
  for(int e = bossY+2; e < array.length-2 && e < bossY+15; e++){
    if(e == playerY && playerX == bossX){
      health--;
      array[bossY][e] = " ";
      render();
      break;
    }
    if(array[e][bossX] != material && array[e][bossX] != "."){
      array[e][bossX] = "*";
      render();
      Thread.sleep(25);
      array[e][bossX] = " ";
      render();
    }
    if(array[e][bossX] == material){
      break;
      }
    }
  }
}