package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.view.Gravity;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.drawable.DOMElemDrawable;
import org.itsnat.droid.impl.util.NamespaceUtil;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflater.XMLInflaterContext;
import org.itsnat.droid.impl.xmlinflater.XMLInflaterRegistry;
import org.itsnat.droid.impl.xmlinflater.drawable.AttrDrawableContext;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;
import org.itsnat.droid.impl.xmlinflater.shared.GravityUtil;
import org.itsnat.droid.impl.xmlinflater.shared.attr.AttrDesc;

import java.util.ArrayList;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescScaleDrawable extends ClassDescDrawableWrapper<ScaleDrawable>
{

    public ClassDescScaleDrawable(ClassDescDrawableMgr classMgr)
    {
        super(classMgr,"scale");
    }

    @Override
    public ElementDrawableRoot createElementDrawableRoot(DOMElemDrawable rootElem, AttrDrawableContext attrCtx)
    {
        ElementDrawableRoot elementDrawableRoot = new ElementDrawableRoot();

        XMLInflaterContext xmlInflaterContext = attrCtx.getXMLInflaterContext();

        XMLInflaterDrawable xmlInflaterDrawable = attrCtx.getXMLInflaterDrawable();
        xmlInflaterDrawable.processChildElements(rootElem, elementDrawableRoot,attrCtx);
        ArrayList<ElementDrawable> childList = elementDrawableRoot.getChildElementDrawableList();

        Drawable childDrawable = getChildDrawable("drawable", rootElem, xmlInflaterContext, childList);

        XMLInflaterRegistry xmlInflaterRegistry = classMgr.getXMLInflaterRegistry();

        DOMAttr attrGravity = rootElem.getDOMAttribute(NamespaceUtil.XMLNS_ANDROID, "scaleGravity");
        int gravity = attrGravity != null ? AttrDesc.parseMultipleName(attrGravity.getValue(), GravityUtil.nameValueMap) : Gravity.LEFT; // Valor concreto no puede ser un recurso

        DOMAttr attrScaleHeight = rootElem.getDOMAttribute(NamespaceUtil.XMLNS_ANDROID, "scaleHeight");
        float scaleHeight = attrScaleHeight != null ? xmlInflaterRegistry.getPercent(attrScaleHeight.getResourceDesc(), xmlInflaterContext) : -1;

        DOMAttr attrScaleWidth = rootElem.getDOMAttribute(NamespaceUtil.XMLNS_ANDROID, "scaleWidth");
        float scaleWidth = attrScaleWidth != null ? xmlInflaterRegistry.getPercent(attrScaleWidth.getResourceDesc(),xmlInflaterContext) : -1;

        elementDrawableRoot.setDrawable(new ScaleDrawable(childDrawable,gravity,scaleWidth,scaleHeight));

        return elementDrawableRoot;
    }

    @Override
    protected boolean isAttributeIgnored(DrawableOrElementDrawableWrapper draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;

        if (NamespaceUtil.XMLNS_ANDROID.equals(namespaceURI))
        {
            // Se usan en tiempo de construcción
            return ("drawable".equals(name) || "scaleGravity".equals(name) ||  "scaleHeight".equals(name) ||  "scaleWidth".equals(name));
        }
        return false;
    }


    @Override
    public Class<ScaleDrawable> getDrawableOrElementDrawableClass()
    {
        return ScaleDrawable.class;
    }

    protected void init()
    {
        super.init();

    }


}
