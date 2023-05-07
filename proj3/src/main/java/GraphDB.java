import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */

    class Node {
        final long id;
        final double lat;
        final double lon;

        public Node(long id, double lon, double lat) {
            this.id = id;
            this.lat = lat;
            this.lon = lon;
        }

        public long getId() {
            return id;
        }
        public double getLon() {
            return lon;
        }
        public double getLat() {
            return lat;
        }

        @Override
        public String toString() {
            return Long.toString(id);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Node other = (Node) obj;
            return this.id == other.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    class Edge {
        // this class stores outgoing edges of nodes
        HashSet<Node> edges;
        HashSet<Long> edgesInLong;
        public Edge() {
            this.edges = new HashSet<>();
            this.edgesInLong = new HashSet<>();
        }
        @Override
        public String toString() {
            return edges.toString();
        }
    }

    private final Map<Long, Node> verticesToNode = new HashMap<>();
    private final Map<Node, Edge> adjacencyList = new HashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        Set<Node> nodesToRemove = new HashSet<>();

        for (Map.Entry<Node, Edge> entry : adjacencyList.entrySet()) {
            if (entry.getValue().edges.isEmpty()) {
                nodesToRemove.add(entry.getKey());
            }
        }

        for (Node node : nodesToRemove) {
            adjacencyList.remove(node);
        }
    }


    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        ArrayList<Long> vertices = new ArrayList<>();
        for (Node n: adjacencyList.keySet()) {
            vertices.add(n.getId());
        }
        return vertices;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node n = this.verticesToNode.get(v);
        return this.adjacencyList.get(n).edgesInLong;
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestId = 0;
        double maxDist = 10000000;
        for (long id: vertices()) {
            double distance = Math.abs(distance(this.verticesToNode.get(id).getLon(), this.verticesToNode.get(id).getLat(), lon, lat));
            if (distance < maxDist) {
                maxDist = distance;
                closestId = this.verticesToNode.get(id).getId();
            }
        }
        return closestId;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return this.verticesToNode.get(v).getLon();
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return this.verticesToNode.get(v).getLat();
    }

    /**
     * Insert the given Node into the graph
     * @param id The id of the vertex.
     * @param lon The longitude of the vertex
     * @param lat The latitude of the vertex.
     */
    void addNode(long id, double lon, double lat) {
        Node node = new Node(id, lon, lat);
        this.verticesToNode.put(id, node);
        this.adjacencyList.put(node, new Edge());
    }

    void addEdge(long id, ArrayList<Long> ids) {
        Node node = this.verticesToNode.get(id);
        if (ids.size() == 1) {
            Node n = this.verticesToNode.get(ids.get(0));
            this.adjacencyList.get(node).edges.add(n);
            this.adjacencyList.get(n).edgesInLong.add(node.getId());
        } else if (ids.size() == 2) {
            Node n1 = this.verticesToNode.get(ids.get(0));
            Node n2 = this.verticesToNode.get(ids.get(1));
            this.adjacencyList.get(node).edges.add(n1);
            this.adjacencyList.get(n1).edgesInLong.add(node.getId());
            this.adjacencyList.get(node).edges.add(n2);
            this.adjacencyList.get(n2).edgesInLong.add(node.getId());
        }
    }

    Node getNode(long id) {
        return this.verticesToNode.get(id);
    }

    void printConnections() {
        for (Node node : adjacencyList.keySet()) {
            System.out.println("Node: " + node + ": " + adjacencyList.get(node));
        }
    }
}
