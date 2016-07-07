import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by cristph on 2015/11/18.
 */
public class LexicalAnalysis {

    Map<String,String> tokenMap;
    ArrayList<ArrayList> tokenList;

    public LexicalAnalysis(){
        tokenMap=new HashMap<>();
        tokenMap.put("int","INT");
        tokenMap.put("float","FLOAT");
        tokenMap.put("void","VOID");
        tokenMap.put("if","IF");
        tokenMap.put("while","WHILE");
        tokenMap.put("return","RETURN");
        tokenMap.put("=","assignOP");
        tokenMap.put("==","EQ");
        tokenMap.put("!=","NE");
        tokenMap.put(">=","GE");
        tokenMap.put("<=","LE");
        tokenMap.put(">","GT");
        tokenMap.put("<","LT");
        tokenMap.put("(","LS");
        tokenMap.put(")","RS");
        tokenMap.put("{","LB");
        tokenMap.put("}","RB");
        tokenMap.put(";","endLine");
        tokenMap.put("string","ID");
        tokenMap.put("number","NUMBER");

        tokenList=new ArrayList<>();
    }


    public void readFile(String filePath){
        File file=new File(filePath);
        try {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNext()){
                String line=scanner.nextLine();
                //System.out.println(line);
                char []charSet=line.toCharArray();
                int charSetLength=charSet.length;
                //System.out.println(charSet.length);
                String tempString="";
                String value="";
                ArrayList<String> list=new ArrayList<>();
                for(int i=0;i<charSetLength;i++){
                    //System.out.println(charSet[i]);
                    char tempChar=charSet[i];
                    if(tempChar==' '||tempChar=='\t'){//sapce or tab
                        if(tempString.length()>0){
                            if(tokenMap.containsKey(tempString)){
                                value=tokenMap.get(tempString);
                            }else if(tempString.matches("^(-?\\d+)(\\.\\d+)?$")){
                                value="NUMBER";
                            }else{
                                value="ID";
                            }
                            //System.out.print("<"+value+" "+tempString+"> ");
                            list.add(value);
                            tempString="";
                        }
                    }else{
                        String charCopy=String.valueOf(tempChar);
                        if(tokenMap.containsKey(charCopy)){//operand in tpkenMap
                            if(tempString.length()>0){
                                if(tokenMap.containsKey(tempString)){
                                    value=tokenMap.get(tempString);
                                }else if(tempString.matches("^(-?\\d+)(\\.\\d+)?$")){
                                    value="NUMBER";
                                }else{
                                    value="ID";
                                }
                                //System.out.print("<"+value+" "+tempString+"> ");
                                list.add(value);
                            }

                            //determine if the this char is also part of operand
                            if(i+1<charSet.length){//not last char
                                String test=charCopy+charSet[i+1];
                                if(tokenMap.containsKey(test)){//if part of double-operand
                                    //System.out.print("/--------after--------/");//do nothing now
                                    i++;
                                    value=tokenMap.get(test);
                                    //System.out.print("<"+value+" "+test+"> ");
                                    list.add(value);
                                }else{//not part of double-operand
                                    value=tokenMap.get(charCopy);
                                    //System.out.print("<"+value+" "+charCopy+"> ");
                                    list.add(value);
                                    tempString="";
                                }
                            }else{//last char
                                value=tokenMap.get(charCopy);
                                //System.out.print("<"+value+" "+charCopy+"> ");
                                list.add(value);
                                tempString="";
                            }
                        }else{
                            String test=charCopy+charSet[i+1];
                            if(tokenMap.containsKey(test)){//if part of double-operand
                                //System.out.print("/--------after--------/");//do nothing now
                                i++;
                                value=tokenMap.get(test);
                                //System.out.print("<"+value+" "+test+"> ");
                                list.add(value);
                                tempString="";
                            }else{//not part of double-operand
                                tempString+=tempChar;
                            }

                            //tempString+=tempChar;
                        }
                    }
                }
                tokenList.add(list);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void analyse(String filePath){
        readFile(filePath);
//        for(ArrayList<String> list:tokenList){
//            for(String string:list){
//                System.out.print(string);
//                System.out.print("  ");
//            }
//            System.out.println();
//        }
//        System.out.println();
    }

    public String getLexicalAnalysisResult(){
        String result="";
        for(ArrayList<String> list:tokenList){
            for(String string:list){
                result+=string;
                result+=" ";
            }
        }
        result+="$";
        return result;
    }
}