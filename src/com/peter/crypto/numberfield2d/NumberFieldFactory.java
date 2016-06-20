package com.peter.crypto.numberfield2d;

import com.peter.crypto.CryptMath;
import com.peter.crypto.GaloisField256;
import com.peter.crypto.PermutationArrayList;

import java.util.Arrays;
import java.util.Random;

public class NumberFieldFactory
{
    public static Double[] ln_x_div_x (int len, double start)
    {
        Double[] vect = new Double[len];
        for (int s = 0; s < len; s++)
        {
            vect[s] = Math.log(start + s) / (start + s);
        }
        return vect;
    }

    public static Double[] squareDoubleArray (int len, double start)
    {
        Double[] vect = new Double[len];
        vect[0] = start;
        for (int s = 1; s < len; s++)
        {
            vect[s] = vect[s - 1] * vect[s - 1];
        }
        return vect;
    }

    public static NumberField countedUp (int x, int y, int start)
    {
        Integer[] a = countedIntegerArray(x * y, start);
        return fromArray(a, x, y);
    }

    /**
     * Generates a counted array from start to len-1
     *
     * @param len   Length of array
     * @param start First value
     * @return A new array
     */
    public static Integer[] countedIntegerArray (int len, int start)
    {
        Integer[] vect = new Integer[len];
        for (int s = 0; s < len; s++)
        {
            vect[s] = s + start;
        }
        return vect;
    }

    /**
     * Creates a field from one-dimensional array
     * The array must match X * Y
     *
     * @param arr Source array
     * @param x   Width of field
     * @param y   Height of field
     * @return The new field
     */
    public static <T extends Number> NumberField fromArray (T[] arr, int x, int y)
    {
        NumberField m = new NumberField (arr[0].getClass(), x, y);
        for (int s = 0; s < y; s++)
        {
            System.arraycopy(arr, s * x, m.values[s], 0, x);
        }
        return m;
    }

    public static NumberField countedDown (int x, int y, int start)
    {
        Double[] a = countedDoubleArrayReverse(x * y, start);
        return fromArray(a, x, y);
    }

    /**
     * Generates a reverse counted array from len-1 to start
     *
     * @param len   Length of array
     * @param start First value
     * @return A new array
     */
    public static Double[] countedDoubleArrayReverse (int len, double start)
    {
        Double[] vect = new Double[len];
        for (int s = 0; s < len; s++)
        {
            vect[s] = len - s + start - 1;
        }
        return vect;
    }

    /**
     * Returns a number field with counted values
     * Given 3,3 the resulting field is:<br>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     *
     * @param x Width
     * @param y Height
     * @return A new Field
     */
    public static NumberField countedUpDown (int x, int y)
    {
        return countedUpDown(x, y, 0);
    }

    /**
     * Returns a number field with counted values beginning with <b>start</b>
     *
     * @param x     Width
     * @param y     Height
     * @param start First value
     * @return A new Field
     */
    public static NumberField countedUpDown (int x, int y, int start)
    {
        Double[] a = countedDoubleArrayUpDown(x * y, start);
        return fromArray(a, x, y);
    }

    /**
     * Generates an up/down counted array from start to start
     *
     * @param len   Length of array
     * @param start First value and last
     * @return A new array
     */
    public static Double[] countedDoubleArrayUpDown (int len, double start)
    {
        Double[] vect = new Double[len];
        for (int s = 0; s < len / 2; s++)
        {
            vect[s] = (s + start);
        }
        for (int s = len / 2; s < len; s++)
        {
            vect[s] = (len - s + start - 1);
        }
        return vect;
    }

    public static NumberField countedDownUp (int x, int y)
    {
        return countedDownUp(x, y, 0);
    }

    public static NumberField countedDownUp (int x, int y, int start)
    {
        Double[] a = countedDoubleArrayDownUp(x * y, start);
        return fromArray(a, x, y);
    }

    /**
     * Generates an down/up counted array from start to 0 back to start
     *
     * @param len   Length of array
     * @param start First value and last
     * @return A new array
     */
    public static Double[] countedDoubleArrayDownUp (int len, double start)
    {
        Double[] vect = countedDoubleArrayUpDown(len, start);
        int v = vect.length / 2;
        if ((vect.length & 1) == 0)
        {
            v--;
        }
        for (int s = 0; s < vect.length; s++)
        {
            vect[s] = v - vect[s];
        }
        return vect;
    }

    /**
     * Generates quadratic counted field filled by Z-Order
     * Given 4, the following field is created</p>
     * 0 2  8 10<br>
     * 1 3  9 11<br>
     * 4 6 12 14<br>
     * 5 7 13 15<br>
     * </p>
     *
     * @param xy Width==Height. Should be power of 2
     * @return The new field
     */
    public static NumberField countedZorder (int xy)
    {
        int orig = xy;
        // Round up to next power of 2
        xy = (int) Math.pow(2.0, Math.ceil(Math.log(xy) / Math.log(2.0)));
        NumberField n = new NumberField (Double.class, xy, xy);

        int x = 0;
        int y = 0;
        double val = 0;

        n.values[x][y] = val++;

        for (int s = 0; s < xy * xy; s++)
        {
            int b = 1;
            do
            {
                x ^= b;
                b &= ~x;
                y ^= b;
                b &= ~y;
                b <<= 1;
            } while (b != 0);

            if (x == xy || y == xy)
            {
                break;
            }
            n.values[x][y] = val++;
        }

        return n.getSubField(0, orig - 1, 0, orig - 1);
    }

    public static NumberField countedSpiralRightDown (Class<? extends Number> c, int xy, String str)
    {
        NumberField n = new NumberField (c, xy, xy);
        //NumberField n = new NumberField (Integer.class, xy, xy);

        int min = 0;
        int max = xy;
        int loops = xy / 2;
        int idx = 0;

        try
        {
            for (int r = 0; r < loops; r++)
            {
                for (int s = min; s < max; s++)
                {
                    n.values[min][s] = n.createNumberObject(""+(int)str.charAt(idx));
                    idx++;
                }

                for (int s = min + 1; s < max; s++)
                {
                    int ch = str.charAt(idx);
                    n.values[s][max - 1] = n.createNumberObject(""+(int)str.charAt(idx));
                    idx++;
                }

                for (int s = max - 2; s >= min; s--)
                {
                    n.values[max - 1][s] = n.createNumberObject(""+(int)str.charAt(idx));
                    idx++;
                }

                for (int s = max - 2; s >= min + 1; s--)
                {
                    n.values[s][min] = n.createNumberObject(""+(int)str.charAt(idx));
                    idx++;
                }
                min++;
                max--;
            }
            // Set center value when square is odd
            if ((xy & 1) == 1)
            {
                n.values[loops][loops] = n.createNumberObject(""+(int)str.charAt(idx));
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        return n;
    }

    public static NumberField countedSpiralRightDownReverse (int xy)
    {
        return countedSpiralRightDownReverse(xy, 0);
    }

    public static NumberField countedSpiralRightDownReverse (int xy, int val)
    {
        NumberField n = countedSpiralRightDown(xy);
        int sub = xy * xy - 1 + val;
        for (int a = 0; a < xy; a++)
        {
            for (int b = 0; b < xy; b++)
            {
                n.values[a][b] = sub - n.values[a][b].intValue();
            }
        }
        return n;
    }

    public static NumberField countedSpiralRightDown (int xy)
    {
        return countedSpiralRightDown(xy, 0);
    }

    /**
     * Create a spiral matrix
     *
     * @param xy  Size of both width and height of quadratic matrix
     * @param val start value
     * @return the new matrix
     */
    public static NumberField countedSpiralRightDown (int xy, double val)
    {
        NumberField n = new NumberField (Double.class, xy, xy);

        int min = 0;
        int max = xy;
        int loops = xy / 2;

        for (int r = 0; r < loops; r++)
        {
            for (int s = min; s < max; s++)
            {
                n.values[min][s] = val++;
            }

            for (int s = min + 1; s < max; s++)
            {
                n.values[s][max - 1] = val++;
            }

            for (int s = max - 2; s >= min; s--)
            {
                n.values[max - 1][s] = val++;
            }

            for (int s = max - 2; s >= min + 1; s--)
            {
                n.values[s][min] = val++;
            }

            min++;
            max--;
        }
        // Set center value when square is odd
        if ((xy & 1) == 1)
        {
            n.values[loops][loops] = val;
        }
        return n;
    }

    /**
     * Generates a quadratic checker board
     *
     * @param xy Width and Height
     * @return The checker board
     */
    public static NumberField chessBoard (int xy)
    {
        return countedSnake(xy).mod(2);
    }

    public static NumberField countedSnake (int xy)
    {
        return countedSnake(xy, xy, 0);
    }

    /**
     * Generates a field which elements have unique values beginning with <b>start</b>
     * Rows alternate from smallest to biggest value and vice versa
     *
     * @param x     Width
     * @param y     Height
     * @param start first value
     * @return A new field
     */
    public static NumberField countedSnake (int x, int y, double start)
    {
        NumberField m = new NumberField (Double.class, x, y);
        for (int a = 0; a < y; a += 2)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = a * x + b + start;
            }
        }
        for (int a = 1; a < y; a += 2)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = a * x + x - b - start - 1;
            }
        }
        return m;
    }

    /**
     * Generates a quadratic checker board
     *
     * @param xy Width and Height
     * @param v1 Value of odd fields
     * @param v2 Value of even fields
     * @return The checker board
     */
    public static NumberField chessBoard (int xy, double v1, double v2)
    {
        return countedSnake(xy).mod(2).substitute(0L, v1).substitute(1L, v2);
    }

    public static NumberField counted (int xy)
    {
        return counted(xy, xy, 0);
    }

    /**
     * Generates a field which elements have unique values beginning with <b>start</b>
     * Each row begins with the smallest value
     *
     * @param x     Width
     * @param y     Height
     * @param start first value
     * @return A new field
     */
    public static NumberField counted (int x, int y, double start)
    {
        NumberField m = new NumberField (Double.class, x, y);
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = a * x + b + start;
            }
        }
        return m;
    }

    /**
     * Generates a counted field which elements have unique values beginning with x*y-1
     *
     * @param x Width
     * @param y Height
     * @return A new field
     */
    public static NumberField countedReverse (int x, int y)
    {
        return counted(x, y).reverse();
    }

    /**
     * Generates a counted field which elements have unique values beginning with 0
     *
     * @param x Width
     * @param y Height
     * @return A new field
     */
    public static NumberField counted (int x, int y)
    {
        return counted(x, y, 0);
    }

    /**
     * Generates a counted field which elements have unique values beginning 0
     * Every row changes direction
     *
     * @param x Width
     * @param y Height
     * @return A new field
     */
    public static NumberField countedSnake (int x, int y)
    {
        return countedSnake(x, y, 0);
    }

    /**
     * Generates a counted field which elements have unique values beginning x*y-1
     * Every row changes direction
     *
     * @param x Width
     * @param y Height
     * @return A new field
     */
    public static NumberField countedSnakeReverse (int x, int y)
    {
        return countedSnake(x, y, 0).reverse();
    }

    /**
     * Create a new Square Field with a diagonal value from Left to Right
     *
     * @param xy  width == height
     * @param val Diagonal value
     * @return A new field
     */
    public static NumberField diagLR (int xy, double val)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        for (int a = 0; a < xy; a++)
        {
            m.values[a][a] = val;
        }
        return m;
    }

    /**
     * Create a new Square Field with a diagonal value from Right to Left
     *
     * @param xy  width == height
     * @param val Diagonal value
     * @return A new field
     */
    public static NumberField diagRL (int xy, double val)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        for (int a = 0; a < xy; a++)
        {
            m.values[a][xy - a - 1] = val;
        }
        return m;
    }

    /**
     * Generates a quadrativ field with both diagonals having the same value
     * for xy=4 and val=1 this gives<br>
     * 1   0   0   1<br>
     * 0   1   1   0<br>
     * 0   1   1   0<br>
     * 1   0   0   1<br>
     *
     * @param xy  Width and height
     * @param val Value of all diagonal points
     * @return A new field
     */
    public static NumberField diagsBoth (int xy, double val)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        for (int a = 0; a < xy; a++)
        {
            m.values[a][xy - a - 1] = val;
            m.values[a][a] = val;
        }
        return m;
    }

    /**
     * Generates a field of size xy*xy which borders are val
     *
     * @param xy  Width and Height
     * @param val Border value
     * @return A new field
     */
    public static NumberField frame (int xy, int val)
    {
        xy -= 2;
        NumberField m = new NumberField (Double.class, xy, xy);
        m = m.appendFrame(val);
        return m;
    }

    /**
     * Generates a field of size xy*xy with a cross in it
     *
     * @param xy  Width and Height
     * @param val Cross value
     * @return A new field
     */
    public static NumberField cross (int xy, double val)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        int half = xy / 2;
        for (int s = 0; s < xy; s++)
        {
            m.values[s][half] = val;
            m.values[half][s] = val;
        }
        return m;
    }

    /**
     * Creates a new Square Field with a triangle right/bottom which elements are the same value
     *
     * @param xy  width == height
     * @param val Value of all elemnts of the triangle
     * @return A new field
     */
    public static NumberField triangleRightBottom (int xy, int val)
    {
        return triangleRightTop(xy, val).rotateRight();
    }

    /**
     * Creates a new Square Field with a triangle right/top which elements are the same value
     *
     * @param xy  width == height
     * @param val Value of all elemnts of the triangle
     * @return A new field
     */
    public static NumberField triangleRightTop (int xy, int val)
    {
        return triangleLeftTop(xy, val).rotateRight();
    }

    /**
     * Creates a new Square Field with a triangle left/top which elements are the same value
     *
     * @param xy  width == height
     * @param val Value of all elemnts of the triangle
     * @return A new field
     */
    public static NumberField triangleLeftTop (int xy, int val)
    {
        return triangleLeftBottom(xy, val).rotateRight();
    }

    /**
     * Creates a new Square Field with a triangle left/bottom which elements are the same value
     *
     * @param xy  width == height
     * @param val Value of all elemnts of the triangle
     * @return A new field
     */
    public static NumberField triangleLeftBottom (int xy, double val)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        for (int a = 0; a < xy; a++)
        {
            for (int b = 0; b <= a; b++)
            {
                m.values[a][b] = val;
            }
        }
        return m;
    }

    public static NumberField logicalOrTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return logicalOrTable(vect, vect);
    }

    public static NumberField logicalOrTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = col[a].intValue() | row[b].intValue();
            }
        }
        return m;
    }

    public static NumberField logicalAndTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return logicalAndTable (vect, vect);
    }

    public static NumberField logicalAndTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = col[a] & row[b];
            }
        }
        return m;
    }

    public static NumberField logicalXorTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return logicalXorTable (vect, vect);
    }

    public static NumberField logicalXorTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Double.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] =  col[a] ^ row[b];
            }
        }
        return m;
    }

    public static NumberField logicalEquTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return logicalEquTable (vect, vect);
    }

    public static NumberField logicalEquTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] =  ~(col[a].intValue() ^ row[b].intValue());
            }
        }
        return m;
    }

    public static NumberField logicalImpTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return logicalImpTable (vect, vect);
    }

    public static NumberField logicalImpTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = (~(col[a] ^ row[b])) | row[b];
            }
        }
        return m;
    }

    public static NumberField modTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return modTable (vect, vect);
    }

    public static NumberField modTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                if (row[b] == 0)
                {
                    m.values[a][b] = col[a];
                }
                else
                {
                    m.values[a][b] = col[a] % row[b];
                }
            }
        }
        return m;
    }

    public static NumberField divTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return divTable(vect, vect);
    }

    public static NumberField divTable (Integer[] row, Integer col[])
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                if (row[b] == 0)
                {
                    m.values[a][b] = col[a];
                }
                else
                {
                    m.values[a][b] = col[a] / row[b];
                }
            }
        }
        return m;
    }

    public static NumberField moduloMultiplicationTable (Integer[] row, Integer[] col, int mod)
    {
        return moduloMultiplicationTable(row, col, mod, 0);
    }

    public static NumberField moduloMultiplicationTable (Integer[] row, Integer[] col, int mod, int offset)
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = ((col[a] * row[b]) + offset) % mod;
            }
        }
        return m;
    }

    public static NumberField moduloMultiplicationTable (int mod)
    {
        return moduloMultiplicationTable(mod, 0);
    }

    /**
     * Creates a multiplication table mod X
     *
     * @param mod    X
     * @param offset Offset from 0
     * @return A new field
     */
    public static NumberField moduloMultiplicationTable (int mod, int offset)
    {
        Integer[] vect = countedIntegerArray(mod, 0);
        return moduloMultiplicationTable (vect, vect, mod, offset);
    }

    public static NumberField multiplicationTable (Integer[] row, Integer[] col)
    {
        return multiplicationTable(row, col, 0);
    }

    /**
     * Generates new NumberField consisting of multiplication results
     *
     * @param row    Array of row values
     * @param col    Array of column values
     * @param offset Number added to each multiplication
     * @return New Numberfield that is: row[n][m] * col[n][m]
     */
    public static NumberField multiplicationTable (Integer[] row, Integer[] col, int offset)
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = col[a] * row[b] + offset;
            }
        }
        return m;
    }

    /**
     * Creates an addition table mod X
     * Such table contains <b>mod * (mod-1) different values</b>
     *
     * @param mod    X
     * @param offset Offset from 0
     * @return A new field
     */
    public static NumberField moduloAdditionTable (int mod, int offset)
    {
        Integer[] vect = countedIntegerArray(mod, 0);
        return moduloAdditionTable(vect, vect, offset);
    }

    public static NumberField moduloAdditionTable (Integer[] row, Integer[] col, int mod)
    {
        return moduloAdditionTable(row, col, mod, 0);
    }

    public static NumberField moduloAdditionTable (Integer[] row, Integer[] col, int mod, int offset)
    {
        NumberField m = new NumberField (Double.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = (col[a] + row[b] + offset) % mod;
            }
        }
        return m;
    }

    public static NumberField gcdTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 1);
        return gcdTable(vect, vect);
    }

    public static NumberField gcdTable (Integer[] row, Integer[] col)
    {
        NumberField m = new NumberField (Double.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = CryptMath.gcd(col[a], row[b]);
            }
        }
        return m;
    }

    public static NumberField lcmTable (int xy)
    {
        Integer[] vect = countedIntegerArray(xy, 1);
        return lcmTable(vect, vect);
    }

    public static NumberField lcmTable (Integer[] row, Integer[] col)
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = CryptMath.lcm(col[a], row[b]);
            }
        }
        return m;
    }

    public static NumberField additionTable (Integer[] v1, Integer[] v2)
    {
        return additionTable(v1, v2, 0);
    }

//    public static NumberField moduloAdditionTable (int xy)
//    {
//        return moduloAdditionTable (xy, 0);
//    }

    public static NumberField additionTable (Integer[] row, Integer[] col, int offset)
    {
        NumberField m = new NumberField (Integer.class, row.length, col.length);
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = (col[a] + row[b] + offset);
            }
        }
        return m;
    }

    /**
     * Generates a table with up and down counting diagonal values
     * For 4*4 this gives:
     * <p>
     * 0 1 2 3<br>
     * 1 2 3 2<br>
     * 2 3 2 1<br>
     * 3 2 1 0<br>
     * <p>
     *
     * @param xy Width = Length
     * @return A new field
     */
    public static NumberField diagsTable (int xy)
    {
        int len = 2 * xy - 1;
        double[] arr = new double[len];
        int s;
        for (s = 0; s < xy; s++)
        {
            arr[s] = s;
        }
        while (s < arr.length)
        {
            arr[s] = len - s - 1;
            s++;
        }
        return diagsTableFromArray(xy, arr);
    }

    public static NumberField diagsTableFromArray (int xy, double[] arr)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        for (int a = 0; a < xy; a++)
        {
            for (int b = 0; b < xy; b++)
            {
                m.values[a][b] = arr[(a + b) % arr.length];
            }
        }
        return m;
    }

    /**
     * Generates a table with down and up counting diagonal values
     * For 4*4 this gives:
     * <p>
     * 3 2 1 0<br>
     * 3 1 0 1<br>
     * 1 0 1 2<br>
     * 0 1 2 3<br>
     * <p>
     *
     * @param xy Width = Length
     * @return A new field
     */
    public static NumberField diagsTableReverse (int xy)
    {
        int len = 2 * xy - 1;
        double[] arr = new double[len];
        int s;
        for (s = 0; s < xy; s++)
        {
            arr[s] = xy - s - 1;
        }
        while (s < arr.length)
        {
            arr[s] = s - xy + 1;
            s++;
        }
        return diagsTableFromArray(xy, arr);
    }

    public static NumberField fromArraysMultiply (double[] arr1, double[] arr2)
    {
        NumberField m = new NumberField (Double.class, arr1.length, arr2.length);
        for (int a = 0; a < arr1.length; a++)
        {
            for (int b = 0; b < arr2.length; b++)
            {
                m.values[a][b] = arr1[a] * arr2[b];
            }
        }
        return m;
    }

    public static NumberField fromArraysAdd (double[] arr1, double[] arr2)
    {
        NumberField m = new NumberField (Double.class, arr1.length, arr2.length);
        for (int a = 0; a < arr1.length; a++)
        {
            for (int b = 0; b < arr2.length; b++)
            {
                m.values[a][b] = arr1[a] + arr2[b];
            }
        }
        return m;
    }

    /**
     * Generates Field with same columns
     *
     * @param arr Column values
     * @param rep Number of colums
     * @return A new field
     */
    public static NumberField fromColumnArray (Double[] arr, int rep)
    {
        return fromRowArray(arr, rep).transpose();
    }

    /**
     * Generates Field with same rows
     *
     * @param arr Row values
     * @param rep Number of rows
     * @return A new field
     */
    public static NumberField fromRowArray (Double[] arr, int rep)
    {
        NumberField m = new NumberField (Double.class, arr.length, rep);
        for (int s = 0; s < rep; s++)
        {
            System.arraycopy(arr, 0, m.values[s], 0, arr.length);
        }
        return m;
    }

    /**
     * Create all bit combinations
     *
     * @param numBits Number of bits
     * @return A new field
     */
    public static NumberField binaryTable (int numBits)
    {
        return baseTable(numBits, 2);
    }

    /**
     * Create all base X combinations
     *
     * @param numBits Number of digits
     * @param base    Base of numbering system
     * @return A new field
     */
    public static NumberField baseTable (int numBits, int base)
    {
        NumberField m = new NumberField (Double.class, numBits, (int) Math.pow(base, numBits));
        Double[] arr = new Double[numBits];

        for (int s = 0; s < numBits; s++)
        {
            arr[s] = 0d;
        }

        for (int s = 0; s < m.getHeight(); s++)
        {
            System.arraycopy(arr, 0, m.values[s], 0, arr.length);
            for (int n = 0; n < numBits; n++)
            {
                arr[n]++;
                if (arr[n] != base)
                {
                    break;
                }
                arr[n] = 0d;
            }
        }
        return m;
    }

    /**
     * Constructor that generates a field with all elements having the same value
     *
     * @param x   width of field
     * @param y   height of field
     * @param val Value of all elements
     * @return A new Field
     */
    public static NumberField sameValue (int x, int y, int val)
    {
        NumberField m = new NumberField (Integer.class, x, y);
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[b][a] = val;
            }
        }
        return m;
    }

    /**
     * Generates a Field that is filled with a repeating sequence of numbers
     * Given new int[]{9,4,6,1}, 3, 3 the generated field is</p>
     * 9 4 6<br>
     * 1 9 4<br>
     * 6 1 9<br>
     *
     * @param seq The sequence
     * @param x   Width
     * @param y   Height
     * @return A new field
     */
    public static NumberField fromSequence (double[] seq, int x, int y)
    {
        NumberField m = new NumberField (Double.class, x, y);
        int idx = 0;
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = seq[idx];
                idx = (idx + 1) % seq.length;
            }
        }
        return m;
    }

    /**
     * Generates a random Field from 0...max
     *
     * @param x   Width
     * @param y   Height
     * @param max max value
     * @return The new field
     */
    @SuppressWarnings("unchecked")
    public static NumberField random (int x, int y, int max)
    {
        max++;
        Random rnd = new Random();
        NumberField m = new NumberField (Double.class, x, y);
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = (double) rnd.nextInt(max);

            }
        }
        return m;
    }

    /**
     * Generates a left/top to right/bottom expansion field
     * e.g. for 4/4 it will produce:
     * <p>
     * 0 1 2 3<br>
     * 1 1 2 3<br>
     * 2 2 2 3<br>
     * 3 3 3 3<br>
     * <p>
     *
     * @param x Width
     * @param y Height
     * @return A new field
     */
    @SuppressWarnings("unchecked")
    public static NumberField expand (int x, int y)
    {
        NumberField m = new NumberField (Double.class, x, y);
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = (double) Math.max(a, b);
            }
        }
        return m;
    }

    /**
     * Generates a quadratic field that has an incrementing diagonal
     *
     * @param xy Width == Height
     * @return A new Field
     */
    @SuppressWarnings("unchecked")
    public static NumberField diagLRIncrementing (int xy)
    {
        NumberField m = new NumberField (Double.class, xy, xy);
        for (int a = 0; a < xy; a++)
        {
            m.values[a][a] = (double) a;
        }
        return m;
    }

    /**
     * Generates an incrementing columns table
     * for 4/4 it will make
     * <p>
     * 0 1 2 3<br>
     * 0 1 2 3<br>
     * 0 1 2 3<br>
     * 0 1 2 3<br>
     * <p>
     *
     * @param x Width
     * @param y Height
     * @return A new Field
     */
    @SuppressWarnings({"SuspiciousNameCombination"})
    public static NumberField incrementingColumns (int x, int y)
    {
        return incrementingRows(y, x).transpose();
    }

    /**
     * Generates an incrementing rows table
     * for 4/4 it will make
     * <p/>
     * 0 0 0 0<br>
     * 1 1 1 1<br>
     * 2 2 2 2<br>
     * 3 3 3 3<br>
     * </p>
     *
     * @param x Width
     * @param y Height
     * @return A new Field
     */
    @SuppressWarnings("unchecked")

    public static NumberField incrementingRows (int x, int y)
    {
        NumberField m = new NumberField (Double.class, x, y);
        int i = 0;
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = (double) i;
            }
            i++;
        }
        return m;
    }

    public static NumberField circle (int xy)
    {
        xy /= 2;
        NumberField a = multiplicationTable(xy);
        NumberField b = a.reverseRows();
        a = a.appendRight(b);
        b = a.reverseColumns();
        a = a.appendBottom(b);
        return a;
    }

    public static NumberField multiplicationTable (int xy)
    {
        return multiplicationTable(xy, 0);
    }

    /**
     * Creates a quadratic multiplication table
     * for 4 it makes:
     * <p>
     * 0 0 0 0<br>
     * 0 1 2 3<br>
     * 0 2 4 6<br>
     * 0 3 6 9<br>
     * <p>
     *
     * @param xy     width and height
     * @param offset Offset from 0
     * @return A new Field
     */
    public static NumberField multiplicationTable (int xy, int offset)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return multiplicationTable (vect, vect, offset);
    }

    public static NumberField rhombus (int xy)
    {
        xy /= 2;
        NumberField a = additionTable(xy);
        NumberField b = a.reverseRows();
        a = a.appendRight(b);
        b = a.reverseColumns();
        a = a.appendBottom(b);
        return a;
    }

    public static NumberField additionTable (int xy)
    {
        return additionTable(xy, 0);
    }

    /**
     * Creates a quadratic addition table
     * for 4 it makes:
     * <p>
     * 0 1 2 3<br>
     * 1 2 3 4<br>
     * 2 3 4 5<br>
     * 3 4 5 6<br>
     * <p>
     *
     * @param xy     width and height
     * @param offset value of first element
     * @return A new Field
     */
    public static NumberField additionTable (int xy, int offset)
    {
        Integer[] vect = countedIntegerArray(xy, 0);
        return additionTable(vect, vect, offset);
    }

    public static NumberField square (int xy)
    {
        xy /= 2;
        NumberField a = shrink(xy, xy);
        NumberField b = a.reverseRows();
        a = a.appendRight(b);
        b = a.reverseColumns();
        a = a.appendBottom(b);
        return a;
    }

    /**
     * Generates a left/top to right/bottom shrinking field
     * e.g. for 4/4 it will produce:
     * <p/>
     * 0 0 0 0<br>
     * 0 1 1 1<br>
     * 0 1 2 2<br>
     * 0 1 2 3<br>
     * </p>
     *
     * @param x Width
     * @param y Height
     * @return A new field
     */
    public static NumberField shrink (int x, int y)
    {
        NumberField m = new NumberField (Double.class, x, y);
        for (int a = 0; a < y; a++)
        {
            for (int b = 0; b < x; b++)
            {
                m.values[a][b] = (double) Math.min(a, b);
            }
        }
        return m;
    }

    /////////////////////////////////////////////////// Special cases /////////////////////////////////////////////////

    public static NumberField squareFromString (String in)
    {
        int sq = (int) CryptMath.nextSquare(in.length());
        Double[] d = new Double[sq * sq];
        for (int s = 0; s < d.length; s++)
        {
            if (s < in.length())
            {
                d[s] = (double) in.charAt(s);
            }
            else
            {
                d[s] = (double) " ".charAt(0);
            }
        }
        return fromArray(d, sq, sq);
    }

    public static NumberField fromArray (Integer[] arr, int x, int y)
    {
        Double[] d = fromIntArray(arr);
        return fromArray(d, x, y);
    }

    public static Double[] fromIntArray (Integer[] arr)
    {
        Double[] d = new Double[arr.length];
        for (int s = 0; s < arr.length; s++)
        {
            d[s] = arr[s].doubleValue();
        }
        return d;
    }

    public static NumberField fromArray (Integer[] arr)
    {
        Double[] d = fromIntArray(arr);
        return fromArray(d, d.length, 1);
    }


    public static NumberField fromArray (Double[][] arr)
    {
        NumberField m = new NumberField (Double.class, arr[0].length, arr.length);
        for (int s = 0; s < arr.length; s++)
        {
            System.arraycopy(arr[s], 0, m.values[s], 0, arr[s].length);
        }
        return m;
    }

    public static NumberField permutations (Double[] arr)
    {
        if (arr.length > 8)
        {
            System.out.println("Array too big");
            return null;
        }
        Double[] i = new Double[arr.length];
        System.arraycopy(arr, 0, i, 0, arr.length);
        PermutationArrayList<Double> list = new PermutationArrayList<Double>(Arrays.asList(i));
        NumberField n = new NumberField (Double.class, 0, 0);
        Double[][] perm = list.getAllPermutationsArrays();
        for (Double[] aPerm : perm)
        {
            System.arraycopy(aPerm, 0, arr, 0, perm[0].length);
            n = n.appendBottom(fromArray(arr));
        }
        return n;
    }

    /**
     * Creates a one-dimensional field from one-dimensional array
     * The array must match X * Y
     *
     * @param arr Source array
     * @return The new field
     */
    public static NumberField fromArray (Double[] arr)
    {
        return fromArray(arr, arr.length, 1);
    }

    public static NumberField primes (int x, int y)
    {
        Double[] primes = primesDoubleArray(x * y, 2);
        return fromArray(primes, x, y);
    }

    /**
     * Generates array of primes
     *
     * @param len   Length of array
     * @param start Next prime equal to o above
     * @return A new array
     */
    public static Double[] primesDoubleArray (int len, int start)
    {
        Double[] vect = new Double[len];
        vect[0] = (double) CryptMath.getNextPrimeAbove(start);
        for (int s = 1; s < len; s++)
        {
            vect[s] = (double) CryptMath.getNextPrimeAbove(vect[s - 1].longValue() + 1);
        }
        return vect;
    }

    public static NumberField squareSums (int x, int y)
    {
        Double[] sqsums = squareSumsDoubleArray(x * y, 0);
        return fromArray(sqsums, x, y);
    }

    public static Double[] squareSumsDoubleArray (int len, double start)
    {
        Double[] vect = new Double[len];
        for (int s = 0; s < len; s++)
        {
            double n = s + start;
            vect[s] = (n * (n + 1) * (2 * n + 1) / 6);
        }
        return vect;
    }

    public static NumberField sums (int x, int y)
    {
        Double[] sums = sumsDoubleArray(x * y, 0);
        return fromArray(sums, x, y);
    }

    public static Double[] sumsDoubleArray (int len, double start)
    {
        Double[] vect = new Double[len];
        for (int s = 0; s < len; s++)
        {
            double n = s + start;
            vect[s] = (n * (n + 1) / 2);
        }
        return vect;
    }

    public static NumberField galois256MultiplicationTable (Double[] row, Double[] col)
    {
        NumberField m = new NumberField (Double.class, row.length, col.length);
        GaloisField256 gf = GaloisField256.getInstance();
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = (double) gf.Product(col[a].intValue(), row[b].intValue());
            }
        }
        return m;
    }

    public static NumberField galois256DivisionTable (Double[] row, Double[] col)
    {
        NumberField m = new NumberField (Double.class, row.length, col.length);
        GaloisField256 gf = GaloisField256.getInstance();
        for (int a = 0; a < col.length; a++)
        {
            for (int b = 0; b < row.length; b++)
            {
                m.values[a][b] = (double) gf.Quotient(col[a].intValue(), row[b].intValue());
            }
        }
        return m;
    }

}