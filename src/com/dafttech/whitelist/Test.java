package com.dafttech.whitelist;

public class Test implements IWhitelistable<Test> {
    public static void test(IWhitelistable<Test> test) {
        
    }
    
    public static void test2() {
        test(new Test());
        test(new Whitelist<Test>());
    }
}
