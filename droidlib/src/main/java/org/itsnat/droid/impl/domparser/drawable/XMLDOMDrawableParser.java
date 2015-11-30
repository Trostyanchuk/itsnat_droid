package org.itsnat.droid.impl.domparser.drawable;

import android.content.res.AssetManager;

import org.itsnat.droid.ItsNatDroidException;
import org.itsnat.droid.impl.dom.DOMElement;
import org.itsnat.droid.impl.dom.DOMElementDefault;
import org.itsnat.droid.impl.dom.drawable.XMLDOMDrawable;
import org.itsnat.droid.impl.domparser.XMLDOMParser;
import org.itsnat.droid.impl.xmlinflater.XMLInflateRegistry;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Created by jmarranz on 31/10/14.
 */
public class XMLDOMDrawableParser extends XMLDOMParser
{
    public XMLDOMDrawableParser(XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        super(xmlInflateRegistry,assetManager);
    }

    public static XMLDOMDrawableParser createXMLDOMDrawableParser(XMLInflateRegistry xmlInflateRegistry,AssetManager assetManager)
    {
        return new XMLDOMDrawableParser(xmlInflateRegistry,assetManager);
    }

    public XMLDOMDrawable parse(String markup)
    {
        StringReader input = new StringReader(markup);
        return parse(input);
    }

    private XMLDOMDrawable parse(Reader input)
    {
        try
        {
            XmlPullParser parser = newPullParser(input);
            return parse(parser);
        }
        catch (IOException ex) { throw new ItsNatDroidException(ex); }
        catch (XmlPullParserException ex) { throw new ItsNatDroidException(ex); }
        finally
        {
            try { input.close(); }
            catch (IOException ex) { throw new ItsNatDroidException(ex); }
        }
    }

    private XMLDOMDrawable parse(XmlPullParser parser) throws IOException, XmlPullParserException
    {
        XMLDOMDrawable xmlDOMDrawable = new XMLDOMDrawable();
        String rootElemName = getRootElementName(parser);
        parseRootElement(rootElemName, parser, xmlDOMDrawable);
        return xmlDOMDrawable;
    }

    @Override
    protected DOMElement createElement(String name,DOMElement parent)
    {
        return new DOMElementDefault(name,parent);
    }
}
