package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.animation.LayoutTransition;
import android.view.View;
import android.view.ViewGroup;

import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;
import org.itsnat.droid.impl.xmlinflater.shared.attr.AttrDesc;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_ViewGroup_animateLayoutChanges extends AttrDesc<ClassDescViewBased,View,AttrLayoutContext>
{
    public AttrDescView_view_ViewGroup_animateLayoutChanges(ClassDescViewBased parent)
    {
        super(parent,"animateLayoutChanges");
    }

    @Override
    public void setAttribute(View view, DOMAttr attr, AttrLayoutContext attrCtx)
    {
        // Tomado del código fuente de ViewGroup
        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.0.3_r1/android/view/ViewGroup.java?av=f
        LayoutTransition trn = "true".equals(attr.getValue()) ? new LayoutTransition() : null; // null es el valor por defecto

        ((ViewGroup)view).setLayoutTransition(trn);
    }

    @Override
    public void removeAttribute(View view, AttrLayoutContext attrCtx)
    {
        setAttributeToRemove(view, "false",attrCtx);
    }
}
