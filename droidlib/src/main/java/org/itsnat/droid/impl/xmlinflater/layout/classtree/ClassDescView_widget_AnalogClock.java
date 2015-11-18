package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.shared.attr.AttrDescReflecFieldSetDrawable;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescView_widget_AnalogClock extends ClassDescViewBased
{
    public ClassDescView_widget_AnalogClock(ClassDescViewMgr classMgr,ClassDescView_view_View parentClass)
    {
        super(classMgr,"android.widget.AnalogClock",parentClass);
    }

    @SuppressWarnings("unchecked")
    protected void init()
    {
        super.init();

        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"dial","mDial",null));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"hand_hour","mHourHand",null));
        addAttrDesc(new AttrDescReflecFieldSetDrawable(this,"hand_minute","mMinuteHand",null));

    }
}

