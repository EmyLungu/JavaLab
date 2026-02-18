public class lab {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        String[] languages = {"C", "C++", "C#", "Python", "Go", "Rust", "JavaScript", "PHP", "Swift", "Java"};

        int n = (int) (Math.random() * 1_000_000);
        n *= 3;
        n += 0b10101;
        n += 0xFF;
        n *= 6;

        while (n > 9) {
            int digits_sum = 0;
            while (n != 0) {
                digits_sum += n % 10;
                n /= 10;
            }

            n = digits_sum;
        }

        System.out.println("Willy-nilly, this semester I will learn " + languages[n]);
    }
}
