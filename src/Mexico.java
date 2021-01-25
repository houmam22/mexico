
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.*;

/*
 *  The Mexico dice game
 *  See https://en.wikipedia.org/wiki/Mexico_(game)
 *
 */
public class Mexico {

    public static void main(String[] args) {
        new Mexico().program();
    }

    final Random rand = new Random();
    final Scanner sc = new Scanner(in);
    final int maxRolls = 3;      // No player may exceed this
    int maxRollsRound = maxRolls;
    final int startAmount = 3;   // Money for a player. Select any
    final int mexico = 1000;     // A value greater than any other


    void program() {
        //test();            // <----------------- UNCOMMENT to test

        int pot = 0;         // What the winner will get
        Player[] players;    // The players (array of Player objects)
        Player current;      // Current player for round
        Player leader;       // Player starting the round

        players = getPlayers();
        current = getRandomPlayer(players);
        leader = current;

        out.println("Mexico Game Started");
        statusMsg(players);

        while (players.length > 1) {   // Game over when only one player left

            //Every player should roll at least once
            out.println("Next to roll is " + current.name);
            current.rollDice();

    
            //A player may reroll as long as they do not exceed maxRolls
            while(current.nRolls < (maxRollsRound)) {

            // ----- In ----------e
                String cmd = getPlayerChoice(current);
                if ("r".equals(cmd)) {
                    //Reroll, and increment the players nRolls

                        // --- Process ------
                    current.rollDice();

                } else if ("n".equals(cmd)) {
                    //Nobody may roll more times than the leader
                    if(current.equals(leader)) {
                        maxRollsRound = current.nRolls;
                    }
                    break;
                } else {
                    out.println("Invalid input, try again");
                }
            }
            //Go to the next player
            current = next(players, current);

            
            //When we reach the leader again one round is done
            if(current.equals(leader)) {
                // --- Process -----
                //Execute the allRolled method, which takes care of updating
                //the array of players according to the game rules
                players = allRolled(players,current);
                //Since there always will be one (and only one) loser the pot should be
                //incremented once at the end of every round
                pot++;
                current = players[0];
                leader = current;



                // ----- Out --------------------
                //out.println("Round done ... lost!");
                //out.println("Next to roll is " + current.name);

                //statusMsg(players);
            }



        }
        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }


    // ---- Game logic methods --------------


    /*  allRolled method that is executed after a round is done.
     *  It removes amount from the loser if they have any amount left,
     *  otherwise removes the loser. It also resets the number of rolls
     *  player = array of Player
     *  current = Player
     *  Returns: array of players such that the loser has been removed if 
     *  they don't have any money left and the next player is the first
     *  element of the array
     */
    Player[] allRolled(Player[] players, Player current){
        Player loser = getLoser(players);

        loser.amount--;

        if (loser.getAmount() == 0){
            current = next(players, loser);
            players = removeLoser(players, loser);

        }else{

            current = loser;
        }

        clearRoundResults(players); //Reset player rolls
        out.println("Round Done " + loser.name + " lost!");
        //The next player should be the first element of the returned array.
        //This is done because it is more difficult to return multiple values of 
        //different tyes in java than in other languages, and we need to know the
        //(updated) value of current outside of this method.
        players = sortPlayers(players, current); 
        statusMsg(players);

        return players;
    }

    /* sortPlayers does not actually sort the array of players, but rotates it such 
     * that the passed player "current" will be the first element of the returned 
     * array, and the order of players otherwise is preserved.
     * players = array of Player
     * current = Player in players that should be placed at the start, needs to be an element
     * of players
     * returns: new Player array
     */
    Player[] sortPlayers(Player[] players, Player current){
        Player[] newPlayers = new Player[players.length];
        for(int i = 0; i< players.length; i++){
            newPlayers[i] = players[(indexOf(players, current) + i) % players.length];
        }

        return newPlayers;
    }

    /*Sets nRolls to 0 for all players and resets the max amount of rolls to its predefined
     * value.
     */
    void clearRoundResults(Player[] players){
        for (Player player:players) {
            player.nRolls=0;
        }
        maxRollsRound = maxRolls;
    }


    int indexOf(Player[] players, Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    /*Returns the next player
     */
    Player next(Player[] players, Player player){
        return players[(indexOf(players, player)+1)% players.length];
    }


    /* Given an array of players returns the player with the lowest score.
     */
    Player getLoser(Player[] players){
        Player loser = players[0];
        for (Player player: players){
            if(player.getScore() < loser.getScore()){
                loser = player;
            }
        }
        return (loser);
    }
    /*Creates and returns an array of players containing all of the
     * players in the original array in the same order, except for the
     * loser.
     */
    Player[] removeLoser(Player[] players, Player loser){
        Player[] newPlayers = new Player[players.length - 1];

        for (int i = 0, j = 0; i < players.length; i++) {
            if(!(players[i].equals(loser))){
                newPlayers[j++] = players[i];
            }
        }
        return newPlayers;
    }

    Player getRandomPlayer(Player[] players) {
        return players[rand.nextInt(players.length)];
    }


    // ---------- IO methods (nothing to do here) -----------------------

    Player[] getPlayers() {
        // Ugly for now. If using a constructor this may
        // be cleaned up.
        Player[] players = new Player[3];
        Player p1 = new Player("Olle");
        Player p2 = new Player("Fia");
        Player p3 = new Player("Lisa");
        players[0] = p1;
        players[1] = p2;
        players[2] = p3;
        return players;
    }

    void statusMsg(Player[] players) {
        out.print("Status: ");
        for (int i = 0; i < players.length; i++) {
            out.print(players[i].name + " " + players[i].amount + " ");
        }
        out.println();
    }

    void roundMsg(Player current) {
        out.println(current.name + " got " +
                current.fstDice + " and " + current.secDice);
    }

    String getPlayerChoice(Player player) {
        out.print("Player is " + player.name + " > ");
        return sc.nextLine();
    }

    // Possibly useful utility during development
    String toString(Player p){
        return p.name + ", " + p.amount + ", " + p.fstDice + ", "
                + p.secDice + ", " + p.nRolls;
    }

    // Class for a player
    class Player {
        String name;
        int amount;   // Start amount (money)
        int fstDice;  // Result of first dice
        int secDice;  // Result of second dice
        int nRolls;   // Current number of rolls

        public Player(){
            new Player("unNamed");
        }

        public Player(String name) {
            this.name = name;
            this.amount = startAmount;
        }


        public int getAmount() {
            return amount;
        }

        public void rollDice() {
            this.fstDice = rand.nextInt(6) + 1;
            this.secDice = rand.nextInt(6) + 1;
            roundMsg(this);
            this.nRolls++;
        }


        public int getScore(){
            //Calculate most of the score in a helper function, if the result is 21
            //the player gets a Mexico, special case. Otherwise, just return the result
            int score = this.getScoreAux();
            if (score == 21) {
                out.print(".___  ___.  __________   ___  __    ______   ______   \n" +
                        "|   \\/   | |   ____\\  \\ /  / |  |  /      | /  __  \\  \n" +
                        "|  \\  /  | |  |__   \\  V  /  |  | |  ,----'|  |  |  | \n" +
                        "|  |\\/|  | |   __|   >   <   |  | |  |     |  |  |  | \n" +
                        "|  |  |  | |  |____ /  .  \\  |  | |  `----.|  `--'  | \n" +
                        "|__|  |__| |_______/__/ \\__\\ |__|  \\______| \\______/  ");

                return (mexico);
            } else {
                return score;
            }
       }

       private int getScoreAux() {
           //The players score is 10*the die with the highest value + the die with the lowest
           //value, unless the dice have the same value.
            switch(Integer.compare(fstDice, secDice)) {
                case (-1):
                    return (10 * secDice + fstDice);
                case (0):
                    return (70+fstDice);
                case (1):
                    return (10 * fstDice + secDice);
            }
            return (0);
       }
    }

    /**************************************************
     *  Testing
     *
     *  Test are logical expressions that should
     *  evaluate to true (and then be written out)
     *  No testing of IO methods
     *  Uncomment in program() to run test (only)
     ***************************************************/
    void test() {
        // A few hard coded player to use for test
        // NOTE: Possible to debug tests from here, very efficient!
        Player[] ps = {new Player(), new Player(), new Player()};
        ps[0].fstDice = 2;
        ps[0].secDice = 6;
        ps[1].fstDice = 6;
        ps[1].secDice = 5;
        ps[2].fstDice = 1;
        ps[2].secDice = 1;

        out.println(ps[0].getScore() == 62);
        out.println(ps[1].getScore() == 65);
        out.println(next(ps, ps[0]) == ps[1]);
        out.println(getLoser(ps) == ps[0]);

        exit(0);
    }


}
