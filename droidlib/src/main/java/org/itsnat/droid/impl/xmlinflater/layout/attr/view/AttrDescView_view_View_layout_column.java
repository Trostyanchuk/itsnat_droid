package org.itsnat.droid.impl.xmlinflater.layout.attr.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TableRow;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescView;
import org.itsnat.droid.impl.xmlinflater.layout.attr.GridLayout_columnSpec;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_view_View_layout_column extends AttrDescView
{
    public AttrDescView_view_View_layout_column(ClassDescViewBased parent)
    {
        super(parent,"layout_column");
    }

    public void setAttribute(final View view, DOMAttr attr, XMLInflaterLayout xmlInflaterLayout, Context ctx, final OneTimeAttrProcess oneTimeAttrProcess, PendingPostInsertChildrenTasks pending)
    {
        final int column = getInteger(attr.getValue(),ctx);

        Runnable task = new Runnable(){
            @Override
            public void run()
            {
                ViewGroup.LayoutParams params = view.getLayoutParams();

                if (params instanceof GridLayout.LayoutParams)
                {
                    OneTimeAttrProcessChildGridLayout oneTimeAttrProcessGrid = (OneTimeAttrProcessChildGridLayout) oneTimeAttrProcess;
                    if (oneTimeAttrProcessGrid.gridLayout_columnSpec == null) oneTimeAttrProcessGrid.gridLayout_columnSpec = new GridLayout_columnSpec();

                    oneTimeAttrProcessGrid.gridLayout_columnSpec.layout_column = column;
                }
                else if (params instanceof TableRow.LayoutParams)
                {
                    ((TableRow.LayoutParams) params).column = column;
                }
            }};

        if (oneTimeAttrProcess != null)
            oneTimeAttrProcess.addLayoutParamsTask(task);
        else
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params instanceof GridLayout.LayoutParams)
            {
                throw new ItsNatDroidException("Attribute " + getName() + " cannot be changed post creation");
            }
            else if (params instanceof TableRow.LayoutParams)
            {
                task.run();
                view.setLayoutParams(view.getLayoutParams());
            }
        }
    }

    public void removeAttribute(View view, XMLInflaterLayout xmlInflaterLayout, Context ctx)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params instanceof GridLayout.LayoutParams)
        {
            // No hacemos nada, no puede ser cambiado "post-creación"
        }
        else if (params instanceof TableRow.LayoutParams)
        {
            ((TableRow.LayoutParams)params).column = -1;

            view.setLayoutParams(view.getLayoutParams());
        }

    }
}
