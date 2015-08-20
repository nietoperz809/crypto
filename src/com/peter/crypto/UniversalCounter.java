package com.peter.crypto;

/**
 * Character-based counter
 */
public final class UniversalCounter
{
    private char[] _digitSet;
    
    private long _count;
    private int _len;

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
    
    /**
     * Constructor
     * @param len Length of counter
     */
    public UniversalCounter(int len)
    {
        _count = 0;
        _len = len;
        setMaterial('0', '9'-'0'+1);
    }

    /**
     * Advances the counter
     * @return false if counter reached the end
     */
    public void setValue(long n)
    {
        _count = n;
    }

    /**
     * Get Counter as string
     * @param rev true if output should be reverted
     * @return the string
     */
    public String toString(boolean rev)
    {
        char[] c = getResult(rev);
        return String.valueOf(c);
    }

    /**
     * Returns as String but omits leading zeros
     * @param rev true if string is reversed
     * @return the string
     */
    public String toTrimmedString (boolean rev)
    {
        String s = toString(rev);
        String test = ""+_digitSet[0];
        while (s.startsWith(test))
            s = s.substring(1);
        if (s.length() == 0)
            return test;
        return s;
    }
    
    /**
     * Get counter as array
     * @param rev true if output should be reverted
     * @return the array
     */
    public char[] getResult(boolean rev)
    {
        char[] res = new char[_len];
        long c = _count;
        for (int s=0; s<_len; s++)
        {
            int c2 = (int)(c%_digitSet.length);
            c = c/_digitSet.length;
            if (rev == true)
                res[_len-s-1] = _digitSet[c2];
            else
                res[s] = _digitSet[c2];
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
        UniversalCounter cc = new UniversalCounter(8);
        cc.setMaterial('0', '9'-'0'+1);

//        cc.tick(100);
//        System.out.println(cc.toTrimmedString(true));
        
        long n = 1;
        for (;;)
        {
            System.out.println(cc.toTrimmedString(true));
            cc.setValue(n);
            n++;
        }
    }
}
