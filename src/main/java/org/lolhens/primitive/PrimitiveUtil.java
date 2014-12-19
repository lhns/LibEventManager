package org.lolhens.primitive;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class PrimitiveUtil {
    private static final List<Primitive<?>> PRIMITIVES = new ArrayList<>();
    private static volatile Primitive<?>[] PRIMITIVE_ARRAY;

    public static final Primitive<Byte> BYTE = new Primitive<>(byte.class, Byte.class, "byte", 1, 0, (byteBuffer, value) -> byteBuffer.put(value), (byteBuffer) -> byteBuffer.get());
    public static final Primitive<Integer> INTEGER = new Primitive<>(int.class, Integer.class, "int", 4, 0, (byteBuffer, value) -> byteBuffer.putInt(value), (byteBuffer) -> byteBuffer.getInt());
    public static final Primitive<Long> LONG = new Primitive<>(long.class, Long.class, "long", 8, 0, (byteBuffer, value) -> byteBuffer.putLong(value), (byteBuffer) -> byteBuffer.getLong());
    public static final Primitive<Float> FLOAT = new Primitive<>(float.class, Float.class, "float", 4, 0, (byteBuffer, value) -> byteBuffer.putFloat(value), (byteBuffer) -> byteBuffer.getFloat());
    public static final Primitive<Double> DOUBLE = new Primitive<>(double.class, Double.class, "double", 8, 0, (byteBuffer, value) -> byteBuffer.putDouble(value), (byteBuffer) -> byteBuffer.getDouble());
    public static final Primitive<Short> SHORT = new Primitive<>(short.class, Short.class, "short", 2, 0, (byteBuffer, value) -> byteBuffer.putShort(value), (byteBuffer) -> byteBuffer.getShort());
    public static final Primitive<Character> CHARACTER = new Primitive<>(char.class, Character.class, "char", 2, '\u0000', (byteBuffer, value) -> byteBuffer.putChar(value), (byteBuffer) -> byteBuffer.getChar());
    public static final Primitive<Boolean> BOOLEAN = new Primitive<>(boolean.class, Boolean.class, "boolean", 1, false, (byteBuffer, value) -> byteBuffer.put((byte) (value ? 1 : 0)), (byteBuffer) -> byteBuffer.get() != 0);

    public static Primitive<?> get(Class<?> clazz) {
        for (int i = 0; i < PRIMITIVE_ARRAY.length; i++)
            if (PRIMITIVE_ARRAY[i].equals(clazz)) return PRIMITIVE_ARRAY[i];
        return null;
    }

    public static class Primitive<ClassType> {
        public final String name;
        public final Class<ClassType> primitiveClass, wrapperClass;
        public final int size;
        public final Object nullObj;

        private final BiConsumer<ByteBuffer, ClassType> writeToByteBuffer;
        private final Function<ByteBuffer, ClassType> readFromByteBuffer;

        @SuppressWarnings("unchecked")
        private Primitive(Class<ClassType> primitiveClass, Class<ClassType> wrapperClass, String name, int size, Object nullObj, BiConsumer<ByteBuffer, ClassType> writeToByteBuffer, Function<ByteBuffer, ClassType> readFromByteBuffer) {
            this.primitiveClass = primitiveClass;
            this.wrapperClass = wrapperClass;
            this.name = name;
            this.size = size;
            this.nullObj = nullObj;
            this.writeToByteBuffer = writeToByteBuffer;
            this.readFromByteBuffer = readFromByteBuffer;

            PRIMITIVES.add(this);
            PRIMITIVE_ARRAY = PRIMITIVES.toArray(new Primitive[0]);
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
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes).order(order);
            return readFromByteBuffer.apply(byteBuffer);
        }

        @Override
        public String toString() {
            return name;
        }

        @Override
        public boolean equals(Object obj) {
            return obj == this || obj == wrapperClass || obj == primitiveClass;
        }
    }
}
