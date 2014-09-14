package com.dafttech.eventmanager;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.NoSuchElementException;

import com.dafttech.hash.HashUtil;
import com.dafttech.reflect.ReflectionUtil;

class AnnotatedElementContainer {
	protected final AnnotatedElement target;
	protected final Class<?> targetClass;
	protected final Object targetInstance;
	protected final Class<?> type;
	protected final boolean isStatic;
	protected final Class<?> retType;
	protected final Class<?>[] argTypes;
	protected final Object[] nullArgs;

	protected AnnotatedElementContainer(AnnotatedElement target,
			Object targetInstance) {
		this.target = target;
		this.targetInstance = targetInstance;

		type = target.getClass();
		if (type == Field.class) {
			Field field = (Field) target;
			targetClass = field.getDeclaringClass();
			isStatic = Modifier.isStatic(field.getModifiers());
			retType = field.getType();
			argTypes = new Class<?>[0];
		} else if (type == Method.class) {
			Method method = (Method) target;
			targetClass = method.getDeclaringClass();
			isStatic = Modifier.isStatic(method.getModifiers());
			retType = method.getReturnType();
			argTypes = method.getParameterTypes();
		} else if (type == Constructor.class) {
			Constructor<?> constructor = (Constructor<?>) target;
			targetClass = constructor.getDeclaringClass();
			isStatic = true;
			retType = constructor.getDeclaringClass();
			argTypes = constructor.getParameterTypes();
		} else if (type == Class.class) {
			Class<?> clazz = (Class<?>) target;
			targetClass = clazz;
			isStatic = true;
			retType = null;
			argTypes = new Class<?>[0];
		} else {
			throw new NoSuchElementException();
		}

		if (!isStatic && targetInstance == null)
			throw new IllegalArgumentException(
					"Instance of non-static AccessObject cannot be null!");

		nullArgs = ReflectionUtil.buildArgumentArray(argTypes);
	}

	protected AnnotatedElementContainer(AnnotatedElement target) {
		this(target, null);
	}

	public boolean isField() {
		return type == Field.class;
	}

	public boolean isMethod() {
		return type == Method.class;
	}

	public boolean isConstructor() {
		return type == Constructor.class;
	}

	public boolean isClass() {
		return type == Class.class;
	}

	public AnnotatedElement getTarget() {
		return target;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Object getTargetInstance() {
		return targetInstance;
	}

	public Class<?> getType() {
		return type;
	}

	public boolean isStatic() {
		return isStatic;
	}

	@Override
	public int hashCode() {
		return HashUtil.hashCode(target, isStatic, targetClass, targetInstance);
	}

	@Override
	public boolean equals(Object obj) {
		return HashUtil.equals(this, obj);
	}
}
