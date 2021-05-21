package RabinSignature;


import java.awt.font.ShapeGraphicAttribute;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class Rabin {

    public boolean checkPrime(int p, int q) { // check if p and q are prime and 3mod4
        if (p > 1 && q > 1) {
            for (int i = 2; i < p; i++) {
                if (p % i == 0) {
                    System.out.println("p and q must be prime numbers.");
                    return false;
                }
            }
            for (int i = 2; i < q; i++) {
                if (q % i == 0) {
                    System.out.println("p and q must be prime numbers.");
                    return false;
                }
            }
        }

        if ((p % 4 == 3) && (q % 4 == 3)) {
            return true;
        } else {
            System.out.println("p and q must be equal to 3mod4");
            return false;
        }
    }


    public int H(int m, int u){//hash function, get message as integer, and u generated random number.
        int c = m+u;
        return c;
    }


    public ArrayList<Integer> rabinSignature(byte[] m, int p, int q){//rabin signature algorithm
        int u = 0;
        int sig = 0;
        while(true){
            //convert byte to integer number.
            byte[] bytes = m;
            for (int i = 0; i < bytes.length / 2; i++) {
                byte temp = bytes[i];
                bytes[i] = bytes[bytes.length - i - 1];
                bytes[bytes.length - i - 1] = temp;
            }
            int mInteger = new BigInteger(1, bytes).intValue();
            System.out.println("Converted message to Integer = "+ mInteger);

            //generate U.
            //u = new Random().nextInt(990) +10;
            u = new Random().nextInt(990) +10;
            System.out.println("generated U is " + u );

            // calculate x using hash function.
            int x = H(mInteger,u);
            System.out.println("hash function result = " + x);

            int power1 = 0, power2 = 0;
            power1 = (p - 1) / 2;
            power2 = (q - 1) / 2;
            System.out.println("pow 1 = " + (Math.pow(x, power1) % p));
            System.out.println("pow 2 = " + (Math.pow(x, power2) % q));
            if ((((Math.pow(x, power1) % p) == 1) && (((Math.pow(x, power2) % q) == 1)))) {
                sig = 0;
                power1 = q - 2;
                power2 = (q + 1) / 4;
                sig = ((int) Math.pow(p, power1) % q) * p * (int) (Math.pow(x, power2) % q); //sig = x
                power1 = p - 2;
                power2 = (p + 1) / 4;
                sig = ((int) Math.pow(q, power1) % p) * q * (int) (Math.pow(x, power2) % p) + sig; //sig = x
                sig = sig % (p * q);
                System.out.println("sig = " + sig + ", mod n is " + sig % (p * q));

                //S computes one value x that solves the equation x^2 ==h(m,u) mod n

                if ((x % (p * q)) == ((sig * sig) % (p * q))) {
                    System.out.println("x is modulo n ");
                    break;
                } else {
                    System.out.println("x is not a square modulo n , need to choose new U value.");
                }
            } else {
                System.out.println("need to choose a new u.");
            }
        }

        System.out.println("the signature is : (message =" +sig + ", u= " + u+").");
        ArrayList<Integer> b = new ArrayList<>();
        b.add(u);
        b.add(sig);
        return b;
    }





    public static void main(String[] args) {

        int p = 11;
        int q = 7;
        Rabin rabin = new Rabin();

        if(!rabin.checkPrime(p,q)) {
            p = 31;
            q = 23;
        }
        String message = "hi ";
        byte[] msg = message.getBytes(StandardCharsets.UTF_8);

        int nRabin = p*q;
        ArrayList<Integer> arrayList = rabin.rabinSignature(msg,p,q); // call signature algorithm.
        int u = arrayList.get(0);//get u
        int x = arrayList.get(1);//get x = sig

        //verification
        //convert byte to integer number.
        byte[] bytes = msg;
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        int mInteger = new BigInteger(1, bytes).intValue();
        int sig2 = x*x;
        System.out.println("sig*sig = " + sig2 % (nRabin));
        System.out.println("H(m,u) = "+ rabin.H(mInteger,u) % (nRabin));
        boolean isVerified = ((rabin.H(mInteger, u) % (p*q)) == (sig2 % (nRabin)));
        System.out.println("the verified status is: "+ (isVerified == true ? "Verified" : "Not Verified"));
    }

}
