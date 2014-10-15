package com.dafttech.test.bytearray;

import com.dafttech.primitive.PrimitiveUtil;

import java.nio.ByteBuffer;

/**
 * Created by LolHens on 15.10.2014.
 */
public class ByteArray {
    public static void main(String[] args) {
        byte[] byteArray1 = new byte[4];
        byte[] byteArray2 = null;

        long time, all;

        all = 0;
        for (int i = 0; i < 100; i++) {
            time = System.nanoTime();
            byteArray2 = PrimitiveUtil.FLOAT.toByteArray(4f);
            all += System.nanoTime() - time;
        }
        System.out.println(all / 100);

        all = 0;
        for (int i = 0; i < 100; i++) {
            time = System.nanoTime();
            ByteBuffer buffer = ByteBuffer.wrap(byteArray1);
            buffer.putFloat(4f);
            all += System.nanoTime() - time;
        }
        System.out.println(all / 100);

        for (byte b : byteArray1) System.out.print(b + " ");
        System.out.println();
        for (byte b : byteArray2) System.out.print(b + " ");
    }
}
