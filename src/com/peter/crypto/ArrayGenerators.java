package com.peter.crypto;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * To change this template use File | Settings | File Templates.
 */
public class ArrayGenerators
{
    /**
     * Generates a copy of an array that doesn't have a specific value
     * @param in Input array
     * @param exclude Value to exclude
     * @param other Substitution value
     * @return A new array that does not have a specific value
     */
    public static byte[] exludeValue (byte[] in, int exclude, int other)
    {
        byte[] arr = new byte[in.length];
        for (int s=0; s<in.length; s++)
        {
            if (in[s] == exclude)
                arr[s] = (byte)other;
            else
                arr[s] = in[s];
        }
        return arr;
    }

    /**
     * Generate an array whose bytes have the same value
     * @param n  Number of bytes to generate
     * @param val Value of all bytes
     * @return Generated array
     */
    public static byte[] makeSameValueBytes(int n, int val)
    {
        byte[] a = new byte[n];
        Arrays.fill(a, (byte)val);
        return a;
    }

    /**
     * Generates an array with 'counted' bytes from start to end step 'offset'
     * @param offset size of array
     * @param start first value
     * @param end last value
     * @return the new array
     */
    public static byte[] makeCountedValueBytes(int start, int end, int offset)
    {
        byte[] a;
        if (offset > 0)
            a = new byte[(end-start)/offset+1];
        else
            a = new byte[(end-start)/-offset+1];
        byte val = (byte)start;
        for (int s=0; s<a.length; s++)
        {
            a[s] = val;
            val += offset;
        }
        return a;
    }

    /**
     * Generates an array with 'counted' bytes from start to end
     * @param start first value
     * @param end last value
     * @return the new array
     */
    public static byte[] makeCountedValueBytes(int start, int end)
    {
        byte[] a = new byte[end-start+1];
        for (int s=start; s<=end; s++)
        {
            a[s-start] = (byte)s;
        }
        return a;
    }

    /**
     * Generates array of 'secure' random bytes
     * @param size Size of the new array
     * @return the new array filled with random bytes
     */
    public static byte[] generateSecureRandomBytes (int size)
    {
        byte[] sr = new byte[size];
        new SecureRandom().nextBytes(sr);
        return sr;
    }

    /**
     * Doubles an array
     * @param in Array to be doubled
     * @return 2 times the input array
     */
    public static byte[] doubleArray (byte[] in)
    {
        byte[] out = new byte[in.length+in.length];
        System.arraycopy (in, 0, out, 0, in.length);
        System.arraycopy (in, 0, out, in.length, in.length);
        return out;
    }
}
