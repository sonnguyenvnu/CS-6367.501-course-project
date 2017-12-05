package edu.utd.methodcoverage;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
/**
 * MethodCollector (is a MethodVisitor) collect all methods' information in a project
 * @author sonnguyen
 *
 */
class MethodCollector extends MethodVisitor implements Opcodes {
	String mName;

	public MethodCollector(final MethodVisitor mv, String name) {
		super(ASM5, mv);
		this.mName = name;
	}
	/**
	 * Collect method information by printing method name when it runs
	 */
	@Override
	public void visitCode() {
		if (mName.contains("junit.framework.AssertionFailedError")) {
			print("===");
		}
		print(mName);
		/*
		 * Write method name to file, but I dont know why it dose not work in the large number of methods
		 * So I have just print methods' name to console and cat them to file, then do post process
		*/
		
		super.visitCode();
	}
	private void print(String content) {
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(content);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
	}

}