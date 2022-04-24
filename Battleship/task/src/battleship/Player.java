package battleship;

import java.util.Arrays;

public enum Player {

    PLAYER_1("Player 1"),
    PLAYER_2("Player 2");

    private final Battlefield battlefield = new Battlefield();
    private final Ship aircraftCarrier = new Ship("Aircraft Carrier", 5);
    private final Ship battleship = new Ship("Battleship", 4);
    private final Ship submarine = new Ship("Submarine", 3);
    private final Ship cruiser = new Ship("Cruiser", 3);
    private final Ship destroyer = new Ship("Destroyer", 2);

    private final Ship[] ships = new Ship[]{aircraftCarrier, battleship, submarine, cruiser, destroyer};
    private final String name;

    private int ordinal = 0;

    Player(String name) {
        this.name = name;
    }

    public void setCurrentShipNumber(int shipNumber) {
        this.ordinal = shipNumber;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public Ship getCurrentShip() {
        return ships[ordinal];
    }

    public Ship[] getShips() {
        return ships;
    }

    @Override
    public String toString() {
        return this.name;
    }


}
