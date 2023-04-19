package byog.Core;

// a simple class that creates a position containing x and y
public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean equals(Position otherPos, int epsilon) {
        int deltaX = this.x - otherPos.x;
        int deltaY = this.y - otherPos.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= epsilon;
    }

    public boolean near(Position otherPos) {
        int deltaX = this.x - otherPos.x;
        int deltaY = this.y - otherPos.y;
        return Math.sqrt(deltaX * deltaX + deltaY * deltaY) <= 20;
    }
}
