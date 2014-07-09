package tfs.csvsorter;


/**
 *  LineSplitterSimple: Splitting a String (without brackets)
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      July 2014
 */
public class LineSplitterSimple extends LineSplitter
{  
  
  
  public LineSplitterSimple(String delimiter)
  {
    super(delimiter);
  }
    

  int getNextDelimiterPosition(String line, int fromIndex)
  {
    return line.indexOf(delimiter, fromIndex);
  }



  String trimString(String s)
  {    
    return s.trim();
  }
  
   
}
