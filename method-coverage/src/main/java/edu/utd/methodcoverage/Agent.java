package edu.utd.methodcoverage;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

/**
 * Agent instruments ASM ClassCollector
 * 
 * @author sonnguyen
 *
 */
public class Agent {

	public static void premain(String agentArgs, Instrumentation inst) {
		inst.addTransformer(new ClassFileTransformer() {
			@Override
			public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass,
					ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
				if (!isNotSystemRunning(s)) {
					System.out.println(s);
					try {
						ClassReader cr = new ClassReader(bytes);
						System.out.println(cr == null);
						ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
						// Visit all classes and "instrument"
						ClassCollector ca = new ClassCollector(cw);
						cr.accept(ca, 0);
						return cw.toByteArray();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				return null;
			}

			/**
			 * filter some system files and tool file (eg. maven, junit)
			 * 
			 * @param s
			 *            class file path
			 * @return
			 */
			private boolean isNotSystemRunning(String s) {
				if (s.contains("java/") 
						|| s.contains("sun/") 
						|| s.contains("maven/") 
//						|| s.contains("junit/")
						|| s.contains("phosphor/"))
					return true;
				return false;
			}
		}, true);
	}

}
