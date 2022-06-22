package com.peter.crypto;

import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Mar 26, 2013
 * Time: 10:15:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class DES
{
    /** The actual DES key. */
    private final long key;

    /** The 16 sub-keys used for each iteration. */
    private long[] subKeys;


    /** Create a new random DES key. */
    public DES()
    {
        key = (new Random()).nextLong();
        buildSubKeys();
    }


    /** Create a new DES key with the key bits from <code>key[0..7]</code>.
     * @param key key*/
    public DES(byte[] key)
    {
        this.key = makeLong(key,0,8);
        buildSubKeys();
    }


    /** Create a new DES key with the key bits from <code>key</code>.
     * @param key key*/
    public DES(long key)
    {
        this.key = key;
        buildSubKeys();
    }


    /** Return the key-bits for this key as an array of 8 bytes.
     * @return key*/
    public byte[] getKey()
    {
        byte[] a= new byte[8];
        writeBytes(key,a,0,8);
        return a;
    }


    /**
     * Transformation applied to key to drop parity
     * bits and permute remaining bits.
     */
    static private final byte[] PC1 = {
                                          56, 48, 40, 32, 24, 16,  8,
                                          0, 57, 49, 41, 33, 25, 17,
                                          9,  1, 58, 50, 42, 34, 26,
                                          18, 10,  2, 59, 51, 43, 35,
                                          62, 54, 46, 38, 30, 22, 14,
                                          6, 61, 53, 45, 37, 29, 21,
                                          13,  5, 60, 52, 44, 36, 28,
                                          20 ,12,  4, 27, 19, 11,  3
                                      };


    /**
     * Transformation used to select bits for the sub-keys
     * from the rotated key-halves.
     */
    static private final byte[] PC2 = {
                                           //shifted to bit-positions in a 64-bit integer
                                          21, 24, 18, 31,  8, 12,
                                          10, 35, 22, 13, 28, 17,
                                          30, 26, 19, 11, 33, 15,
                                          23, 14, 34, 27, 20,  9,
                                          48, 59, 38, 44, 54, 62,
                                          37, 47, 58, 52, 40, 55,
                                          51, 56, 46, 63, 41, 60,
                                          53, 49, 57, 43, 36, 39
                                      };


    /**
     * Create the array of sub-keys from the key.
     */
    private void buildSubKeys()
    {
        long K = pickBits(key,PC1);
        subKeys = new long[16];
        for (int i=0; i<16; i++) {
            if ((i==0) || (i==1) || (i==8) || (i==15)) {
                K = ((K<<1) & 0xffffffeffffffeL) | ((K>>>27) & 0x00000010000001L);
            } else {
                K = ((K<<2) & 0xffffffcffffffcL) | ((K>>>26) & 0x00000030000003L);
            }
            subKeys[i] = pickBits(K, PC2);
        }
    }

    /*
     The content of the S-box permutations has been shifted left to make
     the re-assembly of the S-substituted value faster. The P-box
     permutation was then applied to the S-boxes, so this does not need
     to be done at run-time.

     This modified version of the class was tested against the original
     on a few million messages and checked against the official NIST
     test-vectors.
   */
    /** shuffled, shifted and permuted S-box number 1 */
    private static final int[] S1 = {
                                        0x00808200, 0x00000000, 0x00008000, 0x00808202, 0x00808002, 0x00008202, 0x00000002, 0x00008000,
                                        0x00000200, 0x00808200, 0x00808202, 0x00000200, 0x00800202, 0x00808002, 0x00800000, 0x00000002,
                                        0x00000202, 0x00800200, 0x00800200, 0x00008200, 0x00008200, 0x00808000, 0x00808000, 0x00800202,
                                        0x00008002, 0x00800002, 0x00800002, 0x00008002, 0x00000000, 0x00000202, 0x00008202, 0x00800000,
                                        0x00008000, 0x00808202, 0x00000002, 0x00808000, 0x00808200, 0x00800000, 0x00800000, 0x00000200,
                                        0x00808002, 0x00008000, 0x00008200, 0x00800002, 0x00000200, 0x00000002, 0x00800202, 0x00008202,
                                        0x00808202, 0x00008002, 0x00808000, 0x00800202, 0x00800002, 0x00000202, 0x00008202, 0x00808200,
                                        0x00000202, 0x00800200, 0x00800200, 0x00000000, 0x00008002, 0x00008200, 0x00000000, 0x00808002
                                    };

    /** shuffled, shifted and permuted S-box number 2 */
    private static final int[] S2 = {
                                        0x40084010, 0x40004000, 0x00004000, 0x00084010, 0x00080000, 0x00000010, 0x40080010, 0x40004010,
                                        0x40000010, 0x40084010, 0x40084000, 0x40000000, 0x40004000, 0x00080000, 0x00000010, 0x40080010,
                                        0x00084000, 0x00080010, 0x40004010, 0x00000000, 0x40000000, 0x00004000, 0x00084010, 0x40080000,
                                        0x00080010, 0x40000010, 0x00000000, 0x00084000, 0x00004010, 0x40084000, 0x40080000, 0x00004010,
                                        0x00000000, 0x00084010, 0x40080010, 0x00080000, 0x40004010, 0x40080000, 0x40084000, 0x00004000,
                                        0x40080000, 0x40004000, 0x00000010, 0x40084010, 0x00084010, 0x00000010, 0x00004000, 0x40000000,
                                        0x00004010, 0x40084000, 0x00080000, 0x40000010, 0x00080010, 0x40004010, 0x40000010, 0x00080010,
                                        0x00084000, 0x00000000, 0x40004000, 0x00004010, 0x40000000, 0x40080010, 0x40084010, 0x00084000
                                    };

    /** shuffled, shifted and permuted S-box number 3 */
    private static final int[] S3 = {
                                        0x00000104, 0x04010100, 0x00000000, 0x04010004, 0x04000100, 0x00000000, 0x00010104, 0x04000100,
                                        0x00010004, 0x04000004, 0x04000004, 0x00010000, 0x04010104, 0x00010004, 0x04010000, 0x00000104,
                                        0x04000000, 0x00000004, 0x04010100, 0x00000100, 0x00010100, 0x04010000, 0x04010004, 0x00010104,
                                        0x04000104, 0x00010100, 0x00010000, 0x04000104, 0x00000004, 0x04010104, 0x00000100, 0x04000000,
                                        0x04010100, 0x04000000, 0x00010004, 0x00000104, 0x00010000, 0x04010100, 0x04000100, 0x00000000,
                                        0x00000100, 0x00010004, 0x04010104, 0x04000100, 0x04000004, 0x00000100, 0x00000000, 0x04010004,
                                        0x04000104, 0x00010000, 0x04000000, 0x04010104, 0x00000004, 0x00010104, 0x00010100, 0x04000004,
                                        0x04010000, 0x04000104, 0x00000104, 0x04010000, 0x00010104, 0x00000004, 0x04010004, 0x00010100
                                    };

    /** shuffled, shifted and permuted S-box number 4 */
    private static final int[] S4 = {
                                        0x80401000, 0x80001040, 0x80001040, 0x00000040, 0x00401040, 0x80400040, 0x80400000, 0x80001000,
                                        0x00000000, 0x00401000, 0x00401000, 0x80401040, 0x80000040, 0x00000000, 0x00400040, 0x80400000,
                                        0x80000000, 0x00001000, 0x00400000, 0x80401000, 0x00000040, 0x00400000, 0x80001000, 0x00001040,
                                        0x80400040, 0x80000000, 0x00001040, 0x00400040, 0x00001000, 0x00401040, 0x80401040, 0x80000040,
                                        0x00400040, 0x80400000, 0x00401000, 0x80401040, 0x80000040, 0x00000000, 0x00000000, 0x00401000,
                                        0x00001040, 0x00400040, 0x80400040, 0x80000000, 0x80401000, 0x80001040, 0x80001040, 0x00000040,
                                        0x80401040, 0x80000040, 0x80000000, 0x00001000, 0x80400000, 0x80001000, 0x00401040, 0x80400040,
                                        0x80001000, 0x00001040, 0x00400000, 0x80401000, 0x00000040, 0x00400000, 0x00001000, 0x00401040
                                    };

    /** shuffled, shifted and permuted S-box number 5 */
    private static final int[] S5 = {
                                        0x00000080, 0x01040080, 0x01040000, 0x21000080, 0x00040000, 0x00000080, 0x20000000, 0x01040000,
                                        0x20040080, 0x00040000, 0x01000080, 0x20040080, 0x21000080, 0x21040000, 0x00040080, 0x20000000,
                                        0x01000000, 0x20040000, 0x20040000, 0x00000000, 0x20000080, 0x21040080, 0x21040080, 0x01000080,
                                        0x21040000, 0x20000080, 0x00000000, 0x21000000, 0x01040080, 0x01000000, 0x21000000, 0x00040080,
                                        0x00040000, 0x21000080, 0x00000080, 0x01000000, 0x20000000, 0x01040000, 0x21000080, 0x20040080,
                                        0x01000080, 0x20000000, 0x21040000, 0x01040080, 0x20040080, 0x00000080, 0x01000000, 0x21040000,
                                        0x21040080, 0x00040080, 0x21000000, 0x21040080, 0x01040000, 0x00000000, 0x20040000, 0x21000000,
                                        0x00040080, 0x01000080, 0x20000080, 0x00040000, 0x00000000, 0x20040000, 0x01040080, 0x20000080
                                    };

    /** shuffled, shifted and permuted S-box number 6 */
    private static final int[] S6 = {
                                        0x10000008, 0x10200000, 0x00002000, 0x10202008, 0x10200000, 0x00000008, 0x10202008, 0x00200000,
                                        0x10002000, 0x00202008, 0x00200000, 0x10000008, 0x00200008, 0x10002000, 0x10000000, 0x00002008,
                                        0x00000000, 0x00200008, 0x10002008, 0x00002000, 0x00202000, 0x10002008, 0x00000008, 0x10200008,
                                        0x10200008, 0x00000000, 0x00202008, 0x10202000, 0x00002008, 0x00202000, 0x10202000, 0x10000000,
                                        0x10002000, 0x00000008, 0x10200008, 0x00202000, 0x10202008, 0x00200000, 0x00002008, 0x10000008,
                                        0x00200000, 0x10002000, 0x10000000, 0x00002008, 0x10000008, 0x10202008, 0x00202000, 0x10200000,
                                        0x00202008, 0x10202000, 0x00000000, 0x10200008, 0x00000008, 0x00002000, 0x10200000, 0x00202008,
                                        0x00002000, 0x00200008, 0x10002008, 0x00000000, 0x10202000, 0x10000000, 0x00200008, 0x10002008
                                    };

    /** shuffled, shifted and permuted S-box number 7 */
    private static final int[] S7 = {
                                        0x00100000, 0x02100001, 0x02000401, 0x00000000, 0x00000400, 0x02000401, 0x00100401, 0x02100400,
                                        0x02100401, 0x00100000, 0x00000000, 0x02000001, 0x00000001, 0x02000000, 0x02100001, 0x00000401,
                                        0x02000400, 0x00100401, 0x00100001, 0x02000400, 0x02000001, 0x02100000, 0x02100400, 0x00100001,
                                        0x02100000, 0x00000400, 0x00000401, 0x02100401, 0x00100400, 0x00000001, 0x02000000, 0x00100400,
                                        0x02000000, 0x00100400, 0x00100000, 0x02000401, 0x02000401, 0x02100001, 0x02100001, 0x00000001,
                                        0x00100001, 0x02000000, 0x02000400, 0x00100000, 0x02100400, 0x00000401, 0x00100401, 0x02100400,
                                        0x00000401, 0x02000001, 0x02100401, 0x02100000, 0x00100400, 0x00000000, 0x00000001, 0x02100401,
                                        0x00000000, 0x00100401, 0x02100000, 0x00000400, 0x02000001, 0x02000400, 0x00000400, 0x00100001
                                    };

    /** shuffled, shifted and permuted S-box number 8 */
    private static final int[] S8 = {
                                        0x08000820, 0x00000800, 0x00020000, 0x08020820, 0x08000000, 0x08000820, 0x00000020, 0x08000000,
                                        0x00020020, 0x08020000, 0x08020820, 0x00020800, 0x08020800, 0x00020820, 0x00000800, 0x00000020,
                                        0x08020000, 0x08000020, 0x08000800, 0x00000820, 0x00020800, 0x00020020, 0x08020020, 0x08020800,
                                        0x00000820, 0x00000000, 0x00000000, 0x08020020, 0x08000020, 0x08000800, 0x00020820, 0x00020000,
                                        0x00020820, 0x00020000, 0x08020800, 0x00000800, 0x00000020, 0x08020020, 0x00000800, 0x00020820,
                                        0x08000800, 0x00000020, 0x08000020, 0x08020000, 0x08020020, 0x08000000, 0x00020000, 0x08000820,
                                        0x00000000, 0x08020820, 0x00020020, 0x08000020, 0x08020000, 0x08000800, 0x08000820, 0x00000000,
                                        0x08020820, 0x00020800, 0x00020800, 0x00000820, 0x00000820, 0x00020020, 0x08000000, 0x08020800
                                    };

    /*
    FIXME convert these permutation arrays to bit-shuffling code for speed.
     (I just get too frustrated each time I try to find the pattern in these
      things and throw away the code. Low-level bit-twiddling is best left
      to someone who doesn't loathe and hate it.)
    */
    /** Initial Permutation. */
    protected static final byte[] IP = {
                                           57, 49, 41, 33, 25, 17,  9,  1,
                                           59, 51, 43, 35, 27, 19, 11,  3,
                                           61, 53, 45, 37, 29, 21, 13,  5,
                                           63, 55, 47, 39, 31, 23, 15,  7,
                                           56, 48, 40, 32, 24, 16,  8,  0,
                                           58, 50, 42, 34, 26, 18, 10,  2,
                                           60, 52, 44, 36, 28, 20, 12,  4,
                                           62, 54, 46, 38, 30, 22, 14,  6,
                                       };

    /** Final Permutation. */
    protected static final byte[] FP = {
                                           39,  7, 47, 15, 55, 23, 63, 31,
                                           38,  6, 46, 14, 54, 22, 62, 30,
                                           37,  5, 45, 13, 53, 21, 61, 29,
                                           36,  4, 44, 12, 52, 20, 60, 28,
                                           35,  3, 43, 11, 51, 19, 59, 27,
                                           34,  2, 42, 10, 50, 18, 58, 26,
                                           33,  1, 41,  9, 49, 17, 57, 25,
                                           32,  0, 40,  8, 48, 16, 56, 24
                                       };


    /** Feistel function.
     * @param R
     * @param K
     * @return*/
    private static int f(int R, long K)
    {
        return
            S1[ (int)((((R << 5) & 0x20) | ((R >>> 27) & 0x1f)) ^ ((K >>> 42) & 0x3f) ) ] |
            S2[ (int)(                     ((R >>> 23) & 0x3f)  ^ ((K >>> 36) & 0x3f) ) ] |
            S3[ (int)(                     ((R >>> 19) & 0x3f)  ^ ((K >>> 30) & 0x3f) ) ] |
            S4[ (int)(                     ((R >>> 15) & 0x3f)  ^ ((K >>> 24) & 0x3f) ) ] |
            S5[ (int)(                     ((R >>> 11) & 0x3f)  ^ ((K >>> 18) & 0x3f) ) ] |
            S6[ (int)(                     ((R >>>  7) & 0x3f)  ^ ((K >>> 12) & 0x3f) ) ] |
            S7[ (int)(                     ((R >>>  3) & 0x3f)  ^ ((K >>>  6) & 0x3f) ) ] |
            S8[ (int)((((R>>>31) & 0x01) | ((R  <<  1) & 0x3e)) ^ ( K         & 0x3f) ) ];
    }


    /**
     * Encrypt one block of data, sans initial and final permutations.
     */
    protected long subCrypt(long block)
    {
        int L = (int)(block >>> 32);
        int R = (int) block;
        for (int k=0; k<16; k++) {
            int t = L;
            L = R;
            R = t ^ f(R,subKeys[k]);
        }
        return ((long)R<<32) | (L & 0xffffffffL);
    }


    /**
     * Encrypt one block of data. The plaintext is taken from
     * <code>source[i..i+7]</code> and ciphertext is written to
     * <code>dest[j..j+7]</code>
     */
	public void encrypt(byte[] source, int i, byte[] dest, int j)
	{
		long block = makeLong(source,i,8);
		block = pickBits(block,IP);
		block = subCrypt(block);
		block = pickBits(block,FP);
		writeBytes(block,dest,j,8);
	}

    /**
     * Decrypt one block of data, sans initial and final permutations.
     */
    protected long subDecrypt(long block)
    {
        int L = (int)(block >>>32);
        int R = (int) block;
        for (int k=15; k>=0; k--) {
            int t = L;
            L = R;
            R = t ^ f(R,subKeys[k]);
        }
        return ((long)R<<32) | (L & 0xffffffffL);
    }


    /**
     * Decrypt one block of data. The encrypted data is taken from
     * <code>dest[i..i+7]</code> and plaintext is written to
     * <code>source[j..j+7]</code>.
     */
    public void decrypt(byte[] source, int i, byte[] dest, int j)
    {
        long block = makeLong(source,i,8);
        block = pickBits(block,IP);
        block = subDecrypt(block);
        block = pickBits(block,FP);
        writeBytes(block, dest, j, 8);
    }

	/**
	 * Convert a byte array to a long. Bits are collected from
	 * <code>buf[i..i+length-1]</code>. */
	public static long makeLong(byte[] buf, int i, int length)
	{
		long r=0;
		length+=i;
		for (int j=i; j<length; j++) {
            r= (r<<8) | (buf[j] & 0xffL);
        }
		return r;
	}

	/**
	 * Construct an int by picking bits from another int. The number in
	 * <code>bits[i]</code> is the index of the bit within <code>a</code>
	 * that should be put at index <code>i</code> in the result.
	 * <p>
	 * The most-significant bit is number 0.
	 */
	public static int pickBits(int a, byte[] bits)
	{
		int r=0;
		int l=bits.length;
        for (byte bit : bits) {
            r = (r << 1) | ((a >>> (31 - bit)) & 1);
        }
		return r;
	}

	/**
	 * Construct an long by picking bits from another long. The number in
	 * <code>bits[i]</code> is the index of the bit within <code>a</code>
	 * that should be put at index <code>i</code> in the result.
	 * <p>
	 * The most-significant bit is number 0.
	 */
	public static long pickBits(long a, byte[] bits)
	{
		long r=0;
		int l=bits.length;
        for (byte bit : bits) {
            r = (r << 1) | ((a >>> (63 - bit)) & 1);
        }
		return r;
	}

	/**
	 * Write a long to a byte array. Bits from <code>a</code> are written
	 * to <code>dest[i..i+length-1]</code>. */
	public static void writeBytes(long a, byte[] dest, int i, int length)
	{
		for (int j=i+length-1; j>=i; j--) {
			dest[j]=(byte)a;
			a >>>= 8;
		}
	}

    public static void test ()
    {
        byte[] k2 = {0x01, 0x23, 0x45, 0x67,
                    (byte)0x89, (byte)0xab, (byte)0xcd, (byte)0xef};

        byte[] a = {0x6b, (byte)0xc1, (byte)0xbe, (byte)0xe2, 0x2e, 0x40, (byte)0x9f, (byte)0x96};
        byte[] expected = {114, 119, -96, 13, -63, -63, -61, 107};
        byte[] b = new byte[8];

        DES des2;
        des2 = new DES(k2);
        des2.encrypt(a, 0, b, 0);
        System.out.println (Arrays.toString(b));
        if (Arrays.equals(b, expected))
        {
        }
        else
        {
        }
    }
    private static final Logger LOG = Logger.getLogger(DES.class.getName());
}
