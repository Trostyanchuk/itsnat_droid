package org.itsnat.droid.impl.xmlinflated.values;

import android.content.Context;

import org.itsnat.droid.impl.ItsNatDroidImpl;
import org.itsnat.droid.impl.dom.values.XMLDOMValues;

/**
 * Created by jmarranz on 20/08/14.
 */
public class InflatedValuesStandalone extends InflatedValues
{
    public InflatedValuesStandalone(ItsNatDroidImpl itsNatDroid, XMLDOMValues xmlDOMValues, Context ctx)
    {
        super(itsNatDroid, xmlDOMValues,ctx);
    }
}
