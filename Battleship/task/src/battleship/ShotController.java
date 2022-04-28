package battleship;

public class ShotController extends Controller{

    private boolean aShipWasHit = false;
    private boolean aShipHasSunk = false;
    private boolean gameIsFinished = false;

    public ShotController(UserInterface userInterface) {
        super(userInterface);
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
