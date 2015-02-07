package main;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import javax.script.ScriptException;

import logic.Node;

public class Main 
{

	private static boolean debug;
	public static void main(String[] args) throws ScriptException 
	{
		if(args.length < 5)
		{
			System.out.println("not enough args");
			return;
		}
		debug = args[0].equals("1");
		String search = args[1];
		Number target = Integer.parseInt(args[2]);
		ArrayList<Integer> av = new ArrayList<Integer>();
		for (int i = 3; i < args.length; i++)
		{
			av.add(Integer.parseInt(args[i]));
		}

		switch (search)
		{
		case "DFS":
			dfs(target,av);
			break;
		case "BFS":
			bfs(target,av);
			break;
		case "IDS":
			ids(target,av);
			break;
		case "UCS":
			ucs(target,av);
			break;
		
		default:
			System.out.println("Bad search param, bailing");
			System.exit(0);
		}
		System.out.println("It seems that the search has failed, so sorry");
	}

	private static void ucs(Number target, ArrayList<Integer> av) throws ScriptException 
	{
		System.out.println("Starting UCS, making initial nodes");
		Node[] ns = new Node(av).expand();
		
		if(debug)
		{
			System.out.println("initial nodes generated:");
			printAll(ns);
		}
		PriorityQueue<Node> h = new PriorityQueue<Node>();
		while(ns.length >0 || !h.isEmpty())
		{
			if(ns.length > 0)//push stuff onto stack
			{
				for(Node n: ns)
				{
					if(trial(n,target))
					{
						System.exit(0);
					}
					h.add(n);
				}
				//should never get here, but I am being defensive
				
			}
			if(h.isEmpty())
			{
				continue;
			}
			Node n = h.remove();
			ns=n.expand();
			
			if (debug)
			{
				System.out.println("expanding node "+n);
				System.out.println("Childeren are:");
				printAll(ns);
			}
		}


		
	}

	private static void ids(Number target, ArrayList<Integer> av) throws ScriptException 
	{
		for(int d = 1; d < 2*av.size(); ++d)//since we have av.size # we should have have av.size rounds at the most, so doubling that is conservative
		{
			
			System.out.println("Starting round depth"+d+" IDS, making initial nodes");
			Node[] ns = new Node(av).expand();
			Wrap[] ws = new Wrap[ns.length];
			for(int i = 0; i< ws.length; ++i)
			{
				ws[i]=new Wrap();
				ws[i].depth=0;
				ws[i].n=ns[i];
			}
			if(debug)
			{
				System.out.println("initial nodes generated:");
				printAll(ns);
			}
			Stack<Wrap> st = new Stack<Wrap>();
		
		
			while(ws.length >0 || !st.isEmpty())
			{
				if(ws.length > 0)//push stuff onto stack
				{
					for(Wrap w : ws)
					{
						if(trial(w.n,target))
						{
							System.exit(0);
						}
						if(w.depth <d)
						{
							st.push(w);
						}
					}
					
				}
				//nothing got added this round, and stack is dry
				if(st.empty())
				{
					break;
				}
				Wrap w = st.pop();
				ns=w.n.expand();
				ws = new Wrap[ns.length];
				for(int i = 0; i <ns.length;i++ )
				{
					ws[i]= new Wrap();
					ws[i].depth=w.depth+1;
					ws[i].n=ns[i];
				}
				
				if (debug)
				{
					System.out.println("expanding node "+w.n);
					System.out.println("Childeren are:");
					printAll(ns);
				}
			}
			System.out.println("Out of while");
		}

	}

	private static void bfs(Number target, ArrayList<Integer> av) throws ScriptException 
	{
		
		System.out.println("Starting  bfs, making initial nodes");
		Node[] ns = new Node(av).expand();
		
		if(debug)
		{
			System.out.println("initial nodes generated:");
			printAll(ns);
		}
		Queue<Node> q = new ArrayDeque<Node>();
		while(ns.length >0 || !q.isEmpty())
		{
			if(ns.length > 0)//push stuff onto stack
			{
				for(Node n: ns)
				{
					if(trial(n,target))
					{
						System.exit(0);
					}
					q.add(n);
				}
				
			}
			//should never get here, but I am being defensive
			if(q.isEmpty())
			{
				continue;
			}
			Node n = q.remove();
			ns=n.expand();
			
			if (debug)
			{
				System.out.println("expanding node "+n);
				System.out.println("Childeren are:");
				printAll(ns);
			}
		}

	}

	private static void dfs(Number target, ArrayList<Integer> av) throws ScriptException 
	{
		System.out.println("Starting dfs, making initial nodes");
		Node[] ns = new Node(av).expand();
		
		if(debug)
		{
			System.out.println("initial nodes generated:");
			printAll(ns);
		}
		Stack<Node> st = new Stack<Node>();
		while(ns.length >0 || !st.isEmpty())
		{
			if(ns.length > 0)//push stuff onto stack
			{
				for(Node n: ns)
				{
					if(trial(n,target))
					{
						System.exit(0);
					}
					st.push(n);
				}
				
			}
			//should never get here, but I am being defensive
			if(st.empty())
			{
				continue;
			}
			Node n = st.pop();
			ns=n.expand();
			
			if (debug)
			{
				System.out.println("expanding node "+n);
				System.out.println("Childeren are:");
				printAll(ns);
			}
		}
	
		
		
	}

	private static boolean trial(Node n, Number target) 
	{
		if( n.VAL.intValue()==target.intValue() && target.doubleValue()== target.intValue()*1.0)//because I didn't explicitly check division
		{//I make sure the answer isn't a fraction
			System.out.println("The answer is: "+n+"with cost "+n.COST);
			return true;
		}
		return false;
		
	}

	private static void printAll(Node[] ns)
	{
		for(Node n :ns)
		{
			System.out.println("\t"+n);
		}
	}
	
	private static class Wrap
	{
		public	Node n;
		public int depth;
	}
	
}
