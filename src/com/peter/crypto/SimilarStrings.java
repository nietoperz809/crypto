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
public class SimilarStrings
{
    private final double result;
    
    public SimilarStrings (String a, String b)
    {
        result = find3 (a, b);
    }
    
    public double getResult()
    {
        return result;
    }
    
    private double find2 (String s1, String s2)
    {
        double ret = 0.0;
        for (int s=0; s<s2.length(); s++)
        {
            ret += find1 (s2, s1, s);
        }
        return ret;
    }
    
    private double find3 (String s1, String s2)
    {
        double ret;
        ret = find2 (s1, s2);
        ret += find2 (s2, s1);
        return ret/(s1.length() + s2.length());
    }
    
    private boolean check(String s, char c, int idx)
    {
        try
        {
            if (s.charAt(idx) == c)
            {
                return true;
            }
        }
        catch (Exception ex)
        {
        }
        return false;
    }

    private double find1(String s1, String s2, int idx)
    {
        char c = s1.charAt(idx);
        double start = 1.0;
        double min = 1.0 / s2.length();
        for (int s = idx; s < s2.length(); s++) // seek forward
        {
            if (check(s2, c, s))
            {
                return start;
            }
            if (start > 0)
            {
                start -= min;
            }
        }
        start = 1.0;
        for (int s = idx; s >= 0; s--) // seek backward
        {
            if (check(s2, c, s))
            {
                return start;
            }
            if (start > 0)
            {
                start -= min;
            }
        }
        return 0;
    }   
}
