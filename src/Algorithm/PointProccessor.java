/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Algorithm;

import ElGamal.Pair;


/**
 * @author Vincent TC
 * @author William Sentosa
 */
public class PointProccessor {

    public Point doublePoint(Point P){
        Point result = new Point();

        if (P.getY() == 0){
            result.setX(0);
            result.setY(0);
        } else {
            long lambda, inv, xr, yr;
            long p = EllipticCurveAlgorithm.P;
            long a = EllipticCurveAlgorithm.A;

            lambda = (((3*P.getX()*P.getX()) % p) + a) % p;
            inv = getInverse(2*P.getY(), p);
            lambda *= inv;
            lambda %= p;

            xr = (((lambda*lambda) % p) - (2*P.getX() % p) + p) % p;
            yr = (((lambda*(P.getX() - xr)) % p) - P.getY() + p) % p;

            if (yr < 0){
                yr += p;
            }

            result.setX(xr);
            result.setY(yr);
        }

        return result;
    }

    public Point multiply(long n, Point P) {
        Point result = new Point();
        Point base = new Point(P.getX(), P.getY());

        String binary = Long.toBinaryString(n);
        for (int i=binary.length()-1; i>=0; i--) {
            if (binary.charAt(i) == '1') {
                if (i == binary.length()-1){
                    result = base;
                } else {
                    result = add(result, base);
                }
            }
            base = doublePoint(base);
        }

        return result;
    }

    public long getInverse(long n, long m) {
        while (n > m) {
            n -= m;
        }

        while (n < 0) {
            n += m;
        }

        long gq = m, gy = 0;
        long lq = n, ly = 1;
        long tq = lq, ty = ly;
        while (lq != 1) {
            long d = gq/lq;
            lq = gq - d*lq; ly = gy - d*ly;
            gq = tq; gy = ty;
            tq = lq; ty = ly;
        }
        if (ly < 0) {
            ly += m;
        }
        return ly;
    }

    public Point add(Point p1, Point p2) {
        Point result = new Point();
        long p = EllipticCurveAlgorithm.P;

        if (p1.getX() == 0 && p1.getY() == 0){
            result.setX(p2.getX());
            result.setY(p2.getY());
        } else if (p2.getX() == 0 && p2.getY() == 0){
            result.setX(p1.getX());
            result.setY(p1.getY());
        } else if (p1.getY() == -p2.getY()){
            result.setX(0);
            result.setY(0);
        } else if (p1.getX() - p2.getX() == 0){
            result.setX(Long.MAX_VALUE);
            result.setY(Long.MAX_VALUE);
        } else {
            long lambda, xr, yr, inv;

            lambda = (p1.getY() - p2.getY()) % p;
            inv = getInverse(p1.getX() - p2.getX(), p);
            lambda *= inv;
            lambda %= p;

            xr = (((lambda*lambda) % p) - p1.getX() - p2.getX() + 2*p) % p;
            yr = (((lambda*(p1.getX()-xr)) % p) - p1.getY() + 2*p) % p;

            result.setX(xr);
            result.setY(yr);
        }

        return result;
    }

    public Point minus(Point p1, Point p2){
        Point temp = new Point();
        Point res = new Point();

        temp.setX(p2.getX());
        temp.setY(-p2.getY());

        res = add(p1, temp);

        return res;
    }

    public Pair<Point, Point> encrypt(Point pm, Point pub, Point base, long a) { // plain text , bob public key , base point

        Pair<Point, Point> Pc = null;
        long p = EllipticCurveAlgorithm.P;

        long k = a;//alice private key

        Point px = new Point();//y1
        Point py = new Point();//y2

        px = multiply(k, base); // y1 = alice private key * base point .
        py = add(pm, multiply(k,pub)); // y2 = ( x=pm= plainText ) + (alice private key * bob public key )

        Pc = new Pair(px, py); //y1, y2

        return Pc;
    }

    public Point decrypt(Pair<Point, Point> Pc, long pri, Point base) { // this is how bob decrypts y1,y2 Pair from alice.
        Point temp = new Point();
        Point pm = new Point();

        temp = multiply(pri, Pc.getKey());// y1* bob private key

        pm = minus(Pc.getValue(),temp);//y2 - temp

        return pm;
    }

    public static void main(String args[]) {

        PointProccessor pp = new PointProccessor();
        EllipticCurveAlgorithm algorithm = new EllipticCurveAlgorithm();
        long a = 3;
        long b = 7;

        Point B = new Point(2,7);
        Point x = new Point(10,9);


        Point pa = pp.multiply(a,B);
        Point pb = pp.multiply(b,B);

        System.out.println("Alice Private key =" + a);
        System.out.println("Alice public key =" + pa);
        System.out.println("Bob Private key =" + b);
        System.out.println("Bob public key =" + pb);

        System.out.println("plain text = " + x );
        Pair<Point, Point> AliceEncryption = pp.encrypt(x,pb,B, a);

        System.out.println("encryption = "+ AliceEncryption.getKey().toString() + " , "+ AliceEncryption.getValue().toString());

        System.out.println("\nnow bob will decrypt y1,y2 ");
        System.out.println("original plain text is = " + pp.decrypt(AliceEncryption,b,B));

    }
}