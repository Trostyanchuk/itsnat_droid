package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.shared.attr.AttrDescReflecMethodBoolean;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ScrollView extends ClassDescViewBased
{
    public ClassDescView_widget_ScrollView(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.ScrollView",parentClass);
    }

    @SuppressWarnings("unchecked")
    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecMethodBoolean(this,"fillViewport",false));
    }
}

