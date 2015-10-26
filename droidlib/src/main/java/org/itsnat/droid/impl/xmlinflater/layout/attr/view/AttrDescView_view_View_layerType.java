package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layerType extends AttrDescView
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 3 );
    static
    {
        valueMap.put("none", View.LAYER_TYPE_NONE);
        valueMap.put("software",View.LAYER_TYPE_SOFTWARE);
        valueMap.put("hardware",View.LAYER_TYPE_HARDWARE);
    }

    public AttrDescView_view_View_layerType(ClassDescViewBased parent)
    {
        super(parent,"layerType");
    }

    public void setAttribute(View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        int convertedValue = AttrDescView.<Integer>parseSingleName(attr.getValue(), valueMap);

        int layerType = (Integer)convertedValue;
        view.setLayerType(layerType,null);
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        setToRemoveAttribute(view, "none", xmlInflaterLayout, ctx);
    }


}
