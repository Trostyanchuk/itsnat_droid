package org.itsnat.droid.impl.domparser.values;

import android.content.res.AssetManager;
import android.text.Html;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMAttr;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.XMLDOM;
import org.itsnat.droid.impl.dom.values.DOMElemValuesItemNormal;
import org.itsnat.droid.impl.dom.values.DOMElemValuesItemStyle;
import org.itsnat.droid.impl.dom.values.DOMElemValuesNoChildElem;
import org.itsnat.droid.impl.dom.values.DOMElemValuesResources;
import org.itsnat.droid.impl.dom.values.DOMElemValuesStyle;
import org.itsnat.droid.impl.dom.values.XMLDOMValues;
import org.itsnat.droid.impl.dommini.DMNode;
import org.itsnat.droid.impl.dommini.DOMMiniParser;
import org.itsnat.droid.impl.dommini.DOMMiniRender;
import org.itsnat.droid.impl.domparser.XMLDOMParser;
import org.itsnat.droid.impl.domparser.XMLDOMRegistry;
import org.itsnat.droid.impl.util.MiscUtil;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import static org.itsnat.droid.impl.dom.values.XMLDOMValues.TYPE_COLOR;
import static org.itsnat.droid.impl.dom.values.XMLDOMValues.TYPE_BOOL;
import static org.itsnat.droid.impl.dom.values.XMLDOMValues.TYPE_DIMEN;
import static org.itsnat.droid.impl.dom.values.XMLDOMValues.TYPE_STRING;

/**
 * Created by jmarranz on 31/10/14.
 */
public class XMLDOMValuesParser extends XMLDOMParser
{
    public XMLDOMValuesParser(XMLDOMRegistry xmlDOMRegistry, AssetManager assetManager)
    {
        super(xmlDOMRegistry,assetManager);
    }

    public static XMLDOMValuesParser createXMLDOMValuesParser(XMLDOMRegistry xmlDOMRegistry,AssetManager assetManager)
    {
        return new XMLDOMValuesParser(xmlDOMRegistry,assetManager);
    }

    public static boolean isResourceTypeValues(String resourceType)
    {
        return "style".equals(resourceType) ||
                TYPE_BOOL.equals(resourceType) || TYPE_COLOR.equals(resourceType) || TYPE_DIMEN.equals(resourceType)|| TYPE_STRING.equals(resourceType);
    }

    @Override
    protected boolean isAndroidNSPrefixNeeded()
    {
        return false;
    }

    public void parse(String markup,XMLDOMValues xmlDOMValues)
    {
        StringReader input = new StringReader(markup);
        parse(input,xmlDOMValues);
    }

    private void parse(Reader input,XMLDOMValues xmlDOMValues)
    {
        try
        {
            XmlPullParser parser = newPullParser(input);
            parse(parser,xmlDOMValues);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private void parse(XmlPullParser parser,XMLDOMValues xmlDOMValues) throws IOException, XmlPullParserException
    {
        String rootElemName = getRootElementName(parser);
        parseRootElement(rootElemName,parser, xmlDOMValues);
    }

    @Override
    protected DOMElement createElement(String name,DOMElement parent)
    {
        if (hasChildElements(name))
        {
            if ("resources".equals(name))
            {
                if (parent != null) throw new ItsNatDroidException("<resources> element must be root");
                return new DOMElemValuesResources();
            }
            else if ("style".equals(name))
                return new DOMElemValuesStyle((DOMElemValuesResources) parent);
            else if ("declare-styleable".equals(name) || "string-array".equals(name) || "integer-array".equals(name))
                throw new ItsNatDroidException("Not supported yet:" + name);

            throw new ItsNatDroidException("Unrecognized element name:" + name);
        }
        else
        {
            if (parent instanceof DOMElemValuesStyle)
                return new DOMElemValuesItemStyle((DOMElemValuesStyle)parent);
            else
                return new DOMElemValuesItemNormal(name, (DOMElemValuesResources) parent);
        }
    }

    @Override
    protected void processChildElements(DOMElement parentElement,XmlPullParser parser,XMLDOM xmlDOM) throws IOException, XmlPullParserException
    {
        if (parentElement instanceof DOMElemValuesNoChildElem)
        {
            DOMElemValuesNoChildElem parentElementNoChildElem = (DOMElemValuesNoChildElem)parentElement;

            // Lo normal es que esperemos un único nodo de texto hijo, pero también podemos esperar un texto con HTML, que será tolerado y procesado por Resources.getText()

            DMNode[] nodeList = DOMMiniParser.parse(parser);
            String text = DOMMiniRender.toString(nodeList);

            /*
            while (parser.next() != XmlPullParser.TEXT) ;

            String text = parser.getText();

            while (parser.next() != XmlPullParser.END_TAG) ;
            */

            DOMAttr valueAsDOMAttr = parentElementNoChildElem.setTextNode(text); // El nodo de texto lo tratamos de forma especial como un atributo para resolver si es asset o remote y así cargarlo
            addDOMAttr(parentElementNoChildElem,valueAsDOMAttr, xmlDOM);

        }
        else
        {
            super.processChildElements(parentElement, parser, xmlDOM);
        }
    }


    public static boolean hasChildElements(String elemName)
    {
        // http://developer.android.com/guide/topics/resources/available-resources.html
        return "resources".equals(elemName) || "style".equals(elemName) || "declare-styleable".equals(elemName) || "string-array".equals(elemName) || "integer-array".equals(elemName);
    }
}
