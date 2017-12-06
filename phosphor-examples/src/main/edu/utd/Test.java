package edu.utd;

public class Test {
	public int testMe(int input1, int input2){
		int a = getValue1(input1);
		int b = getValue2(input2);  
		getValue4(b);				
		b = getValue3(input2);
		return a+b;
	}
	public int getValue1(int x){ return x + 3; }
	
	public int getValue2(int x){ return x*10; }
	
	public int getValue3(int x){ return x-5; }
	
	public int getValue4(int x){ 
		int y = getValue3(x);
		return y%10; 
	}
	
	public static void main(String[] args){
		Test test = new Test();
		int result = test.testMe(1, 2);
		assert(result==100);
	}
}