package it.unibo.map_editor_bcr.model.map_config;

import unibo.planner22.model.RoomMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MapConfig implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<ArrayList<CellType>> roomConfig = new ArrayList<ArrayList<CellType>>();

    public MapConfig(int dimX, int dimY) {
        for(int y = 0; y < dimY; y++) {
            this.roomConfig.add(new ArrayList<CellType>());
            for(int x = 0; x < dimX; x++) {
                this.roomConfig.get(y).add(CellType.NONE);
            }
        }
    }
    public MapConfig(RoomMap map) {
        for(int y = 0; y < map.getDimY(); y++) {
            this.roomConfig.add(new ArrayList<CellType>());
            for(int x = 0; x < map.getDimX(); x++) {
                this.roomConfig.get(y).add(CellType.NONE);
            }
        }
    }

    public CellType get(int x, int y) {
        return this.roomConfig.get(y).get(x);
    }

    public void put(int x, int y, CellType ct) {
        try {
            roomConfig.get(y);
        } catch (IndexOutOfBoundsException e) {
            for (int i = roomConfig.size(); i < y; i++) {
                roomConfig.add(new ArrayList<CellType>());
            }
            roomConfig.add(y, new ArrayList<CellType>());
        }
        try {
            roomConfig.get(y).get(x);
            roomConfig.get(y).remove(x);
            roomConfig.get(y).add(x, ct);
        } catch (IndexOutOfBoundsException e) {
            for (int j = roomConfig.get(y).size(); j < x; j++) {
                roomConfig.get(y).add(CellType.NONE);
            }
            roomConfig.get(y).add(x, ct);
        }
    }

    public void move(int srcX, int srcY, int dstX, int dstY) {
        this.put(dstX, dstY, this.get(srcX, srcY));
        this.put(srcX, srcY, CellType.NONE);
    }
    public void swap(int srcX, int srcY, int dstX, int dstY) {
        CellType ctTMP = this.get(dstX, dstY);
        this.put(dstX, dstY, this.get(srcX, srcY));
        this.put(srcX, srcY, ctTMP);
    }

    public int getDimX() {
        int result=0;
        for (int i=0; i < roomConfig.size(); i++) {
            if (result < roomConfig.get(i).size())
                result = roomConfig.get(i).size();
        }
        return result;
    }

    public int getDimY() {
        return roomConfig.size();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (ArrayList<CellType> ctList : this.roomConfig) {
            builder.append("|");
            for (CellType ct : ctList) {
                builder.append(ct.code + ", ");
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
                else sb.append(" " + this.roomConfig.get(y-1).get(x-1).code + " ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
