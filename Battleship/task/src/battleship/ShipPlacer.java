package battleship;

public class ShipPlacer extends Controller {

    public ShipPlacer(UserInterface userInterface) {
        super(userInterface);
    }

    public void placeShips() {
        for (int shipNumber = 0; shipNumber < 5; shipNumber++) {
            currentPlayer.setCurrentShipNumber(shipNumber);
            userInterface.promptUserToEnterCoordinatesForShipPlacement(currentPlayer);
            do {
                getCurrentShipPosition();
                System.out.println();
            } while (userInterface.enteredShipDataIsInvalid(currentPlayer));
            currentPlayersBattlefield().updateCoordinatesToOccupied(shipsFirstRow(), shipsFirstColumn(), shipsSecondRow(), shipsSecondColumn());
            currentPlayersBattlefield().printBattlefield();
        }
    }

    public void getCurrentShipPosition() {
        System.out.print("\n> ");
        try {
            getShipCoordinatesFromUser();
            adjustRowOrder();
            adjustColumnOrder();
            currentShip().setShipArray();
        } catch (Exception e) {
            userInterface.setCoordinatesDoNotMatchFormat(true);
            scanner.nextLine();
        }
    }

    private void getShipCoordinatesFromUser() {
        String firstCoordinates = scanner.next();
        String secondCoordinates = scanner.next();
        currentShip().setFirstRow(firstCoordinates.charAt(0) - 65);
        currentShip().setFirstColumn(Integer.parseInt(firstCoordinates.replaceAll("\\D", "")) - 1);
        currentShip().setSecondRow(secondCoordinates.charAt(0) - 65);
        currentShip().setSecondColumn(Integer.parseInt(secondCoordinates.replaceAll("\\D", "")) - 1);
    }

    private void adjustRowOrder() {
        if (shipsFirstRow() > shipsSecondRow()) {
            int tmp = shipsSecondRow();
            currentShip().setSecondRow(shipsFirstRow());
            currentShip().setFirstRow(tmp);
        }
    }

    private void adjustColumnOrder() {
        if (shipsFirstColumn() > shipsSecondColumn()) {
            int tmp = shipsSecondColumn();
            currentShip().setSecondColumn(shipsFirstColumn());
            currentShip().setFirstColumn(tmp);
        }
    }

    private int shipsFirstRow() {
        return currentShip().getFirstRow();
    }

    private int shipsFirstColumn() {
        return currentShip().getFirstColumn();
    }

    private int shipsSecondRow() {
        return currentShip().getSecondRow();
    }

    private int shipsSecondColumn() {
        return currentShip().getSecondColumn();
    }

    public boolean shipLocationIsWrong() {
        return shipsFirstRow() != shipsSecondRow() && shipsFirstColumn() != shipsSecondColumn();
    }

    private Ship currentShip() {
        return currentPlayer.getCurrentShip();
    }

    private Battlefield currentPlayersBattlefield() {
        return currentPlayer.getBattlefield();
    }

    public boolean shipLengthIsWrong() {
        return Math.abs(shipsSecondRow() - shipsFirstRow()) != currentShipsHealthPoints() - 1 && (shipsFirstColumn() == shipsSecondColumn())
                || Math.abs(shipsSecondColumn() - shipsFirstColumn()) != currentShipsHealthPoints() - 1 && (shipsFirstRow() == shipsSecondRow());
    }

    private int currentShipsHealthPoints() {
        return currentShip().getHealthPoints();
    }

    public boolean shipIsTooClose() {
        return currentPlayersBattlefield().isOccupied(shipsFirstRow(), shipsFirstColumn(), shipsSecondRow(), shipsSecondColumn());
    }

}
