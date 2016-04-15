package com.peter.crypto;

import java.math.BigInteger;

/**
 * This class implements a shift register
 */
public class ShiftRegister
{
    /**
     * Holds the bits of the LFSR
     */
    BigInteger val;
    /**
     * Implements the upper end since BigIntegers grow to nowhere
     */
    BigInteger andval;
    /**
     * MSB position
     */
    int highbit;

    /**
     * Constructor
     * @param size Bit length of this ShiftReg
     * @param initialValue Value on construction
     */
    public ShiftRegister (int size, long initialValue)
    {
        andval = BigInteger.valueOf (2).pow(size).subtract(BigInteger.ONE);
        val = BigInteger.valueOf (initialValue);
        highbit = size-1;
    }

    /**
     * Shifts left one bit
     */
    public void clockLeft()
    {
        val = val.shiftLeft(1).and(andval);
    }

    /**
     * Shifts right one bit
     */
    public void clockRight()
    {
        val = val.shiftRight(1).and(andval);
    }

    /**
     * Shifts the LFSR left multiple times
     * @param times Number of shift operations
     */
    public void clockLeft (int times)
    {
        for (int s=0; s<times; s++)
            clockLeft();
    }

    /**
     * Shifts the LFSR right multiple times
     * @param times Number of shift operations
     */
    public void clockRight (int times)
    {
        for (int s=0; s<times; s++)
            clockRight();
    }

    /**
     * Returns the MSB of this LFSR
     * @return false or true depending on the state of this bit
     */
    public int getMSB()
    {
        if (val.testBit (highbit))
            return 1;
        return 0;
    }

    /**
     * Returns the LSB of this LFSR
     * @return false or true depending on the state of this bit
     */
    public int getLSB()
    {
        if (val.testBit (0))
            return 1;
        return 0;
    }

    public int getBit (int num)
    {
        if (val.testBit (num))
            return 1;
        return 0;
    }

    public void setBit (int num, int bit)
    {
        if ((bit & 1) == 1)
            val = val.setBit(num);
        else
            val = val.clearBit(num);
    }

    /**
     * Sets all bits to zero
     */
    public void clear()
    {
        val = BigInteger.ZERO;
    }

    /**
     * Gets the value of the LFSR as long
     * @return Current value
     */
    public long getValue()
    {
        return val.longValue();
    }

    /**
     * Returns string representation with leading zeros
     * @return A string containing only ones and zeros
     */
    @Override
    public String toString()
    {
        String str = val.toString(2);
        while (str.length() <= highbit)
            str = '0'+str;
        return str;
    }
}
