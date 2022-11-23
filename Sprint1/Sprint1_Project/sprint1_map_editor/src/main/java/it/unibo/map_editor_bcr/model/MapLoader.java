package it.unibo.map_editor_bcr.model;

import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import it.unibo.map_editor_bcr.model.room_map.RoomMapParser;
import unibo.planner22.model.RoomMap;

import java.io.*;

public class MapLoader {
    /**
     * Load RoomMap object from a file
     * @param filename
     * @return a RoadMap
     */
    public static RoomMap loadRoomMap(String filename) {
        RoomMap map = null;
        try {
            ObjectInputStream inps = new ObjectInputStream(new FileInputStream(filename + ".bin"));
            map = (RoomMap) inps.readObject();
            System.out.println("loadRoomMap = " + filename + " DONE");
        } catch (Exception e) {
            System.out.println("loadRoomMap = " + filename + " FAILED: " + e.getMessage());
        }
        return map;
    }

    public static void saveRoomMap(RoomMap map, String filename) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(filename + ".txt"))) {
            pw.print(map.toString());
            System.out.println("saveMap in " + filename + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename + ".bin"))) {
            os.writeObject(map.getRoomMap());
            System.out.println("saveMap in " + filename + ".bin");
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MapConfig loadMapConfig(String filename) {
        MapConfig config = null;
        try {
            ObjectInputStream inps = new ObjectInputStream(new FileInputStream(filename + ".bin"));
            config = (MapConfig) inps.readObject();
            System.out.println("loadRoomConfig = " + filename + " DONE");
        } catch (Exception e) {
            System.out.println("loadRoomConfig = " + filename + " FAILED: " + e.getMessage());
        }
        return config;
    }

    public static boolean saveMapConfig(MapConfig mapConfig, String filename) {
        try(PrintWriter pw = new PrintWriter(new FileWriter(filename + ".txt"))) {
            pw.print(mapConfig.toString());
            System.out.println("saveMap in " + filename + ".txt");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try(ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename + ".bin"))) {
            os.writeObject(mapConfig);
            System.out.println("saveMap in " + filename + ".bin");
            os.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /*public static boolean saveMap(String filename, Map map) {
        map.fillBorderElements(CellType.BORDER);
        map.fillInnerElements(CellType.EMPTY);
        System.out.println(map.toFancyString());
        try {
            File fout = new File(filename);

            if(fout.exists()) {
                return false;
            }
            fout.createNewFile();

            FileOutputStream fos = new FileOutputStream(fout);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for(String s : map.toString().split("\n")) {
                bw.write(s);
                bw.newLine();
            }

            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }*/

    // Test
    public static void main(String[] args) {
        /*
        RoomMap m = MapLoaderNew.loadMap("mapRoomEmpty.txt");

        System.out.println(m.toString());*/

        RoomMap m = loadRoomMap("mapRoomEmpty");
        System.out.println(m.toString());
        System.out.println(m.getDimX());

        RoomMapParser rmp = new RoomMapParser(m);
        System.out.println(rmp.getCellType(0,0).toString());

        /*MapConfig mc = new MapConfig(10, 4);
        mc.put(0,0, it.unibo.sprint1_map_editor.model.map_config.CellType.PLASTIC);
        mc.put(5,0, it.unibo.sprint1_map_editor.model.map_config.CellType.GLASS);

        System.out.println(mc.toFancyString());

        mc.move(0,0, 5,1);
        System.out.println(mc.toFancyString());

        mc.swap(5,1, 9,1);
        System.out.println(mc.toFancyString());*/

        MapConfig mc = new MapConfig(7, 5);
        mc.put(0,0, CellType.HOME);
        mc.put(0,4, CellType.INDOOR);
        mc.put(1,4, CellType.INDOOR);
        mc.put(2,4, CellType.INDOOR);
        mc.put(4,0, CellType.GLASS);
        mc.put(5,0, CellType.GLASS);
        mc.put(6,0, CellType.GLASS);
        mc.put(4,4, CellType.PLASTIC);
        mc.put(5,4, CellType.PLASTIC);
        mc.put(6,4, CellType.PLASTIC);
        System.out.println(mc.toString());
        saveMapConfig(mc, "mapConfigWasteService");

        /*
        // Test: OK
        m.put(1,1, new Box(false, true, true));
        saveRoomMap(m, "test");
        */
    }
}
