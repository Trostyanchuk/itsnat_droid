package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.view.View;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_padding extends AttrDescView
{
    public AttrDescView_view_View_padding(ClassDescViewBased parent, String name)
    {
        super(parent,name);
    }

    public void setAttribute(View view, DOMAttr attr, AttrLayoutContext attrCtx)
    {
        int convValue = getDimensionIntRound(attr.getValue(), attrCtx.getContext());

        String name = getName();
        if ("padding".equals(name))
            view.setPadding(convValue,convValue,convValue,convValue);
        else if ("paddingLeft".equals(name))
            view.setPadding(convValue,view.getPaddingTop(),view.getPaddingRight(),view.getPaddingBottom());
        else if ("paddingTop".equals(name))
            view.setPadding(view.getPaddingLeft(),convValue,view.getPaddingRight(),view.getPaddingBottom());
        else if ("paddingRight".equals(name))
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),convValue,view.getPaddingBottom());
        else if ("paddingBottom".equals(name))
            view.setPadding(view.getPaddingLeft(),view.getPaddingTop(),view.getPaddingRight(),convValue);
    }

    public void removeAttribute(View view, AttrLayoutContext attrCtx)
    {
        setToRemoveAttribute(view, "0dp",attrCtx);
    }
}
