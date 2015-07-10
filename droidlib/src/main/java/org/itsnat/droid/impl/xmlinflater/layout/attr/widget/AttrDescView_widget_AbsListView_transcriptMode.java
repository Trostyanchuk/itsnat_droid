package org.itsnat.droid.impl.xmlinflater.layout.attr.widget;

import android.widget.AbsListView;

import org.itsnat.droid.impl.xmlinflater.layout.attr.AttrDescViewReflecMethodSingleName;
import org.itsnat.droid.impl.xmlinflater.layout.classtree.ClassDescViewBased;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmarranz on 30/04/14.
 */
public class AttrDescView_widget_AbsListView_transcriptMode extends AttrDescViewReflecMethodSingleName
{
    static Map<String, Integer> valueMap = new HashMap<String, Integer>( 3 );
    static
    {
        valueMap.put("disabled", AbsListView.TRANSCRIPT_MODE_DISABLED);
        valueMap.put("normal",AbsListView.TRANSCRIPT_MODE_NORMAL);
        valueMap.put("alwaysScroll",AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
    }

    public AttrDescView_widget_AbsListView_transcriptMode(ClassDescViewBased parent)
    {
        super(parent,"transcriptMode",int.class,valueMap,"disabled");
    }


}
