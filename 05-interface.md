# Java Core 05 - Working with Interfaces

> Tổng hợp từ slide 2 đến slide 13 của phần Interface. Nội dung gồm khai báo, triển khai, quan hệ giữa class và interface, nested interface, `default`/`static` method và xử lý xung đột.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Introduction to interface](#1-introduction-to-interface)
  - [1.1 Cách sử dụng interface](#11-cách-sử-dụng-interface)
  - [1.2 Quan hệ giữa class và interface](#12-quan-hệ-giữa-class-và-interface)
  - [1.3 So sánh interface và abstract class](#13-so-sánh-interface-và-abstract-class)
  - [1.4 Nested interface](#14-nested-interface)
- [2. New features for working with interface](#2-new-features-for-working-with-interface)
  - [2.1 Default method](#21-default-method)
  - [2.2 Static method](#22-static-method)
  - [2.3 Xử lý xung đột default method](#23-xử-lý-xung-đột-default-method)
  - [2.4 Private method trong interface](#24-private-method-trong-interface)
  - [2.5 Ví dụ production: Payment Gateway](#25-ví-dụ-production-payment-gateway)
- [3. Practice](#3-practice)

---

## Mục tiêu

Sau phần này, cần nắm được:

1. Interface đại diện cho contract/capability trong Java.
2. Cách class `implements` interface và interface `extends` interface khác.
3. Khi nào nên chọn interface hoặc abstract class.
4. Cách dùng nested interface, `default`, `static` và `private` method.
5. Cách xử lý xung đột khi nhiều interface cung cấp cùng default method.
6. Cách dùng interface để giảm coupling trong code production.

---

## 1. Introduction to interface

Interface là một **contract** mô tả object có thể làm gì. Nó tập trung vào hành vi được cung cấp cho caller, không quyết định toàn bộ cách implementation bên trong hoạt động.

```java
public interface Movable {
    void move();
}
```

Class triển khai contract bằng `implements`:

```java
public class Person implements Movable {
    @Override
    public void move() {
        System.out.println("Person is walking");
    }
}

public class Cat implements Movable {
    @Override
    public void move() {
        System.out.println("Cat is running");
    }
}
```

Caller có thể làm việc qua interface:

```java
List<Movable> objects = List.of(new Person(), new Cat());

for (Movable object : objects) {
    object.move();
}
```

Kết quả:

```text
Person is walking
Cat is running
```

Đây là runtime polymorphism: cùng lời gọi `move()` nhưng implementation phụ thuộc object thực tế.

### Interface không phải “empty shell”

Mô tả trên slide rằng interface chỉ là vỏ rỗng và chỉ có abstract/default method đã lỗi thời. Interface hiện đại có thể chứa:

- Abstract method.
- `default` method có body, từ Java 8.
- `static` method có body, từ Java 8.
- `private` instance/static method có body, từ Java 9.
- Hằng số `public static final`.
- Nested class, interface, enum hoặc record.

Interface không có:

- Constructor.
- Instance field mang trạng thái riêng cho từng object.
- Khả năng tự tạo instance bằng `new`.

### Cú pháp khai báo

```java
[access_modifier] interface InterfaceName {
    // constants, methods, nested types
}
```

Top-level interface chỉ có thể là `public` hoặc package-private. Không thể khai báo top-level interface là `private` hoặc `protected`.

Ví dụ:

```java
public interface Active {
    void run();

    void makeSound();
}
```

Tên `Actives` trên slide vẫn có thể biên dịch, nhưng interface biểu diễn capability thường nên đặt bằng tính từ hoặc danh từ rõ nghĩa như `Active`, `Runnable`, `Movable`, `Soundable`.

### Modifier ngầm định

Abstract method không ghi modifier vẫn là `public abstract`:

```java
public interface Active {
    void run();
}
```

Tương đương:

```java
public interface Active {
    public abstract void run();
}
```

Field trong interface luôn là `public static final`:

```java
public interface HttpStatus {
    int OK = 200;
}
```

Tương đương:

```java
public interface HttpStatus {
    public static final int OK = 200;
}
```

Do đó field interface phải được khởi tạo và không thể gán lại:

```java
System.out.println(HttpStatus.OK);

// HttpStatus.OK = 201; // Không biên dịch
```

Không nên đặt state thay đổi trong interface. Nếu nhiều implementation cần dùng chung state, cân nhắc abstract class hoặc composition.

## 1.1 Cách sử dụng interface

### Class implements một interface

```java
public class Person implements Active {
    @Override
    public void run() {
        System.out.println("Person runs");
    }

    @Override
    public void makeSound() {
        System.out.println("Hello");
    }
}
```

Method implementation phải là `public`. Không thể giảm quyền truy cập của method `public` trong interface:

```java
public class Cat implements Active {
    @Override
    public void run() {
        System.out.println("Cat runs");
    }

    @Override
    public void makeSound() {
        System.out.println("Meow");
    }
}
```

Nếu concrete class không implement đầy đủ abstract method, code không biên dịch. Class đó phải được đánh dấu `abstract`:

```java
public abstract class BaseActive implements Active {
    @Override
    public void run() {
        System.out.println("Running");
    }

    // Chưa implement makeSound(), nên class phải abstract
}
```

### Một class implements nhiều interface

```java
public class Dog extends Animal
        implements Movable, Smellable, Swimmable {

    @Override
    public void move() {
        System.out.println("Dog moves");
    }

    @Override
    public void smell() {
        System.out.println("Dog detects a smell");
    }

    @Override
    public void swim() {
        System.out.println("Dog swims");
    }
}
```

Một class có thể đồng thời:

- `extends` tối đa một class.
- `implements` một hoặc nhiều interface.

Thứ tự cú pháp luôn là `extends` trước `implements`:

```java
class Dog extends Animal implements Movable, Swimmable {
}
```

### Interface extends interface

Interface dùng `extends`, không dùng `implements`, khi kế thừa interface khác:

```java
public interface Readable {
    String read();
}

public interface Writable {
    void write(String content);
}

public interface ReadWriteRepository
        extends Readable, Writable {
}
```

Khác với class, một interface có thể `extends` nhiều interface.

### Interface không extends class

Những quan hệ hợp lệ:

| Bên trái | Từ khóa | Bên phải |
|---|---|---|
| Class | `extends` | Một class |
| Class | `implements` | Một hoặc nhiều interface |
| Interface | `extends` | Một hoặc nhiều interface |

Interface không `extends` hoặc `implements` class.

## 1.2 Quan hệ giữa class và interface

Interface thường biểu diễn capability độc lập với cây class.

Ví dụ:

```java
public interface Movable {
    void move();
}

public interface Swimmable {
    void swim();
}
```

Các class rất khác nhau vẫn có thể cung cấp cùng capability:

```java
public class Dog extends Animal implements Movable, Swimmable {
    @Override
    public void move() {
        System.out.println("Dog runs");
    }

    @Override
    public void swim() {
        System.out.println("Dog swims");
    }
}

public class Boat extends Vehicle implements Movable, Swimmable {
    @Override
    public void move() {
        System.out.println("Boat moves on water");
    }

    @Override
    public void swim() {
        System.out.println("Boat floats and travels");
    }
}
```

Caller có thể phụ thuộc vào capability cần dùng thay vì concrete class:

```java
public void startMoving(Movable movable) {
    movable.move();
}
```

```java
startMoving(new Dog());
startMoving(new Boat());
```

### Multiple inheritance thông qua interface

Java không cho class kế thừa nhiều class, nhưng cho phép class implement nhiều interface. Điều này cung cấp nhiều contract mà không nhận nhiều bộ instance state.

```java
public class SmartDevice
        implements Connectable, Updatable, Monitorable {
    // Implement các contract
}
```

Nếu các interface chỉ có abstract method, class cung cấp implementation duy nhất nên không có diamond conflict về code. Khi nhiều interface có `default method` cùng signature, class phải xử lý xung đột; nội dung này nằm ở mục 2.3.

### Interface Segregation Principle

Không nên gom quá nhiều trách nhiệm không liên quan vào một interface lớn:

```java
public interface Worker {
    void work();
    void eat();
    void sleep();
    void submitTax();
}
```

Nên tách contract theo nhu cầu caller:

```java
public interface Workable {
    void work();
}

public interface Taxable {
    void submitTax();
}
```

Implementation chỉ nhận contract thực sự phù hợp. Interface nhỏ cũng dễ mock, test và tái sử dụng hơn.

## 1.3 So sánh interface và abstract class

| Tiêu chí | Interface | Abstract class |
|---|---|---|
| Mục đích chính | Contract/capability | Base class dùng chung state và behavior |
| Constructor | Không có | Có |
| Instance field | Không có | Có |
| Field khai báo trực tiếp | Luôn `public static final` | Có thể là instance/static với mọi modifier hợp lệ |
| Abstract method | Có | Có |
| Method có body | `default`, `static`, `private` | Instance/static method bình thường |
| Kế thừa nhiều type | Interface có thể extends nhiều interface; class implements nhiều interface | Class chỉ extends một class |
| Access của abstract method | Thường là `public` | Có thể `public`, `protected` hoặc package-private |
| Chia sẻ state | Không phù hợp | Phù hợp |

### Chọn interface khi

- Nhiều class không cùng cây kế thừa cần chung một capability.
- Caller chỉ cần một contract nhỏ và ổn định.
- Cần dễ thay implementation, mock hoặc fake trong test.
- Muốn class vẫn có thể kế thừa một base class khác.

### Chọn abstract class khi

- Các subclass có quan hệ IS-A chặt chẽ.
- Cần dùng chung instance state hoặc constructor logic.
- Cần `protected` helper/member cho subclass.
- Có nhiều behavior dùng chung và kiểm soát evolution trong cùng hierarchy.

### Có thể kết hợp cả hai

```java
public interface MessageSender {
    void send(String destination, String message);
}

public abstract class BaseMessageSender implements MessageSender {
    protected final AuditLogger auditLogger;

    protected BaseMessageSender(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    protected void audit(String destination) {
        auditLogger.log(destination);
    }
}
```

Interface là contract cho caller, abstract class là lựa chọn tái sử dụng code cho một số implementation. Caller không cần phụ thuộc vào abstract class.

## 1.4 Nested interface

Interface có thể khai báo bên trong interface hoặc class khác.

### Interface bên trong interface

```java
public interface Showable {
    void show();

    interface Message {
        void printMessage();
    }
}
```

Nested interface trong interface ngầm định là `public static`. Tham chiếu bằng tên đầy đủ:

```java
public class ConsoleMessage implements Showable.Message {
    @Override
    public void printMessage() {
        System.out.println("Hello nested interface");
    }
}
```

```java
Showable.Message message = new ConsoleMessage();
message.printMessage();
```

### Interface bên trong class

```java
public class ApiClient {
    public interface Listener {
        void onResponse(String body);
    }
}
```

```java
ApiClient.Listener listener = body ->
        System.out.println("Response: " + body);
```

Nested interface hữu ích khi contract chỉ có ý nghĩa trong phạm vi type bao ngoài, ví dụ listener, callback hoặc strategy cục bộ. Nếu contract được dùng rộng rãi trong nhiều module, top-level interface thường dễ tìm và tái sử dụng hơn.

### Lưu ý mục 1

- Interface là contract/capability, không phải class và không thể tạo instance trực tiếp.
- Interface hiện đại không phải “empty shell”; nó có thể chứa abstract, default, static và private method.
- Field trong interface luôn là `public static final`, không phải instance state.
- Class dùng `implements`; interface kế thừa interface khác bằng `extends`.
- Class chỉ extends một class nhưng có thể implements nhiều interface.
- Method triển khai abstract method của interface phải là `public`.
- Ưu tiên interface nhỏ, tập trung vào một nhóm trách nhiệm liên quan.
- Chọn abstract class khi cần chia sẻ state/constructor; chọn interface khi cần contract linh hoạt giữa nhiều hierarchy.

---

## 2. New features for working with interface

Trước Java 8, interface chủ yếu chứa abstract method và constant. Java 8 bổ sung `default` và `static` method để interface có thể phát triển mà vẫn hỗ trợ implementation cũ. Java 9 bổ sung `private` method để tái sử dụng logic nội bộ.

## 2.1 Default method

Default method có body và dùng từ khóa `default`:

```java
public interface Auditable {
    default void audit(String action) {
        System.out.println("AUDIT: " + action);
    }
}
```

Class triển khai tự động nhận default implementation:

```java
public class OrderService implements Auditable {
    public void createOrder() {
        audit("CREATE_ORDER");
    }
}
```

Class có thể override default method:

```java
public class StructuredOrderService implements Auditable {
    @Override
    public void audit(String action) {
        System.out.println("{\"action\":\"" + action + "\"}");
    }
}
```

### Mục đích chính

Default method cho phép thêm hành vi mới vào interface mà không bắt mọi implementation hiện có phải sửa ngay.

```java
public interface Repository<T> {
    T findById(String id);

    default boolean existsById(String id) {
        return findById(id) != null;
    }
}
```

Tuy nhiên, default method không nên biến interface thành nơi chứa quá nhiều business logic. Logic cần state, dependency hoặc thay đổi thường xuyên thường phù hợp hơn với class/service riêng.

### Gọi default method của interface cha

```java
public interface Named {
    default String label() {
        return "unknown";
    }
}

public class Product implements Named {
    @Override
    public String label() {
        return "product:" + Named.super.label();
    }
}
```

Cú pháp là `InterfaceName.super.method()`.

## 2.2 Static method

Static method thuộc về interface, không thuộc instance implementation:

```java
public interface EmailValidator {
    static boolean isValid(String email) {
        return email != null
                && email.contains("@");
    }
}
```

Gọi bằng tên interface:

```java
boolean valid = EmailValidator.isValid("user@example.com");
```

Không gọi qua implementation:

```java
class UserValidator implements EmailValidator {
}

// UserValidator.isValid("..."); // Không hợp lệ
```

Static method của interface **không được kế thừa** bởi implementing class. Đây là điểm khác với default method.

Static method phù hợp với:

- Factory/helper gắn chặt với contract.
- Validation đơn giản không cần state.
- Chuyển đổi dữ liệu liên quan trực tiếp đến type.

Ví dụ factory:

```java
public interface Identifier {
    String value();

    static Identifier of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Identifier is blank");
        }
        return () -> value;
    }
}
```

```java
Identifier identifier = Identifier.of("ORD-1001");
```

## 2.3 Xử lý xung đột default method

Hai interface có thể khai báo default method cùng signature:

```java
public interface InterfaceA {
    default void display() {
        System.out.println("Interface A");
    }
}

public interface InterfaceB {
    default void display() {
        System.out.println("Interface B");
    }
}
```

Class implement cả hai phải override để giải quyết xung đột:

```java
public class DisplayService
        implements InterfaceA, InterfaceB {

    @Override
    public void display() {
        InterfaceA.super.display();
        InterfaceB.super.display();
        System.out.println("DisplayService");
    }
}
```

Không override sẽ gây compile error vì compiler không thể tự chọn implementation.

### Ba quy tắc ưu tiên

1. **Class thắng interface**: instance method từ superclass được ưu tiên hơn default method cùng signature.
2. **Interface cụ thể hơn thắng**: default method từ subinterface được ưu tiên hơn interface cha.
3. **Hai interface không liên quan**: class phải override và tự giải quyết.

### Class thắng interface

```java
class Parent {
    public void display() {
        System.out.println("Parent");
    }
}

interface Displayable {
    default void display() {
        System.out.println("Displayable");
    }
}

class Child extends Parent implements Displayable {
}
```

```java
new Child().display(); // Parent
```

### Interface cụ thể hơn thắng

```java
interface BaseDisplay {
    default void display() {
        System.out.println("Base");
    }
}

interface DetailedDisplay extends BaseDisplay {
    @Override
    default void display() {
        System.out.println("Detailed");
    }
}

class Report implements BaseDisplay, DetailedDisplay {
}
```

```java
new Report().display(); // Detailed
```

## 2.4 Private method trong interface

Từ Java 9, interface có thể dùng private method để chia sẻ logic giữa default/static method mà không làm lộ helper ra contract công khai.

```java
public interface Loggable {
    default void info(String message) {
        log("INFO", message);
    }

    default void error(String message) {
        log("ERROR", message);
    }

    private void log(String level, String message) {
        System.out.printf("[%s] %s%n", level, message);
    }
}
```

Private method:

- Phải có body.
- Không được gọi từ implementing class.
- Không được override.
- Chỉ phục vụ code bên trong interface.

Interface cũng có thể có `private static` method.

## 2.5 Ví dụ production: Payment Gateway

Giả sử checkout cần thanh toán qua nhiều nhà cung cấp. Service nên phụ thuộc vào contract thay vì SDK cụ thể.

### Contract

```java
import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentResult charge(PaymentRequest request);

    default boolean supports(String currency) {
        return "VND".equalsIgnoreCase(currency);
    }
}
```

```java
public record PaymentRequest(
        String orderId,
        BigDecimal amount,
        String currency) {
}
```

```java
public record PaymentResult(
        boolean successful,
        String transactionId,
        String message) {
}
```

### Production implementation

```java
public final class BankPaymentGateway
        implements PaymentGateway {

    private final BankClient bankClient;

    public BankPaymentGateway(BankClient bankClient) {
        this.bankClient = bankClient;
    }

    @Override
    public PaymentResult charge(PaymentRequest request) {
        BankResponse response = bankClient.charge(
                request.orderId(),
                request.amount(),
                request.currency());

        return new PaymentResult(
                response.isSuccessful(),
                response.transactionId(),
                response.message());
    }
}
```

### Service phụ thuộc vào interface

```java
public final class CheckoutService {
    private final PaymentGateway paymentGateway;

    public CheckoutService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public PaymentResult checkout(PaymentRequest request) {
        if (!paymentGateway.supports(request.currency())) {
            throw new IllegalArgumentException(
                    "Unsupported currency: " + request.currency());
        }

        return paymentGateway.charge(request);
    }
}
```

### Fake implementation cho unit test

```java
public final class FakePaymentGateway
        implements PaymentGateway {

    private final PaymentResult result;

    public FakePaymentGateway(PaymentResult result) {
        this.result = result;
    }

    @Override
    public PaymentResult charge(PaymentRequest request) {
        return result;
    }
}
```

```java
PaymentGateway gateway = new FakePaymentGateway(
        new PaymentResult(true, "TX-001", "OK"));

CheckoutService service = new CheckoutService(gateway);
```

Lợi ích:

- `CheckoutService` không phụ thuộc trực tiếp vào SDK ngân hàng.
- Có thể thay ngân hàng hoặc thêm implementation khác.
- Unit test không gọi network thật.
- Contract giới hạn chính xác những gì checkout cần.

Đây là ứng dụng của Dependency Inversion Principle: business logic phụ thuộc vào abstraction, còn integration chi tiết triển khai abstraction đó.

### Lưu ý mục 2

- `default` và `static` method có từ Java 8; `private` method có từ Java 9.
- Default method được kế thừa và có thể override; static method phải gọi bằng tên interface và không được implementing class kế thừa.
- Khi hai interface không liên quan có cùng default method, implementing class bắt buộc override.
- Dùng `InterfaceName.super.method()` để gọi default implementation cụ thể.
- Default method giúp evolution contract nhưng không nên chứa business logic lớn hoặc phụ thuộc nhiều state.
- Private method giúp tái sử dụng logic nội bộ mà không mở rộng public API.
- Trong production, inject interface vào service để thay implementation và test dễ hơn.

---

## 3. Practice

### Bài thực hành: Notification Sender

Xây dựng contract gửi thông báo:

```java
public interface NotificationSender {
    void send(String recipient, String message);
}
```

Yêu cầu:

1. Tạo `EmailNotificationSender` và `SmsNotificationSender`.
2. Mỗi class override `send()` và kiểm tra input cần thiết.
3. Tạo `NotificationService` nhận `NotificationSender` qua constructor.
4. Viết `FakeNotificationSender` lưu message vào danh sách để kiểm tra trong unit test.
5. Bổ sung default method `sendWelcome(String recipient)` vào interface.
6. Quan sát rằng implementation cũ không cần sửa nếu chấp nhận default behavior.

Khung service:

```java
public final class NotificationService {
    private final NotificationSender sender;

    public NotificationService(NotificationSender sender) {
        this.sender = sender;
    }

    public void notifyOrderCreated(
            String recipient,
            String orderId) {
        sender.send(
                recipient,
                "Order " + orderId + " was created");
    }
}
```

### Lưu ý mục 3

- Bài thực hành nên giữ `NotificationService` độc lập với Email/SMS cụ thể.
- Không dùng `instanceof` để phân nhánh theo implementation; polymorphism phải chịu trách nhiệm chọn hành vi.
- Fake trong unit test nên chạy hoàn toàn trong memory và không gửi request ra ngoài.
- Validation chung nhỏ có thể dùng default/private method; validation phụ thuộc từng kênh nên nằm trong implementation tương ứng.
