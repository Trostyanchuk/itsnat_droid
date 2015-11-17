package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.util.MapSmart;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 1/05/14.
 */
public abstract class _DELETED_AttrDescViewReflecMethodNameBased<Treturn> extends _DELETED_AttrDescViewReflecMethod
{
    protected MapSmart<String, Treturn> valueMap;
    protected String defaultName;

    public _DELETED_AttrDescViewReflecMethodNameBased(ClassDescViewBased parent, String name, String methodName, Class classParam, MapSmart<String, Treturn> valueMap, String defaultName)
    {
        super(parent,name,methodName,classParam);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public _DELETED_AttrDescViewReflecMethodNameBased(ClassDescViewBased parent, String name, Class classParam, MapSmart<String, Treturn> valueMap, String defaultName)
    {
        super(parent, name,classParam);
        this.valueMap = valueMap;
        this.defaultName = defaultName;
    }

    public void setAttribute(View view, DOMAttr attr, AttrLayoutContext attrCtx)
    {
        Treturn valueRes = parseNameBasedValue(attr.getValue());
        callMethod(view, valueRes);
    }

    public void removeAttribute(View view, AttrLayoutContext attrCtx)
    {
        if (defaultName != null)
        {
            if (defaultName.equals("")) callMethod(view, -1); // Android utiliza el -1 de vez en cuando como valor por defecto
            else setToRemoveAttribute(view, defaultName,attrCtx);
        }
    }

    protected abstract Treturn parseNameBasedValue(String value);
}
