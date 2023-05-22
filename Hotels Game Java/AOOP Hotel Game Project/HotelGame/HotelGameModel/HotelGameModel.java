package HotelGameModel;

public class HotelGameModel {
    private int player1Money;
    private int player2Money;
    private int playerTurn;
    private int[] hotelPrices;
    private int[] hotelOwners;
    private int[] hotelRatings;
    private boolean cheatMode = false;

    public HotelGameModel() {
        playerTurn = 1;
        player1Money = 2000;
        player2Money = 2000;
        hotelOwners = new int[25];
        hotelRatings = new int[25];
        hotelPrices = new int[]{0, 50, 50, 70, 100, 100, 120, 150, 150, 170, 200, 200, 220, 250, 250, 270, 300, 300, 320, 350, 350, 370, 400, 400, 420};
        for (int i = 0; i < 25; i++) {
            hotelRatings[i] = 0; // Initializing hotel ratings to zero
        }
    }

    public int getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;

        // Precondition: playerTurn == 1 || playerTurn == 2.

        // Postcondition: this.playerTurn == playerTurn.
    }

    public int getPlayer1Money() {
        return player1Money;
    }

    public void setPlayer1Money(int player1Money) {
        this.player1Money = player1Money;

        // Precondition: player1Money >= 0.

        // Postcondition: this.player1Money == player1Money.
    }

    public int getPlayer2Money() {
        return player2Money;
    }

    public void setPlayer2Money(int player2Money) {
        this.player2Money = player2Money;

        // Precondition: player2Money >= 0.

        // Postcondition: this.player2Money == player2Money.
    }

    public int[] getHotelOwners() {
        return hotelOwners;
    }

    public void setHotelOwners(int[] hotelOwners) {
        this.hotelOwners = hotelOwners;
        // Precondition: hotelOwners[i] == 0 || hotelOwners[i] == 1 || hotelOwners[i] == 2 for all i in range of hotelOwners array.

        // Postcondition: this.hotelOwners[i] == hotelOwners[i] for all i in range of hotelOwners array.
    }

    public int[] getHotelRatings() {
        return hotelRatings;
    }

    public void setHotelRatings(int[] hotelRatings) {
        this.hotelRatings = hotelRatings;
        // Precondition: hotelRatings[i] >= 0 && hotelRatings[i] <= 5 for all i in range of hotelRatings array.

        // Postcondition: this.hotelRatings[i] == hotelRatings[i] for all i in range of hotelRatings array.

    }

    public int[] getHotelPrices() {
        return hotelPrices;
    }

    public void setHotelPrices(int[] hotelPrices) {
        this.hotelPrices = hotelPrices;
        // Precondition: hotelPrices[i] >= 0 for all i in range of hotelPrices array.

        // Postcondition: this.hotelPrices[i] == hotelPrices[i] for all i in range of hotelPrices array.

    }
    
    // Getting the hotel name and group 
    public String getHotelName(int hotelIndex) {
        int adjustedIndex = hotelIndex - 1; // Adjust for GO square
        int group = adjustedIndex / 3;
        int position = adjustedIndex % 3 + 1;
        char groupName = (char) ('A' + group);
        return groupName + Integer.toString(position);

        // Precondition: hotelIndex >= 1 && hotelIndex <= 24.

        // Postcondition: Returns a valid hotel name based on the hotel index.
    }
    
    public String getHotelGroup(int hotelIndex) {
        int adjustedIndex = hotelIndex - 1; // Adjust for GO square
        int group = adjustedIndex / 3;
        char groupName = (char) ('A' + group);
        return String.valueOf(groupName);

        // Precondition: hotelIndex >= 1 && hotelIndex <= 24.

        // Postcondition: Returns a valid hotel group name based on the hotel index.
    }
    
    
    
    // Check if game is over.
    public boolean gameOver() {
        return player1Money < 0 || player2Money < 0;
        // Precondition: None.

        // Postcondition: Returns true if player1Money < 0 || player2Money < 0; otherwise, 
    }
    // Gets the winner of the game
    public int getWinner() {
        if (player1Money < 0) {
            return 2;
        } else if (player2Money < 0) {
            return 1;
        } else {
            return 0; // Game is not over
        }
        // Precondition: None.

        // Postcondition: Returns 2 if player1Money < 0, 1 if player2Money < 0, and 0 if the game is not over.
    }
    // Switch turns between players
    public void switchTurn() {
        playerTurn = playerTurn == 1 ? 2 : 1;

        // Precondition: None.

        // Postcondition: playerTurn changes to 2 if it was 1 and vice versa.
    }

    public boolean buyHotel(int hotelIndex) {
        int hotelPrice = hotelPrices[hotelIndex];

        if (hotelOwners[hotelIndex] == 0) { // Check if the hotel is not owned
            if (playerTurn == 1 && player1Money >= hotelPrice) {
                player1Money -= hotelPrice;
                hotelOwners[hotelIndex] = 1;
                return true;
            } else if (playerTurn == 2 && player2Money >= hotelPrice) {
                player2Money -= hotelPrice;
                hotelOwners[hotelIndex] = 2;
                return true;
            }
        }

        return false;

        // Precondition: hotelIndex >= 0 && hotelIndex < hotelOwners.length && hotelOwners[hotelIndex] == 0.

        // Postcondition: If the player can afford the hotel, hotelOwners[hotelIndex] == playerTurn and the player's money is decreased by the price of the hotel.
    }


    // Handle payment of fees when player lands on hotel owned by another player
    public void payFee(int hotelIndex, int player, int fee) {
        if (hotelOwners[hotelIndex] != 0 && hotelOwners[hotelIndex] != player) { // Check if the hotel is owned by the other player
            if (player == 1) {
                player1Money -= fee;
                if (player1Money < 0) {
                    player2Money += player1Money; // Transfer remaining money
                } else {
                    player2Money += fee;
                }
            } else {
                player2Money -= fee;
                if (player2Money < 0) {
                    player1Money += player2Money; // Transfer remaining money
                } else {
                    player1Money += fee;
                }
            }
        }
        // Precondition: hotelIndex >= 0 && hotelIndex < hotelOwners.length && hotelOwners[hotelIndex] != 0 && hotelOwners[hotelIndex] != player && fee >= 0.

        // Postcondition: The player's money is decreased by the fee and the other player's money is increased by the fee.
    }
    

    // Method to calculate the fee a player must pay when landing on a hotel owned by another player
    public int calculateFee(int hotelIndex) {
        int hotelPrice = hotelPrices[hotelIndex];
        int hotelRating = hotelRatings[hotelIndex];
        int fee = (int) (0.1 * hotelPrice * Math.pow(hotelRating, 2));

        // Apply fee modifiers based on ownership of other hotels in the group
        int group = hotelIndex / 6;
        int[] groupIndices = new int[]{group * 6, group * 6 + 1, group * 6 + 2, group * 6 + 3, group * 6 + 4, group * 6 + 5};
        int ownerCount = 0;
        int guestCount = 0;

        for (int index : groupIndices) {
            if (hotelOwners[index] == playerTurn) {
                ownerCount++;
            } else if (hotelOwners[index] != 0) {
                guestCount++;
            }
        }

        if (ownerCount == 3) {
            fee *= 2; // Owner owns all hotels in the group, fee is doubled
        } else if (guestCount > 0) {
            fee /= 2; // Guest owns at least one hotel in the group, fee is halved
        }

        return fee;
        // Precondition: hotelIndex >= 0 && hotelIndex < hotelOwners.length.

        // Postcondition: Returns the fee for landing on the specified hotel based on its price, rating, and group ownership.
    }


    // Method to upgrade a hotel's rating
    public boolean upgradeHotel(int hotelIndex) {
        int hotelPrice = hotelPrices[hotelIndex];
        int upgradeCost = (int) (0.5 * hotelPrice);

        if (playerTurn == 1) {
            if (player1Money >= upgradeCost) {
                player1Money -= upgradeCost;
                hotelRatings[hotelIndex] = Math.min(hotelRatings[hotelIndex] + 1, 5);
                return true;
            }
        } else {
            if (player2Money >= upgradeCost) {
                player2Money -= upgradeCost;
                hotelRatings[hotelIndex] = Math.min(hotelRatings[hotelIndex] + 1, 5);
                return true;
            }
        }

        return false;
        // Precondition: hotelIndex >= 0 && hotelIndex < hotelOwners.length && hotelOwners[hotelIndex] == playerTurn && hotelRatings[hotelIndex] < 5.

        // Postcondition: If the player can afford the upgrade, hotelRatings[hotelIndex] increases by 1 and the player's money is decreased by the upgrade cost.

    }

    // Handle purchase of hotel by player
    public boolean buyHotel(int hotelIndex, int player) {
        int hotelPrice = hotelPrices[hotelIndex];
    
        if (hotelOwners[hotelIndex] == 0) { // Check if the hotel is not owned
            if (player == 1 && player1Money >= hotelPrice) {
                player1Money -= hotelPrice;
                hotelOwners[hotelIndex] = 1;
                return true;
            } else if (player == 2 && player2Money >= hotelPrice) {
                player2Money -= hotelPrice;
                hotelOwners[hotelIndex] = 2;
                return true;
            }
        }
    
        return false;

        // Precondition: hotelIndex >= 0 && hotelIndex < hotelOwners.length && hotelOwners[hotelIndex] == 0 && player >= 1 && player <= 2.

        // Postcondition: If the specified player can afford the hotel, hotelOwners[hotelIndex] == player and the player's money is decreased by the price of the hotel.
    }


    // Adjust array for GO square/ checks if they own all hotels in group
    public boolean ownsAllInGroup(int player, String group) {
        int groupIndex = group.charAt(0) - 'A';
        for (int i = groupIndex * 3 + 1; i <= (groupIndex + 1) * 3 + 1; i++) { 
            if (hotelOwners[i] != player) {
                return false;
            }
        }
        return true;

        // Precondition: player >= 1 && player <= 2 && group.length() == 1 && group.charAt(0) >= 'A' && group.charAt(0) <= 'H'.

        // Postcondition: Returns true if the specified player owns all hotels in the specified group; otherwise, returns false.
    }

    // Adjust array for GO square/ checks if owns any in the group
    public boolean ownsAnyInGroup(int player, String group) {
        int groupIndex = group.charAt(0) - 'A';
        for (int i = groupIndex * 3 + 1; i < (groupIndex + 1) * 3 + 1; i++) { 
            if (hotelOwners[i] == player) {
                return true;
            }
        }
        return false;

        // Precondition: player >= 1 && player <= 2 && group.length() == 1 && group.charAt(0) >= 'A' && group.charAt(0) <= 'H'.

        // Postcondition: Returns true if the specified player owns any hotels in the specified group; otherwise, returns false.
    }
    
    // Check if cheat mode is on.
    public boolean isCheatMode() {
        return cheatMode;
    }
    // Enables or disables cheat mode.
    public void setCheatMode(boolean cheatMode) {
        this.cheatMode = cheatMode;
    }

}
