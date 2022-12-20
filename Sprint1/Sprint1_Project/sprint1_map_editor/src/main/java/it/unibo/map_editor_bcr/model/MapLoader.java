package it.unibo.map_editor_bcr.model;

import it.unibo.map_editor_bcr.model.exceptions.FileFormatException;
import it.unibo.map_editor_bcr.model.exceptions.FileNameException;
import it.unibo.map_editor_bcr.model.map_config.CellType;
import it.unibo.map_editor_bcr.model.map_config.MapConfig;
import it.unibo.map_editor_bcr.model.room_map.RoomMapParser;
import unibo.planner22.model.RoomMap;

import java.io.*;

public class MapLoader {
    private static Logger logger = Logger.getInstance();

    /**
     * Load RoomMap object from a file
     * @param filename
     * @return a RoadMap
     */
    public static RoomMap loadRoomMap(String filename) throws IOException, ClassNotFoundException, FileFormatException, FileNameException {
        if(filename.isEmpty()) {
            throw new FileNameException("File name '" + filename + "' is invalid.");
        }
        if(!filename.endsWith(".bin")) {
            throw new FileFormatException("Invalid format for file " + filename + ". RoomMap file format must be '.bin'.");
        }

        ObjectInputStream inps = new ObjectInputStream(new FileInputStream(filename));
        RoomMap map = (RoomMap) inps.readObject();

        inps.close();
        //logger.logMessage("loadRoomMap = " + filename + " DONE"); // TO-DO
        //System.out.println("loadRoomMap = " + filename + " DONE");
        //System.out.println("loadRoomMap = " + filename + " FAILED: " + e.getMessage());

        return map;
    }

    public static void saveRoomMap(RoomMap map, String filename) {
        // TO-DO: check .bin

        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(map.getRoomMap());
            System.out.println("saveMap in " + filename);
            oos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveRoomMapRepresentation(RoomMap map, String filename) {
        // TO-DO: check .txt

        try(PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.print(map.toString());
            System.out.println("saveMap in " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MapConfig loadMapConfig(String filename) throws IOException, ClassNotFoundException, FileFormatException, FileNameException {
        if(filename.isEmpty()) {
            throw new FileNameException("File name '" + filename + "' is invalid.");
        }
        if(!filename.endsWith(".bin")) {
            throw new FileFormatException("Invalid format for file " + filename + ". MapConfig file format must be '.bin'.");
        }

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        MapConfig config = (MapConfig) ois.readObject();

        ois.close();

        return config;
    }

    public static void saveMapConfig(MapConfig mapConfig, String filename) throws FileFormatException, FileNameException, IOException {
        if(mapConfig == null) {
            throw new IllegalArgumentException("MapConfig is null.");
        }
        if(filename.isEmpty()) {
            throw new FileNameException("File name '" + filename + "' is invalid.");
        }
        if(!filename.endsWith(".bin")) {
            throw new FileFormatException("Invalid format for file " + filename + ". MapConfig file format must be '.bin'.");
        }

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));
        oos.writeObject(mapConfig);

        oos.close();
    }

    public static boolean saveMapConfigRepresentation(MapConfig mapConfig, String filename) {
        // TO-DO: check .txt

        try(PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            pw.print(mapConfig.toString());
            System.out.println("saveMap in " + filename);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Test
    public static void main(String[] args) {
        /*
        RoomMap m = MapLoaderNew.loadMap("mapRoomEmpty.txt");

        System.out.println(m.toString());*/

        String filename = "mapRoomEmpty.bin";
        RoomMap m = null;
        try {
            m = loadRoomMap("mapRoomEmpty.bin");
            System.out.println("loadRoomMap = " + filename + " DONE");
        } catch (Exception e) {
            System.out.println("loadRoomMap = " + filename + " FAILED: " + e.getMessage());
        }
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
        try {
            saveMapConfig(mc, "mapConfigWasteService.bin");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
        // Test: OK
        m.put(1,1, new Box(false, true, true));
        saveRoomMap(m, "test");
        */
    }
}
