package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.widget.ListPopupWindow;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecFieldMethodDrawable;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;


/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_AutoCompleteTextView_dropDownSelector extends AttrDescViewReflecFieldMethodDrawable
{
    public AttrDescView_widget_AutoCompleteTextView_dropDownSelector(ClassDescViewBased parent)
    {
        super(parent,"dropDownSelector","mPopup","setListSelector",ListPopupWindow.class,null); // Hay un background por defecto de Android en ListPopupWindow aunque parece que por defecto se pone un null si no hay atributo
    }

}
