package ca.mcgill.ecse223.quoridor;

import java.util.Iterator;
import java.util.LinkedList;

public class WallGraph {

    private int V;
    private static WallGraph graph_instance;

    private LinkedList<Integer> adj[];

    public WallGraph(){
        V = 81;
        this.adj = new LinkedList[81];
        for(int i = 0; i<81 ;i++){
            this.adj[i] = new LinkedList<>();
        }
    }

    public static WallGraph getWallGraph(){
        if(graph_instance==null){
            graph_instance = new WallGraph();
        }
        return graph_instance;
    }

    public void addEdge(int v, int w){
        this.adj[v].add(w);
    }

    public void cutEdge(int v, int w){
        this.adj[v].removeFirstOccurrence(w);
        this.adj[w].removeFirstOccurrence(v);
    }

    // prints BFS traversal from a given source s
    void BFS(int s)
    {
        // Mark all the vertices as not visited(By default
        // set as false)
        boolean visited[] = new boolean[V];

        // Create a queue for BFS
        LinkedList<Integer> queue = new LinkedList<Integer>();

        // Mark the current node as visited and enqueue it
        visited[s]=true;
        queue.add(s);

        while (queue.size() != 0)
        {
            // Dequeue a vertex from queue and print it
            s = queue.poll();
            System.out.print(s+" ");

            // Get all adjacent vertices of the dequeued vertex s
            // If a adjacent has not been visited, then mark it
            // visited and enqueue it
            Iterator<Integer> i = adj[s].listIterator();
            while (i.hasNext())
            {
                int n = i.next();
                if (!visited[n])
                {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
    }
}