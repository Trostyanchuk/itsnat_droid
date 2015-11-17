package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class _DELETED_AttrDescViewReflecMethodObject extends _DELETED_AttrDescViewReflecMethod
{
    public _DELETED_AttrDescViewReflecMethodObject(ClassDescViewBased parent, String name, String methodName)
    {
        super(parent,name,methodName,getClassParam());
    }

    public _DELETED_AttrDescViewReflecMethodObject(ClassDescViewBased parent, String name)
    {
        super(parent,name,getClassParam());
    }

    protected static Class<?> getClassParam()
    {
        return Object.class;
    }

    public void setAttribute(View view, DOMAttr attr, AttrLayoutContext attrCtx)
    {
        // El único caso que usa AttrDescReflecMethodObject es el atributo android:tag y sólo veo el caso de uso de ser una cadena
        CharSequence convValue = getText(attr.getValue(),attrCtx.getContext());
        callMethod(view, convValue);
    }

    public void removeAttribute(View view, AttrLayoutContext attrCtx)
    {
        callMethod(view, null);
    }

}
