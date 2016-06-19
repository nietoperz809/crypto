import com.peter.crypto.numberfield2d.NumberField;
import com.peter.crypto.numberfield2d.NumberFieldFactory;


public class Test
{
    public static void main (String[] args) throws Exception
    {
        NumberField n =
                NumberFieldFactory.countedSpiralRightDown (Byte.class, 5, "motherfucker");
        System.out.println(n);

//        BigInteger bi = PiDigitsHex.packedPi(0,1000);
//        byte[] bb = bi.toByteArray();
//        IO.writeFile("c:\\pidigits2.bin", bb);
    }
}

