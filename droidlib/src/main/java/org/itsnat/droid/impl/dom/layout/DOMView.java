package org.itsnat.droid.impl.dom.layout;

import org.itsnat.droid.impl.dom.DOMElement;

/**
 * Created by jmarranz on 27/10/14.
 */
public class DOMView extends DOMElement
{
    protected String styleAttr;

    public DOMView(String name,DOMElement parentElement)
    {
        super(name,parentElement);
    }

    public DOMView(DOMElement toCopy)
    {
        super(toCopy);
        if (toCopy instanceof DOMView)
            this.styleAttr = ((DOMView)toCopy).styleAttr;
    }

    public DOMView getParentDOMView()
    {
        return (DOMView) getParentDOMElement();
    }

    public String getStyleAttr()
    {
        return styleAttr;
    }

    public void setStyleAttr(String styleAttr)
    {
        this.styleAttr = styleAttr;
    }

    public void addChildView(DOMView domView)
    {
        super.addChildDOMElement(domView);
    }


}
