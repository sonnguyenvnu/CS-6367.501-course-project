package edu.utd.methodcoverage2;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
/**
 * ClassCollector (is a ClassVisitor) collect all class in a project
 * @author sonnguyen
 *
 */
public class ClassCollector extends ClassVisitor {
	/**
	 * Class name
	 */
	String className;
    public ClassCollector(ClassWriter writer) {
        super(Opcodes.ASM5, writer);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    	className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		return mv == null ? null : new MethodCollector(mv, className.replace("/", ".")+"."+name);
    }

}