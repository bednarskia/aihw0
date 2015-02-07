package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Node implements Comparable<Node>
{
	private static Hashtable<Character,Integer> OpCosts;
	public final int COST;
	
	private static Pattern  regex= Pattern.compile("\\d+");
	
	//thank you Kuhar for mentioning this in class, I bet it is slow as heck however
	private static ScriptEngine e = new ScriptEngineManager().getEngineByName("js");
	private String state;
	public ArrayList<Integer> available;
	public final Number VAL;
	
	static
	{
		OpCosts= new Hashtable<Character,Integer>();
		OpCosts.put('+', 4);
		OpCosts.put('*', 6);
		OpCosts.put('-', 4);
		OpCosts.put('/', 32);
		
		
	}
	
	public Node(ArrayList<Integer> available) throws ScriptException
	{
		this(available,"",0);
	}
	//if I get an excpetion I'd rather let it bubble up for testing reasons
	private Node(ArrayList<Integer> available,  String state, int cost  ) throws ScriptException
	{
		this.available=available;
		this.state = state;
		this.COST=cost;
		
		//Once again I overheard Kuhar mention this in class, I'm all about that attribution, no plagarism.
		//(which couldn't be a worse song than "all about that bass")
		if(state !="")
		{
			this.VAL=(Number) e.eval(state);
		}
		else
		{
			this.VAL=0;
		}
	}
	@SuppressWarnings("unchecked")//casting cloned arraylist<int>, javas generics were totally ducttaped on
	//but still better than go's
	public Node[] expand() throws ScriptException
	{
		if(state=="")
		{
			HashSet<Node> h = new HashSet<Node>();
			
			for(int i =0; i < available.size(); i++)
			{
				//clone is shallow, but for ints who cares? Hint: not me, not anyone
				ArrayList<Integer> temp = (ArrayList<Integer>) available.clone();
				temp.remove(i);
				h.add(new Node(temp,"("+available.get(i)+")",0));
			}
			return h.toArray(new Node[0]);
		}
		//first generate everything we can via appending an op+number
		//ArrayList<Node> ret = new ArrayList<Node>();
		HashSet<Node> ret = new HashSet<Node>();
		for(Entry<Character,Integer> op: OpCosts.entrySet())
		{
			for(int num: available)
			{
				//( OLDSTATE + (9))
				String ns ="("+state+op.getKey()+"("+num+"))";
				ArrayList<Integer> temp = (ArrayList<Integer>) available.clone();
				temp.remove((Integer)num);
				Node n = new Node(temp,ns,COST+op.getValue());
				boolean b =ret.add(n);
				
			}
		}
		
		//for each atomic expression (number) try all possible simple expressions ((number) op (number))
		Matcher match = regex.matcher(state);
		while(match.find())
		{
			String front = state.substring(0, match.start());
			String back = state.substring(match.end());
			ArrayList<Integer> newAvailable = (ArrayList<Integer>) available.clone();
			
			//add the old int back into the eligible pool
			newAvailable.add(Integer.parseInt(state.substring(match.start(), match.end())));
			
			for(int i =0; i < newAvailable.size();++i)
			{
				for(int j=i+1; j < newAvailable.size();++j)
				{
					int a = newAvailable.get(i);
					int b = newAvailable.get(j);
					ArrayList<Integer> temp = (ArrayList<Integer>) newAvailable.clone();
					temp.remove((Integer)a);//hurray for method overloading
					temp.remove((Integer)b);//when I don't want to use ints as indexes...
					//for the ops +,* a op b = b op a, for others this is not so, so I will special case them
					for(Entry<Character,Integer> op: OpCosts.entrySet())
					{
						//((3)+(4)) -> (((a)*(b))+(4))
						Node n = new Node((ArrayList<Integer>) temp.clone(),front+"("+a+")"+op.getKey()+"("+b+")"+back,COST+op.getValue());
						ret.add(n);
						//above mentioned spec case
						if(op.getKey()=='+'||op.getKey()=='*' || b==a)
						{
							continue;
						}
						n = new Node((ArrayList<Integer>) temp.clone(),front+"("+b+")"+op.getKey()+"("+a+")"+back,COST+op.getValue());
						ret.add(n);
					}
				}
			}
		}
		
		return  ret.toArray(new Node[0]);//wow, that is one clunker
		//since (cast) ret.toArray no worky
	}
	@Override
	public int hashCode()
	{
		return state.hashCode();
		//what create my own hash function when a string encapsulates my unique state?
	}
	@Override
	public boolean equals(Object o)
	{
		if(o.getClass()==this.getClass())
		{
			return this.state.equals(((Node)o).state);
			//once again, my string has my state, so this is cool
		}
		return false;
	}
	
	//for uniform cost search
	@Override
	public int compareTo(Node o) 
	{
		return this.COST-o.COST;//gonna use costs in min-heap probably 
	}
	
	@Override
	public String toString()
	{
		return state;
	}
	
}

