import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;
    public static final int TILE_SIZE = 256;
    private final Map<Integer, Double> depthToLonDPP;
    private final Map<Integer, Double> depthToLatDPP;
    public Rasterer() {
        // YOUR CODE HERE
        depthToLonDPP = new HashMap<>();
        depthToLatDPP = new HashMap<>();
        double oriLonDPP = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        double oriLatDPP = (ROOT_ULLAT - ROOT_LRLAT) / TILE_SIZE;
        for (int i = 0; i < 8; i++) {
            depthToLonDPP.put(i, oriLonDPP / Math.pow(2, i));
            depthToLatDPP.put(i, oriLatDPP / Math.pow(2, i));
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // extract all elements from the params
        double lrlon = params.get("lrlon");
        double ullon = params.get("ullon");
        double lrlat = params.get("lrlat");
        double ullat = params.get("ullat");
        double width = params.get("w");
        Map<String, Object> results = new HashMap<>();
        if (ullon > lrlon || ullat < lrlat) {
            results.put("depth", 0);
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            results.put("render_grid", 0);
            results.put("query_success", false);
            return results;
        }
        else {
            // calculating each fields for return
            double queryLonDPP = (lrlon - ullon) / width;
            int depth = getDepth(queryLonDPP);
            double raster_ul_lon = calcRasterLeftLon(depth, ullon);
            double raster_ul_lat = calcRasterUpperLat(depth, ullat);
            double raster_lr_lon = calcRasterRightLon(depth, lrlon);
            double raster_lr_lat = calcRasterLowerLat(depth, lrlat);
            int minX = minX(depth, ullon);
            int maxX = maxX(depth, lrlon);
            int minY = minY(depth, ullat);
            int maxY = maxY(depth, lrlat);
            String[][] renderGrid = getRenderGrid(depth, minX - 1, minY - 1, maxX, maxY);
            results.put("depth", depth);
            results.put("raster_ul_lon", raster_ul_lon);
            results.put("raster_ul_lat", raster_ul_lat);
            results.put("raster_lr_lon", raster_lr_lon);
            results.put("raster_lr_lat", raster_lr_lat);
            results.put("render_grid", renderGrid);
            results.put("query_success", true);
            return results;
        }
    }

    private int minX(int depth, double query_ul_lon) {
        int minX = 0;
        double baseX = ROOT_ULLON;
        while (baseX < query_ul_lon) {
            baseX += depthToLonDPP.get(depth) * TILE_SIZE;
            minX++;
        }
        return minX;
    }

    private int maxX(int depth, double query_lr_lon) {
        int maxX = 0;
        double baseX = ROOT_ULLON;
        while (baseX < query_lr_lon) {
            baseX += depthToLonDPP.get(depth) * TILE_SIZE;
            maxX++;
        }
        return maxX;
    }

    private int minY(int depth, double query_ul_lat) {
        int minY = 0;
        double baseY = ROOT_ULLAT;
        while (baseY > query_ul_lat) {
            baseY -= depthToLatDPP.get(depth) * TILE_SIZE;
            minY++;
        }
        return minY;
    }

    private int maxY(int depth, double query_lr_lat) {
        int maxY = 0;
        double baseY = ROOT_ULLAT;
        while (baseY > query_lr_lat) {
            baseY -= depthToLatDPP.get(depth) * TILE_SIZE;
            maxY++;
        }
        return maxY;
    }

    private String[][] getRenderGrid(int depth, int minX, int minY, int maxX, int maxY) {
        String[][] renderGrid = new String[maxY - minY][maxX - minX];
        // in this case we've got the maxX and the maxY.
        // so we know we need the images we need!
        for (int i = 0; i < maxY - minY; i++) {
            for (int j = 0; j < maxX - minX; j++) {
                renderGrid[i][j] = "d" + depth + "_x" + (j + minX) + "_y" + (i + minY) + ".png";
            }
        }
        return renderGrid;
    }
    private double calcRasterLeftLon(int depth, double queryLeftLon) {
        double leftLon = ROOT_ULLON;
        double lonDPP = depthToLonDPP.get(depth);
        while (leftLon + (lonDPP * TILE_SIZE) < queryLeftLon) {
            leftLon += lonDPP * TILE_SIZE;
        }
        return leftLon;
    }

    private double calcRasterLowerLat(int depth, double queryLowerLat) {
        double lowerLat = ROOT_LRLAT;
        double latDPP = depthToLatDPP.get(depth);
        while (lowerLat + (latDPP * TILE_SIZE) < queryLowerLat) {
            lowerLat += latDPP * TILE_SIZE;
        }
        return lowerLat;
    }

    private double calcRasterRightLon(int depth, double queryRightLon) {
        double rightLon = ROOT_LRLON;
        double lonDPP = depthToLonDPP.get(depth);
        while (rightLon - (lonDPP * TILE_SIZE) > queryRightLon) {
            rightLon -= lonDPP * TILE_SIZE;
        }
        return rightLon;
    }

    private double calcRasterUpperLat(int depth, double queryUpperLat) {
        double upperLat = ROOT_ULLAT;
        double latDPP = depthToLatDPP.get(depth);
        while (upperLat - (latDPP * TILE_SIZE) > queryUpperLat) {
            upperLat -= latDPP * TILE_SIZE;
        }
        return upperLat;
    }

    private static int getDepth(double lonDpp) {
        int depth = 0;
        double imgLonDpp = (ROOT_LRLON - ROOT_ULLON) / TILE_SIZE;
        while (imgLonDpp > lonDpp) {
            if (depth == 7) {
                break;
            }
            imgLonDpp /= 2.0;
            depth++;
        }
        return depth;
    }
}
