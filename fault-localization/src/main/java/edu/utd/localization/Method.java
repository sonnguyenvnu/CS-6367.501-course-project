package edu.utd.localization;

import java.util.ArrayList;
import java.util.List;

public class Method {
	protected String name;
	protected List<Method> dependencies;
	protected String type;

	@Override
	public boolean equals(Object obj) {
		return ((Method) obj).name.equals(this.name);
	}

	public Method(String name) {
		this.name = name;
		dependencies = new ArrayList<>();
	}

	@Override
	public String toString() {
		String result = name;
		if (!dependencies.isEmpty()){
			result += "-(";
			for (Method m : dependencies) {
				result += (m.name+", ");
			}
			result += ")";
		}
		return result;
	}
}
