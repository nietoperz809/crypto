import com.peter.crypto.galois.FiniteByteField;
import com.peter.crypto.galois.GaloisField256;
import com.peter.crypto.numberfield2d.NumberField;
import com.peter.crypto.numberfield2d.NumberFieldFactory;
import org.junit.Test;

/**
 * Created by Administrator on 12/21/2016.
 */
public class GaloisTest
{
    @Test
    public void TestMult()
    {
        int p = GaloisField256.Product(10,20);
        int q = FiniteByteField.mul((byte)10,(byte)20);
        System.out.println(p);
        System.out.println(q);
    }

    @Test
    public void TestMulTable()
    {
        Integer[] a = {1,2,3,4,5,6,7,8,9,10};
        NumberField n = NumberFieldFactory.galois256MultiplicationTable(Integer.class, a, a);
        NumberField m = NumberFieldFactory.galois256MultiplicationTable2(Integer.class, a, a);
        System.out.println(n);
        System.out.println(m);
    }

    @Test
    public void TestDivTable()
    {
        Integer[] a = {1,2,3,4,5,6,7,8,9,10};
        NumberField n = NumberFieldFactory.galois256DivisionTable(Integer.class, a, a);
        NumberField m = NumberFieldFactory.galois256DivisionTable2(Integer.class, a, a);
        System.out.println(n);
        System.out.println(m);
    }

}
