package com.peter.crypto;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

/**
 * ArrayList implementation capable of doing cyclic permutations
 * @param <E>
 */
class PermutationArrayList<E> extends ArrayList<E>
{
    private int[] permutationArray; /* Array to hold generated permutation */
    private int[] xx;
    private boolean first_call = true;

    public PermutationArrayList(Collection<? extends E> c)
    {
        super(c);
    }

    /**
     * Can be called to restart the cycle
     */
    public void reset()
    {
        first_call = true;
    }

    /**
     * Calculates the next permutation
     * Returns null if there are no more permutations in the cycle
     * @return An ArrayList holding the current permutation
     */
    public List<E> getNextPermutation()
    {
        List<E> al = new ArrayList<E>();
        if (gen_perm(this.size()))
        {
            first_call = true;
            return null;    // Last permutation generated
        }
        for (int s = 0; s < this.size(); s++)
        {
            al.add(this.get(permutationArray[s] - 1));
        }
        return al;
    }

    /**
     * Returns next permutation as array
     * @return The array
     */
    public E[] getNextPermutationArray()
    {
        List<E> al = getNextPermutation();
        E[] arr = (E[]) Array.newInstance(this.get(0).getClass(), this.size());
        al.toArray(arr);
        return arr;
    }

    /**
     * Get a list of all perutations
     * @return An ArrayList of ArrayLists that holds all permutations
     */
    public List<List<E>> getAllPermutations()
    {
        List<List<E>> al = new ArrayList<List<E>>();
        first_call = true;
        for (; ;)
        {
            List<E> a = getNextPermutation();
            if (a == null)
            {
                break;
            }
            al.add(a);
        }
        return al;
    }

    /**
     * Return all permutations as array of arrays
     * @return The generated array of arrays
     */
    public E[][] getAllPermutationsArrays()
    {
        List<List<E>> al = getAllPermutations();
        E[][] all = (E[][]) Array.newInstance(this.get(0).getClass(), al.size(), this.size());
        for (int s = 0; s < al.size(); s++)
        {
            al.get(s).toArray(all[s]);
        }
        return all;
    }

    /**
     * Get a specific permutation
     * @param n Permutation offset
     * @return ArrayList holding that permutation
     */
    public List<E> getPermutation(int n)
    {
        n++;
        ArrayList<E> al = new ArrayList<E>();
        first_call = true;
        while (n != 0)
        {
            if (gen_perm(this.size()))
            {
                first_call = true;
                return null;    // Last permutation generated
            }
            n--;
        }
        for (int s = 0; s < this.size(); s++)
        {
            al.add(this.get(permutationArray[s] - 1));
        }
        return al;
    }

    @Override
    public String toString()
    {
        String str = super.toString();
        if (this.size() == 0)
        {
            return str + " EMPTY";
        }
        else
        {
            return str + " " + get(0).getClass().getSimpleName();
        }
    }

    /**
     * The <b>workhorse</b>
     * @param n Number of elements to permute
     * @return true if there are no more permutations
     */
    private boolean gen_perm(int n)
    {
        int i;
        if (first_call)
        {
            /* initialize */
            permutationArray = new int[n + 1]; /* Array to hold generated permutation */
            xx = new int[n + 1];
            for (i = 0; i < n - 1; i++)
            {
                xx[i] = 0;
            }
            xx[n - 1] = -1;
            first_call = false;
        }
        /* Update xx array */
        for (i = n - 1; i > 0; i--)
        {
            if (xx[i] != i)
            {
                break;
            }
            xx[i] = 0;
        }
        if (i == 0)
        {
            return true;
        }
        xx[i] = xx[i] + 1;
        permutationArray[0] = 1;
        for (i = 0; i < n; i++)
        {
            permutationArray[i] = permutationArray[i - xx[i]];
            permutationArray[i - xx[i]] = i + 1;
        }
        return false;
    }

    /**
     * Prints all permutations
     * @param ps Where to print
     * @return Number of lines
     */
    public int printAll (PrintStream ps)
    {
        List<List<E>> res = getAllPermutations();
        for (List<E> re : res)
        {
            ps.println(re);
        }
        return res.size();
    }

    public static void test()
    {
        PermutationArrayList<String> p = new PermutationArrayList<String>(Arrays.asList("hallo", "lala", "doof"));
        p.printAll(System.out);
    }
}
