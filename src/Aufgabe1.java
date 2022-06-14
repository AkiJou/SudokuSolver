/*
    Aufgabe 1) Zweidimensionale Arrays - Spiel Sudoku
*/

import codedraw.CodeDraw;
import codedraw.Palette;
import codedraw.textformat.HorizontalAlign;
import codedraw.textformat.TextFormat;

import java.awt.*;
import java.io.File;
import java.util.Scanner;

public class Aufgabe1 {

    private static final int sSize = 9; //sudoku field size ==> 9x9
    private static final int subSize = 3; //sudoku subfield size ==> 3x3
    private static final int cellSize = 40; //sudoku cell size in pixel
    private static final int[][] heatMap = new int[sSize][sSize]; // array for generating the heatmap


    private static int[][] readArrayFromFile(String filename) {
        int[][] array = new int[sSize][sSize];
        try {
            Scanner myFileReader = new Scanner(new File(filename));
            // *********************************************************
                int i = 0;
                while (myFileReader.hasNextLine()){
                    String s = myFileReader.nextLine();
                    String [] separate = s.split(";");
                    for (int j = 0; j < separate.length; j++) {
                        array[i][j] = Integer.parseInt(separate[j]);
                    }
                    i++;
                }
            // *********************************************************
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return array;
    }

    private static boolean solveSudoku(int[][] sudokuField, int idx) {
        if (idx > (sSize * sSize - 1)) {
            return true;
        } else {
            if (sudokuField[idx / sSize][idx % sSize] == 0) {
                for (int num = 1; num <= sSize; num++) {
                    if (!isNumUsedInRow(sudokuField, num, idx / sSize) && !isNumUsedInCol(sudokuField, num, idx % sSize) && !isNumUsedInBox(sudokuField, num, idx / sSize - ((idx / sSize) % subSize), idx % sSize - ((idx % sSize) % subSize))) {
                        sudokuField[idx / sSize][idx % sSize] = num;
                        heatMap[idx / sSize][idx % sSize] = heatMap[idx / sSize][idx % sSize] + 1;
                        if (solveSudoku(sudokuField, idx + 1)) {
                            return true;
                        } else {
                            sudokuField[idx / sSize][idx % sSize] = 0;
                        }
                    }
                }
            } else {
                return solveSudoku(sudokuField, idx + 1);
            }
        }
        return false;
    }

    private static boolean isNumUsedInBox(int[][] sudokuField, int num, int row, int col) {
        for (int rowIn = 0; rowIn < subSize; rowIn++) {
            for (int colIn = 0; colIn < subSize; colIn++) {
                if(sudokuField[rowIn + row][colIn + col] == num){
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isNumUsedInRow(int[][] sudokuField, int num, int row) {
        for (int i = 0; i < sudokuField.length; i++) {
            if(sudokuField[row][i] == num){
                return true;
            }
        }
        return false;
    }

    private static boolean isNumUsedInCol(int[][] sudokuField, int num, int col) {
        for (int i = 0; i < sudokuField.length; i++) {
            if(sudokuField[i][col] == num){
                return true;
            }
        }
        return false;
    }

    private static boolean isValidSudokuSolution(int[][] sudokuField) {
        for (int num = 0; num < sudokuField.length - 1 ; num++) {
            for (int i = 0; i < sudokuField[i].length - 1 ; i++) {
                if (!isNumUsedInRow(sudokuField, i + 1, num) || !isNumUsedInCol(sudokuField, i + 1, num) || !isNumUsedInBox(sudokuField, i+1, (i + 1) / sSize - (((i + 1) / sSize) % subSize), (i + 1) % sSize - (((i + 1) % sSize) % subSize))) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void drawSudokuField(CodeDraw myDrawObj, int[][] sudokuField) {
        double g = cellSize;
        //Quadrate Farbe
        int sum = cellSize * 9;
        int count = 0;
        for (int i = 0; i < sum; i+= cellSize*subSize) {
            for (int j = 0; j < sum; j+= cellSize*subSize) {
                myDrawObj.setColor(Color.PINK);
                myDrawObj.fillSquare(i, j, g*subSize);
                count++;
                if(count % 2 == 0){
                    myDrawObj.setColor(Color.ORANGE);
                    myDrawObj.fillSquare(i, j, g*subSize);
                }
            }
        }
        myDrawObj.setColor(Color.BLACK);

        //Ziffern ausgeben
        int j = cellSize/2;
        for (int k = 0; k < sudokuField.length; k++) {
            int i = cellSize/2;
            for (int l = 0; l < sudokuField[k].length; l++) {
                myDrawObj.drawText(i, j, Integer.toString(sudokuField[k][l]));
                i += cellSize;
            }
            j += cellSize;
        }

        //Linien zeichnen
        for (int i = cellSize;  i < cellSize * 9; i += cellSize) {
            myDrawObj.setLineWidth(3);
            myDrawObj.drawLine(i,0,i,i * 9);
            myDrawObj.drawLine(0,i,i * 9,i);
        }
        myDrawObj.show();
    }

    private static void printArray(int[][] inputArray) {
        for (int y = 0; y < inputArray.length; y++) {
            for (int x = 0; x < inputArray[y].length; x++) {
                System.out.print(inputArray[y][x] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {

        CodeDraw myDrawObj = new CodeDraw(sSize * cellSize, sSize * cellSize);
        TextFormat format = myDrawObj.getTextFormat();
        format.setFontSize(16);
        format.setHorizontalAlign(HorizontalAlign.CENTER);

        String filename = "./src/sudoku4.csv"; //sudoku0.csv - sudoku7.csv available!
        System.out.println("Reading " + filename + " ...");
        int[][] sudokuField = readArrayFromFile(filename);
        printArray(sudokuField);

        System.out.println("Solving Sudoku ...");
        solveSudoku(sudokuField, 0);
        printArray(sudokuField);
        System.out.println("Valid solution: " + isValidSudokuSolution(sudokuField));
        System.out.println();

        drawSudokuField(myDrawObj, sudokuField);

        System.out.println("Results of the heatMap:");
        printArray(heatMap);
    }
}










