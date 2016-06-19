/**
 * Copyright (c) Stefan Münchow. All rights reserved.
 * <p>
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 **/

package com.stefanmuenchow.arithmetic;

import com.stefanmuenchow.arithmetic.converter.ConverterRepository;
import com.stefanmuenchow.arithmetic.operation.OperationsRepository;

import java.lang.reflect.Array;

/**
 * Provides generic arithmetic for all subclasses of class {@link Number}.
 *
 * @author Stefan Münchow
 */
public class Arithmetic<X extends Number>
{
    private Class<X> targetClass;
    private X value;
    private transient TypeConverter<X> converter;
    private transient Operations<X> operations;

    /**
     * Create a new instance with initial value.
     *
     * @param value    Initial value
     */
    @SuppressWarnings("unchecked")
    public Arithmetic (X value)
    {
        this.targetClass = (Class<X>) value.getClass();
        this.value = value;
    }

    /**
     * Constructor
     * @param clazz Type of work Object of this class
     */
    public Arithmetic (Class<X> clazz)
    {
        try
        {
            this.targetClass = clazz;
            this.value = createNumberObject(clazz); //clazz.getConstructor (String.class).newInstance("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new target type for generic Arithmetic. After this method is called once,
     * the new target type is known by all newly created {@link Arithmetic} instances.
     *
     * @param <Y>			Target type
     * @param targetType    Class object of target type
     * @param converter        {@link TypeConverter} for target type
     * @param operations    {@link Operations} for target type
     */
    public static <Y extends Number> void addTargetType (Class<Y> targetType,
                                                         TypeConverter<Y> converter, Operations<Y> operations)
    {
        ConverterRepository.getInstance().addConverter(targetType, converter);
        OperationsRepository.getInstance().addOperations(targetType, operations);
    }

    /**
     * Return current value of this instance.
     * @return Current value of type X.
     */
    public X value ()
    {
        return value;
    }

    /**
     * Get the Type of work object used by this class
     * @return see above
     */
    public Class<X> type ()
    {
        return targetClass;
    }


    /**
     * Add operand to current value of this instance.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> add (A operand)
    {
        value = getOperations().add(value, convertNumber(operand));
        return this;
    }

    private Operations<X> getOperations ()
    {
        if (operations == null)
        {
            operations = OperationsRepository.getInstance().getOperations(targetClass);
        }
        return operations;
    }

    private X convertNumber (Number n)
    {
        if (converter == null)
        {
            converter = ConverterRepository.getInstance().getConverter(targetClass);
        }
        return converter.convertNumber(n);
    }

    /**
     * Subtract operand from current value of this instance.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> sub (A operand)
    {
        value = getOperations().sub(value, convertNumber(operand));
        return this;
    }

    /**
     * Divide current value of this instance by operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> div (A operand)
    {
        value = getOperations().div(value, convertNumber(operand));
        return this;
    }

    /**
     * Multiply current value of this instance with operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with modified value
     */
    public <A extends Number> Arithmetic<X> mul (A operand)
    {
        value = getOperations().mul(value, convertNumber(operand));
        return this;
    }

    /**
     * Return maximum value of current value of this instance and operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with maximum as new value
     */
    public <A extends Number> Arithmetic<X> max (A operand)
    {
        value = getOperations().max(value, convertNumber(operand));
        return this;
    }

    /**
     * Return minimum value of current value of this instance and operand.
     *
     * @param <A>		Operand type
     * @param operand    Operand value
     * @return            {@link Arithmetic} with minimum as new value
     */
    public <A extends Number> Arithmetic<X> min (A operand)
    {
        value = getOperations().min(value, convertNumber(operand));
        return this;
    }

    /**
     * Create absolute of the current value of this instance.
     *
     * @param <A>		Operand type
     * @return            {@link Arithmetic} with absolute value
     */
    public <A extends Number> Arithmetic<X> abs ()
    {
        value = getOperations().abs(value);
        return this;
    }

    /**
     * Negate the current value of this instance.
     *
     * @param <A>		Operand type
     * @return            {@link Arithmetic} with negated value
     */
    public <A extends Number> Arithmetic<X> neg ()
    {
        value = getOperations().neg(value);
        return this;
    }

    public <A extends Number> A[] createNumberArray (int size)
    {
        return (A[]) Array.newInstance(targetClass, size);
    }

    public <A extends Number> A[][] createNumberArray (int w, int h)
    {
        return (A[][]) Array.newInstance(targetClass, w,h);
    }

    public <A extends Number> A createNumberObject(String num)
    {
        try
        {
            return (A) targetClass.getConstructor (String.class).newInstance(num);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

/////////////////////// static funcs /////////////////////

    public static <A extends Number> A createNumberObject (Class<A> clazz)
    {
        try
        {
            return (A) clazz.getConstructor (String.class).newInstance("0");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <A extends Number> A createNumberObject (Class<A> clazz, String init)
    {
        try
        {
            return (A) clazz.getConstructor (String.class).newInstance(init);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static <A extends Number> A[] createNumberArray (Class<A> clazz, int size)
    {
        return (A[]) Array.newInstance(clazz, size);
    }

    public static <A extends Number> A[][] createNumberArray (Class<A> clazz, int w, int h)
    {
        return (A[][]) Array.newInstance(clazz, w,h);
    }
}
