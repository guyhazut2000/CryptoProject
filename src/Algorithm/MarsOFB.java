package Algorithm;

import java.util.Random;


public class MarsOFB {
/*
MARS has a 128-bit block size.
 */
    static final int MARS_KEY_SIZE = 128;
    public static byte[] iv = getRandomBytes(112);


    public static byte[] getRandomBytes(int size){
        Random rd = new Random();
        byte[] arr = new byte[size];
        rd.nextBytes(arr);
        return arr;
    }

    public static byte[] xor(byte[] arr1,byte[] arr2) throws Exception {
        if(arr1.length != arr2.length)
            throw new Exception("arr1 and arr2 have different length's. Can't perform xor operation.\nsizes:" +arr1.length + ", " + arr2.length);

        byte[] arr3 = new byte[arr1.length];

        int i = 0;
        for(byte b : arr1)
            arr3[i] = (byte) (arr1[i]^arr2[i]);

        return arr3;
    }

    public static byte[] ofbEncrypt(byte[] in,byte[] key) throws Exception {

        Mars cipher = new Mars();//our mars encryption algorithm.

        /*
        initialized byte[][] to save the input byte.
        copy all input bytes into blocks of 128 bytes.
         */
        int numOfBlocks = in.length / MARS_KEY_SIZE;
        byte[][] blocks = new byte[numOfBlocks][MARS_KEY_SIZE];

        for(int i = 0; i< numOfBlocks;i++){
            for(int j=0;j<MARS_KEY_SIZE && (i*MARS_KEY_SIZE) + j < in.length;j++){
                    blocks[i][j] = in[(i*MARS_KEY_SIZE) + j];
            }
        }
        if(blocks.length / MARS_KEY_SIZE != 0 ){//padd blocks if needed.

        }
        /*
        start the OFB process.
         */
        byte[][] cipherTextChunks = new byte[numOfBlocks][MARS_KEY_SIZE];//create cipher byte[][] with size of total blocks.
        byte[] iv = MarsOFB.iv;
        byte[] originalIV = iv;

        for(int i = 0; i< numOfBlocks;i++){
            byte[] tmp = Mars.encrypt(iv, key); //encrypt the key and iv.
            byte[] block = blocks[i];
            byte[] res = new byte[128];
            res = xor(tmp,block);//xor operation using ofb mode between plaintext and encrypt(iv,key).

            cipherTextChunks[i] = res;//save cipher block i result.
            iv = tmp;//new iv is the result of encrypt(old iv, key).
        }

        byte[] cipherText = new byte[numOfBlocks*MARS_KEY_SIZE]; // the return cipher Text.

        for(int i = 0; i< numOfBlocks;i++){
            for(int j=0;j<MARS_KEY_SIZE ;j++){
                cipherText[i+j] = cipherTextChunks[i][j];
            }
        }

       return cipherText;
    }


    public static byte[] ofbDecrypt(byte[] in,byte[] key) throws Exception {

        Mars cipher = new Mars();//our mars encryption algorithm.

        /*
        initialized byte[][] to save the input byte.
        copy all input bytes into blocks of 128 bytes.
         */
        int numOfBlocks = in.length / MARS_KEY_SIZE;
        byte[][] cipherBlocks = new byte[numOfBlocks][MARS_KEY_SIZE];

        for(int i = 0; i< numOfBlocks;i++){
            for(int j=0;j<MARS_KEY_SIZE && (i*MARS_KEY_SIZE) + j < in.length;j++){
                cipherBlocks[i][j] = in[(i*MARS_KEY_SIZE) + j];
            }
        }
        /*
        start the OFB process.
         */
        byte[][] plainTextChunks = new byte[numOfBlocks][MARS_KEY_SIZE];//create plain text byte[][] with size of total blocks.
        byte[] iv = MarsOFB.iv;
        byte[] originalIV = iv;

        for(int i = 0; i< numOfBlocks;i++){
            byte[] tmp = Mars.encrypt(iv, key); //encrypt the key and iv.
            byte[] block = cipherBlocks[i];
            byte[] res = new byte[128];
            res = xor(tmp,block);//xor operation using ofb mode between plaintext and encrypt(iv,key).

            plainTextChunks[i] = res;//save cipher block i result.
            iv = tmp;//new iv is the result of encrypt(old iv, key).
        }

        byte[] plainText = new byte[numOfBlocks*MARS_KEY_SIZE]; // the return cipher Text.

        for(int i = 0; i< numOfBlocks;i++){
            for(int j=0;j<MARS_KEY_SIZE ;j++){
                plainText[i+j] = plainTextChunks[i][j];
            }
        }

        return plainText;

    }
}
