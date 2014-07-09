package tfs.csvsorter;


/**
 *  LineContainer: Container for one line and sorting key
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      January 2009
 */
public class LineContainer implements Comparable<LineContainer>
{  
  private String line;
  @SuppressWarnings("rawtypes")
  private Comparable[] keyarray;
  private String[] parts;
  
  
  public LineContainer(String line, String[] parts, @SuppressWarnings("rawtypes") Comparable[] keyarray)
  {
    this.line = line;
    this.parts = parts;
    this.keyarray = keyarray;
  }

  
  @SuppressWarnings("rawtypes")
  public static LineContainer makeLineContainer(String line, String[] parts, ColumnComparator[] ccArray) throws Exception
  {
    Comparable[] key = new Comparable[ccArray.length];
    for (int i=0; i<ccArray.length; i++)
    {
      ColumnComparator cc = ccArray[i];
      key[i] = cc.generateKey(parts);
    }
    return new LineContainer(line, parts, key);
  }
    

  public final String getOriginalLine()
  {
    return line;
  }
  
  
  private final static String filterLaTeX(String s)
  {
    s = s.replace("\\", "\\textbackslash{}");    
    s = s.replace("&", "\\&{}");    
    s = s.replace("#", "\\#{}");    
    s = s.replace("%", "\\%{}");    
    return s;
  }


  private final static String getTransformedLineLaTeX(String[] parts, int[] index, String delimiter,String bracketLeft,String bracketRight)
  {
    String s = bracketLeft+filterLaTeX(parts[index[0]])+bracketRight;    
    {
      for (int i=1;i<index.length;i++)
      {
        s = s + delimiter+bracketLeft+filterLaTeX(parts[index[i]])+bracketRight;
      }
    }
    return s;
  }
  
  
  private final static String getTransformedLine(String[] parts, int[] index, String delimiter,String bracketLeft,String bracketRight)
  {
    String s = bracketLeft+parts[index[0]]+bracketRight;    
    {
      for (int i=1;i<index.length;i++)
      {
        s = s + delimiter+bracketLeft+parts[index[i]]+bracketRight;
      }
    }
    return s;
  }
  
  
  
  
  public final static String getTransformedLine(String[] parts, int[] index, String delimiter,String bracketLeft,String bracketRight,boolean toLaTeX)
  {
    if (parts.length==0)
    {
      return "";
    }
    if (toLaTeX)
    {
      return getTransformedLineLaTeX(parts,index,delimiter,bracketLeft,bracketRight);
    }
    else
    {
      return getTransformedLine(parts,index,delimiter,bracketLeft,bracketRight);
    }
  }

  
  public final String getTransformedLine(int[] index,String delimiter,String bracketLeft,String bracketRight,boolean toLaTeX)
  {
    return getTransformedLine(parts,index,delimiter,bracketLeft,bracketRight,toLaTeX);
  }
  
    

  @SuppressWarnings("rawtypes")
  public final Comparable[] getKeyarray()
  {
    return keyarray;
  }
  
  
  public int compareTo(LineContainer anotherLC)
  {
    @SuppressWarnings("rawtypes")
    Comparable[] anotherKeyarray = anotherLC.getKeyarray();
    for (int i=0; i<keyarray.length; i++)
    {
      @SuppressWarnings("unchecked")
      int comp = keyarray[i].compareTo(anotherKeyarray[i]);
      if (comp!=0)
      {
        return comp;
      }
    }
    return 0;    
  }
    
}
