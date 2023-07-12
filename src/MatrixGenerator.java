import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MatrixGenerator {
    private static List<boolean[][]> mapsList;
    private static boolean[][] elem = new boolean[][]{{true, true},
            {true, true}};
    private static boolean[][] elem1 = new boolean[][]{{false, true, false},
            {true, true, true},
            {false, true, false}};

    private static boolean[][] elem2 = new boolean[][]{{true, true}};
    private static boolean[][] elem3 = new boolean[][]{{false, true},
            {false,true}};
    private static boolean[][] elem4 = new boolean[][]{{true, true},
            {false,true}};
    private static boolean[][] elem5 = new boolean[][]{{true, true},
            {true,false}};

    private static boolean[][] createRandomMap() {
        mapsList = new ArrayList<boolean[][]>();
        mapsList.add(elem);
        mapsList.add(elem1);
        mapsList.add(elem2);
        mapsList.add(elem3);
        mapsList.add(elem4);
        mapsList.add(elem5);
        Random rand = new Random();
        return mapsList.get(rand.nextInt(mapsList.size()));
    }

    public static int[][] generateMatrix() {
        int[][] matrix = new int[25][25];
        Random random = new Random();
        int count = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i == 0 || j == 0 || i == 24 || j == 24) {
                    matrix[i][j] = 6;
                } else{
                    matrix[i][j] = 7;
                }
            }
        }

        int quantityOfElements = 0;
        while (quantityOfElements < 48) {
            int randX = (int) (Math.random() * 23) + 1;
            int randY = (int) (Math.random() * 23) + 1;
            boolean[][] element = createRandomMap();
            boolean shouldContinue = false;

            for (int i = 0; i < element.length; i++) {
                for (int j = 0; j < element[0].length; j++) {
                    int yCoordinate = i + randY;
                    int xCoordinate = j + randX;

                    if (yCoordinate > 23 || xCoordinate > 23) {
                        shouldContinue = true;
                        break;
                    }

                    if (element[i][j]) {
                        if (!nextCell(yCoordinate, xCoordinate, matrix)) {
                            shouldContinue = true;
                            break;
                        }
                    }
                }

                if (shouldContinue) {
                    break;
                }
            }

            if (!shouldContinue) {
                for (int i = 0; i < element.length; i++) {
                    for (int j = 0; j < element[0].length; j++) {
                        if (element[i][j]) {
                            matrix[i + randY][j + randX] = 6;
                        }
                    }
                }
                quantityOfElements++;
            }
        }

        while (count < 4) {
            int i = random.nextInt(1, 24);
            int j = random.nextInt(1, 24);
            if (matrix[i][j] == 7) {
                matrix[i][j] = 5;
                count++;
            }
        }

        for (int i = 1; i < 24; i++) {
            matrix[i][0] = 9;
            matrix[i][24] = 9;
        }

        for (int i = 1; i < 24; i++) {
            matrix[0][i] = 10;
            matrix[24][i] = 10;
        }
        matrix[0][0] = 1;
        matrix[0][24] = 3;
        matrix[24][0] = 2;
        matrix[24][24] = 4;
        matrix[19][4] = 14;

        return matrix;
    }

    private static boolean nextCell(int y, int x, int[][] map) {
        return map[y][x + 1] == 7 && map[y][x - 1] == 7 &&
                map[y + 1][x] == 7 && map[y - 1][x] == 7 &&
                map[y + 1][x + 1] == 7 && map[y - 1][x + 1] == 7 &&
                map[y + 1][x - 1] == 7 && map[y - 1][x - 1] == 7;
    }
}
