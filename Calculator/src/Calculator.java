
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	
	static String num="\\d+\\.?(\\d+)?";
	static String fullExpr=new String("((10-15)*5)*(6+4)");
	
	public static String openBrackets(String localExpr){
		boolean insideBrackets=false;
		int bracketsCounter=0, currentPos=0, openingBracketPos=-1, closingBracketPos=-1;
		while(currentPos<localExpr.length()){
			
			char currentChar=localExpr.charAt(currentPos);
			if(currentChar=='('){
				insideBrackets=true;
				bracketsCounter++;
				if(openingBracketPos==-1)
					openingBracketPos=currentPos;
				currentPos++;
				
			}else if(currentChar==')'){
				closingBracketPos=currentPos;
				bracketsCounter--;
				currentPos++;
				if(bracketsCounter==0){
					String exprInBrackets=localExpr.substring(openingBracketPos+1, closingBracketPos);
					String resultFromBracket=openBrackets(exprInBrackets);
					String beforeBrackets=localExpr.substring(0, openingBracketPos);
					String afterBrackets=localExpr.substring(closingBracketPos+1, localExpr.length());
					String beforeBracketsInversedSign;
					
					if(resultFromBracket.startsWith("-") & beforeBrackets.length()>0){
						
						beforeBracketsInversedSign=inverseSign(beforeBrackets, resultFromBracket, afterBrackets);
						localExpr=beforeBracketsInversedSign+resultFromBracket.substring(1,resultFromBracket.length())+afterBrackets;
					}else if(resultFromBracket.startsWith("-") & beforeBrackets.length()==0){
						localExpr="-"+resultFromBracket.substring(1,resultFromBracket.length())+afterBrackets;
					}else{
						localExpr=beforeBrackets+resultFromBracket+afterBrackets;
					}
					currentPos=openingBracketPos;
					bracketsCounter=0;
					openingBracketPos=-1;
					closingBracketPos=-1;
					insideBrackets=false;
				}
			}else{
				currentPos++;
			}
		}
		
		String beforeCalculating=reduce(localExpr, "[\\*\\/]");
		beforeCalculating=reduce(beforeCalculating, "[\\+\\-]");
		return beforeCalculating;
	}
	
	public static String inverseSign(String beforeBrackets, String inBrackets, String afterBrackets){
		int pos=searchForNearestSign(beforeBrackets);
		
		char nearestSign=beforeBrackets.charAt(pos);
		String beforeBracketsInversedSign;
		if(nearestSign=='+'){
			beforeBracketsInversedSign=beforeBrackets.substring(0,pos)+"-"+beforeBrackets.substring(pos+1,beforeBrackets.length());
		}else if(nearestSign=='-'){
			beforeBracketsInversedSign=beforeBrackets.substring(0,pos)+"+"+beforeBrackets.substring(pos+1,beforeBrackets.length());
		}else if(nearestSign=='('){
			beforeBracketsInversedSign=beforeBrackets.substring(0,pos+1)+"-"+beforeBrackets.substring(pos+1,beforeBrackets.length());
		}else{
			beforeBracketsInversedSign="-"+beforeBrackets;
		}
		return beforeBracketsInversedSign;
	}
	
	public static int searchForNearestSign(String expr){
		int signPos=-1, currentPos=expr.length()-1;
		char currentChar;
		while(currentPos>=0 & signPos==-1){
			currentChar=expr.charAt(currentPos);
			if(currentChar=='+' | currentChar=='-' | currentChar=='('){
				signPos=currentPos;
			}
			currentPos--;
		}
		if(signPos==-1)
			return 0;
		else
			return signPos;
		
	}
	
	public static String reduce(String expr, String operator){
		int start=0;
		StringBuilder sb=new StringBuilder(expr);
		Pattern oprndsP=Pattern.compile("\\-?"+num+operator+num);
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
		Pattern numPattern=Pattern.compile("\\-?"+num);
		Matcher numMatcher=numPattern.matcher(expr);
		numMatcher.find();
		String firstNumStr=numMatcher.group();
		double firstNum=Double.valueOf(firstNumStr);
		
		Pattern operPattern=Pattern.compile(operator);
		Matcher operMatcher=operPattern.matcher(expr);
		operMatcher.find(numMatcher.end());
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
		System.out.println("Calculate this expression: "+fullExpr);
		String result=openBrackets(fullExpr.toString());
		System.out.println("The result of the expression is: "+result);
		
	}

}
