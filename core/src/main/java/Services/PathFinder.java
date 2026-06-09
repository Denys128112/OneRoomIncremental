package Services;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class PathFinder {
    public static List<Vector2> findPath(int[][] map, int startX, int startY, int targetX, int targetY) {
        int width = map.length;
        int height = map[0].length;
        PriorityQueue<Node> openList = new PriorityQueue<>();
        boolean[][] closedList = new boolean[width][height];
        Node[][] allNodes = new Node[width][height];
        Node startNode = new Node(startX, startY);
        startNode.gCost = 0;
        startNode.hCost = Math.abs(targetX - startX) + Math.abs(targetY - startY);
        allNodes[startX][startY] = startNode;
        openList.add(startNode);
        int[][]directions = {{-1,0},{1,0},{0,-1},{0,1}};
        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            int x = currentNode.x;
            int y = currentNode.y;
            closedList[x][y] = true;
            if (x == targetX && y == targetY) {
                return buildPath(startNode,currentNode);
            }
            for (int[] dir:directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (nx < 0 || nx >= width || ny < 0 || ny >= height || closedList[nx][ny] || map[nx][ny] != 0) {
                    continue;
                }
                int gCost = currentNode.gCost + 1;
                Node neighbor = allNodes[nx][ny];
                if (neighbor == null) {
                    neighbor = new Node(nx, ny);
                    allNodes[nx][ny] = neighbor;
                }
                if (gCost < neighbor.gCost || !openList.contains(neighbor)) {
                    neighbor.gCost = gCost;
                    neighbor.hCost = Math.abs(targetX - nx) + Math.abs(targetY - ny);
                    neighbor.parent = currentNode;
                    if (!openList.contains(neighbor)) {
                        openList.add(neighbor);
                    }
                }
            }
        }
        return null;
    }
    private static List<Vector2> buildPath(Node startNode, Node currentNode) {
        List<Vector2> path= new ArrayList<>();
        while (currentNode != startNode) {
            path.add(new Vector2(currentNode.x, currentNode.y));
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
