package evidence;

public class AttributeValue { 
    private final String _value; 
    private final String _attributeName; 
    private final int _bookCode;
    private final String _personID;

    public AttributeValue(String value, String attributeName, int bookCode, String personID) { 
      this._value=value;
      this._attributeName=attributeName;
      this._bookCode=bookCode;  
      this._personID=personID;
    } 

    public String getValue(){
      return _value;
    }

    public String getAttributeName(){
      return _attributeName;
    }

    public int getBookCode(){
      return _bookCode;
    }

    public String getPersonNumber(){
      return _personID;
    }
  } 