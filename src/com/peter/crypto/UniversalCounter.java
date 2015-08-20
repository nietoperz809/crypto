package com.peter.crypto;

/**
 * Character-based counter
 */
public final class UniversalCounter
{
    private char[] material =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    
    private int _count;
    private int _len;

    /**
     * Sets new material
     * @param mat new material
     */
    public void setMaterial(char[] mat)
    {
        material = mat.clone();
    }

    /**
     * Sets new material
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
        setMaterial(material);
    }

    /**
     * Advances the counter
     * @return false if counter reached the end
     */
    public void tick()
    {
        _count++;
    }

    /**
     * Advances the counter i times
     * @param i
     */
    public void tick (int i)
    {
        while (i-- != 0)
            tick();
    }
    
    /**
     * Recursive Workhorse 
     * @param idx counter index that is currently addressed
     * @return false if counter reached end
     */

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
        String test = ""+material[0];
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
        int c = _count;
        for (int s=0; s<_len; s++)
        {
            int c2 = c%material.length;
            c = c/material.length;
            if (rev == true)
                res[_len-s-1] = material[c2];
            else
                res[s] = material[c2];
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
        cc.setMaterial('0', 2);

        cc.tick(8);
        System.out.println(cc.toTrimmedString(true));
        
//        for (;;)
//        {
//            System.out.println(cc.toTrimmedString(true));
//            //System.out.println(Arrays.toString(cc.getResult(true)));
//            cc.tick();
//        }
    }
}
