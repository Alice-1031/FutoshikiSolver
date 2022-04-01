import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;

/**
 * Fork/Join solver class for puzzle
 * 
 * @author shreya.jaiswal
 *
 */
public class PuzzleSolver extends RecursiveAction {
    /** Solves sequentially once threshold reached */
    private int THRESHOLD = 20;
    /**
     * initial or partially solved input puzzle
     */
    private Puzzle game;
    /**
     * the spot to start solving at
     */
    private int currentSpot;
    /**
     * Final solution
     */
    private Puzzle answer;

    /**
     * Constructs a new solver
     * 
     * @param game        input puzzle
     * @param currentSpot the spot to start solving at
     */
    public PuzzleSolver(Puzzle game, int currentSpot) {

        this.game = game;
        this.currentSpot = currentSpot;
        this.answer = null;

    }

    /**
     * public getter for final answer
     * 
     * @return solution Puzzle
     */
    public Puzzle getAns() {
        return answer;
    }

    @Override
    /**
     * computes the Puzzle using Fork/Join framework
     * 
     */
    public void compute() {
        int size = game.getSize();
        if (size * size - currentSpot <= THRESHOLD) {
            answer = Puzzle.solve(game, currentSpot);
        } else {

            int currentRow = currentSpot / size;
            int currentColumn = currentSpot % size;

            if (game.getValue(currentRow, currentColumn) == Puzzle.EMPTY) {
                // empty spot, can try out possibilities
                ArrayList<PuzzleSolver> friends = new ArrayList<PuzzleSolver>();
                for (int i = 1; i <= size; i++) {
                    Puzzle next = new Puzzle(game);
                    next.insertValue(currentRow, currentColumn, i);

                    if (next.isValid() == true) {

                        friends.add(new PuzzleSolver(next, currentSpot + 1));

                    }

                }

                for (int i = 0; i < friends.size(); i++) {

                    friends.get(i).fork();
                }

                for (int i = 0; i < friends.size(); i++) {

                    friends.get(i).join();
                }
                for (int i = 0; i < friends.size(); i++) {

                    if (friends.get(i).getAns() != null) {

                        this.answer = friends.get(i).getAns();
                    }
                }

            } else {

                PuzzleSolver friend = new PuzzleSolver(game, currentSpot + 1);
                friend.compute();
                this.answer = friend.getAns();

            }
        }

    }

}
