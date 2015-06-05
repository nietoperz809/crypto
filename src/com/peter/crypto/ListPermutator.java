package com.peter.crypto;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Permutator for lists
 */
class ListPermutator<E>
{
    /**
     * Hold reference to source
     */
    List<List<E>> sourceList;
    /**
     * number of lists in source
     */
    int[] listIndex;
    /**
     * First call flag
     */
    boolean firstCall;

    /**
     * Constructor
     * @param list List of list of Objects
     */
    public ListPermutator(List<List<E>> list)
    {
        sourceList = list;
        listIndex = new int[list.size()];
        reset();
    }

    /**
     * begins a new permutation cycle
     */
    public void reset()
    {
        for (int s=0; s< sourceList.size(); s++)
        {
            listIndex[s] = 0;
        }
        firstCall = true;
    }

    /**
     * Get all permutations
     * @return List of list of all objects
     */
    public List<List<E>> getAll()
    {
        reset();
        List<List<E>> arr = new ArrayList<List<E>>();
        for(;;)
        {
            List<E> elem = getNext();
            if (elem == null)
                break;
            arr.add(elem);
        }
        return arr;
    }

    /**
     * Prints all permutations
     * @param ps Where to print
     * @return Number of lines
     */
    public int printAll (PrintStream ps)
    {
        List<List<E>> res = getAll();
        for (List<E> re : res)
        {
            ps.println(re);
        }
        return res.size();
    }

    /**
     * Gets next permutation
     * @return List of objects or <b>null</b> when done
     */
    public List<E> getNext()
    {
        List<E> arr = new ArrayList<E>();

        // Collect new Arrayist
        int sum = 0;
        for (int s=0; s< sourceList.size(); s++)
        {
            arr.add(sourceList.get(s).get(listIndex[s]));
            sum += listIndex[s];
        }

        // Detect end
        if (sum == 0 && !firstCall)
            return null;

        // Update the counter array
        for (int s=0; s< sourceList.size(); s++)
        {
            listIndex[s]++;
            if (listIndex[s] < sourceList.get(s).size())
                break;
            listIndex[s] = 0;
        }

        firstCall = false;
        return arr;
    }

    static public void test()
    {
        List<?>[] mylist =
        {
            new ArrayList<>(Arrays.asList("Wuleins", "Unser", "Mein")),
            new ArrayList<>(Arrays.asList("lieber", "dummer", "geiler", "schwarzer")),
            new ArrayList<>(Arrays.asList("Peter", "Fuchs", "Hase", "Weihnachtsmann")),
            new ArrayList<>(Arrays.asList("flieht", "schiesst", "kackt", "bellt")),
            new ArrayList<>(Arrays.asList("vielleicht", "schoen", "morgen", "nicht", "gewaltig", "herzhaft")),
        };

        ListPermutator<?> perm = new ListPermutator<>(Arrays.asList((List<String>[])mylist));

        int num = perm.printAll(System.out);
        System.out.println ("Perms " + num);
    }
}
