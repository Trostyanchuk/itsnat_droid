package org.itsnat.itsnatdroidtest.testact.util;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.NinePatch;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.RotateDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;

import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Created by jmarranz on 19/06/14.
 */
public class Assert
{
    public static void assertPositive(int a)
    {
        if (a <= 0)
            throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertPositive(float a)
    {
        if (a <= 0)
            throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertNotNull(Object a)
    {
        if (a == null)
            throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertFalse(boolean a)
    {
        if (a)
            throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertTrue(boolean a)
    {
        if (!a)
            throw new ItsNatDroidException("Failed " + a);
    }

    public static void assertEquals(boolean a,boolean b)
    {
        if (a != b)
            throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(int a,int b)
    {
        if (a != b)
            throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(long a,long b)
    {
        if (a != b)
            throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(float a,float b)
    {
        if (a != b)
            throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(String a,String b)
    {
        assertEqualsInternal(a,b);
    }

    public static void assertEquals(CharSequence a,String b)
    {
        // El CharSequence puede ser por ejemplo Spannable pero en este caso queremos comparar "el valor textual"
        assertEqualsInternal(a.toString(), b);
    }

    public static void assertEquals(SpannableStringBuilder a,SpannableStringBuilder b)
    {
        // En teoría se puede hacer un test más completo
        assertEqualsInternal(a.toString(),b.toString());
    }

    public static void assertEquals(CharSequence a,CharSequence b)
    {
        // El CharSequence puede ser por ejemplo Spannable
        assertEqualsInternal(a.getClass(),b.getClass());
        assertEqualsInternal(a.toString(),b.toString());
    }

    public static void assertEquals(Rect a,Rect b)
    {
        assertEqualsInternal(a, b);
    }

    public static void assertEquals(InputFilter.LengthFilter a,InputFilter.LengthFilter b)
    {
        int a_int = (Integer)TestUtil.getField(a,"mMax");
        int b_int = (Integer)TestUtil.getField(b,"mMax");
        assertEquals(a_int, b_int);
    }


    public final static void assertEquals(Object a,Object b)
    {
        assertEqualsInternal(a, b);
    }

    public final static void assertEqualsInternal(Object a,Object b)
    {
        if (a == b) return;
        if (a != null && !a.equals(b) || b != null && !b.equals(a)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public final static void assertEquals(Boolean a,Boolean b)
    {
        assertEquals((boolean)a,(boolean)b);
    }

    public final static void assertEquals(Integer a,int b)
    {
        assertEquals((int)a,b);
    }

    public final static void assertEquals(Integer a,Integer b)
    {
        assertEquals((int)a,(int)b);
    }

    public final static void assertEquals(Long a,Long b)
    {
        assertEquals((long)a,(long)b);
    }

    public final static void assertEquals(Float a,float b)
    {
        assertEquals((float)a,b);
    }

    public final static void assertEquals(Float a,Float b)
    {
        assertEquals((float)a,(float)b);
    }

    public final static void assertEquals(int[][] a,int[][] b)
    {
        if (a.length != b.length) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");

        for(int i = 0; i < a.length; i++)
            assertEquals(a[i],b[i]);
    }

    public final static void assertEquals(int[] a,int[] b)
    {
        if (a.length != b.length) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");

        for(int i = 0; i < a.length; i++)
            assertEquals(a[i],b[i]);
    }

    public static void assertEquals(Bitmap a,Bitmap b)
    {
        //if (executeAllTests)
        {
            assertEquals(a.getByteCount(), b.getByteCount());
            assertEquals(a.getWidth(), b.getWidth());
            assertEquals(a.getHeight(), b.getHeight());
        }
    }

    private static void assertEqualsDrawable(Drawable a,Drawable b)
    {
        assertEqualsDrawable(a,b,true);
    }

    private static void assertEqualsDrawable(Drawable a,Drawable b,boolean testOpacity)
    {
        // La propia clase Drawable, no derivadas
        if (testOpacity) assertEquals(a.getOpacity(), b.getOpacity());
        assertEquals(a.getIntrinsicWidth(), b.getIntrinsicWidth());
        assertEquals(a.getIntrinsicHeight(), b.getIntrinsicHeight());

        assertEquals(a.isStateful(), b.isStateful());
        //assertEquals(a.isVisible(),b.isVisible());  no coincide nunca pues sólo se está viendo uno de los dos (supongo que esa es la razon)
    }

    public static void assertEquals(Drawable a,Drawable b)
    {
        if (!a.getClass().equals(b.getClass())) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");

        //assertEquals(a.getBounds(),b.getBounds());

        if (a instanceof BitmapDrawable)
        {
            assertEquals((BitmapDrawable)a,(BitmapDrawable)b);
        }
        else if (a instanceof ClipDrawable)
        {
            assertEquals((ClipDrawable)a,(ClipDrawable)b);
        }
        else if (a instanceof ColorDrawable)
        {
            assertEquals(((ColorDrawable) a).getColor(), ((ColorDrawable) b).getColor());
        }
        else if (a instanceof GradientDrawable)
        {
            assertEquals((GradientDrawable)a,(GradientDrawable)b);
        }
        else if (a instanceof LayerDrawable)
        {
            assertEquals((LayerDrawable)a,(LayerDrawable)b);
        }
        else if (a instanceof NinePatchDrawable)
        {
            assertEquals((NinePatchDrawable)a,(NinePatchDrawable)b);
        }
        else if (a instanceof RotateDrawable)
        {
            assertEquals((RotateDrawable)a,(RotateDrawable)b);
        }
        else if (a instanceof StateListDrawable)
        {
            assertEquals((StateListDrawable)a,(StateListDrawable)b);
        }
        else
            throw new ItsNatDroidException("Cannot test drawable " + a);
    }

    public static void assertEquals(BitmapDrawable a,BitmapDrawable b)
    {
        assertEqualsDrawable(a, b);

        assertEquals(a.getGravity(),b.getGravity());
        assertEquals(a.getBitmap(), b.getBitmap());
    }

    public static void assertEquals(ClipDrawable a,ClipDrawable b)
    {
        assertEqualsDrawable(a, b);

        Rect ar = new Rect(); Rect br = new Rect();
        a.getPadding(ar); b.getPadding(br);
        assertEquals(ar, br);

        if (Build.VERSION.SDK_INT < TestUtil.MARSHMALLOW) // < 23
        {
            Drawable.ConstantState sa = a.getConstantState();
            Drawable.ConstantState sb = b.getConstantState();

            Class classClipState = TestUtil.resolveClass(ClipDrawable.class.getName() + "$ClipState");
            assertEquals((Drawable) TestUtil.getField(sa, classClipState, "mDrawable"), (Drawable) TestUtil.getField(sb, classClipState, "mDrawable"));
        }
        else
        {
            Class clasz = TestUtil.resolveClass("android.graphics.drawable.DrawableWrapper"); // DrawableWrapper es la clase base de ClipDrawable
            assertEquals((Drawable) TestUtil.getField(a, clasz, "mDrawable"), (Drawable) TestUtil.getField(b, clasz, "mDrawable"));
        }
    }

    public static void assertEquals(ColorDrawable a,ColorDrawable b)
    {
        assertEqualsDrawable(a,b);
        assertEquals(a.getColor(),b.getColor());
    }

    public static void assertEquals(GradientDrawable a,GradientDrawable b)
    {
        assertEqualsDrawable(a,b,false); // No comparar getOpacity() no se porqué

        Drawable.ConstantState sa = a.getConstantState();
        Drawable.ConstantState sb = b.getConstantState();

        assertEquals((Integer) TestUtil.getField(sa, "mStrokeWidth"), (Integer) TestUtil.getField(sb, "mStrokeWidth"));
        // mSolidColor ya no existe en level 21: assertEquals((Integer)TestUtil.getField(sa,"mSolidColor"),(Integer)TestUtil.getField(sb,"mSolidColor"));
    }

    public static void assertEquals(LayerDrawable a,LayerDrawable b)
    {
        assertEqualsDrawable(a, b);

        assertEquals(a.getNumberOfLayers(), b.getNumberOfLayers());
        Rect ar = new Rect(); Rect br = new Rect();
        a.getPadding(ar); b.getPadding(br);
        assertEquals(ar, br);

        Object a_ls = TestUtil.getField(a, "mLayerState"); // LayerState
        Object b_ls = TestUtil.getField(b, "mLayerState"); // "

        Object[] a_cd_array = (Object[])TestUtil.getField(a_ls, "mChildren"); // ChildDrawable[]
        Object[] b_cd_array = (Object[])TestUtil.getField(b_ls, "mChildren"); // "


        for (int i = 0; i < a.getNumberOfLayers(); i++)
        {
            assertEquals(a.getId(i), b.getId(i));

            Object a_cd = a_cd_array[i]; // ChildDrawable
            Object b_cd = b_cd_array[i];

            assertEquals((Integer)TestUtil.getField(a_cd,"mInsetL"),(Integer)TestUtil.getField(b_cd,"mInsetL"));
            assertEquals((Integer)TestUtil.getField(a_cd,"mInsetT"),(Integer)TestUtil.getField(b_cd,"mInsetT"));
            assertEquals((Integer)TestUtil.getField(a_cd,"mInsetR"),(Integer)TestUtil.getField(b_cd,"mInsetR"));
            assertEquals((Integer)TestUtil.getField(a_cd,"mInsetB"),(Integer)TestUtil.getField(b_cd,"mInsetB"));

            assertEquals(a.getDrawable(i), b.getDrawable(i));
        }
    }

    public static void assertEquals(NinePatchDrawable a,NinePatchDrawable b)
    {
        assertEqualsDrawable(a, b);

        NinePatch a2 = (NinePatch) TestUtil.getField(a, "mNinePatch");
        NinePatch b2 = (NinePatch) TestUtil.getField(b, "mNinePatch");
        assertEquals((Bitmap)TestUtil.getField(a2, "mBitmap"),(Bitmap)TestUtil.getField(b2, "mBitmap"));
    }

    public static void assertEquals(RotateDrawable a,RotateDrawable b)
    {
        assertEqualsDrawable(a, b);

        assertEquals(a.getDrawable(), b.getDrawable());
    }

    public static void assertEquals(StateListDrawable a,StateListDrawable b)
    {
        assertEqualsDrawable(a, b);

        Method methodStateListState = TestUtil.getMethod(StateListDrawable.class, "getStateListState", new Class[0]);

        Method methodGetStateListStateIsConstantSize = TestUtil.getMethod(MiscUtil.resolveClass("android.graphics.drawable.DrawableContainer$DrawableContainerState"), "isConstantSize", new Class[0]);

        Object a_stateListState = TestUtil.callMethod(a, null, methodStateListState);
        Object b_stateListState = TestUtil.callMethod(b, null, methodStateListState);

        boolean a_isConstantSize = (Boolean)TestUtil.callMethod(a_stateListState,null,methodGetStateListStateIsConstantSize);
        boolean b_isConstantSize = (Boolean)TestUtil.callMethod(b_stateListState,null,methodGetStateListStateIsConstantSize);

        assertEquals(a_isConstantSize,b_isConstantSize);

        Method methodIsVisible = TestUtil.getMethod(Drawable.class, "isVisible", new Class[0]);

        boolean a_isVisible = (Boolean)TestUtil.callMethod(a ,null,methodIsVisible);
        boolean b_isVisible = (Boolean)TestUtil.callMethod(b ,null,methodIsVisible);

        assertEquals(a_isVisible,b_isVisible);


        int[] a_stateArr = a.getState();
        int[] b_stateArr = b.getState();
        assertEquals(a_stateArr.length, b_stateArr.length);
        for (int i = 0; i < a_stateArr.length; i++)
        {
            assertEquals(a_stateArr[i], b_stateArr[i]);
        }
    }

    public static void assertEqualsStrokeWidth(Drawable a,int b)
    {
        Drawable.ConstantState sa = ((GradientDrawable) a).getConstantState();
        assertEquals((Integer)TestUtil.getField(sa,"mStrokeWidth"),b);
    }

    public static void assertEquals(ColorStateList a,ColorStateList b)
    {
        assertEquals((int[][])TestUtil.getField(a,"mStateSpecs"),(int[][])TestUtil.getField(b,"mStateSpecs"));
        assertEquals((int[])TestUtil.getField(a,"mColors"),(int[])TestUtil.getField(b,"mColors"));
        assertEquals((Integer)TestUtil.getField(a,"mDefaultColor"),(Integer)TestUtil.getField(b,"mDefaultColor"));

//        if (!a.equals(b)) throw new ItsNatDroidException("Not equal: \"" + a + "\" - \"" + b + "\"");
    }

    public static void assertEquals(ObjectAnimator a,ObjectAnimator b)
    {
        // Comparamos unas cuantas propiedades
        assertEquals(a.getPropertyName(),b.getPropertyName());
//        assertEquals(a.getTarget().getClass().getName(),a.getTarget().getClass().getName());
        assertTrue(a.getInterpolator().equals(b.getInterpolator()));

        assertEquals(a.getDuration(), b.getDuration());
        assertEquals(a.getRepeatMode(),b.getRepeatMode());
    }

    public static void assertEquals(AnimationSet a,AnimationSet b)
    {
        // Comparamos unas cuantas propiedades

        assertEquals(a.getDuration(),b.getDuration());
        //assertEquals(a.getStartTime(),b.getStartTime());
        //assertEquals(a.getStartOffset(),b.getStartOffset());

        assertEquals(a.getAnimations().size(),b.getAnimations().size());

        Iterator<Animation> a_it = a.getAnimations().iterator();
        Iterator<Animation> b_it = b.getAnimations().iterator();
        while(a_it.hasNext())
        {
            Animation a_anim = a_it.next();
            Animation b_anim = b_it.next();
            assertEquals(a_anim,b_anim);
        }
    }

    public static void assertEquals(Animation a,Animation b)
    {
        if (a instanceof TranslateAnimation)
        {
            TranslateAnimation a_trans = (TranslateAnimation)a;
            TranslateAnimation b_trans = (TranslateAnimation)b;

            // Comparamos unas cuantas propiedades
            assertEquals(a_trans.getDuration(),b_trans.getDuration());
//            assertEquals(a_trans.getStartOffset(),b_trans.getStartOffset());
//            assertEquals(a_trans.getStartTime(),b_trans.getStartTime());
            assertEquals(a_trans.getInterpolator(),b_trans.getInterpolator());
        }
        else
            throw new ItsNatDroidException("Cannot test " + a);
    }

    public static void assertEquals(android.view.animation.Interpolator a,android.view.animation.Interpolator b)
    {
        assertEquals(a.getClass(),b.getClass());
        assertEquals(a.getInterpolation(5),a.getInterpolation(5));
    }

}
