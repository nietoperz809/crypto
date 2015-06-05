package com.peter.crypto;

import java.util.BitSet;

class CountedBs extends BitSet
{
    private final int size;

    public CountedBs(int nbits)
    {
        super(nbits);
        size = nbits;
    }

    @Override
    public int length()
    {
        return size;
    }
}

/**
 * To change this template use File | Settings | File Templates.
 */
public class BitAnalytics
{
    /**
     * Converts an array to a CountedBs object
     * @param arr
     * @return
     */
    private static CountedBs toCountedBs(byte arr[])
    {
        CountedBs bs = new CountedBs(arr.length * 8);
        int idx = 0;
        for (byte anArr : arr)
        {
            for (int t = 7; t >= 0; t--)
            {
                if ((anArr & (1 << t)) != 0)
                {
                    bs.set(idx);
                }
                idx++;
            }
        }
        return bs;
    }

    /**
     * Returns one-count index of an array
     * @param arr
     * @return
     */
    public static double ones(byte arr[])
    {
        CountedBs bs = toCountedBs(arr);
        return (double) bs.cardinality() / arr.length;
    }

    /**
     * Returns the zero-count index of an array
     * @param arr
     * @return
     */
    public static double zeros(byte arr[])
    {
        CountedBs bs = toCountedBs(arr);
        return (double) (arr.length * 8 - bs.cardinality()) / arr.length;
    }

    /**
     * Calculates the monobit index of the array
     * @param arr
     * @return
     */
    public static double monobit(byte[] arr)
    {
        CountedBs bs = toCountedBs(arr);
        double one = bs.cardinality();
        double zero = arr.length * 8 - one;
        return Math.pow(2, ((zero - one) / arr.length)) / 2.0;
    }

    /**
     * Counts the number of bit flips in the array
     * @param arr
     * @return
     */
    public static double flips(byte arr[])
    {
        CountedBs bs = toCountedBs(arr);
        int fl = 0;
        for (int s = 0; s < arr.length * 8 - 1; s++)
        {
            boolean b1 = bs.get(s);
            boolean b2 = bs.get(s + 1);
            if (b1 != b2)
            {
                fl++;
            }
        }
        return (double) fl / arr.length;
    }

    /**
     * Gives the largest string of consecutive zeros
     * @param arr
     * @return
     */
    public static int largestZeroString(byte arr[])
    {
        CountedBs bs = toCountedBs(arr);
        int len = 0;
        int pos = 0;
        for (; ;)
        {
            if (pos >= (arr.length * 8))
            {
                break;
            }
            int l1 = bs.nextSetBit(pos);
            if (l1 == -1)
            {
                l1 = arr.length * 8;
            }
            if (len < (l1 - pos))
            {
                len = l1 - pos;
            }
            pos = l1 + 1;
        }
        return len;
    }

    private static int sameZeroBitSequences(CountedBs bs, int num)
    {
        int count = 0;
        int pos = 0;
        for (; ;)
        {
            if (pos >= (bs.length()))
            {
                break;
            }
            int l1 = bs.nextSetBit(pos);
            if (l1 == -1)
            {
                l1 = bs.length();
            }
            if ((l1 - pos) >= num)
            {
                count++;
                pos = pos + num;
            }
            else
            {
                pos++;
            }
        }
        return count;
    }

    public static int sameZeroBitSequences(byte[] arr, int num)
    {
        CountedBs bs = toCountedBs(arr);
        return sameZeroBitSequences(bs, num);
    }

    public static int[] sameZeroBitSequenceHistogram(byte arr[], int max)
    {
        if (max == 0)
        {
            return null;
        }
        CountedBs bs = toCountedBs(arr);
        int[] res = new int[max];
        for (int s = 1; s <= max; s++)
        {
            res[s - 1] = sameZeroBitSequences(bs, s);
        }
        return res;
    }

    /**
     * Gives the largest string of consecutive ones
     * @param arr
     * @return
     */
    public static int largestOneString(byte arr[])
    {
        CountedBs bs = toCountedBs(arr);
        int len = 0;
        int pos = 0;
        for (; ;)
        {
            if (pos >= (arr.length * 8))
            {
                break;
            }
            int l1 = bs.nextClearBit(pos);
            if (len < (l1 - pos))
            {
                len = l1 - pos;
            }
            pos = l1 + 1;
        }
        return len;
    }

    /**
     * Counts bit sequences of the specified length
     * @param bs
     * @param num
     * @return
     */
    private static int sameOneBitSequences(CountedBs bs, int num)
    {
        int count = 0;
        int pos = 0;
        for (; ;)
        {
            if (pos >= (bs.length() * 8))
            {
                break;
            }
            int l1 = bs.nextClearBit(pos);
            if ((l1 - pos) >= num)
            {
                count++;
                pos = pos + num;
            }
            else
            {
                pos++;
            }
        }
        return count;
    }

    /**
     * Counts bit sequences of the specified length
     * @param arr
     * @param num
     * @return
     */
    public static int sameOneBitSequences(byte arr[], int num)
    {
        CountedBs bs = toCountedBs(arr);
        return sameOneBitSequences(bs, num);
    }

    /**
     * Creates a histogram of bit sequences from 1 to max length
     * @param arr
     * @param max
     * @return
     */
    public static int[] sameOneBitSequenceHistogram(byte arr[], int max)
    {
        if (max == 0)
        {
            return null;
        }
        CountedBs bs = toCountedBs(arr);
        int[] res = new int[max];
        for (int s = 1; s <= max; s++)
        {
            res[s - 1] = sameOneBitSequences(bs, s);
        }
        return res;
    }
}
