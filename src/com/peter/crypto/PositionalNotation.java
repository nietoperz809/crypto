package com.peter.crypto;

/**
 * Character-based counter
 */
public final class PositionalNotation
{
    private char[] _digitSet;
    
    private long _count;
    private final int _len;

    /**
     * Constructor
     * @param len Length of generated string
     */
    public PositionalNotation(int len)
    {
        _count = 0;
        _len = len;
        setMaterial('0', '9'-'0'+1);
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
     * @param c first character
     * @param num number of characters
     */
    public void setMaterial (char c, int num)
    {
        char[] mat = new char[num];
        for (int s=0; s<num; s++)
        {
            mat[s] = c;
            c++;
        }
        setMaterial(mat);
    }
    
    public void setMaterial (char first, char last)
    {
        setMaterial(first, last-first+1);
    }

    /**
     * Sets a value for conversion
     * @param n the value
     */
    public void setValue(long n)
    {
        _count = n;
    }

    /**
     * Get Counter as string
     * @return the string
     */
    @Override
    public String toString()
    {
        return String.valueOf(getResult());
    }

    /**
     * Returns as String but omits leading zeros
     * @return the string
     */
    public String toTrimmedString()
    {
        String s = toString();
        String test = ""+_digitSet[0];
        while (s.startsWith(test))
            s = s.substring(1);
        if (s.length() == 0)
            return test;
        return s;
    }
    
    /**
     * Workhorse: Get counter as array
     * @return the array
     */
    public char[] getResult()
    {
        char[] res = new char[_len];
        long c = _count;
        int c2;
        for (int s=0; s<_len; s++)
        {
            c2 = (int)(c%_digitSet.length);
            c = c/_digitSet.length;
            res[_len-s-1] = _digitSet[c2];
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
        PositionalNotation cc = new PositionalNotation(8);
        cc.setMaterial('0', '9');

//        cc.tick(100);
//        System.out.println(cc.toTrimmedString(true));
        
        long n = 1;
        for (;;)
        {
            System.out.println(cc.toTrimmedString());
            cc.setValue(n);
            n++;
        }
    }
}
