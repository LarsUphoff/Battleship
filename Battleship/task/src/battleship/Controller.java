package battleship;

import java.util.Scanner;

public abstract class Controller {

    Scanner scanner = new Scanner(System.in);
    Player currentPlayer = Player.PLAYER_1;
    UserInterface userInterface;

    protected Controller(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    protected void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

}
