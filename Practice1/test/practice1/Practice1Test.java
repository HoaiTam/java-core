package practice1;

public final class Practice1Test {
    private static final double EPSILON = 0.0001;

    private Practice1Test() {
    }

    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle(3, 4);
        checkClose(12, rectangle.getArea(), "Diện tích hình chữ nhật");
        checkClose(14, rectangle.getPerimeter(), "Chu vi hình chữ nhật");

        Circle circle = new Circle(2);
        checkClose(12.56, circle.getArea(), "Diện tích hình tròn");
        checkClose(12.56, circle.getCircumference(), "Chu vi hình tròn");

        expectIllegalArgument(() -> new Shape(0, 1));
        expectIllegalArgument(() -> new Circle(-1));

        System.out.println("Practice1Test: ALL TESTS PASSED");
    }

    private static void checkClose(double expected, double actual, String label) {
        if (Math.abs(expected - actual) > EPSILON) {
            throw new AssertionError(label + ": expected=" + expected + ", actual=" + actual);
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
