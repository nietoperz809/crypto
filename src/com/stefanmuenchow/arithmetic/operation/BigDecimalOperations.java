package com.stefanmuenchow.arithmetic.operation;

import com.stefanmuenchow.arithmetic.Operations;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;

public class BigDecimalOperations implements Operations<BigDecimal>
{

    @Override
    public BigDecimal add (BigDecimal a, BigDecimal b)
    {
        return a.add(b);
    }

    @Override
    public BigDecimal sub (BigDecimal a, BigDecimal b)
    {
        return a.subtract(b);
    }

    @Override
    public BigDecimal mul (BigDecimal a, BigDecimal b)
    {
        return a.multiply(b);
    }

    @Override
    public BigDecimal div (BigDecimal a, BigDecimal b)
    {
        return a.divide(b);
    }

    @Override
    public BigDecimal max (BigDecimal a, BigDecimal b)
    {
        return a.max(b);
    }

    @Override
    public BigDecimal min (BigDecimal a, BigDecimal b)
    {
        return a.min(b);
    }

    @Override
    public BigDecimal abs (BigDecimal a)
    {
        return a.abs();
    }

    @Override
    public BigDecimal neg (BigDecimal a)
    {
        return a.negate();
    }

    @Override
    public BigDecimal mod (BigDecimal a, BigDecimal b)
    {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal and (BigDecimal a, BigDecimal b)
    {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal or (BigDecimal a, BigDecimal b)
    {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal xor (BigDecimal a, BigDecimal b)
    {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal equ (BigDecimal a, BigDecimal b)
    {
        throw new NotImplementedException();
    }

    @Override
    public BigDecimal imp (BigDecimal a, BigDecimal b)
    {
        throw new NotImplementedException();
    }

    @Override
    public int compare (BigDecimal a, BigDecimal b)
    {
        return a.compareTo(b);
    }
}
