package tfs.csvsorter;

import java.text.CollationKey;
import java.text.Collator;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Locale;


/**
 *  SingleColumnComparator: Comparator for one column
 *
 * @author     Prof. Dr. Dr. Thomas F. Sturm
 * @since      June 2010
 */
abstract class SingleColumnComparator implements ColumnComparator
{

  private NumberName numberName;


  public SingleColumnComparator(NumberName numberName)
  {
    this.numberName = numberName;
  }


  public int getNumber()
  {
    return numberName.getNumber();
  }


  public void config(HashMap<String,Integer> nameToColumn, int maxNumber) throws Exception
  {
    numberName.config(nameToColumn,maxNumber);
  }


  int getIntegerValue(String[] parts) throws Exception
  {
    return Integer.parseInt(parts[numberName.getNumber()]);
  }

  long getLongValue(String[] parts) throws Exception
  {
    return Long.parseLong(parts[numberName.getNumber()]);
  }


  double getDoubleValue(String[] parts) throws Exception
  {
    return Double.parseDouble(parts[numberName.getNumber()]);
  }


  long getDateValue(String[] parts, Locale locale) throws Exception
  {
    return DateFormat.getDateInstance(DateFormat.DEFAULT,locale).parse(parts[getNumber()]).getTime();
  }


  @SuppressWarnings("rawtypes")
  public static SingleColumnComparator createIntegerComparator(NumberName numberName, boolean ascending, String defaultString) throws Exception
  {
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new SingleColumnComparator(numberName)
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
        return new SingleColumnComparator (numberName)
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
        return new SingleColumnComparator (numberName)
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
        return new SingleColumnComparator (numberName)
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
  public static SingleColumnComparator createLongComparator(NumberName numberName, boolean ascending, String defaultString) throws Exception
  {
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new SingleColumnComparator(numberName)
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
        return new SingleColumnComparator (numberName)
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
        return new SingleColumnComparator (numberName)
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
        return new SingleColumnComparator (numberName)
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
  public static SingleColumnComparator createDoubleComparator(NumberName numberName, boolean ascending, String defaultString) throws Exception
  {
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new SingleColumnComparator(numberName)
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
        return new SingleColumnComparator (numberName)
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
        return new SingleColumnComparator (numberName)
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
        return new SingleColumnComparator (numberName)
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



  @SuppressWarnings("rawtypes")
  public static SingleColumnComparator createDateComparator(NumberName numberName, final Locale locale, boolean ascending, String defaultString) throws Exception
  {
    if (ascending)
    {
      if (defaultString.equals(""))
      {
        return new SingleColumnComparator(numberName)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return new Long(getDateValue(parts,locale));
          }
        };
      }
      else
      {
        final Long defaultKey = new Long(DateFormat.getDateInstance(DateFormat.DEFAULT,locale).parse(defaultString).getTime());
        return new SingleColumnComparator (numberName)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return new Long(getDateValue(parts,locale));
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
        return new SingleColumnComparator (numberName)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            return new Long(-getDateValue(parts,locale));
          }
        };
      }
      else
      {
        final Long defaultKey = new Long(-DateFormat.getDateInstance(DateFormat.DEFAULT,locale).parse(defaultString).getTime());
        return new SingleColumnComparator (numberName)
        {
          public Comparable generateKey(String[] parts) throws Exception
          {
            try
            {
              return new Long(-getDateValue(parts,locale));
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
  public static SingleColumnComparator createStringComparator(NumberName numberName, final Collator collator, boolean ascending) throws Exception
  {
    if (ascending)
    {
      return new SingleColumnComparator(numberName)
      {
        
        public Comparable generateKey(String[] parts) throws Exception
        {
          return collator.getCollationKey(parts[getNumber()]);
        }
      };
    }
    else
    {
      return new SingleColumnComparator (numberName)
      {
        public Comparable generateKey(String[] parts) throws Exception
        {
          final class CollationKeyInverse implements Comparable<CollationKeyInverse>
          {
            private CollationKey theKey;

            public CollationKeyInverse(String s)
            {
              theKey = collator.getCollationKey(s);
            }

            public int compareTo(CollationKeyInverse anotherKey)
            {
              return anotherKey.theKey.compareTo(theKey);
            }
          }
          return new CollationKeyInverse(parts[getNumber()]);
        }
      };
    }
  }



}