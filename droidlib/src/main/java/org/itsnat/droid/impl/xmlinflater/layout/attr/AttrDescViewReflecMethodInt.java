package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecMethodInt extends AttrDescViewReflecMethod
{
    protected Integer defaultValue;

    public AttrDescViewReflecMethodInt(ClassDescViewBased parent, String name, String methodName, Integer defaultValue)
    {
        super(parent,name,methodName,getClassParam());
        this.defaultValue = defaultValue;
    }

    public AttrDescViewReflecMethodInt(ClassDescViewBased parent, String name, Integer defaultValue)
    {
        super(parent,name,getClassParam());
        this.defaultValue = defaultValue;
    }

    protected static Class<?> getClassParam()
    {
        return int.class;
    }

    public void setAttribute(View view, DOMAttr attr, AttrLayoutContext attrCtx)
    {
        int convValue = getInteger(attr.getValue(),attrCtx.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        // En el caso de defaultValue nulo es que no sabemos qué poner, es el caso por ejemplo de poner a cero el tamaño texto, no tiene sentido, se tendría que extraer el tamaño por defecto del Theme actual, un follón y total será muy raro
        if (defaultValue != null)
            callMethod(view, defaultValue);
    }
}
