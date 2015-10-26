package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrLocalResource;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflater.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public abstract class AttrDescView extends AttrDesc<ClassDescViewBased>
{
    private static Class class_R_styleable;

    public AttrDescView(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    protected static Class getClass_R_styleable()
    {
        if (class_R_styleable == null)
            class_R_styleable = MiscUtil.resolveClass("com.android.internal.R$styleable");
        return class_R_styleable;
    }


    protected void setToRemoveAttribute(View view, String value, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        // Este método es llamado desde removeAttribute, cuyo valor será o @null o un recurso de Android, no esperamos
        // nada dinámico (Remote o Asset), por eso hacemos cast sin complejos a AttrParsedLocalResource
        DOMAttrLocalResource attr = (DOMAttrLocalResource) DOMAttr.create(InflatedXML.XMLNS_ANDROID, getName(), value);
        setAttribute(view, attr, xmlInflaterLayout, ctx, null, null);
    }

    public abstract void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending);

    public abstract void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx);
}


