package org.itsnat.droid.impl.xmlinflated.values;

/**
 * Created by jmarranz on 27/11/14.
 */
public abstract class ElementValuesChildNoChildElem extends ElementValuesChild
{
    protected String type;
    protected String name;
    protected String value;

    public ElementValuesChildNoChildElem(ElementValues parentElement, String type, String name, String value)
    {
        super(parentElement);

        this.type = type;
        this.name = name;
        this.value = value;
    }


    public String getType()
    {
        return type;
    }

    public String getName()
    {
        return name;
    }

    public String getValue()
    {
        return value;
    }
}
