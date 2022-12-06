import java.util.Random;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean cont = true;
        int request;

        // Main game loop
        // TODO Should I put this in a method?
        while (cont) {
            /*
             * Main menu where user can select a variety of options
             * There are:
             * - Start a game against computer.
             * - See high scores.
             * - Exit.
             *
             * New options might be added in the future. For now these will suffice. */
            System.out.println("PISTI\n\n1 - New Game Against AI\n2 - See High Scores\n3 - Exit\n");
            request = sc.nextInt();
            switch (request) {
                case 1:
                    new_game();
                    break;
                case 2:
                    show_high_scores();
                    break;
                case 3:
                    System.out.println("Exiting");
                    cont = false;
                    break; // TODO Unnecessary?
            }
        }
    }

    public static void new_game() {

    }

    public static void show_high_scores() {

    }


    public static int[] deck_shuffle(int[] deck) {
        // Shuffles the deck.
        // Exchange the values between two random indexes. Do this a million times.
        // Won't be used more than once per game.
        Random rand = new Random();
        int temp, index1, index2, length;
        length = deck.length;
        for(int i = 0; i < 1000000; i++) {
            index1 = rand.nextInt(length);
            index2 = rand.nextInt(length);
            temp = deck[index1];
            deck[index1] = deck[index2];
            deck[index2] = temp;
        }
        return deck;
    }

    public static void deck_shuffle_test() {
        // Used to test deck_shuffle method. Useless at the moment.
        int[] deck = new int[52];
        for (int i = 0; i < deck.length; i++) deck[i] = i;

        for (int i = 0; i < 5; i++) {
            deck = deck_shuffle(deck);
            int total = 0;
            for (int j = 0; j < deck.length; j++) {
                total+=deck[j];
                System.out.print(deck[j] + " ");
            }
            System.out.println(total);
        }
    }
}
