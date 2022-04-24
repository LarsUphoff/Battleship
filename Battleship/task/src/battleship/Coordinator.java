package battleship;

import java.util.Scanner;

public class Coordinator {
    Scanner scanner = new Scanner(System.in);
    Player currentPlayer = Player.PLAYER_1;
    UserInterface userInterface;

    private boolean aShipWasHit = false;
    private boolean aShipHasSunk = false;
    private boolean gameIsFinished = false;

    public Coordinator(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
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

    public void makeYourMoves() {
        while (playersHaveShipsOnField()) {
            userInterface.promptUserToPassMove();
            changePlayer();
            userInterface.printGameBoard(currentPlayer);
            userInterface.printWhoseTurnItIs(currentPlayer);
            do {
                System.out.print("\n> ");
                attackOtherPlayer();
            } while (userInterface.shotIsInvalid());
        }
    }

    private boolean playersHaveShipsOnField() {
        return (Player.PLAYER_1.getBattlefield().getShipsOnField() > 0) && (Player.PLAYER_2.getBattlefield().getShipsOnField() > 0);
    }

    private void changePlayer() {
        this.currentPlayer = this.currentPlayer == Player.PLAYER_1 ? Player.PLAYER_2 : Player.PLAYER_1;
    }

    private void attackOtherPlayer() {
        try {
            getTargetedCellFromUser();
            getAttackResults();
        } catch (Exception e) {
            userInterface.setCoordinatesDoNotMatchFormat(true);
            scanner.nextLine();
        }
    }

    private void getTargetedCellFromUser() {
        String coordinate = scanner.next();
        otherPlayersBattlefield().setTargetedRow(coordinate.charAt(0) - 65);
        otherPlayersBattlefield().setTargetedColumn(Integer.parseInt(coordinate.replaceAll("\\D", "")) - 1);
    }

    private Battlefield otherPlayersBattlefield() {
        return otherPlayer().getBattlefield();
    }

    public Player otherPlayer() {
        return this.currentPlayer == Player.PLAYER_1 ? Player.PLAYER_2 : Player.PLAYER_1;
    }

    public void getAttackResults() {
        aShipWasHit = false;
        aShipHasSunk = false;
        gameIsFinished = false;
        for (Ship ship : otherPlayer().getShips()) {
            checkIfAShipWasHit(ship);
            checkIfAShipHasSunk(ship);
            checkIfGameIsFinished();
        }
    }

    private void checkIfAShipWasHit(Ship ship) {
        if (targetedCellIsNotYetHit(ship)) {
            ship.markCellAsHit(targetedRow(), targetedColumn());
            ship.reduceHealthPoints();
            aShipWasHit = true;
        } else if (targetedCellIsAlreadyHit(ship)) {
            aShipWasHit = true;
        }
    }

    private boolean targetedCellIsNotYetHit(Ship ship) {
        return ship.getCellStatus(targetedRow(), targetedColumn()) == Cell.O.getStatus();
    }

    private int targetedRow() {
        return otherPlayersBattlefield().getTargetedRow();
    }

    private int targetedColumn() {
        return otherPlayersBattlefield().getTargetedColumn();
    }

    private boolean targetedCellIsAlreadyHit(Ship ship) {
        return ship.getCellStatus(targetedRow(), targetedColumn()) == Cell.X.getStatus();
    }

    private void checkIfAShipHasSunk(Ship ship) {
        if (aShipWasHit && shipsHPAreAtZero(ship) && !ship.hasSunk()) {
            ship.setHasSunk(true);
            otherPlayersBattlefield().reduceShipsOnField();
            aShipHasSunk = true;
        }
    }

    private boolean shipsHPAreAtZero(Ship ship) {
        return ship.getHealthPoints() == 0;
    }

    private void checkIfGameIsFinished() {
        if (aShipHasSunk && otherPlayersBattlefield().isCleared()) {
            gameIsFinished = true;
        }
    }

    public boolean gameIsFinished() {
        return gameIsFinished;
    }

    public void handleShotOutcome(String message, char cellStatus) {
        otherPlayersBattlefield().updateCell(targetedRow(), targetedColumn(), cellStatus);
        System.out.println();
        System.out.println(message);
    }

    public boolean aShipHasSunk() {
        return aShipHasSunk;
    }

    public boolean aShipWasHit() {
        return aShipWasHit;
    }

    public boolean shotIsAMiss() {
        char statusOfTargetedCell = otherPlayersBattlefield().getCellStatus(targetedRow(), targetedColumn());
        return statusOfTargetedCell == Cell.E.getStatus() || statusOfTargetedCell == Cell.M.getStatus();
    }
}