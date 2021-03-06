package model;

import javax.lang.model.element.Modifier;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

public final class Generate {

	public static <T, U> MethodSpec.Builder generateMethodSolve(String nameMethod, T paramType, U returnType,
			boolean isAbstract) {

		MethodSpec.Builder solve = MethodSpec.methodBuilder(nameMethod);
		if (paramType instanceof Class<?>) {
			solve.addParameter((Class<?>)paramType, "param");
		} else {
			solve.addParameter((ClassName)paramType, "param");
		}
		
		if (returnType instanceof Class<?>) {
			solve.returns((Class<?>) returnType);
		} else {
			solve.returns((ClassName) returnType);
		}
		
		solve.addModifiers(Modifier.PUBLIC);
		if (isAbstract)
			solve.addModifiers(Modifier.ABSTRACT);
		return solve;

	}

	public final static MethodSpec.Builder generateConstructorAbstractCassExpertCor(String name) {
		MethodSpec.Builder constructor = MethodSpec.constructorBuilder();
		constructor.addParameter(ClassName.get("cor", name), "next");
		constructor.addStatement("this.$N = $N ", "nextExpert", "next");
		return constructor;
	}

	public final static <T, U> MethodSpec.Builder generateImplementMethodSovle(String nameMethod, T paramType,
			U returnType, String nameMethodhandler, String nameMethodSwitchExpert, boolean isImplement) {
		MethodSpec.Builder solve = Generate.generateMethodSolve(nameMethod, paramType, returnType, false);
		if (isImplement)
			solve.addAnnotation(Override.class);

		solve.addStatement("$T result = this.$N($N)", returnType, nameMethodhandler, "param");
		solve.beginControlFlow("if ( result != null )");
		solve.addStatement("return $N", "result");
		solve.nextControlFlow("else");
		solve.beginControlFlow("if ( this.$N != null )", "nextExpert");
		solve.addStatement("return this.$N.$N($N)", "nextExpert", nameMethodSwitchExpert, "param");
		solve.nextControlFlow("else");
		solve.addStatement("return null");
		solve.endControlFlow();
		solve.endControlFlow();
		return solve;
	}

}
