package com.peter.crypto.numberfield2d;

import java.lang.reflect.Array;

public class GenericNumber <T extends Number>
{
    /**
     * generic Object Prototype
     */
    private T genericObject;

    /**
     * Constructor
     * @param clazz Type of work Object of this class
     */
    public GenericNumber (Class<T> clazz)
    {
        try
        {
            genericObject =  createNumberObject(clazz); //clazz.getConstructor (String.class).newInstance("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Get the Type of work object used by this class
     * @return see above
     */
    public Class<T> getGenericType ()
    {
        return (Class<T>) genericObject.getClass();
    }

    /**
     * Create a new object of same type used as work object by this class
     * @return the new object
     */
    public T createNumberObject ()
    {
        return createNumberObject (getGenericType());
    }

    public T[] createNumberArray (int size)
    {
        return (T[]) Array.newInstance(getGenericType(), size);
    }

    public T[][] createNumberArray (int w, int h)
    {
        return (T[][]) Array.newInstance(getGenericType(), w, h);
    }

    /**
     * Create a new Object derived from java.lang.Number
     * @param clazz concrete Type of new object
     * @return the new object
     */
    public static <T extends Number> T createNumberObject (Class<T> clazz)
    {
        try
        {
            return (T) clazz.getConstructor (String.class).newInstance("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Number> T createNumberObject (Class<T> clazz, String init)
    {
        try
        {
            return (T) clazz.getConstructor (String.class).newInstance(init);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Number> T[] createNumberArray (Class<T> clazz, int size)
    {
        return (T[]) Array.newInstance(clazz, size);
    }

    public static <T extends Number> T[][] createNumberArray (Class<T> clazz, int w, int h)
    {
        return (T[][]) Array.newInstance(clazz, w,h);
    }


    public String toString()
    {
        return getGenericType().getName();
    }

    public static void main (String[] args)
    {
        GenericNumber<Integer> t1 = new GenericNumber<>(Integer.class);
        System.out.println(t1.toString());
        Number n = t1.createNumberObject();
        System.out.println(n.getClass());
    }
}
