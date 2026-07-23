# Java Core 04 - Working with Inheritance

> Tổng hợp từ slide 2 đến slide 30 của phần Inheritance. Nội dung gồm giới thiệu kế thừa, subclass, `super`, overriding, overloading, polymorphism và ép kiểu object.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Introduction to inheritance](#1-introduction-to-inheritance)
  - [1.1 Quan hệ kế thừa](#11-quan-hệ-kế-thừa)
  - [1.2 Superclass và subclass](#12-superclass-và-subclass)
  - [1.3 Các dạng kế thừa](#13-các-dạng-kế-thừa)
- [2. Basic skills for working with inheritance](#2-basic-skills-for-working-with-inheritance)
  - [2.1 Khai báo subclass và phạm vi truy cập](#21-khai-báo-subclass-và-phạm-vi-truy-cập)
  - [2.2 Từ khóa super](#22-từ-khóa-super)
  - [2.3 Method overriding](#23-method-overriding)
  - [2.4 Method và constructor overloading](#24-method-và-constructor-overloading)
  - [2.5 Polymorphism](#25-polymorphism)
  - [2.6 Ví dụ polymorphism hoàn chỉnh](#26-ví-dụ-polymorphism-hoàn-chỉnh)
  - [2.7 Overloading và overriding khác nhau thế nào](#27-overloading-và-overriding-khác-nhau-thế-nào)
  - [2.8 Upcasting và downcasting](#28-upcasting-và-downcasting)
- [3. Abstract và final](#3-abstract-và-final)
- [4. Practice](#4-practice)

---

## Mục tiêu

Sau phần này, cần nắm được:

1. Mục đích và các dạng kế thừa trong Java.
2. Cách tạo subclass, dùng `super`, override và overload method.
3. Cách polymorphism và dynamic dispatch hoạt động.
4. Cách upcast, downcast object an toàn.
5. Phần `abstract`, `final` và bài thực hành sẽ được bổ sung từ các slide tiếp theo.

---

## 1. Introduction to inheritance

**Inheritance** (kế thừa) là cơ chế cho phép một class mới tái sử dụng và mở rộng cấu trúc, hành vi của một class đã có.

- Class được kế thừa gọi là **superclass**, **parent class** hoặc **base class**.
- Class kế thừa gọi là **subclass**, **child class** hoặc **derived class**.
- Trong Java, class con dùng từ khóa `extends`.

Ví dụ:

```java
public class Vehicle {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

public class Car extends Vehicle {
    private String manufacturer;

    public String getManufacturer() {
        return manufacturer;
    }
}
```

`Car` có thể sử dụng các method `public` được kế thừa từ `Vehicle`:

```java
Car car = new Car();
car.setName("City");
System.out.println(car.getName());
```

### Kế thừa không có nghĩa là lấy được mọi thứ

Câu trên slide cho rằng object con nhận **tất cả** thuộc tính và hành vi của object cha là chưa hoàn toàn chính xác:

- Constructor của superclass **không được kế thừa**.
- Member `private` tồn tại trong phần trạng thái superclass của object, nhưng subclass không truy cập trực tiếp được.
- Method `final` được kế thừa nhưng không được override.
- Method `static` có thể được truy cập qua subclass nhưng không tham gia runtime polymorphism; nó bị **hiding**, không phải overriding.

Kế thừa cũng không đồng nghĩa với việc sao chép code của superclass sang subclass. JVM vẫn quản lý các class riêng, còn object con chứa phần trạng thái cần thiết của chuỗi kế thừa.

## 1.1 Quan hệ kế thừa

Kế thừa nên biểu diễn quan hệ **IS-A**:

- `Car` **is a** `Vehicle`.
- `SavingsAccount` **is an** `Account`.
- `FullTimeEmployee` **is an** `Employee`.

Nếu không đọc tự nhiên theo IS-A, kế thừa thường không phù hợp.

Ví dụ quan hệ **HAS-A** nên dùng composition:

- `Car` **has an** `Engine`.
- `Order` **has a** danh sách `OrderItem`.
- `UserService` **has a** `UserRepository`.

```java
public class Engine {
    public void start() {
        System.out.println("Engine started");
    }
}

public class Car {
    private final Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }

    public void start() {
        engine.start();
    }
}
```

### Hiệu chỉnh mô hình trên slide

Slide đặt `Horse` dưới `Vehicle`. Trong mô hình thông thường, `Horse` không phải là một `Vehicle`, nên quan hệ này không phải IS-A tốt. Nếu nghiệp vụ cần gom ngựa, xe đạp, ô tô và xe máy thành phương tiện di chuyển, superclass có thể mang nghĩa rộng hơn như `Transport`:

```java
public class Transport {
    private String name;
}

public class HorseTransport extends Transport {
    private String species;
}

public class Car extends Transport {
    private String manufacturer;
}
```

Tên abstraction phải phản ánh đúng ngôn ngữ nghiệp vụ. Một cây kế thừa sai ngữ nghĩa sẽ khiến code khó hiểu dù vẫn biên dịch.

## 1.2 Superclass và subclass

Superclass chứa phần chung; subclass thêm dữ liệu hoặc chuyên biệt hóa hành vi.

```java
public class Vehicle {
    private final String name;

    public Vehicle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String display() {
        return "Vehicle{name='%s'}".formatted(name);
    }
}

public class Car extends Vehicle {
    private final String manufacturer;

    public Car(String name, String manufacturer) {
        super(name);
        this.manufacturer = manufacturer;
    }

    @Override
    public String display() {
        return super.display()
                + ", manufacturer='" + manufacturer + "'";
    }
}
```

Một object `Car` có thể được nhìn dưới hai kiểu:

```java
Car car = new Car("City", "Honda");
Vehicle vehicle = car;
```

- Kiểu thực tế của object là `Car`.
- Kiểu khai báo của biến `vehicle` là `Vehicle`.
- Method nào được phép gọi được kiểm tra theo kiểu khai báo.
- Implementation override nào chạy được quyết định theo kiểu thực tế tại runtime.

## 1.3 Các dạng kế thừa

### Single inheritance

Một subclass kế thừa trực tiếp từ một superclass:

```text
Vehicle
   |
  Car
```

```java
class Car extends Vehicle {
    // ...
}
```

### Multilevel inheritance

Chuỗi kế thừa nhiều cấp:

```text
Vehicle
   |
  Car
   |
ElectricCar
```

```java
class ElectricCar extends Car {
    // ...
}
```

Mọi class Java cuối cùng đều nằm trong một chuỗi dẫn tới `java.lang.Object`.

### Hierarchical inheritance

Nhiều subclass cùng kế thừa một superclass:

```text
       Vehicle
       /     \
     Car    Bicycle
```

```java
class Car extends Vehicle {
}

class Bicycle extends Vehicle {
}
```

### Multiple inheritance

Java **không cho phép một class extends nhiều class**:

```java
// Không hợp lệ
class AmphibiousVehicle extends Car, Boat {
}
```

Quy tắc này tránh nhiều xung đột trạng thái và hành vi, đặc biệt là diamond problem. Java hỗ trợ một class implement nhiều interface:

```java
class AmphibiousVehicle implements RoadMovable, WaterMovable {
}
```

Đây là multiple inheritance về **type/contract**, không phải đa kế thừa class.

### Lưu ý mục 1

- Chỉ dùng inheritance khi có quan hệ IS-A rõ ràng và object con thay thế được object cha.
- Dùng composition cho quan hệ HAS-A và khi cần thay đổi hành vi linh hoạt.
- Subclass không truy cập trực tiếp member `private` và không kế thừa constructor.
- Java chỉ hỗ trợ single inheritance đối với class, nhưng một class có thể implement nhiều interface.
- Tránh cây kế thừa quá sâu; nó làm tăng coupling và khiến ảnh hưởng của thay đổi khó dự đoán.
- Đặt superclass theo abstraction nghiệp vụ, không chỉ vì nhiều class tình cờ có vài field giống nhau.

---

## 2. Basic skills for working with inheritance

## 2.1 Khai báo subclass và phạm vi truy cập

### Cú pháp `extends`

```java
public class SubclassName extends SuperclassName {
    // Field và method riêng của subclass
}
```

Mỗi class chỉ được `extends` trực tiếp một class.

### Access modifier trong quan hệ kế thừa

| Modifier | Trong cùng class | Cùng package | Subclass khác package | Code khác package |
|---|---:|---:|---:|---:|
| `private` | Có | Không | Không | Không |
| package-private | Có | Có | Không | Không |
| `protected` | Có | Có | Có, với quy tắc protected | Không |
| `public` | Có | Có | Có | Có |

Package-private là trường hợp không ghi access modifier:

```java
String internalCode;
```

### Quy tắc đặc biệt của `protected`

Subclass ở package khác có thể dùng member `protected` thông qua quan hệ kế thừa, nhưng không được truy cập member đó trên một object superclass tùy ý.

```java
package parent;

public class Account {
    protected long balance;
}
```

```java
package child;

import parent.Account;

public class SavingsAccount extends Account {
    public void deposit(long amount) {
        balance += amount;        // Hợp lệ: truy cập member được kế thừa
        this.balance += amount;   // Hợp lệ
    }

    public void copy(Account other) {
        // balance = other.balance; // Không hợp lệ khi khác package
    }
}
```

Không nên đổi field thành `protected` chỉ để subclass sửa trực tiếp. Encapsulation thường tốt hơn với field `private` và method được kiểm soát:

```java
public class Account {
    private long balance;

    protected final void increaseBalance(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        balance += amount;
    }

    public long getBalance() {
        return balance;
    }
}
```

### Constructor superclass chạy trước

Khi tạo subclass, Java luôn khởi tạo phần superclass trước:

```java
public class Parent {
    public Parent() {
        System.out.println("Parent constructor");
    }
}

public class Child extends Parent {
    public Child() {
        System.out.println("Child constructor");
    }
}
```

```java
new Child();
```

Kết quả:

```text
Parent constructor
Child constructor
```

Nếu không ghi rõ, compiler chèn `super()` làm câu lệnh đầu tiên của constructor con. Nếu superclass không có no-argument constructor, subclass phải gọi constructor phù hợp bằng `super(arguments)`.

## 2.2 Từ khóa `super`

`super` là reference tới phần superclass của object hiện tại. Ba cách dùng phổ biến:

1. Gọi constructor superclass bằng `super(...)`.
2. Gọi implementation superclass bằng `super.method(...)`.
3. Truy cập field superclass bị che tên bằng `super.field`.

### Gọi constructor superclass

```java
public class Vehicle {
    private final String name;

    public Vehicle(String name) {
        this.name = name;
    }
}

public class Car extends Vehicle {
    private final String manufacturer;

    public Car(String name, String manufacturer) {
        super(name);
        this.manufacturer = manufacturer;
    }
}
```

`super(...)` phải là câu lệnh đầu tiên trong constructor:

```java
public Car(String name) {
    // System.out.println(name); // Không được đặt trước super(...)
    super(name);
}
```

Một constructor chỉ có thể bắt đầu bằng `this(...)` hoặc `super(...)`, không thể gọi trực tiếp cả hai.

### Gọi method superclass

```java
public class ParentClass {
    public void print() {
        System.out.println("Parent");
    }
}

public class Subclass extends ParentClass {
    @Override
    public void print() {
        super.print();
        System.out.println("Child");
    }
}
```

Kết quả của `new Subclass().print()`:

```text
Parent
Child
```

### Truy cập field superclass

```java
public class ParentClass {
    protected String label = "Parent";
}

public class Subclass extends ParentClass {
    private String label = "Child";

    public void printLabels() {
        System.out.println(this.label);  // Child
        System.out.println(super.label); // Parent
    }
}
```

Field hiding thường làm code khó đọc. Nên ưu tiên field `private`, tên rõ ràng và truy cập qua method thay vì khai báo trùng tên ở subclass.

### Lưu ý về constructor

- Constructor không được kế thừa và không được override.
- Constructor có thể overload.
- Nếu constructor superclass là `private`, subclass bên ngoài superclass không gọi được nó.
- Không gọi method có thể bị override từ constructor superclass; lúc đó phần subclass có thể chưa được khởi tạo xong.

## 2.3 Method overriding

**Overriding** xảy ra khi subclass cung cấp implementation mới cho một instance method đã có trong superclass.

```java
public class Human {
    public void eat() {
        System.out.println("Human is eating");
    }
}

public class Student extends Human {
    @Override
    public void eat() {
        System.out.println("Student is having lunch");
    }
}
```

Nên luôn dùng `@Override`. Compiler sẽ phát hiện sai tên, sai parameter hoặc method cha không tồn tại.

### Quy tắc overriding

1. Tên method và danh sách parameter phải giống nhau.
2. Return type phải giống hoặc là **covariant return type**.
3. Access modifier của method con không được hẹp hơn method cha.
4. Method con không được ném checked exception rộng hơn method cha.
5. Method `final` không thể override.
6. Method `private` không thể override vì không nhìn thấy từ subclass.
7. Method `static` bị hiding, không phải overriding.

Ví dụ covariant return type:

```java
class Document {
}

class Invoice extends Document {
}

class DocumentService {
    public Document create() {
        return new Document();
    }
}

class InvoiceService extends DocumentService {
    @Override
    public Invoice create() {
        return new Invoice();
    }
}
```

### Dynamic dispatch

```java
Human person = new Student();
person.eat();
```

Kết quả là implementation của `Student` được gọi. JVM chọn instance method override theo **kiểu object thực tế**, không phải kiểu biến reference.

### Gọi thêm implementation của superclass

```java
public class AuditedService extends BaseService {
    @Override
    public void execute() {
        super.execute();
        audit();
    }
}
```

Chỉ gọi `super.method()` khi subclass thật sự cần giữ hành vi cha. Không phải override nào cũng phải gọi `super`.

### Hiệu chỉnh Example 3 trên slide

Superclass trên slide có `disp()` và `abc()`, nhưng subclass lại ghi:

```java
@Override
public void XYZ() {
    // ...
}
```

Đoạn này **không biên dịch**, vì superclass không có method `XYZ()` phù hợp để override. Nếu bỏ `@Override`, `XYZ()` chỉ là method mới của subclass. Ví dụ đúng:

```java
class Test extends ABC {
    @Override
    public void disp() {
        System.out.println("disp() method of child class");
    }

    public void xyz() {
        System.out.println("xyz() belongs only to child class");
    }
}
```

Với reference cha:

```java
ABC object = new Test();
object.disp(); // Gọi Test.disp()
object.abc();  // Gọi ABC.abc()

// object.xyz(); // Không biên dịch vì ABC không khai báo xyz()
```

### Field và static method không đa hình như instance method

```java
class Parent {
    String name = "parent";

    static void identify() {
        System.out.println("Parent");
    }
}

class Child extends Parent {
    String name = "child";

    static void identify() {
        System.out.println("Child");
    }
}

Parent reference = new Child();
System.out.println(reference.name); // parent
reference.identify();               // Parent
```

Field và static method được chọn theo kiểu reference tại compile time. Tránh gọi static method qua object; dùng `Parent.identify()` để thể hiện rõ.

## 2.4 Method và constructor overloading

**Overloading** là khai báo nhiều method cùng tên trong cùng class, nhưng khác danh sách parameter.

Danh sách parameter có thể khác ở:

- Số lượng parameter.
- Kiểu dữ liệu parameter.
- Thứ tự kiểu parameter.

```java
public class DisplayService {
    public void display(char value) {
        System.out.println(value);
    }

    public void display(char value, int count) {
        System.out.println(value + " " + count);
    }

    public void display(int count, char value) {
        System.out.println(count + " " + value);
    }
}
```

Compiler chọn overload phù hợp tại compile time.

### Return type không tạo thành overload

Hai method sau không hợp lệ vì có cùng signature:

```java
public void logIt(String value) {
}

// Không hợp lệ: chỉ khác return type
public String logIt(String value) {
    return value;
}
```

Tên parameter cũng không thuộc method signature:

```java
public void logIt(String param, String error) {
}

// Không hợp lệ: vẫn là logIt(String, String)
public void logIt(String name, String message) {
}
```

### Constructor overloading

Constructor có thể cùng tên class và khác parameter:

```java
public class Car extends Vehicle {
    private final String manufacturer;

    public Car() {
        this("Unknown", "Unknown");
    }

    public Car(String manufacturer) {
        this("Unknown", manufacturer);
    }

    public Car(String name, String manufacturer) {
        super(name);
        this.manufacturer = manufacturer;
    }
}
```

Cụm từ “constructor là method overloading đặc biệt” trên slide chưa chính xác. Constructor **không phải method**: nó không có return type, không được kế thừa và không được override. Tuy nhiên, constructor dùng cơ chế overload theo danh sách parameter tương tự method.

Slide cũng viết `public class car() extends Vehicle`, đây là cú pháp sai. Khai báo đúng không có `()` và nên dùng PascalCase:

```java
public class Car extends Vehicle {
}
```

### Cẩn thận overload mơ hồ

```java
void process(String value) {
}

void process(Integer value) {
}

// process(null); // Lỗi: compiler không biết chọn overload nào
```

Autoboxing và varargs cũng có thể làm overload khó đoán. API production nên tránh quá nhiều overload gần giống nhau; đôi khi tên method rõ nghĩa hoặc builder dễ dùng hơn.

## 2.5 Polymorphism

**Polymorphism** (đa hình) cho phép cùng một lời gọi method tạo ra hành vi khác nhau tùy object thực tế.

```java
public class Animal {
    public void speak() {
        System.out.println("Unknown sound");
    }
}

public class Cat extends Animal {
    @Override
    public void speak() {
        System.out.println("Meow");
    }
}

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Woof");
    }
}
```

```java
List<Animal> animals = List.of(new Cat(), new Dog());

for (Animal animal : animals) {
    animal.speak();
}
```

Kết quả:

```text
Meow
Woof
```

Code duyệt danh sách chỉ biết `Animal`, nhưng JVM dispatch tới implementation tương ứng.

### Compile-time và runtime polymorphism

- Overloading thường được gọi là **compile-time polymorphism** vì compiler chọn method dựa trên kiểu và danh sách argument.
- Overriding tạo **runtime polymorphism** vì JVM chọn implementation dựa trên object thực tế.

### Liskov Substitution Principle

Subclass nên thay thế được superclass mà không phá vỡ kỳ vọng của caller. Nếu code dùng `Vehicle` nhưng một subclass ném lỗi cho hầu hết method cơ bản hoặc thay đổi contract bất ngờ, mô hình kế thừa có thể sai.

Ví dụ không tốt:

```java
class Bird {
    public void fly() {
    }
}

class Penguin extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguin cannot fly");
    }
}
```

Mô hình tốt hơn là tách khả năng bay thành abstraction riêng thay vì ép mọi `Bird` phải có `fly()`.

## 2.6 Ví dụ polymorphism hoàn chỉnh

Giả sử hệ thống tính phí giao hàng theo từng chính sách.

```java
public record DeliveryRequest(
        long orderValue,
        double distanceKm) {
}
```

Superclass định nghĩa hành vi chung:

```java
public class DeliveryFeePolicy {
    public long calculate(DeliveryRequest request) {
        return 30_000L;
    }
}
```

Các subclass chuyên biệt hóa công thức:

```java
public class StandardDeliveryPolicy extends DeliveryFeePolicy {
    @Override
    public long calculate(DeliveryRequest request) {
        if (request.orderValue() >= 500_000L) {
            return 0L;
        }
        return 30_000L;
    }
}

public class ExpressDeliveryPolicy extends DeliveryFeePolicy {
    @Override
    public long calculate(DeliveryRequest request) {
        return 50_000L + Math.round(request.distanceKm() * 5_000L);
    }
}
```

Service phụ thuộc vào type cha:

```java
public class CheckoutService {
    private final DeliveryFeePolicy deliveryFeePolicy;

    public CheckoutService(DeliveryFeePolicy deliveryFeePolicy) {
        this.deliveryFeePolicy = deliveryFeePolicy;
    }

    public long calculateTotal(DeliveryRequest request) {
        return request.orderValue()
                + deliveryFeePolicy.calculate(request);
    }
}
```

Chọn policy khi khởi tạo:

```java
DeliveryFeePolicy policy = new ExpressDeliveryPolicy();
CheckoutService checkoutService = new CheckoutService(policy);

long total = checkoutService.calculateTotal(
        new DeliveryRequest(600_000L, 4.5));
```

`CheckoutService` không cần `if` để phân biệt mọi subtype. Khi có policy mới, có thể thêm implementation mới mà không sửa thuật toán checkout.

Trong thiết kế production hiện đại, contract này thường được biểu diễn bằng interface hoặc abstract class. Phần đó sẽ được làm rõ ở các slide tiếp theo.

## 2.7 Overloading và overriding khác nhau thế nào

| Tiêu chí | Overloading | Overriding |
|---|---|---|
| Mục đích | Cùng thao tác với nhiều dạng input | Chuyên biệt hóa hành vi được kế thừa |
| Vị trí | Thường trong cùng class; cũng có thể liên quan method kế thừa | Giữa superclass và subclass |
| Tên method | Giống nhau | Giống nhau |
| Parameter list | Phải khác | Phải giống |
| Return type | Có thể khác, nhưng không đủ để tạo overload | Giống hoặc covariant |
| Thời điểm chọn | Compile time | Runtime với instance method |
| `@Override` | Không dùng | Nên luôn dùng |
| Access modifier | Không có quy tắc mở rộng riêng giữa overload | Không được thu hẹp quyền truy cập |

Ví dụ:

```java
class Formatter {
    String format(int value) {        // Overload 1
        return Integer.toString(value);
    }

    String format(LocalDate value) {  // Overload 2
        return value.toString();
    }
}
```

```java
class JsonFormatter extends Formatter {
    @Override
    String format(int value) {        // Override
        return "{\"value\":" + value + "}";
    }
}
```

## 2.8 Upcasting và downcasting

### Upcasting

Upcasting là gán reference subtype cho biến supertype. Đây là chuyển đổi ngầm định và an toàn:

```java
Car car = new Car("City", "Honda");
Vehicle vehicle = car;
```

Reference `vehicle` chỉ gọi trực tiếp được các member khai báo trong `Vehicle`, nhưng method override của `Car` vẫn chạy nhờ dynamic dispatch.

```java
System.out.println(vehicle.display()); // Gọi Car.display()
```

### Downcasting

Downcasting là chuyển reference supertype về subtype. Cần cast tường minh:

```java
Vehicle vehicle = new Car("City", "Honda");
Car car = (Car) vehicle;
```

Cast chỉ hợp lệ khi object thực tế đúng subtype. Đoạn sau biên dịch nhưng lỗi runtime:

```java
Vehicle vehicle = new Bicycle();
Car car = (Car) vehicle; // ClassCastException
```

### Kiểm tra bằng `instanceof`

Java hiện đại hỗ trợ pattern matching:

```java
public void printManufacturer(Vehicle vehicle) {
    if (vehicle instanceof Car car) {
        System.out.println(car.getManufacturer());
    }
}
```

Với Java cũ hơn:

```java
if (vehicle instanceof Car) {
    Car car = (Car) vehicle;
    System.out.println(car.getManufacturer());
}
```

`null instanceof Car` trả về `false`.

### Ưu tiên polymorphism thay cho nhiều phép cast

Nếu code liên tục kiểm tra subtype, abstraction có thể đang thiếu hành vi:

```java
if (vehicle instanceof Car car) {
    car.moveCar();
} else if (vehicle instanceof Bicycle bicycle) {
    bicycle.moveBicycle();
}
```

Thường nên khai báo method chung:

```java
public class Vehicle {
    public void move() {
    }
}

public class Car extends Vehicle {
    @Override
    public void move() {
        System.out.println("Car is moving");
    }
}
```

Caller chỉ cần:

```java
vehicle.move();
```

Downcasting vẫn hữu ích ở biên hệ thống hoặc khi API bắt buộc, nhưng không nên là cách điều phối nghiệp vụ chính.

### Lưu ý mục 2

- Giữ field superclass là `private`; cung cấp method có kiểm soát thay vì cho subclass sửa trạng thái tùy ý.
- `super(...)` hoặc `this(...)` phải đứng đầu constructor; nếu không ghi, compiler thử chèn `super()`.
- Luôn dùng `@Override` để compiler kiểm tra contract.
- Runtime polymorphism áp dụng cho instance method, không áp dụng theo cùng cách cho field và static method.
- Overload phải khác parameter list; đổi tên parameter hoặc return type không tạo signature mới.
- Constructor có thể overload nhưng không phải method và không thể override.
- Upcasting an toàn; downcasting có thể gây `ClassCastException`.
- Dùng `instanceof` pattern matching khi thật sự cần cast, nhưng ưu tiên đưa hành vi chung vào abstraction.
- Kế thừa tạo coupling mạnh. Hãy kiểm tra IS-A, Liskov Substitution Principle và cân nhắc composition trước khi mở rộng cây class.

---

## 3. Abstract và final

Phần này thuộc mục tiêu của chương nhưng chưa xuất hiện trong nhóm slide 2-30. Nội dung sẽ được bổ sung khi có các slide tiếp theo.

---

## 4. Practice

Bài thực hành sẽ được bổ sung theo tài liệu tiếp theo của công ty.
