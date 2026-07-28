package practice3;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        VehicleManager manager = new VehicleManager();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu();
                int choice = readInt(scanner, "Chọn chức năng: ");
                switch (choice) {
                    case 1 -> addVehicle(scanner, manager);
                    case 2 -> searchByVehicleNumber(scanner, manager);
                    case 3 -> findByOwnerId(scanner, manager);
                    case 4 -> deleteByManufacturer(scanner, manager);
                    case 5 -> displayTopManufacturers(manager);
                    case 6 -> displayVehicles(manager.sortByVehicleNumberDescending());
                    case 7 -> displayStatistics(manager);
                    case 0 -> running = false;
                    default -> System.out.println("Lựa chọn không hợp lệ.");
                }
            }
        }

        System.out.println("Đã kết thúc Practice 3.");
    }

    private static void printMenu() {
        System.out.println("""

                === QUẢN LÝ PHƯƠNG TIỆN ===
                1. Thêm phương tiện
                2. Tìm theo biển số xe
                3. Tìm theo CMND/CCCD của chủ xe
                4. Xóa tất cả phương tiện của một hãng
                5. Tìm hãng có nhiều phương tiện nhất
                6. Sắp xếp biển số xe giảm dần
                7. Thống kê theo loại phương tiện
                0. Thoát
                """);
    }

    private static void addVehicle(Scanner scanner, VehicleManager manager) {
        System.out.println("""
                Chọn loại phương tiện:
                1. Ô tô
                2. Xe máy
                3. Xe tải
                """);
        int type = readInt(scanner, "Loại phương tiện: ");

        try {
            Vehicle vehicle = createVehicle(scanner, type);
            if (manager.addVehicle(vehicle)) {
                System.out.println("Thêm phương tiện thành công.");
            } else {
                System.out.println("Không thể thêm: biển số xe đã tồn tại.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println("Dữ liệu không hợp lệ: " + exception.getMessage());
        }
    }

    private static Vehicle createVehicle(Scanner scanner, int type) {
        if (type < 1 || type > 3) {
            throw new IllegalArgumentException("Loại phương tiện phải từ 1 đến 3.");
        }

        String vehicleNumber = readText(scanner, "Biển số xe (đúng 5 ký tự): ");
        Manufacturer manufacturer = Manufacturer.from(
                readText(scanner, "Hãng (Honda/Yamaha/Toyota/Suzuki): "));
        int manufactureYear = readInt(scanner, "Năm sản xuất: ");
        String color = readText(scanner, "Màu xe: ");
        Owner owner = new Owner(
                readText(scanner, "CMND/CCCD (12 chữ số): "),
                readText(scanner, "Họ và tên chủ xe: "),
                readText(scanner, "Email chủ xe: "));

        return switch (type) {
            case 1 -> new Car(
                    vehicleNumber,
                    manufacturer,
                    manufactureYear,
                    color,
                    owner,
                    readInt(scanner, "Số chỗ ngồi: "),
                    readText(scanner, "Loại động cơ: "));
            case 2 -> new Motorbike(
                    vehicleNumber,
                    manufacturer,
                    manufactureYear,
                    color,
                    owner,
                    readDouble(scanner, "Dung tích xi-lanh (cc): "));
            case 3 -> new Truck(
                    vehicleNumber,
                    manufacturer,
                    manufactureYear,
                    color,
                    owner,
                    readDouble(scanner, "Trọng tải (tấn): "));
            default -> throw new IllegalStateException("Loại phương tiện không được hỗ trợ.");
        };
    }

    private static void searchByVehicleNumber(Scanner scanner, VehicleManager manager) {
        String number = readText(scanner, "Nhập biển số xe: ");
        manager.searchByVehicleNumber(number)
                .ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Không tìm thấy phương tiện."));
    }

    private static void findByOwnerId(Scanner scanner, VehicleManager manager) {
        String ownerId = readText(scanner, "Nhập số CMND/CCCD: ");
        displayVehicles(manager.findByOwnerId(ownerId));
    }

    private static void deleteByManufacturer(Scanner scanner, VehicleManager manager) {
        try {
            Manufacturer manufacturer = Manufacturer.from(readText(scanner, "Nhập hãng: "));
            int deletedCount = manager.deleteByManufacturer(manufacturer);
            System.out.println("Đã xóa " + deletedCount + " phương tiện.");
        } catch (IllegalArgumentException exception) {
            System.out.println("Dữ liệu không hợp lệ: " + exception.getMessage());
        }
    }

    private static void displayTopManufacturers(VehicleManager manager) {
        List<Manufacturer> manufacturers = manager.findManufacturersWithMostVehicles();
        if (manufacturers.isEmpty()) {
            System.out.println("Chưa có phương tiện để thống kê.");
            return;
        }

        String names = manufacturers.stream()
                .map(Manufacturer::getDisplayName)
                .collect(Collectors.joining(", "));
        System.out.println("Hãng có nhiều phương tiện nhất: " + names);
    }

    private static void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("Không có phương tiện phù hợp.");
            return;
        }
        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }
    }

    private static void displayStatistics(VehicleManager manager) {
        for (Map.Entry<VehicleType, Integer> entry
                : manager.statisticsByType().entrySet()) {
            System.out.printf(
                    "%s: %d phương tiện%n",
                    entry.getKey().getDisplayName(),
                    entry.getValue());
        }
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
}
