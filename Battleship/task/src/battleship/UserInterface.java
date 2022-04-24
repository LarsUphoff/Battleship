package battleship;

import java.util.Scanner;

public class UserInterface {
    Scanner scanner = new Scanner(System.in);
    Battlefield emptyBattlefield = new Battlefield();
    Coordinator coordinator = new Coordinator(this);
    private boolean coordinatesDoNotMatchFormat = false;

    public void start() {
        placeYourShips(Player.PLAYER_1);
        promptUserToPassMove();
        placeYourShips(Player.PLAYER_2);
        coordinator.makeYourMoves();
    }

    private void placeYourShips(Player player) {
        coordinator.setCurrentPlayer(player);
        promptPlayerXToPlaceShips(player);
        emptyBattlefield.printBattlefield();
        coordinator.placeShips();
    }

    private void promptPlayerXToPlaceShips(Player player) {
        System.out.println(player + ", place your ships on the game field\n");
    }

    public void promptUserToEnterCoordinatesForShipPlacement(Player player) {
        System.out.println("\nEnter the coordinates of the " + player.getCurrentShip().getName() + " (" + player.getCurrentShip().getHealthPoints() + " cells):");
    }

    public boolean enteredShipDataIsInvalid(Player player) {
        if (coordinatesDoNotMatchFormat) {
            System.out.println("Error! Your input does not match the required format! Try again:");
            coordinatesDoNotMatchFormat = false;
            return true;
        } else if (coordinator.shipLocationIsWrong()) {
            System.out.println("Error! Wrong ship location! Try again:");
            return true;
        } else if (coordinator.shipLengthIsWrong()) {
            System.out.println("Error! Wrong length of the " + player.getCurrentShip().getName() + "! Try again:");
            return true;
        } else if (coordinator.shipIsTooClose()) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            return true;
        }
        return false;
    }

    public void promptUserToPassMove() {
        System.out.println("\nPress Enter and pass the move to another player\n" +
                "...");
        scanner.nextLine();
    }

    public void printGameBoard(Player player) {
        emptyBattlefield.printBattlefield();
        System.out.println("---------------------");
        player.getBattlefield().printBattlefield();
    }

    public void printWhoseTurnItIs(Player player) {
        System.out.println("\n" + player + ", it's your turn:");
    }

    public boolean shotIsInvalid() {
        if (coordinatesDoNotMatchFormat) {
            System.out.println("\nError! You entered the wrong coordinates! Try again:");
            coordinatesDoNotMatchFormat = false;
            return true;
        } else if (coordinator.gameIsFinished()) {
            coordinator.handleShotOutcome("You sank the last ship. You won. Congratulations!", Cell.X.getStatus());
            return false;
        } else if (coordinator.aShipHasSunk()) {
            coordinator.handleShotOutcome("You sank a ship!", Cell.X.getStatus());
            return false;
        } else if (coordinator.aShipWasHit()) {
            coordinator.handleShotOutcome("You hit a ship!", Cell.X.getStatus());
            return false;
        } else if (coordinator.shotIsAMiss()) {
            coordinator.handleShotOutcome("You missed!", Cell.M.getStatus());
            return false;
        }
        return true;
    }

    public void setCoordinatesDoNotMatchFormat(boolean coordinatesDoNotMatchFormat) {
        this.coordinatesDoNotMatchFormat = coordinatesDoNotMatchFormat;
    }

}
