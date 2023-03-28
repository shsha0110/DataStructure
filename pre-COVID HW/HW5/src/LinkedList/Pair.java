package LinkedList;

public class Pair implements Comparable<Pair> {
    public int lineNum, startPoint;

    public Pair(int lineNum, int startPoint) {
        this.lineNum = lineNum;
        this.startPoint = startPoint;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", lineNum, startPoint);
    }

    @Override
    public int compareTo(Pair pair) {
        if (lineNum != pair.lineNum) {
            return lineNum > pair.lineNum ? 1 : -1;
        }
        if (startPoint != pair.startPoint) {
            return startPoint > pair.startPoint ? 1 : -1;
        }
        return 0;
    }

}
