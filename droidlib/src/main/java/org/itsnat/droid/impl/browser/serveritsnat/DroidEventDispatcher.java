package org.itsnat.droid.impl.browser.serveritsnat;

import android.view.View;
import android.view.ViewParent;

import org.itsnat.droid.ClientErrorMode;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.event.DroidInputEventImpl;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;

import java.util.LinkedList;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * Created by jmarranz on 22/08/14.
 */
public class DroidEventDispatcher
{
    protected ItsNatDocImpl itsNatDoc;

    public DroidEventDispatcher(ItsNatDocImpl itsNatDoc)
    {
        this.itsNatDoc = itsNatDoc;
    }

    public void dispatch(ItsNatViewImpl viewDataCurrentTarget,String type,Object nativeEvt)
    {
        String inlineCode = viewDataCurrentTarget.getOnTypeInlineCode("on" + type);
        if (inlineCode != null)
        {
            executeInlineEventHandler(viewDataCurrentTarget, inlineCode, type, nativeEvt);
        }

        View viewTarget = viewDataCurrentTarget.getView(); // En "unload" y "load" viewDataCurrentTarget es ItsNatViewNullImpl por lo que getView() es nulo
        dispatch(viewDataCurrentTarget,type,nativeEvt,true, DroidEventImpl.AT_TARGET,viewTarget);
    }

    private void dispatch(ItsNatViewImpl viewDataCurrentTarget,String type,Object nativeEvt,boolean checkUseCapture,int eventPhase,View viewTarget)
    {
        List<DroidEventListener> list = viewDataCurrentTarget.getEventListeners(type);
        if (list == null) return;

        View viewCurrentTarget = viewDataCurrentTarget.getView();
        for (DroidEventListener listener : list)
        {
            if (checkUseCapture && listener.isUseCapture())
            {
                dispatchCapture(viewCurrentTarget,type,nativeEvt,viewTarget);
            }

            DroidEventImpl evtWrapper = (DroidEventImpl)listener.createNormalEvent(nativeEvt);
            try
            {
                evtWrapper.setEventPhase(eventPhase);
                evtWrapper.setTarget(viewTarget);
                listener.dispatchEvent(evtWrapper);
            }
            catch(Exception ex)
            {
                // Desde aquí capturamos todos los fallos del proceso de eventos, el código anterior a dispatchEvent(String,InputEvent) nunca debería
                // fallar, o bien porque es muy simple o porque hay llamadas al código del usuario que él mismo puede controlar sus fallos

                OnEventErrorListener errorListener = itsNatDoc.getPageImpl().getOnEventErrorListener();
                if (errorListener != null)
                {
                    HttpRequestResult result = (ex instanceof ItsNatDroidServerResponseException) ? ((ItsNatDroidServerResponseException)ex).getHttpRequestResult() : null;
                    errorListener.onError(ex, evtWrapper,result);
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
        }
    }

    private void dispatchCapture(View view,String type,Object nativeEvt,View viewTarget)
    {
        List<ViewParent> tree = getViewTreeParent(view);
        for (ViewParent viewParent : tree)
        {
            ItsNatViewImpl viewParentData = itsNatDoc.getItsNatViewImpl((View) viewParent);
            dispatch(viewParentData, type, nativeEvt, false, DroidInputEventImpl.CAPTURING_PHASE, viewTarget);
        }
    }

    private static List<ViewParent> getViewTreeParent(View view)
    {
        List<ViewParent> tree = new LinkedList<ViewParent>();
        ViewParent parent = view.getParent(); // Asegura que en la lista no está el View inicial
        getViewTree(parent,tree);
        return tree;
    }

    private static void getViewTree(ViewParent view,List<ViewParent> tree)
    {
        if (view == null || !(view instanceof View)) return;
        tree.add(0, view);
        getViewTree(view.getParent(),tree);
    }

    private void executeInlineEventHandler(ItsNatViewImpl viewData,String inlineCode, String type, Object nativeEvt)
    {
        View view = viewData.getView();
        int eventGroupCode = DroidEventGroupInfo.getEventGroupCode(type);
        DroidEventListener listenerFake = new DroidEventListener(itsNatDoc, view, type, null, null, false, -1, -1, eventGroupCode);
        DroidEventImpl event = (DroidEventImpl)listenerFake.createNormalEvent(nativeEvt);
        event.setEventPhase(DroidEventImpl.AT_TARGET);
        event.setTarget(event.getCurrentTarget()); // El inline handler no participa en capture en web


        Interpreter interp = itsNatDoc.getPageImpl().getInterpreter();
        try
        {
            interp.set("event", event);
            try
            {
                interp.eval(inlineCode);
            }
            finally
            {
                interp.set("event", null); // Para evitar un memory leak
            }
        }
        catch (EvalError ex)
        {
            int errorMode = itsNatDoc.getErrorMode();
            if (errorMode != ClientErrorMode.NOT_CATCH_ERRORS)
            {
                itsNatDoc.showErrorMessage(false, ex.getMessage());
            }
            else throw new ItsNatDroidScriptException(ex, inlineCode);
        }
        catch (Exception ex)
        {
            int errorMode = itsNatDoc.getErrorMode();
            if (errorMode != ClientErrorMode.NOT_CATCH_ERRORS)
            {
                itsNatDoc.showErrorMessage(false, ex.getMessage());
            }
            else throw new ItsNatDroidScriptException(ex, inlineCode);
        }
    }
}
