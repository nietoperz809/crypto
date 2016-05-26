import com.peter.crypto.IO;
import com.peter.crypto.pigenerators.ParallelPiHexEngine;
import com.peter.crypto.pigenerators.PiDigits;
import com.peter.crypto.pigenerators.PiDigits2;
import com.peter.crypto.pigenerators.PiDigitsHex;

import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Test
{
    public static void main (String[] args) throws Exception
    {
        BigInteger bi = PiDigitsHex.packedPi(0,1000);
        byte[] bb = bi.toByteArray();
        IO.writeFile("c:\\pidigits2.bin", bb);
    }
}

