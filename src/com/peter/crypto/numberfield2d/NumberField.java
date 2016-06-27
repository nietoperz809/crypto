package com.peter.crypto.numberfield2d;

import com.peter.crypto.CryptMath;
import com.stefanmuenchow.arithmetic.Arithmetic;

import java.awt.Dimension;
import java.util.*;

/**
 * New Class. User: Administrator Date: 19.05.2009 Time: 23:26:15
 */
public class NumberField <T extends Number> extends Arithmetic<T>
{
    final T[][] values;
    private final int width;
    private final int height;

    /**
     * Constructor that generates an empty Field
     *
     * @param x width
     * @param y height
     */
    public NumberField (Class<T> c, int x, int y)
    {
        super(c);
        values = createNumberArray (x,y);
        width = x;
        height = y;
        for (int y1 = 0; y1 < y; y1++)
        {
            for (int x1 = 0; x1 < x; x1++)
            {
                values[y1][x1] =  null;
            }
        }
    }

    /**
     * Fills Numberfield with values from 1D-array
     * @param arr The array to be used
     */
    public void fill (T arr[])
    {
        int arridx = 0;
        for (int h = 0; h < height; h++)
        {
            for (int w = 0; w < width; w++)
            {
                values[h][w] = arr[arridx];
                if (arridx++ == arr.length)
                    return;
            }
        }
    }

    /**
     * Constructs a matrix quickly without checking arguments. Does not copy
     * array elements into a new array. Rather, the new matrix simply references
     * the specified array.
     *
     * @param m the number of rows.
     * @param n the number of columns.
     * @param a the array.
     */
    public NumberField (int m, int n, T[][] a)
    {
        super ((Class<T>) a[0][0].getClass());
        width = m;
        height = n;
        values = a;
    }

    /**
     * Constructor that create a copy of an existing field
     *
     * @param src Source field
     */
    public NumberField (NumberField src)
    {
        super (src.type());
        values = (T[][]) src.createNumberArray (src.getWidth(), src.getHeight());
        width = src.getWidth();
        height = src.getHeight();
        for (int a = 0; a < height; a++)
        {
            System.arraycopy(src.values[a], 0, values[a], 0, width);
        }
    }

    /**
     * Get the width
     *
     * @return X
     */
    public int getWidth()
    {
        return width;
    }

    /**
     * Get the height
     *
     * @return Y
     */
    public int getHeight()
    {
        return height;
    }

    public Dimension getSize()
    {
        return new Dimension (width, height);
    }
    
    /**
     * Sorts all rows
     *
     * @return A new field
     */
    public NumberField sortRows()
    {
        NumberField m = new NumberField(this);
        for (int s = 0; s < height; s++)
        {
            Arrays.sort(m.values[s]);
        }
        return m;
    }

    /**
     * Sorts all keeping field dimensions
     *
     * @return A new field
     */
    public NumberField sort()
    {
        T[] vals = asFlatArray();
        Arrays.sort(vals);
        return NumberFieldFactory.fromArray(vals[0].getClass(), vals, width, height);
    }

    /**
     * Shuffles the filed using same algorithm as collections.shuffle
     *
     * @return The new array
     */
    public NumberField shuffle()
    {
        T[] vals = asFlatArray();
        Random rnd = new Random();
        for (int i = vals.length; i > 1; i--)
        {
            int a = rnd.nextInt(i);
            int b = i - 1;
            T tmp = vals[a];
            vals[a] = vals[b];
            vals[b] = tmp;
        }

        return NumberFieldFactory.fromArray (vals[0].getClass(), vals, width, height);
    }

    public NumberField onlyPrimes()
    {
        T[] vals = asFlatArray();
        for (int s = 0; s < vals.length; s++)
        {
            if (CryptMath.millerRabinPrimeTest(vals[s].longValue()))
            {
                // do nothing
            }
            else
            {
                vals[s] = this.createNumberObject("0");
            }
        }
        return NumberFieldFactory.fromArray(vals[0].getClass(), vals, width, height);
    }

    public NumberField onlyPrimes (T replacement)
    {
        T[] vals = asFlatArray();
        for (int s = 0; s < vals.length; s++)
        {
            if (CryptMath.millerRabinPrimeTest(vals[s].longValue()))
            {
                vals[s] = replacement;
            }
            else
            {
                vals[s] = this.createNumberObject("0");
            }
        }
        return NumberFieldFactory.fromArray(vals[0].getClass(), vals, width, height);
    }

    /**
     * Substitutes all values v1 with v2
     *
     * @param v1 Value to search for
     * @param v2 Value to set
     * @return A new Field
     */
    public NumberField substitute(int v1, int v2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (values[a][b].intValue() == v1)
                {
                    m.values[a][b] = m.createNumberObject(v2);
                }
            }
        }
        return m;
    }

    /**
     * Substitutes all values that are not v1 with v2
     *
     * @param v1 Value to search for
     * @param v2 Value to set
     * @return A new Field
     */
    public NumberField substituteNot(int v1, int v2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (values[a][b].intValue() != v1)
                {
                    m.values[a][b] = v2;
                }
            }
        }
        return m;
    }

    /**
     * Keeps all values x that are a < x > b
     * Others will be set to zero. e.g. given 3 and 7 on 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * evaluates to</p>
     * 0 0 0<br>
     * 0 4 5<br>
     * 6 0 0<br>
     *
     * @param low Lower bound
     * @param high Upper bound
     * @return The new Field
     */
    public NumberField keepValuesBetween (Long low, Long high)
    {
        NumberField m = new NumberField (type(), width, height);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (values[a][b].longValue() > low && values[a][b].longValue() < high)
                {
                    m.values[a][b] = values[a][b];
                }
            }
        }
        return m;
    }

    public NumberField keepEven()
    {
        NumberField m = new NumberField (type(), width, height);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if ((values[a][b].intValue() % 2) == 0)
                {
                    m.values[a][b] = values[a][b];
                }
            }
        }
        return m;
    }

    public NumberField keepOdd()
    {
        NumberField m = new NumberField (type(), width, height);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if ((values[a][b].intValue() % 2) != 0)
                {
                    m.values[a][b] = values[a][b];
                }
            }
        }
        return m;
    }

    /**
     * Sets all elements of a row to a fixed value
     *
     * @param row The row number
     * @param val The value
     * @return A new field
     */
    public NumberField setRow(int row, double val)
    {
        NumberField m = new NumberField(this);
        for (int s = 0; s < width; s++)
        {
            m.values[row][s] = createNumberObject(val);
        }
        return m;
    }

    /**
     * Sets all elements of a row to those given by an array
     *
     * @param row The row number
     * @param val Array containing new values
     * @return A new field
     */
    public NumberField setRow(int row, double[] val)
    {
        NumberField m = new NumberField(this);
        for (int s = 0; s < width; s++)
        {
            m.values[row][s] = createNumberObject(val[s % val.length]);
        }
        return m;
    }

    /**
     * Returns one row
     *
     * @param row The row index
     * @return Array containing all values
     */
    public T[] getRow(int row)
    {
        T[] ret = this.createNumberArray(width);
        System.arraycopy(values[row], 0, ret, 0, width);
        return ret;
    }

    /**
     * Sets all elements of a column to a fixed value
     *
     * @param col The column number
     * @param val The value
     * @return A new field
     */
    public NumberField setColumn(int col, T val)
    {
        NumberField m = new NumberField(this);
        for (int s = 0; s < height; s++)
        {
            m.values[s][col] = val;
        }
        return m;
    }

    /**
     * Sets all elements of a column to those given by an array
     *
     * @param col The column number
     * @param val Array containing new values
     * @return A new field
     */
    public NumberField setColumn(int col, T[] val)
    {
        NumberField m = new NumberField(this);
        for (int s = 0; s < height; s++)
        {
            m.values[s][col] = val[s % val.length];
        }
        return m;
    }

    /**
     * Returns array of all values of a column
     *
     * @param col Column index
     * @return Array containing all values
     */
    public T[] getColumn (int col)
    {
        T[] ret = this.createNumberArray(height);
        for (int s = 0; s < height; s++)
        {
            ret[s] = values[s][col];
        }
        return ret;
    }

    /**
     * Returns the field as one dimensional array
     *
     * @return Array containing all field elements
     */
    public T[] asFlatArray()
    {
        T[] ret = this.createNumberArray(width * height);
        for (int s = 0; s < height; s++)
        {
            System.arraycopy(values[s], 0, ret, width * s, width);
        }
        return ret;
    }

    public String asFlatString()
    {
        T[] dd = asFlatArray();
        StringBuilder sb = new StringBuilder();
        for (T d : dd)
        {
            sb.append ((char)d.intValue());
        }
        return sb.toString();
    }
    
    public T[][] asArray()
    {
        T[][] arr = this.createNumberArray (width, height);
        for (int s = 0; s < arr.length; s++)
        {
            System.arraycopy(values[s], 0, arr[s], 0, arr[s].length);
        }
        return arr;
    }

    public Double[][] asDoubleArray()
    {
        Double[][] arr = createNumberArray (Double.class, width, height);
        for (int s = 0; s < arr.length; s++)
        {
            System.arraycopy(values[s], 0, arr[s], 0, arr[s].length);
        }
        return arr;
    }

    /**
     * Exchanges rows and columns
     *
     * @return A new field
     */
    public NumberField transpose()
    {
        NumberField m = new NumberField (this.type(), getHeight(), getWidth());
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[b][a] = values[a][b];
            }
        }
        return m;
    }

    public NumberField sqrt()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = createNumberObject(Math.sqrt (values[a][b].doubleValue()));
            }
        }
        return m;
    }

    public NumberField absolute()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = abs(values[a][b]);
            }
        }
        return m;
    }

    public NumberField exp()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = createNumberObject(Math.exp (values[a][b].doubleValue()));
            }
        }
        return m;
    }

    public NumberField pow(int n)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = createNumberObject(Math.pow(values[a][b].doubleValue(), n));
            }
        }
        return m;
    }


    /**
     * Applies mod-operator to all elements
     *
     * @param n the mod value
     * @return A new field
     */
    public NumberField mod(int n)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = mod (m.values[a][b],n);
            }
        }
        return m;
    }

    /**
     * Negates all elements of the field
     *
     * @return A new field
     */
    public NumberField negate()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = neg(m.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Adds two fields
     *
     * @param m2 Field to add to this field
     * @return A new field
     */
    public NumberField add(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = add(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    public NumberField sub(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = sub (m.values[a][b], m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Generates logical AND of the integers of two NumberFields Fields must
     * have same dimensions
     *
     * @param m2 Second number field
     * @return A new number field containing resulting integers
     */
    public NumberField logicalAnd(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = and(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Generates logical OR of the integers of two NumberFields Fields must have
     * same dimensions
     *
     * @param m2 Second number field
     * @return A new number field containing resulting integers
     */
    public NumberField logicalOr(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = or(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Generates logical XOR of the integers of two NumberFields Fields must
     * have same dimensions
     *
     * @param m2 Second number field
     * @return A new number field containing resulting integers
     */
    public NumberField logicalXor(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = xor( m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Generates logical EQUALITY of the integers of two NumberFields Fields
     * must have same dimensions
     *
     * @param m2 Second number field
     * @return A new number field containing resulting integers
     */
    public NumberField logicalEqu(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = equ(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Generates logical IMPLICATION of the integers of two NumberFields Fields
     * must have same dimensions
     *
     * @param m2 Second number field
     * @return A new number field containing resulting integers
     */
    public NumberField logicalImp(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = Arithmetic.imp(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Multiplies two fields
     *
     * @param m2 Field to multiply with this field
     * @return A new field
     */
    public NumberField mult(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = mul(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    public NumberField div(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (m2.values[a][b].equals(createNumberObject(0)))
                {
                    m.values[a][b] = createNumberObject(0);
                }
                else
                {
                    m.values[a][b] = div(m.values[a][b],m2.values[a][b]);
                }
            }
        }
        return m;
    }

    public NumberField max(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = Arithmetic.max(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    public NumberField min(NumberField m2)
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = Arithmetic.min(m.values[a][b],m2.values[a][b]);
            }
        }
        return m;
    }

    /**
     * Generates square of minimums<p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * evaluates to</p>
     * 0 1 0<br>
     * 1 4 1<br>
     * 0 1 0<br>
     *
     * @return A new field
     */
    public NumberField minSquare()
    {
        NumberField nn = this;
        nn = nn.min(nn.rotateLeft());
        nn = nn.min(nn.rotateLeft());
        nn = nn.min(nn.rotateLeft());
        return nn;
    }

    public NumberField minReverse()
    {
        return min(reverse());
    }

    public NumberField minTranspose()
    {
        return min(transpose());
    }

    /**
     * Generates square of minimums<p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * evaluates to</p>
     * 8 7 8<br>
     * 7 4 7<br>
     * 8 7 8<br>
     *
     * @return A new field
     */
    public NumberField maxSquare()
    {
        NumberField nn = this;
        nn = nn.max(nn.rotateLeft());
        nn = nn.max(nn.rotateLeft());
        nn = nn.max(nn.rotateLeft());
        return nn;
    }

    public NumberField maxReverse()
    {
        return max(reverse());
    }

    /**
     * Combines two fields where the placeholder in this field will be exchanged
     * by the value in m2
     *
     * @param m2 Other field
     * @param placeholder Placeholder value
     * @return A new field
     */
    public NumberField combine(NumberField m2, int placeholder)
    {
        NumberField m = new NumberField(this);
        T ph = createNumberObject(placeholder);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (m.values[a][b].equals(ph))
                {
                    m.values[a][b] = m2.values[a][b];
                }
            }
        }
        return m;
    }

    /**
     * Rotates all rows of field left
     *
     * @return A new field
     */
    public NumberField rotateRowsLeft()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            System.arraycopy(values[a], 1, m.values[a], 0, width - 1);
            m.values[a][width - 1] = values[a][0];
        }
        return m;
    }

    /**
     * Rotates rows left. First row by one, second by two and so on Thus, given
     * a field:
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * evaluates to:
     * <p/>
     * 1 2 0<br>
     * 5 3 4<br>
     * 6 7 8<br>
     * <p/>
     * @return A new field
     */
    public NumberField rotateRowsLeftIncremental()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int s = 0; s <= a; s++)
            {
                T temp = (T) m.values[a][0];
                System.arraycopy(m.values[a], 1, m.values[a], 0, width - 1);
                m.values[a][width - 1] = temp;
            }
        }
        return m;
    }

    public NumberField rotateRowsLeftIncremental(int n)
    {
        if (n < 1)
        {
            return this;
        }
        NumberField m = rotateRowsLeftIncremental();
        for (int s = 0; s < n; s++)
        {
            m = m.rotateRowsLeftIncremental();
        }
        return m;
    }

    /**
     * Rotates columns up. First row by one, second by two and so on Thus, given
     * a field:
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * evaluates to:
     * <p/>
     * 3 7 2<br>
     * 6 1 5<br>
     * 0 4 8<br>
     * <p/>
     * @return A new field
     */
    public NumberField rotateColumnsUpIncremental()
    {
        return transpose().rotateRowsLeftIncremental().transpose();
    }

    /**
     * Rotates all rows of field right
     *
     * @return A new field
     */
    public NumberField rotateRowsRight()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            System.arraycopy(values[a], 0, m.values[a], 1, width - 1);
            m.values[a][0] = values[a][width - 1];
        }
        return m;
    }

    /**
     * Rotate rows right. First row by one, second by two and so on Thus, given
     * a field:
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * evaluates to:
     * <p/>
     * 2 0 1<br>
     * 4 5 3<br>
     * 6 7 8<br>
     * <p/>
     * @return A new field
     */
    public NumberField rotateRowsRightIncremental()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int s = 0; s <= a; s++)
            {
                T temp = (T) m.values[a][width - 1];
                System.arraycopy(m.values[a], 0, m.values[a], 1, width - 1);
                m.values[a][0] = temp;
            }
        }
        return m;
    }

    public NumberField rotateColumnsDownIncremental()
    {
        return transpose().rotateRowsRightIncremental().transpose();
    }

    /**
     * Does a "right snake rotate" of all rows
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 8 0 1<br>
     * 2 3 4<br>
     * 5 6 7<br>
     * <p/>
     * @return A new field
     */
    public NumberField snakeRotateRowsRight()
    {
        T[] arr = asFlatArray();
        T temp = arr[arr.length - 1];
        System.arraycopy(arr, 0, arr, 1, arr.length - 1);
        arr[0] = temp;
        return NumberFieldFactory.fromArray(arr[0].getClass(), arr, width, height);
    }

    /**
     * Does a "left snake rotate" of all rows
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 1 2 3<br>
     * 4 5 6<br>
     * 7 8 0<br>
     * <p/>
     * @return A new field
     */
    public NumberField snakeRotateRowsLeft()
    {
        T[] arr = asFlatArray();
        T temp = arr[0];
        System.arraycopy(arr, 1, arr, 0, arr.length - 1);
        arr[arr.length - 1] = temp;
        return NumberFieldFactory.fromArray(arr[0].getClass(), arr, width, height);
    }

    /**
     * Does a "down snake rotate" of all colums
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 8 6 7<br>
     * 0 1 2<br>
     * 3 4 5<br>
     * <p/>
     * @return A new field
     */
    public NumberField snakeRotateColumnsDown()
    {
        return transpose().snakeRotateRowsRight().transpose();
    }

    /**
     * Does a "up snake rotate" of all colums
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 3 4 5<br>
     * 6 7 8<br>
     * 1 2 0<br>
     * <p/>
     * @return A new field
     */
    public NumberField snakeRotateColumnsUp()
    {
        return transpose().snakeRotateRowsLeft().transpose();
    }

    /**
     * Rotates all columns of field down
     *
     * @return A new field
     */
    public NumberField rotateColumnsDown()
    {
        return transpose().rotateRowsRight().transpose();
    }

    /**
     * Rotates all columns of field Up
     *
     * @return A new field
     */
    public NumberField rotateColumnsUp()
    {
        return transpose().rotateRowsLeft().transpose();
    }

    /**
     * Reverses one row
     *
     * @param row Row to reverse
     * @return A new field
     */
    public NumberField reverseRow(int row)
    {
        NumberField m = new NumberField(this);
        for (int b = 0; b < width; b++)
        {
            m.values[row][b] = values[row][width - 1 - b];
        }
        return m;
    }

    /**
     * Reverses one column
     *
     * @param col column to reverse
     * @return A new field
     */
    public NumberField reverseColumn(int col)
    {
        return transpose().reverseRow(col).transpose();
    }

    /**
     * Reverses all rows of a field
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 2 1 0<br>
     * 5 4 3<br>
     * 8 7 6<br>
     * <p/>
     * @return A new field
     */
    public NumberField reverseRows()
    {
        NumberField m = new NumberField(this);
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                m.values[a][b] = values[a][width - 1 - b];
            }
        }
        return m;
    }

    /**
     * Swaps all rows
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 6 7 8<br>
     * 3 4 5<br>
     * 0 1 2<br>
     * <p/>
     * @return A new field
     */
    public NumberField swapRows()
    {
        NumberField m = new NumberField (this.values[0].getClass(), getHeight(), getWidth());
        int half = height / 2;
        for (int a = 0; a < half; a++)
        {
            System.arraycopy(values[a], 0, m.values[height - a - 1], 0, width);
            System.arraycopy(values[height - a - 1], 0, m.values[a], 0, width);
        }
        if ((height & 1) == 1)
        {
            System.arraycopy(values[half], 0, m.values[half], 0, width);
        }
        return m;
    }

    /**
     * Swaps all columns
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 2 1 0<br>
     * 5 4 3<br>
     * 8 7 6<br>
     * <p/>
     * @return A new field
     */
    public NumberField swapColumns()
    {
        return transpose().swapRows().transpose();
    }

    /**
     * Reverses all columns of a field
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 6 7 8<br>
     * 5 4 3<br>
     * 0 1 2<br>
     * <p/>
     * @return A new field
     */
    public NumberField reverseColumns()
    {
        return transpose().reverseRows().transpose();
    }

    /**
     * Reverses the entire field
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 8 7 6<br>
     * 5 4 3<br>
     * 2 1 0<br>
     * <p/>
     * @return A new field
     */
    public NumberField reverse()
    {
        return reverseRows().transpose().reverseRows().transpose();
    }

    /**
     * Rotates entire field left
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 2 5 8<br>
     * 1 4 7<br>
     * 0 3 6<br>
     * <p/>
     * @return A new field
     */
    public NumberField rotateLeft()
    {
        return reverseRows().transpose();
    }

    /**
     * Rotates entire field right
     * <p/>
     * 0 1 2<br>
     * 3 4 5<br>
     * 6 7 8<br>
     * <p/>
     * will evaluate to
     * <p/>
     * 6 3 0<br>
     * 7 4 1<br>
     * 8 5 2<br>
     * <p/>
     * @return A new field
     */
    public NumberField rotateRight()
    {
        return transpose().reverseRows();
    }

    /**
     * Generates a sub field of this field
     *
     * @param startx Starting X position
     * @param endx Last X position
     * @param starty Starting Y position
     * @param endy Last Y position
     * @return A new field
     */
    public NumberField getSubField(int startx, int endx, int starty, int endy)
    {
        endx++;
        endy++;
        NumberField m = new NumberField (this.values[0].getClass(), endx - startx, endy - starty);
        {
            for (int a = starty; a < endy; a++)
            {
                System.arraycopy(values[a], startx, m.values[a - starty], 0, endx - startx);
            }
        }
        return m;
    }

    /**
     * Sets sub field to a fixed value
     *
     * @param startx Starting X position
     * @param endx Last X position
     * @param starty Starting Y position
     * @param endy Last Y position
     * @param value The value
     * @return A new field
     */
    public NumberField setSubField(int startx, int endx, int starty, int endy, Number value)
    {
        endx++;
        endy++;
        NumberField m = new NumberField(this);
        {
            for (int a = starty; a < endy; a++)
            {
                for (int b = startx; b < endx; b++)
                {
                    m.values[a][b] = createNumberObject(value.intValue()); //(value);
                }
            }
        }
        return m;
    }

    /**
     * Inserts field f into this field
     *
     * @param startx X Where to insert
     * @param starty Y Where to insert
     * @param f Field to be inserted
     * @return A new result field
     */
    public NumberField setSubField(int startx, int starty, NumberField f)
    {
        int endx = f.getWidth() + startx;
        int endy = f.getHeight() + starty;
        NumberField m = new NumberField(this);
        for (int a = starty; a < endy; a++)
        {
            System.arraycopy(f.values[a - starty], 0, m.values[a], startx, endx - startx);
        }
        return m;
    }

    /**
     * Expands this field by adding another field to the right
     *
     * @param f Other field
     * @return A new field
     */
    public NumberField appendRight(NumberField f)
    {
        NumberField m = new NumberField (this.values[0].getClass(), width + f.getWidth(), Math.max(height, f.getHeight()));
        m = m.setSubField(0, 0, this);
        m = m.setSubField(width, 0, f);
        return m;
    }

    /**
     * Expands this field by adding another field to the left
     *
     * @param f Other field
     * @return A new field
     */
    public NumberField appendLeft(NumberField f)
    {
        NumberField m = new NumberField (this.values[0].getClass(), width + f.getWidth(), Math.max(height, f.getHeight()));
        m = m.setSubField(0, 0, f);
        m = m.setSubField(f.getWidth(), 0, this);
        return m;
    }

    /**
     * Expands this field by adding another field to the bottom
     *
     * @param f Other field
     * @return A new field
     */
    public NumberField appendBottom(NumberField f)
    {
        NumberField m = new NumberField (this.values[0].getClass(), Math.max(width, f.getWidth()), height + f.getHeight());
        m = m.setSubField(0, 0, this);
        m = m.setSubField(0, height, f);
        return m;
    }

    /**
     * Expands this field by adding another field to the top
     *
     * @param f Other field
     * @return A new field
     */
    public NumberField appendTop(NumberField f)
    {
        NumberField m = new NumberField (this.values[0].getClass(), Math.max(width, f.getWidth()), height + f.getHeight());
        m = m.setSubField(0, 0, f);
        m = m.setSubField(0, f.getHeight(), this);
        return m;
    }

    /**
     * Appends a column to the right
     *
     * @return A new value
     */
    public NumberField appendColumnRight()
    {
        NumberField m = new NumberField (this.values[0].getClass(), width + 1, height);
        for (int a = 0; a < height; a++)
        {
            System.arraycopy(values[a], 0, m.values[a], 0, width);
        }
        return m;
    }

    /**
     * Appends a column to the right
     *
     * @param val Value of all elements of that column
     * @return A new value
     */
    public NumberField appendColumnRight(int val)
    {
        return appendColumnRight().setColumn(getWidth(), this.createNumberObject(""+val));
    }

    /**
     * Appends a column to the left
     *
     * @return A new value
     */
    public NumberField appendColumnLeft()
    {
        return appendColumnRight().rotateRowsRight();
    }

    /**
     * Appends a column to the left
     *
     * @param val Value of all elements of that column
     * @return A new value
     */
    public NumberField appendColumnLeft(int val)
    {
        return appendColumnLeft().setColumn (0, createNumberObject(val));
    }

    /**
     * Appends a row at the bottom
     *
     * @return A new value
     */
    public NumberField appendRowBottom()
    {
        NumberField m = new NumberField (this.values[0].getClass(), width, height + 1);
        for (int a = 0; a < height; a++)
        {
            System.arraycopy(values[a], 0, m.values[a], 0, width);
        }
        return m;
    }

    /**
     * Appends a row at the bottom
     *
     * @param val Value of all elements of that row
     * @return A new value
     */
    public NumberField appendRowBottom(int val)
    {
        return appendRowBottom().setRow(getHeight(), val);
    }

    /**
     * Appends a row at the top
     *
     * @return A new value
     */
    public NumberField appendRowTop()
    {
        return appendRowBottom().rotateColumnsDown();
    }

    /**
     * Appends a row at the top
     *
     * @param val Value of all elements of that row
     * @return A new value
     */
    public NumberField appendRowTop(int val)
    {
        return appendRowTop().setRow(0, val);
    }

    /**
     * Appends columns and rows at either side
     *
     * @return A new value
     */
    public NumberField appendFrame()
    {
        return appendRowBottom().appendRowTop().appendColumnLeft().appendColumnRight();
    }

    /**
     * Appends a frame around the field
     *
     * @param val Value of all frame elements
     * @return The new field
     */
    public NumberField appendFrame(int val)
    {
        return appendRowBottom(val).appendRowTop(val).appendColumnLeft(val).appendColumnRight(val);
    }

//////////////////////////////// Informational stuff //////////////////////////////////////////////
    @Override
    public boolean equals(Object n)
    {
        return n.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode()
    {
        Object[] a =
        {
            this
        };
        return Arrays.deepHashCode(a);
    }

    /**
     * Returns string representation
     *
     * @return String describing this field
     */
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                s.append (values[a][b]).append("  ");
            }
            s.append("\n");
        }
        return s.toString();
    }

    public String toCharString()
    {
        StringBuilder s = new StringBuilder();
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                s.append((char)values[a][b].intValue());
                s.append (" ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    
    public void print()
    {
        System.out.println(toString());
    }

    public String toString2()
    {
        StringBuilder s = new StringBuilder();
        Map<T, Double> map = calcStats();
        double[] rowSums = rowSums();
        double[] colSums = colSums();
        double[] diffs = new double[Math.max(rowSums.length, colSums.length)];
        for (int n = 0; n < diffs.length; n++)
        {
            if (n >= rowSums.length || n >= colSums.length)
            {
                diffs[n] = 0;
            }
            else
            {
                diffs[n] = Math.abs(rowSums[n] - colSums[n]);
            }
        }

        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                s.append(values[a][b]).append(" ");
            }
            s.append("\n");
        }
        s.append('[');
        s.append(width);
        s.append('*');
        s.append(height);
        s.append("] ");
        s.append(map.size());
        s.append(" different Values\n");
        s.append(map);
        s.append('\n');
        s.append("TotalSum: ");
        s.append(totalSum());
        s.append('\n');
        s.append("RowSums: ");
        s.append(Arrays.toString(rowSums));
        s.append('\n');
        s.append("ColSums: ");
        s.append(Arrays.toString(colSums));
        s.append('\n');
        s.append("AbsDiffs: ");
        s.append(Arrays.toString(diffs));
        s.append(" (");
        s.append(arraySum(diffs));
        s.append(')');
        s.append('\n');
        return s.toString();
    }

    public void print2()
    {
        System.out.println(toString2());
    }

    public String getStats()
    {
        return "[" + width + "*" + height + "]" + calcStats().toString();
    }

    public Map<T, Double> calcStats()
    {
        TreeMap<T, Double> map = new TreeMap<>();
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                T key = values[a][b];
                Double count = map.get(key);
                if (count == null)
                {
                    count = 1d;
                }
                else
                {
                    count++;
                }
                map.put(key, count);
            }
        }
        return map;
    }

    public double[] rowSums()
    {
        double[] sums = new double[height];
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                sums[a] += values[a][b].doubleValue();
            }
        }
        return sums;
    }

    public double[] rowMult()
    {
        double[] sums = new double[height];
        for (int s = 0; s < height; s++)
        {
            sums[s] = 1;
        }
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                sums[a] *= values[a][b].doubleValue();
            }
        }
        return sums;
    }

    public double[] colSums()
    {
        return transpose().rowSums();
    }

    public double[] colMult()
    {
        return transpose().rowMult();
    }

    public double totalSum()
    {
        double sum = 0;
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                sum += values[a][b].doubleValue();
            }
        }
        return sum;
    }

    private double arraySum(double[] arr)
    {
        int sum = 0;
        for (double anArr : arr)
        {
            sum += anArr;
        }
        return sum;
    }

    public double smallestValue()
    {
        double ret = Double.MAX_VALUE;
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (values[a][b].doubleValue() < ret)
                {
                    ret = values[a][b].doubleValue();
                }
            }
        }
        return ret;
    }

    public double biggestValue()
    {
        Double ret = Double.MIN_VALUE;
        for (int a = 0; a < height; a++)
        {
            for (int b = 0; b < width; b++)
            {
                if (values[a][b].doubleValue() > ret)
                {
                    ret = values[a][b].doubleValue();
                }
            }
        }
        return ret;
    }
}
