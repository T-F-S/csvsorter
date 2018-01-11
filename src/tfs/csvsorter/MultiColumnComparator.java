package tfs.csvsorter;

import java.util.HashMap;

/**
 *  MultiColumnComparator: Comparator for multiple columns
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      June 2010
 */
abstract class MultiColumnComparator implements ColumnComparator
{

  private NumberName[] numberNames;
  private boolean ascending;
  private String type;
  private String defaultValue;


  public MultiColumnComparator(NumberName[] numberNames, boolean ascending, String type, String defaultValue)
  {
    this.numberNames = numberNames;
    this.ascending = ascending;
    this.type = type;
    this.defaultValue = defaultValue;
  }

  
  public String getDescription()
  {
    String s = "Sort "+numberNames[0].getDescription();
    for (int i=1;i<numberNames.length;i++)
    {
      s += " + " + numberNames[i].getDescription();
    }
    s += " in ";
    if (ascending)
    {
      s += "ascending";
    }
    else
    {
      s += "descending";
    }
    s += " order as "+type;
    if (!defaultValue.equals(""))
    {
      return s + " (default: "+defaultValue+").";
    }
    else
    {
      return s+".";
    }
  }
  

  public NumberName[] getNumberNames()
  {
    return numberNames;
  }


  public void config(HashMap<String,Integer> nameToColumn, int maxNumber) throws Exception
  {
    for (NumberName nn : numberNames)
    {
      nn.config(nameToColumn,maxNumber);
    }
  }


  Integer getIntegerValue(String[] parts) throws Exception
  {
    int s = 0;
    for (NumberName nn : getNumberNames())
    {
      s += Integer.parseInt(parts[nn.getNumber()]);
    }
    return s;
  }


  Long getLongValue(String[] parts) throws Exception
  {
    long s = 0;
    for (NumberName nn : getNumberNames())
    {
      s += Long.parseLong(parts[nn.getNumber()]);
    }
    return s;
  }



  Double getDoubleValue(String[] parts) throws Exception
  {
    double s = 0;
    for (NumberName nn : getNumberNames())
    {
      s += Double.parseDouble(parts[nn.getNumber()]);
    }
    return s;
  }



  @SuppressWarnings("rawtypes")
  public static MultiColumnComparator createIntegerComparator(NumberName[] numberNames, boolean ascending, String defaultString) throws Exception
  {
    final String type="integer";
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return getIntegerValue(parts);
          }
        };
      }
      else
      {
        final Integer defaultKey = Integer.valueOf(defaultString);
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return getIntegerValue(parts);
            }
            catch (Exception e)
            {
              return defaultKey;
            }
          }
        };
      }
    }
    else
    {
      if (defaultString.equals(""))
      {
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return Integer.valueOf(-getIntegerValue(parts));
          }
        };
      }
      else
      {
        final Integer defaultKey = Integer.valueOf(-Integer.parseInt(defaultString));
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return Integer.valueOf(-getIntegerValue(parts));
            }
            catch (Exception e)
            {
              return defaultKey;
            }
          }
        };
      }
    }
  }



  @SuppressWarnings("rawtypes")
  public static MultiColumnComparator createLongComparator(NumberName[] numberNames, boolean ascending, String defaultString) throws Exception
  {
    final String type="long";
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return getLongValue(parts);
          }
        };
      }
      else
      {
        final Long defaultKey = Long.valueOf(defaultString);
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return getLongValue(parts);
            }
            catch (Exception e)
            {
              return defaultKey;
            }
          }
        };
      }
    }
    else
    {
      if (defaultString.equals(""))
      {
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return Long.valueOf(-getLongValue(parts));
          }
        };
      }
      else
      {
        final Long defaultKey = Long.valueOf(-Long.parseLong(defaultString));
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return Long.valueOf(-getLongValue(parts));
            }
            catch (Exception e)
            {
              return defaultKey;
            }
          }
        };
      }
    }
  }



  @SuppressWarnings("rawtypes")
  public static MultiColumnComparator createDoubleComparator(NumberName[] numberNames, boolean ascending, String defaultString) throws Exception
  {
    final String type="double";
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return getDoubleValue(parts);
          }
        };
      }
      else
      {
        final Double defaultKey = Double.valueOf(defaultString);
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return getDoubleValue(parts);
            }
            catch (Exception e)
            {
              return defaultKey;
            }
          }
        };
      }
    }
    else
    {
      if (defaultString.equals(""))
      {
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return Double.valueOf(-getDoubleValue(parts));
          }
        };
      }
      else
      {
        final Double defaultKey = Double.valueOf(-Double.parseDouble(defaultString));
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return Double.valueOf(-getDoubleValue(parts));
            }
            catch (Exception e)
            {
              return defaultKey;
            }
          }
        };
      }
    }
  }


}
