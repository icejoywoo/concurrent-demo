package concurrency.atomic;

import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author  wujiabin
 * @date 14-2-12
 */
public class AtomicIntegerTest {
    @Test
    public void testBasic() {
        final AtomicInteger value = new AtomicInteger(10);
        assertThat(value.compareAndSet(1, 2), is(false));
        assertThat(value.get(), is(10));
        assertThat(value.compareAndSet(10, 3), is(true));
        value.set(0);

        assertThat(value.incrementAndGet(), is(1));
        assertThat(value.getAndAdd(2), is(1));
        assertThat(value.getAndSet(5), is(3));
        assertThat(value.get(), is(5));
    }

    @Test
    public void testMultiThread() throws InterruptedException {
        final AtomicInteger value = new AtomicInteger(0);
        final int threadSize = 10;
        final int iteration = 1000;
        Thread[] threads = new Thread[threadSize];
        for (int i = 0; i < threadSize; i++) {
            threads[i] = new Thread() {
                @Override
                public void run() {
                    for (int j = 0; j < iteration; j++)
                        value.incrementAndGet();
                }
            };
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
//        threads[threadSize-1].join();
        assertThat(value.get(), is(threadSize * iteration));
    }
}
