package practice3;

import java.util.List;
import java.util.Map;

public final class Practice3Test {
    private Practice3Test() {
    }

    public static void main(String[] args) {
        Owner firstOwner = new Owner(
                "012345678901", "Nguyễn Văn A", "a@example.com");
        Owner secondOwner = new Owner(
                "012345678902", "Trần Thị B", "b@example.com");

        Vehicle firstCar = new Car(
                "A0001", Manufacturer.HONDA, 2024, "Đen", firstOwner, 5, "Xăng");
        Vehicle motorbike = new Motorbike(
                "C0003", Manufacturer.YAMAHA, 2023, "Đỏ", secondOwner, 150);
        Vehicle secondCar = new Car(
                "B0002", Manufacturer.HONDA, 2025, "Trắng", firstOwner, 7, "Hybrid");

        VehicleManager manager = new VehicleManager();
        check(manager.addVehicle(firstCar), "Phải thêm được xe đầu tiên.");
        check(manager.addVehicle(motorbike), "Phải thêm được xe máy.");
        check(manager.addVehicle(secondCar), "Một chủ sở hữu phải được có nhiều xe.");
        check(!manager.addVehicle(new Truck(
                        "A0001", Manufacturer.SUZUKI, 2022, "Xanh", secondOwner, 2)),
                "Không được thêm biển số xe trùng.");

        check(
                manager.searchByVehicleNumber("a0001").orElseThrow() == firstCar,
                "Tìm kiếm biển số phải không phân biệt chữ hoa/chữ thường.");
        check(
                manager.findByOwnerId(firstOwner.getIdNumber()).size() == 2,
                "Phải tìm được tất cả xe của một chủ sở hữu.");
        check(
                manager.findManufacturersWithMostVehicles().equals(List.of(Manufacturer.HONDA)),
                "Honda phải là hãng có nhiều xe nhất.");

        List<Vehicle> sorted = manager.sortByVehicleNumberDescending();
        check(
                sorted.get(0).getVehicleNumber().equals("C0003"),
                "Danh sách chưa được sắp xếp biển số giảm dần.");

        Map<VehicleType, Integer> statistics = manager.statisticsByType();
        check(statistics.get(VehicleType.CAR) == 2, "Thống kê ô tô không chính xác.");
        check(statistics.get(VehicleType.MOTORBIKE) == 1, "Thống kê xe máy không chính xác.");
        check(statistics.get(VehicleType.TRUCK) == 0, "Thống kê xe tải không chính xác.");

        check(
                manager.deleteByManufacturer(Manufacturer.HONDA) == 2,
                "Phải xóa tất cả phương tiện của Honda.");
        check(manager.getAllVehicles().size() == 1, "Số phương tiện còn lại không chính xác.");

        expectIllegalArgument(() -> new Owner("123", "Tên", "mail@example.com"));
        expectIllegalArgument(() -> new Owner(
                "012345678999", "Tên", "email-khong-hop-le"));
        expectIllegalArgument(() -> new Car(
                "TOO-LONG", Manufacturer.TOYOTA, 2024, "Đen", firstOwner, 5, "Xăng"));
        expectIllegalArgument(() -> Manufacturer.from("Ford"));

        VehicleManager ownerValidationManager = new VehicleManager();
        ownerValidationManager.addVehicle(firstCar);
        Owner conflictingOwner = new Owner(
                firstOwner.getIdNumber(), "Người khác", "other@example.com");
        expectIllegalArgument(() -> ownerValidationManager.addVehicle(new Truck(
                "D0004", Manufacturer.SUZUKI, 2024, "Xám", conflictingOwner, 1.5)));

        System.out.println("Practice3Test: ALL TESTS PASSED");
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
