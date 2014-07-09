package tfs.csvsorter;

import java.util.HashMap;

/**
 *  ColumnComparator: Comparator for columns
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      June 2010
 */  
@SuppressWarnings("rawtypes")
interface ColumnComparator
{
    
  public Comparable generateKey(String[] parts) throws Exception;
  
  public void config(HashMap<String,Integer> nameToColumn, int maxNumber) throws Exception;
   
  
}
