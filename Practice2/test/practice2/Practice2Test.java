package practice2;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Practice2Test {
    private Practice2Test() {
    }

    public static void main(String[] args) {
        LocalDate referenceDate = LocalDate.of(2026, 7, 28);

        Food food = new Food(
                "F01",
                "Sữa",
                2,
                new BigDecimal("100"),
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 27),
                "Nhà cung cấp A");
        Electronics electronics = new Electronics(
                "E01", "Quạt", 2, new BigDecimal("500"), 12, 0.06);
        Crockery crockery = new Crockery(
                "C01",
                "Chén",
                51,
                new BigDecimal("20"),
                "Nhà sản xuất B",
                LocalDate.of(2026, 7, 17));

        check(
                food.evaluateConsumption(referenceDate) == ConsumptionRating.HARD_TO_SELL,
                "Thực phẩm hết hạn phải khó bán.");
        check(
                electronics.evaluateConsumption(referenceDate) == ConsumptionRating.SELLABLE,
                "Điện máy tồn kho dưới 3 phải bán được.");
        check(
                crockery.evaluateConsumption(referenceDate) == ConsumptionRating.SLOW_SELLING,
                "Gốm sứ tồn kho trên 50 và lưu trên 10 ngày phải bán chậm.");
        check(
                food.calculateVat().compareTo(new BigDecimal("10.00")) == 0,
                "VAT thực phẩm phải bằng 5% giá trị tồn kho.");

        DSHH inventory = new DSHH(1);
        check(inventory.addGoods(food), "Phải thêm được mã hàng mới.");
        check(inventory.addGoods(electronics), "Mảng phải tự mở rộng khi đầy.");
        check(inventory.addGoods(crockery), "Phải thêm được đồ gốm sứ.");
        check(!inventory.addGoods(new Electronics(
                        "e01", "Quạt khác", 1, new BigDecimal("300"), 0, 0)),
                "Không được thêm mã hàng trùng.");
        check(inventory.getSize() == 3, "Danh sách phải có đúng 3 hàng hóa.");
        check(
                inventory.getInventoryQuantity(Crockery.class) == 51,
                "Thống kê tồn kho theo loại không chính xác.");

        expectIllegalArgument(() -> new Food(
                "F02",
                "Bánh",
                1,
                BigDecimal.ONE,
                referenceDate,
                referenceDate,
                "Nhà cung cấp"));

        System.out.println("Practice2Test: ALL TESTS PASSED");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void expectIllegalArgument(Runnable action) {
        try {
            action.run();
            throw new AssertionError("Expected IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // Kết quả mong đợi.
        }
    }
}
