package Tests;

import Algorithms.Mars;

import java.nio.charset.StandardCharsets;

public class Main {
    /*
    Project = An Application for secure email exchange:
    encryption-decryption with Mars in OFB mode,
    secret key delivery with EC ELGamal
    + Rabin signature.
     */
    public static void main(String[] args) {

        byte[] plainText = "This is an exmaple.".getBytes(StandardCharsets.UTF_8);//convert string to byte[].
        byte[] key = "the Key".getBytes(StandardCharsets.UTF_8);
        System.out.println("plain text  = " + new String(plainText) + "\n" + "key  = " + new String(key));
        System.out.println("plain text in bytes = " + plainText + "\n" + "key in bytes = " + key);
        System.out.println("**************************************************");
        byte[] ciper = Mars.encrypt(plainText, key);//encrypt plainText with the key.
        System.out.println("Cipher text = " + new String(ciper));
        System.out.println("Cipher text in bytes = " + ciper);
        byte[] decryption = Mars.decrypt(ciper, key);//decrypt the cipher text with the key.
        String decryptedText = new String(decryption);//convert byte[] to String.
        System.out.println("Original text = " + decryptedText);


    }
}
