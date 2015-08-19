/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto;

/**
 *
 * @author Administrator
 */
public final class CharCounter
{
    private char[] defaultMaterial =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private final char[] result;

    public void setMaterial(char[] mat)
    {
        defaultMaterial = mat.clone();
        for (int s = 0; s < result.length; s++)
        {
            result[s] = defaultMaterial[0];
        }
    }

    public CharCounter(int len)
    {
        result = new char[len];
        setMaterial (defaultMaterial);
    }

    private int index(char c)
    {
        for (int s = 0; s < defaultMaterial.length; s++)
        {
            if (defaultMaterial[s] == c)
            {
                return s;
            }
        }
        return -1; // error
    }

    public boolean countUp(int idx)
    {
        int i = index(result[idx]);
        if (i < (defaultMaterial.length - 1))
        {
            result[idx] = defaultMaterial[i + 1];
        }
        else
        {
            result[idx] = defaultMaterial[0];
            if (idx < (result.length - 1))
            {
                return countUp(idx + 1);
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    public String toString (boolean rev)
    {
        char[] c = getResult(rev);
        return String.valueOf(c);
    }
    
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

    public static void main(String[] args) throws Exception
    {
        CharCounter cc = new CharCounter(8);
        char[] material =
        {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        cc.setMaterial(material);

        for (;;)
        {
            System.out.println(cc.toString(true));
            //System.out.println(Arrays.toString(cc.getResult(true)));
            if (false == cc.countUp(0))
            {
                System.exit(999);
            }
            //Thread.sleep(10);
        }
    }
}
