package com.dafttech.primitive;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class PrimitiveUtil {
    private static final List<Primitive<?>> PRIMITIVES = new ArrayList<Primitive<?>>();

    public static final Primitive<Byte> BYTE = new Primitive<Byte>("byte", Byte.class, 0) {
        @Override
        public long toLong(Byte value) {
            return value;
        }

        @Override
        public Byte fromLong(long value) {
            return (byte) value;
        }
    };
    public static final Primitive<Integer> INTEGER = new Primitive<Integer>("int", Integer.class, 0) {
        @Override
        public long toLong(Integer value) {
            return value;
        }

        @Override
        public Integer fromLong(long value) {
            return (int) value;
        }
    };
    public static final Primitive<Long> LONG = new Primitive<Long>("long", Long.class, 0) {
        @Override
        public long toLong(Long value) {
            return value;
        }

        @Override
        public Long fromLong(long value) {
            return value;
        }
    };
    public static final Primitive<Float> FLOAT = new Primitive<Float>("float", Float.class, 0) {
        @Override
        public long toLong(Float value) {
            return Float.floatToIntBits(value);
        }

        @Override
        public Float fromLong(long value) {
            return Float.intBitsToFloat((int) value);
        }
    };
    public static final Primitive<Double> DOUBLE = new Primitive<Double>("double", Double.class, 0) {
        @Override
        public long toLong(Double value) {
            return Double.doubleToLongBits(value);
        }

        @Override
        public Double fromLong(long value) {
            return Double.longBitsToDouble(value);
        }
    };
    public static final Primitive<Short> SHORT = new Primitive<Short>("short", Short.class, 0) {
        @Override
        public long toLong(Short value) {
            return value;
        }

        @Override
        public Short fromLong(long value) {
            return (short) value;
        }
    };
    public static final Primitive<Character> CHARACTER = new Primitive<Character>("char", Character.class, '\u0000') {
        @Override
        public long toLong(Character value) {
            return value;
        }

        @Override
        public Character fromLong(long value) {
            return (char) value;
        }
    };
    public static final Primitive<Boolean> BOOLEAN = new Primitive<Boolean>("boolean", Boolean.class, false) {
        @Override
        public long toLong(Boolean value) {
            return value ? 1 : 0;
        }

        @Override
        public Boolean fromLong(long value) {
            return value != 0;
        }
    };

    public static Primitive<?> get(Class<?> clazz) {
        for (Primitive<?> primitive : PRIMITIVES)
            if (primitive.equals(clazz)) return primitive;
        return null;
    }

    public abstract static class Primitive<ClassType> {
        public final String name;
        public final Class<ClassType> wrapperClass, primitiveClass;
        public final int size, bytes;
        public final Object nullObj;
        private final Field valueField;

        @SuppressWarnings("unchecked")
        private Primitive(String name, Class<ClassType> wrapperClass, Object nullObj) {
            this.name = name;
            this.wrapperClass = wrapperClass;
            Class<ClassType> primitiveClass = null;
            try {
                primitiveClass = (Class<ClassType>) wrapperClass.getDeclaredField("TYPE").get(null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            this.primitiveClass = primitiveClass;
            int size = 8;
            try {
                size = (Integer) wrapperClass.getDeclaredField("SIZE").get(null);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            this.size = size;
            bytes = size / 8;
            this.nullObj = nullObj;
            Field valueField = null;
            try {
                valueField = wrapperClass.getDeclaredField("value");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
            this.valueField = valueField;
            PRIMITIVES.add(this);
        }

        public abstract long toLong(ClassType value);

        public abstract ClassType fromLong(long value);

        public byte[] toByteArray(ClassType value) {
            long longVal = toLong(value);
            byte[] array = new byte[bytes];
            for (int i = 0; i < bytes; i++)
                array[i] = (byte) (longVal >> (bytes - 1 - i) * 8);
            return array;
        }

        public ClassType fromByteArray(byte... array) {
            if (array.length < bytes) return null;
            long longVal = 0;
            for (int i = 0; i < size; i++)
                longVal = longVal | (array[i] & 0xFF) << (bytes - 1 - i) * 8;
            return fromLong(longVal);
        }

        public void set(ClassType wrapper, ClassType value) {
            if (valueField == null) return;
            try {
                valueField.set(wrapper, value);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
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
