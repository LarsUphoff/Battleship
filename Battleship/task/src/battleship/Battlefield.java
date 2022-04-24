package battleship;

public class Battlefield {
    private final char[][] battlefield = new char[10][10];
    private int targetedRow = 0;
    private int targetedColumn = 0;
    private int shipsOnField = 5;

    public Battlefield() {
        setBattlefield();
    }

    public void setBattlefield() {
        for (int i = 0; i < battlefield.length; i++) {
            for (int j = 0; j < battlefield[0].length; j++) {
                battlefield[i][j] = Cell.E.getStatus();
            }
        }
    }

    public void printBattlefield() {
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (int i = 0, ch = 'A'; i < battlefield.length; i++, ch++) {
            System.out.print((char) ch + " ");
            for (int j = 0; j < battlefield[0].length; j++) {
                System.out.print(battlefield[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void updateCoordinatesToOccupied(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        for (int i = firstRow; i <= secondRow; i++) {
            for (int j = firstColumn; j <= secondColumn; j++) {
                battlefield[i][j] = Cell.O.getStatus();
            }
        }
    }

    public boolean isOccupied(int firstRow, int firstColumn, int secondRow, int secondColumn) {
        for (int i = firstRow - 1; i <= secondRow + 1; i++) {
            if (i < 0) i = 0;
            if (i >= 10) continue;
            for (int j = firstColumn - 1; j <= secondColumn + 1; j++) {
                if (j < 0) j = 0;
                if (j >= 10) continue;
                if (battlefield[i][j] != Cell.E.getStatus()) {
                    return true;
                }
            }
        }
        return false;
    }

    public char getCellStatus(int row, int column) {
        return battlefield[row][column];
    }

    public void updateCell(int row, int column, char status) {
        battlefield[row][column] = status;
    }

    public void reduceShipsOnField() {
        shipsOnField--;
    }

    public boolean isCleared() {
        return shipsOnField == 0;
    }

    public int getTargetedRow() {
        return targetedRow;
    }

    public int getShipsOnField() {
        return shipsOnField;
    }

    public void setTargetedRow(int targetedRow) {
        this.targetedRow = targetedRow;
    }

    public int getTargetedColumn() {
        return targetedColumn;
    }

    public void setTargetedColumn(int targetedColumn) {
        this.targetedColumn = targetedColumn;
    }
}
