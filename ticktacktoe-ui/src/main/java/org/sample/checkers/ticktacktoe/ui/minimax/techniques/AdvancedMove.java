package org.sample.checkers.ticktacktoe.ui.minimax.techniques;

import static java.lang.Math.abs;

public class AdvancedMove {

    private final int column; // stlpec
    private final int height;

    public AdvancedMove(int column, int height) {
        this.column = column;
        this.height = height;
    }

    public static AdvancedMove ofStorageEntry(String move, int height){
        return new AdvancedMove(Integer.parseInt(move), height);
    }

    /**
     * Mirror move on y-Axis
     *
     * @return mirrored move
     */
    AdvancedMove mirrorYAxis() {
        return new AdvancedMove(abs(height - this.column), height); // TODO ? 6 - asi kvoli poctu stlpcov
    }

    @Override
    public String toString() {
        return String.valueOf(column);
    }

    public int getColumn() {
        return column;
    }
}
