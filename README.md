# Java Core

Repository gồm tài liệu Java Core được tổng hợp từ slide đào tạo và source code
hoàn chỉnh cho ba bài thực hành OOP.

## Mục lục tài liệu

1. [Java Core Basics](01-java-core-basics.md)
2. [Control Statements](02-control-statements.md)
3. [String, Array and Date/Time](03-string-array-date.md)
4. [Working with Inheritance](04-inheritance.md)
5. [Working with Interfaces](05-interface.md)
6. [Collections, Generics and Lambda](06-collections-generics-lambda.md)
7. [Working with Threads](07-threads.md)
8. [Handle Exceptions](08-exceptions.md)
9. [Bài tập thực hành OOP](09-oop-practice.md)

## Source code bài thực hành

### [Practice 1](Practice1)

Minh họa kế thừa với `Shape`, `Rectangle` và `Circle`:

- Tính diện tích, chu vi hình chữ nhật.
- Tính diện tích, chu vi hình tròn theo `π = 3.14` như đề bài.
- Kiểm tra chiều rộng, chiều cao và bán kính phải là số dương.
- Khởi tạo và hiển thị thông tin của cả ba lớp.

Class chạy chương trình: `practice1.Main`.

### [Practice 2](Practice2)

Chương trình quản lý hàng tồn kho của siêu thị:

- `Goods` là lớp trừu tượng; `Food`, `Electronics`, `Crockery` là các lớp cụ thể.
- Kiểm tra các ràng buộc về tồn kho, ngày sản xuất/hết hạn, thời gian bảo hành
  và công suất.
- Tính VAT bằng đa hình: thực phẩm 5%, điện máy và gốm sứ 10%.
- Đánh giá mức tiêu thụ riêng cho từng loại hàng.
- `DSHH` lưu hàng hóa bằng mảng, tự mở rộng mảng và không cho phép trùng mã hàng.
- Menu cho phép chọn loại hàng cần thêm, hiển thị danh sách và thống kê tồn kho/VAT.

Class chạy chương trình: `practice2.Main`.

### [Practice 3](Practice3)

Chương trình quản lý ô tô, xe máy và xe tải:

- Quản lý thông tin chung bằng lớp trừu tượng `Vehicle`.
- Kiểm tra biển số duy nhất và đúng 5 ký tự.
- Chỉ chấp nhận các hãng Honda, Yamaha, Toyota và Suzuki.
- Kiểm tra năm sản xuất, CCCD 12 chữ số và định dạng email.
- Một chủ sở hữu có thể có nhiều phương tiện; một CCCD không thể thuộc về hai
  hồ sơ chủ sở hữu khác nhau.
- Có đủ chức năng thêm, tìm kiếm, xóa theo hãng, tìm hãng có nhiều xe nhất,
  sắp xếp biển số giảm dần và thống kê theo loại xe.

Class chạy chương trình: `practice3.Main`.

## Yêu cầu môi trường

- JDK 17 trở lên.
- Terminal PowerShell hoặc terminal tương đương.

Kiểm tra Java đã được cài và thêm vào `PATH`:

```powershell
java -version
javac -version
```

## Biên dịch và chạy

Thực hiện các lệnh dưới đây từ thư mục của bài muốn chạy. Ví dụ với Practice 1:

```powershell
cd Practice1
New-Item -ItemType Directory -Force out | Out-Null
$sourceFiles = Get-ChildItem src -Recurse -Filter *.java |
    Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d out $sourceFiles
java -cp out practice1.Main
```

Với hai bài còn lại, thay `Practice1` và `practice1.Main` lần lượt bằng:

| Thư mục | Class chạy |
| --- | --- |
| `Practice2` | `practice2.Main` |
| `Practice3` | `practice3.Main` |

Practice 2 và Practice 3 sử dụng menu nhập liệu trên console. Ngày tháng trong
Practice 2 có định dạng `yyyy-MM-dd`.

## Chạy kiểm thử

Mỗi bài có một bộ kiểm thử không phụ thuộc thư viện bên ngoài. Ví dụ với Practice 1:

```powershell
cd Practice1
New-Item -ItemType Directory -Force out | Out-Null
$allFiles = Get-ChildItem src,test -Recurse -Filter *.java |
    Select-Object -ExpandProperty FullName
javac -encoding UTF-8 -d out $allFiles
java -cp out practice1.Practice1Test
```

Thay package và tên test tương ứng để chạy hai bài còn lại:

| Bài | Test class |
| --- | --- |
| Practice 1 | `practice1.Practice1Test` |
| Practice 2 | `practice2.Practice2Test` |
| Practice 3 | `practice3.Practice3Test` |
