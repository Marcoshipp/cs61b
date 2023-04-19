package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.Random;

public class Room {
    // x and y coordinate of the room
    public Position startingPos;
    public Position[] allPosOfRoom;
    public static ArrayList<Room> rooms;
    public boolean visited;
    public boolean neighbored;

    // height and width of the room
    public int height;
    public int width;

    public Room(int x, int y, int h, int w) {
        rooms = new ArrayList<>();
        this.startingPos = new Position(x, y);
        this.height = h;
        this.width = w;
        this.allPosOfRoom = new Position[h * w];
        this.visited = false;
        this.neighbored = false;
        int index = 0;
        for (int xPos = startingPos.x; xPos < startingPos.x + width; xPos++) {
            for (int yPos = startingPos.y; yPos < startingPos.y + height; yPos++) {
                this.allPosOfRoom[index] = new Position(xPos, yPos);
                index++;
            }
        }
        rooms.add(this);
    }

    public void fillRoom(TETile[][] world) {
        for (Position position : this.allPosOfRoom) {
            int x = position.x;
            int y = position.y;
            world[x][y] = Tileset.FLOOR;
        }
    }

    public ArrayList<Room> getNeighbors(ArrayList<Room> rooms) {
        ArrayList<Room> neighbors = new ArrayList<>();
        for (Room r: rooms) {
            if (r.equals(this)) {
                continue;
            }
            if (this.startingPos.near(r.startingPos)) {
                neighbors.add(r);
            }
        }
        return neighbors;
    }

    public ArrayList<Position> generatePath(Room r, Random random) {
        // randomly select a block within "allPosOfRoom"
        this.visited = true;
        r.visited = true;
        ArrayList<Position> pathBlocks = new ArrayList<>();
        Position thisRandBlock = this.allPosOfRoom[0];
        Position rRandBlock = r.allPosOfRoom[0];
        int xPos = Math.min(thisRandBlock.x, rRandBlock.x);
        int yPos = Math.min(thisRandBlock.y, rRandBlock.y);
        for (; xPos < Math.max(thisRandBlock.x, rRandBlock.x); xPos++) {
            pathBlocks.add(new Position(xPos, yPos));
        }
        for (; yPos < Math.max(thisRandBlock.y, rRandBlock.y); yPos++) {
            pathBlocks.add(new Position(xPos, yPos));
        }
        return pathBlocks;
    }
}
