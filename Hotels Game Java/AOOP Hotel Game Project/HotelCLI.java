import java.util.Scanner;
import java.util.Random;
import HotelGameModel.HotelGameModel;


public class HotelCLI {
    private static HotelGameModel game;
    private static Scanner scanner;

    public static void main(String[] args) {
        // Initialize the game model and the scanner
        game = new HotelGameModel();
        scanner = new Scanner(System.in);
        Random random = new Random();

        // Game loop
        while (!game.gameOver()) {
            // Determine whose turn it is
            int playerTurn = game.getPlayerTurn();

            // Roll the dice and find out what hotel the player landed on
            int diceRoll = random.nextInt(12) + 1; 
            int landedHotel = diceRoll;

            // Inform the player of their turn, their roll, and the hotel they landed on
            System.out.println("Player " + playerTurn + "'s turn");
            System.out.println("You rolled a " + diceRoll);
            System.out.println("You landed on " + game.getHotelName(landedHotel));

            // Handle different scenarios based on the ownership of the landed hotel
            if (game.getHotelOwners()[landedHotel] == 0) {
                handleUnownedHotel(landedHotel, playerTurn);
            } else if (game.getHotelOwners()[landedHotel] == playerTurn) {
                handleOwnedHotel(landedHotel, playerTurn);
            } else {
                // If the hotel is owned by the other player, calculate the fee and deduct it from the current player's money
                int fee = game.calculateFee(landedHotel);
                System.out.println("This hotel is owned by Player " + game.getHotelOwners()[landedHotel] + ". You owe a fee of $" + fee);
                game.payFee(landedHotel, playerTurn, fee);
            }

            // Print out the current player's remaining money and switch turns
            System.out.println("Player " + playerTurn + " now has $" + (playerTurn == 1 ? game.getPlayer1Money() : game.getPlayer2Money()));
            game.switchTurn();
        }

        // When the game is over, print out who the winner is
        System.out.println("Game Over! Player " + game.getWinner() + " won the game.");
    }

    // Handle the scenario where the player landed on a hotel that is unowned
    private static void handleUnownedHotel(int hotelIndex, int playerTurn) {
        System.out.println("Do you want to buy " + game.getHotelName(hotelIndex) + " for $" + game.getHotelPrices()[hotelIndex] + "? (yes/no)");
        String response = scanner.nextLine().toLowerCase();

        if (response.equals("yes")) {
            boolean bought = game.buyHotel(hotelIndex, playerTurn);

            if (bought) {
                System.out.println("You bought " + game.getHotelName(hotelIndex));
                handleOwnedHotel(hotelIndex, playerTurn);
                System.out.println("Player " + playerTurn + " now has $" + (playerTurn == 1 ? game.getPlayer1Money() : game.getPlayer2Money()));
            } else {
                System.out.println("You don't have enough money to buy this hotel.");
            }
        }
    }

    // Handle the scenario where the player landed on a hotel that they own
    private static void handleOwnedHotel(int hotelIndex, int playerTurn) {
        // If the player owns the hotel and it is not yet fully upgraded, offer them the option to upgrade it
        if (game.getHotelOwners()[hotelIndex] == playerTurn && game.getHotelRatings()[hotelIndex] < 5) {
            System.out.println("Do you want to upgrade " + game.getHotelName(hotelIndex) + " for $" + (0.5 * game.getHotelPrices()[hotelIndex]) + "? (yes/no)");
            String response = scanner.nextLine().toLowerCase();

            if (response.equals("yes")) {
                // If they choose to upgrade, try to upgrade the hotel
                boolean upgraded = game.upgradeHotel(hotelIndex);
                if (upgraded) {
                    // If the upgrade is successful, let the player know
                    System.out.println("You upgraded " + game.getHotelName(hotelIndex));
                } else {
                    // If the upgrade is not successful (because they don't have enough money), let the player know
                    System.out.println("You don't have enough money to upgrade this hotel.");
                }
            }
        } else if (game.getHotelOwners()[hotelIndex] == playerTurn) {
            // If the hotel is already fully upgraded, let the player know
            System.out.println(game.getHotelName(hotelIndex) + " is already at max rating.");
        }
    }
}