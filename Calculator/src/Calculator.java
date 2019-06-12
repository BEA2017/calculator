
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {
	
	static String num="\\d+\\.?(\\d+)?";
	static StringBuilder expr=new StringBuilder("2*2/6+10+2*3/6");
	
	public static void reduce(StringBuilder expr, String operator){
		int start=0;
		Pattern oprndsP=Pattern.compile(num+operator+num);
		Matcher oprndsM=oprndsP.matcher(expr);
		while(oprndsM.find(start)){
			String subexpr=oprndsM.group();
			int leftBorder=oprndsM.start();
			int rightBorder=oprndsM.end();
			
			Double result=evaluate(subexpr, operator);
			String res=result.toString();
			
			expr.replace(leftBorder, rightBorder, res);
			start=leftBorder;
			
		}
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
		reduce(expr, "[\\*\\/]");
		reduce(expr, "[\\+\\-]");
		System.out.println("The result of the expression is: "+expr);
		
	}

}
