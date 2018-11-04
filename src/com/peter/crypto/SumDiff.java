package com.peter.crypto;

/**
 * Experimental
 */


public class SumDiff
{
    interface Swapper
    {
        void doIt (int[] arr, int t1, int t2);
    }

    interface Algo
    {
        int[] doIt (int[] in, Swapper t);
    }

    private void sumDiff (int[] arr, int t1, int t2)
    {
        int sum = arr[t1]+arr[t2];
        int diff = arr[t1]-arr[t2];
        arr[t1] = sum;
        arr[t2] = diff;
    }

    private void revSumDiff (int[] arr, int t1, int t2)
    {
        int sum = (arr[t1]+arr[t2])/2;
        int diff = arr[t1]-sum;
        arr[t1] = sum;
        arr[t2] = diff;
    }

    private int[] copyEven (int[] in)
    {
        int l = in.length;
        if (l%2 == 1)
            l++;
        int[] out = new int[l];
        System.arraycopy(in, 0, out, 0,in.length);
        return out;
    }

    public int[] toIntArray (char[] arr)
    {
        int[] ret = new int [arr.length];
        for (int s=0; s<arr.length; s++)
            ret[s] = arr[s];
        return ret;
    }

    public char[] toCharArray (int[] arr)
    {
        char[] ret = new char [arr.length];
        for (int s=0; s<arr.length; s++)
            ret[s] = (char)arr[s];
        return ret;
    }

    /////////////////////////////////////////////////////////////////

    /**
     * Swap Two consecutive elements
     * @param in Input array
     * @param t Swapper algorithm
     * @return Swapped array
     */
    public int[] alternatingSwap (int[] in, Swapper t)
    {
        int[] out = copyEven(in);
        for (int s=0; s<out.length; s+=2)
            t.doIt(out, s, s+1);
        return out;
    }

    public int[] contiguousSwap (int[] in, Swapper t)
    {
        int[] out = copyEven(in);
        for (int s=0; s<(out.length-1); s++)
            t.doIt(out, s, s+1);
        return out;
    }

    public int[] contiguousSwapRev (int[] in, Swapper t)
    {
        int[] out = copyEven(in);
        for (int s=out.length-2; s>=0; s--)
            t.doIt(out, s, s+1);
        return out;
    }

    public int[] outerToInnerSwap (int[] in, Swapper t)
    {
        int[] out = copyEven(in);
        for (int s=0; s<out.length/2; s++)
            t.doIt(out, s, out.length-s-1);
        return out;
    }

    public int[] twoHalfesSwap (int[] in, Swapper t)
    {
        int[] out = copyEven(in);
        for (int s=0; s<out.length/2; s++)
            t.doIt(out, s, out.length/2+s);
        return out;
    }

///////////////////////////////////////////////////////////

    public int[] func (int[] in, Swapper t, Algo a)
    {
        return a.doIt(in, t);
    }

    public int[] contiguousForward (int[] in)
    {
        return func (in, this::sumDiff, this::contiguousSwapRev);
    }

    public int[] contiguousBackward (int[] in)
    {
        return func (in, this::revSumDiff, this::contiguousSwap);
    }

    public int[] alternateForward (int[] in)
    {
        return func (in, this::sumDiff, this::alternatingSwap);
    }

    public int[] alternateBackward (int[] in)
    {
        return func (in, this::revSumDiff, this::alternatingSwap);
    }

    public int[] outerToInnerForward (int[] in)
    {
        return func (in, this::sumDiff, this::outerToInnerSwap);
    }

    public int[] outerToInnerBackward (int[] in)
    {
        return func (in, this::revSumDiff, this::outerToInnerSwap);
    }

    public int[] twoHalfesForward (int[] in)
    {
        return func (in, this::sumDiff, this::twoHalfesSwap);
    }

    public  int[] twoHalfesBackward (int[] in)
    {
        return func (in, this::revSumDiff, this::twoHalfesSwap);
    }


//    public static void main (String[] args)
//    {
//        SumDiff sd = new SumDiff();
//        String s = "Hello World.";
//        String s2;
//        String s3;
//        int[] out;
//        int[] rev;
//
//        out = sd.alternateForward(sd.toIntArray(s.toCharArray()));
//        rev = sd.alternateBackward(out);
//        s2 = new String (sd.toCharArray(out));
//        s3 = new String (sd.toCharArray(rev));
//        System.out.println(s2);
//        System.out.println(s3);
//
//        out = sd.outerToInnerForward(sd.toIntArray(s.toCharArray()));
//        rev = sd.outerToInnerBackward(out);
//        s2 = new String (sd.toCharArray(out));
//        s3 = new String (sd.toCharArray(rev));
//        System.out.println(s2);
//        System.out.println(s3);
//
//        out = sd.twoHalfesForward(sd.toIntArray(s.toCharArray()));
//        rev = sd.twoHalfesBackward(out);
//        s2 = new String (sd.toCharArray(out));
//        s3 = new String (sd.toCharArray(rev));
//        System.out.println(s2);
//        System.out.println(s3);
//    }

    public static void main (String[] args)
    {
        SumDiff sd = new SumDiff();
        String s = "Hello World.";
        String s2;
        String s3;
        int[] out;
        int[] rev;

        out = sd.alternateForward(sd.toIntArray(s.toCharArray()));
        rev = sd.alternateBackward(out);
        s2 = new String (sd.toCharArray(out));
        s3 = new String (sd.toCharArray(rev));
        System.out.println(s2);
        System.out.println(s3);

        out = sd.outerToInnerForward(sd.toIntArray(s.toCharArray()));
        rev = sd.outerToInnerBackward(out);
        s2 = new String (sd.toCharArray(out));
        s3 = new String (sd.toCharArray(rev));
        System.out.println(s2);
        System.out.println(s3);

        out = sd.twoHalfesForward(sd.toIntArray(s.toCharArray()));
        rev = sd.twoHalfesBackward(out);
        s2 = new String (sd.toCharArray(out));
        s3 = new String (sd.toCharArray(rev));
        System.out.println(s2);
        System.out.println(s3);

        out = sd.contiguousForward(sd.toIntArray(s.toCharArray()));
        rev = sd.contiguousBackward(out);
        s2 = new String (sd.toCharArray(out));
        s3 = new String (sd.toCharArray(rev));
        System.out.println(s2);
        System.out.println(s3);
    }



//    public static void main (String[] args)
//    {
//        int[] in = {1,2,3,4};
//        SumDiff sd = new SumDiff();
//        int loops = 50;
//
//        for (int s=0; s<loops; s++)
//        {
//            in = sd.alternateForward(in);
//            System.out.println(Arrays.toString(in));
//        }
//        System.out.println("------------------------");
//        for (int s=0; s<loops; s++)
//        {
//            in = sd.alternateBackward(in);
//            System.out.println(Arrays.toString(in));
//        }
//
//    }

}
