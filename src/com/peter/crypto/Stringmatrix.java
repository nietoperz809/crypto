package com.peter.crypto;

/**
 * Created by Administrator on 4/27/2016.
 */
public class Stringmatrix
{
    public static char[][] toMatrix (String in)
    {
        int xy = (int)Math.ceil(Math.sqrt(in.length()));
        int mlen = xy*xy;
        char out[][] = new char[xy][xy];

        while (in.length() < mlen)
            in += " ";

        for (int x=0; x<xy; x++)
        {
            for (int y=0; y<xy; y++)
            {
                out[x][y] = in.charAt(x+y*xy);
            }
        }

        return out;
    }

    public static String fromMatrix (char in[][])
    {
        StringBuilder out = new StringBuilder();
        int xy = in.length;

        for (int x=0; x<xy; x++)
        {
            for (int y=0; y<xy; y++)
            {
                out.append(in[y][x]);
            }
        }

        return out.toString();
    }

    public static char[][] revMatrix (char in[][])
    {
        int xy = in.length;
        char[][] out = new char[xy][xy];

        for (int x=0; x<xy; x++)
        {
            for (int y=0; y<xy; y++)
            {
                out[x][y] = in[y][x];
            }
        }

        return out;
    }
}
