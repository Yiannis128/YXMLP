package com.yiannis;

/*
 * This class encapsulates an XML elements attributes.
 * @author Yiannis Charalambous
 */
public class XmlAttribute 
{
	private final XmlElement owner;
	private final String name;
	private final String value;
	
	public XmlAttribute(String name, String value, XmlElement owner)
	{
		this.owner = owner;
		this.name = name;
		this.value = value;
	}
	
	public XmlElement getOwner()
	{
		return owner;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public boolean parseValue() throws Exception
	{
		switch(value)
		{
		case "True":
			return true;
		case "False":
			return false;
		default:
			throw new Exception("Boolean parsing failed. " + owner.getName() + "(" + name + "->" + value + "). Ignoring element.");
		}
	}
}
