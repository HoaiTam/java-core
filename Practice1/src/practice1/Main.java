package practice1;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        Shape shape = new Shape(8, 5);
        Rectangle rectangle = new Rectangle(8, 5);
        Circle circle = new Circle(4);

        System.out.println("=== PRACTICE 1 ===");
        System.out.println(shape);
        System.out.println(rectangle);
        System.out.println(circle);
    }
}
