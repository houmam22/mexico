
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

       // while (players.length > 1) {   // Game over when only one player left

            // ----- In ----------
            String cmd = getPlayerChoice(current);
            if ("r".equals(cmd)) {

                    // --- Process ------


                    // ---- Out --------
                    roundMsg(current);

            } else if ("n".equals(cmd)) {
                 // Process
            } else {
                out.println("?");
            }

            //if ( round finished) {
                // --- Process -----

                // ----- Out --------------------
                out.println("Round done ... lost!");
                out.println("Next to roll is " + current.name);

                statusMsg(players);
            //}
        //}
        out.println("Game Over, winner is " + players[0].name + ". Will get " + pot + " from pot");
    }


    // ---- Game logic methods --------------

    // TODO implement and test methods (one at the time)


    int indexOf(Player[] players, Player player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == player) {
                return i;
            }
        }
        return -1;
    }

    Player getLoser(Player[] players){
        Player loser = players[0];
        for (Player player: players){
            if(player.getScore() < loser.getScore()){
                loser = player;
            }
        }
        return (loser);
    }
    Player[] removeLoser(Player loser, Player[] players){
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

        public void setAmount(int amount) {
            this.amount = amount;
        }

       public int getScore(){
            if ((10*fstDice+ secDice == 21) || (10*fstDice+ secDice == 21)){
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

        //out.println(getScore(ps[0]) == 62);
        //out.println(getScore(ps[1]) == 65);
        //out.println(next(ps, ps[0]) == ps[1]);
        //out.println(getLoser(ps) == ps[0]);

        exit(0);
    }


}