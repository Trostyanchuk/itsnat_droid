package org.itsnat.droid;

import android.content.Context;

/**
 * Created by jmarranz on 5/06/14.
 */
public interface PageRequest
{
    public PageRequest setContext(Context ctx);
    public PageRequest setBitmapDensityReference(int density);
    public PageRequest setOnPageLoadListener(OnPageLoadListener listener);
    public PageRequest setOnPageLoadErrorListener(OnPageLoadErrorListener listener);
    public PageRequest setAttrLayoutInflaterListener(AttrLayoutInflaterListener listener);
    public PageRequest setAttrDrawableInflaterListener(AttrDrawableInflaterListener listener);
    public PageRequest setHttpParamMap(HttpParamMap httpParamMap);
    public PageRequest setSynchronous(boolean sync);
    public PageRequest setURL(String url);
    public void execute();
}
