public class homework {
    public enum BorderType {
        NONE,
        BOUNDING_BOX,
        BORDER,
    }

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

    public static void draw_rectangle(int n, int img[][], int width, int height, BorderType borderType) {
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

        if (borderType == BorderType.BOUNDING_BOX) {
            addBoundingBox(n, img, 0);
        } else if (borderType == BorderType.BORDER) {
            addBorder(n, img, 0);
        }

        if (n < 300) {
            System.out.println(imgToString(n, img));
        } else {
            System.out.println("Time is: " + (end - start) + "ms");
        }
    }

    public static void draw_circle(int n, int img[][], int radius, BorderType borderType) {
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

        if (borderType == BorderType.BOUNDING_BOX) {
            addBoundingBox(n, img, 255);
        } else if (borderType == BorderType.BORDER) {
            addBorder(n, img, 255);
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

    public static void addBorder(int n, int img[][], int shapeColor) {
        int[] dx = {-1, 0, 1, 1, 1, 0, -1, -1};
        int[] dy = {-1, -1, -1, 0, 1, 1, 1, 0};

        for (int y = 0; y < n; ++y) {
            for (int x = 0; x < n; ++x) {
                int neighbourCount = 0;

                if (img[y][x] == shapeColor) {
                    for (int i = 0; i < 8; ++i) {
                        int vx = x + dx[i];
                        int vy = y + dy[i];

                        if (vx >= 0 && vy >= 0 && vx < n && vy < n && (img[vy][vx] == shapeColor || img[vy][vx] == 100)) {
                            neighbourCount++;
                        }
                    }
                }

                if (neighbourCount > 0 && neighbourCount < 8) {
                    img[y][x] = 100;
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Wrong number of parameters!");
        }

        int size = Integer.parseInt(args[0]);
        int[][] img = new int[size][size];

        BorderType type = BorderType.NONE;
        if (args.length >= 3) {
            if (args[2].equals("border")) {
                type = BorderType.BORDER;
            } else if (args[2].equals("bounding_box")) {
                type = BorderType.BOUNDING_BOX;
            }
        }

        if (args[1].equals("circle")) {
            draw_circle(size, img, size / 3, type);
        } else if (args[1].equals("rectangle")) {
            draw_rectangle(size, img, size * 8 / 10, size * 3 / 10, type);
        } else {
            System.err.printf("Wrong shape {%s}!", args[1]);
        }

        // 30_500
    }
}
