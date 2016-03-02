package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.graphics.drawable.Drawable;

import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_scrollbarThumbVertical extends AttrDescView_view_View_scrollbar_Base
{
    public AttrDescView_view_View_scrollbarThumbVertical(ClassDescViewBased parent)
    {
        super(parent,"scrollbarThumbVertical","mScrollCache","scrollBar","setVerticalThumbDrawable",
                MiscUtil.resolveClass("android.view.View$ScrollabilityCache"), MiscUtil.resolveClass("android.widget.ScrollBarDrawable"),
                Drawable.class);
    }
}
