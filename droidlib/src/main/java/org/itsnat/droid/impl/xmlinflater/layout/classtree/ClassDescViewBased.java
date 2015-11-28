package org.itsnat.droid.impl.xmlinflater.layout.classtree;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import org.itsnat.droid.AttrLayoutInflaterListener;
import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.browser.PageImpl;
import org.itsnat.droid.impl.browser.serveritsnat.ItsNatDocImpl;
import org.itsnat.droid.impl.browser.serveritsnat.NodeToInsertImpl;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.layout.DOMView;
import org.itsnat.droid.impl.util.IOUtil;
import org.itsnat.droid.impl.util.MiscUtil;
import org.itsnat.droid.impl.xmlinflated.InflatedXML;
import org.itsnat.droid.impl.xmlinflated.layout.InflatedLayoutImpl;
import org.itsnat.droid.impl.xmlinflater.MethodContainer;
import org.itsnat.droid.impl.xmlinflater.layout.AttrLayoutContext;
import org.itsnat.droid.impl.xmlinflater.layout.ClassDescViewMgr;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcess;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcessChildGridLayout;
import org.itsnat.droid.impl.xmlinflater.layout.OneTimeAttrProcessDefault;
import org.itsnat.droid.impl.xmlinflater.layout.PendingPostInsertChildrenTasks;
import org.itsnat.droid.impl.xmlinflater.layout.XMLInflaterLayout;
import org.itsnat.droid.impl.xmlinflater.shared.attr.AttrDesc;
import org.itsnat.droid.impl.xmlinflater.shared.classtree.ClassDesc;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by jmarranz on 30/04/14.
 */
public class ClassDescViewBased extends ClassDesc<View>
{
    protected static MethodContainer<ViewGroup.LayoutParams> methodGenerateLP =
                        new MethodContainer<ViewGroup.LayoutParams>(ViewGroup.class,"generateDefaultLayoutParams");

    protected Class<View> clasz;
    protected Constructor<? extends View> constructor1P;
    protected Constructor<? extends View> constructor3P;

    public ClassDescViewBased(ClassDescViewMgr classMgr, String className,ClassDescViewBased parentClass)
    {
        super(classMgr, className, parentClass);
    }

    @Override
    protected void init()
    {
        initClass();

        super.init();
    }

    public Class<View> getDeclaredClass()
    {
        return clasz;
    }

    @SuppressWarnings("unchecked")
    protected Class<? extends View> initClass()
    {
        // El motivo de ésto es evitar usar el .class lo que obliga a cargar la clase aunque no se use, así la clase nativa se carga cuando se necesita por primera vez
        if (clasz == null)
        {
            String className = getClassOrDOMElemName();
            this.clasz = (Class<View>) MiscUtil.resolveClass(className);
        }
        return clasz;
    }

    public ClassDescViewMgr getClassDescViewMgr()
    {
        return (ClassDescViewMgr)classMgr;
    }

    public ClassDescViewBased getParentClassDescViewBased()
    {
        return (ClassDescViewBased)getParentClassDesc();
    }

    protected static boolean isStyleAttribute(String namespaceURI,String name)
    {
        return MiscUtil.isEmpty(namespaceURI) && name.equals("style");
    }

    protected boolean isAttributeIgnored(String namespaceURI,String name)
    {
        return isStyleAttribute(namespaceURI,name); // Se trata de forma especial en otro lugar
    }

    //@SuppressWarnings("unchecked")
    public boolean setAttribute(View view, DOMAttr attr, AttrLayoutContext attrCtx)
    {
        if (!isInit()) init();

        String namespaceURI = attr.getNamespaceURI();
        String name = attr.getName(); // El nombre devuelto no contiene el namespace
        String value = attr.getValue();

        XMLInflaterLayout xmlInflaterLayout = attrCtx.getXMLInflaterLayout();

        try
        {
            if (isAttributeIgnored(namespaceURI, name)) return false; // Se trata de forma especial en otro lugar

            if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
            {
                AttrDesc<ClassDescViewBased,View,AttrLayoutContext> attrDesc = this.<ClassDescViewBased,View,AttrLayoutContext>getAttrDesc(name);
                if (attrDesc != null)
                {
                    attrDesc.setAttribute(view, attr,attrCtx);
                }
                else
                {
                    // Es importante recorrer las clases de abajo a arriba pues algún atributo se repite en varios niveles tal y como minHeight y minWidth
                    // y tiene prioridad la clase más derivada
                    ClassDescViewBased parentClass = getParentClassDescViewBased();
                    if (parentClass != null)
                    {
                        parentClass.setAttribute(view, attr,attrCtx);
                    }
                    else
                    {
                        // No se encuentra opción de proceso custom
                        AttrLayoutInflaterListener listener = xmlInflaterLayout.getAttrLayoutInflaterListener();
                        if (listener != null)
                        {
                            PageImpl page = getPageImpl(xmlInflaterLayout); // Puede ser null
                            listener.setAttribute(page, view, namespaceURI, name, value);
                        }
                    }
                }
            }
            else if (isXMLIdAttrAsDOM(namespaceURI, name))
            {
                InflatedLayoutImpl inflated = xmlInflaterLayout.getInflatedLayoutImpl();
                inflated.setXMLId(value, view);
            }
            else
            {
                // No se encuentra opción de proceso custom
                AttrLayoutInflaterListener listener = xmlInflaterLayout.getAttrLayoutInflaterListener();
                if (listener != null)
                {
                    PageImpl page = getPageImpl(xmlInflaterLayout); // Puede ser null
                    listener.setAttribute(page, view, namespaceURI, name, value);
                }
            }

            return true;
        }
        catch(Exception ex)
        {
            throw new ItsNatDroidException("Error setting attribute: " + namespaceURI + " " + name + " " + value + " in object " + view, ex);
        }
    }

    //@SuppressWarnings("unchecked")
    public boolean removeAttribute(View view, String namespaceURI, String name, AttrLayoutContext attrCtx)
    {
        if (!isInit()) init();

        try
        {
            if (isAttributeIgnored(namespaceURI,name)) return false; // Se trata de forma especial en otro lugar

            XMLInflaterLayout xmlInflaterLayout = attrCtx.getXMLInflaterLayout();
            if (InflatedXML.XMLNS_ANDROID.equals(namespaceURI))
            {
                AttrDesc<ClassDescViewBased,View,AttrLayoutContext> attrDesc = this.<ClassDescViewBased,View,AttrLayoutContext>getAttrDesc(name);
                if (attrDesc != null)
                {
                    attrDesc.removeAttribute(view,attrCtx);
                }
                else
                {
                    ClassDescViewBased parentClass = getParentClassDescViewBased();
                    if (parentClass != null)
                    {
                        parentClass.removeAttribute(view, namespaceURI, name,attrCtx);
                    }
                    else
                    {
                        // No se encuentra opción de proceso custom
                        AttrLayoutInflaterListener listener = xmlInflaterLayout.getAttrLayoutInflaterListener();
                        if (listener != null)
                        {
                            PageImpl page = getPageImpl(xmlInflaterLayout); // Puede ser null
                            listener.removeAttribute(page, view, namespaceURI, name);
                        }
                    }
                }
            }
            else if (isXMLIdAttrAsDOM(namespaceURI, name))
            {
                InflatedLayoutImpl inflated = xmlInflaterLayout.getInflatedLayoutImpl();
                inflated.unsetXMLId(view);
            }
            else
            {
                // No se encuentra opción de proceso custom
                AttrLayoutInflaterListener listener = xmlInflaterLayout.getAttrLayoutInflaterListener();
                if (listener != null)
                {
                    PageImpl page = getPageImpl(xmlInflaterLayout); // Puede ser null
                    listener.removeAttribute(page, view, namespaceURI, name);
                }
            }
        }
        catch(Exception ex)
        {
            throw new ItsNatDroidException("Error removing attribute: " + namespaceURI + " " + name + " in object " + view, ex);
        }

        return true;
    }

    public static boolean isXMLIdAttrAsDOM(String namespaceURI, String name)
    {
        return (namespaceURI == null || "".equals(namespaceURI)) && "id".equals(name);
    }

    public OneTimeAttrProcess createOneTimeAttrProcess(View view,ViewGroup viewParent)
    {
        // Se redefine en un caso
        return (viewParent instanceof GridLayout)
                     ? new OneTimeAttrProcessChildGridLayout(view)
                     : new OneTimeAttrProcessDefault(view);
    }

    public void addViewObject(ViewGroup viewParent,View view,int index,OneTimeAttrProcess oneTimeAttrProcess, Context ctx)
    {
        if (view.getLayoutParams() != null) throw new ItsNatDroidException("Unexpected");

        if (viewParent != null)
        {
 //           AttributeSet layoutAttrDefault = readAttributeSetLayout(ctx, R.layout.layout_params); // No se puede cachear el AttributeSet, ya lo he intentado
//            ViewGroup.LayoutParams params = viewParent.generateLayoutParams(layoutAttrDefault);

            ViewGroup.LayoutParams params = methodGenerateLP.invoke(viewParent);
            view.setLayoutParams(params);

            oneTimeAttrProcess.executeLayoutParamsTasks(); // Así ya definimos los LayoutParams inmediatamente antes de añadir al padre que es más o menos lo que se hace en addView

            if (index < 0) viewParent.addView(view);
            else viewParent.addView(view, index);
        }
        else // view es el ROOT
        {
            // Esto ocurre con el View root del layout porque hasta el final no podemos insertarlo en el ViewGroup contenedor que nos ofrece Android por ej en la Actividad, no creo que sea necesario algo diferente a un ViewGroup.LayoutParams
            // aunque creo que no funciona el poner valores concretos salvo el match_parent que afortunadamente es el único que interesa para
            // un View root que se inserta.
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);

            oneTimeAttrProcess.executeLayoutParamsTasks();
        }
    }

    protected static String findAttributeFromRemote(String namespaceURI, String attrName, NodeToInsertImpl newChildToIn)
    {
        DOMAttr attr = newChildToIn.getAttribute(namespaceURI,attrName);
        if (attr == null) return null;
        return attr.getValue();
    }

    private int findStyleAttributeFromRemote(ItsNatDocImpl itsNatDoc, NodeToInsertImpl newChildToIn)
    {
        String value = findAttributeFromRemote(null, "style", newChildToIn);
        if (value == null) return 0;
        Context ctx = itsNatDoc.getPageImpl().getContext();
        return getXMLInflateRegistry().getIdentifier(value, ctx);
    }

    public View createViewObjectFromRemote(ItsNatDocImpl itsNatDoc,NodeToInsertImpl newChildToIn,PendingPostInsertChildrenTasks pending)
    {
        int idStyle = findStyleAttributeFromRemote(itsNatDoc, newChildToIn);
        return createViewObjectFromRemote(itsNatDoc,newChildToIn,idStyle,pending);
    }

    protected View createViewObjectFromRemote(ItsNatDocImpl itsNatDoc,NodeToInsertImpl newChildToIn,int idStyle,PendingPostInsertChildrenTasks pending)
    {
        // Se redefine completamente en el caso de Spinner
        Context ctx = itsNatDoc.getPageImpl().getContext();
        return createViewObject(ctx,idStyle,pending);
    }

    private int findStyleAttributeFromParser(InflatedLayoutImpl inflated,DOMView domView)
    {
        String value = domView.getStyleAttr();
        if (value == null) return 0;
        Context ctx = inflated.getContext();
        return getXMLInflateRegistry().getIdentifier(value, ctx);
    }

    public View createViewObjectFromParser(InflatedLayoutImpl inflated,DOMView domView,PendingPostInsertChildrenTasks pending)
    {
        int idStyle = findStyleAttributeFromParser(inflated, domView);
        return createViewObjectFromParser(inflated, domView,idStyle,pending);
    }

    protected View createViewObjectFromParser(InflatedLayoutImpl inflated,DOMView domView,int idStyle,PendingPostInsertChildrenTasks pending)
    {
        // Se redefine completamente en el caso de Spinner
        Context ctx = inflated.getContext();
        return createViewObject(ctx, idStyle,pending);
    }

    protected View createViewObject(Context ctx,int idStyle,PendingPostInsertChildrenTasks pending)
    {
        View view;

        Class<? extends View> clasz = initClass();

        try
        {
            if (idStyle != 0)
            {
                // http://stackoverflow.com/questions/3142067/android-set-style-in-code
                // En teoría un parámetro es suficiente (con ContextThemeWrapper) pero curiosamente por ej en ProgressBar son necesarios los tres parámetros
                // de otra manera el idStyle es ignorado, por tanto aunque parece redundate el paso del idStyle, ambos params son necesarios en algún caso
                if (constructor3P == null) constructor3P = clasz.getConstructor(Context.class, AttributeSet.class, int.class);
                view = constructor3P.newInstance(new ContextThemeWrapper(ctx,idStyle),(AttributeSet)null,idStyle);

                // ALTERNATIVA QUE NO FUNCIONA (idStyle es ignorado):
                //if (constructor3P == null) constructor3P = clasz.getConstructor(Context.class, AttributeSet.class, int.class);
                //view = constructor3P.newInstance(ctx, null, idStyle);
            }
            else
            {
                // Notas: Android suele llamar al constructor de dos params (Context,AttributeSet) supongo al menos que cuando
                // no hay atributo style.
                // En teoría da igual pues el constructor de 1 param (Context) llama al de dos con null, sin embargo
                // nos encontramos por ej con TabHost en donde no es así y el constructor de 1 param inicializa mal el componente.
                if (constructor1P == null) constructor1P = clasz.getConstructor(Context.class,AttributeSet.class);
                view = constructor1P.newInstance(ctx,(AttributeSet)null);
            }
        }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InstantiationException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }

        return view;
    }


    protected static AttributeSet readAttributeSetLayout(Context ctx,int layoutId)
    {
        // Método para crear un AttributeSet del elemento root a partir de un XML compilado

        XmlResourceParser parser = ctx.getResources().getLayout(layoutId);

        try
        {
            while (parser.next() != XmlPullParser.START_TAG) {}
        }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }

        AttributeSet attributes = Xml.asAttributeSet(parser); // En XML compilados es un simple cast
        return attributes;
    }

    protected static AttributeSet readAttributeSet_ANTIGUO(Context ctx)
    {
        // NO SE USA, el método Resources.getLayout() ya devuelve un XmlResourceParser compilado
        // Y antes funcionaba pero ya NO (Android 4.4).


        // Este método experimental es para create un AttributeSet a partir de un XML compilado, se trataria
        // de crear un archivo XML tal y como "<tag />" ir al apk generado y copiar el archivo compilado, abrirlo
        // y copiar el contenido compilado y guardarlo finalmente como un byte[] constante
        // El problema es que no he conseguido usar AttributeSet vacío para lo que lo quería.
        // El método lo dejo inutilizado por si en el futuro se necesita un AttributeSet

        // http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/4.4.2_r1/android/content/res/XmlBlock.java?av=f

        Resources res = ctx.getResources();
        InputStream input = null; // res.openRawResource(R.raw.prueba_compilado_raw);
        byte[] content = IOUtil.read(input);

        try
        {
            Class<?> xmlBlockClass = Class.forName("android.content.res.XmlBlock");

            Constructor xmlBlockClassConstr = xmlBlockClass.getDeclaredConstructor(byte[].class);
            xmlBlockClassConstr.setAccessible(true);
            Object xmlBlock = xmlBlockClassConstr.newInstance(content);

            Method xmlBlockNewParserMethod = xmlBlockClass.getDeclaredMethod("newParser");
            xmlBlockNewParserMethod.setAccessible(true);
            XmlResourceParser parser = (XmlResourceParser)xmlBlockNewParserMethod.invoke(xmlBlock);

            while (parser.next() != XmlPullParser.START_TAG) {}

            AttributeSet attributes = Xml.asAttributeSet(parser);
            return attributes;
        }
        catch (ClassNotFoundException ex) { throw new ItsNatDroidException(ex); }
        catch (NoSuchMethodException ex) { throw new ItsNatDroidException(ex); }
        catch (InstantiationException ex) { throw new ItsNatDroidException(ex); }
        catch (IllegalAccessException ex) { throw new ItsNatDroidException(ex); }
        catch (InvocationTargetException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
    }


}
