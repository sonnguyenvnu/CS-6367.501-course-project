package edu.utd.localization;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DependencyFinder {
	static List<Method> voidMethods = new ArrayList<Method>();
	static List<Method> returnObjectMethods = new ArrayList<Method>();
	static List<Method> returnPrimitiveMethods = new ArrayList<Method>();
	static int level = 0;
	public static int MAX_LEVEL = 20;
	static List<Method> current = new ArrayList<Method>();

	public static void main(String[] args) {
		String test = "[null, null, Taint [lbl=org.joda.time.chrono.ZonedChronology$ZonedDurationField.getDifference  deps = []], Taint [lbl=org.joda.time.chrono.ZonedChronology$ZonedDurationField.getDifference  deps = []], Taint [lbl=org.joda.time.chrono.ZonedChronology$ZonedDurationField.getDifference  deps = []], Taint [lbl=org.joda.time.chrono.ZonedChronology$ZonedDurationField.getDifference  deps = []], Taint [lbl=org.joda.time.chrono.ZonedChronology$ZonedDurationField.getDifference  deps = []], Taint [lbl=org.joda.time.chrono.ZonedChronology$ZonedDurationField.getDifference  deps = []]] ";
		List<Method> methods = extractMethodFromTaints(test);
		for (Method method : methods) {
			System.out.println(method.name);
		}
	}

	public static List<Method> findDependencies(Method method, Set<Method> methods) {
		Set<Method> result = new HashSet<Method>();
		List<Method> ms = findMethodByName(method, methods);
		if (!ms.isEmpty()) {
			result.addAll(ms);
			for (Method m : ms) {
				for (Method mt : m.dependencies) {
					List<Method> d = findMethodByName(mt, methods);
					if (!d.isEmpty()) {
						result.addAll(d);
						for (Method m2 : d) {
							List<Method> dp2 = findDependencies(m2, methods);
							result.addAll(dp2);
						}
					}
				}
			}
		}
		List<Method> r = new ArrayList<>();
		r.addAll(result);
		return r;
	}

	/**
	 * Because same name methods might have different set of dependencies
	 * 
	 * @param mt
	 * @param methods
	 * @return
	 */
	public static List<Method> findMethodByName(Method mt, Set<Method> methods) {
		List<Method> result = new ArrayList<Method>();
		for (Method m : methods) {
			if (m.name.equals(mt.name) && !result.contains(m))
				result.add(m);
		}
		return result;
	}

	public static List<Method> extractMethods(String content) {
		String method;
		String taint = "";
		Method m;
		String[] lines = content.split("\n");
		List<Method> result = new ArrayList<Method>();
		for (String line : lines) {
			if (line.contains("Void: ")) {
				method = line.replace("Void: ", "");
				m = new Method(method);
				m.type = "void";
				voidMethods.add(m);
				result.add(m);
			} else if (line.contains("Return object: ")) {
				method = line.replace("Return object: ", "");
				m = new Method(method);
				m.type = "object";
				List<Method> methods = extractMethodFromTaints(taint);
				m.dependencies = methods;
				taint = "";
				returnObjectMethods.add(m);
				result.add(m);
			} else if (line.contains("Return: ")) {
				method = line.replace("Return: ", "");
				m = new Method(method);
				m.type = "primitive";
				List<Method> methods = extractMethodFromTaints(taint);
				m.dependencies = methods;
				taint = "";
				returnPrimitiveMethods.add(m);
				result.add(m);
			} else {
				taint = line;
			}
		}
		// result.addAll(returnObjectMethods);
		// result.addAll(returnPrimitiveMethods);
		// result.addAll(voidMethods);
		return result;
	}

	public static List<Method> extractMethods(String[] contentSplited) {
		String method;
		String taint = "";
		Method m;
		String[] lines = contentSplited;
		List<Method> result = new ArrayList<Method>();
		for (String line : lines) {
			if (line.contains("Void: ")) {
				method = line.replace("Void: ", "");
				m = new Method(method);
				m.type = "void";
				voidMethods.add(m);
				result.add(m);
			} else if (line.contains("Return object: ")) {
				method = line.replace("Return object: ", "");
				m = new Method(method);
				m.type = "object";
				List<Method> methods = extractMethodFromTaints(taint);
				m.dependencies = methods;
				taint = "";
				returnObjectMethods.add(m);
				result.add(m);
			} else if (line.contains("Return: ")) {
				method = line.replace("Return: ", "");
				m = new Method(method);
				m.type = "primitive";
				List<Method> methods = extractMethodFromTaints(taint);
				m.dependencies = methods;
				taint = "";
				returnPrimitiveMethods.add(m);
				result.add(m);
			} else {
				taint = line;
			}
		}
		// result.addAll(returnObjectMethods);
		// result.addAll(returnPrimitiveMethods);
		// result.addAll(voidMethods);
		return result;
	}

	private static List<Method> extractMethodFromTaints(String taint) {
		List<Method> d = new ArrayList<Method>();
		if (!taint.isEmpty()) {
			taint = taint.replace("Taint ", "").replace("lbl=", "").replace("  deps = ", " ").replace("null", " ")
					.replace("[", "").replace("]", "").replace(",", "").trim();
			if (!taint.isEmpty()) {
				String[] ms = taint.split(" ");
				for (String m : ms) {
					if (!m.trim().isEmpty()) {
						Method mo = new Method(m);
						if (!d.contains(mo))
							d.add(mo);
					}
				}
			}
		}
		return d;
	}

	public static List<Method> findSuspiciousSet(List<Method> ms) {
		List<Method> t = new ArrayList<Method>();
		for (Method method : ms) {
			if (method.type.equals("void") && !t.contains(method)) {
				t.add(method);
			}
		}
		level = 0;
		Method failureMethod = ms.get(ms.size() - 1);
		current = new ArrayList<Method>();
		List<Method> dependencies = getDependencies(failureMethod, ms, ms.size() - 1);
		for (Method method : dependencies) {
			if (!t.contains(method)) {
				t.add(method);
			}
		}
		return t;
	}

	private static List<Method> getDependencies(Method failureMethod, List<Method> ms, int i) {
		List<Method> result = new ArrayList<Method>();
		List<Method> dp = failureMethod.dependencies;
		result.add(failureMethod);
		current.add(failureMethod);
		if (level++ == MAX_LEVEL) {
			result.addAll(dp);
			return result;
		}
		for (Method method : dp) {
			if (!current.contains(method)) {
				method = findMethodByName(method, ms, i);
				List<Method> tem = getDependencies(method, ms, i);
				result.addAll(tem);
			}
		}
		return result;
	}

	private static Method findMethodByName(Method m, List<Method> ms, int i) {
		if (i >= ms.size() || i < 0)
			return null;
		for (int j = i; j >= 0; j--) {
			if (ms.get(j).equals(m))
				return ms.get(j);
		}
		return null;
	}

	public static List<Method> findSuspiciousSet1(List<Method> ms) {
		List<Method> t = new ArrayList<Method>();
		for (Method method : ms) {
			if (!t.contains(method)) {
				t.add(method);
			}
		}
		return t;
	}
}
