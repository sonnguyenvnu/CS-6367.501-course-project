package edu.utd.methodcoverage;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class MethodCollector extends MethodVisitor implements Opcodes {

	String mName;

	public MethodCollector(final MethodVisitor mv, String name) {
		super(ASM5, mv);
		this.mName = name;
	}
	// method coverage collection
	@Override
	public void visitCode() {
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(mName);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		super.visitCode();
	}
}