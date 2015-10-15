package org.itsnat.droid.impl.xmlinflater.drawable.classtree;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawable;
import org.itsnat.droid.impl.xmlinflated.drawable.ElementDrawableRoot;
import org.itsnat.droid.impl.xmlinflated.drawable.LayerDrawableItem;
import org.itsnat.droid.impl.xmlinflater.drawable.ClassDescDrawableOrElementDrawableChildMgr;
import org.itsnat.droid.impl.xmlinflater.drawable.XMLInflaterDrawable;

import java.util.ArrayList;

/**
 * Created by jmarranz on 10/11/14.
 */
public class ClassDescLayerDrawable extends ClassDescRootElementDrawable<LayerDrawable>
{
    public ClassDescLayerDrawable(ClassDescDrawableOrElementDrawableChildMgr classMgr)
    {
        super(classMgr,"layer-list");
    }

    @Override
    public ElementDrawableRoot createRootElementDrawable(DOMElement rootElem, XMLInflaterDrawable inflaterDrawable, Context ctx)
    {
        // http://stackoverflow.com/questions/20120725/layerdrawable-programatically

        ElementDrawableRoot elementDrawableRoot = new ElementDrawableRoot();

        inflaterDrawable.processChildElements(rootElem,elementDrawableRoot);
        ArrayList<ElementDrawable> itemList = elementDrawableRoot.getChildElementDrawableList();
        Drawable[] drawableLayers = new Drawable[itemList.size()];
        for(int i = 0; i < itemList.size(); i++)
        {
            LayerDrawableItem item = (LayerDrawableItem)itemList.get(i);
            drawableLayers[i] = item.getDrawable();
        }

        LayerDrawable drawable = new LayerDrawable(drawableLayers);

        for(int i = 0; i < itemList.size(); i++)
        {
            LayerDrawableItem item = (LayerDrawableItem)itemList.get(i);

            drawable.setId(i,item.getId());
            drawable.setLayerInset(i,item.getLeft(),item.getTop(),item.getRight(),item.getBottom());
        }

        return new ElementDrawableRoot(drawable,itemList);
    }

    @Override
    protected boolean isAttributeIgnored(DrawableOrElementDrawableContainer draw,String namespaceURI,String name)
    {
        if (super.isAttributeIgnored(draw,namespaceURI,name))
            return true;
        return isSrcAttribute(namespaceURI, name); // Se trata de forma especial en otro lugar
    }

    private static boolean isSrcAttribute(String namespaceURI,String name)
    {
        return InflatedXML.XMLNS_ANDROID.equals(namespaceURI) && name.equals("src");
    }

    @Override
    public Class<LayerDrawable> getDrawableOrElementDrawableClass()
    {
        return LayerDrawable.class;
    }

    protected void init()
    {
        super.init();

        //addAttrDesc(new AttrDescDrawableReflecMethodBoolean(this, "dither", true));
    }


}
