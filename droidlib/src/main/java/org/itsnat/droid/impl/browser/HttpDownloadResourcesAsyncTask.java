package org.itsnat.droid.impl.browser;

import android.content.res.AssetManager;

import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnHttpRequestListener;
import org.itsnat.droid.impl.dom.DOMAttrRemote;
import org.itsnat.droid.impl.domparser.XMLDOMRegistry;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jmarranz on 4/06/14.
 */
public class HttpDownloadResourcesAsyncTask extends ProcessingAsyncTask<List<HttpRequestResultOKImpl>>
{
    protected List<DOMAttrRemote> attrRemoteList;
    protected DownloadResourcesHttpClient parent;
    protected String method;
    protected String pageURLBase;
    protected HttpRequestData httpRequestData;
    protected OnHttpRequestListener listener;
    protected OnHttpRequestErrorListener errorListener;
    protected int errorMode;
    protected XMLDOMRegistry xmlDOMRegistry;
    protected AssetManager assetManager;

    public HttpDownloadResourcesAsyncTask(List<DOMAttrRemote> attrRemoteList,DownloadResourcesHttpClient parent, String method, String pageURLBase, OnHttpRequestListener listener, OnHttpRequestErrorListener errorListener, int errorMode,AssetManager assetManager)
    {
        PageImpl page = parent.getPageImpl();

        this.attrRemoteList = attrRemoteList;
        this.parent = parent;
        this.method = method;
        this.pageURLBase = pageURLBase;
        this.httpRequestData = new HttpRequestData(page.getPageRequestClonedImpl());
        this.listener = listener;
        this.errorListener = errorListener;
        this.errorMode = errorMode;
        this.xmlDOMRegistry = page.getItsNatDroidBrowserImpl().getItsNatDroidImpl().getXMLDOMRegistry();
        this.assetManager = assetManager;
    }

    protected List<HttpRequestResultOKImpl> executeInBackground() throws Exception
    {
        HttpResourceDownloader resDownloader =
                new HttpResourceDownloader(pageURLBase,httpRequestData,xmlDOMRegistry,assetManager);
        List<HttpRequestResultOKImpl> resultList = new LinkedList<HttpRequestResultOKImpl>();
        resDownloader.downloadResources(attrRemoteList,resultList);
        return resultList;
    }

    @Override
    protected void onFinishOk(List<HttpRequestResultOKImpl> resultList)
    {
        for (HttpRequestResultOKImpl result : resultList)
        {
            try
            {
                parent.processResult(result, listener, errorMode);
            }
            catch (Exception ex)
            {
                if (errorListener != null)
                {
                    errorListener.onError(parent.getPageImpl(), ex, result);
                    return;
                }
                else
                {
                    if (ex instanceof ItsNatDroidException) throw (ItsNatDroidException) ex;
                    else throw new ItsNatDroidException(ex);
                }
            }
        }
    }

    @Override
    protected void onFinishError(Exception ex)
    {
        ItsNatDroidException exFinal = parent.convertException(ex);

        if (errorListener != null)
        {
            HttpRequestResult result = (exFinal instanceof ItsNatDroidServerResponseException) ?
                    ((ItsNatDroidServerResponseException)exFinal).getHttpRequestResult() : null;
            errorListener.onError(parent.getPageImpl(),exFinal, result);
            return;
        }
        else
        {
            if (exFinal instanceof ItsNatDroidException) throw (ItsNatDroidException)exFinal;
            else throw new ItsNatDroidException(exFinal);
        }
    }
}

