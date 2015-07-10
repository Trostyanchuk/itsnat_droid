package org.itsnat.droid.impl.util;

import android.content.res.Resources;
import android.util.TypedValue;

import org.itsnat.droid.ItsNatDroidException;

import java.io.UnsupportedEncodingException;

/**
 * Se debería usar TypedValue.complexToDimensionPixelOffset y complexToDimensionPixelSize
 * en el caso de necesitar enteros pero es un follón
 *
 * Created by jmarranz on 30/04/14.
 */
public class ValueUtil
{
    public static float dpToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, res.getDisplayMetrics());
    }

    public static int dpToPixelInt(float value,Resources res)
    {
        float valuePx = dpToPixel(value,res);
        return Math.round(valuePx);
    }

    public static float spToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, res.getDisplayMetrics());
    }

    public static float inToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_IN, value, res.getDisplayMetrics());
    }

    public static float mmToPixel(float value,Resources res)
    {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, value, res.getDisplayMetrics());
    }

    public static boolean isEmpty(String str)
    {
        return str == null || str.isEmpty();
    }

    public static String toString(byte[] data,String encoding)
    {
        try { return new String(data,encoding); }
        catch (UnsupportedEncodingException ex) { throw new ItsNatDroidException(ex); }
    }

    public static boolean equalsNullAllowed(Object value1, Object value2)
    {
        if (value1 != null)
            return value1.equals(value2);
        else if (value2 != null)
            return false;
        else
            return true; // Los dos son null
    }

    public static boolean equalsEmptyAllowed(String value1, String value2)
    {
        if (isEmpty(value1))  // null y "" son iguales en este caso
            return isEmpty(value2);
        else
            return value1.equals(value2);
    }

}
