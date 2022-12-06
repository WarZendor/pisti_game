import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean continue_ = true;
        int request;

        // Main game loop
        // TODO Should I put this in a method?
        while (continue_) {
            /*
            Main menu where user can select a variety of options
            There are:
            - Start a game against computer.
            - See high scores.
            - Exit.


            New options might be added in the future. For now these will suffice.
            */
            System.out.println("PISTI\n\n1 - New Game Against AI\n2 - See High Scores\n3 - Exit\n");
            request = sc.nextInt();
            switch (request) {
                case 1:
                    newGame();
                    break;
                case 2:
                    showHighScores();
                    break;
                case 3:
                    System.out.println("Exiting");
                    continue_ = false;
                    break; // TODO Unnecessary?
            }
        }
    }

    public static void newGame() {
        /*
        How to game should run?
        There are certain variables that can be calculated beforehand. Such as total turns.
        Cards can be tied to different values
        Order of suits: ♠♥♣♦
        Order of numbers:

        ♠ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        ♥ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        ♣ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        ♦ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K

         0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12
        13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25
        26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38
        39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51

        This made me realize I should make a method that return card with suit and number from a value

         */
    }

    public static void showHighScores() {
        // Requires I/O

    }


    public static int[] deckShuffle(int[] deck) {
        // Shuffles the deck.
        // Exchange the values between two random indexes. Do this a million times.
        // Won't be used more than once per game.
        Random rand = new Random();
        int temp, index1, index2, length;
        length = deck.length; // Improves performance, I guess.
        for(int i = 0; i < 1000000; i++) {
            index1 = rand.nextInt(length);
            index2 = rand.nextInt(length);
            temp = deck[index1];
            deck[index1] = deck[index2];
            deck[index2] = temp;
        }
        return deck;
    }

    public static String valueToCard(int i) {
        // Returns suit and number as a string for easier reading.
        String suit = "Error"; // Refuses to compile without this initialization.
        switch (i / 13) {
            case 0:
                suit = "♠";
                break;
            case 1:
                suit = "♥";
                break;
            case 2:
                suit = "♣";
                break;
            case 3:
                suit = "♦";
                break;
        }

        String card = "Error"; // Refuses to compile without this initialization.
        switch (i % 13) {
            case 0:
                card = "A";
                break;
            case 1:
                card = "2";
                break;
            case 2:
                card = "3";
                break;
            case 3:
                card = "4";
                break;
            case 4:
                card = "5";
                break;
            case 5:
                card = "6";
                break;
            case 6:
                card = "7";
                break;
            case 7:
                card = "8";
                break;
            case 8:
                card = "9";
                break;
            case 9:
                card = "10";
                break;
            case 10:
                card = "J";
                break;
            case 11:
                card = "Q";
                break;
            case 12:
                card = "K";
                break;
        }
        return suit + card;
    }

    public static void deckShuffleTest() {
        // Used to test deckShuffle method. Useless at the moment.
        int[] deck = new int[52];
        for (int i = 0; i < deck.length; i++) deck[i] = i;

        for (int i = 0; i < 5; i++) {
            deck = deckShuffle(deck);
            int total = 0;
            for (int j = 0; j < deck.length; j++) {
                total+=deck[j];
                System.out.print(deck[j] + " ");
            }
            System.out.println(total);
        }
    }

    public static void valueToCardTest() {
        // Used to test valueToCard method. Useless at the moment.
        for(int i = 0; i < 52; i++) System.out.print(valueToCard(i) + " ");
    }
}
