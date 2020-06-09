package com.github.hcsp.multithread;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.opentest4j.TestAbortedException;

public class ProducerConsumerTest {
    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private final PrintStream ps = new PrintStream(os);
    private PrintStream systemOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(ps);
    }

    @AfterEach
    void cleanUp() {
        System.setOut(systemOut);
    }

    @TestFactory
    public Iterable<DynamicTest> test() throws Exception {
        return IntStream.range(1, 10)
                .mapToObj(this::loadClass)
                .filter(Objects::nonNull)
                .map(this::createOneTest)
                .collect(Collectors.toList());
    }

    private Class<?> loadClass(int i) {
        try {
            return Class.forName("com.github.hcsp.multithread.ProducerConsumer" + i);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private DynamicTest createOneTest(Class<?> testClass) {
        return DynamicTest.dynamicTest(testClass.getSimpleName(), () -> testOne(testClass));
    }

    private void testOne(Class<?> testClass) {
        try {
            Method main = testClass.getMethod("main", String[].class);
            main.invoke(null, new Object[] {null});
            systemOut.println("Output of " + testClass.getName() + ": " + os.toString());
            Assertions.assertTrue(
                    os.toString()
                            .matches("(?s).*(Producing -?\\d+\\s+Consuming -?\\d+\\s+){10}.*"));
        } catch (NoSuchMethodException e) {
            throw new TestAbortedException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
