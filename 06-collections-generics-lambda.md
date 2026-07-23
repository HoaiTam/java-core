# Java Core 06 - Collections, Generics and Lambda

> Tổng hợp từ slide 2 đến slide 23 của phần Collections, Generics and Lambda. Nội dung có bổ sung cách chọn cấu trúc dữ liệu, PECS, type erasure, functional interface và ví dụ production.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Collections](#1-collections)
  - [1.1 So sánh array và collection](#11-so-sánh-array-và-collection)
  - [1.2 Collection Framework](#12-collection-framework)
  - [1.3 Các method của Collection](#13-các-method-của-collection)
  - [1.4 Chọn collection phù hợp](#14-chọn-collection-phù-hợp)
  - [1.5 Ví dụ production](#15-ví-dụ-production)
- [2. Generics](#2-generics)
  - [2.1 Generic class, interface và method](#21-generic-class-interface-và-method)
  - [2.2 Quy ước đặt tên type parameter](#22-quy-ước-đặt-tên-type-parameter)
  - [2.3 Bounded type và wildcard](#23-bounded-type-và-wildcard)
  - [2.4 Nguyên tắc PECS](#24-nguyên-tắc-pecs)
  - [2.5 Raw type](#25-raw-type)
  - [2.6 Primitive, wrapper và autoboxing](#26-primitive-wrapper-và-autoboxing)
  - [2.7 Type erasure và giới hạn](#27-type-erasure-và-giới-hạn)
- [3. Lambda expressions](#3-lambda-expressions)
  - [3.1 Functional interface và cú pháp lambda](#31-functional-interface-và-cú-pháp-lambda)
  - [3.2 Các dạng lambda thường dùng](#32-các-dạng-lambda-thường-dùng)
  - [3.3 Method reference](#33-method-reference)
  - [3.4 Các functional interface chuẩn](#34-các-functional-interface-chuẩn)
  - [3.5 Predicate](#35-predicate)
  - [3.6 Ví dụ production](#36-ví-dụ-production)

---

## Mục tiêu

Sau phần này, cần nắm được:

1. Sự khác nhau giữa array và Collection Framework.
2. Cách chọn `List`, `Set`, `Queue`, `Deque` hoặc `Map` theo nghiệp vụ.
3. Cách generics cung cấp type safety và loại bỏ cast không cần thiết.
4. Cách sử dụng upper/lower bounded wildcard theo nguyên tắc PECS.
5. Cách lambda triển khai functional interface.
6. Cách dùng `Predicate`, `Function`, `Consumer` và `Supplier`.

---

## 1. Collections

Collection là object quản lý một nhóm object khác. Java Collections Framework cung cấp các interface, implementation và thuật toán dùng chung để lưu trữ, truy xuất và biến đổi dữ liệu.

### `Collection` và `Collections` khác nhau

- `java.util.Collection<E>` là interface gốc của phần lớn cấu trúc dữ liệu dạng nhóm.
- `java.util.Collections` là utility class chứa static method như `sort`, `reverse`, `min`, `max` và `unmodifiableList`.

```java
Collection<String> names = new ArrayList<>();
names.add("An");
names.add("Bình");
```

```java
List<String> names = new ArrayList<>(List.of("Bình", "An"));
Collections.sort(names);
```

Tên gần giống nhau nhưng vai trò hoàn toàn khác.

### Collection chỉ chứa reference type

Generic collection không nhận primitive làm type argument:

```java
// List<int> numbers; // Không hợp lệ
List<Integer> numbers = new ArrayList<>();
```

Java tự autobox `int` thành `Integer` khi thêm vào collection.

## 1.1 So sánh array và collection

| Tiêu chí | Array | Collection |
|---|---|---|
| Kích thước | Cố định sau khi tạo | Phần lớn implementation có thể tăng/giảm |
| Phần tử | Primitive hoặc reference | Chỉ reference type |
| Type safety | Có | Có khi dùng generics |
| Truy cập index | `array[index]` | Chỉ `List` hỗ trợ `get(index)` |
| API thao tác | Cơ bản | Thêm, xóa, tìm, lọc, sắp xếp |
| Bộ nhớ | Gọn, dự đoán tốt | Có overhead tùy implementation |
| Quan hệ type | Array covariance | Generics invariant |

### Array

```java
String[] frameworks = new String[3];
frameworks[0] = "Java";
frameworks[1] = "Spring";
frameworks[2] = "Hibernate";

for (String framework : frameworks) {
    System.out.println(framework);
}
```

### ArrayList

```java
List<String> frameworks = new ArrayList<>();
frameworks.add("Java");
frameworks.add("Spring");
frameworks.add("Hibernate");

for (String framework : frameworks) {
    System.out.println(framework);
}
```

`ArrayList` tự quản lý backing array và tăng capacity khi cần. Kích thước logic lấy bằng `size()`, khác với `length` của array.

### Chọn array khi

- Kích thước cố định và đã biết.
- Cần lưu primitive hiệu quả.
- API yêu cầu array.
- Cần layout đơn giản và truy cập index nhanh.

### Chọn collection khi

- Số phần tử thay đổi.
- Cần tìm kiếm, loại trùng, sắp xếp hoặc API thao tác phong phú.
- Muốn thể hiện rõ semantics như danh sách, tập hợp, hàng đợi hoặc ánh xạ key-value.

## 1.2 Collection Framework

Sơ đồ khái quát:

```text
Iterable
   |
Collection
   |-- List
   |     |-- ArrayList
   |     |-- LinkedList
   |     `-- Vector (legacy)
   |-- Set
   |     |-- HashSet
   |     |-- LinkedHashSet
   |     `-- SortedSet / NavigableSet
   |             `-- TreeSet
   `-- Queue
         |-- PriorityQueue
         `-- Deque
               |-- ArrayDeque
               `-- LinkedList

Map (không extends Collection)
   |-- HashMap
   |-- LinkedHashMap
   `-- SortedMap / NavigableMap
           `-- TreeMap
```

### Hiệu chỉnh sơ đồ trên slide

- `Map<K,V>` thuộc Collections Framework nhưng **không kế thừa `Collection`**.
- `Deque<E>` extends `Queue<E>`; `ArrayDeque` là implementation phổ biến.
- `SortedSet` được mở rộng bởi `NavigableSet`; `TreeSet` implement `NavigableSet`.
- `SortedMap` được mở rộng bởi `NavigableMap`; `TreeMap` implement `NavigableMap`.
- `Stack` extends `Vector`, nhưng cả hai là API legacy. Code mới thường dùng `ArrayDeque`.

### List

`List` giữ thứ tự, cho phép phần tử trùng và truy cập theo index.

```java
List<String> events = new ArrayList<>();
events.add("CREATED");
events.add("PAID");
events.add("PAID");
```

`ArrayList` là lựa chọn mặc định tốt cho đa số danh sách. `LinkedList` hiếm khi nhanh hơn trong ứng dụng thực tế vì truy cập cache kém và tìm vị trí vẫn tốn O(n).

### Set

`Set` không chứa phần tử trùng theo `equals()`:

```java
Set<String> roles = new HashSet<>();
roles.add("ADMIN");
roles.add("ADMIN");

System.out.println(roles.size()); // 1
```

- `HashSet`: không đảm bảo thứ tự, thao tác trung bình O(1).
- `LinkedHashSet`: giữ thứ tự insertion.
- `TreeSet`: sắp xếp tự nhiên hoặc theo `Comparator`, thao tác O(log n).

### Queue và Deque

```java
Queue<String> jobs = new ArrayDeque<>();
jobs.offer("job-1");
jobs.offer("job-2");

String nextJob = jobs.poll();
```

`offer`, `poll`, `peek` thường phù hợp hơn `add`, `remove`, `element` vì không ném exception khi queue đầy hoặc rỗng.

`Deque` hỗ trợ hai đầu và có thể dùng làm stack:

```java
Deque<String> history = new ArrayDeque<>();
history.push("page-1");
history.push("page-2");

System.out.println(history.pop()); // page-2
```

### Map

`Map` lưu key-value. Mỗi key chỉ xuất hiện một lần:

```java
Map<String, Long> inventory = new HashMap<>();
inventory.put("SKU-001", 10L);
inventory.put("SKU-002", 5L);

long quantity = inventory.getOrDefault("SKU-003", 0L);
```

- `HashMap`: không đảm bảo thứ tự, thao tác trung bình O(1).
- `LinkedHashMap`: giữ insertion/access order.
- `TreeMap`: sắp xếp theo key, O(log n).

### `equals()` và `hashCode()`

`HashSet` và key của `HashMap` phụ thuộc vào cả `equals()` lẫn `hashCode()`.

```java
public record ProductId(String value) {
}
```

Record tự tạo hai method phù hợp. Với class thường, nếu override `equals()` phải override `hashCode()` theo cùng dữ liệu. Không thay đổi field tham gia hai method khi object đang nằm trong hash-based collection.

## 1.3 Các method của Collection

| Method | Ý nghĩa |
|---|---|
| `add(E e)` | Thêm phần tử |
| `addAll(Collection<? extends E> c)` | Thêm toàn bộ phần tử nguồn |
| `remove(Object o)` | Xóa một phần tử bằng `equals()` |
| `removeAll(Collection<?> c)` | Xóa tất cả phần tử xuất hiện trong collection truyền vào |
| `retainAll(Collection<?> c)` | Chỉ giữ phần tử xuất hiện trong collection truyền vào |
| `contains(Object o)` | Kiểm tra một phần tử |
| `containsAll(Collection<?> c)` | Kiểm tra chứa toàn bộ |
| `clear()` | Xóa toàn bộ phần tử |
| `size()` | Số phần tử |
| `isEmpty()` | Kiểm tra rỗng |
| `removeIf(Predicate<? super E> p)` | Xóa phần tử thỏa điều kiện |

Slide dùng raw signature như `add(Object)` và `addAll(Collection)`. Khi dùng generics, method được nhìn theo type parameter `E`, an toàn hơn ở compile time.

```java
List<String> activeUsers = new ArrayList<>();
activeUsers.add("U001");

// activeUsers.add(100); // Không biên dịch
```

### Mutability không được đảm bảo

```java
List<String> immutable = List.of("A", "B");
// immutable.add("C"); // UnsupportedOperationException
```

`Arrays.asList()` có kích thước cố định: có thể `set`, nhưng không thể `add/remove`.

```java
List<String> fixedSize = Arrays.asList("A", "B");
fixedSize.set(0, "X");
// fixedSize.add("C"); // UnsupportedOperationException
```

Muốn danh sách mutable độc lập:

```java
List<String> mutable = new ArrayList<>(List.of("A", "B"));
```

## 1.4 Chọn collection phù hợp

| Nhu cầu | Lựa chọn thường dùng |
|---|---|
| Danh sách theo thứ tự, có trùng | `ArrayList` |
| Loại trùng, không cần thứ tự | `HashSet` |
| Loại trùng và giữ thứ tự thêm | `LinkedHashSet` |
| Tập hợp được sắp xếp | `TreeSet` |
| FIFO queue hoặc stack | `ArrayDeque` |
| Ưu tiên theo comparator | `PriorityQueue` |
| Tra cứu key-value | `HashMap` |
| Map giữ thứ tự thêm | `LinkedHashMap` |
| Map sắp xếp theo key | `TreeMap` |
| Map truy cập đồng thời | `ConcurrentHashMap` |

Cần xem uniqueness, ordering, kiểu truy cập, concurrency, kích thước và đặc điểm tải trước khi chọn.

### Thread safety

`ArrayList`, `HashSet`, `HashMap` không thread-safe. Nếu nhiều thread cùng thay đổi dữ liệu, dùng đồng bộ hoặc concurrent collection phù hợp.

```java
Map<String, Long> counters = new ConcurrentHashMap<>();
counters.merge("SUCCESS", 1L, Long::sum);
```

## 1.5 Ví dụ production

### Loại trùng nhưng giữ thứ tự input

```java
public List<String> normalizeProductIds(List<String> rawIds) {
    if (rawIds == null) {
        return List.of();
    }

    Set<String> uniqueIds = new LinkedHashSet<>();

    for (String rawId : rawIds) {
        if (rawId == null || rawId.isBlank()) {
            continue;
        }
        uniqueIds.add(rawId.trim().toUpperCase(Locale.ROOT));
    }

    return List.copyOf(uniqueIds);
}
```

- `LinkedHashSet` loại trùng và giữ thứ tự đầu tiên.
- `List.copyOf` trả immutable snapshot.
- `Locale.ROOT` tránh kết quả uppercase phụ thuộc máy chủ.

### Group dữ liệu theo key

```java
public Map<String, List<Order>> groupByCustomer(List<Order> orders) {
    Map<String, List<Order>> result = new HashMap<>();

    for (Order order : orders) {
        result.computeIfAbsent(
                        order.customerId(),
                        ignored -> new ArrayList<>())
                .add(order);
    }

    return result;
}
```

Trong API public, cân nhắc defensive copy sâu nếu caller không được phép sửa map và các list bên trong.

### Lưu ý mục 1

- Phân biệt `Collection` interface và `Collections` utility class.
- `Map` thuộc framework nhưng không kế thừa `Collection`.
- Collection chỉ chứa reference type; primitive được autobox sang wrapper.
- `ArrayList` là lựa chọn mặc định cho list; `ArrayDeque` thường thay `Stack`.
- Key của hash-based collection phải có `equals/hashCode` ổn định.
- Không giả định collection luôn mutable hoặc thread-safe.
- Không trả thẳng mutable collection nội bộ; dùng defensive copy hoặc immutable snapshot.

---

## 2. Generics

Generics cho phép class, interface và method nhận **type parameter**. Mục tiêu chính là type safety ở compile time và giảm cast thủ công.

```java
List<String> names = new ArrayList<>();
names.add("Java");

String first = names.get(0);
```

Nếu không có generics:

```java
List names = new ArrayList();
names.add("Java");

String first = (String) names.get(0);
```

Raw type có thể chứa nhầm kiểu và gây `ClassCastException` ở runtime.

## 2.1 Generic class, interface và method

### Generic class

```java
public class Box<T> {
    private T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
```

```java
Box<String> textBox = new Box<>();
textBox.set("Pankaj");

String value = textBox.get();
```

Compiler ngăn gán sai type:

```java
// textBox.set(10); // Không biên dịch
```

### Nhiều type parameter

```java
public record Pair<K, V>(K key, V value) {
}
```

```java
Pair<String, Long> inventory =
        new Pair<>("SKU-001", 10L);
```

### Generic interface

```java
public interface Repository<ID, E> {
    Optional<E> findById(ID id);

    E save(E entity);
}
```

```java
public class UserRepository
        implements Repository<Long, User> {
    // implementation
}
```

### Generic method

Type parameter của generic method đứng trước return type:

```java
public static <T> boolean isEqual(
        Box<T> first,
        Box<T> second) {
    return Objects.equals(first.get(), second.get());
}
```

Compiler thường suy luận được `T` từ argument.

### Invariance

`List<Integer>` không phải subtype của `List<Number>`:

```java
List<Integer> integers = List.of(1, 2, 3);

// List<Number> numbers = integers; // Không hợp lệ
```

Nếu phép gán này hợp lệ, code có thể thêm `Double` vào list thực tế chỉ chấp nhận `Integer`. Wildcard giải quyết nhu cầu đọc/ghi có kiểm soát.

## 2.2 Quy ước đặt tên type parameter

| Tên | Ý nghĩa thường dùng |
|---|---|
| `T` | Type |
| `E` | Element |
| `K` | Key |
| `V` | Value |
| `N` | Number |
| `R` | Return/Result |
| `S`, `U` | Type thứ hai, thứ ba |

```java
public interface Mapper<T, R> {
    R map(T source);
}
```

Với domain-specific type phức tạp, tên dài hơn có thể dễ hiểu. Quy ước quan trọng nhất là nhất quán và phân biệt rõ type parameter với tên class/biến.

## 2.3 Bounded type và wildcard

### Upper-bounded type parameter

```java
public static <T extends Number> double sum(List<T> numbers) {
    double result = 0.0;

    for (T number : numbers) {
        result += number.doubleValue();
    }

    return result;
}
```

```java
double integerSum = sum(List.of(1, 2, 3));
double decimalSum = sum(List.of(1.2, 2.3, 3.5));
```

`extends` trong generic bound áp dụng cho cả class lẫn interface:

```java
<T extends Comparable<T>>
```

Nếu có nhiều bound, class phải đứng trước interface:

```java
<T extends BaseEntity & Comparable<T> & Serializable>
```

### Upper-bounded wildcard

```java
public static double sumNumbers(
        List<? extends Number> numbers) {
    double result = 0.0;

    for (Number number : numbers) {
        result += number.doubleValue();
    }

    return result;
}
```

Method nhận `List<Integer>`, `List<Long>`, `List<Double>`. Có thể đọc phần tử dưới dạng `Number`, nhưng không thể thêm một `Number` cụ thể:

```java
// numbers.add(1); // Không hợp lệ
```

Lý do: list thực tế có thể là `List<Double>`.

### Lower-bounded wildcard

```java
public static void addDefaults(
        List<? super Integer> target) {
    target.add(0);
    target.add(1);
}
```

Method nhận `List<Integer>`, `List<Number>` hoặc `List<Object>`. Khi đọc, type an toàn duy nhất là `Object`:

```java
Object first = target.get(0);
```

### Unbounded wildcard

```java
public static int count(Collection<?> collection) {
    return collection.size();
}
```

`Collection<?>` nghĩa là collection của một type chưa biết, an toàn hơn raw `Collection`. Nó không giống `Collection<Object>`.

### Hiệu chỉnh thuật ngữ trên slide

- `<T extends GenericDAO>` là **bounded type parameter**, không phải wildcard.
- `? extends Number` mới là upper-bounded wildcard.
- `<T>` trước return type khai báo type parameter của generic method.
- `? super T` là lower-bounded wildcard.

## 2.4 Nguyên tắc PECS

**PECS**: Producer Extends, Consumer Super.

- Nguồn chỉ cung cấp giá trị `T`: dùng `? extends T`.
- Đích nhận giá trị `T`: dùng `? super T`.

### Producer extends

```java
public static double total(
        List<? extends Number> source) {
    double sum = 0;
    for (Number number : source) {
        sum += number.doubleValue();
    }
    return sum;
}
```

### Consumer super

```java
public static void addIds(
        Collection<? super Long> target) {
    target.add(1001L);
    target.add(1002L);
}
```

### Kết hợp producer và consumer

```java
public static <T> void copy(
        Collection<? extends T> source,
        Collection<? super T> target) {
    target.addAll(source);
}
```

```java
List<Integer> source = List.of(1, 2, 3);
List<Number> target = new ArrayList<>();

copy(source, target);
```

Nếu collection vừa sản xuất vừa tiêu thụ đúng `T`, dùng type parameter cụ thể thay vì wildcard:

```java
public static <T> void replaceFirst(
        List<T> list,
        T replacement) {
    if (!list.isEmpty()) {
        list.set(0, replacement);
    }
}
```

PECS là hướng dẫn từ góc nhìn method hiện tại, không phải thuộc tính cố định của collection.

## 2.5 Raw type

Raw type là sử dụng generic type mà không truyền type argument:

```java
List rawList = new ArrayList();
rawList.add("Text");
rawList.add(1);
```

Code biên dịch với warning nhưng mất type safety:

```java
String value = (String) rawList.get(1); // ClassCastException
```

Không nên dùng raw type trong code mới. Nếu type chưa biết, dùng wildcard:

```java
List<?> unknownList = List.of("A", "B");
Object first = unknownList.get(0);
```

Raw type chủ yếu tồn tại để tương thích code Java trước generics. Cần chuyển dữ liệu legacy sang type an toàn sớm nhất có thể.

## 2.6 Primitive, wrapper và autoboxing

| Primitive | Wrapper |
|---|---|
| `byte` | `Byte` |
| `short` | `Short` |
| `int` | `Integer` |
| `long` | `Long` |
| `float` | `Float` |
| `double` | `Double` |
| `char` | `Character` |
| `boolean` | `Boolean` |

```java
List<Integer> numbers = new ArrayList<>();
numbers.add(1); // autobox int -> Integer

int value = numbers.get(0); // unbox Integer -> int
```

### Cẩn thận `null` khi unboxing

```java
Integer quantity = null;
// int value = quantity; // NullPointerException
```

### Cẩn thận overload của `List.remove`

```java
List<Integer> numbers = new ArrayList<>(List.of(1, 2, 3));

numbers.remove(1); // Xóa phần tử tại index 1, tức giá trị 2
numbers.remove(Integer.valueOf(1)); // Xóa object có giá trị 1
```

Wrapper tạo thêm boxing overhead. Với khối lượng primitive rất lớn, array primitive hoặc specialized collection có thể phù hợp hơn.

## 2.7 Type erasure và giới hạn

Java triển khai generics bằng **type erasure**. Phần lớn thông tin type argument không tồn tại đầy đủ ở runtime.

```java
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();

System.out.println(
        strings.getClass() == integers.getClass()); // true
```

Không thể:

```java
// new T();
// T[] values = new T[10];
// if (value instanceof T) { }
// List<int> values;
```

Không overload chỉ dựa trên generic argument vì erasure tạo cùng signature:

```java
// void process(List<String> values) { }
// void process(List<Integer> values) { }
```

Khi cần tạo object generic, truyền factory:

```java
public static <T> T create(Supplier<T> factory) {
    return factory.get();
}
```

```java
User user = create(User::new);
```

### Lưu ý mục 2

- Generics cung cấp type safety ở compile time và giảm cast thủ công.
- `List<Child>` không phải `List<Parent>`; generics là invariant.
- Phân biệt type parameter `<T extends X>` với wildcard `? extends X`.
- Dùng PECS: nguồn đọc dùng `extends`, đích ghi dùng `super`.
- Dùng `Collection<?>` khi type chưa biết, không dùng raw `Collection`.
- Generic type không nhận primitive; dùng wrapper và lưu ý autoboxing/unboxing.
- Type erasure giới hạn `new T()`, generic array, `instanceof T` và overload theo type argument.
- Type signature phải giúp caller an toàn hơn, không nên phức tạp không cần thiết.

---

## 3. Lambda expressions

Lambda expression là cách viết ngắn gọn implementation của **functional interface**. Lambda có thể được truyền như argument, trả về từ method hoặc lưu trong biến.

Cú pháp:

```java
(parameterList) -> expression
```

hoặc:

```java
(parameterList) -> {
    // statements
}
```

Lambda được bổ sung từ Java 8.

## 3.1 Functional interface và cú pháp lambda

Functional interface có đúng **một abstract method**. Nó vẫn có thể chứa nhiều default/static method.

```java
@FunctionalInterface
public interface Task {
    void execute();

    default void logStart() {
        System.out.println("Task started");
    }
}
```

Tạo implementation bằng lambda:

```java
Task task = () ->
        System.out.println("Setup environment");

task.execute();
```

Tương đương anonymous class:

```java
Task task = new Task() {
    @Override
    public void execute() {
        System.out.println("Setup environment");
    }
};
```

### Hiệu chỉnh ví dụ trên slide

Slide khai báo interface `Test` với abstract method `setup()` và default method `run()`, nhưng lambda lại được gọi bằng `test.run()`. Lambda chỉ triển khai `setup()`; gọi `run()` sẽ chạy default method, không chạy body lambda.

```java
@FunctionalInterface
interface Test {
    void setup();

    default void run() {
        System.out.println("Hello Tester");
    }
}
```

```java
Test test = () ->
        System.out.println("Setup environment");

test.setup(); // Chạy lambda
test.run();   // Chạy default method
```

`@FunctionalInterface` không bắt buộc nhưng nên dùng để compiler bảo vệ contract.

### Target typing

Lambda không có type độc lập; type được suy ra từ functional interface đích:

```java
Predicate<String> notBlank = value -> !value.isBlank();
Consumer<String> printer = value -> System.out.println(value);
```

## 3.2 Các dạng lambda thường dùng

### Không có parameter

```java
Runnable task = () -> System.out.println("Running");
```

### Một parameter

```java
Consumer<String> printer =
        value -> System.out.println(value);
```

Dấu ngoặc có thể bỏ khi có đúng một parameter và không khai báo type.

### Nhiều parameter

```java
BinaryOperator<Integer> add = (left, right) -> left + right;
```

### Expression trả về giá trị

```java
Function<String, Integer> length = value -> value.length();
```

Không cần `return` với một expression.

### Block body

```java
Function<String, String> normalize = value -> {
    String trimmed = value.trim();
    return trimmed.toUpperCase(Locale.ROOT);
};
```

Block trả giá trị phải dùng `return` trên mọi nhánh cần thiết.

### `forEach`

```java
List<String> names = List.of("Java", "Spring", "Hibernate");

names.forEach(name -> System.out.println(name));
```

Method reference ngắn hơn:

```java
names.forEach(System.out::println);
```

### Comparator

```java
List<User> users = new ArrayList<>(source);

users.sort(Comparator.comparing(
        User::name,
        String.CASE_INSENSITIVE_ORDER));
```

### Tạo thread

```java
Thread thread = new Thread(() -> processJob());
thread.start();
```

Trong production, không nên tạo thread tùy ý cho từng request. Thường dùng `ExecutorService`, virtual thread hoặc concurrency abstraction được quản lý phù hợp với phiên bản Java và framework.

### Biến local phải effectively final

```java
String prefix = "USER:";

Function<String, String> formatter =
        value -> prefix + value;
```

`prefix` không cần khai báo `final`, nhưng không được gán lại sau đó nếu lambda capture nó. Lambda capture reference, không biến mutable object thành immutable; cần cẩn thận side effect và race condition.

## 3.3 Method reference

Method reference là cú pháp ngắn khi lambda chỉ gọi một method có sẵn.

| Dạng | Cú pháp | Ví dụ |
|---|---|---|
| Static method | `Type::staticMethod` | `Integer::parseInt` |
| Method của object cụ thể | `object::method` | `System.out::println` |
| Method của object bất kỳ thuộc type | `Type::instanceMethod` | `String::trim` |
| Constructor | `Type::new` | `ArrayList::new` |

```java
Function<String, Integer> parser = Integer::parseInt;
Consumer<String> printer = System.out::println;
Function<String, String> trimmer = String::trim;
Supplier<List<String>> listFactory = ArrayList::new;
```

Không cần ép dùng method reference nếu lambda rõ hơn, đặc biệt khi cần đổi thứ tự argument hoặc thêm điều kiện.

## 3.4 Các functional interface chuẩn

Package `java.util.function` cung cấp nhiều contract chuẩn:

| Interface | Abstract method | Ý nghĩa |
|---|---|---|
| `Predicate<T>` | `boolean test(T t)` | Kiểm tra điều kiện |
| `Function<T,R>` | `R apply(T t)` | Biến đổi T thành R |
| `Consumer<T>` | `void accept(T t)` | Tiêu thụ T, không trả kết quả |
| `Supplier<T>` | `T get()` | Cung cấp T, không nhận input |
| `UnaryOperator<T>` | `T apply(T t)` | T -> T |
| `BinaryOperator<T>` | `T apply(T a, T b)` | (T, T) -> T |

```java
Predicate<Order> paid = Order::isPaid;
Function<Order, String> toId = Order::id;
Consumer<Order> audit = order -> auditService.log(order.id());
Supplier<Instant> now = clock::instant;
UnaryOperator<String> normalize = String::trim;
BinaryOperator<BigDecimal> add = BigDecimal::add;
```

Ưu tiên interface chuẩn thay vì tạo custom interface có cùng signature.

## 3.5 Predicate

`Predicate<T>` kiểm tra một object và trả `boolean`:

```java
Predicate<Integer> positive = value -> value > 0;
System.out.println(positive.test(10)); // true
```

### Method filter tổng quát

```java
public static <T> List<T> filter(
        List<T> source,
        Predicate<? super T> predicate) {

    List<T> result = new ArrayList<>();

    for (T element : source) {
        if (predicate.test(element)) {
            result.add(element);
        }
    }

    return result;
}
```

```java
List<Integer> evenNumbers = filter(
        List.of(1, 2, 3, 4, 5),
        value -> value % 2 == 0);
```

`Predicate<? super T>` linh hoạt hơn `Predicate<T>` vì predicate của supertype cũng có thể tiêu thụ `T` theo PECS.

### Kết hợp predicate

```java
Predicate<String> notNull = Objects::nonNull;
Predicate<String> notBlank = value -> !value.isBlank();
Predicate<String> valid = notNull.and(notBlank);
```

Các method hỗ trợ: `and`, `or`, `negate` và `Predicate.not`. Thứ tự quan trọng vì `and/or` short-circuit; kiểm tra `notNull` trước giúp bước sau không nhận `null`.

## 3.6 Ví dụ production

### Lọc order theo điều kiện động

```java
public record OrderFilter(
        String customerId,
        OrderStatus status,
        BigDecimal minimumTotal) {
}
```

```java
public List<Order> filterOrders(
        List<Order> orders,
        OrderFilter filter) {

    Predicate<Order> predicate = order -> true;

    if (filter.customerId() != null) {
        predicate = predicate.and(order ->
                filter.customerId().equals(order.customerId()));
    }

    if (filter.status() != null) {
        predicate = predicate.and(order ->
                filter.status() == order.status());
    }

    if (filter.minimumTotal() != null) {
        predicate = predicate.and(order ->
                order.total().compareTo(filter.minimumTotal()) >= 0);
    }

    return orders.stream()
            .filter(predicate)
            .toList();
}
```

- Mỗi điều kiện là một `Predicate<Order>`.
- `and()` kết hợp điều kiện mà không tạo nhiều nhánh lọc lồng nhau.
- `Stream.toList()` trả unmodifiable list trên Java hiện đại.

Với dữ liệu lớn trong database, không tải toàn bộ về memory chỉ để lọc bằng stream. Nên chuyển filter thành query ở tầng repository.

### Tránh side effect trong lambda

Không nên:

```java
List<String> ids = new ArrayList<>();
orders.parallelStream().forEach(order -> ids.add(order.id()));
```

`ArrayList` không thread-safe và side effect khiến pipeline khó reasoning. Dùng phép biến đổi không side effect:

```java
List<String> ids = orders.stream()
        .map(Order::id)
        .toList();
```

### Lưu ý mục 3

- Lambda chỉ triển khai functional interface có đúng một abstract method.
- Dùng `@FunctionalInterface` để compiler bảo vệ contract.
- Lambda trên slide triển khai `setup()`, không phải default method `run()`.
- Ưu tiên functional interface chuẩn trong `java.util.function`.
- Dùng method reference khi nó thực sự làm code dễ đọc hơn.
- Biến local được capture phải final hoặc effectively final.
- Tránh side effect, mutable shared state và blocking operation trong lambda khó kiểm soát.
- Không mặc định dùng `parallelStream()`; cần đo hiệu năng và hiểu execution context.
- Với dữ liệu database lớn, đẩy filter/sort/aggregation xuống query thay vì xử lý toàn bộ trong memory.