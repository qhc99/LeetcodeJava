import java.io.*;
import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;
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
                char[] buffer = new char[60];
                int n;
                while((n = r.read(buffer)) != -1){
                    System.out.println(String.format("read %d chars", n));
                }
                String res = new String(buffer);
                System.out.println(res);
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
    }
}

