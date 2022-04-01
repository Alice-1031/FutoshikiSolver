import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ForkJoinPool;

/**
 * Driver class with the main method
 * 
 * @author shreya.jaiswal
 *
 */
public class FutoshikiDriver {

    public static void main(String[] args) {
        while (true) {
            Scanner input = new Scanner(System.in);
            Long startTime;
            Long endTime;
            System.out.println("Welcome to the Futoshiki solver\n" + "Enter a puzzle number: ");

            Puzzle puzzle = null;
            try {
                int puzzleNumber = input.nextInt();
                puzzle = Puzzle.fromFile(puzzleNumber + ".txt");
            } catch (IllegalArgumentException e) {
                System.out.println("Problem reading file");
                break;
            } catch (InputMismatchException e) {
                System.out.println("Problem reading file");
                break;
            }
            System.out.println(puzzle.toString());
            System.out.println("How would you like to solve the puzzle\n" + "1. Sequential\n" + "2. Fork/Join");
            int solve = input.nextInt();
            if (solve == 1) {
                startTime = System.currentTimeMillis();
                Puzzle solved = Puzzle.solve(puzzle, 0);
                endTime = System.currentTimeMillis();
                if (solved != null) {
                    System.out.println(solved.toString());
                    System.out.println("Sequential took " + (endTime - startTime) / 1000.0 + " s");
                } else {
                    System.out.println("Unsolvable puzzle");
                }
               
            } else if (solve == 2) {
                startTime = System.currentTimeMillis();
                PuzzleSolver initial = new PuzzleSolver(puzzle, 0);
                ForkJoinPool.commonPool().invoke(initial);
                endTime = System.currentTimeMillis();
                if (initial.getAns() != null) {
                    System.out.println(initial.getAns().toString());
                    System.out.println("Fork/Join took " + (endTime - startTime) / 1000.0 + " s");
                } else {
                    System.out.println("Unsolvable puzzle");
                }
            }

        }
    }
}
