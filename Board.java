/* *****************************************************************************
 *  Name: Willy Chang
 *  Date: 10/18/2021
 *  Description: Defines Board java class.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Board {
    private final int[][] myTiles; // Copy of board tiles
    private int hammingSum = 0; // Hamming distance
    private int manhattanSum = 0; // Manhattan distance
    private int n; // Board dimension
    private int[] empty = new int[2];

    /**
     * Create a board from an n-by-n array of tiles where tiles[row][col] = tile at (row, col).
     *
     * @param tiles 2D int array w/ unique numbers from 0 to n^2 - 1
     */
    public Board(int[][] tiles) {
        n = tiles.length;
        myTiles = new int[n][n];

        // Calculate Hamming and Manhattan distance while making a deep copy of tiles
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                // Make a deep copy
                myTiles[row][col] = tiles[row][col];

                // Check if the tile is not empty and if it's not in the correct position
                if (myTiles[row][col] != 0
                        && myTiles[row][col] != (col + row * n + 1)) {
                    hammingSum++;
                    manhattanSum += Math.abs(row - intendedRow(myTiles[row][col]) + 1)
                            + Math.abs(col - intendedCol(myTiles[row][col]) + 1);
                }
                if (myTiles[row][col] == 0)
                    empty = new int[] { row, col };
            }
        }
    }

    /**
     * Return the intended row for a given number.
     *
     * @param number integer from 0 to n^2 - 1
     * @return intended row index starting from 1
     */
    public int intendedRow(int number) {
        return (int) Math.ceil((double) number / (double) n);
    }

    /**
     * Return the intended column for a given number.
     *
     * @param number integer from 0 to n^2 - 1
     * @return intended column index starting from 1
     */
    public int intendedCol(int number) {
        if (number % n == 0) return n;
        return number % n;
    }

    /**
     * Return string representation of the board.
     *
     * @return string representation of the board with the dimension at the top
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(n + "\n");
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (myTiles[row][col] < 10)
                    result.append(" " + myTiles[row][col] + "\t");
                else
                    result.append(myTiles[row][col] + "\t");
            }
            result.append("\n");
        }
        return result.toString();
    }

    /**
     * Return the dimension of the board.
     *
     * @return {@code n}
     */
    public int dimension() {
        return n;
    }

    /**
     * Return the hamming distance of the board.
     *
     * @return {@code hammingSum}
     */
    public int hamming() {
        return hammingSum;
    }

    /**
     * Return the manhattan distance of the board.
     *
     * @return {@code manhattanSum}
     */
    public int manhattan() {
        return manhattanSum;
    }

    /**
     * Check if the board has met it's goal (i.e. all tiles in correct position).
     *
     * @return {@code true} if the board is the goal;
     * {@code false} otherwise
     */
    public boolean isGoal() {
        if (hammingSum == 0)
            return true;
        return false;
    }

    /**
     * Check if the board {@code y} has the same tile positions as the current board.
     *
     * @param y {@code Board}
     * @return {@code true} if all tiles match; {@code false} otherwise;
     * throw java.lang.IllegalArgumentException on null and non-Board arguments.
     */
    public boolean equals(Object y) {
        if (y == null)
            throw new IllegalArgumentException("Null argument in equals().");
        if (y.getClass() != this.getClass())
            throw new IllegalArgumentException("Argument not a Board class.");

        Board compareObj = (Board) y;

        // Check if the boards are the same size.
        if (compareObj.dimension() != n)
            return false;

        // Go through each tile and if there's a non-matching one, return false
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (myTiles[row][col] != compareObj.myTiles[row][col])
                    return false;
            }
        }
        return true;
    }

    /**
     * Helper function to check if the indexes are within bounds.
     *
     * @param row {@code int} representing the row
     * @param col {@code int} representing the col
     * @return {@code true} if the col and row are between 0 and n (excluding);
     * {@code false} otherwise
     */
    public boolean inside(int row, int col) {
        if (row >= 0 && row < n && col >= 0 && col < n)
            return true;
        return false;
    }

    /**
     * Return iterable Boards representing neighbors (i.e. moving tiles to empty space).
     *
     * @return {@code Iterable<Board>}
     */
    public Iterable<Board> neighbors() {
        Queue<Board> list = new Queue<Board>();

        int row = empty[0];
        int col = empty[1];

        // Move top tile down
        if (inside(row - 1, col)) {
            int[][] tmp = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tmp[i][j] = myTiles[i][j];
                }
            }
            tmp[row][col] = tmp[row - 1][col];
            tmp[row - 1][col] = 0;
            Board tmpB = new Board(tmp);
            list.enqueue(tmpB);
        }
        // Move bottom tile up
        if (inside(row + 1, col)) {
            int[][] tmp = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tmp[i][j] = myTiles[i][j];
                }
            }
            tmp[row][col] = tmp[row + 1][col];
            tmp[row + 1][col] = 0;
            Board tmpB = new Board(tmp);
            list.enqueue(tmpB);
        }
        // Move left tile right
        if (inside(row, col - 1)) {
            int[][] tmp = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tmp[i][j] = myTiles[i][j];
                }
            }
            tmp[row][col] = tmp[row][col - 1];
            tmp[row][col - 1] = 0;
            Board tmpB = new Board(tmp);
            list.enqueue(tmpB);
        }
        // Move right tile left
        if (inside(row, col + 1)) {
            int[][] tmp = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tmp[i][j] = myTiles[i][j];
                }
            }
            tmp[row][col] = tmp[row][col + 1];
            tmp[row][col + 1] = 0;
            Board tmpB = new Board(tmp);
            list.enqueue(tmpB);
        }

        return list;
    }

    /**
     * Randomly select a tile and switch with a random adjacent tile.
     *
     * @return {@code Board} that is a twin of the current Board
     */
    public Board twin() {
        int[][] twin = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                twin[i][j] = myTiles[i][j];
            }
        }

        ArrayList<int[]> offset = new ArrayList<>();
        offset.add(new int[] { 0, -1 });
        offset.add(new int[] { 0, 1 });
        offset.add(new int[] { -1, 0 });
        offset.add(new int[] { 1, 0 });

        int row = (int) Math.round((Math.random() * (n - 1)));
        int col = (int) Math.round((Math.random() * (n - 1)));

        // Find a random tile (non-empty) to switch
        while (twin[row][col] == 0) {
            row = (int) Math.round((Math.random() * (n - 1)));
            col = (int) Math.round((Math.random() * (n - 1)));
        }

        int xOffset;
        int yOffset;
        int offsetIdx = (int) Math.round((Math.random() * (offset.size() - 1)));
        xOffset = offset.get(offsetIdx)[0];
        yOffset = offset.get(offsetIdx)[1];
        offset.remove(offsetIdx);

        // Find a random adjacent tile to switch (non-empty)
        while ((row + yOffset < 0) || (row + yOffset >= twin.length)
                || (col + xOffset < 0) || (col + xOffset >= twin.length)
                || (twin[row + yOffset][col + xOffset] == 0)) {
            offsetIdx = (int) Math.round((Math.random() * (offset.size() - 1)));
            xOffset = offset.get(offsetIdx)[0];
            yOffset = offset.get(offsetIdx)[1];
            offset.remove(offsetIdx);
        }

        // Switch the tiles.
        int tmp = twin[row][col];
        twin[row][col] = twin[row + yOffset][col + xOffset];
        twin[row + yOffset][col + xOffset] = tmp;
        return new Board(twin);
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        String[] inputs = new String[] {
                "puzzle04.txt", "puzzle00.txt", "puzzle07.txt", "puzzle17.txt", "puzzle27.txt",
                "puzzle2x2-unsolvable1.txt"
        };

        System.out.println("Test 1: hamming()");
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
            Board board = new Board(tiles);
            StdOut.println("hamming = " + board.hamming());
        }
        System.out.println("---");
        System.out.println("Test 2: manhattan()");
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

            Board board = new Board(tiles);
            StdOut.println("manhattan = " + board.manhattan());
        }
        System.out.println("---");
        System.out.println("Test 3: dimension()");
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
            Board board = new Board(tiles);
            StdOut.println("dimension = " + board.dimension());
        }
        System.out.println("---");
        inputs = new String[] {
                "puzzle04.txt", "puzzle00.txt", "puzzle06.txt", "puzzle09.txt", "puzzle23.txt",
                "puzzle2x2-unsolvable1.txt"
        };
        System.out.println("Test 4: toString()");
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
            Board board = new Board(tiles);
            StdOut.println(board.toString());
        }
        System.out.println("---");
        System.out.println("Test 5: neighbors()");
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
            Board board = new Board(tiles);
            Iterable<Board> k = board.neighbors();
            for (Board b : k) {
                System.out.println(b.toString());
            }
        }
        System.out.println("---");
        inputs = new String[] {
                "puzzle04.txt", "puzzle00.txt", "puzzle09.txt", "puzzle23.txt",
                "puzzle2x2-unsolvable1.txt"
        };
        System.out.println("Test 6: twin()");
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
            Board board = new Board(tiles);
            StdOut.println(board.twin().toString());
        }
        System.out.println("---");
        inputs = new String[] {
                "puzzle00.txt", "puzzle04.txt", "puzzle16.txt", "puzzle09.txt", "puzzle23.txt",
                "puzzle2x2-unsolvable1.txt", "puzzle3x3-00.txt", "puzzle4x4-00.txt"
        };
        System.out.println("Test 7: isGoal()");
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
            Board board = new Board(tiles);
            StdOut.println("isGoal = " + board.isGoal());
        }
        System.out.println("---");
        System.out.println("Test 8: equals()");
        In in = new In("puzzle04.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board board = new Board(tiles);
        Board twin = board.twin();
        Board mimic1 = board;
        Board mimic2 = board;
        if (board.equals(board))
            System.out.println("equals() is reflexive.");
        if (twin.equals(board) == board.equals(twin))
            System.out.println("equals() is symmetric.");
        if (mimic1.equals(board) && mimic2.equals(mimic1) && board.equals(mimic2))
            System.out.println("equals() is transitive.");
        try {
            StdOut.println(board.equals(null));
        }
        catch (IllegalArgumentException illegalArgumentException) {
            System.out.println("equals() with null produces IllegalArgumentException.");
        }
        System.out.println("---");
        System.out.println("Test 9: Immutable");
        System.out.println("Board is immutable.");
    }
}
