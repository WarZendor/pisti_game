import java.io.IOException;
import java.io.File;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.util.Formatter;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        checkForHighScoresTxt();
        Scanner sc = new Scanner(System.in);
        boolean continue_ = true;
        int request;
        while (continue_) {
            System.out.println("\nPISTI\n\n1 - New Game\n2 - See High Scores\n3 - Exit\n");
            request = intInput(1, 3, sc);
            switch (request) {
                case 1:
                    newGame(sc);
                    break;
                case 2:
                    showHighScores();
                    sc.nextLine();
                    break;
                case 3:
                    System.out.println("Exiting");
                    continue_ = false;
                    break;
            }
        }
    }


    public static void newGame(Scanner sc) {
        /*
        Cards are tied to different values
        Order of suits: ♠♥♣♦
        Order of numbers: A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K

        ♠ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        ♥ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        ♣ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
        ♦ A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K

         0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12
        13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25
        26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38
        39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51

        value % 13 gives number
        value / 13 gives suit
         */
        boolean playerIsDealer;
        System.out.println("Welcome, do you want to be the dealer?\n\n1 - Of course.\n2 - I'll pass this time.");
        int request = intInput( 1, 2, sc);
        playerIsDealer = request == 1;

        if (playerIsDealer) {
            System.out.println("You shuffled the deck. Press Enter to continue.");
            sc.nextLine();
            System.out.println("Your opponent cut the deck. Press Enter to continue.");
            sc.nextLine();
        }
        else {
            System.out.println("Your opponent shuffled the deck. Press Enter to continue");
            sc.nextLine();
            System.out.println("You randomly cut the deck. Press Enter to continue");
            sc.nextLine();
        }

        // Creating the deck.
        int[] deck = new int[52];
        for (int i = 0; i < deck.length; i++) deck[i] = i;

        // Shuffling the deck.
        deck = deckShuffle(deck);

        // Cutting the deck.
        deck = deckCut(deck);

        /*
        ♦10 is 3 points
        ♣2 is 2 points
        If ♦10 ♣2 are pisti they are same as other pistis thus has no extra points.
            Variables that end with "Last" are to keep track where from the list will continue.
            Another solution would be to count until reaching -1, this wouldn't require another variable.
            But through experience I have concluded that former is easier to read and code with.
         */

        int[] playerHand = new int[4];
        int[] opponentHand = new int[4];

        int[] playerCollectedCards = new int[52];
        int[] opponentCollectedCards = new int[52];
        int[] playerPistis = new int[52];
        int[] opponentPistis = new int[52];
        int[] board = new int[52];

        for (int i = 0; i < 52; i++) playerCollectedCards[i] = -1;
        for (int i = 0; i < 52; i++) opponentCollectedCards[i] = -1;
        for (int i = 0; i < 52; i++) playerPistis[i] = -1;
        for (int i = 0; i < 52; i++) opponentPistis[i] = -1;
        for (int i = 0; i < 52; i++) board[i] = -1;

        int playerCollectedCardsLast = 0;
        int opponentCollectedCardsLast = 0;
        int playerPistisLast = 0;
        int opponentPistisLast = 0;
        int boardLast = 0;
        int deckLast = 51;

        // First 4 cards are placed on board.
        for (int i = 0; i < 4; i++) {
            board[i] = deck[deckLast];
            deck[deckLast] = -1;
            deckLast--;
            boardLast++;
        }

        // This variable is to hide first 3 cards until cards are taken from board once.
        boolean firstCardsOut = false;

        // This variable is to decide which player will have the remaining cards once the game is over.
        boolean playerWillHaveRemainingCards = true;

        // Random is needed for computer to select a card, if there is no good action.
        Random random = new Random();

        // Stores the chosen for that player. Used in multiple occurrences, so it is better for it to be here.
        int chosenCard;

        // Game is played on 6 sets. Hands are given in the beginning of a set.
        for (int set = 0; set < 6; set++) {

            if (playerIsDealer) {
                // Giving player and opponent their hands.
                for (int i = 0; i < 4; i++) {
                    opponentHand[i] = deck[deckLast];
                    deck[deckLast] = -1;
                    deckLast--;
                    playerHand[i] = deck[deckLast];
                    deck[deckLast] = -1;
                    deckLast--;
                }

                // Both player plays once every turn.
                for (int turn = 0; turn < 4; turn++) {
                    /*------------------------------------------------------------------------------------------------*/
                    // Player plays

                    // Prints the cards on board, top to bottom. Checks if first three are collected.
                    System.out.println("----------------\nBoard:");
                    for (int i = boardLast - 1; i >= 0; i--) {
                        if ((i == 0 || i == 1 || i == 2) && !firstCardsOut) System.out.print("**" + " ");
                        else System.out.print(valueToSuitAndCard(board[i]) + " ");
                    }
                    System.out.println("\n");

                    // Printing hand
                    for (int i = 0; i < 4; i++) {
                        if (playerHand[i] != -1) System.out.println((i + 1) + " - " + valueToSuitAndCard(playerHand[i]));
                        else System.out.println((i + 1) + " - already played.");
                    }
                    System.out.println("\n");

                    // Ask player to play which card.
                    while (true) {
                        chosenCard = intInput(1, 4, sc);
                        if (playerHand[chosenCard - 1] == -1) System.out.println("\nThat card is already played!");
                        else break;
                    }

                    chosenCard--;
                    board[boardLast] = playerHand[chosenCard];
                    boardLast++;
                    playerHand[chosenCard] = -1;

                    if (boardLast == 2 && board[1] % 13 == board[0] % 13) {
                        for (int i = 0; i < boardLast; i++) {
                            playerPistis[playerPistisLast] = board[i];
                            playerPistisLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    } else if ((boardLast >= 2) && ((board[boardLast - 1] % 13 == board[boardLast - 2] % 13) || (board[boardLast - 1] % 13 == 10))) {
                        for (int i = 0; i < boardLast; i++) {
                            playerCollectedCards[playerCollectedCardsLast] = board[i];
                            playerCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    }
                    /*------------------------------------------------------------------------------------------------*/
                    // Computer plays

                    // Computer checks its hand. If there is a card that matches with top card, plays it
                    chosenCard = -1;
                    if (boardLast > 0) {
                        for (int i = 0; i < 4; i++) {
                            if (opponentHand[i] != -1 && (board[boardLast - 1] % 13 == opponentHand[i] % 13)) {
                                chosenCard = i + 1;
                                break;
                            }
                        }
                    }

                    // Is it worth to play jake?
                    boolean worth = false;

                    // If no cards are matching:
                    if (chosenCard == -1) {
                        // Check each card in hand for jake.
                        for (int i = 0; i < 4; i++) {
                            if (opponentHand[i] != -1 && (opponentHand[i] % 13 == 10)) {
                                // This means there is jake at hand.
                                // Checking the board for ♦10 or ♣2
                                if (firstCardsOut) {
                                    for (int j = boardLast - 1; j >= 0; j--) {
                                        if (valueToSuitAndCard(board[j]).equals("♦10")  || valueToSuitAndCard(board[j]).equals("♣2")) {
                                            // It is worth to play jake.
                                            worth = true;
                                            break;
                                        }
                                    }
                                } else {
                                    for (int j = boardLast - 1; j >= 3; j--) {
                                        if (valueToSuitAndCard(board[j]).equals("♦10")  || valueToSuitAndCard(board[j]).equals("♣2")) {
                                            // It is worth to play jake.
                                            worth = true;
                                            break;
                                        }
                                    }
                                }
                                // Also plays jake if there is at least 4 cards on board.
                                if ((boardLast >= 4) || worth) chosenCard = i + 1;
                                break;
                            }
                        }
                    }

                    // If still no cards are good, plays a random card.
                    if (chosenCard == -1) {
                        do {
                            chosenCard = random.nextInt(1, 5);
                        } while (opponentHand[chosenCard - 1] == -1);
                    }
                    chosenCard--;
                    board[boardLast] = opponentHand[chosenCard];
                    boardLast++;
                    opponentHand[chosenCard] = -1;

                    if (boardLast == 2 && board[1] % 13 == board[0] % 13) {
                        for (int i = 0; i < boardLast; i++) {
                            opponentPistis[opponentPistisLast] = board[i];
                            opponentPistisLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    } else if ((boardLast >= 2) && ((board[boardLast - 1] % 13 == board[boardLast - 2] % 13) || (board[boardLast - 1] % 13 == 10))) {
                        for (int i = 0; i < boardLast; i++) {
                            opponentCollectedCards[opponentCollectedCardsLast] = board[i];
                            opponentCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    }
                }
            }
            else {
                // Same code as above except they switched turns

                // Giving player and opponent their hands.
                for (int i = 0; i < 4; i++) {
                    playerHand[i] = deck[deckLast];
                    deck[deckLast] = -1;
                    deckLast--;
                    opponentHand[i] = deck[deckLast];
                    deck[deckLast] = -1;
                    deckLast--;
                }

                // Both player plays once every turn.
                for (int turn = 0; turn < 4; turn++) {
                    /*------------------------------------------------------------------------------------------------*/
                    // Computer plays

                    // Computer checks its hand. If there is a card that matches with top card, plays it
                    chosenCard = -1;
                    if (boardLast > 0) {
                        for (int i = 0; i < 4; i++) {
                            if (opponentHand[i] != -1 && (board[boardLast - 1] % 13 == opponentHand[i] % 13)) {
                                chosenCard = i + 1;
                                break;
                            }
                        }
                    }

                    // Is it worth to play jake?
                    boolean worth = false;

                    // If no cards are matching:
                    if (chosenCard == -1) {
                        // Check each card in hand for jake.
                        for (int i = 0; i < 4; i++) {
                            if (opponentHand[i] != -1 && (opponentHand[i] % 13 == 10)) {
                                // This means there is jake at hand.
                                // Checking the board for ♦10 or ♣2
                                if (firstCardsOut) {
                                    for (int j = boardLast - 1; j >= 0; j--) {
                                        if (valueToSuitAndCard(board[j]).equals("♦10")  || valueToSuitAndCard(board[j]).equals("♣2")) {
                                            // It is worth to play jake.
                                            worth = true;
                                            break;
                                        }
                                    }
                                } else {
                                    for (int j = boardLast - 1; j >= 3; j--) {
                                        if (valueToSuitAndCard(board[j]).equals("♦10")  || valueToSuitAndCard(board[j]).equals("♣2")) {
                                            // It is worth to play jake.
                                            worth = true;
                                            break;
                                        }
                                    }
                                }
                                // Also plays jake if there is at least 4 cards on board.
                                if ((boardLast >= 4) || worth) chosenCard = i + 1;
                                break;
                            }
                        }
                    }

                    // If still no cards are good, plays a random card.
                    if (chosenCard == -1) {
                        do {
                            chosenCard = random.nextInt(1, 5);
                        } while (opponentHand[chosenCard - 1] == -1);
                    }
                    chosenCard--;
                    board[boardLast] = opponentHand[chosenCard];
                    boardLast++;
                    opponentHand[chosenCard] = -1;

                    if (boardLast == 2 && board[1] % 13 == board[0] % 13) {
                        for (int i = 0; i < boardLast; i++) {
                            opponentPistis[opponentPistisLast] = board[i];
                            opponentPistisLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    } else if ((boardLast >= 2) && ((board[boardLast - 1] % 13 == board[boardLast - 2] % 13) || (board[boardLast - 1] % 13 == 10))) {
                        for (int i = 0; i < boardLast; i++) {
                            opponentCollectedCards[opponentCollectedCardsLast] = board[i];
                            opponentCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    }

                    /*----------------------------------------------------------------------------------------------------*/
                    // Player plays

                    // Prints the cards on board, top to bottom. Checks if first three are collected.
                    System.out.println("----------------\nBoard:");
                    for (int i = boardLast - 1; i >= 0; i--) {
                        if ((i == 0 || i == 1 || i == 2) && !firstCardsOut) System.out.print("**" + " ");
                        else System.out.print(valueToSuitAndCard(board[i]) + " ");
                    }
                    System.out.println("\n");

                    // Printing hand
                    for (int i = 0; i < 4; i++) {
                        if (playerHand[i] != -1) System.out.println((i + 1) + " - " + valueToSuitAndCard(playerHand[i]));
                        else System.out.println((i + 1) + " - already played.");
                    }
                    System.out.println("\n");

                    // Ask player to play which card.
                    while (true) {
                        chosenCard = intInput(1, 4, sc);
                        if (playerHand[chosenCard - 1] == -1) System.out.println("\nThat card is already played!");
                        else break;
                    }

                    chosenCard--;
                    board[boardLast] = playerHand[chosenCard];
                    boardLast++;
                    playerHand[chosenCard] = -1;

                    if (boardLast == 2 && board[1] % 13 == board[0] % 13) {
                        for (int i = 0; i < boardLast; i++) {
                            playerPistis[playerPistisLast] = board[i];
                            playerPistisLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    } else if ((boardLast >= 2) && ((board[boardLast - 1] % 13 == board[boardLast - 2] % 13) || (board[boardLast - 1] % 13 == 10))) {
                        for (int i = 0; i < boardLast; i++) {
                            playerCollectedCards[playerCollectedCardsLast] = board[i];
                            playerCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    }
                }
            }
        }

        // Remaining cards go to whoever got cards last.
        if (playerWillHaveRemainingCards) {
            for (int i = 0; i < boardLast; i++) {
                playerCollectedCards[playerCollectedCardsLast] = board[i];
                playerCollectedCardsLast++;
                board[i] = -1;
            }
        } else {
            for (int i = 0; i < boardLast; i++) {
                opponentCollectedCards[opponentCollectedCardsLast] = board[i];
                opponentCollectedCardsLast++;
                board[i] = -1;
            }
        }

        // Calculating scores
        int playerScore = 0;
        int opponentScore = 0;

        playerScore+=playerPistisLast * 5;
        opponentScore+=opponentPistisLast * 5;

        for (int card : playerCollectedCards) {
            if (valueToSuitAndCard(card).equals("♦10")) playerScore+=3;
            else if (valueToSuitAndCard(card).equals("♣2")) playerScore+=2;
            else if (card != -1) playerScore+=1;
        }
        for (int card : opponentCollectedCards) {
            if (valueToSuitAndCard(card).equals("♦10")) opponentScore+=3;
            else if (valueToSuitAndCard(card).equals("♣2")) opponentScore+=2;
            else if (card != -1) opponentScore+=1;
        }

        // Add 3 points whoever collected more cards.
        if (playerPistisLast + playerCollectedCardsLast > opponentPistisLast + opponentCollectedCardsLast) playerScore+=3;
        else if (playerPistisLast + playerCollectedCardsLast < opponentPistisLast + opponentCollectedCardsLast) opponentScore+=3;

        System.out.println("Your score: " + playerScore);
        System.out.println("Opponent score: " + opponentScore);
        System.out.println();
        if (playerScore > opponentScore) System.out.println("You win!");
        else if (playerScore < opponentScore) System.out.println("You lost!");
        else System.out.println("How unusual, it is a draw!");

        Scanner reader = null;
        String[] line;
        String[] names = new String[10];
        int[] scores = new int[10];
        int place = 0;
        try {
            reader = new Scanner((Paths.get("highScores.txt")));
            while(reader.hasNextLine()) {
                line = reader.nextLine().split(",");
                names[place]  = line[0];
                scores[place] = Integer.parseInt(line[1]);
                place++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }

        for (int i = 0; i < 10; i++) {
            if (playerScore > scores[i]) {
                for (int j = 8; j > i - 1; j--) {
                    scores[j + 1] = scores[j];
                    names[j + 1] = names[j];
                }
                switch (i) {
                    case 0:
                        System.out.println("Congratulations, you have reached 1st spot on leaderboard!");
                        break;
                    case 1:
                        System.out.println("Congratulations, you have reached 2nd spot on leaderboard!");
                        break;
                    case 2:
                        System.out.println("Congratulations, you have reached 3rd spot on leaderboard!");
                        break;
                    default:
                        System.out.println("Congratulations, you have reached " + (i + 1) + "th spot on leaderboard!");
                }
                System.out.println("Please enter your name:");
                scores[i] = playerScore;
                String playerName;
                do {
                    playerName = sc.nextLine();
                    if (playerName.contains(",")) System.out.println("You can't use \",\" in your name.\nPlease enter your name:");
                    if (playerName.isBlank()) System.out.println("Your name can't be blank.\nPlease enter your name:");
                } while (playerName.contains(",") || playerName.isBlank());

                names[i] = sc.nextLine();
                Formatter f = null;
                try {
                    f = new Formatter("highScores.txt");
                    String write =
                            names[0] + "," + scores[0] + "\n" +
                            names[1] + "," + scores[1] + "\n" +
                            names[2] + "," + scores[2] + "\n" +
                            names[3] + "," + scores[3] + "\n" +
                            names[4] + "," + scores[4] + "\n" +
                            names[5] + "," + scores[5] + "\n" +
                            names[6] + "," + scores[6] + "\n" +
                            names[7] + "," + scores[7] + "\n" +
                            names[8] + "," + scores[8] + "\n" +
                            names[9] + "," + scores[9] + "\n";
                            f.format(write);
                } catch (Exception e) {
                    System.err.println("Something went wrong");
                } finally {
                    if (f != null) f.close();
                }
                showHighScores();
                break;
            }
        }

        System.out.println("\n Thanks for playing!\n\n");
    }

    public static void showHighScores() {
        Scanner reader = null;
        String[] line;
        try {
            reader = new Scanner(Paths.get("highScores.txt"));
            while(reader.hasNextLine()) {
                line = reader.nextLine().split(",");
                if (!(line[1].equals("-1"))) System.out.println(line[0] + " " + line[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) reader.close();
        }
    }

    public static int[] deckCut(int[] deck) {
        Random rand = new Random();
        int random = rand.nextInt(2, 50);
        int[] cutDeck = new int[deck.length];
        for (int i = 0; i < random; i++) cutDeck[i] = deck[deck.length - random + i];
        for (int i = random; i < deck.length; i++) cutDeck[i] = deck[i - random];
        return cutDeck;
    }

    public static int[] deckShuffle(int[] deck) {
        Random rand = new Random();
        int temp, i1, i2;
        for(int i = 0; i < 1000000; i++) {
            i1 = rand.nextInt(deck.length);
            i2 = rand.nextInt(deck.length);
            temp = deck[i1];
            deck[i1] = deck[i2];
            deck[i2] = temp;
        }
        return deck;
    }

    public static String valueToSuitAndCard(int i) {
        String suit;
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
            default:
                suit = "Error";
        }

        String card;
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
            default:
                 card = "Error";
        }
        return suit + card;
    }

    public static int intInput(int beginning, int end, Scanner sc) {
        int returnValue;
        while (true) {
            try {
                returnValue = Integer.parseInt(sc.nextLine());
                if ((returnValue < beginning) || (returnValue > end)) System.out.println("Enter a valid number from " + beginning + " to " + end + ".");
                else return returnValue;
            } catch (InputMismatchException e) {
                System.out.println("Enter a valid number from " + beginning + " to " + end + ".");
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid number from " + beginning + " to " + end + ".");
            }
        }
    }

    public static void checkForHighScoresTxt() {
        File myObj = null;
        try {
            myObj = new File("highScores.txt");
            if (myObj.createNewFile()) {
                Formatter f = null;
                try {
                    f = new Formatter("highScores.txt");
                    String write = "ABIGAIL,100\n" +
                            "ELLIOT,90\n" +
                            "ALEX,80\n" +
                            "LEWIS,70\n" +
                            "PAM,60\n" +
                            "SEBASTIAN,50\n" +
                            "DEMETRIUS,40\n" +
                            "WIZARD,30\n" +
                            "GEORGE,20\n" +
                            "PIERRE,10\n";
                    f.format(write);
                } catch (Exception e) {
                    System.err.println("Something went wrong");
                } finally {
                    if (f != null) f.close();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
