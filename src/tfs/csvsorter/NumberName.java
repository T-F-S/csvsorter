package tfs.csvsorter;

import java.util.HashMap;
import java.util.Locale;

import org.w3c.dom.Element;

/**
 *  NumberName: Pait of number and name
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      June 2010
 */  
class NumberName
{
  
  private int number;
  private String name;
  
  
  public NumberName(String theNumber, String theName) throws Exception
  {
    if (theNumber!="")
    {
      number = Integer.parseInt(theNumber)-1;
    }
    else
    {
      number = -1;
    }
    if (theName!="")
    {
      name = theName;  
    }
    else
    {
      name = null;
      if (number<0)
      {
        throw new Exception("Column without number and name");
      }
    }      
  }
  
  
  public NumberName(Element columnElement) throws Exception
  {
    this(columnElement.getAttribute("number"),columnElement.getAttribute("name"));    
  }
  
  
  public int getNumber()
  {
    return number;
  }


  public int getNumberPlusOne()
  {
    return number+1;
  }
  
    
  public String getName()
  {
    return name;
  }
  
  
  public String getDescription()
  {
    if (name!=null)
    {
      return "\""+name+"\"["+getNumberPlusOne()+"]";
    }
    else
    {
      return "["+getNumberPlusOne()+"]";
    }
  }

  
  
  public void config(HashMap<String,Integer> nameToColumn, int maxNumber) throws Exception
  {
    if (number<0)
    {
      Integer number = nameToColumn.get(name.toLowerCase(Locale.ROOT));
      if (number==null)
      {
        throw new Exception("Unknown column name: >"+name+"<");
      }
      else
      {
        this.number = number;
      }
    }
    if (number>=maxNumber)
    {
      throw new Exception("Column number "+(number+1)+" too large (maximal "+maxNumber+")");
    }
  }
    
}