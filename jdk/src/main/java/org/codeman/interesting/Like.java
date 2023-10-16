package org.codeman.interesting;

public class Like {
    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(2000);
        float x, y;

        for (y = 1.3f; y > -1.1f; y -= 0.15f) {
            for (x = -1.2f; x <= 1.2f; x += 0.05f) {
                float temp = x * x + y * y - 1;
                if ((Math.pow(temp, 3) - (x * x * Math.pow(y, 3))) <= 0.0f) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
            Thread.sleep(200);
        }
    }
}
