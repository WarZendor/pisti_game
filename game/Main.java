import java.util.Random;
public class Main {
    public static void main(String[] args) {

    }

    public static int[] deck_shuffle(int[] deck) {
        // Shuffles the deck.
        // Exchange the values between two random indexes. Do this a million times.
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
        // Used to test deck_shuffle method.
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
