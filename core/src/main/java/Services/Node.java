package Services;

public class Node implements Comparable<Node> {
    public int x, y;
    public int gCost;
    public int hCost;
    public Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getFCost() {
        return gCost + hCost;
    }
    @Override
    public int compareTo(Node other) {
        int compare = Integer.compare(this.getFCost(), other.getFCost());
        if (compare == 0) {
            compare = Integer.compare(this.hCost, other.hCost);
        }
        return compare;
    }
}
