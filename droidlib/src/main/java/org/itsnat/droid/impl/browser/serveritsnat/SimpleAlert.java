package org.itsnat.droid.impl.browser.serveritsnat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by jmarranz on 30/06/14.
 */
public class SimpleAlert
{
    public static void show(String title,String text,Context ctx)
    {
        new AlertDialog.Builder(ctx).setTitle(title).setMessage(text)
        .setCancelable(false)
        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
            }
        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}

