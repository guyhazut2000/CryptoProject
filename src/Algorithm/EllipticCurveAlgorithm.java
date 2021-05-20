package Algorithm;


import java.util.ArrayList;

public class EllipticCurveAlgorithm {
    public static final long P = 11;
    public static final long A = 1;
    public static final long B = 6;
    public final Point base = null;
    private final long NULL_VALUE = -1;

    private ArrayList<Point> field;
    private long[] poweredByTwo;

    /**
     * Elliptic Curve Algorithm with the function of y^2 = x^3 + Ax + B mod P
     */

    public EllipticCurveAlgorithm() {
    }
}
