
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
        test();            // <----------------- UNCOMMENT to test

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
            current = next(players, current);

            
            //When we reach the leader again one round is done
            if(current.equals(leader)) {
                // --- Process -----
                players = allRolled(players,current,leader);
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

    // TODO implement and test methods (one at the time)

    /*  allRolled method that is executed after a round is done.
     *  It removes amount from the loser if they have any amount left,
     *  otherwise removes the loser. It also resets the number of rolls
     *  player = array of Player
     *  current = Player
     */
    Player[] allRolled(Player[] players, Player current, Player leader){
        Player loser = getLoser(players);

        loser.amount--;

        if (loser.getAmount() == 0){
            current = next(players, loser);
            players = removeLoser(players, loser);

        }else{

            current = loser;
        }

        leader = current;
        clearRoundResults(players);
        out.println("Round Done " + loser.name + " lost!");
        players = sortPlayers(players, current);
        statusMsg(players);

        return players;
    }

    Player[] sortPlayers(Player[] players, Player current){
        Player[] newPlayers = new Player[players.length];
        for(int i = 0; i< players.length; i++){
            newPlayers[i] = players[(indexOf(players, current) + i) % players.length];
        }

        return newPlayers;
    }

    void clearRoundResults(Player[] players){
        for (Player player:players) {
            player.setnRolls(0);
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

    Player next(Player[] players, Player player){
        return players[(indexOf(players, player)+1)% players.length];
    }

    /*
     * method to select the next player by index instead of a player object
     * this method is needed in case we remove the loser from the player array
     * before select the next player
     */
    /*Player next(Player[] players, int id) {
        return players[(id+1)%players.length];
    }*/

    Player getLoser(Player[] players){
        Player loser = players[0];
        for (Player player: players){
            if(player.getScore() < loser.getScore()){
                loser = player;
            }
        }
        return (loser);
    }
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

        public void setAmount(int amount) {
            this.amount = amount;
        }

       public int getScore(){
            if ((10*fstDice+ secDice == 21) || (10*fstDice+ secDice == 21)){
                out.print(".___  ___.  __________   ___  __    ______   ______   \n" +
                        "|   \\/   | |   ____\\  \\ /  / |  |  /      | /  __  \\  \n" +
                        "|  \\  /  | |  |__   \\  V  /  |  | |  ,----'|  |  |  | \n" +
                        "|  |\\/|  | |   __|   >   <   |  | |  |     |  |  |  | \n" +
                        "|  |  |  | |  |____ /  .  \\  |  | |  `----.|  `--'  | \n" +
                        "|__|  |__| |_______/__/ \\__\\ |__|  \\______| \\______/  ");

                return (256);
            }
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

        public void setFstDice(int fstDice) {
            this.fstDice = fstDice;
        }



        public void setSecDice(int secDice) {
            this.secDice = secDice;
        }

        public int getnRolls() {
            return nRolls;
        }

        public void setnRolls(int nRolls) {
            this.nRolls = nRolls;
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
