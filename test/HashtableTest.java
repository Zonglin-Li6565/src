package test;

import java.util.Hashtable;

public class HashtableTest {

	public static void main(String[] args){
		Hashtable<String, String> tableTest = new Hashtable<String, String>();
		tableTest.put("hello$1", "hello world");
		tableTest.put("hello$2", "good morning");
		System.out.println(tableTest.get("hello"));
	}
}
