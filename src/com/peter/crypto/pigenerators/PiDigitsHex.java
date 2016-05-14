/*

https://gist.github.com/anonymous/6558e5dc75a909bc7bd580a13ef2ca5b

 */

package com.peter.crypto.pigenerators;

import java.math.BigInteger;

/**
 * Created by Administrator on 4/29/2016.
 */
public class PiDigitsHex
{
    private final static BigInteger sixteen = BigInteger.valueOf(16);

    /**
     * Get fractional partitions of PI as hex values
     * @param offset start digit
     * @param length number of digits
     * @return array of hex values
     */
    public static int[] piArray (int offset, int length)
    {
        int[] arr = new int[length];
        for (int s=0; s<length; s++)
        {
            try
            {
                arr[s] = ParallelPiHexEngine.taskedPiDigit(s+offset);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return arr;
    }

    /**
     * Get packed byte value of hexadecimal PI fractional part in big endian notation
     * @param offset position
     * @return a byte value representing 2 digits of the PI fraction
     */
    public static byte packedPi8 (int offset)
    {
        try
        {
            int b = ParallelPiHexEngine.taskedPiDigit(offset);
            b = b<<4;
            b |= ParallelPiHexEngine.taskedPiDigit(offset+1);
            return (byte)b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Packs hex PI fraction bytes into big integer
     * @param offset where to begin
     * @param length how many of them
     * @return a bigint
     */
    public static BigInteger packedPi (int offset, int length)
    {
        try
        {
            BigInteger b = BigInteger.valueOf(ParallelPiHexEngine.taskedPiDigit(offset));
            for (int s=1; s<length; s++)
            {
                b = b.multiply(sixteen);
                b = b.add (BigInteger.valueOf(ParallelPiHexEngine.taskedPiDigit(offset+s)));
            }
            return b;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
