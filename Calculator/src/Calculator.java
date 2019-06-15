
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	
	static String num="\\d+\\.?(\\d+)?";
	static String expr="5+(10+2*12+4)+6";
	
	public static String openBrackets(String expr){
		boolean insideBrackets=false;
		int bracketsCounter=0, currentPos=0, openingBracketPos=-1, closingBracketPos=-1;
		while(currentPos<expr.length()){
			
			char currentChar=expr.charAt(currentPos);
			if(currentChar=='('){
				insideBrackets=true;
				bracketsCounter++;
				if(openingBracketPos==-1)
					openingBracketPos=currentPos;
				currentPos++;
				
			}else if(currentChar==')'){
				closingBracketPos=currentPos;
				bracketsCounter--;
				if(bracketsCounter==0){
					String resultFromBracket=openBrackets(expr.substring(openingBracketPos+1, closingBracketPos));
					expr=expr.substring(0,openingBracketPos)+resultFromBracket+expr.substring(closingBracketPos+1,expr.length());
					currentPos=openingBracketPos+resultFromBracket.length();
					bracketsCounter=0;
					openingBracketPos=-1;
					closingBracketPos=-1;
					insideBrackets=false;
				}
			}else{
				currentPos++;
			}
		}
		
		String exp=reduce(expr, "[\\*\\/]");
		exp=reduce(exp, "[\\+\\-]");
		return exp;
	}
	
	public static String reduce(String expr, String operator){
		int start=0;
		StringBuilder sb=new StringBuilder(expr);
		Pattern oprndsP=Pattern.compile(num+operator+num);
		Matcher oprndsM=oprndsP.matcher(sb);
		while(oprndsM.find(start)){
			String subexpr=oprndsM.group();
			int leftBorder=oprndsM.start();
			int rightBorder=oprndsM.end();
			
			Double result=evaluate(subexpr, operator);
			String res=result.toString();
			
			sb.replace(leftBorder, rightBorder, res);
			start=leftBorder;
		}
		return sb.toString();
	}
	
	public static double evaluate(String expr, String operator){
		Pattern numPattern=Pattern.compile(num);
		Matcher numMatcher=numPattern.matcher(expr);
		numMatcher.find();
		String firstNumStr=numMatcher.group();
		double firstNum=Double.valueOf(firstNumStr);
		
		Pattern operPattern=Pattern.compile(operator);
		Matcher operMatcher=operPattern.matcher(expr);
		operMatcher.find();
		String oper=operMatcher.group();
		
		numMatcher.find(operMatcher.end());
		String secondNumStr=numMatcher.group();
		double secondNum=Double.valueOf(secondNumStr);
		
		double result=0;
		if(oper.equals("*")){
			 result=firstNum*secondNum;
		}
		else if(oper.equals("/")){
			result=firstNum/secondNum;
		}
		else if(oper.equals("+")){
			result=firstNum+secondNum;
		}
		else if(oper.equals("-")){
			result=firstNum-secondNum;
		}
		
		return result;
		
	}
	
	public static void main(String[] args){
		System.out.println("Calculate this expression: "+expr);
		String result=openBrackets(expr);
		System.out.println("The result of the expression is: "+result);
		
	}

}
