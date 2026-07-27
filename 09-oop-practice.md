# Java Core 09 - Bài tập thực hành OOP

## Mục tiêu

1. Biết cách cài đặt và cấu hình Java.
2. Nhận biết cú pháp của ngôn ngữ Java.
3. Áp dụng ngôn ngữ Java để giải quyết các bài toán cụ thể.
4. Hiểu các đặc điểm và sự khác biệt giữa Java với các ngôn ngữ lập trình hướng đối tượng khác.

---

## Bài thực hành 1

1. Tạo lớp `Shape` có các thuộc tính chiều rộng và chiều cao.
2. Tạo lớp con `Rectangle` kế thừa lớp `Shape` với các phương thức:
   - `getArea()`: tính diện tích theo công thức `w * l`.
   - `getPerimeter()`: tính chu vi theo công thức `2 * (l + w)`.
3. Tạo lớp con `Circle` kế thừa lớp `Shape` với các phương thức:
   - `getArea()`: tính diện tích theo công thức `πr²`.
   - `getCircumference()`: tính chu vi theo công thức `d * 3.14`.
4. Viết chương trình khởi tạo các đối tượng thuộc ba lớp trên và hiển thị thông tin của chúng.
5. Có thể mở rộng bài tập bằng cách bổ sung các lớp con và phương thức khác.

---

## Bài thực hành 2

Viết chương trình quản lý hàng tồn kho của siêu thị, bao gồm:

- Thực phẩm.
- Đồ gốm sứ.
- Hàng điện máy.

Mỗi loại hàng hóa có các thông tin:

- Mã hàng.
- Tên hàng.
- Số lượng tồn kho, phải lớn hơn hoặc bằng `0`.
- Đơn giá.

Thông tin riêng của từng loại hàng:

- **Thực phẩm:** ngày sản xuất, ngày hết hạn và nhà cung cấp. Ngày hết hạn phải sau ngày sản xuất.
- **Hàng điện máy:** thời gian bảo hành theo tháng, lớn hơn hoặc bằng `0`; công suất tính bằng kW, lớn hơn hoặc bằng `0`.
- **Đồ gốm sứ:** thông tin nhà sản xuất và ngày nhập kho.

Ngoài ra, người quản lý cần biết số lượng tồn kho của cả ba loại hàng và số tiền VAT của từng loại:

- VAT của hàng điện máy và đồ gốm sứ: `10%`.
- VAT của thực phẩm: `5%`.

### Yêu cầu 1

Dựa trên thông tin trên, hãy xác định:

1. Các lớp có thể có, bao gồm lớp trừu tượng và lớp cụ thể.
2. Thuộc tính và phương thức của từng lớp.
3. Mối quan hệ giữa các lớp, sử dụng kế thừa và đa hình nếu phù hợp.

### Yêu cầu 2

Tạo phương thức đánh giá mức độ tiêu thụ hàng hóa:

- **Hàng điện máy:** nếu số lượng tồn kho nhỏ hơn `3` thì được đánh giá là bán được.
- **Thực phẩm:** nếu vẫn còn hàng trong kho nhưng đã hết hạn thì được đánh giá là khó bán.
- **Đồ gốm sứ:** nếu số lượng tồn kho lớn hơn `50` và thời gian lưu kho lớn hơn `10` ngày thì được đánh giá là bán chậm.
- Các trường hợp còn lại không được đánh giá.

### Yêu cầu 3

1. Khởi tạo lớp quản lý danh sách hàng hóa `DSHH`, sử dụng mảng để lưu danh sách.
2. Viết phương thức thêm hàng hóa vào danh sách:
   - Chỉ thêm thành công khi mã hàng không bị trùng.
   - Cho phép người dùng chọn loại hàng hóa cần thêm.

---

## Bài thực hành 3

Ngành công an cần quản lý các phương tiện giao thông gồm ô tô, xe máy và xe tải.

Mỗi loại phương tiện có các thông tin chung:

- Biển số xe.
- Hãng sản xuất.
- Năm sản xuất.
- Màu xe.
- Chủ phương tiện.

Thông tin riêng của từng loại phương tiện:

- **Ô tô:** số chỗ ngồi và loại động cơ.
- **Xe máy:** dung tích xi-lanh.
- **Xe tải:** trọng tải.

Thông tin của chủ phương tiện:

- Số CMND/CCCD.
- Họ và tên.
- Email.

Vận dụng kiến thức OOP đã học để viết chương trình Java đáp ứng các yêu cầu sau.

### Yêu cầu 1

Xây dựng các lớp để quản lý hiệu quả những đối tượng được mô tả ở trên.

### Yêu cầu 2

Chương trình thực hiện các chức năng:

1. Thêm phương tiện giao thông.
2. Tìm kiếm phương tiện theo biển số xe.
3. Tìm các phương tiện thuộc sở hữu của một người theo số CMND/CCCD.
4. Xóa tất cả phương tiện của một hãng sản xuất.
5. Xác định hãng sản xuất có nhiều phương tiện đang được quản lý nhất.
6. Sắp xếp phương tiện theo biển số xe giảm dần.
7. Thống kê số lượng phương tiện đang được quản lý theo từng loại.

### Quy tắc của chương trình

- Biển số của mỗi phương tiện phải là duy nhất và có đúng `5` ký tự.
- Hãng sản xuất chỉ gồm: `Honda`, `Yamaha`, `Toyota`, `Suzuki`.
- Năm sản xuất phải lớn hơn `2000` và nhỏ hơn hoặc bằng năm hiện tại.
- Số CMND/CCCD phải có đúng `12` chữ số và không được trùng giữa các chủ phương tiện.
- Email phải đúng định dạng.

