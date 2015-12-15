package org.itsnat.droid.impl.browser.serveritsnat.event;

import android.view.View;

import org.itsnat.droid.event.DroidEvent;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;
import org.itsnat.droid.impl.util.NameValue;

import java.util.List;

/**
 * Created by jmarranz on 31/07/14.
 */
public abstract class DroidEventImpl extends NormalEventImpl implements DroidEvent
{
    /**
     * The current event phase is the capturing phase.
     */
    public static final short CAPTURING_PHASE           = 1;
    /**
     * The event is currently being evaluated at the target
     * <code>EventTarget</code>.
     */
    public static final short AT_TARGET                 = 2;
    /**
     * The current event phase is the bubbling phase.
     */
    public static final short BUBBLING_PHASE            = 3;

    protected int eventPhase;
    protected View viewTarget;

    public DroidEventImpl(DroidEventListener listener)
    {
        super(listener);

        this.eventPhase = AT_TARGET;
    }

    public DroidEventListener getDroidEventListener()
    {
        return (DroidEventListener)listener;
    }

    @Override
    public String getType()
    {
        return getDroidEventListener().getType();
    }

    public int getEventPhase()
    {
        return eventPhase;
    }
    public void setEventPhase(int eventPhase)
    {
        this.eventPhase = eventPhase;
    }

    public View getTarget()
    {
        return viewTarget;
    }
    public void setTarget(View view)
    {
        this.viewTarget = view;
    }

    @Override
    public List<NameValue> genParamURL()
    {
        List<NameValue> params = super.genParamURL();
        params.add(new NameValue("itsnat_evt_eventPhase", "" + eventPhase));

        View view = getDroidEventListener().getCurrentTarget();
        String viewTargetStr = viewTarget != null && viewTarget != view? listener.getItsNatDocImpl().getStringPathFromView(viewTarget) : "null";
        // Si viewTarget == currentTarget enviamos null para evitar tráfico, ya sabemos que es el currentTarget
        params.add(new NameValue("itsnat_evt_target",viewTargetStr));

        return params;
    }
}
