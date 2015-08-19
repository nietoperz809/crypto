/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto;

import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public class CharCounter
{
    private char[] material =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    private char[] result;
    private char[] reverse;
    
    public CharCounter(int len)
    {
        result = new char[len];
        reverse = new char[len];
        for (int s = 0; s < len; s++)
        {
            result[s] = material[0];
        }
    }

    public CharCounter(char[] m, int len)
    {
        material = m;
        result = new char[len];
        reverse = new char[len];
        for (int s = 0; s < len; s++)
        {
            result[s] = material[0];
        }
    }

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

    public boolean countUp (int idx)
    {
        int i = index(result[idx]);
        if (i < (material.length - 1))
        {
            result[idx] = material[i + 1];
        }
        else
        {
            result[idx] = material[0];
            if (idx < (result.length - 1))
            {
                return countUp(idx + 1);
            }
            else
                return false;
        }
        return true;
    }

    public char[] getResult (boolean rev)
    {
        if (rev == true)
        {
            for (int s=0; s<result.length; s++)
            {
                reverse[s] = result[result.length-s-1];
            }
            return reverse;
        }
        return result;
    }

    public static void main(String[] args) throws Exception
    {
        CharCounter cc = new CharCounter(3);
        for (;;)
        {
            System.out.println(Arrays.toString(cc.getResult(true)));
            if (false == cc.countUp(0))
                System.exit(999);
            Thread.sleep(10);
        }
    }
}
