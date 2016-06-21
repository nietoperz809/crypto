package com.stefanmuenchow.arithmetic.operation;

import com.stefanmuenchow.arithmetic.Operations;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class DoubleOperations implements Operations<Double>
{

	@Override
	public Double add(Double a, Double b) {
		return a + b;
	}

	@Override
	public Double sub(Double a, Double b) {
		return a - b;
	}

	@Override
	public Double mul(Double a, Double b) {
		return a * b;
	}

	@Override
	public Double div(Double a, Double b) {
		return a / b;
	}

	@Override
	public Double max(Double a, Double b) {
		return Math.max(a, b);
	}

	@Override
	public Double min(Double a, Double b) {
		return Math.min(a, b);	
	}
	
	@Override
	public Double abs(Double a) {
		return Math.abs(a);
	}

	@Override
	public Double neg(Double a) {
		return a * (-1);
	}

    @Override
    public Double mod (Double a, Double b)
    {
        return a % b;
    }

    @Override
	public Double and (Double a, Double b)
	{
		throw new NotImplementedException();
	}

    @Override
    public Double or (Double a, Double b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Double xor (Double a, Double b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Double equ (Double a, Double b)
    {
        throw new NotImplementedException();
    }

    @Override
    public Double imp (Double a, Double b)
    {
        throw new NotImplementedException();
    }
}
