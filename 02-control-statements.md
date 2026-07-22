# Java Core 02 - Control Statements

> Tổng hợp từ slide 2 đến slide 33 của phần Control Statements.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Boolean Expressions](#1-boolean-expressions)
  - [1.1 So sánh String](#11-so-sánh-string)
  - [1.2 Biểu thức boolean](#12-biểu-thức-boolean)
  - [1.3 Phép toán logic](#13-phép-toán-logic)
- [2. Selection Statements](#2-selection-statements)
  - [2.1 If statement](#21-if-statement)
  - [2.2 Switch statement](#22-switch-statement)
- [3. Loop/Iteration Statements](#3-loopiteration-statements)
  - [3.1 While statement](#31-while-statement)
  - [3.2 Do-while statement](#32-do-while-statement)
  - [3.3 For statement](#33-for-statement)
  - [3.4 Nested loops](#34-nested-loops)
- [4. Branching/Transfer Statements](#4-branchingtransfer-statements)
  - [4.1 Break statement](#41-break-statement)
  - [4.2 Continue statement](#42-continue-statement)
  - [4.3 Return statement](#43-return-statement)

## Mục tiêu

Phần này tập trung vào cách điều khiển luồng thực thi của chương trình Java:

1. Tạo và kết hợp các biểu thức boolean.
2. Chọn nhánh xử lý bằng `if`, `if-else-if` và `switch`.
3. Lặp một khối lệnh bằng `while`, `do-while` và `for`.
4. Chuyển hướng luồng bằng `break`, `continue` và `return`.

## 1. Boolean Expressions

Boolean expression là biểu thức trả về đúng một trong hai giá trị `true` hoặc `false`. Nó thường được dùng làm điều kiện cho `if`, `switch` có guard, vòng lặp và validation.

### Toán tử so sánh

| Toán tử | Ý nghĩa | Ví dụ |
|---|---|---|
| `==` | Bằng nhau | `quantity == 2` |
| `!=` | Khác nhau | `total != 0` |
| `>` | Lớn hơn | `age > 0` |
| `<` | Nhỏ hơn | `index < months` |
| `>=` | Lớn hơn hoặc bằng | `total >= 0` |
| `<=` | Nhỏ hơn hoặc bằng | `quantity <= orderLimit` |

```java
int stock = 10;
int requestedQuantity = 3;

boolean hasEnoughStock = stock >= requestedQuantity;
System.out.println(hasEnoughStock); // true
```

Các toán tử `>`, `<`, `>=`, `<=` dùng cho kiểu dữ liệu có thể so sánh theo thứ tự như số và `char`. Không thể dùng trực tiếp các toán tử này để so sánh hai `String` hay hai object thông thường.

### `==` với primitive và reference type

Với primitive, `==` so sánh giá trị:

```java
int expected = 10;
int actual = 10;

System.out.println(expected == actual); // true
```

Với reference type, `==` kiểm tra hai biến có cùng trỏ đến một object hay không, không so sánh nội dung object:

```java
String first = new String("java");
String second = new String("java");

System.out.println(first == second);      // false
System.out.println(first.equals(second)); // true
```

## 1.1 So sánh String

### `equals()`

`String.equals()` so sánh nội dung và phân biệt chữ hoa, chữ thường.

```java
String s1 = "java";
String s2 = "java";
String s3 = "JAVA";
String s4 = "ruby";

System.out.println(s1.equals(s2)); // true
System.out.println(s1.equals(s3)); // false
System.out.println(s1.equals(s4)); // false
```

Trong phần `equals()` của slide, ví dụ đang ghi nhầm thành `equalsIgnoreCase()`. Nếu muốn minh họa việc phân biệt hoa/thường thì phải gọi `equals()` như đoạn code trên.

### `equalsIgnoreCase()`

`String.equalsIgnoreCase()` so sánh nội dung nhưng bỏ qua khác biệt chữ hoa và chữ thường.

```java
System.out.println(s1.equalsIgnoreCase(s2)); // true
System.out.println(s1.equalsIgnoreCase(s3)); // true
System.out.println(s1.equalsIgnoreCase(s4)); // false
```

### So sánh String an toàn với `null`

Gọi method trên một reference `null` gây `NullPointerException`:

```java
String actualStatus = null;
// actualStatus.equals("PAID"); // NullPointerException
```

Nếu một vế là hằng số, có thể gọi `equals()` từ hằng số:

```java
if ("PAID".equals(actualStatus)) {
    System.out.println("Order was paid");
}
```

Khi cả hai vế đều có thể `null`, dùng `Objects.equals()`:

```java
import java.util.Objects;

boolean sameEmail = Objects.equals(requestEmail, customerEmail);
```

### Ví dụ production: kiểm tra trạng thái đơn hàng

Trong code domain, nên ưu tiên `enum` thay cho các chuỗi trạng thái rời rạc:

```java
public enum OrderStatus {
    CREATED,
    PAID,
    CANCELLED
}
```

```java
if (order.getStatus() == OrderStatus.PAID) {
    invoiceService.issueInvoice(order);
}
```

`enum` có thể so sánh bằng `==` vì mỗi enum constant là một instance duy nhất. Cách này tránh lỗi chính tả như `"paied"` và giúp compiler kiểm tra type.

## 1.2 Biểu thức boolean

Với biến boolean, có thể dùng trực tiếp biến đó làm điều kiện:

```java
boolean isValid = true;

if (isValid) {
    System.out.println("Valid data");
}
```

Không cần viết dài dòng:

```java
if (isValid == true) {
    System.out.println("Valid data");
}
```

Toán tử NOT `!` đảo giá trị boolean:

```java
boolean isValid = true;
System.out.println(!isValid); // false

isValid = false;
System.out.println(!isValid); // true
```

Các cách đặt tên như `isValid`, `hasPermission`, `canRetry`, `shouldNotify` giúp điều kiện đọc gần giống một câu tự nhiên:

```java
if (user.hasPermission() && order.canBeCancelled()) {
    order.cancel();
}
```

## 1.3 Phép toán logic

Giả sử:

```java
boolean a = true;
boolean b = false;
```

| Toán tử | Ý nghĩa | Kết quả ví dụ |
|---|---|---|
| `&&` | AND: đúng khi cả hai vế đúng | `a && b` là `false` |
| `\|\|` | OR: đúng khi ít nhất một vế đúng | `a \|\| b` là `true` |
| `!` | NOT: đảo giá trị boolean | `!(a && b)` là `true` |

### Short-circuit evaluation

`&&` và `||` là các toán tử short-circuit:

- Với `left && right`, nếu `left` là `false`, Java không đánh giá `right`.
- Với `left || right`, nếu `left` là `true`, Java không đánh giá `right`.

Điều này đặc biệt hữu ích khi kiểm tra `null`:

```java
if (customer != null && customer.isActive()) {
    promotionService.sendTo(customer);
}
```

Thứ tự hai vế rất quan trọng. Đoạn sau có thể gây `NullPointerException`:

```java
if (customer.isActive() && customer != null) {
    promotionService.sendTo(customer);
}
```

### Ví dụ production: điều kiện cho phép thanh toán

```java
public boolean canPay(Order order, User user) {
    return order != null
            && user != null
            && user.isActive()
            && order.belongsTo(user.getId())
            && order.getStatus() == OrderStatus.CREATED
            && order.getTotalAmount().signum() > 0;
}
```

Nếu biểu thức bắt đầu quá dài, nên tách thành các method có tên thể hiện business rule thay vì tiếp tục nối nhiều điều kiện:

```java
public boolean canPay(Order order, User user) {
    return isValidPaymentRequest(order, user)
            && isOwnedBy(order, user)
            && isPayable(order);
}
```

### Lưu ý mục 1

- Dùng `equals()` để so sánh nội dung `String`; không dùng `==` trừ khi thật sự muốn so sánh reference.
- `equals()` phân biệt hoa/thường, còn `equalsIgnoreCase()` thì không.
- Khi so sánh các trạng thái cố định, ưu tiên `enum` thay cho `String`.
- Với biến boolean, ưu tiên `if (isValid)` và `if (!isValid)` thay cho so sánh với `true`/`false`.
- Sắp xếp null check ở vế trái của `&&` để tận dụng short-circuit.
- Tách biểu thức nghiệp vụ dài thành các method có tên rõ ràng.

## 2. Selection Statements

Selection statement cho phép chương trình chọn nhánh xử lý dựa trên điều kiện. Ba dạng chính trong slide:

- `if`.
- `if-else-if`.
- `switch-case`.

## 2.1 If statement

### Cú pháp

```java
if (booleanExpression) {
    // Chạy khi điều kiện là true
} else if (anotherBooleanExpression) {
    // Chạy khi điều kiện thứ nhất false và điều kiện này true
} else {
    // Chạy khi không điều kiện nào phía trên đúng
}
```

Chỉ một nhánh trong chuỗi `if` - `else if` - `else` được thực thi. Java kiểm tra từ trên xuống và dừng ở điều kiện đầu tiên có kết quả `true`.

### Ví dụ kiểm tra số chẵn

```java
int number = 10;

if (number % 2 == 0) {
    System.out.println("This is an even number");
}
```

### Ví dụ phân loại số

Slide dùng `number >= 0` rồi in “positive number”. Điều này chưa chính xác vì `0` không phải số dương. Có thể sửa thành ba nhánh:

```java
int number = 0;

if (number > 0) {
    System.out.println("Positive number");
} else if (number < 0) {
    System.out.println("Negative number");
} else {
    System.out.println("Zero");
}
```

Nếu nghiệp vụ chỉ cần phân biệt âm và không âm, có thể viết:

```java
if (number >= 0) {
    System.out.println("Non-negative number");
} else {
    System.out.println("Negative number");
}
```

### Guard clause

Trong production, guard clause giúp giảm `if` lồng nhau và xử lý trường hợp không hợp lệ sớm:

```java
public void cancelOrder(Order order, User user) {
    if (order == null) {
        throw new IllegalArgumentException("Order is required");
    }
    if (user == null || !user.isActive()) {
        throw new AccessDeniedException("Active user is required");
    }
    if (!order.belongsTo(user.getId())) {
        throw new AccessDeniedException("Cannot cancel another user's order");
    }
    if (!order.canBeCancelled()) {
        throw new IllegalStateException("Order cannot be cancelled");
    }

    order.cancel();
}
```

So với một cây `if` lồng nhiều tầng, guard clause giúp luồng chính nằm ở cuối method và dễ đọc hơn.

### Có nên bỏ dấu `{}` khi chỉ có một statement?

Java cho phép:

```java
if (isValid)
    process();
```

Tuy nhiên, production code nên giữ dấu `{}` để hạn chế bug khi thêm statement mới:

```java
if (isValid) {
    process();
}
```

## 2.2 Switch statement

### Cú pháp switch truyền thống

```java
switch (expression) {
    case value1:
        // statements
        break;
    case value2:
        // statements
        break;
    default:
        // statements
}
```

`break` kết thúc `switch`. Nếu thiếu `break`, Java tiếp tục chạy các case phía sau, hiện tượng này gọi là fall-through.

### Ví dụ ngày trong tuần

```java
int day = 5;

switch (day) {
    case 1:
        System.out.println("Monday");
        break;
    case 2:
        System.out.println("Tuesday");
        break;
    case 3:
        System.out.println("Wednesday");
        break;
    case 4:
        System.out.println("Thursday");
        break;
    case 5:
        System.out.println("Friday");
        break;
    case 6:
        System.out.println("Saturday");
        break;
    case 7:
        System.out.println("Sunday");
        break;
    default:
        System.out.println("Invalid day");
}
```

### Switch expression hiện đại

Với Java hiện đại, cú pháp `->` ngắn gọn hơn và không có fall-through ngoài ý muốn:

```java
String dayName = switch (day) {
    case 1 -> "Monday";
    case 2 -> "Tuesday";
    case 3 -> "Wednesday";
    case 4 -> "Thursday";
    case 5 -> "Friday";
    case 6 -> "Saturday";
    case 7 -> "Sunday";
    default -> throw new IllegalArgumentException("Invalid day: " + day);
};
```

### Switch với enum trong production

```java
public String displayStatus(OrderStatus status) {
    return switch (status) {
        case CREATED -> "Waiting for payment";
        case PAID -> "Payment completed";
        case CANCELLED -> "Order cancelled";
    };
}
```

Khi switch bao phủ toàn bộ constant của `enum`, compiler có thể hỗ trợ phát hiện case còn thiếu khi enum thay đổi.

### Khi nào chọn `if`, khi nào chọn `switch`?

- Dùng `if` cho range và điều kiện kết hợp: `age >= 18 && country.equals("VN")`.
- Dùng `switch` khi chọn một hành vi dựa trên một giá trị rời rạc như enum, mã lệnh hoặc loại sự kiện.
- Nếu mỗi nhánh chứa business logic lớn, cân nhắc polymorphism hoặc strategy pattern thay vì một `switch` rất dài.

### Lưu ý mục 2

- Thứ tự `else if` quan trọng: đặt điều kiện cụ thể trước điều kiện tổng quát có thể bao phủ nó.
- Dùng guard clause để giảm số tầng lồng nhau.
- Luôn dùng `{}` cho `if` trong production code để giảm lỗi khi bảo trì.
- Switch truyền thống cần chú ý `break` và fall-through.
- Ưu tiên switch expression với `->` khi Java version của dự án hỗ trợ.
- Không dùng một chuỗi `if`/`switch` quá dài để chứa toàn bộ business logic; tách method hoặc dùng abstraction phù hợp.

## 3. Loop/Iteration Statements

Loop cho phép thực thi một khối lệnh nhiều lần. Ba loại chính:

- `while`: kiểm tra điều kiện trước khi chạy thân vòng lặp.
- `do-while`: chạy thân vòng lặp trước, rồi mới kiểm tra điều kiện.
- `for`: phù hợp khi có bước khởi tạo, điều kiện và cập nhật rõ ràng.

| Vòng lặp | Thân vòng lặp chạy tối thiểu | Trường hợp thường dùng |
|---|---:|---|
| `while` | 0 lần | Số vòng chưa biết trước, phụ thuộc điều kiện |
| `do-while` | 1 lần | Cần thực hiện trước rồi mới quyết định lặp lại |
| `for` | 0 lần | Duyệt theo index hoặc số vòng xác định |

## 3.1 While statement

### Cú pháp

```java
while (condition) {
    // one or more statements
}
```

Luồng thực thi:

1. Kiểm tra `condition`.
2. Nếu `false`, thoát vòng lặp ngay.
3. Nếu `true`, chạy thân vòng lặp.
4. Quay lại bước kiểm tra điều kiện.

### Ví dụ in các số từ 1 đến 5

```java
int number = 1;

while (number <= 5) {
    System.out.println("Number: " + number);
    number++;
}
```

Kết quả:

```text
Number: 1
Number: 2
Number: 3
Number: 4
Number: 5
```

Slide đang khởi tạo `number = 5` nhưng lại minh họa output từ 1 đến 5. Với `number = 5`, vòng lặp chỉ in `Number: 5` một lần. Muốn có output như slide phải khởi tạo bằng `1`.

### Ví dụ production: xử lý các trang dữ liệu

```java
String nextCursor = null;

do {
    CustomerPage page = customerClient.fetchPage(nextCursor, 100);

    for (Customer customer : page.customers()) {
        customerSyncService.sync(customer);
    }

    nextCursor = page.nextCursor();
} while (nextCursor != null);
```

Số trang không biết trước nên điều kiện dừng phụ thuộc `nextCursor` do hệ thống bên ngoài trả về.

### Nguy cơ vòng lặp vô hạn

```java
int number = 1;

while (number <= 5) {
    System.out.println(number);
    // Quên number++ khiến điều kiện luôn đúng
}
```

Trong production, vòng lặp chờ hoặc retry cần có giới hạn số lần, timeout hoặc điều kiện hủy rõ ràng.

## 3.2 Do-while statement

### Cú pháp

```java
do {
    // one or more statements
} while (condition);
```

Chú ý dấu `;` sau `while (condition)`. Thân `do` luôn chạy ít nhất một lần vì điều kiện chỉ được kiểm tra ở cuối.

### Ví dụ điều kiện đúng nhiều lần

```java
int number = 1;

do {
    System.out.println("Number: " + number);
    number++;
} while (number <= 5);
```

### Ví dụ điều kiện sai ngay từ đầu

```java
int number = 8;

do {
    System.out.println("Number: " + number);
    number++;
} while (number <= 5);
```

Đoạn code vẫn in `Number: 8` một lần rồi mới kết thúc.

### Khi nào dùng do-while?

`do-while` phù hợp khi thao tác phải xảy ra ít nhất một lần, chẳng hạn:

- Hiển thị menu rồi hỏi người dùng có muốn tiếp tục.
- Thử đọc một batch đầu tiên rồi kiểm tra còn cursor hay không.
- Thực hiện retry đầu tiên rồi quyết định có thử lại.

Trong backend production, `do-while` ít phổ biến hơn `for` và `while`; chỉ nên dùng khi yêu cầu “chạy ít nhất một lần” thực sự rõ ràng.

## 3.3 For statement

### Cú pháp

```java
for (initialization; condition; incrementOrDecrement) {
    // one or more statements
}
```

Thứ tự thực thi:

1. Chạy `initialization` đúng một lần.
2. Kiểm tra `condition`.
3. Nếu đúng, chạy thân vòng lặp.
4. Chạy phần cập nhật.
5. Quay lại bước kiểm tra điều kiện.

### Ví dụ in các số từ 1 đến 5

```java
for (int number = 1; number <= 5; number++) {
    System.out.println("Number: " + number);
}
```

Biến `number` chỉ tồn tại trong phạm vi vòng `for`.

### Enhanced for

Khi không cần index, dùng enhanced for để duyệt array hoặc collection:

```java
List<Order> orders = orderRepository.findPendingOrders();

for (Order order : orders) {
    orderService.process(order);
}
```

Enhanced for dễ đọc và giảm lỗi off-by-one. Nếu cần index, sửa phần tử theo vị trí hoặc duyệt ngược thì dùng `for` truyền thống.

### Ví dụ production: validate danh sách sản phẩm

```java
public void validateItems(List<OrderItem> items) {
    if (items == null || items.isEmpty()) {
        throw new IllegalArgumentException("Order must contain at least one item");
    }

    for (int index = 0; index < items.size(); index++) {
        OrderItem item = items.get(index);

        if (item.quantity() <= 0) {
            throw new IllegalArgumentException(
                    "Item at index " + index + " has invalid quantity");
        }
    }
}
```

Index có ích ở đây vì thông báo lỗi cần chỉ ra vị trí phần tử không hợp lệ.

## 3.4 Nested Loops

Nested loop là vòng lặp nằm bên trong một vòng lặp khác. Với mỗi lần chạy của vòng ngoài, vòng trong chạy lại từ đầu.

### Ví dụ in tam giác dấu sao

```java
int rows = 5;

for (int row = 1; row <= rows; row++) {
    for (int column = 1; column <= row; column++) {
        System.out.print("*");
    }
    System.out.println();
}
```

Kết quả:

```text
*
**
***
****
*****
```

Slide dùng điều kiện `row < n` với `n = 5`, vì vậy chỉ in bốn dòng. Nếu muốn đúng năm dòng thì dùng `row <= rows`.

### Độ phức tạp của nested loop

Hai vòng lặp chạy lần lượt `n` và `m` lần thường tạo khoảng `n * m` lượt xử lý. Nếu cả hai đều chạy `n` lần, độ phức tạp thường là `O(n²)`.

Ví dụ dễ gây chậm:

```java
for (Order order : orders) {
    for (Customer customer : customers) {
        if (order.customerId().equals(customer.id())) {
            // Ghép order với customer
        }
    }
}
```

Có thể tạo map để tra cứu nhanh hơn:

```java
Map<String, Customer> customerById = customers.stream()
        .collect(Collectors.toMap(Customer::id, customer -> customer));

for (Order order : orders) {
    Customer customer = customerById.get(order.customerId());
    if (customer != null) {
        // Xử lý order và customer
    }
}
```

Cách thứ hai thường giảm từ `O(n * m)` xuống gần `O(n + m)`, đổi lại cần thêm bộ nhớ cho `Map`.

### Lưu ý mục 3

- `while` có thể không chạy lần nào; `do-while` luôn chạy ít nhất một lần.
- Mọi vòng lặp phải có điều kiện dừng hoặc cơ chế hủy rõ ràng.
- Dùng `for` khi số vòng hoặc bước cập nhật rõ ràng; dùng enhanced for khi chỉ cần từng phần tử.
- Kiểm tra cẩn thận `<` và `<=` để tránh lỗi off-by-one.
- Không thay đổi kích thước `List` trực tiếp trong enhanced for; việc đó có thể gây `ConcurrentModificationException`.
- Nested loop có thể làm thời gian xử lý tăng rất nhanh. Với dữ liệu lớn, cân nhắc `Map`, `Set`, database join hoặc thuật toán phù hợp.
- Tránh gọi database hoặc HTTP API riêng cho từng phần tử trong vòng lặp vì dễ tạo N+1 query/call. Nên batch hoặc tải dữ liệu cần thiết trước.

## 4. Branching/Transfer Statements

Branching/transfer statement thay đổi luồng chạy tuần tự của chương trình:

| Statement | Tác dụng chính | Phạm vi bị ảnh hưởng |
|---|---|---|
| `break` | Kết thúc sớm vòng lặp hoặc `switch` | Loop/`switch` gần nhất, hoặc statement có label |
| `continue` | Bỏ phần còn lại của lượt lặp hiện tại | Vòng lặp gần nhất, hoặc loop có label |
| `return` | Kết thúc method và có thể trả về giá trị | Toàn bộ method hiện tại |

Ba statement này có phạm vi tác động khác nhau. Chọn sai có thể khiến chương trình bỏ qua quá nhiều hoặc quá ít công việc.

## 4.1 Break statement

`break` thường được dùng theo hai cách:

1. Kết thúc một case trong cú pháp `switch` truyền thống.
2. Thoát sớm khỏi `for`, enhanced `for`, `while` hoặc `do-while`.

### Unlabeled break

Cú pháp:

```java
break;
```

`break` không có label chỉ thoát khỏi loop hoặc `switch` gần nhất đang chứa nó. Chương trình tiếp tục ở statement đầu tiên ngay sau cấu trúc đó.

### Break trong vòng `for`

```java
int[] numbers = {10, 20, 30, 40, 50};

for (int number : numbers) {
    if (number == 30) {
        break;
    }
    System.out.println(number);
}
```

Kết quả:

```text
10
20
```

Khi gặp `30`, `break` kết thúc toàn bộ vòng lặp. Phần tử `30` và các phần tử sau nó không được in.

### Break trong `while`

Ví dụ tìm vị trí đầu tiên của chữ `E`:

```java
String[] letters = {"A", "E", "I", "O", "U"};
int index = 0;

while (index < letters.length) {
    if (letters[index].equals("E")) {
        System.out.println("Found E at index: " + index);
        break;
    }
    index++;
}
```

Khi tìm thấy phần tử cần thiết, không cần duyệt phần còn lại của array.

### Break trong `do-while`

```java
String[] letters = {"A", "E", "I", "O", "U"};
int index = 0;

do {
    if (letters[index].equals("U")) {
        System.out.println("Found U at index: " + index);
        break;
    }
    index++;
} while (index < letters.length);
```

Slide đang dùng `arr[len]`, nhưng không khai báo `len` và có thể truy cập sai index. Phần tử hiện tại phải là `letters[index]`. Với array, index hợp lệ chạy từ `0` đến `length - 1`.

`do-while` luôn truy cập phần tử trước khi kiểm tra điều kiện cuối vòng. Vì vậy, nếu array có thể rỗng thì phải kiểm tra trước:

```java
if (letters.length == 0) {
    return;
}
```

### Break trong switch truyền thống

```java
String permission = "user";

switch (permission) {
    case "admin":
        System.out.println("Permission is admin");
        break;
    case "user":
        System.out.println("Permission is user");
        break;
    default:
        System.out.println("Permission is guest");
}
```

Nếu bỏ `break`, chương trình tiếp tục chạy case phía dưới, kể cả khi label của case đó không khớp. Đây là fall-through.

Với switch expression dùng `->`, không cần `break` để chống fall-through:

```java
String displayPermission = switch (permission) {
    case "admin" -> "Administrator";
    case "user" -> "Standard user";
    default -> "Guest";
};
```

Trong production, nếu tập permission cố định, nên dùng `enum` thay vì String để compiler kiểm tra type.

### Labeled break

Label được khai báo bằng tên theo sau bởi dấu hai chấm:

```java
labelName:
for (...) {
    // ...
    break labelName;
}
```

`break labelName;` thoát khỏi statement mang đúng label đó. Nó không mặc định thoát “vòng ngoài cùng”; phạm vi phụ thuộc nơi label được đặt.

Ví dụ tìm số đầu tiên lớn hơn `10` trong mảng hai chiều:

```java
int[][] numbers = {
        {1, 2},
        {3, 4},
        {9, 10},
        {11, 12}
};

boolean found = false;
int foundRow = -1;
int foundColumn = -1;

searching:
for (int row = 0; row < numbers.length; row++) {
    for (int column = 0; column < numbers[row].length; column++) {
        if (numbers[row][column] > 10) {
            found = true;
            foundRow = row;
            foundColumn = column;
            break searching;
        }
    }
}

if (found) {
    System.out.printf(
            "First number greater than 10 is at [%d, %d]%n",
            foundRow,
            foundColumn);
}
```

Nếu chỉ dùng `break;`, chương trình thoát vòng `for` bên trong nhưng vòng ngoài vẫn tiếp tục. `break searching;` thoát cả khối loop được gắn label `searching`.

### Ví dụ production: dừng khi tìm thấy nhà cung cấp phù hợp

```java
public ShippingProvider findAvailableProvider(
        List<ShippingProvider> providers,
        Shipment shipment) {
    ShippingProvider selected = null;

    for (ShippingProvider provider : providers) {
        if (provider.supports(shipment) && provider.isAvailable()) {
            selected = provider;
            break;
        }
    }

    if (selected == null) {
        throw new ProviderNotFoundException(shipment.id());
    }
    return selected;
}
```

Trường hợp này có thể viết gọn hơn bằng Stream `filter(...).findFirst()`, nhưng vòng lặp vẫn phù hợp nếu logic cần debug từng bước hoặc có nhiều thao tác hơn một phép lọc đơn giản.

## 4.2 Continue statement

`continue` bỏ qua phần còn lại của lượt lặp hiện tại và chuyển sang lượt tiếp theo. Nó chỉ dùng bên trong loop, không dùng để thoát `switch` độc lập.

Cú pháp:

```java
continue;
```

### Continue trong vòng `for`

```java
for (int number = 0; number <= 5; number++) {
    if (number == 3) {
        continue;
    }
    System.out.println(number);
}
```

Kết quả không có số `3`:

```text
0
1
2
4
5
```

Khi gặp `continue`, vòng `for` chạy phần update (`number++`) rồi mới kiểm tra điều kiện cho lượt tiếp theo.

### Continue trong `while` và `do-while`

Với `while`, phải bảo đảm biến điều khiển được cập nhật trước khi `continue`; nếu không có thể tạo vòng lặp vô hạn:

```java
int number = 0;

while (number <= 5) {
    number++;

    if (number == 3) {
        continue;
    }
    System.out.println(number);
}
```

Trong `do-while`, `continue` chuyển tới bước kiểm tra condition ở cuối vòng:

```java
int number = 0;

do {
    number++;

    if (number == 3) {
        continue;
    }
    System.out.println(number);
} while (number < 5);
```

### Ví dụ production: bỏ qua record không hợp lệ

```java
public void importCustomers(List<CustomerRecord> records) {
    for (CustomerRecord record : records) {
        if (record == null) {
            continue;
        }
        if (record.email() == null || record.email().isBlank()) {
            auditService.recordInvalidCustomer(record);
            continue;
        }
        if (customerRepository.existsByEmail(record.email())) {
            auditService.recordDuplicateCustomer(record.email());
            continue;
        }

        customerRepository.save(Customer.from(record));
    }
}
```

Các `continue` đóng vai trò guard clause cho từng phần tử, giúp luồng xử lý hợp lệ không bị lồng trong nhiều tầng `if`.

Tuy nhiên, không nên âm thầm bỏ qua dữ liệu lỗi. Production code thường cần log, metric, audit hoặc danh sách kết quả để biết record nào bị loại và vì sao.

### Labeled continue

Java cũng hỗ trợ `continue labelName;`. Statement này bỏ phần còn lại của lượt hiện tại thuộc loop mang label rồi bắt đầu lượt kế tiếp của loop đó:

```java
nextRow:
for (int row = 0; row < matrix.length; row++) {
    for (int column = 0; column < matrix[row].length; column++) {
        if (matrix[row][column] < 0) {
            continue nextRow;
        }
    }
    processValidRow(matrix[row]);
}
```

Nếu một phần tử âm xuất hiện, toàn bộ phần còn lại của row hiện tại bị bỏ qua. Labeled `continue` khá hiếm; nếu luồng khó đọc, nên tách logic thành method.

## 4.3 Return statement

`return` kết thúc ngay method hiện tại và chuyển quyền điều khiển về caller.

Có hai dạng:

```java
return value; // Method có return type khác void
return;       // Method void
```

### Return một giá trị

```java
static String welcome() {
    return "java";
}

static void hello() {
    System.out.println("Hello " + welcome());
}
```

Khi gọi `hello()`, Java gọi `welcome()` trước, nhận chuỗi `"java"`, nối với `"Hello "` rồi in:

```text
Hello java
```

Tên method trong Java nên dùng camelCase và bắt đầu bằng chữ thường, vì vậy ví dụ được viết là `hello()` và `welcome()` thay cho `Hello()` và `Welcome()` trên slide.

### Return trong method `void`

```java
public void sendEmail(String email) {
    if (email == null || email.isBlank()) {
        return;
    }

    emailClient.send(email);
}
```

`return;` không trả về dữ liệu nhưng vẫn kết thúc method.

### Guard clause bằng return

```java
public PaymentResult pay(Order order, User user) {
    if (order == null) {
        return PaymentResult.rejected("Order is required");
    }
    if (user == null || !user.isActive()) {
        return PaymentResult.rejected("Active user is required");
    }
    if (!order.belongsTo(user.id())) {
        return PaymentResult.rejected("Order does not belong to user");
    }
    if (!order.canBePaid()) {
        return PaymentResult.rejected("Order cannot be paid");
    }

    return paymentGateway.charge(order);
}
```

Early return giúp xử lý trường hợp không hợp lệ trước và giữ luồng chính ít bị lồng nhau.

### Return bên trong vòng lặp

`return` bên trong loop kết thúc toàn bộ method, không chỉ kết thúc loop:

```java
public Optional<Customer> findCustomer(
        List<Customer> customers,
        String customerId) {
    for (Customer customer : customers) {
        if (customer.id().equals(customerId)) {
            return Optional.of(customer);
        }
    }
    return Optional.empty();
}
```

So sánh phạm vi:

```java
for (...) {
    if (condition) {
        break;    // Thoát loop, method vẫn chạy tiếp
    }
}

for (...) {
    if (condition) {
        return;   // Kết thúc method ngay
    }
}
```

### Quy tắc return type

- Method `void` chỉ có thể dùng `return;`.
- Method có return type phải trả về giá trị tương thích trên mọi đường chạy có thể hoàn thành bình thường.
- Constructor không có return type. Có thể dùng `return;` để kết thúc sớm constructor, nhưng thường nên validation rồi ném exception thay vì tạo object chưa hoàn chỉnh.
- Statement sau một `return` vô điều kiện trong cùng block là unreachable và gây lỗi compile.
- `return` kết thúc method hiện tại, không nhất thiết kết thúc thread hay toàn bộ ứng dụng.

### Lưu ý mục 4

- `break` thoát loop/`switch`; `continue` bỏ phần còn lại của lượt lặp; `return` kết thúc method.
- Unlabeled `break` và `continue` tác động đến loop gần nhất.
- Label được khai báo bằng cú pháp `labelName:`. Chỉ dùng labeled statement khi nó thực sự dễ đọc hơn cách tách method.
- Switch truyền thống cần `break` để tránh fall-through; switch expression dùng `->` không cần cơ chế này.
- Trong `while`/`do-while`, cập nhật biến điều khiển trước `continue` để tránh vòng lặp vô hạn.
- Early return và `continue` có thể giảm nesting, nhưng quá nhiều điểm thoát cũng làm method khó theo dõi.
- Khi bỏ qua dữ liệu lỗi bằng `continue`, cần lưu lý do qua log, metric hoặc kết quả import.
- Sau nhóm slide này, bốn mục tiêu của tài liệu Control Statements đã được bao phủ đầy đủ.
