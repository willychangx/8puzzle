/* *****************************************************************************
 *  Name: Willy Chang
 *  Date: 10/18/2021
 *  Description: Defines Solver java class.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class Solver {
    public int manhattanCount = 0;
    public int boardCount = 0;
    public int equalsCount = 0;
    private MinPQ<SearchNode> pq; // PriorityQueue for initial Board
    private final int minMoves; // min number of moves to reach solution
    private final boolean solvable; // flag if the Board is solvable

    /**
     * Finds solution to initial board (using A* algorithm).
     *
     * @param initial {@code Board}
     */
    public Solver(Board initial) {
        // Throw null on bad argument
        if (initial == null)
            throw new java.lang.IllegalArgumentException("Null argument to solver.");

        // Initialize first min priority queue
        pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, 0, null));

        // Initialize the priority queue for the twin board
        MinPQ<SearchNode> pqTwin = new MinPQ<SearchNode>();
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));

        // search for solution on either first pqueue or twin pqueue
        while (!pq.min().b.isGoal() && !pqTwin.min().b.isGoal()) {
            // Go through neighbors of the min SearchNode and add them to the queue if the new board is not equal to the previous board
            SearchNode curr = pq.min();
            pq.delMin();
            for (Board neighbor : curr.b.neighbors()) {
                boardCount++;
                if (curr.moves == 0)
                    pq.insert(new SearchNode(neighbor, curr.moves + 1, curr));
                else if (!neighbor.equals(curr.previous.b))
                    pq.insert(new SearchNode(neighbor, curr.moves + 1, curr));
                if (curr.moves != 0)
                    equalsCount++;
            }

            // Go through neighbors of the min SearchNode for the twin pqueue and add them to the queue if the new board is not equal to the previous board
            SearchNode currTwin = pqTwin.min();
            pqTwin.delMin();
            for (Board neighbor : currTwin.b.neighbors()) {
                boardCount++;
                if (currTwin.moves == 0)
                    pqTwin.insert(new SearchNode(neighbor, currTwin.moves + 1, currTwin));
                else if (!neighbor.equals(currTwin.previous.b))
                    pqTwin.insert(new SearchNode(neighbor, currTwin.moves + 1, currTwin));
                if (curr.moves != 0)
                    equalsCount++;
            }
        }

        // Check if there's a solution and update solvable and minMoves
        if (pqTwin.min().b.isGoal()) {
            solvable = false;
            minMoves = -1;
        }
        else {
            solvable = true;
            minMoves = pq.min().moves;
        }
    }

    /**
     * Comparable class {@code SearchNode}
     * Used to store {@code previous} SearchNode, current Board {@code b}, number of {@code moves}
     * to reach the current board, and the {@code priority} of the SearchNode.
     */
    private class SearchNode implements Comparable<SearchNode> {
        private SearchNode previous; // previous SearchNode
        private Board b; // current Board
        private int moves; // moves to the current Board
        private int priority; // manhattan priority

        /**
         * Constructor for SearchNode.
         *
         * @param initial {@code Board}
         * @param nMoves  {@code int} moves to get to current board
         * @param prev    {@code SearchNode} previous SearchNode
         */
        public SearchNode(Board initial, int nMoves, SearchNode prev) {
            this.previous = prev;
            this.b = initial;
            this.moves = nMoves;
            this.priority = nMoves + initial.manhattan();
            manhattanCount++;
        }

        /**
         * Comparison definition to sort priorities in PQ.
         *
         * @param that {@code SearchNode} other SearchNode
         * @return comparison value; positive if current node has a higher priority,
         * zero if the current node the same priority, and negative if current
         * node has a lower priority
         */
        public int compareTo(SearchNode that) {
            return this.priority - that.priority;
        }
    }

    /**
     * Return if the initial Board is solvable
     *
     * @return {@code true} if Board is solvable; {@code false} otherwise
     */
    public boolean isSolvable() {
        return solvable;
    }

    /**
     * Check the minimum moves to solve the board.
     *
     * @return {@code minMoves} to solve initial board; -1 if unsolvable
     */
    public int moves() {
        if (solvable)
            return minMoves;
        return -1;
    }

    /**
     * Sequence of boards in a shortest solution; null if unsolvable.
     *
     * @return {@code solution}
     */
    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;

        Stack<Board> solution = new Stack<Board>();
        SearchNode curr = pq.min();
        while (curr.previous != null) {
            solution.push(curr.b);
            curr = curr.previous;
        }
        solution.push(curr.b);
        return solution;
    }

    /**
     * Test client
     *
     * @param args command-line inputs
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            // create initial board from file
            In in = new In(args[0]);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    tiles[i][j] = in.readInt();
            Board initial = new Board(tiles);
            // solve the puzzle
            Solver solver = new Solver(initial);
            // print solution to standard output
            if (!solver.isSolvable())
                StdOut.println("No solution possible");
            else {
                StdOut.println("Minimum number of moves = " + solver.moves());
                for (Board board : solver.solution())
                    StdOut.println(board);
            }
        }
        else {
            String[] inputs = new String[] {
                    "puzzle00.txt", "puzzle01.txt", "puzzle02.txt", "puzzle03.txt", "puzzle04.txt",
                    "puzzle05.txt", "puzzle06.txt", "puzzle07.txt", "puzzle08.txt", "puzzle09.txt",
                    "puzzle10.txt", "puzzle11.txt", "puzzle12.txt", "puzzle13.txt"
            };
            System.out.println("Test 1: moves()");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                StdOut.println("Minimum number of moves = " + solver.moves());
            }
            System.out.println("---");
            System.out.println("Test 2: solution()");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                for (Board board : solver.solution())
                    StdOut.println(board);
            }
            System.out.println("---");
            inputs = new String[] {
                    "puzzle01.txt", "puzzle03.txt", "puzzle04.txt", "puzzle17.txt",
                    "puzzle3x3-unsolvable1.txt", "puzzle3x3-unsolvable2.txt",
                    "puzzle4x4-unsolvable.txt"
            };
            System.out.println("Test 3: isSolvable()");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                StdOut.println("isSolvable() = " + solver.isSolvable());
            }
            System.out.println("---");
            System.out.println("Test 4: Constructor Exceptions");
            try {
                Solver solver = new Solver(null);
                System.out.println("Solver constructed with null.");
            }
            catch (IllegalArgumentException illegalArgumentException) {
                System.out.println("Illegal argument in constructor.");
            }
            System.out.println("---");
            inputs = new String[] {
                    "puzzle2x2-00.txt", "puzzle2x2-01.txt", "puzzle2x2-02.txt", "puzzle2x2-03.txt",
                    "puzzle2x2-04.txt", "puzzle2x2-05.txt", "puzzle2x2-06.txt"
            };
            System.out.println("Test 5: moves() w/ 2x2");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                StdOut.println("moves() = " + solver.moves());
            }
            System.out.println("---");
            System.out.println("Test 6: solution() w/ 2x2");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                for (Board board : solver.solution())
                    StdOut.println(board);
            }
            System.out.println("---");
            inputs = new String[] {
                    "puzzle3x3-00.txt", "puzzle3x3-01.txt", "puzzle3x3-02.txt", "puzzle3x3-03.txt",
                    "puzzle3x3-04.txt", "puzzle3x3-05.txt", "puzzle3x3-06.txt", "puzzle3x3-07.txt",
                    "puzzle3x3-08.txt", "puzzle3x3-09.txt", "puzzle3x3-10.txt"
            };
            System.out.println("Test 7: moves() w/ 3x3");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                StdOut.println("moves() = " + solver.moves());
            }
            System.out.println("---");
            inputs = new String[] {
                    "puzzle3x3-02.txt", "puzzle3x3-03.txt", "puzzle3x3-04.txt",
                    "puzzle3x3-05.txt", "puzzle3x3-06.txt", "puzzle3x3-07.txt",
                    "puzzle3x3-08.txt", "puzzle3x3-09.txt", "puzzle3x3-10.txt"
            };
            System.out.println("Test 8: solution() w/ 3x3");
            for (String s : inputs) {
                System.out.println("* " + s);
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                for (Board board : solver.solution())
                    StdOut.println(board);
            }
            System.out.println("---");
            inputs = new String[] {
                    "puzzle41.txt", "puzzle34.txt", "puzzle37.txt",
                    "puzzle44.txt", "puzzle32.txt", "puzzle35.txt",
                    "puzzle33.txt", "puzzle43.txt", "puzzle46.txt",
                    "puzzle40.txt", "puzzle36.txt", "puzzle45.txt",
                    };
            System.out.println("Test 9: Timing");
            System.out.println("filename\tmoves\tn\tseconds");
            System.out.println("===============================================");
            for (String s : inputs) {
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Stopwatch timer = new Stopwatch();
                Solver solver = new Solver(initial);
                double time = timer.elapsedTime();
                System.out.println(
                        s + "\t" + solver.moves() + "\t" + initial.dimension() + "\t" + time);
            }
            System.out.println("---");
            inputs = new String[] {
                    "puzzle32.txt", "puzzle33.txt", "puzzle34.txt",
                    "puzzle35.txt", "puzzle36.txt", "puzzle37.txt",
                    "puzzle39.txt", "puzzle40.txt", "puzzle41.txt",
                    "puzzle43.txt", "puzzle44.txt", "puzzle45.txt", "puzzle46.txt"
            };
            System.out.println("Test 10: Method Counts");
            System.out.println("filename\tBoard()\tequals()\tmanhattan()");
            System.out.println("===============================================");
            for (String s : inputs) {
                In in = new In(s);
                int n = in.readInt();
                int[][] tiles = new int[n][n];
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        tiles[i][j] = in.readInt();
                    }
                }
                Board initial = new Board(tiles);
                Solver solver = new Solver(initial);
                System.out.println(
                        s + "\t" + solver.boardCount + "\t" + solver.equalsCount + "\t"
                                + solver.manhattanCount);
            }
        }
    }
}
