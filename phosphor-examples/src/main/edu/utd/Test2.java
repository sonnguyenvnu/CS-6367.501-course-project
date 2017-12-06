package edu.utd;

import edu.columbia.cs.psl.phosphor.runtime.MultiTainter;
import edu.columbia.cs.psl.phosphor.runtime.Taint;

public class Test2 {
	public String test5(int x) {
		if (x == 0) {
			return null;
		}
		if (x == 1) {
			return test6(testTernaryWithObject(x + ""));
		}
		return test6("Hello" + x);
	}

	public String test6(String x) {
		String result = "Hello" + x;
		return result;
	}

	public String testTernaryWithObject(String x) {
		String result = null;
		return x == null ? "" : test6(result);
	}

	public String testObj1(int x) {
		int y = getCastingValue(x);
		return testSwitchCase(2);
	}

	public String testSwitchCase(int x) {
		int y = 1;
		switch (x) {
		case 1:
			y = getReturnPrimitiveConst(x);
			return test5(y);
		case 2:
			y = getCastingValue(x) + 2;
			return test5(y);
		default:
			break;
		}
		return test6(y + "");
	}

	public TestX testCustomizedObject() {
		TestX result = new TestX();
		result.setF1(testNomalCase(3, 4));
		return result;
	}

	public int testNomalCase(int input1, int input2) {
		int a = getCastingValue(input1);
		int b = getNormalValue(input2);
		getValue4(b);
		b = getReturnPrimitiveConst(input2);
		int c = a + b + getNormalValue(input2);
		return c;
	}

	public int testArrayUsage(int x) {
		int[] array = getArray(x);
		return array[0];
	}

	public int[] getArray(int x) {
		// TODO length of array
		int[] y = new int[getNormalValue(x)];
		for (int i = 0; i < x; i++) {
			y[i] = i;
		}
		y[x] = getReturnPrimitiveConst(x);
		y[x + 1] = getCastingValue(x);
		return y;
	}

	public int getCastingValue(int x) {
		float f = 3;
		return (int) (x + f);
	}

	public int getNormalValue(int x) {
		return getValue4(x) + 3;
	}

	public int getReturnPrimitiveConst(int x) {
		return 9;
	}

	public short getValue4(int x) {
		short y =(short) (x + getCastingValue(9));
		return y;
	}

	public void testThrow(String a) {
		try {
			int x = Integer.parseInt(a);
		} catch (Exception e) {
			throw new NumberFormatException();
		}
	}

	public int testTernaryWithPrimitive() {
		int[] nameSet = getArray(4);
		return nameSet == null ? null : nameSet[1];
	}

	public String testObjectConst(long instant, String displayZone, int i) {
		if (displayZone == null) {
			return ""; // no zone
		}
		String x = "";
		switch (i) {
		case 1:
			return test5(i);
		case 2:
			return testTernaryWithObject("" + i);
		}
		return "";
	}

	public void testxxx(int x, double[] y, long z) {

	}

	public void testxxx(int u, int[] t,double kx, TestX x, TestX k, long[] z) {

	}
	
	public String testNull(){
		String b = null;
		MultiTainter.taintedObject(b, new Taint<String>("SONNV"));
		System.out.println(MultiTainter.getTaint(b));
		return b;
	}

	public static void main(String[] args) {
		Test2 test = new Test2();
		int result = test.testNomalCase(3, 3); // pass
		assert(result==3);
	}

	public TestX castting(Object a) {
		if (a instanceof TestX) {
			return (TestX) a;
		}
		return new TestX();
	}

	public TestX castting() {
		return new TestX();
	}

	static class TestX {
		int f1;
		static final String f2 = new String("sonnv");
		String f3;

		public int getNonStaticField() {
			return f1;
		}

		public static String getStaticValue() {
			return f2;
		}

		public void setF1(int f1) {
			this.f1 = f1;
		}

		public String getCombineValue() {
			StringBuffer buffer = new StringBuffer();
			buffer.append(f3).append(f2);
			return buffer.toString();
		}
	}
}