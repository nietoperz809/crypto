package com.peter.crypto;

/**
 * Class that implements a linear feedback shift register
 */
class LFSR extends ShiftRegister
{
    /**
     * The list of 'Taps'
     */
    int[] xorTaps;

    /**
     * Constructor
     * @param size Number of bits of this LFSR
     * @param initialValue Value after 0 clocks
     * @param taps List of XOR taps. May be <b>null</b>
     */
    LFSR (int size, long initialValue, int[] taps)
    {
        super (size, initialValue);
        xorTaps = taps;
    }

    /**
     * Private function to XOR all taps together
     * @return The XORed result
     */
    private boolean testTaps()
    {
        if (xorTaps == null)
            return false;
        boolean xx = false;
        for (int xorTap : xorTaps)
        {
            if (val.testBit(xorTap))
            {
                xx = !xx;
            }
        }
        return xx;
    }

    /**
     * Shifts the LFSR left one bit
     */
    @Override
    public void clockLeft()
    {
        boolean test = testTaps();
        super.clockLeft();
        if (test)
            val = val.setBit(0);
    }

    /**
     * Shifts the LFSR right one bit
     */
    @Override
    public void clockRight()
    {
        boolean test = testTaps();
        super.clockRight();
        if (test)
            val = val.setBit(highbit);
    }
}
