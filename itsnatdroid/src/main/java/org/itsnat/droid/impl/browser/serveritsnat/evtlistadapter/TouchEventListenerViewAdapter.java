package org.itsnat.droid.impl.browser.serveritsnat.evtlistadapter;

import android.view.MotionEvent;
import android.view.View;

import org.itsnat.droid.impl.browser.ItsNatViewImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidMotionEventImpl;

/**
 * Created by jmarranz on 24/07/14.
 */
public class TouchEventListenerViewAdapter extends DroidEventListenerViewAdapter implements View.OnTouchListener
{
    protected View.OnTouchListener touchListener;

    public TouchEventListenerViewAdapter(ItsNatViewImpl viewData)
    {
        super(viewData);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        String type = DroidMotionEventImpl.getTouchTypeFromAction(motionEvent);

        dispatch(type,motionEvent);

        boolean res = false; // Conviene que sea false porque de otra podemos bloquear un scrollview padre. Sabemos que si es false y hay un View contenedor no se procesan los eventos touchmove etc, si es false apenas el down. El listener del usuario puede forzar un true si quiere
        if (touchListener != null) res = touchListener.onTouch(viewData.getView(), motionEvent);

        return res;
    }

    public void setOnTouchListener(View.OnTouchListener touchListener)
    {
        this.touchListener = touchListener;
    }
}
