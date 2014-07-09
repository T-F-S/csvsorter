package tfs.csvsorter;

import java.util.ArrayList;


/**
 *  LineSplitter: Splitting a String
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      January 2009
 */
public abstract class LineSplitter
{  
  
  final String delimiter;
  
  abstract int getNextDelimiterPosition(String line, int fromIndex);
  
  abstract String trimString(String s);
  
  
  
  public LineSplitter(String delimiter)
  {
    this.delimiter = delimiter;
  }  

  
  
  public String[] splitTrimmed(String line)
  {
    ArrayList<String> parts = new ArrayList<String>();    
    int pdel = getNextDelimiterPosition(line, 0);
    while (pdel>=0)
    {
      parts.add(trimString(line.substring(0, pdel)));
      line = line.substring(pdel+delimiter.length());
      pdel = getNextDelimiterPosition(line, 0);
    }
    parts.add(trimString(line));
    return parts.toArray(new String[0]);
  }
  
  
  public String[] splitTrimmed(String line, int length) throws Exception
  {
    String[] parts = new String[length];
    for (int i=0;i<length-1;i++)
    {
      int pdel = getNextDelimiterPosition(line, 0);
      if (pdel<0)
      {
        throw new Exception("Too few columns ("+(i+1)+")");
      }
      parts[i] = trimString(line.substring(0, pdel));
      line = line.substring(pdel+delimiter.length());
    }
    int pdel = getNextDelimiterPosition(line, 0);
    if (pdel>=0)
    {
      throw new Exception("Too many columns");
    }
    parts[length-1] = trimString(line);
    return parts;
  }
  
  
}