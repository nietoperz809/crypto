package com.peter.crypto.numberfield2d;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 6/26/2016.
 */
public class NumberFieldTest
{
    @Test
    public void testConstructor0()
    {
        try
        {
            NumberField n = new NumberField(Integer.class, 3, 4);
            assertNotNull(n);
            System.out.println(n);
        }
        catch (Exception e)
        {
            fail (e.getMessage());
        }
    }


    @Test
    public void testConstructor1()
    {
        try
        {
            NumberField n = new NumberField(Byte.class, 10, 10);
            assertNotNull(n);
            NumberField m = n.setSubField(3, 5, 3, 5, 3);
            //System.out.println(m);
        }
        catch (Exception e)
        {
            fail (e.getMessage());
        }
    }

    @Test
    public void testConstructor2()
    {
        try
        {
            Short[][] vals = new Short[10][10];
            for (Short[] row: vals)
                Arrays.fill(row, (short)1);
            NumberField n = new NumberField (10, 10, vals);
            assertNotNull(n);
            NumberField m = n.setSubField(3, 5, 3, 5, 3);
            //System.out.println(m);
        }
        catch (Exception e)
        {
            fail (e.getMessage());
        }
    }

    @Test
    public void testConstructor3()
    {
        try
        {
            Short[][] vals = new Short[10][10];
            for (Short[] row: vals)
                Arrays.fill(row, (short)1);
            NumberField n = new NumberField (10, 10, vals);
            NumberField m = n.setSubField(3, 5, 3, 5, 3);
            NumberField o = new NumberField(m);
            assertNotNull(o);
        }
        catch (Exception e)
        {
            fail (e.getMessage());
        }
    }

    @Test
    public void fromArray1()
    {
        Integer arr[] = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
        NumberField n = NumberFieldFactory.fromArray(Integer.class, arr,3,4);
        System.out.println(n);
    }

    @Test
    public void testCountedUp()
    {
        try
        {
            NumberField n = NumberFieldFactory.countedUp(Byte.class, 10, 10, 1);
            assertNotNull(n);
            System.out.println(n);
        }
        catch (Exception e)
        {
            fail (e.getMessage());
        }

    }

    @Test
    public void testCountedDown()
    {
        try
        {
            NumberField n = NumberFieldFactory.countedDown (Byte.class, 10, 10, 100);
            assertNotNull(n);
            System.out.println(n);
        }
        catch (Exception e)
        {
            fail (e.getMessage());
        }

    }

}