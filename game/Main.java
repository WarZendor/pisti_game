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
                    break;
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

         */

        /*
        Lists that will be used in game:
        - Deck
        - Players hand
        - Cards taken by player
        - Pistis taken by player
        - Everything on top but for opponent
         */
        Scanner sc = new Scanner(System.in);
        boolean playerIsDealer;

        System.out.println("Welcome, do you want to be the dealer?\n1 - Of course.\n2 - I'll pass this time.");
        int request = sc.nextInt();

        // Witchcraft
        playerIsDealer = request == 1;

        if (playerIsDealer) System.out.println("You shuffled the deck.\nYour opponent cut the deck.");
        else System.out.println("Your opponent shuffled the deck.\nYou randomly cut the deck.");

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
        If ♦10 ♣2 are pisti, they are same as other pistis.
            Variables that end with "Last" are to keep track where the list continues. Another solution would be to count
        until reaching -1, this wouldn't require another variable. But I have concluded that
        latter is easier to read and code with.
         */
        int[] playerHand = new int[4];
        int[] opponentHand = new int[4];

        int[] playerCollectedCards = new int[52];;
        int[] opponentCollectedCards = new int[52];;
        int[] playerPistis = new int[52];;
        int[] opponentPistis = new int[52];;
        int[] board = new int[52];;

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

        // Game is played on 6 sets. Hands are given in the beginning of a set.
        for (int set = 0; set < 6; set++) {

            // Giving player their cards.
            for (int i = 0; i < 4; i++) {
                playerHand[i] = deck[deckLast];
                deck[deckLast] = -1;
                deckLast--;
            }

            // Giving computer their cards.
            for (int i = 0; i < 4; i++) {
                opponentHand[i] = deck[deckLast];
                deck[deckLast] = -1;
                deckLast--;
            }

            // Both player plays once every turn.
            for (int turn = 0; turn < 4; turn++) {

                // Prints the cards on board, last to first. Checks if first three are collected.
                for (int i = boardLast - 1; i >= 0; i--) {
                    if ((i == 0 || i == 1 || i == 2) && !firstCardsOut) System.out.print("**" + " ");
                    else System.out.print(valueToSuitAndCard(board[i]) + " ");
                }
                System.out.println("\n");

                // Printing hand
                for (int i = 0; i < 4; i++) {
                    if (playerHand[i] != -1) System.out.println((i + 1) + " - " + valueToSuitAndCard(playerHand[i]));
                    else System.out.println((i + 1) + " - already played");
                }
                // Ask player to play which card.
                boolean continue_ = false;
                int choosenCard = -1;
                while (!continue_) {
                    try {
                        choosenCard = sc.nextInt();
                        // Chosen number shouldn't be below 1, above 4 or shouldn't have been played (is not equal to -1) .
                        if ((choosenCard < 1) || (choosenCard > 4) || (playerHand[choosenCard - 1] == -1)) System.out.println("Enter a valid number.");
                        else {
                            continue_ = true;
                            // chosen card is one bigger than the actual index.
                            choosenCard--;
                        }
                    } catch (Exception InputMismatchException) {
                        System.out.println("Enter a valid number.");
                    }
                }
                board[boardLast] = playerHand[choosenCard];
                boardLast++;
                playerHand[choosenCard] = -1;

                // TODO this code can be shortened, a lot.
                if (boardLast == 2) {
                    if (board[1] % 13 == board[0] % 13) {
                        for (int i = 0; i < 2; i++) {
                            playerPistis[playerPistisLast] = board[i];
                            playerPistisLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    }
                    else if (board[1] % 13 == 10) {
                        for (int i = 0; i < 2; i++) {
                            playerCollectedCards[playerCollectedCardsLast] = board[i];
                            playerCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    }
                }
                else if (boardLast > 2) {
                    if (board[boardLast - 1] % 13 == board[boardLast - 2] % 13) {
                        for (int i = 0; i < boardLast; i++) {
                            playerCollectedCards[playerCollectedCardsLast] = board[i];
                            playerCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = true;
                    }
                    else if (board[boardLast - 1] % 13 == 10) {
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

                // Computer plays, currently randomly selects a card.
                continue_ = false;
                choosenCard = -1;
                Random random = new Random();
                while (!continue_) {
                    try {
                        // TODO Computer should be able to play for itself, currently randomly picks a card.
                        choosenCard = random.nextInt(6);
                        if ((choosenCard < 1) || (choosenCard > 4) || (opponentHand[choosenCard - 1] == -1)) System.out.print("");
                        else {
                            continue_ = true;
                            choosenCard--;
                        }
                    } catch (Exception InputMismatchException) {
                        System.out.println("CP! Enter a valid number.");
                    }
                }

                // PC Chosen card is placed on board.
                board[boardLast] = opponentHand[choosenCard];
                boardLast++;
                opponentHand[choosenCard] = -1;

                if (boardLast == 2) {
                    if (board[1] % 13 == board[0] % 13) {
                        for (int i = 0; i < 2; i++) {
                            opponentPistis[opponentPistisLast] = board[i];
                            opponentPistisLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    }
                    else if (board[1] % 13 == 10) {
                        for (int i = 0; i < 2; i++) {
                            opponentCollectedCards[opponentCollectedCardsLast] = board[i];
                            opponentCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    }
                }
                else if (boardLast > 2) {
                    if (board[boardLast - 1] % 13 == board[boardLast - 2] % 13) {
                        for (int i = 0; i < boardLast; i++) {
                            opponentCollectedCards[opponentCollectedCardsLast] = board[i];
                            opponentCollectedCardsLast++;
                            board[i] = -1;
                        }
                        boardLast = 0;
                        firstCardsOut = true;
                        playerWillHaveRemainingCards = false;
                    }
                    else if (board[boardLast - 1] % 13 == 10) {
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

        System.out.println("\n Thanks for playing!\n\n");
    }
    
    public static void showHighScores() {
        // Requires I/O
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

    public static void printList(int[] list, String res) {
        System.out.print(res + ": ");
        for (int i : list) System.out.print(i + " ");
        System.out.println();
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
        for(int i = 0; i < 52; i++) System.out.print(valueToSuitAndCard(i) + " ");
    }

}
