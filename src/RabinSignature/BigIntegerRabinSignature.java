package RabinSignature;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

public class BigIntegerRabinSignature {

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

    public BigInteger H(BigInteger m, BigInteger u){//hash function, return m+u.
        BigInteger c = m.add(u);
        return c;
    }

    public ArrayList<BigInteger> rabinSignature(byte[] m, int p, int q){//rabin signature algorithm
        BigInteger u = new BigInteger("0");
        BigInteger sig = new BigInteger("0");

        while(true){
            byte[] bytes =m.clone();
            for (int i = 0; i < bytes.length / 2; i++) {
                byte temp = bytes[i];
                bytes[i] = bytes[bytes.length - i - 1];
                bytes[bytes.length - i - 1] = temp;
            }
            BigInteger mInteger = new BigInteger(1, bytes);
            System.out.println("Converted message in BigInteger value = "+ mInteger);

            //generate random U value.
            u = new BigInteger(new Random().nextInt(990) +10 +"" );
            u = new BigInteger("927");
            System.out.println("generated U is " + u );

            // calculate x using hash function.
            BigInteger x = H(mInteger,u); // x = mInteger + u.
            System.out.println("hash function result = " + x);
            // check if pow(x,int((p-1)/2),p) == 1 and pow(x,int((q-1)/2),q) == 1:  if not generate new u value.    (python code)
            int power1 = 0, power2 = 0;
            power1 = (p - 1) / 2;
            power2 = (q - 1) / 2;
            //System.out.println("pow 1 = " + (Math.pow(x.intValue(), power1) % p));
            //System.out.println("pow 2 = " + (Math.pow(x.intValue(), power2) % q));
            //BigInteger res1= new BigInteger("" +(int)((Math.pow(x.intValue(), power1) % p)));
            BigInteger res1= x.pow(power1).mod(BigInteger.valueOf(p));
            //BigInteger res2= new BigInteger(""+ (int)((Math.pow(x.intValue(), power2) % q)));
            BigInteger res2= x.pow(power2).mod(BigInteger.valueOf(q));
            System.out.println("res1 = " + res1 + "\nres2 = " +res2);

            if ( res1.intValue() ==  1 && res2.intValue() ==1 ) {
                power1 = q - 2;
                power2 = (q + 1) / 4;
                //sig = (new BigInteger("" + ((int) Math.pow(p, power1) % q) * p * (int) (Math.pow(x.intValue(), power2) % q))); //sig = x
                sig = new BigInteger("" + ((int) Math.pow(p, power1) % q) * p * x.pow(power2).mod(BigInteger.valueOf(q)).intValue()); //sig = x
                power1 = p - 2;
                power2 = (p + 1) / 4;
                //sig = ( new BigInteger("" + ((int) Math.pow(q, power1) % p) * q * (int) (Math.pow(x.intValue(), power2) % p) + sig)); //sig = x
                sig =  new BigInteger("" + ((int) Math.pow(q, power1) % p) * q * ((x.pow(power2).mod(BigInteger.valueOf(p))).add(sig)).intValue()).mod(BigInteger.valueOf(p*q)); //sig = x
                //sig= sig.mod(BigInteger.valueOf((p*q)));
                System.out.println("sig = " + sig + ", mod n = " + (p*q));

                //S computes one value x that solves the equation x^2 ==h(m,u) mod n
                int tmp1 = x.mod(BigInteger.valueOf(p * q)).intValue();
                int tmp2=((sig.multiply(sig)).mod(BigInteger.valueOf(p*q))).intValue();
                System.out.println("tmp1 = " + tmp1 + ", tmp2 = " + tmp2);

                if (tmp1 == tmp2 ) {
                    System.out.println("x is modulo n ");
                    break;
                } else {
                    System.out.println("x is not a square modulo n , need to choose new U value.");
                }
            } else {
                System.out.println("need to generate a new u value.");
            }
        }
        System.out.println("the signature is : (message =" +sig + ", u= " + u+").");
        ArrayList<BigInteger> b = new ArrayList<>();
        b.add(u);
        b.add(sig);
        return b;
    }





    public static void main(String[] args) {
        int p = 11;
        int q = 7;
        BigIntegerRabinSignature rabin = new BigIntegerRabinSignature();

        String message = "abdfgdfg";
        byte[] msg = message.getBytes(StandardCharsets.UTF_8);

        int nRabin = p*q;
        ArrayList<BigInteger> arrayList = rabin.rabinSignature(msg.clone(),p,q); // call signature algorithm.
        BigInteger u = arrayList.get(0);//get u
        BigInteger x = arrayList.get(1);//get x = sig

        //verification
        //convert byte to integer number.
        byte[] bytes = msg.clone();
        for (int i = 0; i < bytes.length / 2; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        BigInteger mInteger = new BigInteger(1, bytes);
        //BigInteger sig2 = x.multiply(x).mod(BigInteger.valueOf(nRabin));
        BigInteger sig2 = x.multiply(x);
        System.out.println("sig*sig = " + sig2);
        System.out.println("Hash = " + rabin.H(mInteger, u).mod(BigInteger.valueOf(nRabin)));
        boolean isVerified = ((rabin.H(mInteger, u).mod (BigInteger.valueOf(nRabin))) == (sig2.mod(BigInteger.valueOf(nRabin))));
        System.out.println("the verified status is: "+ (isVerified == true ? "Verified" : "Not Verified"));
    }

}
