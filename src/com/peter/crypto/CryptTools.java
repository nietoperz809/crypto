package com.peter.crypto;

import com.peter.crypto.galois.GaloisField256;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Converts various array types
 */
public class CryptTools
{
    /**
     * Converts a string to an integer array
     *
     * @param string The source
     * @return a new integer array
     */
    public static int[] toIntArray (String string)
    {
        return toIntArray(string.getBytes());
    }

    /**
     * Converts a byte- to an integer array
     *
     * @param byteArray The source
     * @return The new integer array
     */
    public static int[] toIntArray (byte[] byteArray)
    {
        if (byteArray == null)
        {
            return null;
        }
        int[] res = new int[(byteArray.length + 3) / 4];
        for (int i = 0; i < byteArray.length; i++)
        {
            res[i / 4] |= (byteArray[i] << (8 * (i % 4)));
        }
        return res;
    }

    /**
     * Converts an integer array to a string
     *
     * @param intArray The source
     * @return The new string
     */
    public static String toString (int[] intArray)
    {
        return new String(toByteArray(intArray));
    }

    /**
     * Converts an integer- to a byte array
     *
     * @param intArray The source
     * @return A new byte array
     */
    public static byte[] toByteArray (int[] intArray)
    {
        byte[] res = new byte[intArray.length * 4];
        for (int i = 0; i < res.length; i++)
        {
            res[i] = (byte) (intArray[i / 4] >>> (8 * (i % 4)));
        }
        return res;
    }

    /**
     * Generates random data in file
     *
     * @param filename Name of new file
     * @param size     Size of that file
     * @param password Password to generate random data
     * @throws Exception If anything fails
     */
    public static void generateRandomFileFromPassword (String filename, int size, String password) throws Exception
    {
        FileOutputStream fout = new FileOutputStream(filename);
        byte[] random = generateRandomDataFromPassword(size, password);
        fout.write(random);
        fout.close();
    }

    /**
     * Generates a byte array of arbitrary length filled with random data
     *
     * @param size     Size of file
     * @param password Password that generates random data
     * @return the new byte array
     * @throws Exception if anything fails
     */
    public static byte[] generateRandomDataFromPassword (int size, String password) throws Exception
    {
        ByteArrayOutputStream bs = new ByteArrayOutputStream(size);
        byte[] key = CryptTools.passwordHash(password.getBytes());

        for (; size != 0; )
        {
            int blocklen;
            key = passwordHash(key);
            if (size < key.length)
            {
                blocklen = size;
            }
            else
            {
                blocklen = key.length;
            }
            bs.write(key, 0, blocklen);
            size -= blocklen;
        }

        return bs.toByteArray();
    }

    /**
     * Generates a (hopefully) secure hash value of 32 bytes from a given password
     *
     * @param pwd The password
     * @return The hash value
     * @throws NoSuchAlgorithmException Forget it
     */
    public static byte[] passwordHash (byte[] pwd) throws NoSuchAlgorithmException
    {
        byte[] res = new byte[32];
        byte[] md5bytes = new byte[16];
        byte[] sha1bytes = new byte[20];
        byte[] salt = {-48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88,
                -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8,

        };
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");

        System.arraycopy(sha1.digest(pwd), 0, sha1bytes, 0, 20);
        sha1bytes = rotateArrayRight(sha1bytes, sha1bytes[7] & 0xff);
        System.arraycopy(md5.digest(pwd), 0, md5bytes, 0, 16);
        md5bytes = rotateArrayRight(md5bytes, md5bytes[11] & 0xff);

        res[0] = (byte) (md5bytes[0] ^ sha1bytes[18]);
        res[1] = (byte) (md5bytes[1] ^ sha1bytes[19]);
        res[2] = (byte) (md5bytes[2] ^ sha1bytes[16]);
        res[3] = (byte) (md5bytes[3] ^ sha1bytes[17]);
        res[4] = (byte) (md5bytes[4] ^ sha1bytes[14]);
        res[5] = (byte) (md5bytes[5] ^ sha1bytes[15]);
        res[6] = (byte) (md5bytes[6] ^ sha1bytes[12]);
        res[7] = (byte) (md5bytes[7] ^ sha1bytes[13]);
        res[8] = (byte) (md5bytes[8] ^ sha1bytes[10]);
        res[9] = (byte) (md5bytes[9] ^ sha1bytes[11]);
        res[10] = (byte) (md5bytes[10] ^ sha1bytes[8]);
        res[11] = (byte) (md5bytes[11] ^ sha1bytes[9]);
        res[12] = (byte) (md5bytes[12] ^ sha1bytes[6]);
        res[13] = (byte) (md5bytes[13] ^ sha1bytes[7]);
        res[14] = (byte) (md5bytes[14] ^ sha1bytes[4]);
        res[15] = (byte) (md5bytes[15] ^ sha1bytes[5]);
        res[16] = (byte) (md5bytes[14] ^ sha1bytes[2]);
        res[17] = (byte) (md5bytes[13] ^ sha1bytes[3]);
        res[18] = (byte) (md5bytes[12] ^ sha1bytes[0]);
        res[19] = (byte) (md5bytes[11] ^ sha1bytes[1]);
        res[20] = (byte) (md5bytes[10] ^ sha1bytes[17]);
        res[21] = (byte) (md5bytes[9] ^ sha1bytes[11]);
        res[22] = (byte) (md5bytes[8] ^ sha1bytes[13]);
        res[23] = (byte) (md5bytes[7] ^ sha1bytes[5]);
        res[24] = (byte) (md5bytes[6] ^ sha1bytes[9]);
        res[25] = (byte) (md5bytes[5] ^ sha1bytes[3]);
        res[26] = (byte) (md5bytes[4] ^ sha1bytes[7]);
        res[27] = (byte) (md5bytes[3] ^ sha1bytes[0]);
        res[28] = (byte) (md5bytes[2] ^ sha1bytes[4]);
        res[29] = (byte) (md5bytes[1] ^ sha1bytes[8]);
        res[30] = (byte) (md5bytes[11] ^ sha1bytes[12]);
        res[31] = (byte) (md5bytes[0] ^ sha1bytes[15]);

        res = rotateArrayRight(res, res[2] & 0xff);
        res = xor(res, salt);
        return res;
    }

    /**
     * Rotates byte array to the right
     *
     * @param arr The source array
     * @param num number of rotations
     * @return The rotated array
     */
    public static byte[] rotateArrayRight (byte[] arr, int num)
    {
        int rot = num % arr.length;     // --> to the right
        int diff = arr.length - rot;    // difference
        byte[] res = new byte[arr.length];
        System.arraycopy(arr, 0, res, rot, diff);
        System.arraycopy(arr, diff, res, 0, rot);
        return res;
    }

    /**
     * Xors two byte arrays.
     *
     * @param in  IN block.
     * @param src XOR operands.
     * @return The new array
     */
    public static byte[] xor (byte[] in, byte[] src)
    {
        byte[] x = new byte[in.length];
        for (int s = 0; s < in.length; s++)
        {
            x[s] = (byte) (in[s] ^ src[s % src.length]);
        }
        return x;
    }

    public static byte inv (byte in)
    {
        return (byte) (in ^ 0xff);
    }

    public static byte[] inv (byte[] in)
    {
        int s;
        byte[] out = new byte[in.length];
        for (s = 0; s < in.length; s++)
        {
            out[s] = (byte) (in[s] ^ 0xff);
        }
        return out;
    }

    /**
     * Rotates byte array left
     *
     * @param arr The source array
     * @param num Number of rotations
     * @return The rotated array
     */
    public static byte[] rotateArrayLeft (byte[] arr, int num)
    {
        int rot = num % arr.length;     // to the left
        int diff = arr.length - rot;    // difference
        byte[] res = new byte[arr.length];
        System.arraycopy(arr, rot, res, 0, diff);
        System.arraycopy(arr, 0, res, diff, rot);
        return res;
    }

    /**
     * Converts string into gray codes String
     *
     * @param in The source string
     * @return The converted string
     */
    public static String grayString (String in)
    {
        return new String(grayByteArray(in.getBytes()));
    }

    /**
     * Converts byte array into gray code
     *
     * @param in Source array
     * @return Converted array
     */
    public static byte[] grayByteArray (byte[] in)
    {
        int s;
        byte[] out = new byte[in.length];
        for (s = 0; s < in.length; s++)
        {
            out[s] = CryptTools.grayByte(in[s]);
        }
        return out;
    }

    /**
     * Convert byte to gray code
     *
     * @param in The byte
     * @return The gray byte
     */
    public static byte grayByte (byte in)
    {
        return (byte) (in ^ (in & 0xff) >>> 1);
    }

    /**
     * Decrypts gray coded string
     *
     * @param in The source string
     * @return The encrypted string
     */
    public static String ungrayString (String in)
    {
        return new String(ungrayByteArray(in.getBytes()));
    }

    /**
     * Converts gray code array into normal array
     *
     * @param in The gray code array
     * @return The normal array
     */
    public static byte[] ungrayByteArray (byte[] in)
    {
        int s;
        byte[] out = new byte[in.length];
        for (s = 0; s < in.length; s++)
        {
            out[s] = CryptTools.ungrayByte(in[s]);
        }
        return out;
    }

    /**
     * Coverts gray byte to normal byte
     *
     * @param in The gray byte
     * @return The normal byte
     */
    public static byte ungrayByte (byte in)
    {
        int r = 8;
        while (--r != 0)
        {
            in ^= (in & 0xff) >>> 1;
        }
        return in;
    }

    public static byte[] addChain (byte[] in, byte[] key)
    {
        byte[] out = addChain(in, key[0]);
        for (int s = 1; s < key.length; s++)
        {
            out = addChain(out, key[s]);
        }
        return out;
    }

    public static byte[] addChain (byte[] in, byte init)
    {
        byte[] out = new byte[in.length];
        out[0] = (byte) (in[0] + init);
        for (int s = 1; s < in.length; s++)
        {
            out[s] = (byte) (in[s] + out[s - 1]);
        }
        return out;
    }

    public static byte[] unaddChain (byte[] in, byte[] key)
    {
        byte[] out = unaddChain(in, key[key.length - 1]);
        for (int s = key.length - 2; s >= 0; s--)
        {
            out = unaddChain(out, key[s]);
        }
        return out;
    }

    public static byte[] unaddChain (byte[] in, byte init)
    {
        byte[] out = new byte[in.length];
        for (int s = in.length - 1; s > 0; s--)
        {
            out[s] = (byte) (in[s] - in[s - 1]);
        }
        out[0] = (byte) (in[0] - init);
        return out;
    }

    public static byte[] xorChain (byte[] in, byte[] key)
    {
        byte[] out = xorChain(in, key[0]);
        for (int s = 1; s < key.length; s++)
        {
            out = xorChain(out, key[s]);
        }
        return out;
    }

    /**
     * Encrypts byte array as XOR chain
     *
     * @param in   The source byte array
     * @param init The init value
     * @return The encrypted byte array
     */
    public static byte[] xorChain (byte[] in, byte init)
    {
        byte[] out = new byte[in.length];
        out[0] = (byte) (in[0] ^ init);
        for (int s = 1; s < in.length; s++)
        {
            out[s] = (byte) (in[s] ^ out[s - 1]);
        }
        return out;
    }

    public static byte[] unxorChain (byte[] in, byte[] key)
    {
        byte[] out = unxorChain(in, key[key.length - 1]);
        for (int s = key.length - 2; s >= 0; s--)
        {
            out = unxorChain(out, key[s]);
        }
        return out;
    }

    /**
     * Decrypts XOR chained byte array
     *
     * @param in   The encrypted source byte array
     * @param init The init value
     * @return The decrypted byte array
     */
    public static byte[] unxorChain (byte[] in, byte init)
    {
        byte[] out = new byte[in.length];
        for (int s = in.length - 1; s > 0; s--)
        {
            out[s] = (byte) (in[s - 1] ^ in[s]);
        }
        out[0] = (byte) (in[0] ^ init);
        return out;
    }

    /**
     * Encrypts string to XOR chain
     *
     * @param in   The source string
     * @param init The init value
     * @return The encrypted String
     */
    public static String xorChainString (String in, byte init)
    {
        return new String(xorChain(in.getBytes(), init));
    }

    /**
     * Decrypts XOR chained string
     *
     * @param in   The encrypted source string
     * @param init The init value
     * @return The decrypted source string
     */
    public static String unxorChainString (String in, byte init)
    {
        return new String(unxorChain(in.getBytes(), init));
    }

    /**
     * Rotates byte right (one bit)
     *
     * @param in The source byte
     * @return The rotated byte
     */
    public static byte rotateByteRight (byte in)
    {
        return (byte) ((in & 0xff) >>> 1 | (in & 1) << 7);
    }

    /**
     * Rotates byte left (one bit)
     *
     * @param in The source byte
     * @return The rotated byte
     */
    public static byte rotateByteLeft (byte in)
    {
        return (byte) (in << 1 | (in & 0x80) >>> 7);
    }

    /**
     * Rotates array 'count' bits to the right
     *
     * @param in    The source array
     * @param count Number of bits to rotate
     * @return The rotated array
     */
    public static byte[] rotateArrayBitsRightCount (byte[] in, int count)
    {
        if (count == 0)
        {
            return in;
        }
        if (count < 0)
        {
            count = -count;
        }
        byte[] out = rotateArrayBitsRight(in);
        for (int s = 0; s < count - 1; s++)
        {
            out = rotateArrayBitsRight(out);
        }
        return out;
    }

    /**
     * Rotates array one bit to the right
     *
     * @param in The source array
     * @return The rotated array
     */
    public static byte[] rotateArrayBitsRight (byte[] in)
    {
        byte[] out = new byte[in.length];
        for (int s = 1; s < in.length; s++)
        {
            out[s] = (byte) ((in[s] & 0xff) >>> 1 | (in[s - 1] & 1) << 7);
        }
        out[0] = (byte) ((in[0] & 0xff) >>> 1 | (in[in.length - 1] & 1) << 7);
        return out;
    }

    /**
     * Rotates array 'count' bits to the left
     *
     * @param in    The source array
     * @param count Number of bits to rotate
     * @return The rotated array
     */
    public static byte[] rotateArrayBitsLeftCount (byte[] in, int count)
    {
        if (count == 0)
        {
            return in;
        }
        if (count < 0)
        {
            count = -count;
        }
        byte[] out = rotateArrayBitsLeft(in);
        for (int s = 0; s < count - 1; s++)
        {
            out = rotateArrayBitsLeft(out);
        }
        return out;
    }

    /**
     * Rotates array one bit to the left
     *
     * @param in The source array
     * @return The rotated array
     */
    public static byte[] rotateArrayBitsLeft (byte[] in)
    {
        byte[] out = new byte[in.length];
        for (int s = 0; s < in.length - 1; s++)
        {
            out[s] = (byte) (in[s] << 1 | (in[s + 1] & 0x80) >>> 7);
        }
        out[in.length - 1] = (byte) (in[in.length - 1] << 1 | (in[0] & 0x80) >>> 7);
        return out;
    }

    public static String rotateStringBitsRight (String in)
    {
        return new String(rotateArrayBitsRight(in.getBytes()));
    }

    public static String rotateStringBitsLeft (String in)
    {
        return new String(rotateArrayBitsLeft(in.getBytes()));
    }

    /**
     * Does galois field multiplication with array
     *
     * @param in    Array to be multiplied
     * @param mults Array of multiplicators
     * @return Multiplicated array
     */
    public static byte[] galoisFieldMult (byte[] in, byte[] mults)
    {
        for (byte mult : mults)
        {
            in = galoisFieldMult(in, mult);
        }
        return in;
    }

    /**
     * Does galois field multiplication with value
     *
     * @param in  Array to be multiplied
     * @param val Multiplicator
     * @return Multiplicated array
     */
    public static byte[] galoisFieldMult (byte[] in, byte val)
    {
        byte[] out = new byte[in.length];
        for (int s = 0; s < out.length; s++)
        {
            out[s] = (byte) GaloisField256.Product((int) in[s] & 0xff, (int) val & 0xff);
        }
        return out;
    }

    /**
     * Does galois field division with array
     *
     * @param in   Array to be divided
     * @param divs Array of divisors (none must be 0)
     * @return Divided array
     */
    public static byte[] galoisFieldDiv (byte[] in, byte[] divs)
    {
        for (byte div : divs)
        {
            in = galoisFieldDiv(in, div);
        }
        return in;
    }

    /**
     * Does galois field division with value
     *
     * @param in  Array to be divided
     * @param val Divisor (must not be 0)
     * @return Divided array
     */
    public static byte[] galoisFieldDiv (byte[] in, byte val)
    {
        byte[] out = new byte[in.length];
        for (int s = 0; s < out.length; s++)
        {
            out[s] = (byte) GaloisField256.Quotient((int) in[s] & 0xff, (int) val & 0xff);
        }
        return out;
    }

    public static byte[] yellowCode (byte[] in)
    {
        byte[] out = new byte[in.length];
        for (int n = 0; n < in.length; n++)
        {
            byte a = in[n];
            byte s = 8 >> 1;
            byte m = (byte) (0xff >> s);
            while (s != 0)
            {
                a ^= ((a & m) << s);
                s >>= 1;
                m ^= (m << s);
            }
            out[n] = a;
        }
        return out;
    }

    /**
     * generate random string from charset
     * characters can occur more than once
     *
     * @param charset String containing the charset
     * @param len     length of return string
     * @return a random string
     */
    public static String generateRandomString (CharSequence charset, int len)
    {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int s = 0; s < len; s++)
        {
            sb.append(charset.charAt(rnd.nextInt(charset.length())));
        }
        return sb.toString();
    }

    /**
     * returns first len values of shuffled string
     *
     * @param charset the input string
     * @param len     length of return string
     * @return result string
     */
    public static String generateRandomString (String charset, int len)
    {
        String s = generateRandomString(charset);
        return s.substring(0, len);
    }


    /**
     * Shuffles input string
     *
     * @param in the input
     * @return shuffled output
     */
    public static String generateRandomString (String in)
    {
        List<Character> characters = new ArrayList<>();
        for (char c : in.toCharArray())
        {
            characters.add(c);
        }
        Collections.shuffle(characters);
        StringBuilder sb = new StringBuilder();
        for (char c : characters)
        {
            sb.append(c);
        }
        return sb.toString();
    }

}
