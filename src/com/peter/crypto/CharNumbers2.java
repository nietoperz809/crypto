package com.peter.crypto;

/**
 * Character-based counter
 */
public final class CharNumbers2
{
    private char[] _digitSet;
    
    private final int _len;

    /**
     * Constructor
     * @param len Length of generated string
     */
    public CharNumbers2(int len)
    {
        _len = len;
        setMaterial("0123456789");
    }

    /**
     * Sets new _digitSet
     * @param mat new _digitSet
     */
    public void setMaterial(char[] mat)
    {
        _digitSet = mat.clone();
    }

    /**
     * Sets new _digitSet
     * @param s new digitSet as String
     */
    public void setMaterial (String s)
    {
        setMaterial (s.toCharArray());
    }
    
    /**
     * Get Counter as string
     * @param value input Value
     * @return the string
     */
    public String toString (long value)
    {
        return String.valueOf(getResult(value));
    }

    /**
     * Returns as String but omits leading zeros
     * @param value input Value
     * @return the string
     */
    public String toTrimmedString(long value)
    {
        String s = toString(value);
        String test = ""+_digitSet[0];
        while (s.startsWith(test))
            s = s.substring(1);
        if (s.length() == 0)
            return test;
        return s;
    }
    
    /**
     * Workhorse: Get counter as array
     * @param value input value
     * @return the array
     */
    public char[] getResult (long value)
    {
        char[] res = new char[_len];
        long c = value;
        int c2;
        for (int s=1; s<=_len; s++)
        {
            c2 = (int)(c%_digitSet.length);
            c = c/_digitSet.length;
            res[_len-s] = _digitSet[c2];
        }
        return res;
    }

    /**
     * Test routine
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        CharNumbers2 cc = new CharNumbers2(10);
        cc.setMaterial("01234567890abcdef");

        for (long n=0; n<20; n++)
        {
            System.out.println(cc.toTrimmedString(123456789));
        }
    }
}
