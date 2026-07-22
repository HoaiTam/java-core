# Java Core 03 - String, Array and Date/Time

> Tổng hợp từ slide 2 đến slide 32, bao gồm String, Array và Date/Time API.

## Mục lục

- [Mục tiêu](#mục-tiêu)
- [1. Working with Strings](#1-working-with-strings)
  - [1.1 String class](#11-string-class)
  - [1.2 Tạo, nối và bổ sung chuỗi](#12-tạo-nối-và-bổ-sung-chuỗi)
  - [1.3 So sánh String](#13-so-sánh-string)
  - [1.4 Các method thường dùng](#14-các-method-thường-dùng)
- [2. Working with Arrays](#2-working-with-arrays)
  - [2.1 Tổng quan](#21-tổng-quan)
  - [2.2 Ưu và nhược điểm](#22-ưu-và-nhược-điểm)
  - [2.3 Mảng một chiều](#23-mảng-một-chiều)
  - [2.4 Mảng hai chiều](#24-mảng-hai-chiều)
  - [2.5 Lớp Arrays](#25-lớp-arrays)
  - [2.6 So sánh hai mảng](#26-so-sánh-hai-mảng)
- [3. Working with Date/Time APIs](#3-working-with-datetime-apis)
  - [3.1 Java Date/Time API](#31-java-datetime-api)
  - [3.2 Các kiểu dữ liệu quan trọng](#32-các-kiểu-dữ-liệu-quan-trọng)
  - [3.3 Factory method và parse](#33-factory-method-và-parse)
  - [3.4 Tạo và thay đổi date/time object](#34-tạo-và-thay-đổi-datetime-object)
  - [3.5 Format ngày](#35-format-ngày)
  - [3.6 Format ngày giờ, thời gian và locale](#36-format-ngày-giờ-thời-gian-và-locale)
  - [3.7 Time zone, Period và Duration](#37-time-zone-period-và-duration)

## Mục tiêu

Phần này tập trung vào ba nhóm kiến thức:

1. Tạo, nối, so sánh và xử lý `String`.
2. Khai báo, duyệt, sao chép, sắp xếp và so sánh array.
3. Tạo, điều chỉnh, format và tính toán ngày giờ bằng `java.time`.

## 1. Working with Strings

Trong Java, `String` là class đại diện cho một chuỗi ký tự. `String` không phải primitive type mà là reference type thuộc package `java.lang`.

```java
String greeting = "Hello";
```

Khác với mảng ký tự trong C, Java `String` là object và cung cấp sẵn nhiều method để tìm kiếm, so sánh, cắt, thay thế và chuyển đổi nội dung.

Đặc điểm quan trọng nhất: **String là immutable**. Sau khi một object `String` được tạo, nội dung bên trong object đó không thay đổi. Mọi thao tác nhìn giống như sửa chuỗi thực chất tạo hoặc trả về một chuỗi khác.

```java
String name = "java";
name.toUpperCase();

System.out.println(name); // java

name = name.toUpperCase();
System.out.println(name); // JAVA
```

Lần gọi đầu không thay đổi object mà `name` đang trỏ đến. Muốn dùng kết quả mới, phải gán lại hoặc lưu vào biến khác.

## 1.1 String class

### Tạo String bằng literal

```java
String language = "java";
String empty = "";
```

Đây là cách được dùng phổ biến nhất. String literal có thể được JVM tái sử dụng thông qua String Pool.

### Tạo String bằng constructor

```java
String empty = new String();
String language = new String("java");

char[] characters = {'j', 'a', 'v', 'a'};
String fromCharacters = new String(characters);
```

Các constructor trên hợp lệ, nhưng production code thường không viết `new String("java")` vì nó tạo thêm object không cần thiết. Nên dùng literal:

```java
String language = "java";
```

Slide minh họa `str = "Hello word"`; nếu muốn câu chào thông thường thì nội dung đúng là `"Hello world"`.

### String Pool

Các literal giống nhau thường trỏ đến cùng object trong String Pool:

```java
String first = "java";
String second = "java";

System.out.println(first == second); // thường là true do String Pool
```

Không nên dựa vào đặc điểm này để so sánh nội dung. Dữ liệu đọc từ database, HTTP request, file hoặc được tạo bằng `new String(...)` không nhất thiết dùng cùng reference.

### `null`, chuỗi rỗng và chuỗi trắng

Ba trạng thái này khác nhau:

```java
String missing = null; // Không trỏ tới String nào
String empty = "";     // length() == 0
String blank = "   ";  // Có ký tự khoảng trắng
```

Kiểm tra:

```java
if (value == null) {
    // Không có object
}

if (value != null && value.isEmpty()) {
    // Chuỗi có độ dài bằng 0
}

if (value != null && value.isBlank()) {
    // Rỗng hoặc chỉ chứa khoảng trắng
}
```

`isBlank()` phù hợp hơn `isEmpty()` trong nhiều validation nhập liệu vì chuỗi chỉ có khoảng trắng thường cũng không hợp lệ.

## 1.2 Tạo, nối và bổ sung chuỗi

### Gán và dùng chung reference

```java
String first = "";
String second = "hello java";
String third = second;
```

`third = second` không sao chép nội dung sang một vùng nhớ mới. Hai biến cùng trỏ đến một object `String`. Vì `String` immutable nên việc dùng chung object này an toàn.

### Nối chuỗi bằng toán tử `+`

```java
int age = 25;
String message = "Age: " + age;

System.out.println(message); // Age: 25
```

Thứ tự đánh giá cần được chú ý:

```java
System.out.println("Total: " + 1 + 2);   // Total: 12
System.out.println("Total: " + (1 + 2)); // Total: 3
```

Khi Java gặp một operand là `String`, các phép `+` tiếp theo được dùng để nối chuỗi.

### `+=` không sửa object String

```java
String name = "java";
name += " ";
```

Đoạn code tạo một chuỗi mới `"java "` rồi gán reference mới cho `name`. Object `"java"` ban đầu vẫn không thay đổi.

### `String.join()`

Khi cần nối nhiều thành phần bằng một delimiter cố định:

```java
String fullName = String.join(" ", "Nguyen", "Van", "An");
System.out.println(fullName); // Nguyen Van An
```

Ví dụ tạo dòng CSV đơn giản:

```java
String csvLine = String.join(",", orderId, customerId, status);
```

Nếu dữ liệu có thể chứa dấu phẩy, dấu nháy hoặc xuống dòng thì không nên tự tạo CSV bằng `String.join`; cần dùng thư viện CSV để escape đúng chuẩn.

### `StringBuilder` cho thao tác nối lặp lại

Nối chuỗi trong loop bằng `+` có thể tạo nhiều object tạm:

```java
String result = "";

for (String item : items) {
    result += item;
}
```

Nên dùng `StringBuilder`:

```java
StringBuilder builder = new StringBuilder();

for (String item : items) {
    if (!builder.isEmpty()) {
        builder.append(", ");
    }
    builder.append(item);
}

String result = builder.toString();
```

`StringBuilder` là mutable và không thread-safe. Thường tạo nó như local variable trong một method. Nếu chỉ cần nối collection với delimiter, có thể dùng `String.join()` hoặc `Collectors.joining()`.

### Ví dụ production: tạo nội dung log có kiểm soát

```java
public String summarizeOrder(Order order) {
    return new StringBuilder()
            .append("Order{id=")
            .append(order.id())
            .append(", status=")
            .append(order.status())
            .append(", itemCount=")
            .append(order.items().size())
            .append('}')
            .toString();
}
```

Không đưa password, token, số thẻ hoặc dữ liệu cá nhân nhạy cảm vào chuỗi log.

## 1.3 So sánh String

### `==` và `equals()`

```java
String s1 = "java";
String s2 = "java";
String s3 = new String("java");
String s4 = "JAVA";

System.out.println(s1 == s2);      // true: thường cùng object trong pool
System.out.println(s1 == s3);      // false: khác reference
System.out.println(s1 == s4);      // false

System.out.println(s1.equals(s2)); // true
System.out.println(s1.equals(s3)); // true
System.out.println(s1.equals(s4)); // false
```

- `==` so sánh hai reference có cùng trỏ đến một object hay không.
- `equals()` so sánh nội dung và phân biệt chữ hoa/thường.

Trong production, gần như luôn dùng `equals()` khi mục tiêu là so sánh nội dung String.

### `equalsIgnoreCase()`

```java
System.out.println(s1.equalsIgnoreCase(s2)); // true
System.out.println(s1.equalsIgnoreCase(s3)); // true
System.out.println(s1.equalsIgnoreCase(s4)); // true
```

Method này phù hợp với mã hoặc lựa chọn không phân biệt hoa/thường. Không nên tự động dùng nó cho mọi văn bản tự nhiên vì quy tắc chữ hoa có thể phụ thuộc locale và nghiệp vụ.

### So sánh an toàn với `null`

```java
if ("PAID".equals(actualStatus)) {
    // Không lỗi nếu actualStatus là null
}
```

Hoặc dùng:

```java
boolean same = Objects.equals(firstValue, secondValue);
```

Với trạng thái cố định, ưu tiên `enum` thay vì String:

```java
if (order.status() == OrderStatus.PAID) {
    invoiceService.issue(order);
}
```

### `compareTo()`

`compareTo()` so sánh chuỗi theo thứ tự từ điển dựa trên giá trị Unicode:

```java
String s1 = "Hello";
String s2 = "Hello";
String s3 = "Hi";

System.out.println(s1.compareTo(s2)); // 0
System.out.println(s1.compareTo(s3)); // Giá trị âm
```

Ý nghĩa kết quả:

- Bằng `0`: hai chuỗi bằng nhau theo thứ tự so sánh.
- Nhỏ hơn `0`: chuỗi bên trái đứng trước chuỗi bên phải.
- Lớn hơn `0`: chuỗi bên trái đứng sau chuỗi bên phải.

Với `"Hello"` và `"Hi"`, kết quả trong ví dụ hiện tại là `-4`, nhưng production code chỉ nên dựa vào dấu âm/0/dương, không phụ thuộc chính xác con số `-4`.

Để sắp xếp theo quy tắc ngôn ngữ của người dùng, không nên chỉ dùng `compareTo()`; có thể cần `Collator` với locale phù hợp.

### `startsWith()` và `endsWith()`

```java
String value = "java string";

System.out.println(value.startsWith("ja")); // true
System.out.println(value.endsWith("ing"));  // true
```

Các method này phân biệt hoa/thường.

## 1.4 Các method thường dùng

| Method | Ý nghĩa |
|---|---|
| `length()` | Trả về số UTF-16 code unit trong chuỗi |
| `substring(beginIndex)` | Lấy chuỗi từ `beginIndex` đến hết |
| `substring(beginIndex, endIndex)` | Lấy từ begin inclusive đến end exclusive |
| `contains(sequence)` | Kiểm tra có chứa chuỗi con hay không |
| `isEmpty()` | Kiểm tra độ dài bằng 0 |
| `isBlank()` | Kiểm tra rỗng hoặc chỉ có khoảng trắng |
| `replace(old, replacement)` | Thay tất cả phần khớp literal |
| `split(regex)` | Tách chuỗi theo regular expression |
| `trim()` | Bỏ khoảng trắng kiểu cũ ở hai đầu |
| `strip()` | Bỏ Unicode whitespace ở hai đầu |
| `toLowerCase()` / `toUpperCase()` | Chuyển hoa/thường |
| `String.valueOf(value)` | Chuyển nhiều kiểu dữ liệu thành String |

### `length()` và Unicode

```java
String text = "Java";
System.out.println(text.length()); // 4
```

`length()` trả số UTF-16 code unit, không phải lúc nào cũng bằng số ký tự người dùng nhìn thấy. Một số Unicode character cần surrogate pair và có thể làm `length()` trả về `2` cho một ký tự hiển thị.

### `substring()`

```java
String orderCode = "ORD-2026-001";

String year = orderCode.substring(4, 8);
System.out.println(year); // 2026
```

Index bắt đầu từ `0`; `beginIndex` được lấy, còn `endIndex` không được lấy. Index sai gây `StringIndexOutOfBoundsException`.

### `contains()`

```java
boolean containsJava = "Learn Java Core".contains("Java");
```

`contains()` phân biệt hoa/thường và chỉ trả về boolean. Nếu cần vị trí, dùng `indexOf()`.

### `replace()`

```java
String normalizedPhone = "090-123-4567".replace("-", "");
```

`replace()` xử lý literal. `replaceAll()` nhận regular expression, nên cần cẩn thận với ký tự đặc biệt regex.

### `split()` nhận regex

```java
String[] parts = "java,spring,mysql".split(",");
```

Dấu chấm là ký tự đặc biệt trong regex:

```java
String[] versionParts = "17.0.10".split("\\.");
```

Không viết `split(".")` nếu muốn tách theo dấu chấm vì `.` trong regex khớp gần như mọi ký tự.

### `String.valueOf()`

```java
String quantity = String.valueOf(100);
String active = String.valueOf(true);
```

`String.valueOf((Object) null)` trả về chuỗi `"null"`. Cần cân nhắc nếu giá trị này được gửi ra API hoặc hiển thị cho người dùng.

### Ví dụ production: chuẩn hóa mã tìm kiếm

```java
public String normalizeSearchKeyword(String keyword) {
    if (keyword == null) {
        return "";
    }

    return keyword
            .strip()
            .toLowerCase(Locale.ROOT)
            .replaceAll("\\s+", " ");
}
```

`Locale.ROOT` giúp việc chuẩn hóa kỹ thuật có kết quả ổn định, không phụ thuộc locale mặc định trên server.

### Lưu ý mục 1

- `String` là immutable; method xử lý chuỗi không sửa object gốc.
- Ưu tiên String literal thay cho `new String("...")`.
- Không dùng `==` để so sánh nội dung String; dùng `equals()`, `equalsIgnoreCase()` hoặc `Objects.equals()` tùy nghiệp vụ.
- Phân biệt `null`, `""` và chuỗi chỉ có khoảng trắng.
- Nối ít thành phần có thể dùng `+`; nối lặp lại trong loop nên dùng `StringBuilder`.
- `split()` và `replaceAll()` dùng regex, cần escape ký tự đặc biệt.
- Không log secret hoặc dữ liệu cá nhân nhạy cảm khi xây dựng chuỗi log.
- Với mã trạng thái cố định, ưu tiên `enum` thay vì String.

## 2. Working with Arrays

Array là object chứa một số lượng phần tử cố định có cùng kiểu khai báo. Mỗi phần tử được truy cập bằng index bắt đầu từ `0`.

```java
int[] scores = {80, 90, 75};

System.out.println(scores[0]);      // 80
System.out.println(scores.length);  // 3
```

Mảng rỗng hoàn toàn hợp lệ:

```java
int[] empty = new int[0];
```

Vì vậy, mô tả “array chứa một hoặc nhiều phần tử” trên slide chưa đầy đủ; array có thể chứa **không hoặc nhiều phần tử**.

## 2.1 Tổng quan

### Index và length

Với array có độ dài `n`:

- Index đầu tiên là `0`.
- Index cuối cùng là `n - 1`.
- `array.length` là field, không phải method.

```java
int[] values = new int[5];

values[0] = 10;
values[4] = 50;

// values[5] = 60; // ArrayIndexOutOfBoundsException
```

Không nhầm:

```java
text.length();  // String dùng method
values.length;  // Array dùng field
```

### Giá trị mặc định

Khi tạo array bằng `new`, Java gán giá trị mặc định:

| Kiểu phần tử | Giá trị mặc định |
|---|---|
| `byte`, `short`, `int`, `long` | `0` |
| `float`, `double` | `0.0` |
| `boolean` | `false` |
| `char` | `'\u0000'` |
| Reference type | `null` |

```java
String[] names = new String[3];
System.out.println(names[0]); // null
```

### Array là reference type

```java
int[] first = {1, 2, 3};
int[] second = first;

second[0] = 99;
System.out.println(first[0]); // 99
```

Hai biến cùng trỏ đến một array. Gán reference không tạo bản sao.

## 2.2 Ưu và nhược điểm

### Ưu điểm

- Truy cập ngẫu nhiên theo index có độ phức tạp `O(1)`.
- Cấu trúc đơn giản, ít overhead hơn nhiều collection.
- Phù hợp với dữ liệu có kích thước cố định hoặc buffer hiệu năng cao.
- Primitive array như `byte[]`, `int[]` lưu trực tiếp primitive, tránh boxing.

### Nhược điểm

- Kích thước cố định sau khi tạo.
- Chèn hoặc xóa giữa mảng thường cần dịch chuyển phần tử.
- Chỉ biểu diễn trực tiếp các phần tử cùng kiểu khai báo.
- API thao tác ít linh hoạt hơn `List`, `Set` và các collection khác.
- Phải tự quản lý index, dễ gặp lỗi off-by-one.

### Khi nào nên dùng array?

Array phù hợp khi:

- Kích thước đã biết và ít thay đổi.
- Làm việc với binary data bằng `byte[]`.
- Tương tác với API yêu cầu array.
- Dữ liệu dạng ma trận hoặc bảng nhỏ, cố định.
- Đo đạc hiệu năng cho thấy array thực sự cần thiết.

Nếu cần thêm/xóa phần tử thường xuyên, production code thường dùng `ArrayList` hoặc collection phù hợp.

## 2.3 Mảng một chiều

### Khai báo

Cả hai cú pháp đều hợp lệ:

```java
int[] preferred;
int alternative[];
```

Nên dùng `int[] preferred` vì dấu `[]` nằm cạnh type và dễ đọc hơn.

### Khởi tạo theo kích thước

```java
int[] numbers = new int[5];
numbers[0] = 1;
```

Các phần tử chưa gán còn lại có giá trị mặc định `0`.

### Khởi tạo bằng danh sách giá trị

```java
int[] first = {1, 2, 3, 9, 10};
int[] second = new int[]{1, 2, 3, 9, 10};

System.out.println(first[3]); // 9
```

Dạng `new int[]{...}` hữu ích khi truyền array trực tiếp vào method:

```java
process(new int[]{1, 2, 3});
```

### Duyệt bằng `for`

```java
double[] values = {1.9, 2.9, 3.4, 3.5};
double total = 0;

for (int index = 0; index < values.length; index++) {
    total += values[index];
}

System.out.println("Total is " + total);
```

Dùng `for` truyền thống khi cần index hoặc cần thay đổi phần tử theo vị trí.

```java
for (int index = 0; index < values.length; index++) {
    values[index] *= 2;
}
```

### Enhanced for

```java
for (double element : values) {
    System.out.println(element);
}
```

Enhanced for phù hợp khi chỉ cần đọc từng phần tử. Gán lại biến `element` không sửa phần tử primitive trong array:

```java
for (double element : values) {
    element = 0; // Chỉ đổi local variable element
}
```

### Ví dụ production: tính tổng batch

```java
public long calculateTotal(long[] amounts) {
    if (amounts == null) {
        throw new IllegalArgumentException("Amounts are required");
    }

    long total = 0;
    for (long amount : amounts) {
        total = Math.addExact(total, amount);
    }
    return total;
}
```

`Math.addExact()` phát hiện overflow thay vì để kết quả âm thầm quay vòng.

## 2.4 Mảng hai chiều

Java không có matrix đặc biệt ở mức ngôn ngữ. `int[][]` thực chất là **array chứa các `int[]` khác**.

### Tạo mảng hình chữ nhật

```java
int[][] matrix = new int[4][2];
```

Mảng có 4 row, mỗi row có 2 phần tử.

```java
matrix[0][0] = 1;
matrix[0][1] = 4;
matrix[1][0] = 5;
matrix[1][1] = 6;
matrix[2][0] = 7;
matrix[2][1] = 2;
matrix[3][0] = 6;
matrix[3][1] = 9;
```

### Khởi tạo trực tiếp

```java
int[][] matrix = {
        {1, 4},
        {5, 6},
        {7, 2},
        {6, 9}
};
```

Hoặc:

```java
int[][] matrix = new int[][]{
        {1, 4},
        {5, 6},
        {7, 2},
        {6, 9}
};
```

### Jagged array

Vì mỗi row là một array riêng, chúng có thể có độ dài khác nhau:

```java
int[][] triangle = {
        {1},
        {2, 3},
        {4, 5, 6}
};
```

Không được giả định mọi row có cùng độ dài nếu dữ liệu có thể là jagged array.

### Duyệt mảng hai chiều

```java
for (int row = 0; row < matrix.length; row++) {
    for (int column = 0; column < matrix[row].length; column++) {
        System.out.printf(
                "Index [%d, %d]: %d%n",
                row,
                column,
                matrix[row][column]);
    }
}
```

`matrix.length` là số row; `matrix[row].length` là số phần tử của row hiện tại.

Enhanced for:

```java
for (int[] row : matrix) {
    for (int value : row) {
        System.out.println(value);
    }
}
```

### Ví dụ production: bảng phí vận chuyển

```java
int[][] shippingFees = {
        {20_000, 30_000}, // Khu vực 0: tiêu chuẩn, nhanh
        {25_000, 40_000}, // Khu vực 1
        {35_000, 55_000}  // Khu vực 2
};

int fee = shippingFees[regionIndex][serviceIndex];
```

Index số khó diễn giải và dễ truyền sai. Với business domain phức tạp, nên dùng enum, object hoặc `Map` thay vì dựa hoàn toàn vào “magic index”.

## 2.5 Lớp Arrays

`java.util.Arrays` cung cấp các static method để điền, sao chép, sắp xếp, tìm kiếm, so sánh và hiển thị array.

```java
import java.util.Arrays;
```

### Gán reference không phải sao chép

```java
double[] grades = {92.2, 80.5, 69.5, 72.0};
double[] percentages = grades;

percentages[3] = 70.0;
System.out.println(grades[3]); // 70.0
```

`grades` và `percentages` cùng trỏ đến một array.

### `Arrays.copyOf()`

```java
double[] grades = {92.2, 80.5, 69.5, 72.0};
double[] percentages = Arrays.copyOf(grades, grades.length);

percentages[3] = 70.0;
System.out.println(grades[3]); // 72.0
```

Bản sao có array object riêng. Với object array, đây vẫn là shallow copy: các phần tử reference bên trong có thể cùng trỏ đến object cũ.

### `Arrays.copyOfRange()`

```java
double[] lowestGrades = Arrays.copyOfRange(grades, 0, 2);
```

Index đầu được lấy, index cuối không được lấy. Kết quả chứa `grades[0]` và `grades[1]`.

### `Arrays.sort()`

```java
Arrays.sort(grades);
```

`sort()` thay đổi trực tiếp thứ tự trong array gốc. Nếu cần giữ thứ tự cũ, sao chép trước khi sort:

```java
double[] sortedGrades = Arrays.copyOf(grades, grades.length);
Arrays.sort(sortedGrades);
```

### `Arrays.fill()`

```java
int[] counters = new int[5];
Arrays.fill(counters, -1);
```

### `Arrays.binarySearch()`

```java
int[] numbers = {9, 2, 7, 1};
Arrays.sort(numbers);

int index = Arrays.binarySearch(numbers, 7);
```

Array phải được sắp xếp theo cùng thứ tự trước khi binary search. Nếu không tìm thấy, method trả về số âm biểu diễn insertion point, không đơn giản luôn là `-1`.

### `Arrays.toString()` và `deepToString()`

```java
System.out.println(Arrays.toString(numbers));
System.out.println(Arrays.deepToString(matrix));
```

In array trực tiếp thường không cho nội dung hữu ích:

```java
System.out.println(numbers); // Dạng như [I@6d03e736
```

## 2.6 So sánh hai mảng

### `==` so sánh reference

```java
int[] first = {0, 2, 4, 6};
int[] second = {0, 2, 4, 6};

System.out.println(first == second); // false
```

Hai array có nội dung giống nhau nhưng là hai object khác nhau.

### `Arrays.equals()` so sánh nội dung

```java
int[] first = {0, 2, 4, 6};
int[] second = {0, 2, 4, 6};
int[] third = {10, 8, 6, 4};

System.out.println(Arrays.equals(first, second)); // true
System.out.println(Arrays.equals(first, third));  // false
```

Thứ tự phần tử có ý nghĩa. Hai array chứa cùng tập giá trị nhưng thứ tự khác nhau vẫn không bằng nhau.

### Mảng nhiều chiều

Với nested array, dùng `Arrays.deepEquals()`:

```java
int[][] firstMatrix = {{1, 2}, {3, 4}};
int[][] secondMatrix = {{1, 2}, {3, 4}};

System.out.println(Arrays.equals(firstMatrix, secondMatrix));     // false
System.out.println(Arrays.deepEquals(firstMatrix, secondMatrix)); // true
```

`Arrays.equals()` trên `int[][]` chỉ so sánh reference của từng row; `deepEquals()` đi sâu vào nested array.

### Defensive copy trong production

Array là mutable. Nếu class lưu trực tiếp array do caller truyền vào hoặc trả array nội bộ ra ngoài, code khác có thể sửa state của class:

```java
public final class ScoreBoard {
    private final int[] scores;

    public ScoreBoard(int[] scores) {
        if (scores == null) {
            throw new IllegalArgumentException("Scores are required");
        }
        this.scores = Arrays.copyOf(scores, scores.length);
    }

    public int[] getScores() {
        return Arrays.copyOf(scores, scores.length);
    }
}
```

Đây là defensive copy: object không giữ reference mutable từ bên ngoài và cũng không lộ array nội bộ.

### Lưu ý mục 2

- Array là object có kích thước cố định; index hợp lệ từ `0` đến `length - 1`.
- `array.length` là field, còn `String.length()` là method.
- Gán một biến array cho biến khác chỉ sao chép reference.
- Dùng `Arrays.copyOf()` khi cần array object riêng; với object array, đây vẫn là shallow copy.
- `Arrays.sort()` thay đổi array gốc và `binarySearch()` yêu cầu dữ liệu đã sort đúng cách.
- Dùng `Arrays.equals()` cho mảng một chiều và `Arrays.deepEquals()` cho nested array.
- Dùng defensive copy khi nhận hoặc trả array mutable qua public API.
- Nếu kích thước thay đổi thường xuyên, ưu tiên collection như `ArrayList`.
- Với nested loop trên array lớn, chú ý độ phức tạp `O(n²)` và khả năng tiêu thụ bộ nhớ.

## 3. Working with Date/Time APIs

Java không có primitive type dành riêng cho ngày và giờ. Date/time được biểu diễn bằng object.

Trước Java 8, code thường dùng:

- `java.util.Date`.
- `java.util.Calendar`.
- `java.text.SimpleDateFormat`.

Các API cũ có thiết kế khó dùng, mutable và một số formatter không thread-safe. Từ Java 8, package `java.time` cung cấp API rõ ràng hơn, immutable và thread-safe.

```java
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
```

## 3.1 Java Date/Time API

### API cũ trước Java 8

Ví dụ cộng hai giờ bằng `Calendar`:

```java
Calendar calendar = Calendar.getInstance();
calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 2);
```

`Calendar` là mutable: method thay đổi trực tiếp object hiện tại. Các constant và quy tắc của API cũ cũng dễ dùng nhầm, ví dụ tháng từng được đánh số từ `0`.

### API `java.time`

```java
LocalTime now = LocalTime.now();
LocalTime later = now.plusHours(2);
```

`LocalTime` immutable. `plusHours(2)` không sửa `now` mà trả về object mới.

```java
System.out.println(now);
System.out.println(later);
```

### Ưu điểm

- Immutable và thread-safe.
- Tên class, method rõ nghĩa hơn.
- Phân biệt ngày, giờ, múi giờ và thời điểm tuyệt đối.
- Hỗ trợ parse/format bằng `DateTimeFormatter` thread-safe.
- Hỗ trợ locale, time zone, date-based amount và time-based amount.

### Tương thích

`java.time` có từ Java 8. Đây không còn là nhược điểm đáng kể với ứng dụng Java hiện đại, nhưng hệ thống legacy chạy Java 7 trở xuống không dùng trực tiếp được API này.

Khi tích hợp code cũ, có thể cần chuyển đổi giữa `java.util.Date` và `Instant`:

```java
Date legacyDate = new Date();
Instant instant = legacyDate.toInstant();

Date convertedBack = Date.from(instant);
```

## 3.2 Các kiểu dữ liệu quan trọng

| Class/enum | Ý nghĩa | Có date? | Có time? | Có zone/offset? |
|---|---|---:|---:|---:|
| `LocalDate` | Một ngày theo lịch, ví dụ `2026-07-22` | Có | Không | Không |
| `LocalTime` | Giờ trong ngày, ví dụ `17:30:20` | Không | Có | Không |
| `LocalDateTime` | Ngày và giờ địa phương | Có | Có | Không |
| `Instant` | Một thời điểm tuyệt đối trên UTC timeline | Có tính thời điểm | Có | UTC |
| `OffsetDateTime` | Ngày giờ kèm UTC offset | Có | Có | Offset như `+07:00` |
| `ZonedDateTime` | Ngày giờ kèm region time zone | Có | Có | Zone như `Asia/Ho_Chi_Minh` |
| `Month` | Enum từ `JANUARY` đến `DECEMBER` | - | - | - |
| `DayOfWeek` | Enum từ `MONDAY` đến `SUNDAY` | - | - | - |
| `Period` | Khoảng cách theo năm, tháng, ngày | - | - | - |
| `Duration` | Khoảng cách theo giây và nano giây | - | - | - |

Slide ghi `TimeDate` và `DateOfWeek`; tên class/enum đúng trong Java là `LocalTime` và `DayOfWeek`.

### Chọn đúng type

```java
LocalDate birthday = LocalDate.of(1998, Month.JULY, 10);
LocalTime openingTime = LocalTime.of(8, 30);
LocalDateTime appointment = LocalDateTime.of(2026, 7, 22, 14, 0);
```

- Ngày sinh, ngày hết hạn: `LocalDate`.
- Giờ mở cửa lặp lại mỗi ngày: `LocalTime`.
- Ngày giờ không gắn múi giờ: `LocalDateTime`.
- Timestamp tạo/cập nhật bản ghi: thường dùng `Instant`.
- Lịch họp gắn với thành phố/múi giờ: `ZonedDateTime`.

`LocalDateTime` không chứa zone hoặc offset. Giá trị `2026-07-22T09:00` có thể là những instant khác nhau tại Việt Nam, Paris hoặc New York.

## 3.3 Factory method và parse

Các class `java.time` thường được tạo qua static factory method thay vì constructor public.

### `now()`

```java
LocalDate currentDate = LocalDate.now();
LocalTime currentTime = LocalTime.now();
LocalDateTime currentDateTime = LocalDateTime.now();
Instant currentInstant = Instant.now();
```

`LocalDate.now()` và các method local tương tự dùng clock và default time zone của hệ thống. Kết quả có thể khác giữa máy local và server nếu zone khác nhau.

Có thể chỉ định zone:

```java
ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
LocalDate vietnamToday = LocalDate.now(vietnamZone);
```

### `of(...)`

```java
LocalDate date = LocalDate.of(2017, 7, 10);
LocalDate readableDate = LocalDate.of(2017, Month.JULY, 10);

LocalTime time = LocalTime.of(17, 30, 20);

LocalDateTime dateTime = LocalDateTime.of(
        2017,
        Month.JULY,
        10,
        17,
        30,
        20);
```

Giá trị không hợp lệ gây `DateTimeException`:

```java
// LocalDate.of(2026, 2, 30); // Invalid date
// LocalTime.of(25, 0);       // Invalid hour
```

### `parse(...)`

Các method `parse(String)` mặc định đọc định dạng ISO phù hợp:

```java
LocalDate date = LocalDate.parse("2017-07-10");
LocalTime time = LocalTime.parse("17:30:20");
LocalDateTime dateTime = LocalDateTime.parse("2017-07-10T17:30:20");
```

Chuỗi không đúng định dạng hoặc ngày không tồn tại gây `DateTimeParseException`.

Với định dạng tùy chỉnh:

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");
LocalDate date = LocalDate.parse("22/07/2026", formatter);
```

Trong pattern dành cho năm theo lịch, thường ưu tiên `uuuu` thay cho `yyyy`; `yyyy` biểu diễn year-of-era và có thể cần era khi xử lý strict parsing.

## 3.4 Tạo và thay đổi date/time object

Các object trong `java.time` immutable. Method `with...`, `plus...`, `minus...` trả về object mới.

### `with...()` thay một thành phần

```java
LocalDate original = LocalDate.of(2017, Month.JULY, 10);

LocalDate endOfMonth = original.withDayOfMonth(31);
LocalDate dayOfYear = original.withDayOfYear(32);

System.out.println(original);   // 2017-07-10
System.out.println(endOfMonth); // 2017-07-31
System.out.println(dayOfYear);  // 2017-02-01
```

Object `original` không thay đổi.

```java
LocalDateTime dateTime = LocalDateTime.parse("2017-07-31T15:30");
LocalDateTime october = dateTime.withMonth(10);

System.out.println(october); // 2017-10-31T15:30
```

Khi thay năm hoặc tháng làm ngày hiện tại không hợp lệ, một số method điều chỉnh về ngày hợp lệ cuối tháng. Ví dụ `2017-01-31.withMonth(2)` trở thành `2017-02-28`. Business rule cần xem hành vi này có phù hợp hay không.

### `plus...()` và `minus...()`

```java
LocalDate today = LocalDate.now();

LocalDate nextWeek = today.plusWeeks(1);
LocalDate previousMonth = today.minusMonths(1);
LocalDate inTenDays = today.plusDays(10);
```

Có thể dùng `ChronoUnit`:

```java
LocalDate nextWeek = today.plus(1, ChronoUnit.WEEKS);
```

Method chuyên biệt như `plusWeeks(1)` thường dễ đọc hơn.

### Ví dụ production: dùng `Clock` để test ổn định

Gọi `now()` trực tiếp khiến unit test phụ thuộc thời gian thật. Inject `Clock` giúp cố định thời gian:

```java
public class OrderService {
    private final Clock clock;

    public OrderService(Clock clock) {
        this.clock = clock;
    }

    public Order createOrder(String customerId) {
        Instant createdAt = Instant.now(clock);
        return new Order(customerId, createdAt);
    }
}
```

Trong production:

```java
Clock clock = Clock.systemUTC();
```

Trong test:

```java
Clock fixedClock = Clock.fixed(
        Instant.parse("2026-07-22T03:00:00Z"),
        ZoneOffset.UTC);
```

## 3.5 Format ngày

`DateTimeFormatter` dùng để format object thành String và parse String thành object. Formatter immutable và thread-safe nên có thể tái sử dụng.

### Localized formatter

| Factory method | Dùng cho |
|---|---|
| `ofLocalizedDate(dateStyle)` | Chỉ date |
| `ofLocalizedTime(timeStyle)` | Chỉ time |
| `ofLocalizedDateTime(dateTimeStyle)` | Date và time cùng style |
| `ofLocalizedDateTime(dateStyle, timeStyle)` | Date và time có style riêng |

Các style trong `FormatStyle`:

- `SHORT`.
- `MEDIUM`.
- `LONG`.
- `FULL`.

### Format LocalDate

```java
LocalDate date = LocalDate.now();

DateTimeFormatter shortFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
DateTimeFormatter mediumFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);
DateTimeFormatter longFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);
DateTimeFormatter fullFormatter =
        DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL);

System.out.println(shortFormatter.format(date));
System.out.println(mediumFormatter.format(date));
System.out.println(longFormatter.format(date));
System.out.println(fullFormatter.format(date));
```

Kết quả phụ thuộc locale. Với locale Mỹ có thể thấy:

```text
7/22/26
Jul 22, 2026
July 22, 2026
Wednesday, July 22, 2026
```

Không nên viết test khẳng định chính xác localized output nếu không đặt locale rõ ràng.

```java
DateTimeFormatter formatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale.US);
```

### Custom pattern

```java
DateTimeFormatter apiFormatter =
        DateTimeFormatter.ofPattern("uuuu-MM-dd");

String formatted = apiFormatter.format(LocalDate.of(2026, 7, 22));
System.out.println(formatted); // 2026-07-22
```

Với API và trao đổi giữa hệ thống, ưu tiên ISO-8601 thay vì format hiển thị phụ thuộc locale.

## 3.6 Format ngày giờ, thời gian và locale

### LocalDateTime

```java
LocalDate date = LocalDate.now();
LocalTime time = LocalTime.of(17, 30, 20);
LocalDateTime dateTime = LocalDateTime.of(date, time);

DateTimeFormatter shortFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
DateTimeFormatter mediumFormatter =
        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

System.out.println(shortFormatter.format(dateTime));
System.out.println(mediumFormatter.format(dateTime));
```

### LocalTime

```java
LocalTime time = LocalTime.of(17, 30, 20);

DateTimeFormatter shortTime =
        DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
DateTimeFormatter mediumTime =
        DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);

System.out.println(shortTime.format(time));
System.out.println(mediumTime.format(time));
```

`FormatStyle.LONG` và `FULL` cho time thường cần thông tin time zone. Format một `LocalTime` không có zone bằng các style đó có thể gây `DateTimeException`, đúng như bảng trên slide. Nếu cần zone name, dùng `ZonedDateTime`.

### Chỉ định locale

```java
DateTimeFormatter germanFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.MEDIUM)
        .withLocale(Locale.GERMAN);

System.out.println(germanFormatter.format(dateTime));
```

Với Indian English:

```java
Locale indianEnglish = Locale.forLanguageTag("en-IN");

DateTimeFormatter indianFormatter = DateTimeFormatter
        .ofLocalizedDateTime(FormatStyle.SHORT)
        .withLocale(indianEnglish);
```

`new Locale("en", "IN")` trên slide vẫn dùng được, nhưng `Locale.forLanguageTag("en-IN")` thể hiện chuẩn BCP 47 rõ ràng hơn.

### Phân biệt dữ liệu và cách hiển thị

Date/time object giữ giá trị thời gian, không giữ “format”. Format chỉ được áp dụng khi chuyển sang String:

```java
LocalDate date = LocalDate.of(2026, 7, 22);

String vietnameseDisplay = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.FULL)
        .withLocale(Locale.forLanguageTag("vi-VN"))
        .format(date);
```

Không lưu localized display string làm dữ liệu ngày chính trong database. Hãy lưu type ngày/timestamp phù hợp và format ở boundary hiển thị.

## 3.7 Time zone, Period và Duration

### `ZoneId` và `ZonedDateTime`

```java
ZoneId parisZone = ZoneId.of("Europe/Paris");
ZoneId vietnamZone = ZoneId.of("Asia/Ho_Chi_Minh");
ZoneId systemZone = ZoneId.systemDefault();
```

`systemDefault()` phụ thuộc máy đang chạy. Production code có nghiệp vụ múi giờ rõ ràng nên dùng zone cấu hình hoặc zone từ dữ liệu người dùng thay vì ngầm dựa vào server.

Parse một ZonedDateTime:

```java
ZonedDateTime dateTime = ZonedDateTime.parse(
        "2017-07-10T10:15:30+05:00[Asia/Karachi]");
```

Slide dùng offset `+05:30` với `Asia/Karachi`, nhưng zone này dùng `+05:00` tại thời điểm minh họa. Offset không khớp với zone có thể gây `DateTimeParseException`. Nếu muốn `+05:30`, có thể dùng region phù hợp như `Asia/Kolkata`.

### Chuyển cùng một instant sang zone khác

```java
Instant instant = Instant.parse("2026-07-22T03:00:00Z");

ZonedDateTime vietnamTime = instant.atZone(
        ZoneId.of("Asia/Ho_Chi_Minh"));
ZonedDateTime parisTime = instant.atZone(
        ZoneId.of("Europe/Paris"));
```

Hai object hiển thị giờ địa phương khác nhau nhưng biểu diễn cùng một instant.

### `Period`

`Period` biểu diễn khoảng cách theo năm, tháng và ngày:

```java
LocalDate currentDate = LocalDate.now();
LocalDate nextMonth = currentDate.plusMonths(1);

Period period = Period.between(currentDate, nextMonth);
System.out.println(period); // Thường là P1M
```

Slide dùng `Period.between(date2, date1)` trong khi `date2` là tháng sau, vì vậy kết quả sẽ âm. Muốn khoảng thời gian từ hiện tại tới tháng sau phải dùng `Period.between(date1, date2)`.

`Period.ofMonths(1)` không luôn bằng cùng số ngày vì mỗi tháng có độ dài khác nhau.

Ví dụ tính tuổi theo lịch:

```java
LocalDate birthday = LocalDate.of(1998, 7, 10);
LocalDate today = LocalDate.now();

int age = Period.between(birthday, today).getYears();
```

### `Duration`

`Duration` biểu diễn lượng thời gian theo giây và nano giây:

```java
LocalTime start = LocalTime.now();
Duration twoHours = Duration.ofHours(2);
LocalTime end = start.plus(twoHours);

Duration elapsed = Duration.between(start, end);
System.out.println(elapsed); // PT2H
```

Trong production, đo thời gian xử lý nên dùng monotonic clock như `System.nanoTime()` hoặc framework metrics thay vì lấy hai `LocalDateTime.now()`, vì wall clock có thể bị điều chỉnh.

### Period và Duration khác nhau

| Tiêu chí | `Period` | `Duration` |
|---|---|---|
| Đơn vị chính | Năm, tháng, ngày | Giây, nano giây |
| Dùng với | `LocalDate` | `Instant`, `LocalTime`, date-time phù hợp |
| Ví dụ | Một tháng sau | Sau đúng hai giờ |
| Liên quan DST | Theo lịch | Theo elapsed time |

Qua thời điểm đổi daylight saving time, “ngày kế tiếp theo lịch” và “sau đúng 24 giờ” có thể cho giờ địa phương khác nhau:

```java
ZonedDateTime nextCalendarDay = zonedDateTime.plusDays(1);
ZonedDateTime after24Hours = zonedDateTime.plus(Duration.ofHours(24));
```

Điều này quan trọng với hệ thống quốc tế.

### Ví dụ production: lưu timestamp và hiển thị theo người dùng

```java
public record Order(
        String id,
        Instant createdAt) {
}
```

Lưu thời điểm tạo bằng `Instant`:

```java
Order order = new Order(orderId, Instant.now(clock));
```

Hiển thị theo zone và locale của người dùng:

```java
public String formatCreatedAt(
        Instant createdAt,
        ZoneId userZone,
        Locale userLocale) {
    DateTimeFormatter formatter = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.MEDIUM)
            .withLocale(userLocale);

    return formatter.format(createdAt.atZone(userZone));
}
```

### Lưu ý mục 3

- Ưu tiên `java.time` thay cho `Date`, `Calendar` và `SimpleDateFormat` trong code mới.
- `LocalDateTime` không chứa zone; không dùng nó để biểu diễn một instant toàn cầu nếu nghiệp vụ cần xác định chính xác thời điểm.
- Thường lưu timestamp bằng `Instant`/UTC và chỉ chuyển sang user zone khi hiển thị.
- Không phụ thuộc `ZoneId.systemDefault()` hoặc locale mặc định trong business logic quan trọng.
- Object `java.time` immutable; phải giữ giá trị trả về từ `plus...`, `minus...` và `with...`.
- Dùng `Clock` để code gọi `now()` có thể test ổn định.
- `DateTimeFormatter` immutable, thread-safe và nên được tái sử dụng khi phù hợp.
- Format hiển thị phụ thuộc locale; format trao đổi giữa hệ thống nên ưu tiên ISO-8601.
- `Period` dùng cho khoảng cách theo lịch; `Duration` dùng cho lượng thời gian chính xác.
- Kiểm tra time zone và DST khi xây dựng lịch, deadline hoặc hệ thống hoạt động ở nhiều quốc gia.
