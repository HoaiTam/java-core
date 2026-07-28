package practice2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        DSHH inventory = new DSHH();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readInt(scanner, "Chọn chức năng: ");
                switch (choice) {
                    case 1 -> addGoods(scanner, inventory);
                    case 2 -> displayGoods(inventory);
                    case 3 -> displayStatistics(inventory);
                    case 0 -> running = false;
                    default -> System.out.println("Lựa chọn không hợp lệ.");
                }
            }
        }

        System.out.println("Đã kết thúc Practice 2.");
    }

    private static void printMenu() {
        System.out.println("""

                === QUẢN LÝ HÀNG HÓA ===
                1. Thêm hàng hóa
                2. Hiển thị danh sách
                3. Thống kê tồn kho và VAT
                0. Thoát
                """);
    }

    private static void addGoods(Scanner scanner, DSHH inventory) {
        System.out.println("""
                Chọn loại hàng:
                1. Thực phẩm
                2. Hàng điện máy
                3. Đồ gốm sứ
                """);
        int type = readInt(scanner, "Loại hàng: ");

        try {
            Goods goods = createGoods(scanner, type);
            if (inventory.addGoods(goods)) {
                System.out.println("Thêm hàng hóa thành công.");
            } else {
                System.out.println("Không thể thêm: mã hàng đã tồn tại.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Dữ liệu không hợp lệ: " + exception.getMessage());
        }
    }

    private static Goods createGoods(Scanner scanner, int type) {
        if (type < 1 || type > 3) {
            throw new IllegalArgumentException("Loại hàng phải từ 1 đến 3.");
        }

        String productCode = readText(scanner, "Mã hàng: ");
        String name = readText(scanner, "Tên hàng: ");
        int quantity = readInt(scanner, "Số lượng tồn kho: ");
        BigDecimal unitPrice = readBigDecimal(scanner, "Đơn giá: ");

        return switch (type) {
            case 1 -> new Food(
                    productCode,
                    name,
                    quantity,
                    unitPrice,
                    readDate(scanner, "Ngày sản xuất (yyyy-MM-dd): "),
                    readDate(scanner, "Ngày hết hạn (yyyy-MM-dd): "),
                    readText(scanner, "Nhà cung cấp: "));
            case 2 -> new Electronics(
                    productCode,
                    name,
                    quantity,
                    unitPrice,
                    readInt(scanner, "Số tháng bảo hành: "),
                    readDouble(scanner, "Công suất (kW): "));
            case 3 -> new Crockery(
                    productCode,
                    name,
                    quantity,
                    unitPrice,
                    readText(scanner, "Nhà sản xuất: "),
                    readDate(scanner, "Ngày nhập kho (yyyy-MM-dd): "));
            default -> throw new IllegalStateException("Loại hàng không được hỗ trợ.");
        };
    }

    private static void displayGoods(DSHH inventory) {
        Goods[] allGoods = inventory.getAll();
        if (allGoods.length == 0) {
            System.out.println("Danh sách hàng hóa đang trống.");
            return;
        }
        System.out.println("=== DANH SÁCH HÀNG HÓA ===");
        for (Goods goods : allGoods) {
            System.out.println(goods);
        }
    }

    private static void displayStatistics(DSHH inventory) {
        printStatistics("Thực phẩm", Food.class, inventory);
        printStatistics("Điện máy", Electronics.class, inventory);
        printStatistics("Gốm sứ", Crockery.class, inventory);
    }

    private static void printStatistics(
            String label,
            Class<? extends Goods> goodsType,
            DSHH inventory) {
        System.out.printf(
                "%s: tồn kho = %d, VAT = %s%n",
                label,
                inventory.getInventoryQuantity(goodsType),
                inventory.getVatAmount(goodsType).toPlainString());
    }

    private static String readText(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(scanner, prompt).trim());
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập một số nguyên.");
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readText(scanner, prompt).trim());
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập một số hợp lệ.");
            }
        }
    }

    private static BigDecimal readBigDecimal(Scanner scanner, String prompt) {
        while (true) {
            try {
                return new BigDecimal(readText(scanner, prompt).trim());
            } catch (NumberFormatException exception) {
                System.out.println("Vui lòng nhập số tiền hợp lệ.");
            }
        }
    }

    private static LocalDate readDate(Scanner scanner, String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readText(scanner, prompt).trim());
            } catch (DateTimeParseException exception) {
                System.out.println("Ngày phải có định dạng yyyy-MM-dd.");
            }
        }
    }
}
