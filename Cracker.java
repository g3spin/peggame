import java.util.*;

public class gamePeg {

	final public static int SIZE = 5;
	final public static boolean DEBUG = false;
	final public static int HOLE_R = 4;
	final public static int HOLE_C = 2;
	public static HashMap<Integer,Integer> distances;

	final public static int[] DX = {-1,-1,0,0,1,1};
	final public static int[] DY = {-1,0,-1,1,0,1};

	public static void main(String[] args) {

		int start = boardInitiation(HOLE_R, HOLE_C);

		distances = new HashMap<Integer,Integer>();
		distances.put(start, 0);

		LinkedList<Integer> q = new LinkedList<Integer>();
		q.offer(start);

		while (q.size() > 0) {

			int cur = q.poll();
			int curdist = distances.get(cur);

			ArrayList<Integer> nextList = getNextPos(cur);

			for (int i=0; i<nextList.size(); i++) {
				if (!distances.containsKey(nextList.get(i))) {
					distances.put(nextList.get(i), curdist+1);
					q.offer(nextList.get(i));
				}
			}

			// Initial version will only print possible ending boards.
			if (nextList.size() == 0) print(cur);
		}

		if (DEBUG) {
			for (Integer board: distances.keySet())
				print(board);
		}
	}

	public static int boardInitiation(int holeR, int holeC) {

		// This is the full board.
		int mask = 0;
		for (int i=0; i<SIZE; i++)
			for (int j=0; j<=i; j++)
				mask = mask | (1<<(SIZE*i+j));

		// Just subtract out the current hole.
		return mask - (1<<(SIZE*holeR+holeC));
	}
	public static ArrayList<Integer> getNextPos(int mask) {

		ArrayList<Integer> pos = new ArrayList<Integer>();

		for (int r =0; r<SIZE; r++) {
			for (int c=0; c<=SIZE; c++) {

				// Now try each move.
				for (int dir=0; dir<DX.length; dir++) {

					// Ending square is out of bounds.
					if (!inbounds(r+2*DX[dir], c+2*DY[dir])) continue;

					// A move is valid only if the first two holes have gamePegs and the destination doesn't.
					if (on(mask, SIZE*r+c) && on(mask, SIZE*(r+DX[dir]) + c + DY[dir]) && !on(mask, SIZE*(r+2*DX[dir]) + c + 2*DY[dir])) {
						int newpos = go(mask, dir, r, c);
						pos.add(newpos);
					}
				}
			}
		}

		return pos;
	}

	public static void print(int mask) {

		for (int i=0; i<SIZE; i++) {

			for (int j=0; j<SIZE-1-i; j++) System.out.print(" ");

			for (int j=0; j<=i; j++) {
				if (on(mask, SIZE*i+j)) System.out.print("X ");
				else					System.out.print("_ ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static int go(int mask, int dir, int r, int c) {

		int start = SIZE*r + c;
		int mid = SIZE*(r+DX[dir]) + c + DY[dir];
		int end = SIZE*(r+2*DX[dir]) + c + 2*DY[dir];

		return mask - (1<<start) - (1<<mid) + (1<<end);
	}

	public static boolean on(int mask, int bit) {
		return (mask & (1<<bit)) != 0;
	}

	public static boolean inbounds(int myr, int myc) {
		return myr >= 0 && myr < SIZE && myc >= 0 && myc <= myr;
	}
}
