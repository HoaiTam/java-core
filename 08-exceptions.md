# Java Core 08 - Handle Exceptions

Tài liệu này tổng hợp phần **Exception Handling** trong Java, đồng thời bổ sung cách xử lý lỗi thường dùng trong production.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Introduction to Exceptions](#1-introduction-to-exceptions)
  - [1.1 Throwable hierarchy](#11-throwable-hierarchy)
  - [1.2 Checked và unchecked exception](#12-checked-và-unchecked-exception)
  - [1.3 Error và Exception](#13-error-và-exception)
  - [1.4 Exception propagation](#14-exception-propagation)
  - [1.5 Các method quan trọng của Throwable](#15-các-method-quan-trọng-của-throwable)
- [2. Working with Exceptions](#2-working-with-exceptions)
  - [2.1 Xử lý checked exception](#21-xử-lý-checked-exception)
  - [2.2 Khi nào catch, khi nào propagate](#22-khi-nào-catch-khi-nào-propagate)
  - [2.3 Try-catch và nhiều catch block](#23-try-catch-và-nhiều-catch-block)
  - [2.4 Finally](#24-finally)
  - [2.5 Try-with-resources](#25-try-with-resources)
  - [2.6 Nested try](#26-nested-try)
  - [2.7 Throw và throws](#27-throw-và-throws)
  - [2.8 Nguyên tắc xử lý exception trong production](#28-nguyên-tắc-xử-lý-exception-trong-production)
- [3. User-defined Exceptions](#3-user-defined-exceptions)
  - [3.1 Tạo custom exception](#31-tạo-custom-exception)
  - [3.2 Chọn checked hay unchecked](#32-chọn-checked-hay-unchecked)
  - [3.3 Ví dụ production](#33-ví-dụ-production)
  - [3.4 Exception translation](#34-exception-translation)

---

## Mục tiêu

Sau phần này cần nắm được:

1. Cấu trúc phân cấp của `Throwable`.
2. Sự khác nhau giữa checked exception, unchecked exception và `Error`.
3. Cách exception lan truyền qua call stack.
4. Cách sử dụng `try`, `catch`, `finally`, `throw` và `throws`.
5. Cách giải phóng tài nguyên bằng try-with-resources.
6. Cách thiết kế custom exception có ý nghĩa trong production.

---

## 1. Introduction to Exceptions

Exception là một sự kiện bất thường xảy ra trong lúc chương trình chạy và làm gián đoạn luồng thực thi bình thường.

Một số nguồn gây exception:

- Lỗi lập trình: truy cập `null`, index vượt phạm vi, ép kiểu sai.
- Input không hợp lệ từ người dùng hoặc client.
- Dữ liệu không thỏa business rule.
- File, database, network hoặc dịch vụ bên ngoài gặp sự cố.
- Tài nguyên hệ thống không còn khả dụng.

Ví dụ:

```java
int quantity = 0;
int averagePrice = 100 / quantity; // ArithmeticException
```

Nếu exception không được xử lý, thread hiện tại sẽ kết thúc sau khi JVM in stack trace. Với ứng dụng server, request hiện tại có thể thất bại nhưng process thường vẫn tiếp tục phục vụ request khác.

### Exception không thay thế validation

Nếu có thể kiểm tra một điều kiện bình thường, nên kiểm tra trực tiếp:

```java
public int calculateUnitPrice(int total, int quantity) {
    if (quantity <= 0) {
        throw new IllegalArgumentException("quantity must be greater than zero");
    }
    return total / quantity;
}
```

Không nên cố tình chia cho `0`, chờ `ArithmeticException`, rồi dùng exception như một nhánh `if`.

## 1.1 Throwable hierarchy

Gốc của hệ thống lỗi Java là `java.lang.Throwable`:

```text
Throwable
├── Error
└── Exception
    ├── RuntimeException
    │   └── Unchecked exceptions
    └── Checked exceptions
```

### `Throwable`

Chỉ object kế thừa `Throwable` mới có thể được ném bằng `throw` và bắt bằng `catch`.

Thông thường application code không kế thừa trực tiếp `Throwable`. Custom type nên kế thừa:

- `Exception` nếu muốn tạo checked exception.
- `RuntimeException` nếu muốn tạo unchecked exception.

### `Error`

`Error` mô tả vấn đề nghiêm trọng ở JVM hoặc môi trường chạy, ví dụ:

- `OutOfMemoryError`
- `StackOverflowError`
- `NoClassDefFoundError`

Business code thường không thể phục hồi một cách đáng tin cậy từ các lỗi này.

### `Exception`

`Exception` mô tả điều kiện bất thường mà application có thể muốn xử lý hoặc chuyển đổi, ví dụ:

- `IOException`
- `SQLException`
- `IllegalArgumentException`
- `NullPointerException`

## 1.2 Checked và unchecked exception

### Checked exception

Checked exception là subclass của `Exception` nhưng không thuộc nhánh `RuntimeException`.

Ví dụ:

- `IOException`
- `FileNotFoundException`
- `SQLException`
- `ClassNotFoundException`

Compiler yêu cầu code phải:

1. Bắt exception bằng `try-catch`; hoặc
2. Khai báo nó bằng `throws`.

```java
public String readConfig(Path path) throws IOException {
    return Files.readString(path);
}
```

Hoặc:

```java
public String readConfig(Path path) {
    try {
        return Files.readString(path);
    } catch (IOException e) {
        throw new IllegalStateException("Cannot read config: " + path, e);
    }
}
```

### Unchecked exception

Unchecked exception là `RuntimeException` hoặc subclass của nó.

Ví dụ:

- `NullPointerException`
- `IllegalArgumentException`
- `IllegalStateException`
- `IndexOutOfBoundsException`
- `ClassCastException`

Compiler không bắt buộc phải `catch` hoặc khai báo `throws`:

```java
public void updateQuantity(int quantity) {
    if (quantity < 0) {
        throw new IllegalArgumentException("quantity cannot be negative");
    }
}
```

Unchecked exception thường biểu diễn:

- Vi phạm precondition.
- Object đang ở trạng thái không hợp lệ.
- Bug trong logic hoặc cách sử dụng API.
- Business rule không thể tiếp tục tại tầng hiện tại.

> Unchecked không có nghĩa là exception không thể bắt. Nó chỉ có nghĩa compiler không bắt buộc caller phải xử lý.

### So sánh nhanh

| Tiêu chí | Checked exception | Unchecked exception |
|---|---|---|
| Nhánh kế thừa | `Exception`, trừ `RuntimeException` | `RuntimeException` |
| Compiler kiểm tra | Có | Không |
| Bắt hoặc khai báo | Bắt buộc | Không bắt buộc |
| Ví dụ | `IOException` | `IllegalArgumentException` |
| Trường hợp phổ biến | I/O hoặc operation có khả năng thất bại được dự đoán | Sai input, sai state, lỗi lập trình |

## 1.3 Error và Exception

| Tiêu chí | `Error` | `Exception` |
|---|---|---|
| Package hierarchy | Kế thừa `java.lang.Error` | Kế thừa `java.lang.Exception` |
| Compile-time checking | Không | Có thể checked hoặc unchecked |
| Ý nghĩa phổ biến | Lỗi nghiêm trọng của JVM hoặc môi trường | Lỗi của operation/application |
| Khả năng phục hồi | Thường không đáng tin cậy | Nhiều trường hợp có thể xử lý |
| Ví dụ | `OutOfMemoryError`, `StackOverflowError` | `IOException`, `IllegalArgumentException` |

Slide mô tả `Error` là “không thể phục hồi”. Cách nói chính xác hơn là application **thường không nên kỳ vọng có thể phục hồi an toàn**.

Không nên viết:

```java
try {
    processOrder();
} catch (Throwable throwable) {
    // Vô tình nuốt cả Error.
}
```

Thông thường chỉ bắt những exception mà code thực sự biết cách xử lý.

## 1.4 Exception propagation

Khi một method ném exception:

1. JVM tìm `catch` phù hợp trong method hiện tại.
2. Nếu không có, frame hiện tại bị tháo khỏi call stack.
3. JVM tiếp tục tìm handler ở method gọi nó.
4. Quá trình tiếp diễn đến khi tìm thấy handler hoặc đến đầu thread.

Quá trình tháo các stack frame được gọi là **stack unwinding**.

```java
public void loadApplication() {
    loadConfiguration();
}

public void loadConfiguration() {
    parseConfiguration();
}

public void parseConfiguration() {
    throw new IllegalStateException("Invalid configuration");
}
```

`IllegalStateException` tự động lan truyền từ `parseConfiguration()` đến `loadConfiguration()`, rồi đến `loadApplication()`.

### Checked exception khi propagation

Với checked exception, mỗi method không xử lý nó phải khai báo `throws`:

```java
public void importData(Path path) throws IOException {
    readData(path);
}

private void readData(Path path) throws IOException {
    Files.readAllLines(path);
}
```

### Handler phù hợp

Một `catch` có thể bắt đúng type hoặc superclass của exception:

```java
try {
    Files.readString(path);
} catch (IOException e) {
    // Bắt được FileNotFoundException vì nó là subclass của IOException.
}
```

### Bảo toàn nguyên nhân gốc

Khi chuyển exception sang abstraction khác, luôn truyền exception gốc làm `cause`:

```java
try {
    repository.save(order);
} catch (SQLException e) {
    throw new OrderPersistenceException(
            "Cannot save order " + order.getId(),
            e
    );
}
```

Nếu bỏ `e`, stack trace mất thông tin quan trọng để điều tra nguyên nhân.

## 1.5 Các method quan trọng của Throwable

| Method | Ý nghĩa |
|---|---|
| `getMessage()` | Lấy message mô tả lỗi |
| `toString()` | Trả về tên class và message |
| `printStackTrace()` | In stack trace ra `System.err` |
| `printStackTrace(PrintStream)` | In stack trace ra stream được chỉ định |
| `getCause()` | Lấy exception nguyên nhân |
| `getSuppressed()` | Lấy các exception bị suppress, thường từ đóng resource |

Ví dụ:

```java
try {
    process();
} catch (RuntimeException e) {
    System.out.println(e.getMessage());
    System.out.println(e.getClass().getName());
    System.out.println(e.getCause());
}
```

### Logging trong production

Không nên dùng `printStackTrace()` làm cơ chế logging chính:

```java
try {
    process();
} catch (RuntimeException e) {
    e.printStackTrace(); // Khó tìm kiếm, thiếu context và khó quản lý output.
}
```

Nên dùng logging framework và truyền nguyên exception:

```java
try {
    processOrder(orderId);
} catch (RuntimeException e) {
    logger.error("Failed to process orderId={}", orderId, e);
    throw e;
}
```

Tuy nhiên, tránh log cùng một exception ở mọi tầng vì sẽ tạo nhiều stack trace trùng nhau. Thường log một lần tại application boundary, nơi có đủ request context.

### Lưu ý mục 1

- `Throwable` có hai nhánh chính là `Error` và `Exception`.
- Checked exception phải được catch hoặc khai báo bằng `throws`.
- Unchecked exception vẫn có thể được bắt; compiler chỉ không bắt buộc làm vậy.
- Không bắt `Throwable` hoặc `Error` trong business code thông thường.
- Khi chuyển đổi exception, luôn bảo toàn nguyên nhân gốc bằng constructor có `cause`.
- Exception là cơ chế báo lỗi, không nên dùng làm control flow cho tình huống bình thường.

---

## 2. Working with Exceptions

Mục tiêu của exception handling không phải là làm cho lỗi “biến mất”. Handler tốt phải thực hiện được ít nhất một việc có ý nghĩa:

- Phục hồi bằng phương án thay thế hợp lệ.
- Retry khi operation thực sự có tính tạm thời và idempotent.
- Chuyển lỗi sang abstraction phù hợp hơn.
- Bổ sung context rồi propagate.
- Chuyển lỗi thành response phù hợp tại application boundary.
- Cleanup tài nguyên.

## 2.1 Xử lý checked exception

Có hai cách chính:

### Catch và xử lý

```java
public String loadTemplate(Path path) {
    try {
        return Files.readString(path);
    } catch (NoSuchFileException e) {
        return "Default template";
    } catch (IOException e) {
        throw new TemplateLoadingException("Cannot load template " + path, e);
    }
}
```

Fallback chỉ hợp lệ nếu business cho phép dùng template mặc định.

### Khai báo bằng `throws`

```java
public String loadTemplate(Path path) throws IOException {
    return Files.readString(path);
}
```

Cách này phù hợp khi caller có đủ context để quyết định retry, fallback hoặc trả lỗi.

### Không khai báo quá rộng

Tránh:

```java
public void importFile(Path path) throws Exception {
    // Caller không biết operation có thể thất bại theo cách nào.
}
```

Ưu tiên type cụ thể:

```java
public void importFile(Path path) throws IOException, InvalidFileFormatException {
    // ...
}
```

## 2.2 Khi nào catch, khi nào propagate

### Catch khi

- Có thể phục hồi đúng nghĩa.
- Có fallback hợp lệ.
- Cần chuyển exception sang abstraction của tầng hiện tại.
- Đang ở application boundary và cần map sang response/log/exit code.
- Cần cleanup mà try-with-resources không áp dụng được.

### Propagate khi

- Method không biết cách xử lý đúng.
- Caller có nhiều context hơn để quyết định.
- Việc catch chỉ dẫn đến in message rồi tiếp tục với state sai.
- Exception thể hiện operation không thể hoàn thành contract.

### Không catch rồi bỏ qua

Anti-pattern:

```java
try {
    chargeCustomer();
} catch (PaymentException e) {
    // Bỏ qua lỗi, hệ thống có thể đánh dấu đơn hàng đã thanh toán sai.
}
```

Ít nhất phải chuyển trạng thái đúng, propagate hoặc ghi nhận lỗi tại boundary phù hợp.

### Catch để đổi type

```java
public Customer loadCustomer(long customerId) {
    try {
        return customerRepository.findById(customerId);
    } catch (SQLException e) {
        throw new CustomerDataAccessException(
                "Cannot load customerId=" + customerId,
                e
        );
    }
}
```

Service layer không cần phụ thuộc trực tiếp vào `SQLException`.

## 2.3 Try-catch và nhiều catch block

Cú pháp:

```java
try {
    // Code có thể ném exception.
} catch (SpecificException e) {
    // Xử lý exception cụ thể.
} catch (Exception e) {
    // Handler tổng quát hơn.
}
```

### Bắt exception cụ thể trước

```java
try {
    importFile(path);
} catch (NoSuchFileException e) {
    handleMissingFile(e);
} catch (IOException e) {
    handleIoFailure(e);
}
```

Không thể đảo thứ tự:

```java
try {
    importFile(path);
} catch (IOException e) {
    // ...
} catch (NoSuchFileException e) { // Compile error: unreachable catch.
    // ...
}
```

Vì `NoSuchFileException` là subclass của `IOException` và đã bị catch phía trên bắt hết.

### Multi-catch

Nếu nhiều type có cùng cách xử lý:

```java
try {
    parseAndSave(input);
} catch (NumberFormatException | DateTimeParseException e) {
    throw new InvalidInputException("Invalid input: " + input, e);
}
```

Các type trong multi-catch không được có quan hệ cha con trực tiếp.

### Catch `Exception`

`catch (Exception e)` có thể phù hợp tại boundary, ví dụ worker loop hoặc request handler, để một task lỗi không làm dừng toàn bộ consumer. Tuy nhiên handler vẫn phải:

- Ghi đủ context.
- Đưa message/task vào dead-letter queue nếu cần.
- Không che giấu lỗi.
- Tôn trọng `InterruptedException`.

Ví dụ worker:

```java
public void consume(Job job) {
    try {
        jobProcessor.process(job);
    } catch (KnownBusinessException e) {
        deadLetterQueue.publish(job, e.getMessage());
    } catch (RuntimeException e) {
        logger.error("Unexpected failure, jobId={}", job.id(), e);
        throw e;
    }
}
```

## 2.4 Finally

`finally` dùng cho cleanup cần thực hiện dù:

- Khối `try` hoàn tất bình thường.
- `try` thực hiện `return`, `break` hoặc `continue`.
- Exception được bắt.
- Exception tiếp tục lan truyền.

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
    updateSharedState();
} finally {
    lock.unlock();
}
```

### `finally` không tuyệt đối luôn chạy

Slide nói `finally` “always executed”. Trong thực tế có ngoại lệ:

- Gọi `System.exit(...)`.
- Gọi `Runtime.halt(...)`.
- JVM hoặc process bị crash/kill.
- Mất điện hoặc hệ điều hành dừng process.

Vì vậy không dùng `finally` để bảo đảm nghiệp vụ quan trọng đã được commit ra hệ thống ngoài.

### Không return trong finally

```java
public int calculate() {
    try {
        throw new IllegalStateException("failed");
    } finally {
        return 0; // Nuốt exception phía trên.
    }
}
```

`return`, `break`, `continue` hoặc ném exception mới trong `finally` có thể che mất exception ban đầu. Đây là anti-pattern.

### Cleanup thủ công

```java
InputStream input = null;
try {
    input = Files.newInputStream(path);
    return input.read();
} finally {
    if (input != null) {
        try {
            input.close();
        } catch (IOException closeError) {
            // Cleanup thủ công khá rườm rà và dễ che exception chính.
        }
    }
}
```

Với resource triển khai `AutoCloseable`, nên dùng try-with-resources.

## 2.5 Try-with-resources

Try-with-resources tự động đóng resource sau khi khối `try` kết thúc.

```java
public List<String> readLines(Path path) throws IOException {
    try (BufferedReader reader = Files.newBufferedReader(path)) {
        return reader.lines().toList();
    }
}
```

Resource phải implement `AutoCloseable`.

### Nhiều resource

```java
try (
        InputStream input = Files.newInputStream(source);
        OutputStream output = Files.newOutputStream(target)
) {
    input.transferTo(output);
}
```

Resource được đóng theo thứ tự ngược lại: `output` đóng trước, `input` đóng sau.

### Suppressed exception

Nếu operation trong `try` ném exception và `close()` cũng ném exception:

- Exception từ operation là exception chính.
- Exception từ `close()` được lưu trong `getSuppressed()`.

```java
catch (IOException e) {
    for (Throwable suppressed : e.getSuppressed()) {
        logger.warn("Failure while closing resource", suppressed);
    }
    throw e;
}
```

Try-with-resources tránh việc lỗi khi đóng resource che mất lỗi chính.

## 2.6 Nested try

Java cho phép đặt `try-catch` bên trong một `try` khác:

```java
try {
    loadBatch();

    try {
        processOptionalMetadata();
    } catch (MetadataException e) {
        logger.warn("Metadata is unavailable", e);
    }

    saveBatch();
} catch (BatchException e) {
    handleBatchFailure(e);
}
```

Nested try có thể phù hợp khi inner operation là tùy chọn và có chính sách lỗi khác hẳn outer operation.

Tuy nhiên nested try sâu làm code khó đọc. Thường nên tách thành method:

```java
public void importBatch() {
    try {
        loadBatch();
        processMetadataIfAvailable();
        saveBatch();
    } catch (BatchException e) {
        handleBatchFailure(e);
    }
}

private void processMetadataIfAvailable() {
    try {
        processOptionalMetadata();
    } catch (MetadataException e) {
        logger.warn("Metadata is unavailable", e);
    }
}
```

## 2.7 Throw và throws

### `throw`

`throw` là statement dùng để ném một object `Throwable` tại một vị trí cụ thể trong method body:

```java
public int divide(int dividend, int divisor) {
    if (divisor == 0) {
        throw new IllegalArgumentException("divisor must not be zero");
    }
    return dividend / divisor;
}
```

Mỗi lần `throw` chỉ ném một object:

```java
throw new IllegalStateException("Order is already completed");
```

### `throws`

`throws` nằm trong method declaration và mô tả các checked exception có thể thoát khỏi method:

```java
public String readFile(Path path) throws IOException {
    return Files.readString(path);
}
```

Có thể khai báo nhiều type:

```java
public Object loadPlugin(String className)
        throws ClassNotFoundException, ReflectiveOperationException {
    Class<?> type = Class.forName(className);
    return type.getDeclaredConstructor().newInstance();
}
```

### So sánh

| Tiêu chí | `throw` | `throws` |
|---|---|---|
| Vị trí | Method body | Method declaration |
| Vai trò | Thực sự ném exception | Khai báo exception có thể propagate |
| Số lượng | Một object mỗi statement | Có thể khai báo nhiều type |
| Theo sau bởi | Object `Throwable` | Tên class exception |

### Hiệu chỉnh nội dung slide

- `throw` không bắt buộc phải đi cùng `throws` nếu ném unchecked exception.
- `throws` không tự tạo hoặc tự ném exception.
- Khai báo `throws` không thay thế việc xử lý; nó chuyển trách nhiệm cho caller.
- Không cần khai báo unchecked exception, dù Java vẫn cho phép khai báo.

## 2.8 Nguyên tắc xử lý exception trong production

### 1. Không nuốt exception

Tránh:

```java
try {
    repository.save(entity);
} catch (Exception ignored) {
}
```

### 2. Bắt type cụ thể

```java
try {
    repository.save(entity);
} catch (DuplicateKeyException e) {
    throw new CustomerAlreadyExistsException(entity.getEmail(), e);
}
```

### 3. Thêm context có ích

Message tốt:

```text
Cannot reserve inventory for orderId=ORD-102, sku=BOOK-9
```

Message kém:

```text
Something went wrong
```

Không đưa password, token, thông tin thẻ hoặc dữ liệu nhạy cảm vào message/log.

### 4. Bảo toàn cause

```java
throw new OrderProcessingException(
        "Cannot process orderId=" + orderId,
        originalException
);
```

### 5. Không vừa log vừa throw ở mọi tầng

Nếu repository log, service log lại và controller tiếp tục log, cùng một lỗi xuất hiện ba lần. Thường:

- Tầng dưới thêm context và throw.
- Boundary ngoài cùng log một lần với correlation/request ID.

### 6. Không dùng exception làm control flow

Tránh:

```java
try {
    return users.get(index);
} catch (IndexOutOfBoundsException e) {
    return null;
}
```

Nên kiểm tra contract hoặc thiết kế API rõ ràng.

### 7. Xử lý interruption đúng cách

Không nuốt `InterruptedException`:

```java
try {
    blockingQueue.take();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    throw new TaskInterruptedException("Task was interrupted", e);
}
```

Khôi phục interrupt flag để tầng trên biết thread đã nhận yêu cầu dừng.

### 8. Retry có chọn lọc

Chỉ retry lỗi tạm thời và operation an toàn để thực hiện lại:

```java
public PaymentResult chargeWithRetry(PaymentCommand command) {
    for (int attempt = 1; attempt <= 3; attempt++) {
        try {
            return paymentGateway.charge(command);
        } catch (TemporaryGatewayException e) {
            if (attempt == 3) {
                throw new PaymentUnavailableException(
                        "Gateway unavailable after 3 attempts",
                        e
                );
            }
            backoff(attempt);
        }
    }
    throw new IllegalStateException("Unreachable");
}
```

Trong production, cần thêm exponential backoff, jitter, timeout và idempotency key. Không retry lỗi validation hoặc payment bị từ chối.

### 9. Map exception tại boundary

Ví dụ mapping khái niệm trong HTTP service:

| Exception | HTTP status phù hợp |
|---|---|
| `InvalidOrderException` | `400 Bad Request` |
| `OrderNotFoundException` | `404 Not Found` |
| `OrderConflictException` | `409 Conflict` |
| Lỗi không dự đoán | `500 Internal Server Error` |

Response cho client nên ổn định và không lộ stack trace nội bộ.

### Lưu ý mục 2

- Chỉ catch khi có thể xử lý, chuyển đổi hoặc bổ sung context có ích.
- Catch subclass trước superclass.
- Dùng try-with-resources cho `AutoCloseable`.
- `finally` không tuyệt đối chạy trong mọi tình huống và không nên chứa `return`.
- `throw` thực sự ném exception; `throws` chỉ khai báo khả năng propagate.
- Không bắt `Exception` quá sớm rồi tiếp tục với state không hợp lệ.
- Không nuốt `InterruptedException`; cần restore interrupt flag hoặc propagate.
- Retry phải có giới hạn, backoff và chỉ áp dụng cho lỗi tạm thời.

---

## 3. User-defined Exceptions

Custom exception giúp biểu diễn lỗi bằng ngôn ngữ của domain thay vì để các implementation exception như `SQLException` hoặc `IOException` rò rỉ qua mọi tầng.

## 3.1 Tạo custom exception

### Unchecked custom exception

```java
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
    }

    public OrderNotFoundException(String orderId, Throwable cause) {
        super("Order not found: " + orderId, cause);
    }
}
```

### Checked custom exception

```java
public class ReportGenerationException extends Exception {

    public ReportGenerationException(String message) {
        super(message);
    }

    public ReportGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

### Constructor nên có

Tùy nhu cầu, custom exception thường cung cấp:

```java
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(Throwable cause) {
        super(cause);
    }
}
```

Không cần override `getMessage()` nếu chỉ lưu message thông thường.

## 3.2 Chọn checked hay unchecked

### Cân nhắc checked exception khi

- Caller được kỳ vọng phải đưa ra quyết định phục hồi.
- Lỗi là kết quả có thể dự đoán của operation.
- Việc ép caller catch/declare làm API rõ hơn, không tạo quá nhiều boilerplate.

Ví dụ: một thư viện đọc file chuyên dụng có thể khai báo checked exception cho file format không hợp lệ.

### Cân nhắc unchecked exception khi

- Vi phạm precondition hoặc invariant.
- Caller khó có thể phục hồi ngay tại chỗ.
- Exception sẽ được xử lý tập trung tại application boundary.
- Đây là domain/application exception trong service architecture.

Phần lớn custom exception trong web service hiện đại thường là unchecked để tránh lan truyền `throws` qua nhiều tầng, nhưng đây không phải quy tắc tuyệt đối.

### Không tạo custom exception nếu type chuẩn đã đủ nghĩa

```java
if (quantity < 0) {
    throw new IllegalArgumentException("quantity cannot be negative");
}
```

Không nhất thiết tạo `NegativeQuantityArgumentException` nếu caller không cần phân biệt riêng.

## 3.3 Ví dụ production

Giả sử service đặt chỗ tồn kho:

```java
public final class InventoryReservationException extends RuntimeException {

    private final String orderId;
    private final String sku;

    public InventoryReservationException(
            String orderId,
            String sku,
            String message
    ) {
        super(message);
        this.orderId = orderId;
        this.sku = sku;
    }

    public InventoryReservationException(
            String orderId,
            String sku,
            String message,
            Throwable cause
    ) {
        super(message, cause);
        this.orderId = orderId;
        this.sku = sku;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSku() {
        return sku;
    }
}
```

Service sử dụng exception:

```java
public class InventoryService {

    private final InventoryRepository repository;

    public InventoryService(InventoryRepository repository) {
        this.repository = repository;
    }

    public void reserve(String orderId, String sku, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "quantity must be greater than zero"
            );
        }

        try {
            int available = repository.getAvailableQuantity(sku);

            if (available < quantity) {
                throw new InventoryReservationException(
                        orderId,
                        sku,
                        "Insufficient inventory"
                );
            }

            repository.decreaseQuantity(sku, quantity);
        } catch (RepositoryException e) {
            throw new InventoryReservationException(
                    orderId,
                    sku,
                    "Cannot reserve inventory",
                    e
            );
        }
    }
}
```

Điểm quan trọng:

- Exception mang context cần thiết: `orderId`, `sku`.
- Không lưu object domain lớn hoặc dữ liệu nhạy cảm trong exception.
- Business failure và technical failure có thể dùng type riêng nếu caller cần phản ứng khác nhau.
- Technical cause được bảo toàn.

### Tách lỗi business và lỗi technical

Thiết kế rõ hơn:

```java
public class InsufficientInventoryException extends RuntimeException {
    public InsufficientInventoryException(String sku, int requested, int available) {
        super(
                "Insufficient inventory for sku=" + sku
                        + ", requested=" + requested
                        + ", available=" + available
        );
    }
}

public class InventoryUnavailableException extends RuntimeException {
    public InventoryUnavailableException(String sku, Throwable cause) {
        super("Inventory service unavailable for sku=" + sku, cause);
    }
}
```

Caller có thể:

- Trả `409 Conflict` cho `InsufficientInventoryException`.
- Trả `503 Service Unavailable` cho `InventoryUnavailableException`.

## 3.4 Exception translation

Mỗi layer nên giao tiếp bằng abstraction phù hợp với layer đó:

```text
Database driver
    throws SQLException
Repository
    translates to OrderRepositoryException
Service
    translates to OrderProcessingException khi cần
API boundary
    maps to stable error response
```

Ví dụ repository:

```java
public Order findById(String orderId) {
    try {
        return jdbcClient.queryForObject(
                "select * from orders where id = ?",
                orderMapper,
                orderId
        );
    } catch (SQLException e) {
        throw new OrderRepositoryException(
                "Cannot load orderId=" + orderId,
                e
        );
    }
}
```

Không nên chuyển đổi exception ở mọi method nếu type hiện tại đã đúng abstraction. Translation chỉ có ích khi:

- Che giấu implementation detail.
- Bổ sung domain context.
- Cho phép boundary phân loại lỗi.

### Error response ổn định

Không trả trực tiếp `exception.getMessage()` cho client vì message nội bộ có thể thay đổi hoặc chứa thông tin nhạy cảm.

```java
public record ErrorResponse(
        String code,
        String message,
        String correlationId
) {
}
```

Ví dụ response:

```json
{
  "code": "INVENTORY_NOT_AVAILABLE",
  "message": "Sản phẩm hiện không đủ số lượng",
  "correlationId": "req-7f3a1"
}
```

Stack trace và cause được giữ trong log nội bộ, gắn cùng `correlationId`.

### Lưu ý mục 3

- Custom exception nên thể hiện khái niệm có ý nghĩa với domain hoặc application layer.
- Kế thừa `Exception` cho checked exception, `RuntimeException` cho unchecked exception.
- Cung cấp constructor nhận `cause` để không mất nguyên nhân gốc.
- Không tạo quá nhiều exception nhỏ nếu caller không cần phân biệt.
- Không để exception tầng database/network rò rỉ trực tiếp qua API.
- Không đưa secret hoặc dữ liệu cá nhân nhạy cảm vào message.
- Response cho client cần ổn định; stack trace chỉ nên nằm trong log nội bộ.

