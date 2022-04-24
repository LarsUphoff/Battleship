package battleship;

public class Ship {
    private final String name;
    private int healthPoints;
    private int firstRow = 0;
    private int firstColumn = 0;
    private int secondRow = 0;
    private int secondColumn = 0;

    private boolean hasSunk = false;
    private final char[][] shipArray = new char[10][10];

    Ship(String name, int healthPoints) {
        this.name = name;
        this.healthPoints = healthPoints;
    }

    public void reduceHealthPoints() {
        healthPoints--;
    }

    public void setShipArray() {
        for (int i = firstRow; i <= secondRow; i++) {
            for (int j = firstColumn; j <= secondColumn; j++) {
                shipArray[i][j] = Cell.O.getStatus();
            }
        }
    }

    public char getCellStatus(int row, int column) {
        return shipArray[row][column];
    }

    public void markCellAsHit(int row, int column) {
        shipArray[row][column] = Cell.X.getStatus();
    }

    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn) {
        this.firstColumn = firstColumn;
    }

    public int getSecondRow() {
        return secondRow;
    }

    public void setSecondRow(int secondRow) {
        this.secondRow = secondRow;
    }

    public int getSecondColumn() {
        return secondColumn;
    }

    public void setSecondColumn(int secondColumn) {
        this.secondColumn = secondColumn;
    }

    public boolean hasSunk() {
        return hasSunk;
    }

    public void setHasSunk(boolean hasSunk) {
        this.hasSunk = hasSunk;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "name='" + name + '\'' +
                ", healthPoints=" + healthPoints +
                ", firstRow=" + firstRow +
                ", firstColumn=" + firstColumn +
                ", secondRow=" + secondRow +
                ", secondColumn=" + secondColumn +
                '}';
    }


}
