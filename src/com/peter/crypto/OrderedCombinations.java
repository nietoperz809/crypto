/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.peter.crypto;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class OrderedCombinations
{
    private int size;
    private int maxnumber;
    private int[] field;

    ArrayList<int[]> vec = new ArrayList<int[]>();

    private void allOrderedCombinations (int startIndex, int max)
    {
        if (startIndex == size)
        {
            vec.add(field.clone());
        }
        else
        {
            if (max >= 0)
            {
                field[startIndex] = max;
                allOrderedCombinations (startIndex + 1, max);
            }
            if (max < maxnumber)
            {
                field[startIndex] = max + 1;
                allOrderedCombinations (startIndex + 1, max + 1);
            }
        }
    }

    public ArrayList<int[]> start (int num, int max)
    {
        size = num;
        maxnumber = max;
        field = new int[num];
        allOrderedCombinations(0, -1);
        return vec;
    }
}
