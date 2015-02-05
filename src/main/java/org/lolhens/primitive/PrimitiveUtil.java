package org.lolhens.primitive;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PrimitiveUtil {
    private static final List<Primitive<?>> PRIMITIVES = new ArrayList<>();

    public static final Primitive<Byte> BYTE = new Primitive<>(byte.class, Byte.class, "byte", 1, 0, ByteBuffer::put, ByteBuffer::get);
    public static final Primitive<Integer> INTEGER = new Primitive<>(int.class, Integer.class, "int", 4, 0, ByteBuffer::putInt, ByteBuffer::getInt);
    public static final Primitive<Long> LONG = new Primitive<>(long.class, Long.class, "long", 8, 0, ByteBuffer::putLong, ByteBuffer::getLong);
    public static final Primitive<Float> FLOAT = new Primitive<>(float.class, Float.class, "float", 4, 0, ByteBuffer::putFloat, ByteBuffer::getFloat);
    public static final Primitive<Double> DOUBLE = new Primitive<>(double.class, Double.class, "double", 8, 0, ByteBuffer::putDouble, ByteBuffer::getDouble);
    public static final Primitive<Short> SHORT = new Primitive<>(short.class, Short.class, "short", 2, 0, ByteBuffer::putShort, ByteBuffer::getShort);
    public static final Primitive<Character> CHARACTER = new Primitive<>(char.class, Character.class, "char", 2, '\u0000', ByteBuffer::putChar, ByteBuffer::getChar);
    public static final Primitive<Boolean> BOOLEAN = new Primitive<>(boolean.class, Boolean.class, "boolean", 1, false, (byteBuffer, value) -> byteBuffer.put((byte) (value ? 1 : 0)), (byteBuffer) -> byteBuffer.get() != 0);

    public static Primitive<?> get(Class<?> clazz) {
        for (Primitive<?> primitive : PRIMITIVES)
            if (primitive.isSameType(clazz)) return primitive;
        return null;
    }

    public static class Primitive<ClassType> {
        public final String name;
        public final Class<ClassType> primitiveClass, wrapperClass;
        public final int size;
        public final Object nullObj;

        private final BiConsumer<ByteBuffer, ClassType> writeToByteBuffer;
        private final Function<ByteBuffer, ClassType> readFromByteBuffer;

        private Primitive(Class<ClassType> primitiveClass, Class<ClassType> wrapperClass, String name, int size, Object nullObj, BiConsumer<ByteBuffer, ClassType> writeToByteBuffer, Function<ByteBuffer, ClassType> readFromByteBuffer) {
            this.primitiveClass = primitiveClass;
            this.wrapperClass = wrapperClass;
            this.name = name;
            this.size = size;
            this.nullObj = nullObj;
            this.writeToByteBuffer = writeToByteBuffer;
            this.readFromByteBuffer = readFromByteBuffer;

            PRIMITIVES.add(this);
        }

        public void writeToByteBuffer(ByteBuffer buffer, ClassType value) {
            writeToByteBuffer.accept(buffer, value);
        }

        public ClassType readFromByteBuffer(ByteBuffer buffer) {
            return readFromByteBuffer.apply(buffer);
        }

        public byte[] toByteArray(ClassType value, ByteOrder order) {
            byte[] bytes = new byte[size];

            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(order);
            writeToByteBuffer.accept(byteBuffer, value);

            return bytes;
        }

        public ClassType fromByteArray(byte[] bytes, int offset, ByteOrder order) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(order);
            byteBuffer.position(offset);
            return readFromByteBuffer.apply(byteBuffer);
        }

        public ClassType fromByteArray(byte[] bytes, ByteOrder order) {
            return fromByteArray(bytes, 0, order);
        }

        @Override
        public String toString() {
            return name;
        }

        public boolean isSameType(Object obj) {
            return obj == this || obj == wrapperClass || obj == primitiveClass;
        }
    }
}
