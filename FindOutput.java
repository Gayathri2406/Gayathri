
import java.util.ArrayList;

public class FindOutput {
    private boolean isCorrect=true;
    private ArrayList<String> varibales=new ArrayList<>();
    private ArrayList<String> dataTypes=new ArrayList<>();
    private String output="";

    public String getOutput() {
        return output;
    }
    
    public String removeSpace(String input)
    {
        String temp="";
        for(int i=0;i<input.length();i++)
        {
            if(input.charAt(i)=='"')
            {
                temp+=input.substring(i,input.indexOf('"', i+1)+1);
                i=input.indexOf('"', i+1);
            }
            else if(input.charAt(i)!=' ')
                temp+=input.charAt(i);
        }
        return temp;
    }
    
    public boolean checkForParanthesis(String input)
    {
        if(input.charAt(0)!='{' || input.charAt(input.length()-1)!='}')
            return false;
        return true;
    }
    
    public String getDataType(String input)
    {
        if(input.charAt(0)=='[' && input.charAt(input.length()-1)==']')
        {
            int index=input.indexOf(",");
            if(index==-1)
                index=input.indexOf("]");
            return "List("+getDataType(input.substring(1,index))+")";
        }
        else if(input.charAt(0)=='"' && input.charAt(input.length()-1)=='"')
            return "String";
        else{
            int i=0;
            boolean isDot=false;
            for(;i<input.length();i++)
            {
                if(i==0 && input.charAt(i)=='-')
                    continue;
                else if(input.charAt(i)=='.')
                {
                    if(isDot)
                        isCorrect=false;
                    isDot=true;
                }
                else if(input.charAt(i)<'0' || input.charAt(i)>'9')
                    break;
            }
            if(i==input.length())
            {
                if(isDot)
                    return "double";
                else
                {
                    if(input.length()>9)
                        return "long";
                    else
                        return "int";
                }
           }
        }
        isCorrect=false;
        return "";
    }
    
    public void separateVariables(String input)
    {
        int index;
        String var,data;
        for(int i=0;i<input.length();i++)
        {
            if(input.charAt(i)!='"')
            {
                isCorrect=false;
                break;
            }
            else
            {
                index=input.indexOf('"',i+1);
                if(index==-1)
                {
                    isCorrect=false;
                    break;
                }
                var=input.substring(i+1,index);
                i=index+2;
                if(var.contains(" "))
                {
                    isCorrect=false;
                    break;
                }
                varibales.add(var);
                if(input.charAt(i)=='[' || input.charAt(i)=='"')
                {
                    index=input.indexOf("]",i);
                    if(index==-1)
                        index=input.indexOf('"',i+1);
                    if(index==-1)
                    {
                        isCorrect=false;
                        break;
                    }
                    data=getDataType(input.substring(i,index+1));
                    i=index+1;
                }
                else
                {
                    index=input.indexOf(',',i);
                    if(index==-1)
                    {
                        index=input.length();
                    }
                    data=getDataType(input.substring(i,index));
                    i=index;
                }
                dataTypes.add(data);
            }
        }
    }
    
    public String generateGetter(String dataType,String variable)
    {
        return "public "+dataType+" get"+variable.substring(0,1).toUpperCase()+variable.substring(1)+"(){\n"+
                "return this."+variable+";\n}\n";
    }
    
    public String generateSetter(String dataType,String variable)
    {
        return "public void set"+variable.substring(0,1).toUpperCase()+variable.substring(1)+
                "("+dataType+" "+variable+"){\n"+
                "this."+variable+" = "+variable+";\n}\n";
    }
    
    public boolean isCorrectSyntax(String input)
    {
        input=removeSpace(input);
        isCorrect=checkForParanthesis(input);
        output="import java.util.*;\n";
        output+="public class POJO\n";
        output+="{\n";
        separateVariables(input.substring(1,input.length()-1));
        if(!isCorrect)
            return isCorrect;
        for(int i=0;i<varibales.size();i++)
        {
            output+="private "+dataTypes.get(i)+" "+varibales.get(i)+";\n";
            output+="\n";
        }
        for(int i=0;i<varibales.size();i++)
        {
            output+=generateSetter(dataTypes.get(i), varibales.get(i));
            output+=generateGetter(dataTypes.get(i), varibales.get(i));
        }
        output+="}\n";
        return isCorrect;
    }
}
