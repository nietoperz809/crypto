import com.peter.crypto.Matrix;
import com.peter.crypto.numberfield2d.NumberFieldMath;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


/**
 * Created by Administrator on 11/7/2016.
 */
public class MatrixTest
{
    @Test
    public void TestMult()
    {
        double[][] d = {{1,2,3},{4,5,6},{7,8,9}};
        double[][] e = {{9,8,7},{6,5,4},{3,2,1}};
        Matrix m = new Matrix(d);
        Matrix n = new Matrix(e);
        Matrix p = m.times(n);
        Matrix q = n.times(m);
        System.out.println(p.toString());
        System.out.println(q.toString());
    }

    @Test
    public void TestDeterminant()
    {
        Double[][] d = {{1.0,2.0,3.0},{4.0,5.0,6.0},{7.0,8.0,9.0}};
        double det = NumberFieldMath.Determinant(d);
        assertEquals(det, 0.0, 1e-9);
    }

    @Test
    public void TestUpperTriangle()
    {
        Double[][] d = {{1.0,2.0,3.0},{4.0,5.0,6.0},{7.0,8.0,9.0}};
        Double[][] expect = {{1.0,2.0,3.0},{0.0,-3.0,-6.0},{0.0,0.0,0.0}};
        Double[][] e = NumberFieldMath.UpperTriangle(d);
        assertArrayEquals(e, expect);
    }

    @Test
    public void TestAdjoint()
    {
        Double[][] d = {{1.0,2.0,3.0},{4.0,5.0,6.0},{7.0,8.0,9.0}};
        Double[][] expect = {{-3.0,6.0,-3.0},{6.0,-12.0,6.0},{-3.0,6.0,-3.0}};
        Double[][] e = NumberFieldMath.adjoint(d);
//        System.out.println(Arrays.toString(f[0]));
//        System.out.println(Arrays.toString(f[1]));
//        System.out.println(Arrays.toString(f[2]));
        assertArrayEquals(e, expect);
    }

}
