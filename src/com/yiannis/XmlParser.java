package com.yiannis;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*
 * @author Yiannis Charalambous
 * This is a class that easily parses XmlFiles.
 */
public class XmlParser 
{
	private XmlParser() {}
	
	/*
	 * Opens the xml file, parses it and creates a tree of the XmlElements and attributes and returns the root node of the file.
	 * @param filePath The path to the file that will be parsed.
	 */
	public static XmlElement openFile(String filePath) throws ParserConfigurationException, SAXException, IOException, Exception
	{
		File file = new File(filePath);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);
		document.getDocumentElement().normalize();
		
		Node rootNode = document.getDocumentElement();
		XmlElement root = new XmlElement(rootNode.getNodeName(), null);
		
		exploreTree(root, rootNode);

		return root;
	}
	
	public static void saveFile(String filePath, XmlElement root) throws ParserConfigurationException, TransformerException, SAXException, IOException
	{
		File file = new File(filePath);
		
		if(file.exists())
		{
			int result = JOptionPane.showConfirmDialog(null, "File already exists. Overwrite file?", "Warning",
					                                   JOptionPane.WARNING_MESSAGE);

			if(result == JOptionPane.OK_OPTION)
			{
				file.delete();
			}
			else
			{
				return;
			}
		}

		file.createNewFile();
		
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		
		//Create the document root.
		Element rootNode = document.createElement(root.getName());
		document.appendChild(rootNode);
		
		//Add the categories.
		for(int x = 0; x < root.getChildCount(); x++)
		{
			XmlElement categoryElement = root.getChild(x);
			
			Element categoryNode = document.createElement(categoryElement.getName());
			rootNode.appendChild(categoryNode);
			
			//Add the attributes for each category.
			for(int y = 0; y < categoryElement.getAttributeCount(); y++)
			{
				XmlAttribute attribute = categoryElement.getAttribute(y);
				
				categoryNode.setAttribute(attribute.getName(), attribute.getValue());
			}
			
			//Add the child items in the category.
			for(int y = 0; y < categoryElement.getChildCount(); y++)
			{
				XmlElement itemElement = categoryElement.getChild(y);
				
				Element itemNode = document.createElement(itemElement.getName());
				categoryNode.appendChild(itemNode);
				
				//Add the start item attributes.
				for(int z = 0; z < itemElement.getAttributeCount(); z++)
				{
					XmlAttribute itemAttribute = itemElement.getAttribute(z);
					
					itemNode.setAttribute(itemAttribute.getName(), itemAttribute.getValue());
				}
			}
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource domSource = new DOMSource(document);
		StreamResult streamResult = new StreamResult(file);
		
		transformer.transform(domSource, streamResult);
	}
	
	//This method explores the tree recursively.
	private static void exploreTree(XmlElement base, Node baseNode) throws Exception
	{
		baseNode.normalize();
		
		//Get the children of the tree.
		if(baseNode.hasChildNodes())
		{
			NodeList childNodes = baseNode.getChildNodes();
			
			for(int x = 0; x < childNodes.getLength(); x++)
			{
				if(isValid(childNodes.item(x)))
				{
					XmlElement child = new XmlElement(childNodes.item(x).getNodeName(), base);
					
					try
					{
						NamedNodeMap nodeAttributes = childNodes.item(x).getAttributes();
						
						//Get the attributes of the object.
						for(int y = 0; y < nodeAttributes.getLength(); y++)
						{
							Node attribute = nodeAttributes.item(y);
							
							child.addAttribute(new XmlAttribute(attribute.getNodeName(), attribute.getNodeValue(), child));
						}
					}
					catch(NullPointerException e)
					{
						//Ignore if there are no attributes.
					}
					
					base.addChild(child);

					exploreTree(child, childNodes.item(x));
				}
			}
		}
		else
		{
			return;
		}
	}
	
	private static boolean isValid(Node node)
	{
		return node.getNodeType() == Node.ELEMENT_NODE;
	}
}
