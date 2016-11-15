package com.peter.crypto;


/******************************************************************************
 * Compilation:  javac Matrix.java
 * Execution:    java Matrix
 * <p>
 * A bare-bones collection of static methods for manipulating
 * matrices.
 ******************************************************************************/

final public class Matrix
{
    private final int thisM;             // number of rows
    private final int thisN;             // number of columns
    private final double[][] data;   // thisM-by-thisN array

    // create thisM-by-thisN matrix of 0's
    public Matrix (int M, int N)
    {
        this.thisM = M;
        this.thisN = N;
        data = new double[M][N];
    }

    // copy constructor
    private Matrix (Matrix A)
    {
        this(A.data);
    }

    // create matrix based on 2d array
    public Matrix (double[][] data)
    {
        thisM = data.length;
        thisN = data[0].length;
        this.data = new double[thisM][thisN];
        for (int i = 0; i < thisM; i++)
        {
            for (int j = 0; j < thisN; j++)
            {
                this.data[i][j] = data[i][j];
            }
        }
    }



    // return x^T y
    public static double dDot (double[] x, double[] y)
    {
        if (x.length != y.length)
        {
            throw new RuntimeException("Illegal vector dimensions.");
        }
        double sum = 0.0;
        for (int i = 0; i < x.length; i++)
        {
            sum += x[i] * y[i];
        }
        return sum;
    }

    // return c = a - b
    public static double[][] dSubtract (double[][] a, double[][] b)
    {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                c[i][j] = a[i][j] - b[i][j];
            }
        }
        return c;
    }

//    // test client
//    public static void main (String[] args)
//    {
//        StdOut.println("D");
//        StdOut.println("--------------------");
//        double[][] d = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
//        double[] v = {2, 4, 6};
//        double[] m1 = dMultiply(d, v);
//        double[] m2 = dMultiply(v, d);
//        StdArrayIO.print(m1);  // 28,64,100
//        StdArrayIO.print(m2);  // 60,72,84

//        StdArrayIO.print(d);
//        StdOut.println();
//
//        StdOut.println("I");
//        StdOut.println("--------------------");
//        double[][] c = Matrix.identity(5);
//        StdArrayIO.print(c);
//        StdOut.println();
//
//        StdOut.println("A");
//        StdOut.println("--------------------");
//        double[][] a = Matrix.random(5, 5);
//        StdArrayIO.print(a);
//        StdOut.println();
//
//        StdOut.println("A^T");
//        StdOut.println("--------------------");
//        double[][] b = Matrix.transpose(a);
//        StdArrayIO.print(b);
//        StdOut.println();
//
//        StdOut.println("A + A^T");
//        StdOut.println("--------------------");
//        double[][] e = Matrix.add(a, b);
//        StdArrayIO.print(e);
//        StdOut.println();
//
//        StdOut.println("A * A^T");
//        StdOut.println("--------------------");
//        double[][] f = Matrix.multiply(a, b);
//        StdArrayIO.print(f);
//        StdOut.println();
//    }

    // matrix-vector multiplication (y = A * x)
    public static double[] dMultiply (double[][] a, double[] x)
    {
        int m = a.length;
        int n = a[0].length;
        if (x.length != n)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[] y = new double[m];
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                y[i] += a[i][j] * x[j];
            }
        }
        return y;
    }

    // vector-matrix multiplication (y = x^T A)
    public static double[] dMultiply (double[] x, double[][] a)
    {
        int n = a.length;
        int m = a[0].length;
        if (x.length != m)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[] y = new double[n];
        for (int j = 0; j < n; j++)
        {
            for (int i = 0; i < m; i++)
            {
                y[j] += a[i][j] * x[i];
            }
        }
        return y;
    }

    // return n-by-n identity matrix I
    public static double[][] dIdentity (int n)
    {
        double[][] a = new double[n][n];
        for (int i = 0; i < n; i++)
        {
            a[i][i] = 1;
        }
        return a;
    }

    // return a random m-by-n matrix with values between 0 and 1
    public static double[][] dRandom (int m, int n)
    {
        double[][] a = new double[m][n];
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                a[i][j] = Math.random();
            }
        }
        return a;
    }

    // return B = A^T
    public static double[][] dTranspose (double[][] a)
    {
        int m = a.length;
        int n = a[0].length;
        double[][] b = new double[n][m];
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                b[j][i] = a[i][j];
            }
        }
        return b;
    }

    // return c = a + b
    public static double[][] dAdd (double[][] a, double[][] b)
    {
        int m = a.length;
        int n = a[0].length;
        double[][] c = new double[m][n];
        for (int i = 0; i < m; i++)
        {
            for (int j = 0; j < n; j++)
            {
                c[i][j] = a[i][j] + b[i][j];
            }
        }
        return c;
    }

    // return c = a * b
    public static double[][] dMultiply (double[][] a, double[][] b)
    {
        int m1 = a.length;
        int n1 = a[0].length;
        int m2 = b.length;
        int n2 = b[0].length;
        if (n1 != m2)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        double[][] c = new double[m1][n2];
        for (int i = 0; i < m1; i++)
        {
            for (int j = 0; j < n2; j++)
            {
                for (int k = 0; k < n1; k++)
                {
                    c[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return c;
    }

    // create and return a random thisM-by-thisN matrix with values between 0 and 1
    public static Matrix random (int M, int N)
    {
        Matrix A = new Matrix(M, N);
        for (int i = 0; i < M; i++)
        {
            for (int j = 0; j < N; j++)
            {
                A.data[i][j] = Math.random();
            }
        }
        return A;
    }

    // create and return the thisN-by-thisN identity matrix
    public static Matrix identity (int N)
    {
        Matrix I = new Matrix(N, N);
        for (int i = 0; i < N; i++)
        {
            I.data[i][i] = 1;
        }
        return I;
    }

    // create and return the transpose of the invoking matrix
    public Matrix transpose ()
    {
        Matrix A = new Matrix(thisN, thisM);
        for (int i = 0; i < thisM; i++)
        {
            for (int j = 0; j < thisN; j++)
            {
                A.data[j][i] = this.data[i][j];
            }
        }
        return A;
    }

    // return C = A + B
    public Matrix plus (Matrix B)
    {
        Matrix A = this;
        if (B.thisM != A.thisM || B.thisN != A.thisN)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        Matrix C = new Matrix(thisM, thisN);
        for (int i = 0; i < thisM; i++)
        {
            for (int j = 0; j < thisN; j++)
            {
                C.data[i][j] = A.data[i][j] + B.data[i][j];
            }
        }
        return C;
    }

    // return C = A - B
    public Matrix minus (Matrix B)
    {
        Matrix A = this;
        if (B.thisM != A.thisM || B.thisN != A.thisN)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        Matrix C = new Matrix(thisM, thisN);
        for (int i = 0; i < thisM; i++)
        {
            for (int j = 0; j < thisN; j++)
            {
                C.data[i][j] = A.data[i][j] - B.data[i][j];
            }
        }
        return C;
    }

    // does A = B exactly?
    @Override
    public boolean equals (Object ob)
    {
        Matrix B = (Matrix)ob;
        Matrix A = this;
        if (B.thisM != A.thisM || B.thisN != A.thisN)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        for (int i = 0; i < thisM; i++)
        {
            for (int j = 0; j < thisN; j++)
            {
                if (A.data[i][j] != B.data[i][j])
                {
                    return false;
                }
            }
        }
        return true;
    }

    // return C = A * B
    public Matrix times (Matrix B)
    {
        Matrix A = this;
        if (A.thisN != B.thisM)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }
        Matrix C = new Matrix(A.thisM, B.thisN);
        for (int i = 0; i < C.thisM; i++)
        {
            for (int j = 0; j < C.thisN; j++)
            {
                for (int k = 0; k < A.thisN; k++)
                {
                    C.data[i][j] += (A.data[i][k] * B.data[k][j]);
                }
            }
        }
        return C;
    }

    // return x = A^-1 b, assuming A is square and has full rank
    public Matrix solve (Matrix rhs)
    {
        if (thisM != thisN || rhs.thisM != thisN || rhs.thisN != 1)
        {
            throw new RuntimeException("Illegal matrix dimensions.");
        }

        // create copies of the data
        Matrix A = new Matrix(this);
        Matrix b = new Matrix(rhs);

        // Gaussian elimination with partial pivoting
        for (int i = 0; i < thisN; i++)
        {

            // find pivot row and swap
            int max = i;
            for (int j = i + 1; j < thisN; j++)
            {
                if (Math.abs(A.data[j][i]) > Math.abs(A.data[max][i]))
                {
                    max = j;
                }
            }
            A.swap(i, max);
            b.swap(i, max);

            // singular
            if (A.data[i][i] == 0.0)
            {
                throw new RuntimeException("Matrix is singular.");
            }

            // pivot within b
            for (int j = i + 1; j < thisN; j++)
            {
                b.data[j][0] -= b.data[i][0] * A.data[j][i] / A.data[i][i];
            }

            // pivot within A
            for (int j = i + 1; j < thisN; j++)
            {
                double m = A.data[j][i] / A.data[i][i];
                for (int k = i + 1; k < thisN; k++)
                {
                    A.data[j][k] -= A.data[i][k] * m;
                }
                A.data[j][i] = 0.0;
            }
        }

        // back substitution
        Matrix x = new Matrix(thisN, 1);
        for (int j = thisN - 1; j >= 0; j--)
        {
            double t = 0.0;
            for (int k = j + 1; k < thisN; k++)
            {
                t += A.data[j][k] * x.data[k][0];
            }
            x.data[j][0] = (b.data[j][0] - t) / A.data[j][j];
        }
        return x;

    }

    // swap rows i and j
    private void swap (int i, int j)
    {
        double[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    // print matrix to standard output
    @Override
    public String toString ()
    {
        StringBuffer bu = new StringBuffer();
        for (int i = 0; i < thisM; i++)
        {
            for (int j = 0; j < thisN; j++)
            {
                bu.append(data[i][j]).append(' '); //StdOut.printf("%9.4f ", data[i][j]);
            }
            bu.append('\n');
        }
        return bu.toString();
    }


//        // test client
//        public static void main(String[] args) {
//            double[][] d = { { 1, 2, 3 }, { 4, 5, 6 }, { 9, 1, 3} };
//            Matrix D = new Matrix(d);
//            D.show();
//            StdOut.println();
//
//            Matrix A = Matrix.random(5, 5);
//            A.show();
//            StdOut.println();
//
//            A.swap(1, 2);
//            A.show();
//            StdOut.println();
//
//            Matrix B = A.transpose();
//            B.show();
//            StdOut.println();
//
//            Matrix C = Matrix.identity(5);
//            C.show();
//            StdOut.println();
//
//            A.plus(B).show();
//            StdOut.println();
//
//            B.times(A).show();
//            StdOut.println();
//
//            // shouldn't be equal since AB != BA in general
//            StdOut.println(A.times(B).eq(B.times(A)));
//            StdOut.println();
//
//            Matrix b = Matrix.random(5, 1);
//            b.show();
//            StdOut.println();
//
//            Matrix x = A.solve(b);
//            x.show();
//            StdOut.println();
//
//            A.times(x).show();
//
//        }
}

