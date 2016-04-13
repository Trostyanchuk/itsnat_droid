package org.itsnat.itsnatdroidtest.testact.remote;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.itsnat.droid.AttrAnimatorInflaterListener;
import org.itsnat.droid.AttrDrawableInflaterListener;
import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.HttpRequestResult;
import org.itsnat.droid.ItsNatDroidBrowser;
import org.itsnat.droid.ItsNatDroidScriptException;
import org.itsnat.droid.ItsNatDroidServerResponseException;
import org.itsnat.droid.ItsNatSession;
import org.itsnat.droid.OnEventErrorListener;
import org.itsnat.droid.OnHttpRequestErrorListener;
import org.itsnat.droid.OnPageLoadErrorListener;
import org.itsnat.droid.OnPageLoadListener;
import org.itsnat.droid.OnScriptErrorListener;
import org.itsnat.droid.OnServerStateLostListener;
import org.itsnat.droid.Page;
import org.itsnat.droid.PageRequest;
import org.itsnat.droid.event.Event;
import org.itsnat.droid.event.NormalEvent;
import org.itsnat.itsnatdroidtest.R;
import org.itsnat.itsnatdroidtest.testact.TestActivity;
import org.itsnat.itsnatdroidtest.testact.TestActivityTabFragment;
import org.itsnat.itsnatdroidtest.testact.util.TestUtil;

import bsh.EvalError;

/**
 * Created by jmarranz on 13/08/14.
 */
public abstract class TestRemotePageBase implements OnPageLoadListener,OnPageLoadErrorListener,OnScriptErrorListener,OnEventErrorListener,
                            AttrLayoutInflaterListener,AttrDrawableInflaterListener,AttrAnimatorInflaterListener
{
    public static final boolean TEST_SYNC_REQUESTS = false;

    protected final TestActivityTabFragment fragment;
    protected final ItsNatDroidBrowser droidBrowser;
    protected boolean useItsNatServer;

    public TestRemotePageBase(TestActivityTabFragment fragment,ItsNatDroidBrowser droidBrowser)
    {
        this(fragment,droidBrowser,true);
    }

    public TestRemotePageBase(TestActivityTabFragment fragment,ItsNatDroidBrowser droidBrowser,boolean useItsNatServer)
    {
        this.fragment = fragment;
        this.droidBrowser = droidBrowser;
        this.useItsNatServer = useItsNatServer;

        if (TEST_SYNC_REQUESTS)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    protected TestActivity getTestActivity()
    {
        return fragment.getTestActivity();
    }

    public int getConnectionTimeout()
    {
        return 4000;
    }

    public int getReadTimeout()
    {
        return 10000;
    }

    protected void bindBackAndReloadButton(final Page page,View rootView)
    {
        View backButton = rootView.findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                page.dispose();
                fragment.gotoLayoutIndex();
            }
        });

        View buttonReload = rootView.findViewById(R.id.buttonReload);
        if (buttonReload == null) throw new RuntimeException("FAIL");

        buttonReload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                TestActivity act = getTestActivity();
                Toast.makeText(act, "DOWNLOADING AGAIN", Toast.LENGTH_SHORT).show();
                page.dispose();
                page.reusePageRequest().execute();
                //downloadLayoutRemote(act,droidBrowser);
            }
        });
    }

    public boolean isScriptingDisabled()
    {
        return false;
    }

    @Override
    public void onPageLoad(final Page page)
    {
        final TestActivity act = getTestActivity();

        if (!isScriptingDisabled() && useItsNatServer)
        {
            if (page.getId() == null)
            {
                TestUtil.alertDialog(act, "LAYOUT", "It seems page is not found or no ItsNat server used or scripting disabled");
                View rootView = page.getItsNatDoc().getRootView();
                changeLayout(rootView);
                return;
            }

            ItsNatSession session = page.getItsNatSession();
            if (session.getPageCount() > droidBrowser.getMaxPagesInSession()) throw new RuntimeException("FAIL");
        }


        String responseText = page.getHttpRequestResult().getResponseText();
        Log.v("TestActivity", "CONTENT:" + new String(responseText));

        boolean showContentInAlert = false;
        if (showContentInAlert)
        {
            TestUtil.alertDialog(act, "LAYOUT", new String(responseText));
        }

        View rootView = page.getItsNatDoc().getRootView();
        changeLayout(rootView);
        Toast.makeText(act, "OK LAYOUT REMOTE", Toast.LENGTH_SHORT).show();


        bindBackAndReloadButton(page, rootView);

        if (useItsNatServer)
        {
            page.setOnEventErrorListener(this); // Comentar para testear el sistema de errores built-in
        }


        page.setOnHttpRequestErrorListener(new OnHttpRequestErrorListener(){
            @Override
            public void onError(Page page, Exception ex, HttpRequestResult response)
            {
                ex.printStackTrace();

                String responseText = response != null ? response.getResponseText() : null;
                TestUtil.alertDialog(act, "User Msg: Failed HTTP request! \n" + responseText);
            }
        });

        page.setOnServerStateLostListener(new OnServerStateLostListener()
        {
            @Override
            public void onServerStateLost(Page page)
            {
                if (page.isDisposed())
                    TestUtil.alertDialog(act, "User Msg: SERVER STATE LOST!!");
            }
        });
    }

    @Override
    public void onError(PageRequest pageRequest, Exception ex, HttpRequestResult response)
    {
        ex.printStackTrace();

        TestActivity act = getTestActivity();
        Toast.makeText(act, "ERROR:" + ex.getMessage(), Toast.LENGTH_SHORT).show();
        //throw new RuntimeException(ex);

        if (ex instanceof ItsNatDroidScriptException)
        {
            ItsNatDroidScriptException exScr = (ItsNatDroidScriptException) ex;
            if (exScr.getCause() instanceof EvalError)
                ((EvalError) exScr.getCause()).printStackTrace();

            Log.v("TestActivity", "CODE:" + exScr.getScript());
        }
        else if (ex instanceof ItsNatDroidServerResponseException)
        {
            ItsNatDroidServerResponseException ex2 = (ItsNatDroidServerResponseException)ex;
            TestUtil.alertDialog(act, "User Msg: Server loaded content returned error: " + ex2.getMessage() + "\n" + ex2.getHttpRequestResult().getResponseText());
            Log.v("TestActivity", "RESPONSE:" + ex2.getHttpRequestResult().getResponseText());
        }
    }

    @Override
    public void onError(Page page,String code, Exception ex, Object context)
    {
        ex.printStackTrace();

        TestActivity act = getTestActivity();
        TestUtil.alertDialog(act, "Error in Code: \n" + code + "\nContext:" + context);
    }

    @Override
    public void onError(Page page,Event evt, Exception ex, HttpRequestResult response)
    {
        TestActivity act = getTestActivity();

        ex.printStackTrace();

        if (ex instanceof ItsNatDroidServerResponseException)
        {
            if (page.isDisposed())
                TestUtil.alertDialog(act, "User Msg: received event in page disposed (this error usually should be hidden for end users) " + ((ItsNatDroidServerResponseException) ex).getHttpRequestResult().getResponseText());
            else
                TestUtil.alertDialog(act, "User Msg: Server loaded content returned error: " + ((ItsNatDroidServerResponseException) ex).getHttpRequestResult().getResponseText());
        }
        else if (ex instanceof ItsNatDroidScriptException)
        {
            ItsNatDroidScriptException exScr = (ItsNatDroidScriptException) ex;
            StringBuilder msg = new StringBuilder();
            msg.append("User Msg: Event processing error (executing script code)");
            if (evt instanceof NormalEvent)
                msg.append("\nType: " + ((NormalEvent)evt).getType());
            msg.append("\nException Msg: " + exScr.getMessage());
            msg.append("\nCode: " + exScr.getScript());

            if (exScr.getCause() instanceof EvalError)
                ((EvalError) exScr.getCause()).printStackTrace();

            Log.v("TestActivity", "CODE:" + exScr.getScript());
        }
        else
        {
            StringBuilder msg = new StringBuilder();
            msg.append("User Msg: Event processing error");
            if (evt instanceof NormalEvent)
                msg.append("\nType: " + ((NormalEvent)evt).getType());
            msg.append("\nException Msg: " + ex.getMessage());

            TestUtil.alertDialog(act, msg.toString());
        }

    }

    @Override
    public boolean setAttribute(final Page page,View view, String namespace, String name, final String value)
    {
        System.out.println("NOT FOUND LAYOUT ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
        return true;
    }

    @Override
    public boolean removeAttribute(Page page,View view, String namespace, String name)
    {
        System.out.println("NOT FOUND LAYOUT ATTRIBUTE (removeAttribute): " + namespace + " " + name);
        return true;
    }

    @Override
    public boolean setAttribute(Page page, Drawable obj, String namespace, String name, String value)
    {
        System.out.println("NOT FOUND DRAWABLE ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
        return true;
    }

    @Override
    public boolean removeAttribute(Page page, Drawable obj, String namespace, String name)
    {
        System.out.println("NOT FOUND DRAWABLE ATTRIBUTE (removeAttribute): " + namespace + " " + name);
        return true;
    }

    @Override
    public boolean setAttribute(Page page, Animator obj, String namespace, String name, String value)
    {
        System.out.println("NOT FOUND ANIMATOR ATTRIBUTE (setAttribute): " + namespace + " " + name + " " + value);
        return true;
    }

    @Override
    public boolean removeAttribute(Page page, Animator obj, String namespace, String name)
    {
        System.out.println("NOT FOUND ANIMATOR ATTRIBUTE (removeAttribute): " + namespace + " " + name);
        return true;
    }

    protected void changeLayout(View rootView)
    {
        fragment.setRootView(rootView);
        fragment.updateFragmentLayout();
    }

    public void executePageRequest(String url)
    {
        TestActivity act = getTestActivity();

        PageRequest pageRequest = droidBrowser.createPageRequest();
        pageRequest.setContext(act)
        .setSynchronous(TEST_SYNC_REQUESTS)
        .setBitmapDensityReference(DisplayMetrics.DENSITY_XHIGH)
        .setOnPageLoadListener(this)
        .setOnPageLoadErrorListener(this)
        .setOnScriptErrorListener(this) // Comentar para ver el modo de error built-in
        .setAttrLayoutInflaterListener(this)
        .setAttrDrawableInflaterListener(this)
        .setAttrAnimatorInflaterListener(this)
        .setConnectTimeout(getConnectionTimeout())
        .setReadTimeout(getReadTimeout())
        .setURL(url)
        .execute();
    }
}
