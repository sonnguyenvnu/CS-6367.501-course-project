package edu.utd.methodcoverage2;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * MethodCollector (is a MethodVisitor) collect all methods' information in a
 * project
 * 
 * @author sonnguyen
 *
 */
class MethodCollector extends MethodVisitor implements Opcodes {
	String mName;
	int tempVar = -1;
	public MethodCollector(final MethodVisitor mv, String name) {
		super(ASM5, mv);
		this.mName = name;
	}
	@Override
	public void visitCode() {
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(mName);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		super.visitCode();
	}
	@Override
	public void visitInsn(int opcode) {
		if (opcode == Opcodes.IRETURN) {
			mv.visitLdcInsn(mName);
			mv.visitMethodInsn(INVOKESTATIC, "edu/columbia/cs/psl/phosphor/runtime/MultiTainter", "taintedInt", "(ILjava/lang/Object;)I", false);
			mv.visitVarInsn(ISTORE, tempVar);
			mv.visitVarInsn(ILOAD, tempVar);
		}
		super.visitInsn(opcode);
	}
	@Override
	public void visitVarInsn(int opcode, int var) {
		if(opcode == Opcodes.ILOAD)
			tempVar = var;
		super.visitVarInsn(opcode, var);
	}
}