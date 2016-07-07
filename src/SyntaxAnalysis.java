import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by cristph on 2015/11/19.
 */
public class SyntaxAnalysis {

    Map<String,String> programTable;
    Map<String,String> dataTypeTable;
    Map<String,String> functionNameTable;
    Map<String,String> parameterListTable;
    Map<String,String> parameterTable;
    Map<String,String> statementListTable;
    Map<String,String> statementTable;
    Map<String,String> subStatementTable;
    Map<String,String> conditionTable;
    Map<String,String> compareOpTable;
    Map<String,String> compareParaTable;
    Map<String,String> blockTable;
    Map<String,String> returnStatementTable;

    Stack<String> stack;

    public SyntaxAnalysis(){
        /*initial program预测分析表*/
        programTable=new HashMap<>();
        programTable.put("VOID","program -> void functionName LS parameterList RS LB statementList RB");
        programTable.put("INT","program -> dataType functionName LS parameterList RS LB statementList returnStatement RB");
        programTable.put("FLOAT","program -> dataType functionName LS parameterList RS LB statementList returnStatement RB");
        programTable.put("$","program -> ");

        /*initial dataType预测分析表*/
        dataTypeTable=new HashMap<>();
        dataTypeTable.put("INT","dataType -> INT");
        dataTypeTable.put("FLOAT","dataType -> FLOAT");

        /*initial functionNameType预测分析表*/
        functionNameTable=new HashMap<>();
        functionNameTable.put("ID","functionName -> ID");

        /*initial parameterList预测分析表*/
        parameterListTable=new HashMap<>();
        parameterListTable.put("INT","parameterList -> parameter");
        parameterListTable.put("FLOAT","parameterList -> parameter");
        parameterListTable.put("RS","parameterList -> ");//RS　means ')'

        /*initial parameter预测分析表*/
        parameterTable=new HashMap<>();
        parameterTable.put("INT","parameter -> dataType ID");
        parameterTable.put("FLOAT","parameter -> dataType ID");

        /*initial statementList预测分析表*/
        statementListTable=new HashMap<>();
        statementListTable.put("INT","statementList -> statement endLine statementList");
        statementListTable.put("FLOAT","statementList -> statement endLine statementList");
        statementListTable.put("ID","statementList -> statement endLine statementList");
        statementListTable.put("IF","statementList -> statement");
        statementListTable.put("WHILE","statementList -> statement");
        statementListTable.put("RB","statementList -> ");

        /*initial statement预测分析表*/
        statementTable=new HashMap<>();
        statementTable.put("INT","statement -> dataType subStatement");
        statementTable.put("FLOAT","statement -> dataType subStatement");
        statementTable.put("ID","statement -> subStatement");
        statementTable.put("IF","statement -> IF LS condition RS block");
        statementTable.put("WHILE","statement -> WHILE LS condition RS block");

        /*initial subStatement预测分析表*/
        subStatementTable=new HashMap<>();
        subStatementTable.put("ID","subStatement -> ID assignOP NUMBER");

        /*initial condition预测分析表*/
        conditionTable=new HashMap<>();
        conditionTable.put("ID","condition -> comparePara compareOp comparePara");
        conditionTable.put("NUMBER","condition -> comparePara compareOp comparePara");

        /*initial compareOp预测分析表*/
        compareOpTable=new HashMap<>();
        compareOpTable.put("EQ","compareOp -> EQ");
        compareOpTable.put("NE","compareOp -> NE");
        compareOpTable.put("GE","compareOp -> GE");
        compareOpTable.put("LE","compareOp -> LE");
        compareOpTable.put("GT","compareOp -> GT");
        compareOpTable.put("LT","compareOp -> LT");

        /*initial comparePara预测分析表*/
        compareParaTable=new HashMap<>();
        compareParaTable.put("ID","comparePara -> ID");
        compareParaTable.put("NUMBER","comparePara -> NUMBER");

        /*initial block预测分析表*/
        blockTable=new HashMap<>();
        blockTable.put("INT","block -> statement endLine");
        blockTable.put("FLOAT","block -> statement endLine");
        blockTable.put("ID","block -> statement endLine");
        blockTable.put("IF","block -> statement endLine");
        blockTable.put("WHILE","block -> statement endLine");
        blockTable.put("LB","block -> LB statementList RB");

        /*initial returnStatement预测分析表*/
        returnStatementTable=new HashMap<>();
        returnStatementTable.put("RETURN","returnStatement -> RETURN comparePara endLine");
    }

    public String getMatchedToken(String nonTerminal,String pattern){
        switch (nonTerminal){
            case "program":
                return programTable.get(pattern);
            case "dataType":
                return dataTypeTable.get(pattern);
            case "functionName":
                return functionNameTable.get(pattern);
            case "parameterList":
                return parameterListTable.get(pattern);
            case "parameter":
                return parameterTable.get(pattern);
            case "statementList":
                return statementListTable.get(pattern);
            case "statement":
                return statementTable.get(pattern);
            case "subStatement":
                return subStatementTable.get(pattern);
            case "condition":
                return conditionTable.get(pattern);
            case "compareOp":
                return compareOpTable.get(pattern);
            case "comparePara":
                return compareParaTable.get(pattern);
            case "block":
                return blockTable.get(pattern);
            case "returnStatement":
                return returnStatementTable.get(pattern);
            default:
                return "error";
        }
    }

    public void pushString(Stack stack,String string){
        String temp=string.substring(string.indexOf(">")+2);
        if(temp.length()==0){
            //System.out.println("insert empty String");
            return;
        }
        String []result=temp.split(" ");
        int size=result.length;
        //System.out.println("size: "+size);
        for(int i=size-1;i>=0;i--){
            //System.out.println("push: "+result[i]);
            stack.push(result[i]);
        }
    }

    public void showStack(Stack<String> stack){
        for(String string:stack){
            System.out.print(string + " ");
        }
        System.out.println();
    }

    public void showMatchedString(String []pattern,int pos){
        System.out.print("have been matched: ");
        for(int i=0;i<=pos;i++){
            System.out.print(pattern[i] + " ");
        }
        System.out.println();
    }

    public void analyse(String lexicalAnalysisResult){
        //System.out.println("ini:"+lexicalAnalysisResult);
        String []pattern=lexicalAnalysisResult.split(" ");
        stack=new Stack<>();
        stack.push("$");
        stack.push("program");

        int pos=0;
        String top=stack.peek();
        while(!top.equals("$")){
            String pattern_now=pattern[pos];

            if(top.equals(pattern_now)){//如果top匹配pattern
                showMatchedString(pattern,pos);
                stack.pop();
                top=stack.peek();
                pos++;
                System.out.print("stack after match: ");
                showStack(stack);
                System.out.println();
            }else {//如果top不匹配pattern
                System.out.println("not match!");
                //System.out.println("top: "+top+" pos"+pos+"  pattern: "+pattern[pos]);
                String str=getMatchedToken(top,pattern[pos]);
                if(str.equals("error")){
                    System.out.println("error! top: "+top+" pos"+pos+"  pattern: "+pattern[pos]);
                    break;
                }
                System.out.println("get input str: " + str);

                stack.pop();
                pushString(stack, str);
                System.out.print("after push into stack: ");
                showStack(stack);

                top=stack.peek();
                System.out.println("now stack peek:"+top);
                System.out.println();
            }
        }
        System.out.println("Syntax Analysis Finished!");
    }
}
