package org.itsnat.droid.impl.browser.serveritsnat.event;

import android.os.Parcel;
import android.view.InputEvent;

import org.itsnat.droid.event.DroidInputEvent;
import org.itsnat.droid.impl.browser.serveritsnat.evtlistener.DroidEventListener;

/**
 * Created by jmarranz on 7/07/14.
 */
public abstract class DroidInputEventImpl extends DroidEventImpl implements DroidInputEvent
{
    protected InputEvent evtNative;

    public DroidInputEventImpl(DroidEventListener listener, InputEvent evtNative)
    {
        super(listener);
        this.evtNative = evtNative;
    }

    public InputEvent getInputEvent()
    {
        return evtNative;
    }



    @Override
    public void saveEvent()
    {
        // Para evitar el problema de acceder en modo ASYNC_HOLD al evento original tras haberse encolado y terminado el proceso del evento por el navegador
        // En Android lo normal es reutilizar el objeto evento para siguientes eventos por ello tenemos que hacer una copia
        if (evtNative == null) return;

        // http://stackoverflow.com/questions/1626667/how-to-use-parcel-in-android

        Parcel parcelOut = Parcel.obtain();
        parcelOut.writeValue(evtNative);
        byte[] data = parcelOut.marshall();
        parcelOut.recycle();

        Parcel parcelIn = Parcel.obtain();
        parcelIn.unmarshall(data, 0, data.length);
        parcelIn.setDataPosition(0);
        this.evtNative = (InputEvent) parcelIn.readValue(evtNative.getClass().getClassLoader());
        parcelIn.recycle();
    }


}
