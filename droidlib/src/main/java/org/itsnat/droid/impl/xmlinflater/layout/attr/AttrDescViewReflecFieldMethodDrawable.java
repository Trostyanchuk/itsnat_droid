package org.itsnat.droid.impl.xmlinflater.layout.attr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescViewReflecFieldMethodDrawable extends AttrDescViewReflecFieldMethod
{
    protected String defaultValue;

    public AttrDescViewReflecFieldMethodDrawable(ClassDescViewBased parent, String name, String fieldName, String methodName, Class methodClass, String defaultValue)
    {
        super(parent,name,fieldName,methodName,methodClass,Drawable.class);
        this.defaultValue = defaultValue;
    }

    public void setAttribute(final View view, final DOMAttr attr,final AttrLayoutContext attrCtx)
    {
        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                Drawable convertedValue = getDrawable(attr,attrCtx.getContext(),attrCtx.getXMLInflaterLayout());
                callFieldMethod(view, convertedValue);
            }
        };
        if (DOMAttrRemote.isPendingToDownload(attr))
            processDownloadTask((DOMAttrRemote)attr,task,attrCtx.getXMLInflaterLayout());
        else
            task.run();
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        if (defaultValue != null) // Para especificar null se ha de usar "@null"
            setToRemoveAttribute(view, defaultValue, xmlInflaterLayout, ctx); // defaultValue puede ser null (ej attr background), también valdría "@null" en el atributo
    }
}
