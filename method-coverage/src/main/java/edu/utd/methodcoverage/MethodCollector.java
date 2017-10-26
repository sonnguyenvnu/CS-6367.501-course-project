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
		mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
		mv.visitLdcInsn(mName);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		
		/*
		 * Write method name to file, but I dont know why it dose not work in the large number of methods
		 * So I have just print methods' name to console and cat them to file, then do post process
		/*
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
		Label l3 = new Label();
		Label l4 = new Label();
		Label l5 = new Label();
		mv.visitTryCatchBlock(l3, l4, l5, "java/lang/Throwable");
		Label l6 = new Label();
		mv.visitTryCatchBlock(l3, l4, l6, null);
		Label l7 = new Label();
		Label l8 = new Label();
		Label l9 = new Label();
		mv.visitTryCatchBlock(l7, l8, l9, "java/lang/Throwable");
		Label l10 = new Label();
		mv.visitTryCatchBlock(l5, l10, l6, null);
		Label l11 = new Label();
		Label l12 = new Label();
		Label l13 = new Label();
		mv.visitTryCatchBlock(l11, l12, l13, "java/io/IOException");
		mv.visitLabel(l11);
		mv.visitTypeInsn(NEW, "java/io/BufferedWriter");
		mv.visitInsn(DUP);
		mv.visitTypeInsn(NEW, "java/io/FileWriter");
		mv.visitInsn(DUP);
		mv.visitLdcInsn("/Users/sonnguyen/Desktop/test.txt");
		mv.visitInsn(ICONST_1);
		mv.visitMethodInsn(INVOKESPECIAL, "java/io/FileWriter", "<init>", "(Ljava/lang/String;Z)V", false);
		mv.visitMethodInsn(INVOKESPECIAL, "java/io/BufferedWriter", "<init>", "(Ljava/io/Writer;)V", false);
		mv.visitVarInsn(ASTORE, 1);
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitLabel(l3);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitLdcInsn(mName+"\n");
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "write", "(Ljava/lang/String;)V", false);
		mv.visitLabel(l4);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitJumpInsn(IFNULL, l12);
		mv.visitVarInsn(ALOAD, 2);
		Label l14 = new Label();
		mv.visitJumpInsn(IFNULL, l14);
		mv.visitLabel(l0);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "close", "()V", false);
		mv.visitLabel(l1);
		mv.visitJumpInsn(GOTO, l12);
		mv.visitLabel(l2);
		mv.visitFrame(Opcodes.F_FULL, 3, new Object[] {"[Ljava/lang/String;", "java/io/BufferedWriter", "java/lang/Throwable"}, 1, new Object[] {"java/lang/Throwable"});
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "addSuppressed", "(Ljava/lang/Throwable;)V", false);
		mv.visitJumpInsn(GOTO, l12);
		mv.visitLabel(l14);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "close", "()V", false);
		mv.visitJumpInsn(GOTO, l12);
		mv.visitLabel(l5);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitVarInsn(ASTORE, 2);
		mv.visitVarInsn(ALOAD, 3);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l6);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/lang/Throwable"});
		mv.visitVarInsn(ASTORE, 4);
		mv.visitLabel(l10);
		mv.visitVarInsn(ALOAD, 1);
		Label l15 = new Label();
		mv.visitJumpInsn(IFNULL, l15);
		mv.visitVarInsn(ALOAD, 2);
		Label l16 = new Label();
		mv.visitJumpInsn(IFNULL, l16);
		mv.visitLabel(l7);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "close", "()V", false);
		mv.visitLabel(l8);
		mv.visitJumpInsn(GOTO, l15);
		mv.visitLabel(l9);
		mv.visitFrame(Opcodes.F_FULL, 5, new Object[] {"[Ljava/lang/String;", "java/io/BufferedWriter", "java/lang/Throwable", Opcodes.TOP, "java/lang/Throwable"}, 1, new Object[] {"java/lang/Throwable"});
		mv.visitVarInsn(ASTORE, 5);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "addSuppressed", "(Ljava/lang/Throwable;)V", false);
		mv.visitJumpInsn(GOTO, l15);
		mv.visitLabel(l16);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/BufferedWriter", "close", "()V", false);
		mv.visitLabel(l15);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitInsn(ATHROW);
		mv.visitLabel(l12);
		mv.visitFrame(Opcodes.F_FULL, 1, new Object[] {"[Ljava/lang/String;"}, 0, new Object[] {});
		Label l17 = new Label();
		mv.visitJumpInsn(GOTO, l17);
		mv.visitLabel(l13);
		mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {"java/io/IOException"});
		mv.visitVarInsn(ASTORE, 1);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/IOException", "printStackTrace", "()V", false);
		mv.visitLabel(l17);
		mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
		*/
		super.visitCode();
	}
}