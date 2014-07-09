package tfs.csvsorter;


/**
 *  LineSplitterBrackets: Splitting a String (with brackets)
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      January 2009, July 2014
 */
public class LineSplitterBrackets extends LineSplitter
{  

  private final String bracketLeft;
  private final String bracketRight;
  private final BracketFinder bracketFinder;
  
  
  public LineSplitterBrackets(String delimiter, String theBracketLeft, String theBracketRight)
  {
    super(delimiter);
    this.bracketLeft = theBracketLeft;
    this.bracketRight = theBracketRight;
    
    if (bracketLeft.equals(bracketRight))
    {
      bracketFinder = new BracketFinder()
      {
        public int getNextClosingBracketPosition(String line, int fromIndex)
        {
          return line.indexOf(bracketLeft,fromIndex);
        }        
      };      
    }
    else
    {
      bracketFinder = new BracketFinder()
      {
        public int getNextClosingBracketPosition(String line, int fromIndex)
        {
          int pzu = line.indexOf(bracketRight,fromIndex);
          if (pzu<0)
          {
            return pzu;
          }
          int pauf = line.indexOf(bracketLeft,fromIndex);
          if ((pauf<0) || (pauf>pzu))
          {
            return pzu;
          }
          pzu = getNextClosingBracketPosition(line, pauf+1);
          if (pzu<0)
          {
            return pzu;
          }
          else
          {
            return getNextClosingBracketPosition(line, pzu+1);
          }
        }        
      };
    }
  }
  
  
  
  int getNextDelimiterPosition(String line, int fromIndex)
  {
    int pdel = line.indexOf(delimiter,fromIndex);
    if (pdel<0)
    {
      return pdel;
    }
    int pauf = line.indexOf(bracketLeft,fromIndex);
    if ((pauf<0) || (pauf>pdel))
    {
      return pdel;
    }
    int pzu = bracketFinder.getNextClosingBracketPosition(line, pauf+1);
    if (pzu<0) 
    {
      // actually, an error
      return pzu;
    }
    else 
    {
      return getNextDelimiterPosition(line, pzu+1);
    }
  }
  
  
  String trimString(String s)
  {
    s = s.trim();
    int pos = s.indexOf(bracketLeft);
    if (pos>=0)
    {
      s = s.substring(pos+bracketLeft.length());
    }    
    pos = s.lastIndexOf(bracketRight);
    if (pos>=0)
    {
      s = s.substring(0,pos);
    }    
    return s;      
  }
  
    
  private interface BracketFinder
  {
    int getNextClosingBracketPosition(String line, int fromIndex);    
  }
  
  
}
