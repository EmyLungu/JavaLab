public class homework {
    public static String imgToString(int n, int img[][]) {
        String output = "";

        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                if (img[y][x] == 0) {
                    output += "■ ";
                } else if (img[y][x] == 255) {
                    output += "□ ";
                } else {
                    output += "▨ ";
                }
            }
            output += '\n';
        }

        return output;
    }

    public static void fill(int n, int img[][], int color) {
        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                img[y][x] = color;
           }
        }
    }

    public static void draw_rectangle(int n, int img[][], int width, int height, boolean boundingBox) {
        long start = System.currentTimeMillis();
        fill(n, img, 255);

        int startX = (n - width)  / 2;
        int startY = (n - height) / 2;
        for (int y = startY; y < startY + height; ++y) {
            for (int x = startX; x < startX + width; ++x) {
                img[y][x] = 0;
            }
        }
        long end = System.currentTimeMillis();

        if (boundingBox) {
            addBoundingBox(n, img, 0);
        }

        if (n < 300) {
            System.out.println(imgToString(n, img));
        } else {
            System.out.println("Time is: " + (end - start) + "ms");
        }
    }

    public static void draw_circle(int n, int img[][], int radius, boolean boundingBox) {
        long start = System.currentTimeMillis();
        fill(n, img, 0);

        int center = n / 2;
        int r2 = radius * radius;

        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                int dx = x - center;
                int dy = y - center;

                if (dx * dx + dy * dy <= r2) {
                    img[y][x] = 255;
                }
            }
        }

        long end = System.currentTimeMillis();

        if (boundingBox) {
            addBoundingBox(n, img, 255);
        }
        
        if (n < 300) {
            System.out.println(imgToString(n, img));
        } else {
            System.out.println("Time is: " + (end - start) + "ms");
        }
    }

    public static void addBoundingBox(int n, int img[][], int shapeColor) {
        int minX =  n, minY =  n;
        int maxX = -1, maxY = -1;

        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                if (img[y][x] == shapeColor) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }

        for (int x = minX; x <= maxX; ++x) {
            img[minY][x] = img[maxY][x] = 100;
        }

        for (int y = minY; y <= maxY; ++y) {
            img[y][minX] = img[y][maxX] = 100;
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong number of parameters!");
        }

        int size = Integer.parseInt(args[0]);
        int[][] img = new int[size][size];

        boolean boundingBox = false;
        if (args.length >= 3) {
            boundingBox = Boolean.parseBoolean(args[2]);
        }

        if (args[1].equals("circle")) {
            draw_circle(size, img, size / 3, boundingBox);
        } else if (args[1].equals("rectangle")) {
            draw_rectangle(size, img, size * 8 / 10, size * 3 / 10, boundingBox);
        } else {
            System.err.printf("Wrong shape {%s}!", args[1]);
        }
    }
}
