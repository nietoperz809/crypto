package com.peter.crypto.unix;

import java.math.BigDecimal;

public class UnixMath
{
    private static final double invpi = 1.27323954473516268;
    private static final double twoopi = 0.63661977236758134308;
    private static final double log2 = 0.693147180559945309e0;
    private static final double ln10 = 2.302585092994045684;
    private static final double sqrto2 = 0.707106781186547524e0;
    private static final double log2e = 1.4426950408889634073599247;
    private static final double sqrt2 = 1.4142135623730950488016887;
    private static final double maxf = 10000;

    private static final double sinp0 = .1357884097877375669092680e8;
    private static final double sinp1 = -.4942908100902844161158627e7;
    private static final double sinp2 = .4401030535375266501944918e6;
    private static final double sinp3 = -.1384727249982452873054457e5;
    private static final double sinp4 = .1459688406665768722226959e3;
    private static final double sinq0 = .8644558652922534429915149e7;
    private static final double sinq1 = .4081792252343299749395779e6;
    private static final double sinq2 = .9463096101538208180571257e4;
    private static final double sinq3 = .1326534908786136358911494e3;

    private static final double logp0 = -.240139179559210510e2;
    private static final double logp1 = 0.309572928215376501e2;
    private static final double logp2 = -.963769093368686593e1;
    private static final double logp3 = 0.421087371217979714e0;
    private static final double logq0 = -.120069589779605255e2;
    private static final double logq1 = 0.194809660700889731e2;
    private static final double logq2 = -.891110902798312337e1;

    private static final double expp0 = .2080384346694663001443843411e7;
    private static final double expp1 = .3028697169744036299076048876e5;
    private static final double expp2 = .6061485330061080841615584556e2;
    private static final double expq0 = .6002720360238832528230907598e7;
    private static final double expq1 = .3277251518082914423057964422e6;
    private static final double expq2 = .1749287689093076403844945335e4;

    private static final double tanp0 = -0.1306820264754825668269611177e+5;
    private static final double tanp1 = 0.1055970901714953193602353981e+4;
    private static final double tanp2 = -0.1550685653483266376941705728e+2;
    private static final double tanp3 = 0.3422554387241003435328470489e-1;
    private static final double tanp4 = 0.3386638642677172096076369e-4;
    private static final double tanq0 = -0.1663895238947119001851464661e+5;
    private static final double tanq1 = 0.4765751362916483698926655581e+4;
    private static final double tanq2 = -0.1555033164031709966900124574e+3;

    private static final double p0 = -0.6307673640497716991184787251e+6;
    private static final double p1 = -0.8991272022039509355398013511e+5;
    private static final double p2 = -0.2894211355989563807284660366e+4;
    private static final double p3 = -0.2630563213397497062819489e+2;
    private static final double q0 = -0.6307673640497716991212077277e+6;
    private static final double q1 = 0.1521517378790019070696485176e+5;
    private static final double q2 = -0.173678953558233699533450911e+3;

    /**
     * UNIX V6/7	
     * sinh(arg) returns the hyperbolic sine of its floating-
	point argument.

	The exponential function is called for arguments
	greater in magnitude than 0.5.

	A series is used for arguments smaller in magnitude than 0.5.
	The coefficients are #2029 from Hart & Cheney. (20.36D)

     * @param arg input value
     * @return sinh(arg)
     */
    public static double sinh(double arg)
    {
        double temp, argsq;
        int sign;

        sign = 1;
        if (arg < 0)
        {
            arg = -arg;
            sign = -1;
        }

        if (arg > 21.)
        {
            temp = exp(arg) / 2;
            if (sign > 0)
            {
                return (temp);
            }
            else
            {
                return (-temp);
            }
        }

        if (arg > 0.5)
        {
            return (sign * (exp(arg) - exp(-arg)) / 2);
        }

        argsq = arg * arg;
        temp = (((p3 * argsq + p2) * argsq + p1) * argsq + p0) * arg;
        temp /= (((argsq + q2) * argsq + q1) * argsq + q0);
        return (sign * temp);
    }

    /**
     * UNIX V6/7
     * 	cosh(arg) is computed from the exponential function for
	all arguments.

     * @param arg argument
     * @return cosh of arg
     */
    public static double cosh(double arg)
    {
        if (arg < 0)
        {
            arg = -arg;
        }
        if (arg > 21.)
        {
            return (exp(arg) / 2);
        }

        return ((exp(arg) + exp(-arg)) / 2);
    }

    /**
     * floating point tangent (Unix V6/7)
     *
     * A series is used after range reduction. Coefficients are #4285 from Hart
     * & Cheney. (19.74D)
     *
     * @param arg Input value
     * @return Result
     */
    public static double tan(double arg)
    {
        double sign, temp, e, x, xsq;
        int flag, i;

        flag = 0;
        sign = 1.;
        if (arg < 0.)
        {
            arg = -arg;
            sign = -1.;
        }
        arg = arg * invpi;   /*overflow?*/

        double[] dd = modf(arg);
        x = dd[1];
        e = dd[0];
        i = (int) e;
        switch (i % 4)
        {
            case 1:
                x = 1. - x;
                flag = 1;
                break;

            case 2:
                sign = -sign;
                flag = 1;
                break;

            case 3:
                x = 1. - x;
                sign = -sign;
                break;

            case 0:
                break;
        }

        xsq = x * x;
        temp = ((((tanp4 * xsq + tanp3) * xsq + tanp2) * xsq + tanp1) * xsq + tanp0) * x;
        temp = temp / (((1.0 * xsq + tanq2) * xsq + tanq1) * xsq + tanq0);

        if (flag == 1)
        {
            if (temp == 0.)
            {
//                if (sign > 0)
//                {
//                }
                throw new ArithmeticException();
            }
            temp = 1. / temp;
        }
        return (sign * temp);
    }

    /**
     * Ansi C ldexp
     *
     * @param x Multiplicator
     * @param exp Power of two
     * @return x * 2^exp
     */
    private static double ldexp(double x, int exp)
    {
        return x * Math.pow(2.0, exp);
    }

    /**
     * Unix V6/7 exp returns the exponential function of its floating-point
     * argument.
     *
     * The coefficients are #1069 from Hart and Cheney. (22.35D)
     *
     * @param arg
     * @return
     */
    public static double exp(double arg)
    {
        double fract;
        double temp1, temp2, xsq;
        int ent;

        if (arg == 0.)
        {
            return (1.);
        }
        if (arg < -maxf)
        {
            return (0.);
        }
        if (arg > maxf)
        {
            throw new ArithmeticException();
        }
        arg *= log2e;
        ent = (int) Math.floor(arg);
        fract = (arg - ent) - 0.5;
        xsq = fract * fract;
        temp1 = ((expp2 * xsq + expp1) * xsq + expp0) * fract;
        temp2 = ((1.0 * xsq + expq2) * xsq + expq1) * xsq + expq0;
        return (ldexp(sqrt2 * (temp2 + temp1) / (temp2 - temp1), ent));
    }

    /**
     * Unix V6/7
     *
     * @param arg Input
     * @return log10 of arg
     */
    public static double log10(double arg)
    {
        return (log(arg) / ln10);
    }

    /**
     * Unix V6/7
     *
     * @param arg input
     * @return log of arg
     */
    public static double log(double arg)
    {
        double x, z, zsq, temp;
        double exp;

        if (arg <= 0.)
        {
            throw new ArithmeticException();
        }
        double[] dd = frexp(arg);
        exp = dd[0];
        x = dd[1];
        while (x < 0.5)
        {
            x = x * 2;
            exp = exp - 1;
        }
        if (x < sqrto2)
        {
            x = 2 * x;
            exp = exp - 1;
        }

        z = (x - 1) / (x + 1);
        zsq = z * z;

        temp = ((logp3 * zsq + logp2) * zsq + logp1) * zsq + logp0;
        temp = temp / (((1.0 * zsq + logq2) * zsq + logq1) * zsq + logq0);
        temp = temp * z + exp * log2;
        return (temp);
    }

    /**
     * Peters version of frexp
     *
     * @param d input value
     * @return double array containing both values
     */
    private static double[] frexp(double d)
    {
        long bits = Double.doubleToLongBits(d);
        long exp = ((bits >> 52) & 0x7ff) - 1022;
        double mant = d / Math.pow(2, exp);
        return new double[]
        {
            exp, mant
        };
    }

    /**
     * UNIX V6/7 SQRT
     *
     * @param arg input
     * @return sqrt (arg)
     */
    public static double sqrt(double arg)
    {
        double x, temp;
        double exp;
        int i;

        if (arg < 0.)
        {
            throw new ArithmeticException();
        }
        if (arg <= 0.)
        {
            return (0.);
        }
        double dd[] = frexp(arg);
        exp = dd[0];
        x = dd[1];
        while (x < 0.5)
        {
            x *= 2;
            exp--;
        }
        /*
         * NOTE
         * this wont work on 1's comp
         */
        if (((int) exp & 1) == 1)
        {
            x *= 2;
            exp--;
        }
        temp = 0.5 * (1.0 + x);

        while (exp > 60)
        {
            temp *= (1L << 30);
            exp -= 60;
        }
        while (exp < -60)
        {
            temp /= (1L << 30);
            exp += 60;
        }
        if (exp >= 0)
        {
            temp *= 1L << ((int) exp / 2);
        }
        else
        {
            temp /= 1L << (-(int) exp / 2);
        }
        for (i = 0; i <= 4; i++)
        {
            temp = 0.5 * (temp + arg / temp);
        }
        return temp;
    }

    /**
     * UNIX cos
     *
     * @param arg Input value
     * @return cos(arg)
     */
    public static double cos(double arg)
    {
        if (arg < 0)
        {
            arg = -arg;
        }
        return (sinus(arg, 1));
    }

    /**
     * UNIX V6/7 sin
     *
     * @param arg input value
     * @return sin(arg)
     */
    public static double sin(double arg)
    {
        return (sinus(arg, 0));
    }

    /**
     * Peters mods
     *
     * @param d double array
     * @return double array, index 0 == intvalue, index 1 == remainder
     */
    private static double[] modf(double d)
    {
        BigDecimal bd = new BigDecimal(d);
        return new double[]
        {
            bd.intValue(),
            bd.remainder(BigDecimal.ONE).doubleValue()
        };
    }

    private static double sinus(double arg, int quad)
    {
        double e, f;
        double ysq;
        double x, y;
        int k;
        double temp1, temp2;

        x = arg;
        if (x < 0)
        {
            x = -x;
            quad = quad + 2;
        }
        x = x * twoopi;	/*underflow?*/

        if (x > 32764)
        {
            double[] dbl1 = modf(x);
            y = dbl1[1];
            e = dbl1[0];
            e = e + quad;
            double[] dbl2 = modf(0.25 * e);
            f = dbl2[0];
            quad = (int) (e - 4 * f);
        }
        else
        {
            k = (int) x;
            y = x - k;
            quad = (quad + k) & 03;
        }
        if ((quad & 01) == 1)
        {
            y = 1 - y;
        }
        if (quad > 1)
        {
            y = -y;
        }

        ysq = y * y;
        temp1 = ((((sinp4 * ysq + sinp3) * ysq + sinp2) * ysq + sinp1) * ysq + sinp0) * y;
        temp2 = ((((ysq + sinq3) * ysq + sinq2) * ysq + sinq1) * ysq + sinq0);
        return (temp1 / temp2);
    }
}
