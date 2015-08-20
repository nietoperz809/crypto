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
    private final char[] result;

    /**
     * Sets new material
     * @param mat new material
     */
    public void setMaterial(char[] mat)
    {
        material = mat.clone();
        for (int s = 0; s < result.length; s++)
        {
            result[s] = material[0];
        }
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
        result = new char[len];
        setMaterial(material);
    }

    /**
     * Calculates index of character in material array
     * @param c the character
     * @return the index
     */
    private int index(char c)
    {
        for (int s = 0; s < material.length; s++)
        {
            if (material[s] == c)
            {
                return s;
            }
        }
        return -1; // error
    }

    /**
     * Advances the counter
     * @return false if counter reached the end
     */
    public boolean tick()
    {
        return countUp(0);
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
    private boolean countUp(int idx)
    {
        int i = index(result[idx]);
        if (i < (material.length - 1))
        {
            result[idx] = material[i + 1];
            return true;
        }
        result[idx] = material[0];
        if (idx < (result.length - 1))
        {
            return countUp(idx + 1);
        }
        return false;
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
     * Get counter as array
     * @param rev true if output should be reverted
     * @return the array
     */
    public char[] getResult(boolean rev)
    {
        if (rev == true)
        {
            char[] reverse = new char[result.length];
            for (int s = 0; s < result.length; s++)
            {
                reverse[s] = result[result.length - s - 1];
            }
            return reverse;
        }
        return result.clone();
    }

    /**
     * Test routine
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
        UniversalCounter cc = new UniversalCounter(8);
        cc.setMaterial('a', 'z'-'a'+1);

//        cc.tick(257);
//        System.out.println(cc.toString(true));
        
        for (;;)
        {
            System.out.println(cc.toString(true));
            //System.out.println(Arrays.toString(cc.getResult(true)));
            if (false == cc.tick())
            {
                System.exit(999);
            }
        }
    }
}
