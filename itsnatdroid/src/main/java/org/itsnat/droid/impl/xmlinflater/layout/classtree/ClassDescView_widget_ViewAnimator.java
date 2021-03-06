package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.attr.widget.AttrDescView_widget_ViewAnimator_inoutAnimation;
import org.itsnat.droid.impl.xmlinflater.shared.attr.AttrDescReflecMethodBoolean;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_ViewAnimator extends ClassDescViewBased
{
    public ClassDescView_widget_ViewAnimator(ClassDescViewMgr classMgr,ClassDescView_widget_FrameLayout parentClass)
    {
        super(classMgr,"android.widget.ViewAnimator",parentClass);
    }

    @SuppressWarnings("unchecked")
    protected void init()
    {
        super.init();

        addAttrDescAN(new AttrDescReflecMethodBoolean(this, "animateFirstView", true));
        addAttrDescAN(new AttrDescView_widget_ViewAnimator_inoutAnimation(this, "inAnimation"));
        addAttrDescAN(new AttrDescView_widget_ViewAnimator_inoutAnimation(this, "outAnimation"));

    }
}

