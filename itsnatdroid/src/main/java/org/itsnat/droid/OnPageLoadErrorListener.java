package org.itsnat.droid;

/**
 * Created by jmarranz on 4/06/14.
 */
public interface OnPageLoadErrorListener
{
    public void onError(PageRequest pageRequest, Exception ex, HttpRequestResult response);
}
