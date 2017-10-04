package com.peter.crypto;

import java.util.Arrays;
import java.util.Random;

/**
 * This is my fancy Aes256 class.
 * Written from scratch 'cause i want to learn how that works
 */
public class MyAES256
{
    private final byte[] key = new byte[32];
    private final byte[] enckey = new byte[32];
    private final byte[] deckey = new byte[32];

    private static final byte[] sbox =
            {
                    99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118,
                    -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64,
                    -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21,
                    4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117,
                    9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124,
                    83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49,
                    -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88,
                    81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46,
                    -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115,
                    96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37,
                    -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121,
                    -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8,
                    -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118,
                    112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98,
                    -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33,
                    -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22
            };

    private static final byte[] inv_sbox =
            {
                    82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5,
                    124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53,
                    84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78,
                    8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37,
                    114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110,
                    108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124,
                    -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6,
                    -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107,
                    58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115,
                    -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110,
                    71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27,
                    -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12,
                    31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95,
                    96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17,
                    -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97,
                    23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125
            };
    private static final byte xtime[] =
            {
                    0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30,
                    32, 34, 36, 38, 40, 42, 44, 46, 48, 50, 52, 54, 56, 58, 60, 62,
                    64, 66, 68, 70, 72, 74, 76, 78, 80, 82, 84, 86, 88, 90, 92, 94,
                    96, 98, 100, 102, 104, 106, 108, 110, 112, 114, 116, 118, 120, 122, 124, 126,
                    -128, -126, -124, -122, -120, -118, -116, -114, -112, -110, -108, -106, -104, -102, -100, -98,
                    -96, -94, -92, -90, -88, -86, -84, -82, -80, -78, -76, -74, -72, -70, -68, -66,
                    -64, -62, -60, -58, -56, -54, -52, -50, -48, -46, -44, -42, -40, -38, -36, -34,
                    -32, -30, -28, -26, -24, -22, -20, -18, -16, -14, -12, -10, -8, -6, -4, -2,
                    27, 25, 31, 29, 19, 17, 23, 21, 11, 9, 15, 13, 3, 1, 7, 5,
                    59, 57, 63, 61, 51, 49, 55, 53, 43, 41, 47, 45, 35, 33, 39, 37,
                    91, 89, 95, 93, 83, 81, 87, 85, 75, 73, 79, 77, 67, 65, 71, 69,
                    123, 121, 127, 125, 115, 113, 119, 117, 107, 105, 111, 109, 99, 97, 103, 101,
                    -101, -103, -97, -99, -109, -111, -105, -107, -117, -119, -113, -115, -125, -127, -121, -123,
                    -69, -71, -65, -67, -77, -79, -73, -75, -85, -87, -81, -83, -93, -95, -89, -91,
                    -37, -39, -33, -35, -45, -47, -41, -43, -53, -55, -49, -51, -61, -63, -57, -59,
                    -5, -7, -1, -3, -13, -15, -9, -11, -21, -23, -17, -19, -29, -31, -25, -27
            };

    /**
     * Call this first
     * @param k Key used for en- and decryption. Must be 32 byte long
     */
    void init(byte[] k)
    {
        int rcon = 1;
        int i;
        for (i = 0; i < key.length; i++)
        {
            enckey[i] = deckey[i] = k[i];
        }
        for (i = 8; --i != 0;)
        {
            //System.out.println ("("+rc+")");

            deckey[0] ^= sbox[deckey[29] & 0xff] ^ rcon;
            deckey[1] ^= sbox[deckey[30] & 0xff];
            deckey[2] ^= sbox[deckey[31] & 0xff];
            deckey[3] ^= sbox[deckey[28] & 0xff];
            deckey[4] ^= deckey[0];
            deckey[5] ^= deckey[1];
            deckey[6] ^= deckey[2];
            deckey[7] ^= deckey[3];
            deckey[8] ^= deckey[4];
            deckey[9] ^= deckey[5];
            deckey[10] ^= deckey[6];
            deckey[11] ^= deckey[7];
            deckey[12] ^= deckey[8];
            deckey[13] ^= deckey[9];
            deckey[14] ^= deckey[10];
            deckey[15] ^= deckey[11];
            deckey[16] ^= sbox[deckey[12] & 0xff];
            deckey[17] ^= sbox[deckey[13] & 0xff];
            deckey[18] ^= sbox[deckey[14] & 0xff];
            deckey[19] ^= sbox[deckey[15] & 0xff];
            deckey[20] ^= deckey[16];
            deckey[21] ^= deckey[17];
            deckey[22] ^= deckey[18];
            deckey[23] ^= deckey[19];
            deckey[24] ^= deckey[20];
            deckey[25] ^= deckey[21];
            deckey[26] ^= deckey[22];
            deckey[27] ^= deckey[23];
            deckey[28] ^= deckey[24];
            deckey[29] ^= deckey[25];
            deckey[30] ^= deckey[26];
            deckey[31] ^= deckey[27];

            rcon = rcon << 1 ^ ((rcon & 0xff) >>> 7 & 1) * 0x1b;
        }
    }

    /**
     * Encrypts a chunk of 16 byte
     * @param buf Points to the beginning of the block that should be encrypted
     */
    void encrypt(byte[] buf)
    {
        int i;
        int rcon = 1;

        buf[15] ^= (key[15] = enckey[15]);
        key[31] = enckey[31];
        buf[14] ^= (key[14] = enckey[14]);
        key[30] = enckey[30];
        buf[13] ^= (key[13] = enckey[13]);
        key[29] = enckey[29];
        buf[12] ^= (key[12] = enckey[12]);
        key[28] = enckey[28];
        buf[11] ^= (key[11] = enckey[11]);
        key[27] = enckey[27];
        buf[10] ^= (key[10] = enckey[10]);
        key[26] = enckey[26];
        buf[9] ^= (key[9] = enckey[9]);
        key[25] = enckey[25];
        buf[8] ^= (key[8] = enckey[8]);
        key[24] = enckey[24];
        buf[7] ^= (key[7] = enckey[7]);
        key[23] = enckey[23];
        buf[6] ^= (key[6] = enckey[6]);
        key[22] = enckey[22];
        buf[5] ^= (key[5] = enckey[5]);
        key[21] = enckey[21];
        buf[4] ^= (key[4] = enckey[4]);
        key[20] = enckey[20];
        buf[3] ^= (key[3] = enckey[3]);
        key[19] = enckey[19];
        buf[2] ^= (key[2] = enckey[2]);
        key[18] = enckey[18];
        buf[1] ^= (key[1] = enckey[1]);
        key[17] = enckey[17];
        buf[0] ^= (key[0] = enckey[0]);
        key[16] = enckey[16];
        for (i = 1; i < 14; ++i)
        {
            buf[15] = sbox[buf[15] & 0xff];
            buf[14] = sbox[buf[14] & 0xff];
            buf[13] = sbox[buf[13] & 0xff];
            buf[12] = sbox[buf[12] & 0xff];
            buf[11] = sbox[buf[11] & 0xff];
            buf[10] = sbox[buf[10] & 0xff];
            buf[9] = sbox[buf[9] & 0xff];
            buf[8] = sbox[buf[8] & 0xff];
            buf[7] = sbox[buf[7] & 0xff];
            buf[6] = sbox[buf[6] & 0xff];
            buf[5] = sbox[buf[5] & 0xff];
            buf[4] = sbox[buf[4] & 0xff];
            buf[3] = sbox[buf[3] & 0xff];
            buf[2] = sbox[buf[2] & 0xff];
            buf[1] = sbox[buf[1] & 0xff];
            buf[0] = sbox[buf[0] & 0xff];

            byte i1;

            i1 = buf[1];
            buf[1] = buf[5];
            buf[5] = buf[9];
            buf[9] = buf[13];
            buf[13] = i1;
            i1 = buf[10];
            buf[10] = buf[2];
            buf[2] = i1;
            i1 = buf[3];
            buf[3] = buf[15];
            buf[15] = buf[11];
            buf[11] = buf[7];
            buf[7] = i1;
            i1 = buf[14];
            buf[14] = buf[6];
            buf[6] = i1;
            int a, b, c, d, e;

            a = buf[0];
            b = buf[1];
            c = buf[2];
            d = buf[3];
            e = a ^ b ^ c ^ d;
            buf[0] ^= e ^ xtime[(a ^ b) & 0xff];
            buf[1] ^= e ^ xtime[(b ^ c) & 0xff];
            buf[2] ^= e ^ xtime[(c ^ d) & 0xff];
            buf[3] ^= e ^ xtime[(d ^ a) & 0xff];
            a = buf[4];
            b = buf[5];
            c = buf[6];
            d = buf[7];
            e = a ^ b ^ c ^ d;
            buf[4] ^= e ^ xtime[(a ^ b) & 0xff];
            buf[5] ^= e ^ xtime[(b ^ c) & 0xff];
            buf[6] ^= e ^ xtime[(c ^ d) & 0xff];
            buf[7] ^= e ^ xtime[(d ^ a) & 0xff];
            a = buf[8];
            b = buf[9];
            c = buf[10];
            d = buf[11];
            e = a ^ b ^ c ^ d;
            buf[8] ^= e ^ xtime[(a ^ b) & 0xff];
            buf[9] ^= e ^ xtime[(b ^ c) & 0xff];
            buf[10] ^= e ^ xtime[(c ^ d) & 0xff];
            buf[11] ^= e ^ xtime[(d ^ a) & 0xff];
            a = buf[12];
            b = buf[13];
            c = buf[14];
            d = buf[15];
            e = a ^ b ^ c ^ d;
            buf[12] ^= e ^ xtime[(a ^ b) & 0xff];
            buf[13] ^= e ^ xtime[(b ^ c) & 0xff];
            buf[14] ^= e ^ xtime[(c ^ d) & 0xff];
            buf[15] ^= e ^ xtime[(d ^ a) & 0xff];
            if ((i & 1) == 1)
            {
                buf[15] ^= key[31];
                buf[14] ^= key[30];
                buf[13] ^= key[29];
                buf[12] ^= key[28];
                buf[11] ^= key[27];
                buf[10] ^= key[26];
                buf[9] ^= key[25];
                buf[8] ^= key[24];
                buf[7] ^= key[23];
                buf[6] ^= key[22];
                buf[5] ^= key[21];
                buf[4] ^= key[20];
                buf[3] ^= key[19];
                buf[2] ^= key[18];
                buf[1] ^= key[17];
                buf[0] ^= key[16];
            }
            else
            {
                //System.out.println ("("+rc+")");

                key[0] ^= sbox[key[29] & 0xff] ^ rcon;
                key[1] ^= sbox[key[30] & 0xff];
                key[2] ^= sbox[key[31] & 0xff];
                key[3] ^= sbox[key[28] & 0xff];
                key[4] ^= key[0];
                key[5] ^= key[1];
                key[6] ^= key[2];
                key[7] ^= key[3];
                key[8] ^= key[4];
                key[9] ^= key[5];
                key[10] ^= key[6];
                key[11] ^= key[7];
                key[12] ^= key[8];
                key[13] ^= key[9];
                key[14] ^= key[10];
                key[15] ^= key[11];
                key[16] ^= sbox[key[12] & 0xff];
                key[17] ^= sbox[key[13] & 0xff];
                key[18] ^= sbox[key[14] & 0xff];
                key[19] ^= sbox[key[15] & 0xff];
                key[20] ^= key[16];
                key[21] ^= key[17];
                key[22] ^= key[18];
                key[23] ^= key[19];
                key[24] ^= key[20];
                key[25] ^= key[21];
                key[26] ^= key[22];
                key[27] ^= key[23];
                key[28] ^= key[24];
                key[29] ^= key[25];
                key[30] ^= key[26];
                key[31] ^= key[27];

                rcon = rcon << 1 ^ ((rcon & 0xff) >>> 7 & 1) * 0x1b;
                buf[15] ^= key[15];
                buf[14] ^= key[14];
                buf[13] ^= key[13];
                buf[12] ^= key[12];
                buf[11] ^= key[11];
                buf[10] ^= key[10];
                buf[9] ^= key[9];
                buf[8] ^= key[8];
                buf[7] ^= key[7];
                buf[6] ^= key[6];
                buf[5] ^= key[5];
                buf[4] ^= key[4];
                buf[3] ^= key[3];
                buf[2] ^= key[2];
                buf[1] ^= key[1];
                buf[0] ^= key[0];
            }
        }
        buf[15] = sbox[buf[15] & 0xff];
        buf[14] = sbox[buf[14] & 0xff];
        buf[13] = sbox[buf[13] & 0xff];
        buf[12] = sbox[buf[12] & 0xff];
        buf[11] = sbox[buf[11] & 0xff];
        buf[10] = sbox[buf[10] & 0xff];
        buf[9] = sbox[buf[9] & 0xff];
        buf[8] = sbox[buf[8] & 0xff];
        buf[7] = sbox[buf[7] & 0xff];
        buf[6] = sbox[buf[6] & 0xff];
        buf[5] = sbox[buf[5] & 0xff];
        buf[4] = sbox[buf[4] & 0xff];
        buf[3] = sbox[buf[3] & 0xff];
        buf[2] = sbox[buf[2] & 0xff];
        buf[1] = sbox[buf[1] & 0xff];
        buf[0] = sbox[buf[0] & 0xff];

        byte i1;

        i1 = buf[1];
        buf[1] = buf[5];
        buf[5] = buf[9];
        buf[9] = buf[13];
        buf[13] = i1;
        i1 = buf[10];
        buf[10] = buf[2];
        buf[2] = i1;
        i1 = buf[3];
        buf[3] = buf[15];
        buf[15] = buf[11];
        buf[11] = buf[7];
        buf[7] = i1;
        i1 = buf[14];
        buf[14] = buf[6];
        buf[6] = i1;
        //System.out.println ("("+rc+")");

        key[0] ^= sbox[key[29] & 0xff] ^ rcon;
        key[1] ^= sbox[key[30] & 0xff];
        key[2] ^= sbox[key[31] & 0xff];
        key[3] ^= sbox[key[28] & 0xff];
        key[4] ^= key[0];
        key[5] ^= key[1];
        key[6] ^= key[2];
        key[7] ^= key[3];
        key[8] ^= key[4];
        key[9] ^= key[5];
        key[10] ^= key[6];
        key[11] ^= key[7];
        key[12] ^= key[8];
        key[13] ^= key[9];
        key[14] ^= key[10];
        key[15] ^= key[11];
        key[16] ^= sbox[key[12] & 0xff];
        key[17] ^= sbox[key[13] & 0xff];
        key[18] ^= sbox[key[14] & 0xff];
        key[19] ^= sbox[key[15] & 0xff];
        key[20] ^= key[16];
        key[21] ^= key[17];
        key[22] ^= key[18];
        key[23] ^= key[19];
        key[24] ^= key[20];
        key[25] ^= key[21];
        key[26] ^= key[22];
        key[27] ^= key[23];
        key[28] ^= key[24];
        key[29] ^= key[25];
        key[30] ^= key[26];
        key[31] ^= key[27];

        buf[15] ^= key[15];
        buf[14] ^= key[14];
        buf[13] ^= key[13];
        buf[12] ^= key[12];
        buf[11] ^= key[11];
        buf[10] ^= key[10];
        buf[9] ^= key[9];
        buf[8] ^= key[8];
        buf[7] ^= key[7];
        buf[6] ^= key[6];
        buf[5] ^= key[5];
        buf[4] ^= key[4];
        buf[3] ^= key[3];
        buf[2] ^= key[2];
        buf[1] ^= key[1];
        buf[0] ^= key[0];
    }

    /**
     * Decrypts a chunk of 16 bytes
     * @param buf Points to the beginning of the block that should be decrypted
     */
    void decrypt(byte[] buf)
    {
        int i;
        int rcon = 0x80;

        buf[15] ^= (key[15] = deckey[15]);
        key[31] = deckey[31];
        buf[14] ^= (key[14] = deckey[14]);
        key[30] = deckey[30];
        buf[13] ^= (key[13] = deckey[13]);
        key[29] = deckey[29];
        buf[12] ^= (key[12] = deckey[12]);
        key[28] = deckey[28];
        buf[11] ^= (key[11] = deckey[11]);
        key[27] = deckey[27];
        buf[10] ^= (key[10] = deckey[10]);
        key[26] = deckey[26];
        buf[9] ^= (key[9] = deckey[9]);
        key[25] = deckey[25];
        buf[8] ^= (key[8] = deckey[8]);
        key[24] = deckey[24];
        buf[7] ^= (key[7] = deckey[7]);
        key[23] = deckey[23];
        buf[6] ^= (key[6] = deckey[6]);
        key[22] = deckey[22];
        buf[5] ^= (key[5] = deckey[5]);
        key[21] = deckey[21];
        buf[4] ^= (key[4] = deckey[4]);
        key[20] = deckey[20];
        buf[3] ^= (key[3] = deckey[3]);
        key[19] = deckey[19];
        buf[2] ^= (key[2] = deckey[2]);
        key[18] = deckey[18];
        buf[1] ^= (key[1] = deckey[1]);
        key[17] = deckey[17];
        buf[0] ^= (key[0] = deckey[0]);
        key[16] = deckey[16];
        byte i2;

        i2 = buf[1];
        buf[1] = buf[13];
        buf[13] = buf[9];
        buf[9] = buf[5];
        buf[5] = i2;
        i2 = buf[2];
        buf[2] = buf[10];
        buf[10] = i2;
        i2 = buf[3];
        buf[3] = buf[7];
        buf[7] = buf[11];
        buf[11] = buf[15];
        buf[15] = i2;
        i2 = buf[6];
        buf[6] = buf[14];
        buf[14] = i2;
        buf[15] = inv_sbox[(buf[15] & 0xff)];
        buf[14] = inv_sbox[(buf[14] & 0xff)];
        buf[13] = inv_sbox[(buf[13] & 0xff)];
        buf[12] = inv_sbox[(buf[12] & 0xff)];
        buf[11] = inv_sbox[(buf[11] & 0xff)];
        buf[10] = inv_sbox[(buf[10] & 0xff)];
        buf[9] = inv_sbox[(buf[9] & 0xff)];
        buf[8] = inv_sbox[(buf[8] & 0xff)];
        buf[7] = inv_sbox[(buf[7] & 0xff)];
        buf[6] = inv_sbox[(buf[6] & 0xff)];
        buf[5] = inv_sbox[(buf[5] & 0xff)];
        buf[4] = inv_sbox[(buf[4] & 0xff)];
        buf[3] = inv_sbox[(buf[3] & 0xff)];
        buf[2] = inv_sbox[(buf[2] & 0xff)];
        buf[1] = inv_sbox[(buf[1] & 0xff)];
        buf[0] = inv_sbox[(buf[0] & 0xff)];

        for (i = 14; (--i) != 0;)
        {
            if ((i & 1) == 1)
            {
                key[28] ^= key[24];
                key[29] ^= key[25];
                key[30] ^= key[26];
                key[31] ^= key[27];
                key[24] ^= key[20];
                key[25] ^= key[21];
                key[26] ^= key[22];
                key[27] ^= key[23];
                key[20] ^= key[16];
                key[21] ^= key[17];
                key[22] ^= key[18];
                key[23] ^= key[19];
                key[16] ^= sbox[(int) key[12] & 0xff];
                key[17] ^= sbox[(int) key[13] & 0xff];
                key[18] ^= sbox[(int) key[14] & 0xff];
                key[19] ^= sbox[(int) key[15] & 0xff];
                key[12] ^= key[8];
                key[13] ^= key[9];
                key[14] ^= key[10];
                key[15] ^= key[11];
                key[8] ^= key[4];
                key[9] ^= key[5];
                key[10] ^= key[6];
                key[11] ^= key[7];
                key[4] ^= key[0];
                key[5] ^= key[1];
                key[6] ^= key[2];
                key[7] ^= key[3];

                if ((rcon & 1) == 1)
                {
                    rcon = (rcon & 0xff) >>> 1 ^ 0x8d;
                }
                else
                {
                    rcon = (rcon & 0xff) >>> 1;
                }

                //System.out.println ("["+rcon+"]");

                key[0] ^= sbox[(int) key[29] & 0xff] ^ rcon;
                key[1] ^= sbox[(int) key[30] & 0xff];
                key[2] ^= sbox[(int) key[31] & 0xff];
                key[3] ^= sbox[(int) key[28] & 0xff];

                buf[15] ^= key[31];
                buf[14] ^= key[30];
                buf[13] ^= key[29];
                buf[12] ^= key[28];
                buf[11] ^= key[27];
                buf[10] ^= key[26];
                buf[9] ^= key[25];
                buf[8] ^= key[24];
                buf[7] ^= key[23];
                buf[6] ^= key[22];
                buf[5] ^= key[21];
                buf[4] ^= key[20];
                buf[3] ^= key[19];
                buf[2] ^= key[18];
                buf[1] ^= key[17];
                buf[0] ^= key[16];
            }
            else
            {
                buf[15] ^= key[15];
                buf[14] ^= key[14];
                buf[13] ^= key[13];
                buf[12] ^= key[12];
                buf[11] ^= key[11];
                buf[10] ^= key[10];
                buf[9] ^= key[9];
                buf[8] ^= key[8];
                buf[7] ^= key[7];
                buf[6] ^= key[6];
                buf[5] ^= key[5];
                buf[4] ^= key[4];
                buf[3] ^= key[3];
                buf[2] ^= key[2];
                buf[1] ^= key[1];
                buf[0] ^= key[0];
            }

            int a, b, c, d, e, x, y, z;
            a = buf[0];
            b = buf[1];
            c = buf[2];
            d = buf[3];
            e = (a ^ b ^ c ^ d);
            z = xtime[e & 0xff];
            x = e ^ xtime[xtime[(z ^ a ^ c) & 0xff] & 0xff];
            y = e ^ xtime[xtime[(z ^ b ^ d) & 0xff] & 0xff];
            buf[0] ^= x ^ xtime[(a ^ b) & 0xff];
            buf[1] ^= y ^ xtime[(b ^ c) & 0xff];
            buf[2] ^= x ^ xtime[(c ^ d) & 0xff];
            buf[3] ^= y ^ xtime[(d ^ a) & 0xff];
            a = buf[4];
            b = buf[5];
            c = buf[6];
            d = buf[7];
            e = (a ^ b ^ c ^ d);
            z = xtime[e & 0xff];
            x = e ^ xtime[xtime[(z ^ a ^ c) & 0xff] & 0xff];
            y = e ^ xtime[xtime[(z ^ b ^ d) & 0xff] & 0xff];
            buf[4] ^= x ^ xtime[(a ^ b) & 0xff];
            buf[5] ^= y ^ xtime[(b ^ c) & 0xff];
            buf[6] ^= x ^ xtime[(c ^ d) & 0xff];
            buf[7] ^= y ^ xtime[(d ^ a) & 0xff];
            a = buf[8];
            b = buf[9];
            c = buf[10];
            d = buf[11];
            e = (a ^ b ^ c ^ d);
            z = xtime[e & 0xff];
            x = e ^ xtime[xtime[(z ^ a ^ c) & 0xff] & 0xff];
            y = e ^ xtime[xtime[(z ^ b ^ d) & 0xff] & 0xff];
            buf[8] ^= x ^ xtime[(a ^ b) & 0xff];
            buf[9] ^= y ^ xtime[(b ^ c) & 0xff];
            buf[10] ^= x ^ xtime[(c ^ d) & 0xff];
            buf[11] ^= y ^ xtime[(d ^ a) & 0xff];
            a = buf[12];
            b = buf[13];
            c = buf[14];
            d = buf[15];
            e = (a ^ b ^ c ^ d);
            z = xtime[e & 0xff];
            x = e ^ xtime[xtime[(z ^ a ^ c) & 0xff] & 0xff];
            y = e ^ xtime[xtime[(z ^ b ^ d) & 0xff] & 0xff];
            buf[12] ^= x ^ xtime[(a ^ b) & 0xff];
            buf[13] ^= y ^ xtime[(b ^ c) & 0xff];
            buf[14] ^= x ^ xtime[(c ^ d) & 0xff];
            buf[15] ^= y ^ xtime[(d ^ a) & 0xff];

            byte i1;

            i1 = buf[1];
            buf[1] = buf[13];
            buf[13] = buf[9];
            buf[9] = buf[5];
            buf[5] = i1;
            i1 = buf[2];
            buf[2] = buf[10];
            buf[10] = i1;
            i1 = buf[3];
            buf[3] = buf[7];
            buf[7] = buf[11];
            buf[11] = buf[15];
            buf[15] = i1;
            i1 = buf[6];
            buf[6] = buf[14];
            buf[14] = i1;
            buf[15] = inv_sbox[(buf[15] & 0xff)];
            buf[14] = inv_sbox[(buf[14] & 0xff)];
            buf[13] = inv_sbox[(buf[13] & 0xff)];
            buf[12] = inv_sbox[(buf[12] & 0xff)];
            buf[11] = inv_sbox[(buf[11] & 0xff)];
            buf[10] = inv_sbox[(buf[10] & 0xff)];
            buf[9] = inv_sbox[(buf[9] & 0xff)];
            buf[8] = inv_sbox[(buf[8] & 0xff)];
            buf[7] = inv_sbox[(buf[7] & 0xff)];
            buf[6] = inv_sbox[(buf[6] & 0xff)];
            buf[5] = inv_sbox[(buf[5] & 0xff)];
            buf[4] = inv_sbox[(buf[4] & 0xff)];
            buf[3] = inv_sbox[(buf[3] & 0xff)];
            buf[2] = inv_sbox[(buf[2] & 0xff)];
            buf[1] = inv_sbox[(buf[1] & 0xff)];
            buf[0] = inv_sbox[(buf[0] & 0xff)];
        }
        buf[15] ^= key[15];
        buf[14] ^= key[14];
        buf[13] ^= key[13];
        buf[12] ^= key[12];
        buf[11] ^= key[11];
        buf[10] ^= key[10];
        buf[9] ^= key[9];
        buf[8] ^= key[8];
        buf[7] ^= key[7];
        buf[6] ^= key[6];
        buf[5] ^= key[5];
        buf[4] ^= key[4];
        buf[3] ^= key[3];
        buf[2] ^= key[2];
        buf[1] ^= key[1];
        buf[0] ^= key[0];
    }

    public static void main (String[] args) throws Exception
    {
        MyAES256.test();
        MyAES256.testSpeed(10000);
        AESEngine.testSpeed(10000);
    }

    /**
     * A little test routine
     * Encrypts 1...16 and then decrypts them
     */
    static void test()
    {
        System.out.println ("------------- Test My ENGINE ---------");

        byte key[] = new byte[32];
        Random rnd = new Random();
        rnd.setSeed (123);
        rnd.nextBytes(key);
        System.out.println("key: " + Arrays.toString(key));

        MyAES256 aes = new MyAES256();
        aes.init(key);

        byte[] test1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        System.out.println("before: " + Arrays.toString(test1));
        aes.encrypt(test1);
        System.out.println("after: " + Arrays.toString(test1));
        aes.decrypt(test1);
        System.out.println("decrypted: " + Arrays.toString(test1));

//        byte[] test2 = {-1, -2, -3, -4, -5, -6, -7, -8, -9, -10, -11, -12, -13, -14, -15, -16};
//        System.out.println("before: " + Arrays.toString(test2));
//        aes.encrypt(test2);
//        System.out.println("after: " + Arrays.toString(test2));
//        aes.decrypt(test2);
//        System.out.println("decrypted: " + Arrays.toString(test2));
    }

    public static void testSpeed (long i) throws Exception
    {
        byte key[] = {1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6,7,8,9,10,1,2};
        byte[] test1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

        MyAES256 aes = new MyAES256();
        aes.init(key);

        long start = System.currentTimeMillis();
        for (long s=0; s<i; s++)
        {
            aes.encrypt (test1);
        }
        start = System.currentTimeMillis() - start;

        System.out.println ("Iterations: "+ i + "  Time: "+ start);
    }
}

