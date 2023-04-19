package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class WorldGeneration {
    private final TETile[][] world;
    private final int width;
    private final int height;
    private final Random random;
    private ArrayList<Room> curRooms;

    public WorldGeneration(int width, int height, Random random) {
        this.width = width;
        this.height = height;
        this.curRooms = new ArrayList<>();
        this.world = new TETile[this.width][this.height];
        this.random = random;
        for (int x = 0; x < this.width; x += 1) {
            for (int y = 0; y < this.height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
    }

    public TETile[][] generateWorld() {
        int roomAmount = random.nextInt(20) + 20;
        ArrayList<Room> rooms = placeRooms(roomAmount);
        for (Room r: rooms) {
            r.fillRoom(world);
        }
        generateRandomPaths(rooms);
        addWall();
        return world;
    }

    // generates a random room
    private Room generateRoom() {
        int h = random.nextInt(5) + 2;
        int w = random.nextInt(5) + 2;
        int x = random.nextInt(width - w - 2) + 1;
        int y = random.nextInt(height - h - 2) + 1;
        return new Room(x, y, h ,w);
    }

    private boolean overlapped(Room r) {
        for (Room room: this.curRooms) {
            for (Position pos1: room.allPosOfRoom) {
                for (Position pos2: r.allPosOfRoom) {
                    if (pos1.equals(pos2, 5)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<Room> placeRooms(int limit) {
        ArrayList<Room> rooms = new ArrayList<>();
        int acc = 0;
        while (acc != limit) {
            Room room = generateRoom();
            if (!overlapped(room)) {
                rooms.add(room);
                this.curRooms.add(room);
                acc += 1;
            }
        }
        return rooms;
    }

    // create a method that generates random paths, within the rooms
    private void generateRandomPaths(ArrayList<Room> rooms) {
        // Shuffle the list of rooms to visit them in a random order
        Room[] roomsArray = rooms.toArray(new Room[0]);
        RandomUtils.shuffle(this.random, roomsArray);

        // Use DFS to visit all rooms and generate paths between them
        for (Room room : roomsArray) {
            if (!room.visited) {
                dfs(room);
            }
        }
    }

    private void dfs(Room room) {
        room.visited = true;

        // Randomly shuffle the neighboring rooms to visit them in a random order
        ArrayList<Room> neighbors = room.getNeighbors(this.curRooms);
        Room[] neighborsArray = neighbors.toArray(new Room[0]);
        RandomUtils.shuffle(this.random, neighborsArray);

        for (Room neighbor : neighbors) {
            if (!neighbor.visited) {
                ArrayList<Position> pathBlocks = room.generatePath(neighbor, this.random);
                drawPath(pathBlocks);
                dfs(neighbor);
            }
        }
    }

    private void drawPath(ArrayList<Position> pathBlocks) {
        for (Position pos: pathBlocks) {
            int x = pos.x;
            int y = pos.y;
            world[x][y] = Tileset.FLOOR;
        }
    }

    private void addWall() {
        ArrayList<Position> list = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (world[x][y] == Tileset.FLOOR) {
                    list = addWallAround(x, y);
                }
            }
        }
        addLockDoor(list);
    }

    private void addLockDoor(ArrayList<Position> list) {
        int r = this.random.nextInt(list.size());
        Position pos = list.get(r);
        if (isNextToFloor(pos)) {
            world[pos.x][pos.y] = Tileset.LOCKED_DOOR;
            return;
        }
        addLockDoor(list);
    }

    private boolean isNextToFloor(Position pos) {
        return world[pos.x - 1][pos.y] == Tileset.FLOOR || world[pos.x][pos.y - 1] == Tileset.FLOOR || world[pos.x + 1][pos.y] == Tileset.FLOOR || world[pos.x][pos.y + 1] == Tileset.FLOOR;
    }
    private ArrayList<Position> addWallAround(int x, int y) {
        ArrayList<Position> walls = new ArrayList<>();
        for (int i = x - 1; i < x + 2; i++) {
            for (int j = y - 1; j < y + 2; j++) {
                if (world[i][j] == Tileset.FLOOR) {
                    continue;
                }
                walls.add(new Position(i, j));
                TETile colored = TETile.colorVariant(Tileset.WALL, 50, 0, 0, this.random);
                world[i][j] = colored;
            }
        }
        return walls;
    }

}
