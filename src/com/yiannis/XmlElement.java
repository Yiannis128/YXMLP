package com.yiannis;

import java.util.ArrayList;

/*
 * @author Yiannis Charalambous
 * This is a class that represents an XmlElement.
 */
public final class XmlElement 
{
	private final String name;
	private final XmlElement parent;
	private final ArrayList<XmlElement> childElements = new ArrayList<XmlElement>();
	private final ArrayList<XmlAttribute> attributes = new ArrayList<XmlAttribute>();
	
	public XmlElement(String name, XmlElement parent)
	{
		this.name = name;
		this.parent = parent;
	}
	
	public void addChild(XmlElement child) throws Exception
	{
		if(child == this)
		{
			throw new Exception("Cannot have child node as self.");
		}
		else if(childElements.contains(child))
		{
			throw new Exception("Xml Element is already added.");
		}
		else
		{
			childElements.add(child);
		}
	}
	
	public int getChildCount()
	{
		return childElements.size();
	}
	
	public XmlElement getChild(int index)
	{
		return childElements.get(index);
	}
	
	public void addAttribute(XmlAttribute attribute) throws Exception
	{
		if(attributes.contains(attribute))
		{
			throw new Exception("Element already contains attribute.");
		}
		
		attributes.add(attribute);
	}
	
	public XmlAttribute getAttribute(String attributeName) throws Exception
	{
		for(XmlAttribute attribute : attributes) 
		{
			if(attributeName.equals(attribute.getName()))
			{
				return attribute;
			}
		}

		throw new Exception("Could not find attribute with name: " + attributeName);
	}
	
	public XmlAttribute getAttribute(int index) throws IndexOutOfBoundsException
	{
		return attributes.get(index);
	}
	
	public int getAttributeCount()
	{
		return attributes.size();
	}
	
	public String getName()
	{
		return name;
	}
	
	public XmlElement getParent()
	{
		return parent;
	}
}
