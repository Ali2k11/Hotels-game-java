package HotelGameView;

import javax.swing.*;
import java.awt.*;
import HotelGameModel.HotelGameModel;

import java.awt.event.KeyListener;

public class HotelGameView extends JFrame {
    private JLabel playerTurnLabel;
    private JLabel player1MoneyLabel;
    private JLabel player2MoneyLabel;
    private JLabel diceRollLabel;
    private JPanel boardPanel;
    private JLabel[] squareLabels = new JLabel[25];
    private JLabel cheatModeLabel;
    
    //constructor
    public HotelGameView(HotelGameModel model) {
        //Sets up main frame.
        super("Hotel Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setup player turn label
        playerTurnLabel = new JLabel("Player 1's turn");
        playerTurnLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(playerTurnLabel, BorderLayout.NORTH);
        // setup dice roll label
        diceRollLabel = new JLabel();
        add(diceRollLabel, BorderLayout.EAST);

        // setup money panel with money labels
        JPanel moneyPanel = new JPanel();
        player1MoneyLabel = new JLabel("Player 1: $2000");
        player2MoneyLabel = new JLabel("Player 2: $2000");
        moneyPanel.add(player1MoneyLabel);
        moneyPanel.add(player2MoneyLabel);
        add(moneyPanel, BorderLayout.SOUTH);

        //setup board panel
        boardPanel =new JPanel();
        boardPanel.setLayout(new GridLayout(5, 5));

         // setup cheat mode label
        cheatModeLabel = new JLabel("Press C for cheat mode");
        cheatModeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        cheatModeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(cheatModeLabel, BorderLayout.NORTH);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(playerTurnLabel, BorderLayout.WEST);
        northPanel.add(cheatModeLabel, BorderLayout.EAST);
        
        
        add(northPanel, BorderLayout.NORTH);
        // setup square labels and board
        for (int i = 0; i < 25; i++) {
            squareLabels[i] = new JLabel();
            squareLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            squareLabels[i].setVerticalAlignment(JLabel.TOP);
            squareLabels[i].setHorizontalAlignment(JLabel.CENTER);
            if (i == 0) {
                squareLabels[i].setText("GO");
            } else {
                String hotelName = model.getHotelName(i);
                int hotelPrice = model.getHotelPrices()[i];
                int hotelRating = model.getHotelRatings()[i];
                String owner = model.getHotelOwners()[i] == 0 ? "None" : "Player " + model.getHotelOwners()[i - 1];
                squareLabels[i].setText("<html>" + hotelName + "<br>Price: $" + hotelPrice + "<br>Owner: " + owner + "<br>Rating: " + hotelRating + " stars</html>");
            }
            boardPanel.add(squareLabels[i]);
        }
        add(boardPanel, BorderLayout.CENTER);
        setVisible(true);
        
    }

    // update the cheat mode.
    public void updateCheatMode(boolean cheatMode) {
        if (cheatMode) {
            cheatModeLabel.setText("Cheat mode ON (Press C to disable)");
        } else {
            cheatModeLabel.setText("Press C for cheat mode");
        }
    }

    // getter for dice roll label
    public JLabel getDiceRollLabel() {
        return diceRollLabel;
    }

    // method to add key listener to the frame
    public void addFrameKeyListener(KeyListener listener) {
        this.addKeyListener(listener);
    }


    // getters for player turn and money labels
    public JLabel getPlayerTurnLabel() {
        return playerTurnLabel;
    }

    public JLabel getPlayer1MoneyLabel() {
        return player1MoneyLabel;
    }

    public JLabel getPlayer2MoneyLabel() {
        return player2MoneyLabel;
    }

    public JLabel[] getSquareLabels() {
        return squareLabels;
    }
    
    // method to update player turn label
    public void updatePlayerTurn(int playerTurn) {
        playerTurnLabel.setText("Player " + playerTurn + "'s turn");
    }

}
