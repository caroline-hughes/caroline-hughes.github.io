import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* Caroline Hughes
Programming Assignment 2
 */

public class Flow {


	public static double getProb() {

		int sizeE = 0;
		int sizeS = 1024;

		int n = 10;
		int[] array = new int[n];
		recursiveMethod(n, array);

		return sizeE/sizeS;
	}



	public static void recursiveMethod(int n, int[] array) {
		// interates thru each possible bit string of length 10, calls doSearch on it

		if (n <= 0) {
			//System.out.println(Arrays.toString(array));
			doSearch(makeAdjList(array));
		} else {
			array[n - 1] = 0;
			recursiveMethod(n - 1, array);
			array[n - 1] = 1;
			recursiveMethod(n - 1, array);
		}}



	public static LinkedList<Integer>[] makeAdjList(int[] array) {
		//initialize array of linked lists of size 5
		LinkedList<Integer>[] graph = new LinkedList[5];

		if(array[0]==1)
			graph[0].add(2);
		graph[1].add(1);
		if(array[1]==1)
			graph[0].add(3);
		graph[2].add(1);
		if(array[2]==1)
			graph[0].add(4);
		graph[3].add(1);
		if(array[3]==1)
			graph[0].add(5);
		graph[4].add(1);

		if(array[4]==1)
			graph[1].add(3);
		graph[2].add(2);
		if(array[5]==1)
			graph[1].add(4);
		graph[3].add(2);
		if(array[6]==1)
			graph[1].add(5);
		graph[4].add(2);

		if(array[7]==1)
			graph[2].add(4);
		graph[3].add(3);
		if(array[8]==1)
			graph[2].add(5);
		graph[4].add(3);

		if(array[9]==1)
			graph[3].add(5);
		graph[4].add(4);

		return graph;	}


	// Breadth first search
	public static void doSearch(LinkedList<Integer>[] graph) {

		boolean[] visited = {false, false, false, false, false};
		Queue<Integer> queue = new LinkedList<>();

		queue.add(graph[0]); //add first adj list to queue
		visited[0]=true;

		while(!queue.isEmpty()) {
			List<Integer> blub = new LinkedList<Integer>();
			blub=queue.peek();
			queue.remove();
			for (int i=0; i<blub.size(); i++) {
				int ele = blub.get(i);
				if(!visited[ele-1]) {
					visited[ele-1] = true;
					queue.add(graph[ele-1]);}
			}
		}

		int counter=0;
		for(int p=0; p<5; p++)
		{
			if (!visited[p])
				counter++;
		}

		if (counter==0)
			sizeE++;
	}


	public static void main(String[] args) {

		getProb();
	}

}
