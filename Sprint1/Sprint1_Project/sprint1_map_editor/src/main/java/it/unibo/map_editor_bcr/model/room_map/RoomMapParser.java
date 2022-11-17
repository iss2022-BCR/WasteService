package it.unibo.map_editor_bcr.model.room_map;

import unibo.planner22.model.Box;
import unibo.planner22.model.RoomMap;

import java.util.ArrayList;
import java.util.List;

public class RoomMapParser {
    private List<ArrayList<Box>> listCellType = new ArrayList<ArrayList<Box>>();
    private int dimX, dimY;

    public RoomMapParser(RoomMap map) {
        this.dimX = map.getDimX();
        this.dimY = map.getDimY();

        String s = map.toString();
        String[] lines = s.split("\n");
        for(String l : lines) {
            String[] boxes = l.split(",");

            ArrayList<Box> listY = new ArrayList<Box>();
            this.listCellType.add(listY);
            for(String b : boxes) {
                b = b.replace("|", "").trim();

                if(!b.isEmpty()) {
                    if(b.equals("X")) {
                        listY.add(new Box(true, false));
                    } else if(b.equals("r")) {
                        listY.add(new Box(false, false, true));
                    } else if(b.equals("1")) {
                        listY.add(new Box(false, false));
                    } else {
                        listY.add(new Box());
                    }
                }
            }
        }
    }

    public int getDimX() {
        return this.dimX;
    }
    public int getDimY() {
        return this.dimY;
    }

    public CellType getCellType(int x, int y) {
        Box b = this.getBox(x, y);
        if(b.isObstacle()) {
            return CellType.OBSTACLE;
        } else if(b.isRobot()) {
            return CellType.ROBOT;
        } else if(b.isDirty()) {
            return CellType.DIRTY;
        } else {
            return CellType.EMPTY;
        }
    }

    public Box getBox(int x, int y) {
        return this.listCellType.get(y).get(x);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ArrayList<Box> a : this.listCellType) {
            builder.append("|");
            for (Box b : a) {
                if (b == null)
                    break;
                if (b.isRobot())
                    builder.append("r, ");
                else if (b.isObstacle())
                    builder.append("X, ");
                else if (b.isDirty())
                    builder.append("0, ");
                else
                    builder.append("1, ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public String toFancyString() {
        StringBuilder sb = new StringBuilder();

        int tRows = this.getDimY() + 2;
        int tCols = this.getDimX() + 2;
        for(int y = 0; y < tRows; y++)
        {
            for(int x = 0; x < tCols; x++)
            {
                if((y == 0 && x == 0) ||
                        (y == 0 && x == tCols-1) ||
                        (y == tRows-1 && x == 0) ||
                        (y == tRows-1 && x == tCols-1)) {
                    sb.append("+");
                }
                else if(y == 0 || y == tRows-1) {
                    sb.append("---");
                }
                else if(x == 0 || x == tCols-1) {
                    sb.append("|");
                }
                else sb.append(" " + this.getCellType(x-1, y-1).code + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
