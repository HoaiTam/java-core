# Java Core 07 - Working with Threads

> Tổng hợp từ slide 2 đến slide 20 của phần Threads. Nội dung gồm nền tảng concurrency, vòng đời thread, cách tạo/quản lý thread, synchronization, lock và các lưu ý production.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Introduction to threads](#1-introduction-to-threads)
  - [1.1 Thread hoạt động như thế nào](#11-thread-hoạt-động-như-thế-nào)
  - [1.2 Các trường hợp sử dụng](#12-các-trường-hợp-sử-dụng)
  - [1.3 Vòng đời của thread](#13-vòng-đời-của-thread)
  - [1.4 Constructor thường dùng](#14-constructor-thường-dùng)
  - [1.5 Quản lý thread và priority](#15-quản-lý-thread-và-priority)
- [2. Create threads](#2-create-threads)
  - [2.1 Kế thừa Thread](#21-kế-thừa-thread)
  - [2.2 Implement Runnable](#22-implement-runnable)
  - [2.3 start, run, join và interrupt](#23-start-run-join-và-interrupt)
  - [2.4 Callable và Future](#24-callable-và-future)
  - [2.5 ExecutorService trong production](#25-executorservice-trong-production)
- [3. Synchronize threads](#3-synchronize-threads)
  - [3.1 Race condition, visibility và atomicity](#31-race-condition-visibility-và-atomicity)
  - [3.2 Synchronized method](#32-synchronized-method)
  - [3.3 Synchronized block](#33-synchronized-block)
  - [3.4 Nhiều lock độc lập](#34-nhiều-lock-độc-lập)
  - [3.5 Lock và ReentrantLock](#35-lock-và-reentrantlock)
  - [3.6 Static synchronized](#36-static-synchronized)
  - [3.7 wait, notify và notifyAll](#37-wait-notify-và-notifyall)
  - [3.8 Atomic và concurrent collections](#38-atomic-và-concurrent-collections)
  - [3.9 Deadlock và nguyên tắc production](#39-deadlock-và-nguyên-tắc-production)

---

## Mục tiêu

Sau phần này, cần nắm được:

1. Thread, concurrency và parallelism khác nhau thế nào.
2. Vòng đời và các trạng thái chuẩn của `Thread`.
3. Cách tạo task bằng `Runnable`/`Callable` và thực thi qua executor.
4. Cách dùng `start`, `join`, `interrupt` đúng mục đích.
5. Vì sao xảy ra race condition và cách tạo happens-before bằng synchronization.
6. Cách chọn `synchronized`, `Lock`, atomic type hoặc concurrent collection.

---

## 1. Introduction to threads

Thread là một luồng thực thi bên trong process. Một process Java có ít nhất main thread và có thể có nhiều thread cùng hoạt động.

```java
public static void main(String[] args) {
    Thread current = Thread.currentThread();

    System.out.println(current.getName()); // main
}
```

Các thread trong cùng process:

- Chia sẻ heap, object và tài nguyên process.
- Có stack, program counter và trạng thái thực thi riêng.
- Có thể chạy xen kẽ hoặc song song tùy CPU và scheduler.

Vì cùng chia sẻ object, nhiều thread có thể truy cập cùng mutable state. Đây là nguồn của race condition, visibility bug và deadlock nếu không phối hợp đúng.

### Concurrency và parallelism

- **Concurrency**: nhiều công việc cùng tiến triển trong một khoảng thời gian; chúng có thể chạy xen kẽ trên một CPU core.
- **Parallelism**: nhiều công việc thật sự chạy cùng lúc trên nhiều core.

Một chương trình multi-thread không đảm bảo luôn chạy song song. Scheduler, số core, workload và blocking operation quyết định cách thực thi thực tế.

## 1.1 Thread hoạt động như thế nào

Với một thread, khi task chờ I/O thì CPU có thể không làm phần việc hữu ích cho process đó. Với nhiều thread, scheduler có thể cho thread khác chạy trong lúc một thread chờ.

```text
Thread 1: [CPU work] [wait I/O] [CPU work] [wait I/O]
Thread 2:           [CPU work]            [CPU work]
```

Điều này thường cải thiện throughput với workload I/O-bound. Với CPU-bound workload, số thread quá lớn gây context switching và contention, có thể làm chậm hơn.

### Thread không tự động làm chương trình nhanh hơn

Chi phí của concurrency gồm:

- Tạo và quản lý thread.
- Context switching.
- Bộ nhớ stack.
- Lock contention.
- Cache invalidation và memory synchronization.
- Độ phức tạp khi debug và test.

Trước khi thêm concurrency, cần xác định bottleneck và đo hiệu năng thực tế.

## 1.2 Các trường hợp sử dụng

### Server xử lý nhiều request

Nhiều request có thể tiến triển đồng thời, đặc biệt khi chờ database hoặc network. Production server thường dùng thread pool hoặc execution model do framework quản lý, không tạo `new Thread()` cho từng request.

### GUI giữ khả năng phản hồi

UI thread xử lý event và cập nhật giao diện; tác vụ chậm chạy ở background. Không cập nhật UI từ thread tùy ý nếu framework yêu cầu UI-thread confinement.

### I/O song song

Đọc nhiều file, gọi nhiều service hoặc xử lý pipeline có bước chờ. Cần giới hạn concurrency để tránh làm quá tải downstream.

### CPU-bound task có thể chia nhỏ

Xử lý ảnh, tính toán hoặc batch có thể chạy song song nếu các phần độc lập. Số worker thường liên quan số CPU core, không phải càng nhiều càng tốt.

### Lợi ích không được đảm bảo

Slide ghi thread cải thiện performance và responsiveness. Đây là **khả năng**, không phải bảo đảm. Thiết kế sai có thể tạo contention, timeout, cạn connection pool hoặc dữ liệu không nhất quán.

## 1.3 Vòng đời của thread

`Thread.State` có sáu trạng thái chính:

| Trạng thái | Ý nghĩa |
|---|---|
| `NEW` | Đã tạo object `Thread` nhưng chưa gọi `start()` |
| `RUNNABLE` | Đang chạy hoặc sẵn sàng được scheduler chạy |
| `BLOCKED` | Chờ lấy intrinsic monitor lock để vào vùng `synchronized` |
| `WAITING` | Chờ vô thời hạn, ví dụ `wait()` hoặc `join()` không timeout |
| `TIMED_WAITING` | Chờ có thời hạn, ví dụ `sleep`, `wait(timeout)`, `join(timeout)` |
| `TERMINATED` | `run()` đã kết thúc do return hoặc exception không được xử lý |

### Hiệu chỉnh sơ đồ trên slide

- Tên chuẩn là `TIMED_WAITING`, không phải “Times Waiting”.
- Java không có trạng thái `RUNNING` riêng trong `Thread.State`; đang chạy được gộp vào `RUNNABLE`.
- Thread chờ monitor lock là `BLOCKED`, khác với `WAITING`.
- `Thread.stop()` đã deprecated và không an toàn; không dùng để kết thúc thread.
- `notify()` chỉ chuyển một waiter sang trạng thái có thể cạnh tranh lock; nó không chạy ngay lập tức.

### Quan sát trạng thái

```java
Thread worker = new Thread(() -> {
    try {
        Thread.sleep(1_000);
    } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
    }
}, "worker-1");

System.out.println(worker.getState()); // NEW
worker.start();
```

State chỉ là snapshot; nó có thể thay đổi ngay sau khi đọc. Không dùng polling state để điều phối business logic.

### `sleep()` không nhả monitor

Nếu thread gọi `Thread.sleep()` khi đang giữ intrinsic lock, lock vẫn bị giữ. Ngược lại, `wait()` nhả monitor của object trước khi chờ và phải được gọi khi thread đang sở hữu monitor đó.

## 1.4 Constructor thường dùng

| Constructor | Mục đích |
|---|---|
| `Thread()` | Tạo thread không có target `Runnable` |
| `Thread(Runnable target)` | Tạo thread chạy target |
| `Thread(String name)` | Tạo thread có tên |
| `Thread(Runnable target, String name)` | Tạo thread với target và tên |

```java
Runnable task = () -> processOrder();
Thread thread = new Thread(task, "order-worker-1");
```

Đặt tên thread giúp log, thread dump và metric dễ phân tích hơn.

Thread còn có constructor nhận `ThreadGroup` và stack size, nhưng hiếm khi cần trong application code hiện đại.

## 1.5 Quản lý thread và priority

Java định nghĩa priority từ 1 đến 10:

```java
Thread.MIN_PRIORITY;  // 1
Thread.NORM_PRIORITY; // 5
Thread.MAX_PRIORITY;  // 10
```

```java
thread.setPriority(Thread.NORM_PRIORITY);
```

### Priority chỉ là hint

Slide mô tả priority quyết định thread nào nhận CPU trước. Thực tế priority chỉ là gợi ý cho scheduler và hành vi phụ thuộc JVM/OS. Không được dựa vào priority để đảm bảo:

- Thứ tự thực thi.
- Fairness.
- Deadline.
- Tính đúng của chương trình.

Muốn điều phối thứ tự, dùng queue, lock, semaphore, latch hoặc executor phù hợp.

### Daemon thread

Daemon thread không giữ JVM sống nếu mọi user thread đã kết thúc:

```java
Thread monitor = new Thread(this::monitor, "monitor");
monitor.setDaemon(true);
monitor.start();
```

Không dùng daemon thread cho công việc bắt buộc phải flush/lưu hoàn tất vì JVM có thể kết thúc mà không chờ nó.

### Lưu ý mục 1

- Thread trong cùng process chia sẻ heap nhưng có stack riêng.
- Concurrency là nhiều task cùng tiến triển; parallelism là thật sự chạy đồng thời.
- Nhiều thread không tự động cải thiện performance.
- Dùng đúng sáu trạng thái của `Thread.State`; `RUNNABLE` bao gồm ready/running.
- `sleep()` không nhả lock; `wait()` nhả monitor tương ứng.
- Không dùng `Thread.stop()`; kết thúc task bằng interruption hoặc tín hiệu cooperative.
- Priority chỉ là scheduler hint, không phải cơ chế điều phối tin cậy.

---

## 2. Create threads

Có hai cách cơ bản trên slide:

1. Kế thừa `Thread` và override `run()`.
2. Implement `Runnable`, sau đó truyền task cho `Thread`.

Trong production, thường tách **task** khỏi **cơ chế thực thi** bằng `Runnable`/`Callable` và `ExecutorService`.

## 2.1 Kế thừa Thread

```java
public class LoggingThread extends Thread {
    public LoggingThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        System.out.println(getName() + " started");

        try {
            Thread.sleep(2_000);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            System.out.println(getName() + " interrupted");
            return;
        }

        System.out.println(getName() + " finished");
    }
}
```

```java
Thread worker = new LoggingThread("io-worker");
worker.start();
```

### Hạn chế

- Java chỉ cho extends một class, nên mất cơ hội kế thừa class khác.
- Task và thread lifecycle bị gắn chặt.
- Khó tái sử dụng task trong pool hoặc test trực tiếp.

Kế thừa `Thread` phù hợp khi thật sự cần tùy biến chính object thread, nhưng hiếm trong business application.

## 2.2 Implement Runnable

```java
public final class IOTask implements Runnable {
    @Override
    public void run() {
        Thread current = Thread.currentThread();

        try {
            Thread.sleep(2_000);
            System.out.println(current.getName() + " finished");
        } catch (InterruptedException exception) {
            current.interrupt();
            System.out.println(current.getName() + " interrupted");
        }
    }
}
```

```java
Runnable task = new IOTask();
Thread thread = new Thread(task, "io-worker");
thread.start();
```

Hoặc dùng lambda:

```java
Thread thread = new Thread(
        () -> processFile(),
        "file-worker");
thread.start();
```

### Hiệu chỉnh code trên slide

Nếu `IOTask implements Runnable`, code sau không biên dịch:

```java
// Thread thread = new IOTask();
// thread.start();
```

`IOTask` là task, không phải `Thread`. Phải bọc bằng `new Thread(new IOTask())` hoặc gửi vào executor.

## 2.3 `start`, `run`, `join` và `interrupt`

### `start()` khác `run()`

```java
Thread thread = new Thread(task);
thread.start();
```

`start()` yêu cầu JVM tạo luồng thực thi mới, sau đó luồng mới gọi `run()`.

```java
thread.run();
```

Gọi `run()` trực tiếp chỉ là gọi method bình thường trên thread hiện tại, không tạo thread mới.

Một `Thread` chỉ được start một lần. Gọi `start()` lần hai gây `IllegalThreadStateException`.

### `join()` chờ thread hoàn tất

```java
Thread worker = new Thread(task, "worker");
worker.start();

try {
    worker.join();
    System.out.println("Worker completed");
} catch (InterruptedException exception) {
    Thread.currentThread().interrupt();
}
```

Slide in “finished” ngay sau `start()`, nhưng main thread có thể in trước khi worker hoàn tất. Muốn bảo đảm thứ tự phải `join()` hoặc dùng future/latch.

### Interruption là yêu cầu cooperative

```java
public void runUntilInterrupted() {
    while (!Thread.currentThread().isInterrupted()) {
        processNextItem();
    }
}
```

```java
worker.interrupt();
```

`interrupt()` không ép dừng thread. Nó đặt interrupt flag; các blocking method như `sleep`, `wait`, `join` thường ném `InterruptedException` và xóa flag.

Khi không thể xử lý hoàn chỉnh, thường phục hồi flag:

```java
catch (InterruptedException exception) {
    Thread.currentThread().interrupt();
    return;
}
```

Không nuốt interruption vì tầng gọi mất khả năng cancel/shutdown task.

## 2.4 Callable và Future

`Runnable.run()` không trả kết quả và không khai báo checked exception. `Callable<V>` phù hợp khi task cần kết quả:

```java
Callable<BigDecimal> calculateTotal = () -> {
    return orderRepository.calculateTotal();
};
```

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<BigDecimal> future = executor.submit(calculateTotal);

try {
    BigDecimal total = future.get(2, TimeUnit.SECONDS);
} catch (TimeoutException exception) {
    future.cancel(true);
} finally {
    executor.shutdown();
}
```

`Future.get()` là blocking. Luôn cân nhắc timeout, cancellation và cách shutdown executor.

## 2.5 ExecutorService trong production

Executor tách task submission khỏi quản lý worker thread:

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

try {
    List<Future<Result>> futures = new ArrayList<>();

    for (Job job : jobs) {
        futures.add(executor.submit(() -> process(job)));
    }

    for (Future<Result> future : futures) {
        Result result = future.get(5, TimeUnit.SECONDS);
        save(result);
    }
} finally {
    executor.shutdown();
}
```

### Shutdown có kiểm soát

```java
executor.shutdown();

try {
    if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
        executor.shutdownNow();
    }
} catch (InterruptedException exception) {
    executor.shutdownNow();
    Thread.currentThread().interrupt();
}
```

### Bounded queue để tạo backpressure

`Executors.newFixedThreadPool()` dùng queue không giới hạn. Khi producer nhanh hơn consumer trong thời gian dài, queue có thể tăng tới mức cạn memory. Production workload quan trọng nên cân nhắc pool có queue giới hạn:

```java
ExecutorService executor = new ThreadPoolExecutor(
        4,
        4,
        0L,
        TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(100),
        new ThreadPoolExecutor.CallerRunsPolicy());
```

Policy phải phù hợp nghiệp vụ: block/backpressure, reject, retry có giới hạn hoặc chuyển sang durable queue. Không retry vô hạn.

### Lưu ý mục 2

- Ưu tiên `Runnable`/`Callable` thay vì extends `Thread` cho business task.
- Gọi `start()` để tạo execution flow mới; gọi `run()` trực tiếp không tạo thread.
- Một object `Thread` chỉ start được một lần.
- Dùng `join`, `Future`, latch hoặc structured coordination thay vì giả định thứ tự log.
- Interruption là cooperative cancellation; phục hồi interrupt flag khi không xử lý được.
- Production code nên dùng executor được quản lý, timeout và shutdown rõ ràng.
- Giới hạn pool/queue theo capacity của downstream để tránh cạn tài nguyên.

---

## 3. Synchronize threads

Khi nhiều thread truy cập mutable shared state, kết quả phụ thuộc thứ tự xen kẽ nếu không có coordination. `synchronized` vừa cung cấp mutual exclusion vừa tạo memory visibility theo Java Memory Model.

## 3.1 Race condition, visibility và atomicity

### Race condition

```java
public class UnsafeCounter {
    private int value;

    public void increment() {
        value++;
    }
}
```

`value++` không atomic. Nó gồm đọc, cộng và ghi. Hai thread có thể cùng đọc giá trị cũ rồi ghi đè kết quả của nhau.

### Visibility

Một thread thay đổi field không đồng nghĩa thread khác lập tức thấy thay đổi nếu thiếu happens-before relationship. Compiler, CPU và cache có thể reorder/giữ giá trị.

### Atomicity

`volatile` hỗ trợ visibility và ordering cho truy cập field, nhưng không biến compound operation thành atomic:

```java
private volatile int count;

// count++ vẫn không atomic
```

Với counter đơn giản dùng `AtomicInteger`; với invariant nhiều field dùng cùng lock.

## 3.2 Synchronized method

```java
public final class SynchronizedCounter {
    private int value;

    public synchronized void increment() {
        value++;
    }

    public synchronized void decrement() {
        value--;
    }

    public synchronized int value() {
        return value;
    }
}
```

Instance synchronized method khóa monitor của `this`. Mỗi instance có monitor riêng, nên hai instance không chặn lẫn nhau.

Lock là reentrant: thread đang giữ monitor có thể gọi synchronized method khác dùng cùng monitor mà không tự deadlock.

### Lock toàn bộ invariant

Nếu tính đúng phụ thuộc nhiều field, mọi thao tác đọc/ghi liên quan phải dùng cùng lock. Chỉ synchronized writer nhưng reader không synchronized vẫn có visibility/race problem.

## 3.3 Synchronized block

Block cho phép giới hạn critical section:

```java
public final class NameRegistry {
    private final Object lock = new Object();
    private final List<String> names = new ArrayList<>();
    private String lastName;
    private int nameCount;

    public void addName(String name) {
        validate(name);

        synchronized (lock) {
            names.add(name);
            lastName = name;
            nameCount++;
        }
    }
}
```

Slide chỉ đặt `lastName` và `nameCount` trong block nhưng gọi `nameList.add` bên ngoài. Nếu list là shared mutable state thuộc cùng invariant, cách đó vẫn race. Tất cả trạng thái liên quan phải được bảo vệ bằng cùng strategy.

### Không khóa trên object public hoặc thay đổi được

Tránh:

```java
synchronized (name) {
}
```

String có thể intern/chia sẻ ngoài ý muốn. Nên dùng lock object `private final` mà code bên ngoài không truy cập được.

## 3.4 Nhiều lock độc lập

Hai state thật sự độc lập có thể dùng hai lock để giảm contention:

```java
public final class TwoCounters {
    private final Object firstLock = new Object();
    private final Object secondLock = new Object();

    private long first;
    private long second;

    public void incrementFirst() {
        synchronized (firstLock) {
            first++;
        }
    }

    public void incrementSecond() {
        synchronized (secondLock) {
            second++;
        }
    }
}
```

Chỉ tách lock khi hai invariant độc lập. Nếu một thao tác cần cập nhật `first` và `second` nguyên tử cùng nhau, hai lock làm coordination phức tạp hơn và dễ deadlock.

## 3.5 Lock và ReentrantLock

Slide dùng:

```java
// Lock lock = new Lock(); // Không hợp lệ: Lock là interface
```

Implementation phổ biến là `ReentrantLock`:

```java
public final class LockedCounter {
    private final Lock lock = new ReentrantLock();
    private int value;

    public int incrementAndGet() {
        lock.lock();
        try {
            return ++value;
        } finally {
            lock.unlock();
        }
    }
}
```

Luôn `unlock()` trong `finally`; nếu code ném exception mà không unlock, các thread khác có thể chờ vĩnh viễn.

### Khi `Lock` hữu ích

- `tryLock()` để không chờ vô hạn.
- `lockInterruptibly()` để cho phép cancel khi chờ lock.
- Nhiều `Condition` trên một lock.
- Tùy chọn fairness khi thật sự cần.

```java
if (lock.tryLock(500, TimeUnit.MILLISECONDS)) {
    try {
        update();
    } finally {
        lock.unlock();
    }
} else {
    throw new TimeoutException("Cannot acquire lock");
}
```

Không dùng `ReentrantLock` chỉ vì nó có vẻ “mạnh hơn”; `synchronized` đơn giản và an toàn hơn cho nhiều trường hợp.

## 3.6 Static synchronized

Static synchronized method khóa monitor của object `Class`, không phải instance:

```java
public final class Screen {
    private static Screen instance;

    private Screen() {
    }

    public static synchronized Screen getInstance() {
        if (instance == null) {
            instance = new Screen();
        }
        return instance;
    }
}
```

Code này thread-safe nhưng mọi lần gọi đều lấy class lock. Với singleton đơn giản, initialization-on-demand holder rõ ràng hơn:

```java
public final class Screen {
    private Screen() {
    }

    private static class Holder {
        private static final Screen INSTANCE = new Screen();
    }

    public static Screen getInstance() {
        return Holder.INSTANCE;
    }
}
```

Class initialization của JVM đảm bảo thread-safe publication.

## 3.7 `wait`, `notify` và `notifyAll`

Các method này thuộc `Object` và phải được gọi khi đang sở hữu monitor:

```java
public final class MessageBox {
    private String message;

    public synchronized String take()
            throws InterruptedException {
        while (message == null) {
            wait();
        }

        String result = message;
        message = null;
        notifyAll();
        return result;
    }

    public synchronized void put(String newMessage)
            throws InterruptedException {
        while (message != null) {
            wait();
        }

        message = newMessage;
        notifyAll();
    }
}
```

Luôn kiểm tra condition trong `while`, không dùng `if`, vì spurious wakeup hoặc thread khác có thể thay đổi condition trước khi thread lấy lại lock.

Production code thường ưu tiên `BlockingQueue`, latch, semaphore hoặc higher-level utility thay vì tự viết wait/notify protocol.

## 3.8 Atomic và concurrent collections

### AtomicInteger

```java
public final class AtomicCounter {
    private final AtomicInteger value = new AtomicInteger();

    public int incrementAndGet() {
        return value.incrementAndGet();
    }
}
```

Atomic type phù hợp với thao tác đơn trên một biến. Nó không tự bảo vệ invariant trải trên nhiều object/field.

### ConcurrentHashMap

```java
ConcurrentMap<String, Long> counters =
        new ConcurrentHashMap<>();

counters.merge("SUCCESS", 1L, Long::sum);
```

Dùng atomic compound method như `compute`, `merge`, `putIfAbsent` thay vì chuỗi `get` rồi `put` không nguyên tử.

### BlockingQueue

```java
BlockingQueue<Job> queue = new ArrayBlockingQueue<>(100);

queue.put(job);   // Chờ khi đầy
Job next = queue.take(); // Chờ khi rỗng
```

Nó cung cấp producer-consumer coordination và backpressure rõ ràng hơn tự wait/notify.

## 3.9 Deadlock và nguyên tắc production

Deadlock xảy ra khi các thread giữ lock và chờ nhau theo chu trình:

```text
Thread A giữ lock1, chờ lock2
Thread B giữ lock2, chờ lock1
```

### Giảm nguy cơ deadlock

- Luôn lấy nhiều lock theo một thứ tự toàn cục.
- Giữ critical section nhỏ, không làm I/O/network khi đang giữ lock.
- Không gọi code ngoài tầm kiểm soát trong lock nếu có thể.
- Dùng `tryLock` với timeout khi phù hợp.
- Theo dõi thread dump và lock contention trong production.

### Ví dụ cập nhật kho an toàn

```java
public final class InventoryService {
    private final ConcurrentMap<String, AtomicInteger> quantities =
            new ConcurrentHashMap<>();

    public void add(String sku, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        quantities.computeIfAbsent(
                        sku,
                        ignored -> new AtomicInteger())
                .addAndGet(amount);
    }

    public boolean reserve(String sku, int amount) {
        AtomicInteger quantity = quantities.get(sku);
        if (quantity == null || amount <= 0) {
            return false;
        }

        while (true) {
            int current = quantity.get();
            if (current < amount) {
                return false;
            }

            if (quantity.compareAndSet(current, current - amount)) {
                return true;
            }
        }
    }
}
```

Ví dụ thể hiện compare-and-set cho một counter trong memory. Hệ thống kho thực tế có database, transaction, nhiều process và distributed consistency; Java lock trong một JVM không thay thế database constraint/transaction.

### Lưu ý mục 3

- `synchronized` cung cấp mutual exclusion và memory visibility.
- Instance synchronized khóa `this`; static synchronized khóa object `Class`.
- `volatile` không làm `count++` atomic.
- Mọi field thuộc cùng invariant phải được bảo vệ bằng cùng coordination strategy.
- `Lock` là interface; dùng `ReentrantLock` và luôn unlock trong `finally`.
- Dùng nhiều lock chỉ cho state độc lập và duy trì lock ordering khi cần lấy nhiều lock.
- Luôn gọi `wait()` trong vòng `while` kiểm tra condition; ưu tiên utility cấp cao khi có thể.
- Dùng atomic type/concurrent collection cho use case phù hợp, không xem chúng là phép màu cho mọi invariant.
- Không giữ lock trong khi gọi network, database hoặc code không kiểm soát.
- Lock trong JVM không bảo vệ dữ liệu giữa nhiều process/instance; distributed system cần transaction hoặc coordination ở tầng phù hợp.
