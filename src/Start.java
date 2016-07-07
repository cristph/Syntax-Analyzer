/**
 * Created by cristph on 2015/11/18.
 */
public class Start {
    public static void main(String args[]){
        LexicalAnalysis la=new LexicalAnalysis();
        la.analyse("program.txt");
        SyntaxAnalysis sa=new SyntaxAnalysis();
        sa.analyse(la.getLexicalAnalysisResult());
    }
}
