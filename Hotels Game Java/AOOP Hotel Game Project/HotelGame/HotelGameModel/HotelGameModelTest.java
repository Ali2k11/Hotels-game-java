package HotelGameModel;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HotelGameModelTest {
    private HotelGameModel game;

    @Before
    public void setUp() {
        game = new HotelGameModel();
    }

    @Test
    public void testBuyHotel() {
        // Scenario: Player 1 lands on hotel index 1 and attempts to buy it
        int hotelIndex = 1;
        assertTrue(game.buyHotel(hotelIndex, 1));
        assertEquals(1, game.getHotelOwners()[hotelIndex]);
    }

    @Test
    public void testPayFee() {
        // Scenario: Player 2 lands on hotel index 1 owned by Player 1 with a rating of 1 and has to pay fee
        int hotelIndex = 1;
        game.buyHotel(hotelIndex, 1);  // Player 1 owns the hotel
        
        // Set hotel rating
        int[] hotelRatings = game.getHotelRatings();
        hotelRatings[hotelIndex] = 1;
        game.setHotelRatings(hotelRatings);
    
        System.out.println("Player 1 money after buying hotel: " + game.getPlayer1Money());
    
        int initialPlayer2Money = game.getPlayer2Money();
        int fee = game.calculateFee(hotelIndex);
        System.out.println("Calculated fee: " + fee);
    
        game.payFee(hotelIndex, 2, fee);  // Player 2 pays fee
    
        assertEquals(initialPlayer2Money - fee, game.getPlayer2Money());
        assertEquals(1950 + fee, game.getPlayer1Money());
    }

    @Test
    public void testUpgradeHotel() {
        // Scenario: Player 1 lands on hotel index 1 they own and attempts to upgrade its rating
        int hotelIndex = 1;
        game.buyHotel(hotelIndex, 1);  // Player 1 owns the hotel
        int initialPlayer1Money = game.getPlayer1Money();
        int upgradeCost = (int) (0.5 * game.getHotelPrices()[hotelIndex]);

        assertTrue(game.upgradeHotel(hotelIndex));  // Player 1 upgrades the hotel

        assertEquals(1, game.getHotelRatings()[hotelIndex]);
        assertEquals(initialPlayer1Money - upgradeCost, game.getPlayer1Money());
    }




}

