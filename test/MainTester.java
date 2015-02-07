package test;

import java.util.ArrayList;
import java.util.Scanner;

import javax.script.ScriptException;

import logic.Node;

public class MainTester {

	//I could have rolled with Junits, but tbh with all the recursion and stuff I'd
	//rather just do it "by hand", this is the class I will use to ensure myself it works, not a formal test class
	//I should probably delete it before handing it in but I won't
	public static void main(String[] args) throws ScriptException 
	{
/*
		int[] b = {2,1,3};
		Node n =new Node(populate(b));
		System.out.println("Round -1");
		System.out.println(n);
		for(int i = 0; i < 10; ++i)
		{
			System.out.println("Round "+i);
			Node[] ns = n.expand();
			if(ns.length==0)
			{
				System.out.println("out of nodes");
				break;
			}
			n=ns[0];//kind of going down in depth here
			printAll(ns);
		}
		*/
		System.out.println("Enter threee things");
		Scanner s = new Scanner(System.in);
		String x =s.nextLine();
		String y = s.nextLine();
		String z = s.nextLine();
		
		String max =max(x,y,z);
		String min = min(x,y,z);
		System.out.println("The min is: "+min);
		System.out.println("The max is: "+max);
	}
	private static ArrayList<Integer> populate(int[] b)
	{
		ArrayList<Integer> a = new ArrayList<Integer>();
		for (int i : b) 
		{
			a.add(i);
		}
		return a;
	}
	private static void printAll(Node[] nodes)
	{
		for(Node n :nodes )
		{
			System.out.println("\t"+n+"\t"+n.available);
		}
	}

	private static String max(String a, String b, String c)
	{
		
		if(a.compareTo(b) >=0 && a.compareTo(c) >=0)
		{//a is biggest
			return a;
		} 
		//a is not biggest
		else if( b.compareTo(c)>=0){
			return b;
		}else{
			return c;
		}
		
	}
	private static String min(String a, String b, String c)
	{
		if(a.compareTo(b) <=0 && a.compareTo(c) <=0)
		{
			return a;
		} else if( b.compareTo(c)<=0){
			return b;
		}else{
			return c;
		}
		
	}
}
