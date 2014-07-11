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


  int getIntegerValue(String[] parts) throws Exception
  {
    int s = 0;
    for (NumberName nn : getNumberNames())
    {
      s += Integer.parseInt(parts[nn.getNumber()]);
    }
    return s;
  }


  long getLongValue(String[] parts) throws Exception
  {
    long s = 0;
    for (NumberName nn : getNumberNames())
    {
      s += Long.parseLong(parts[nn.getNumber()]);
    }
    return s;
  }



  double getDoubleValue(String[] parts) throws Exception
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
            return new Integer(getIntegerValue(parts));
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
              return new Integer(getIntegerValue(parts));
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
            return new Integer(-getIntegerValue(parts));
          }
        };
      }
      else
      {
        final Integer defaultKey = new Integer(-Integer.parseInt(defaultString));
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return new Integer(-getIntegerValue(parts));
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
            return new Long(getLongValue(parts));
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
              return new Long(getLongValue(parts));
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
            return new Long(-getLongValue(parts));
          }
        };
      }
      else
      {
        final Long defaultKey = new Long(-Long.parseLong(defaultString));
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return new Long(-getLongValue(parts));
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
            return new Double(getDoubleValue(parts));
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
              return new Double(getDoubleValue(parts));
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
            return new Double(-getDoubleValue(parts));
          }
        };
      }
      else
      {
        final Double defaultKey = new Double(-Double.parseDouble(defaultString));
        return new MultiColumnComparator(numberNames,ascending,type,defaultString)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return new Double(-getDoubleValue(parts));
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
