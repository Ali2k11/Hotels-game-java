package HotelGameController;

import javax.swing.*;
import java.util.Random;
import HotelGameModel.HotelGameModel;
import HotelGameView.HotelGameView;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;


public class HotelGameController {
    private HotelGameModel model;
    private HotelGameView view;
    private int[] playerPositions;
    private String[] squareContents = new String[25];
    private boolean[][] playerMarkers = new boolean[2][25];  // Tracks the player positions on the board.

    public HotelGameController(HotelGameModel model, HotelGameView view) {
        this.model = model;
        this.view = view;
        setupKeyListener();
        this.playerPositions = new int[] { 0, 0 }; // Initialize player positions at the "GO" square
        
        // Initialize square contents and player markers
        for (int i = 0; i < 25; i++) {
            squareContents[i] = view.getSquareLabels()[i].getText();
            playerMarkers[0][i] = false;
            playerMarkers[1][i] = false;
        }
    }


    //Starts Game
    public void playGame() {
        initializeCounters();
    }
    // Dice Roll handler. In cheat mode or normal mode.
    private void rollDice() {
        int roll;
        if (model.isCheatMode()) {
            String input = JOptionPane.showInputDialog("Enter the number of steps you want to move (1-12):");
            try {
                roll = Integer.parseInt(input);
                if (roll < 1 || roll > 12) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 1 and 12.");
                return;
            }
        } else {
            Random rand = new Random();
            roll = rand.nextInt(12) + 1;
        }
        view.getDiceRollLabel().setText("Roll: " + roll);
        movePlayer(roll);
    }
    // Moves the current player based on the roll.
    private void movePlayer(int roll) {
        int currentPlayer = model.getPlayerTurn() - 1;
        int currentPos = playerPositions[currentPlayer];
        int newPos = (currentPos + roll) % 25;
        setPlayerPos(currentPlayer, currentPos, newPos);
        playerPositions[currentPlayer] = newPos;

        checkSquare(newPos);
    }
    
    

    // Updates the player position on the board
    private void setPlayerPos(int playerIndex, int oldPos, int newPos) {
        playerMarkers[playerIndex][oldPos] = false; // Remove the old marker
        updateSquareText(oldPos); // Update the JLabel's text at the old position
    
        playerMarkers[playerIndex][newPos] = true; // Add the new marker
        updateSquareText(newPos); // Update the JLabel's text at the new position
    }
    


    // Checks the state of the square where the player landed
    private void checkSquare(int squareIndex) {
        JLabel squareLabel = view.getSquareLabels()[squareIndex];
    
        if (squareLabel.getText().matches("<html>[A-H][0-3].*")) {
            int hotelIndex = squareIndex ;
            int hotelPrice = model.getHotelPrices()[hotelIndex];
            int hotelOwner = model.getHotelOwners()[hotelIndex];
            int hotelRating = model.getHotelRatings()[hotelIndex];
            String hotelGroup = model.getHotelGroup(hotelIndex);
    
            String hotelName = model.getHotelName(hotelIndex); 
            if (hotelOwner == 0) {
                // Buying a hotel
                if ((model.getPlayerTurn() == 1 && model.getPlayer1Money() >= hotelPrice) || 
                    (model.getPlayerTurn() == 2 && model.getPlayer2Money() >= hotelPrice)) {
                    int result = JOptionPane.showConfirmDialog(view, "Do you want to buy " + hotelName + " for $" + hotelPrice + "?");
                    if (result == JOptionPane.YES_OPTION) {
                        model.buyHotel(hotelIndex, model.getPlayerTurn()); // pass the player index to buyHotel
                        updateSquareText(squareIndex);
                        updatePlayerMoneyLabels();
                        
                        // Begin of added upgrade immediately after buying
                        // Upgrading a hotel
                        hotelRating = model.getHotelRatings()[hotelIndex]; // Update hotelRating for next iteration
                        while (hotelRating < 5 && 
                            ((model.getPlayerTurn() == 1 && model.getPlayer1Money() >= hotelPrice * 0.5) || 
                            (model.getPlayerTurn() == 2 && model.getPlayer2Money() >= hotelPrice * 0.5))) {
                            int upgradeResult = JOptionPane.showConfirmDialog(view, "Do you want to upgrade " + hotelName + " for $" + (hotelPrice * 0.5) + "?");
                            if (upgradeResult == JOptionPane.YES_OPTION) {
                                model.upgradeHotel(hotelIndex);
                                updateSquareText(squareIndex);
                                updatePlayerMoneyLabels();
                                hotelRating = model.getHotelRatings()[hotelIndex]; // Update hotelRating for next iteration
                            } else {
                                break; // Exit the loop if the player chooses not to upgrade further
                            }
                        }
                    }
                    // End of added upgrade immediately after buying
                    
                } else {
                    JOptionPane.showMessageDialog(view, "You do not have enough money to buy " + hotelName + ".");
                }
            } else if (hotelOwner != 0 && hotelOwner == model.getPlayerTurn()) {
                // Upgrading a hotel
                while (hotelRating < 5 && 
                    ((model.getPlayerTurn() == 1 && model.getPlayer1Money() >= hotelPrice * 0.5) || 
                    (model.getPlayerTurn() == 2 && model.getPlayer2Money() >= hotelPrice * 0.5))) {
                        int result = JOptionPane.showConfirmDialog(view, "Do you want to upgrade " + hotelName + " for $" + (hotelPrice * 0.5) + "?");
                        if (result == JOptionPane.YES_OPTION) {
                            model.upgradeHotel(hotelIndex);
                            updateSquareText(squareIndex);
                            updatePlayerMoneyLabels();
                            hotelRating = model.getHotelRatings()[hotelIndex]; // Update hotelRating for next iteration
                        } else {
                            break; // Exit the loop if the player chooses not to upgrade further
                        }
                    }
                    if (hotelRating == 5) {
                        JOptionPane.showMessageDialog(view, hotelName + " has reached the maximum rating.");
                    } else if ((model.getPlayerTurn() == 1 && model.getPlayer1Money() < hotelPrice * 0.5) || 
                        (model.getPlayerTurn() == 2 && model.getPlayer2Money() < hotelPrice * 0.5)) {
                        JOptionPane.showMessageDialog(view, "You do not have enough money to upgrade " + hotelName + ".");
                    }
                } else {
                    int fee = model.calculateFee(hotelIndex);
                    if (model.ownsAllInGroup(hotelOwner, hotelGroup)) {
                        fee *= 2; // If the owner owns all hotels in the group, the fee is doubled
                    }
                    if (model.ownsAnyInGroup(model.getPlayerTurn(), hotelGroup)) {
                        fee /= 2; // If the guest owns any hotel in the group, the fee is halved
                    }
                    JOptionPane.showMessageDialog(view, "This hotel is owned by Player " + hotelOwner + ". You have to pay a fee of $" + fee);
                    model.payFee(hotelIndex, model.getPlayerTurn(), fee); // pass the fee to the payFee method
                    updatePlayerMoneyLabels();
                }
        
                if (model.getPlayerTurn() == 1) {
                    if (model.getPlayer1Money() < 0) {
                        JOptionPane.showMessageDialog(view, "Player 2 is the winner!");
                        System.exit(0);
                    } else {
                        model.setPlayerTurn(2);
                        view.updatePlayerTurn(model.getPlayerTurn());
                    }
                } else {
                    if (model.getPlayer2Money() < 0) {
                        JOptionPane.showMessageDialog(view, "Player 1 is the winner!");
                        System.exit(0);
                    } else {
                        model.setPlayerTurn(1);
                        view.updatePlayerTurn(model.getPlayerTurn());
                    }
                }
            }
}
                
    
    
    // Updates the money labels for both players
    private void updatePlayerMoneyLabels() {
        view.getPlayer1MoneyLabel().setText("Player 1: $" + model.getPlayer1Money());
        view.getPlayer2MoneyLabel().setText("Player 2: $" + model.getPlayer2Money());
    }
    // Initialize player markers at the "GO" square
    private void initializeCounters() {
        setPlayerPos(0, playerPositions[0], playerPositions[0]);
        setPlayerPos(1, playerPositions[1], playerPositions[1]);
        playerMarkers[0][0] = true; 
        playerMarkers[1][0] = true;  
    }

    private void updateSquareText(int squareIndex) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (squareIndex == 0) {
                    // Handle the "GO" square
                    String text = "<html>GO";
                    if (playerMarkers[0][squareIndex]) {
                        text += " <font color='red'>P1</font>";
                    }
                    if (playerMarkers[1][squareIndex]) {
                        text += " <font color='blue'>P2</font>";
                    }
                    text += "</html>";
                    view.getSquareLabels()[squareIndex].setText(text);
                    return;
                }
    
                int hotelIndex = squareIndex ;
                int hotelPrice = model.getHotelPrices()[hotelIndex];
                int hotelRating = model.getHotelRatings()[hotelIndex];
                String owner = model.getHotelOwners()[hotelIndex] == 0 ? "None" : "Player " + model.getHotelOwners()[hotelIndex];
                String hotelName = model.getHotelName(squareIndex);
                String text = "<html>" + hotelName + "<br>Price: $" + hotelPrice + "<br>Owner: " + owner + "<br>Rating: " + hotelRating + " stars";
    
                // player markers
                if (playerMarkers[0][squareIndex]) {
                    text += "<br><font color='red'>P1</font>";
                }
                if (playerMarkers[1][squareIndex]) {
                    text += "<br><font color='blue'>P2</font>";
                }
    
                text += "</html>";
                view.getSquareLabels()[squareIndex].setText(text);
            }
        });
    }
    
// Key listener for rolling the dice spacebar
    private void setupKeyListener() {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = view.getRootPane().getInputMap(condition);
        ActionMap actionMap = view.getRootPane().getActionMap();
    
        String keyPressed = "keyPressed";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), keyPressed);
        actionMap.put(keyPressed, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rollDice();
            }
        });
    // press c for cheatmode
        String cheatKey = "cheatKey";
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), cheatKey);
        actionMap.put(cheatKey, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean currentCheatModeStatus = model.isCheatMode();
                model.setCheatMode(!currentCheatModeStatus); // Toggle the cheat mode
                String cheatStatus = model.isCheatMode() ? "ON" : "OFF";
                JOptionPane.showMessageDialog(view, "Cheat mode is now " + cheatStatus);
            }
        });
    }
    
    

}
