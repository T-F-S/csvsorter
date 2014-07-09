package tfs.csvsorter;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



/**
 *  Configuration: Configuration settings
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      December 2008, June 2014
 */
public class Configuration
{
  
  public static final String LOG_DEFAULT="csvsorter.log";
   
  private File configFile  = null;
  private File inputFile  = null;
  private File outputFile = null;
  private File logFile = null;
  private String delimiter = ",";
  private String bracketLeft = "\"";
  private String bracketRight = "\"";  
  private boolean inputBrackets = true;
  private String outDelimiter = null;
  private String outBracketLeft = null;
  private String outBracketRight = null;
  private String charset = null;
  private String outCharset = null;
  private boolean transformLine = false;
  private boolean contentToLaTeX = false;    
  private boolean ignoreHeader = false;
  private boolean allowOverwrite = false;
  private NumberName[] outColumnArray = null;
  private ColumnComparator[] ccArray = new ColumnComparator[0];
  private final Locale locale;
  private final Collator collator;
  
  
  private enum SYMBOLS 
  {     
    braceleft("{"),
    braceright("}"),
    comma(","),
    doublequote("\""),
    pipe("|"),
    semicolon(";"),
    singlequote("'"),
    tab("\t")
    ;
    
    SYMBOLS(String s)
    {
      data = s;
    }
    
    public String getData()
    {
      return data;
    }
    
    private String data;
  };

  
  
  public Configuration(String configFileName) throws Exception
  {        
    configFile = new File(configFileName);
    DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
    dbfactory.setIgnoringElementContentWhitespace(true);
    DocumentBuilder builder = dbfactory.newDocumentBuilder();
    Document document = builder.parse(configFile);    
    
    Element elCSV = document.getDocumentElement();
    if (elCSV.getNodeName()!="csv")
    {
      throw new Exception("Document-Element 'csv' not found");
    }
    
    Element elInput = getFirstChildElementByName(elCSV, "input");
    if (elInput!=null)
    {
      String filename = elInput.getAttribute("file");
      if (!filename.equals(""))
      {     
        setInputFile(filename);
        String overwrite = elInput.getAttribute("overwrite");
        if (overwrite.toLowerCase().equals("true"))
        {
          allowOverwrite = true;
        }
      }
    }
    String filename = getAttributeValueForChild(elCSV, "output", "file");
    if (!filename.equals(""))
    {
      setOutputFile(filename);
    }    
    filename = getAttributeValueForChild(elCSV, "log", "file");
    if (!filename.equals(""))
    {
      setLogFile(filename);
    }
    else
    {
      setLogFile(LOG_DEFAULT);
    }
    String sign = getSymbolAttributeValueForChild(elCSV, "delimiter", "sign", "signsymbol");
    if (!sign.equals(""))
    {
      delimiter = sign;
    }
    sign = getSymbolAttributeValueForChild(elCSV, "outDelimiter", "sign", "signsymbol");
    if (!sign.equals(""))
    {
      outDelimiter = sign;
      transformLine = true;
    }
    else
    {
      outDelimiter = delimiter;
    }
    
    Element elBracket = getFirstChildElementByName(elCSV, "bracket");
    if (elBracket!=null)
    {
      String left = getSymbolAttributeValue(elBracket, "left", "leftsymbol");
      if (!left.equals(""))
      {
        bracketLeft = left;
      }
      String right = getSymbolAttributeValue(elBracket, "right", "rightsymbol");
      if (!right.equals(""))
      {
        bracketRight = right;
      }
      String empty = elBracket.getAttribute("empty");
      if (empty.toLowerCase().equals("true"))
      {
        inputBrackets = false;
        bracketLeft = "";
        bracketRight= "";        
      }      
    }
    
    elBracket = getFirstChildElementByName(elCSV, "outBracket");
    if (elBracket!=null)
    {
      transformLine = true;
      String left = getSymbolAttributeValue(elBracket, "left", "leftsymbol");
      if (!left.equals(""))
      {
        outBracketLeft = left;
      }
      else
      {
        outBracketLeft = bracketLeft;
      }
      String right = getSymbolAttributeValue(elBracket, "right", "rightsymbol");
      if (!right.equals(""))
      {
        outBracketRight = right;
      }
      else
      {
        outBracketRight = bracketRight;
      }
      String empty = elBracket.getAttribute("empty");
      if (empty.toLowerCase().equals("true"))
      {
        outBracketLeft = "";
        outBracketRight= "";        
      }      
    }
    else
    {
      outBracketLeft = bracketLeft;
      outBracketRight = bracketRight;
    }
    Element elContentToLaTeX = getFirstChildElementByName(elCSV, "contentToLaTeX");
    if (elContentToLaTeX!=null)
    {
      transformLine = true;
      contentToLaTeX = true;
    }
    Element elTransform = getFirstChildElementByName(elCSV, "transform");
    if (elTransform!=null)
    {
      transformLine = true;
    }
    Element elNoHeader = getFirstChildElementByName(elCSV, "noHeader");
    if (elNoHeader!=null)
    {
      ignoreHeader = true;
    }
    Element elCharset = getFirstChildElementByName(elCSV, "charset");
    if (elCharset!=null)
    {
      String incs = elCharset.getAttribute("in");
      if (!incs.equals(""))
      {
        charset = incs;
        outCharset = incs;
      }
      String outcs = elCharset.getAttribute("out");
      if (!outcs.equals(""))
      {
        outCharset = outcs;
      }
    }
    Locale protoLocale = Locale.getDefault();
    Element elLanguage = getFirstChildElementByName(elCSV, "language");
    if (elLanguage!=null)
    {
      String isocode = elLanguage.getAttribute("iso");
      if (!isocode.equals(""))
      {
        protoLocale = new Locale(isocode);
      }
    }
    locale = protoLocale;    
    collator = Collator.getInstance(locale);
    
    List<ColumnComparator> ccList = new ArrayList<ColumnComparator>();
    
    Element elSortlines = getFirstChildElementByName(elCSV, "sortlines");
    if (elSortlines!=null)
    {
      for (Element elColumn : getChildElements(elSortlines))
      {
        ColumnComparator cc = null;
        if (elColumn.getNodeName().equals("column"))
        {      
          cc = makeColumnComparator(elColumn);
        }
        else if (elColumn.getNodeName().equals("sum"))
        {
          cc = makeSumColumnComparator(elColumn);
        }      
        if (cc!=null)
        {
          ccList.add(cc);
        }
        else
        {
          System.out.println("Configuration error");
        }      
      }
    }
    
    Element elOutColumns = getFirstChildElementByName(elCSV, "outColumns");
    if (elOutColumns!=null)
    {
      List<NumberName> outputColumns = new ArrayList<NumberName>();
      for (Element elColumn : getChildElements(elOutColumns))
      {
        if (elColumn.getNodeName().equals("column"))
        {
          outputColumns.add(new NumberName(elColumn));
        }
      }
      if (outputColumns.size()>0)
      {
        transformLine = true;
        outColumnArray = new NumberName[outputColumns.size()];
        outputColumns.toArray(outColumnArray);
      }
    }    
    
    if (ccList.size()>0)
    {
      ccArray = new ColumnComparator[ccList.size()];
      ccList.toArray(ccArray);
    }               
  }
    
  
  private ColumnComparator makeColumnComparator(Element elColumn) throws Exception
  {
    NumberName numberName = new NumberName(elColumn);
    String type = elColumn.getAttribute("type");
    boolean ascending = !(elColumn.getAttribute("order").toLowerCase().equals("descending"));
    String defaultString = elColumn.getAttribute("default");
    
    if (type.equals("integer"))
    {
      return SingleColumnComparator.createIntegerComparator(numberName, ascending, defaultString);
    }
    else if (type.equals("long"))
    {
      return SingleColumnComparator.createLongComparator(numberName, ascending, defaultString);
    }
    else if (type.equals("double"))
    {
      return SingleColumnComparator.createDoubleComparator(numberName, ascending, defaultString);
    }
    else if (type.equals("date"))
    {
      return SingleColumnComparator.createDateComparator(numberName, locale, ascending, defaultString);
    }
    else if (type.equals("string") || type.equals(""))
    {
      return SingleColumnComparator.createStringComparator(numberName, collator, ascending);
    }    
    else
    {
      throw new Exception("Error in <sortlines>: Unknown column type '"+type+"'");
    }
  }
  
  
  
  private ColumnComparator makeSumColumnComparator(Element elSum) throws Exception
  {
    String type = elSum.getAttribute("type");
    boolean ascending = !(elSum.getAttribute("order").toLowerCase().equals("descending"));
    String defaultString = elSum.getAttribute("default");    

    ArrayList<NumberName> nnList = new ArrayList<NumberName>();
    for (Element elColumn : getChildElementsByName(elSum, "column"))
    {
      nnList.add(new NumberName(elColumn));
    }
    if (nnList.size()<1)
    {
      throw new Exception("Sum without summands");
    }
    NumberName[] numberNames = nnList.toArray(new NumberName[nnList.size()]);
    
    if (type.equals("integer"))
    {
      return MultiColumnComparator.createIntegerComparator(numberNames, ascending, defaultString);
    }
    else if (type.equals("long"))
    {
      return MultiColumnComparator.createLongComparator(numberNames, ascending, defaultString);
    }
    else if (type.equals("double"))
    {
      return MultiColumnComparator.createDoubleComparator(numberNames, ascending, defaultString);
    }
    else if (type.equals(""))
    {
      throw new Exception("Error in <sortlines>: Sum without type");
    }
    else
    {
      throw new Exception("Error in <sortlines>: Unknown sum type '"+type+"'");
    }
  }
  
  
  
  private List<Element> getChildElementsByName(Element parent, String name)
  {
    NodeList nl = parent.getChildNodes();
    List<Element> list = new ArrayList<Element>(nl.getLength());
    for (int i = 0 ; i < nl.getLength(); i++) 
    {
      Node child = nl.item(i);
      if ((child.getNodeType()==Node.ELEMENT_NODE) && (child.getNodeName().equals(name)))
      {
        list.add((Element)child);
      }
    }    
    return list;
  }

  
  private List<Element> getChildElements(Element parent)  {
    NodeList nl = parent.getChildNodes();
    List<Element> list = new ArrayList<Element>(nl.getLength());
    for (int i = 0 ; i < nl.getLength(); i++) 
    {
      Node child = nl.item(i);
      if (child.getNodeType()==Node.ELEMENT_NODE)
      {
        list.add((Element)child);
      }
    }    
    return list;
  }
  

  
  private Element getFirstChildElementByName(Element parent, String name)
  {
    NodeList nl = parent.getChildNodes();
    for (int i = 0 ; i < nl.getLength(); i++) 
    {
      Node child = nl.item(i);
      if ((child.getNodeType()==Node.ELEMENT_NODE) && (child.getNodeName().equals(name)))
      {
        return (Element)child;
      }
    }    
    return null;
  }
  

  
  private String getAttributeValueForChild(Element parent, String elementName, String attributeName)
  {
    Element element = getFirstChildElementByName(parent, elementName);
    if (element==null)
    {
      return "";
    }
    else
    {
      return element.getAttribute(attributeName);
    }
  }

  
  private String getSymbolAttributeValueForChild(Element parent, String elementName, String attributeName, String attributeSymbolName)
  {
    Element element = getFirstChildElementByName(parent, elementName);
    if (element==null)
    {
      return "";
    }
    else
    {
      return getSymbolAttributeValue(element,attributeName,attributeSymbolName);
    }
  }
  

  private String getSymbolAttributeValue(Element element, String attributeName, String attributeSymbolName)
  {
    String s = element.getAttribute(attributeName);
    if (s!="")
    {
      return s;
    }
    else
    {
      try
      {
        return Configuration.SYMBOLS.valueOf(element.getAttribute(attributeSymbolName)).getData();
      }
      catch (Exception ex)
      {
        return "";
      }
    }
  }

  
  public final File getInputFile()
  {
    return inputFile;
  }


  public final void setInputFile(String inputFileName) throws IOException
  {
    this.inputFile = new File(inputFileName).getCanonicalFile();
  }


  public final File getOutputFile()
  {
    return outputFile;
  }


  public final File getConfigFile()
  {
    return configFile;
  }



  public final void setOutputFile(String outputFileName) throws IOException
  {
    this.outputFile = new File(outputFileName).getCanonicalFile();
  }


  public final File getLogFile()
  {
    return logFile;
  }


  public final void setLogFile(String logFileName) throws IOException
  {
    this.logFile = new File(logFileName).getCanonicalFile();
  }


  public final String getDelimiter()
  {
    return delimiter;
  }


  public final String getBracketLeft()
  {
    return bracketLeft;
  }


  public final String getBracketRight()
  {
    return bracketRight;
  }


  public final String getOutDelimiter()
  {
    return outDelimiter;
  }


  public final String getOutBracketLeft()
  {
    return outBracketLeft;
  }


  public final String getOutBracketRight()
  {
    return outBracketRight;
  }


  public final String getCharset()
  {
    return charset;
  }


  public final String getOutCharset()
  {
    return outCharset;
  }


  public final boolean isTransformLine()
  {
    return transformLine;
  }


  public final boolean isContentToLaTeX()
  {
    return contentToLaTeX;
  }


  public final boolean isIgnoreHeader()
  {
    return ignoreHeader;
  }


  public final boolean isFilterOutputColumns()
  {
    return (outColumnArray!=null);
  }

  
  public final boolean isSorting()
  {
    return (ccArray.length>0);
  }
  

  public final NumberName[] getOutColumnArray()
  {
    return outColumnArray;
  }


  public final ColumnComparator[] getCcArray()
  {
    return ccArray;
  }


  public final Locale getLocale()
  {
    return locale;
  }



  public final boolean isInputBrackets()
  {
    return inputBrackets;
  }
  
  
  public LineSplitter createLineSplitter()
  {
    if (inputBrackets)
    {
      return new LineSplitterBrackets(getDelimiter(),getBracketLeft(),getBracketRight());
    }
    else
    {
      return new LineSplitterSimple(getDelimiter());
    }    
  }



  public final boolean isAllowOverwrite()
  {
    return allowOverwrite;
  }



  public final void setAllowOverwrite(boolean allowOverwrite)
  {
    this.allowOverwrite = allowOverwrite;
  }
  
    
}