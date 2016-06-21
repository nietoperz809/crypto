package com.peter.crypto.numberfield2d;

import com.stefanmuenchow.arithmetic.Arithmetic;

import static java.lang.Math.min;
import static java.lang.Math.abs;

/**
 * LU decomposition (with pivoting) of a matrix A.
 * For an m-by-n matrix A, with m&gt;=n, the LU decomposition is
 * A(piv,:) = L*U, where L is an m-by-n unit lower triangular matrix,
 * U is an n-by-n upper-triangular matrix, and piv is a permutation
 * vector of length m.
 * <p/>
 * The LU decomposition with pivoting always exists, even for singular
 * matrices A. The primary use of LU decomposition is in the solution of
 * square systems of simultaneous linear equations. These solutions will
 * fila if the matrix A is singular.
 * <p/>
 * This class was adapted from the package Jama, which was developed by
 * Joe Hicklin, Cleve Moler, and Peter Webb of The MathWorks, Inc., and by
 * Ronald Boisvert, Bruce Miller, Roldan Pozo, and Karin Remington of the
 * National Institue of Standards and Technology.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.09.15
 */
public class NumberFieldMath
{
    ///////////////////////////////////////////////////////////////////////////
    // private
    int _m, _n;
    Double[][] _lu;
    int[] _piv;
    int _pivsign;
    NumberField num;
    private int iDF = 0;

    /**
     * Constructs an LU decomposition for the specified matrix A.
     * @param a the matrix A.
     */
    public NumberFieldMath (NumberField a)
    {
        num = a;
        int m = _m = a.getHeight ();
        int n = _n = a.getWidth ();
        Double[][] lu = _lu = a.asDoubleArray ();
        _piv = new int[m];
        for (int i = 0; i < m; ++i)
            _piv[i] = i;
        _pivsign = 1;
        Double[] lurowi;
        Double[] lucolj = new Double[m];

        // A left-looking, dot-product, Crout/Doolittle algorithm.
        for (int j = 0; j < n; ++j)
        {

            // Copy the j'th column to reduce cost in inner dot-product loop.
            for (int i = 0; i < m; ++i)
                lucolj[i] = lu[i][j];

            // Apply previous transformations
            for (int i = 0; i < m; ++i)
            {
                lurowi = lu[i];

                // Dot product.
                int kmax = min (i, j);
                double s = 0.0;
                for (int k = 0; k < kmax; ++k)
                    s += lurowi[k] * lucolj[k];
                lurowi[j] = lucolj[i] -= s;
            }

            // Find pivot and exchange if necessary.
            int p = j;
            for (int i = j + 1; i < m; ++i)
            {
                if (abs (lucolj[i]) > abs (lucolj[p]))
                    p = i;
            }
            if (p != j)
            {
                for (int k = 0; k < n; ++k)
                {
                    double t = lu[p][k];
                    lu[p][k] = lu[j][k];
                    lu[j][k] = t;
                }
                int k = _piv[p];
                _piv[p] = _piv[j];
                _piv[j] = k;
                _pivsign = -_pivsign;
            }

            // Compute multipliers.
            if (j < m && lu[j][j] != 0.0)
            {
                for (int i = j + 1; i < m; ++i)
                    lu[i][j] /= lu[j][j];
            }
        }
    }

    /**
     * Determines whether the matrix A is non-singular.
     * @return true, if non-singular; false, otherwise.
     */
    public boolean isNonSingular ()
    {
        for (int j = 0; j < _n; ++j)
        {
            if (_lu[j][j] == 0.0)
                return false;
        }
        return true;
    }

    /**
     * Determines whether the matrix A is singular.
     * @return true, if singular; false, otherwise.
     */
    public boolean isSingular ()
    {
        return !isNonSingular ();
    }

    /**
     * Gets the m-by-n unit lower triangular matrix factor L.
     * @return the m-by-n factor L.
     */
    public NumberField getL ()
    {
        Double[][] l = Arithmetic.createNumberArray (Double.class, _m, _n);
        for (int i = 0; i < _m; ++i)
        {
            for (int j = 0; j < _n; ++j)
            {
                if (i > j)
                {
                    l[i][j] = _lu[i][j];
                }
                else if (i == j)
                {
                    l[i][j] = 1.0;
                }
                else
                {
                    l[i][j] = 0.0;
                }
            }
        }
        return new NumberField (_m, _n, l);
    }

    /**
     * Gets the n-by-n upper triangular matrix factor U.
     * @return the n-by-n matrix factor U.
     */
    public NumberField getU ()
    {
        Double[][] u = new Double[_n][_n];
        for (int i = 0; i < _n; ++i)
        {
            for (int j = 0; j < _n; ++j)
            {
                if (i <= j)
                {
                    u[i][j] = _lu[i][j];
                }
                else
                {
                    u[i][j] = 0.0;
                }
            }
        }
        return new NumberField (_n, _n, u);
    }

    /**
     * Gets the pivot vector, an array of length m.
     * @return the pivot vector.
     */
    public int[] getPivot ()
    {
        int[] p = new int[_m];
        for (int i = 0; i < _m; ++i)
            p[i] = _piv[i];
        return p;
    }

//    /**
//     * Returns the solution X of the system A*X = B.
//     * This solution exists only if the matrix A is non-singular.
//     * @param b a matrix of right-hand-side vectors. This matrix must
//     *          have the same number (m) of rows as the matrix A, but may have
//     *          any number of columns.
//     * @return the matrix solution X.
//     * @throws IllegalStateException if A is singular.
//     */
//    public NumberField solve (NumberField b)
//    {
//        // Copy of right-hand side with pivoting.
//        int nx = b.getN ();
//        DMatrix xx = b.get (_piv, 0, nx - 1);
//        double[][] x = xx.getArray ();
//
//        // Solve L*Y = B(piv,:).
//        for (int k = 0; k < _n; ++k)
//        {
//            for (int i = k + 1; i < _n; ++i)
//            {
//                for (int j = 0; j < nx; ++j)
//                {
//                    x[i][j] -= x[k][j] * _lu[i][k];
//                }
//            }
//        }
//
//        // Solve U*X = Y.
//        for (int k = _n - 1; k >= 0; --k)
//        {
//            for (int j = 0; j < nx; ++j)
//            {
//                x[k][j] /= _lu[k][k];
//            }
//            for (int i = 0; i < k; ++i)
//            {
//                for (int j = 0; j < nx; ++j)
//                {
//                    x[i][j] -= x[k][j] * _lu[i][k];
//                }
//            }
//        }
//        return xx;
//    }

    /**
     * Returns the determinant of the  matrix A.
     * @return the the determinant.
     * @throws IllegalStateException if A is not square.
     */
    public double det ()
    {
        double d = _pivsign;
        for (int j = 0; j < _n; ++j)
            d *= _lu[j][j];
        return d;
    }

    public double matrixNorm()
    {
        double m = 0;
        for (int a = 0; a < num.getHeight (); a++)
        {
            for (int b = 0; b < num.getWidth (); b++)
            {
                m = m + (num.values[a][b].intValue() * num.values[a][b].intValue());
            }
        }
        return Math.sqrt(m);
    }

    private Double[][] Transpose(Double[][] a)
    {
        int tms = a.length;

        Double m[][] = new Double[tms][tms];

        for (int i = 0; i < tms; i++)
        {
            for (int j = 0; j < tms; j++)
            {
                m[i][j] = a[j][i];
            }
        }

        return m;
    }


    private Double[][] UpperTriangle(Double[][] m)
    {
        double f1 = 0;
        double temp = 0;
        int tms = m.length;  // get This Matrix Size (could be smaller than global)
        int v = 1;

        iDF = 1;


        for (int col = 0; col < tms - 1; col++)
        {
            for (int row = col + 1; row < tms; row++)
            {
                v = 1;

                outahere:
                while (m[col][col] == 0)             // check if 0 in diagonal
                {                                   // if so switch until not
                    if (col + v >= tms)               // check if switched all rows
                    {
                        iDF = 0;
                        break outahere;
                    }
                    else
                    {
                        for (int c = 0; c < tms; c++)
                        {
                            temp = m[col][c];
                            m[col][c] = m[col + v][c];       // switch rows
                            m[col + v][c] = temp;
                        }
                        v++;                            // count row switchs
                        iDF = iDF * -1;                 // each switch changes determinant factor
                    }
                }

                if (m[col][col] != 0)
                {
                    try
                    {
                        f1 = (-1) * m[row][col] / m[col][col];
                        for (int i = col; i < tms; i++)
                        {
                            m[row][i] = f1 * m[col][i] + m[row][i];
                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Still Here!!!");
                    }
                }
            }
        }
        return m;
    }

    private double Determinant(Double[][] matrix)
    {
        int tms = matrix.length;

        double det = 1;

        matrix = UpperTriangle(matrix);

        for (int i = 0; i < tms; i++)
        {
            det = det * matrix[i][i];
        }      // multiply down diagonal

        det = det * iDF;                    // adjust w/ determinant factor

        return det;
    }

    private Double[][] adjoint (Double[][] a) // throws Exception
    {
        int tms = a.length;

        Double m[][] = new Double [tms][tms];

        int ii, jj, ia, ja;
        double det;

        for (int i = 0; i < tms; i++)
        {
            for (int j = 0; j < tms; j++)
            {
                ia = ja = 0;

                Double ap[][] = new Double[tms - 1][tms - 1];

                for (ii = 0; ii < tms; ii++)
                {
                    for (jj = 0; jj < tms; jj++)
                    {

                        if ((ii != i) && (jj != j))
                        {
                            ap[ia][ja] = a[ii][jj];
                            ja++;
                        }

                    }
                    if ((ii != i) && (jj != j))
                    {
                        ia++;
                    }
                    ja = 0;
                }

                det = Determinant(ap);
                m[i][j] = Math.pow(-1, i + j) * det;
            }
        }

        m = Transpose(m);

        return m;
    }

    private Double[][] Inverse (Double[][] a)
    {
        // Formula used to Calculate Inverse:
        // inv(A) = 1/det(A) * adj(A)
        int tms = a.length;

        Double m[][] = new Double[tms][tms];
        Double mm[][] = adjoint(a);

        double det = Determinant(a);
        double dd = 0;

        if (det == 0)
        {
            return null;
        }
        else
        {
            dd = 1 / det;
        }

        for (int i = 0; i < tms; i++)
        {
            for (int j = 0; j < tms; j++)
            {
                m[i][j] = dd * mm[i][j];
            }
        }

        return m;
    }

    private Double[][] Multiply (Double[][] a, Double[][] b)
    {

        int tms = a.length;
        int tmsB = b.length;
        if (tms != tmsB)
        {
            return null;
        }

        Double matrix[][] = new Double[tms][tms];

        for (int i = 0; i < tms; i++)
        {
            for (int j = 0; j < tms; j++)
            {
                matrix[i][j] = 0.0;
            }
        }

        for (int i = 0; i < tms; i++)
        {
            for (int j = 0; j < tms; j++)
            {
                for (int p = 0; p < tms; p++)
                {
                    matrix[i][j] += a[i][p] * b[p][j];
                }
            }
        }

        return matrix;
    }

    public NumberField upperTriangle()
    {
        Double[][] a= UpperTriangle(num.asDoubleArray());
        return NumberFieldFactory.fromArray(a);
    }

    public NumberField adjoint() // throws Exception
    {
        Double[][] a = num.asDoubleArray();
        a = adjoint(a);
        if (a == null)
            return null;
        return NumberFieldFactory.fromArray(a);
    }

    public NumberField inverse()
    {
        Double[][] a = num.asDoubleArray();
        a = Inverse(a);
        if (a == null)
            return null;
        return NumberFieldFactory.fromArray(a);
    }

    public NumberField multiply (NumberField other)
    {
        Double[][] a = Multiply (num.asDoubleArray(), other.asDoubleArray());
        if (a == null)
            return null;
        return NumberFieldFactory.fromArray(a);
    }
}
