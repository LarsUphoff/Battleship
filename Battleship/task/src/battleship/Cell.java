package battleship;

public enum Cell {
    O('O'),     // Cell with ship
    X('X'),     // Ship was hit
    M('M'),     // Miss
    E('~');     // Empty cell

    private final char status;

    Cell(char status) {
        this.status = status;
    }

    public char getStatus() {
        return status;
    }
}
