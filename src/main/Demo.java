package src.main;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Demo {
    public static class IODemo{
        public static void pathDemo(){
            var f = new File(".\\temp.txt");
            System.out.println(f.getPath());
            System.out.println(f.getAbsolutePath());
            try{
                System.out.println(f.getCanonicalPath());
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
            System.out.println(f.isFile());
            System.out.println(f.isDirectory());
            try{
                if(f.createNewFile()){
                    System.out.print("create");
                    var t = new Scanner(System.in);
                    String input = t.nextLine();
                    if(f.delete()) {
                        System.out.print("delete");
                        t.nextLine();
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
            try{
                File t = File.createTempFile("temp","txt");
            }catch (IOException e){
                e.printStackTrace();
                return;
            }
            File l = new File("..");
            File[] fl = l.listFiles();
            System.out.println(Arrays.toString(fl));

            Path p1 = Paths.get(".", "project", "study"); // 构造一个Path对象
            System.out.println(p1);
            Path p2 = p1.toAbsolutePath(); // 转换为绝对路径
            System.out.println(p2);
            Path p3 = p2.normalize(); // 转换为规范路径
            System.out.println(p3);
            File nf = p3.toFile(); // 转换为File对象
            System.out.println(f);
            for (Path p : Paths.get("..").toAbsolutePath()) { // 可以直接遍历Path
                System.out.println("  " + p);
            }
        }

        public static void inputStreamDemo(){
            int t, len = 0;
            try(InputStream in = new FileInputStream("README.md");) {
                byte[] buffer = new byte[1000];
                while((t = in.read(buffer)) != -1){
                    if(t > 0) len = t;
                    System.out.println(String.format("read %d bytes", t));
                }
                String res = new String(Arrays.copyOf(buffer,len));
                System.out.println(res);
            }catch (IOException e){
                e.printStackTrace();
                return;
            }

            byte[] data = { 72, 101, 108, 108, 111, 33 };
            try (InputStream input = new ByteArrayInputStream(data)) {
                int n;
                while ((n = input.read()) != -1) {
                    System.out.print((char)n);
                }
                System.out.println();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        public static void outputStreamDemo(String s){
            var f = new File(".\\temp.txt");
            boolean have_create = true;
            try{
                if (!f.exists()) {have_create = f.createNewFile();}
            }catch (IOException ie){
                ie.printStackTrace();
                return;
            }
            if(f.exists()){
                try(OutputStream out = new FileOutputStream(f.getAbsolutePath())){
                    out.write(s.getBytes(StandardCharsets.UTF_8));
                }catch (IOException ie){
                    ie.printStackTrace();
                }
            }
        }

        public static void readerDemo(){
            try(Reader r = new FileReader(".\\README.md", StandardCharsets.UTF_8)){
                char[] buffer = new char[5];
                int n = 0;
                StringBuilder b = new StringBuilder();
                while((n = r.read(buffer)) != -1){
                    System.out.println(String.format("read %d chars", n));
                    b.append(new String(Arrays.copyOfRange(buffer, 0, n)));
                }
                System.out.println(b.toString());
            }catch (IOException ie){
                ie.printStackTrace();
                return;
            }

            try(Reader t = new InputStreamReader(System.in)) {
                int i = 0;
            }catch (IOException ie){
                ie.printStackTrace();
                return;
            }

            try (Reader reader = new CharArrayReader("Hello".toCharArray())) {
                System.out.println("simulate char array file");
            }catch (IOException ie){
                ie.printStackTrace();
                return;
            }

            try (Reader reader = new StringReader("Hello")) {
                System.out.println("simulate string file");
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }

        public static void writerDemo(String s){
            try(Writer r = new FileWriter(".\\temp.txt", StandardCharsets.UTF_8,false)){
                r.write(s); // overwrite
            }catch (IOException ie){
                ie.printStackTrace();
                return;
            }

            CharArrayWriter writer = new CharArrayWriter();
            writer.write(65);
            writer.write(66);
            writer.write(67);
            char[] data = writer.toCharArray(); // { 'A', 'B', 'C' }
        }

        public static void printWriterDemo(String[] ss){
            try(var pw = new PrintWriter(new FileWriter("temp.txt", StandardCharsets.UTF_8))){
                for(var s : ss) pw.println(s);
            }catch (IOException ie){
                ie.printStackTrace();
            }
        }
    }

    public static class ThreadDemo{
        public static void createThreadDemo(){
            var t1 = new OneThread();
            t1.start();
            var t2 = new Thread(new OneRunnableClass());
            t2.start();
            var t3 = new Thread(()->{
                System.out.println("start");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
                System.out.println("end");
            });
            t3.start();
            var t4 = new Thread(){
                public void run(){
                    System.out.println("start ".concat(this.toString()));
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException ignored) {}
                    System.out.println("end ".concat(this.toString()));
                }
            };
            t4.start();
        }
        static class OneThread extends Thread{
            @Override public void run(){
                System.out.println("start ".concat(this.toString()));
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) {}
                System.out.println("end ".concat(this.toString()));
            }
        }
        static class OneRunnableClass implements Runnable{
            @Override public void run(){
                System.out.println("start ".concat(this.toString()));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
                System.out.println("end ".concat(this.toString()));
            }
        }

        public static void interruptedThreadDemo(){
            var t1 = new InterruptableThread();
            t1.start();
            try{
                Thread.sleep(1);
            }catch (InterruptedException ignore){}
            t1.interrupt();
            try{
                t1.join();
            }catch (InterruptedException ignore){}
            System.out.println("end");

        }
        static class InterruptableThread extends Thread{
            int n = 0;
            @Override public void run(){
                while(!interrupted()){
                    n++;
                    System.out.println(n);
                }
            }
        }

        public static void volatileDemo(){
            HelloThread t = new HelloThread();
            t.start();
            try{
                Thread.sleep(1);
            }catch (InterruptedException ignore){}
            t.running = false; // 标志位置为false
        }
        static class HelloThread extends Thread {
            public volatile boolean running = true;
            public void run() {
                int n = 0;
                while (running) {
                    n ++;
                    System.out.println(n + " hello!");
                }
                System.out.println("end!");
            }
        }

        public static void daemonDemo(){
            Thread t = new TimerThread();
            t.setDaemon(true);
            t.start();
            try{
                Thread.sleep(1000);
            }catch (InterruptedException ignore){}
        }
        static class TimerThread extends Thread {
            @Override
            public void run() {
                while (true) {
                    System.out.println(LocalTime.now());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }

        public static void synchronizeDemo(){
            var add = new AddThread();
            var dec = new DecThread();
            add.start();
            dec.start();
            try{
                add.join();
                dec.join();
            }catch (InterruptedException ignore){}
            System.out.println(Counter.count);
        }
        static class Counter {
            public static final Object lock = new Object();
            public static int count = 0;
        }
        static class AddThread extends Thread {
            public void run() {
                for (int i=0; i<1000; i++) {
                    synchronized(Counter.lock) {
                        Counter.count += 1;
                    }
                }
            }
        }
        static class DecThread extends Thread {
            public void run() {
                for (int i=0; i<2000; i++) {
                    synchronized(Counter.lock) {
                        Counter.count -= 1;
                    }
                }
            }
        }

        // class design
        public static void waitNotifyAllDemo(){
            var q = new TaskQueue();
            var ts = new ArrayList<Thread>();
            for (int i=0; i<5; i++) {
                var t = new Thread() {
                    public void run() {
                        // 执行task:
                        while (true) {
                            try {
                                String s = q.getTask();
                                System.out.println("execute task: " + s);
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                    }
                };
                t.start();
                ts.add(t);
            }
            var add = new Thread(() -> {
                for (int i=0; i<10; i++) {
                    // 放入task:
                    String s = "t-" + Math.random();
                    System.out.println("add task: " + s);
                    q.addTask(s);
                    try { Thread.sleep(100); } catch(InterruptedException ignore) {}
                }
            });
            add.start();
            try {
                add.join();
                Thread.sleep(100);

            }catch (InterruptedException ignore){}
            for (var t : ts) {
                t.interrupt();
            }
        }
        static class TaskQueue {
            Queue<String> queue = new LinkedList<>();

            public synchronized void addTask(String s) {
                this.queue.add(s);
                this.notifyAll();
            }

            public synchronized String getTask() throws InterruptedException {
                while (queue.isEmpty()) {
                    this.wait();
                }
                return queue.remove();
            }
        }

        // replace synchronised
        public static class ReentrantClassDemo{
            private final Lock this_lock = new ReentrantLock();
            private int count = 0;

            public void add(){
                try {
                    if (this_lock.tryLock(5, TimeUnit.MILLISECONDS)) {
                        try{
                            count++;
                        }finally {
                            this_lock.unlock();
                        }
                    }
                }catch (InterruptedException ignore){}
            }
        }

        // replace wait and notifiy
        public static class ConditionClassDemo{
            private final Lock this_lock = new ReentrantLock();
            private final Condition this_condition = this_lock.newCondition();
            private Queue<String> q = new LinkedList<>();

            public void add(String s){
                this_lock.lock();
                try{
                    q.add(s);
                    this_condition.signalAll();
                }finally {
                    this_lock.unlock();
                }
            }

            public String getTask(){
                this_lock.lock();
                try{
                    while(q.isEmpty()){
                        try{
                            if(!this_condition.await(1, TimeUnit.MILLISECONDS)){
                                throw new RuntimeException("fail to get task");
                            }
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    return q.remove();
                }finally {
                    this_lock.unlock();
                }
            }
        }

        // cannot write while reading
        public static class ReadWriteLockClassDemo{
            private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
            private final Lock rlock = rwlock.readLock();
            private final Lock wlock = rwlock.writeLock();
            private int[] a = new int[5];

            public void inc(int idx){
                if(idx < 0 | idx >= 5) throw new IllegalArgumentException();
                wlock.lock();
                try{
                    a[idx]++;
                }finally {
                    wlock.unlock();
                }
            }

            public int[] read(int idx){
                if(idx < 0 | idx >= 5) throw new IllegalArgumentException();
                rlock.lock();
                try{
                    return Arrays.copyOf(a, a.length);
                }finally {
                    rlock.unlock();
                }
            }
        }

        // can write while reading
        public static class StampedLockClassDemo {
            private final StampedLock stampedLock = new StampedLock();

            private double x;
            private double y;

            public void move(double deltaX, double deltaY) {
                long stamp = stampedLock.writeLock(); // 获取写锁
                try {
                    x += deltaX;
                    y += deltaY;
                } finally {
                    stampedLock.unlockWrite(stamp); // 释放写锁
                }
            }

            public double distanceFromOrigin() {
                long stamp = stampedLock.tryOptimisticRead(); // 获得一个乐观读锁
                double currentX = x;
                double currentY = y;
                if (!stampedLock.validate(stamp)) { // 检查乐观读锁后是否有其他写锁发生
                    stamp = stampedLock.readLock(); // 获取一个悲观读锁
                    try {
                        currentX = x;
                        currentY = y;
                    } finally {
                        stampedLock.unlockRead(stamp); // 释放悲观读锁
                    }
                }
                return Math.sqrt(currentX * currentX + currentY * currentY);
            }
        }

        // atomic class
        public static class IdGenerator {
            AtomicLong var = new AtomicLong(0);

            public long getNextId() {
                return var.incrementAndGet();
            }
        }

        public static void threadPoolDemo(){
            ExecutorService pool = Executors.newFixedThreadPool(4);
            pool.submit(new AddThread());
            pool.submit(new DecThread());
            pool.submit(new AddThread());
            pool.submit(new DecThread());
            try{
                pool.shutdown();
                pool.awaitTermination(1,TimeUnit.NANOSECONDS);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(Counter.count);
            System.out.println(pool.isShutdown());
            System.out.println(pool.isTerminated());


            ScheduledExecutorService spool = Executors.newScheduledThreadPool(4);
            spool.schedule(new Thread(){
                @Override
                public void run(){
                    System.out.println("running");
                }
            }, 1, TimeUnit.MILLISECONDS);

            spool.scheduleAtFixedRate(new Thread(){
                @Override
                public void run(){
                    System.out.println("running");
                }
            }, 0,1, TimeUnit.MILLISECONDS);

            spool.scheduleWithFixedDelay(new Thread(){
                @Override
                public void run(){
                    System.out.println("running");
                }
            }, 0, 1, TimeUnit.MILLISECONDS);
            try{
                Thread.sleep(5);
            }catch (InterruptedException ie){
                ie.printStackTrace();
            }
            spool.shutdownNow();
            System.out.println(spool.isShutdown());
            System.out.println(spool.isTerminated());
            System.out.println(Thread.activeCount());
        }

        public static void futureDemo(){
            var task = new GetAString();
            ExecutorService pool = Executors.newFixedThreadPool(2);
            Future<String> res = pool.submit(task);
            try{
                System.out.println(res.get());
            }catch (ExecutionException | InterruptedException e){
                e.printStackTrace();
            }
            pool.shutdown();
        }
        static class GetAString implements Callable<String>{
            @Override
            public String call(){
                try {
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                return "a future result";
            }
        }

        public static void completableFutureDemo1(){
            // 创建异步执行任务:
            CompletableFuture<Double> cf = CompletableFuture.supplyAsync(ThreadDemo::fetchPrice);
            // 如果执行成功:
            cf.thenAccept((result) -> {
                System.out.println("price: " + result);
            });
            // 如果执行异常:
            cf.exceptionally((e) -> {
                e.printStackTrace();
                return null;
            });
            // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
            try {
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        static Double fetchPrice() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignore) { }
            if (Math.random() < 0.3) {
                throw new RuntimeException("fetch price failed!");
            }
            return 5 + Math.random() * 20;
        }

        public static void completableFutureDemo2() {
            CompletableFuture<String> cfQueryFromSina = CompletableFuture.supplyAsync(() -> {
                return queryCode("中国石油", "https://finance.sina.com.cn/code/");
            });
            CompletableFuture<String> cfQueryFrom163 = CompletableFuture.supplyAsync(() -> {
                return queryCode("中国石油", "https://money.163.com/code/");
            });

            CompletableFuture<Object> cfQuery = CompletableFuture.anyOf(cfQueryFromSina, cfQueryFrom163);

            CompletableFuture<Double> cfFetchFromSina = cfQuery.thenApplyAsync((code) -> {
                return fetchPrice((String) code, "https://finance.sina.com.cn/price/");
            });
            CompletableFuture<Double> cfFetchFrom163 = cfQuery.thenApplyAsync((code) -> {
                return fetchPrice((String) code, "https://money.163.com/price/");
            });

            CompletableFuture<Object> cfFetch = CompletableFuture.anyOf(cfFetchFromSina, cfFetchFrom163);


            cfFetch.thenAccept((result) -> {
                System.out.println("price: " + result);
            });

            try{
                Thread.sleep(200);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        static String queryCode(String name, String url) {
            System.out.println("query code from " + url + "...");
            try {
                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException ignore) {
            }
            return "601857";
        }
        static Double fetchPrice(String code, String url) {
            System.out.println("query price from " + url + "...");
            try {
                Thread.sleep((long) (Math.random() * 100));
            } catch (InterruptedException ignore) {
            }
            return 5 + Math.random() * 20;
        }

        public static void forkJoinDemo(){
            int[] a = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
            var task = new BoringAdd(a, 0, a.length);
            var res = ForkJoinPool.commonPool().invoke(task);
            System.out.println(res);
        }
        static class BoringAdd extends RecursiveTask<Integer> {
            int[] array;
            int start;
            int end;

            public BoringAdd(int[] a, int start, int end){
                array = a;
                this.start = start;
                this.end = end;

            }

            @Override
            public Integer compute(){
                if(end - start <= 2){
                    int res = 0;
                    for(int i = start; i < end; i++){
                        res += array[i];
                    }
                    return res;
                }else {
                    int mid = (start + end) / 2;
                    var t1 = new BoringAdd(array, start, mid);
                    var t2 = new BoringAdd(array, mid, end);
                    invokeAll(t1, t2);
                    var res1 = t1.join();
                    var res2 = t2.join();
                    return res1 + res2;
                }
            }
        }

        public static void threadLocalDemo(){
            var thread1 = new TwicePrintThread("hello", "thread1");
            var thread2 = new TwicePrintThread("world", "thread2");
            thread1.start();
            thread2.start();
            try{
                Thread.sleep(20);
            }catch (InterruptedException e){e.printStackTrace();}
        }
        static class TwicePrintThread extends Thread{
            static ThreadLocal<String> twice_thread_local_string = new ThreadLocal<>();
            String message;
            String other_message;


            public TwicePrintThread(String s, String other_s) {
                message = s;
                other_message = other_s;
            }

            @Override
            public void run(){
                try{
                    twice_thread_local_string.set(message);
                    firstPrint();
                    System.out.println(other_message);
                    secondPrint();
                }finally {
                    twice_thread_local_string.remove();
                }
            }

            private void firstPrint(){
                System.out.println(String.format("first: %s", twice_thread_local_string.get()));
            }

            private void secondPrint(){
                System.out.println(String.format("second: %s", twice_thread_local_string.get()));
            }
        }
    }

    public static class StreamDemo {
        private static final Map<String, String> phone = new HashMap<String, String>() {{
            put("2", "abc");
            put("3", "def");
            put("4", "ghi");
            put("5", "jkl");
            put("6", "mno");
            put("7", "pqrs");
            put("8", "tuv");
            put("9", "wxyz");
        }};
        public static List<String> letterCombinations(String digits) {
            if(digits.equals("")) return List.of();
            int len = digits.length();
            List<String> res = null;
            Stream<String> s1 = Stream.of(digits);
            for(int i = 0; i < len; i++){
                int finalI = i;
                Stream<List<String>> temp = s1.parallel().map(
                        (str) -> {
                            String chars = phone.get(String.valueOf(str.charAt(finalI)));
                            int chars_len = chars.length();
                            String start = str.substring(0, finalI);
                            String end = str.substring(finalI+1, len);
                            String[] t = new String[chars_len];
                            for(int j = 0; j < chars_len; j++){
                                t[j] = start.concat(String.valueOf(chars.charAt(j))).concat(end);
                            }
                            return List.of(t);
                        }
                );
                Stream<String> s2 = temp.parallel().flatMap(Collection::stream);
                res = s2.parallel().collect(Collectors.toList());
                if(i < len - 1)
                    s1 = res.parallelStream();
            }
            return res;
        }
        public static void createDemo(){
            var s1 = Stream.of("a", "b");
            var s2 = Arrays.stream(new String[]{"a", "b"});
            var s3 = List.of(1, 2).stream();
            var s4 = Stream.generate(new FibonacciGenerator());
            s4.limit(9).forEach(System.out::println);

        }
        //0 1 1 2 3 5 8 13 21
        static class FibonacciGenerator implements Supplier<Integer>{
            int a = 0;
            int b = 1;
            @Override
            public Integer get(){
                int t = a;
                int n = a + b;
                a = b;
                b = n;
                return t;
            }
        }
    }
}