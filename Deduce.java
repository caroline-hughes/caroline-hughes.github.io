/* Caroline Hughes
Programming Assignment 1
Sample output: deduce("(B or C) and (not A)") gives output
A	B	C
----------
F	T	T
F	T	F
F	F	T
*/

import java.lang.*;

public class Deduce {

	//method that will be called in main method.
	//takes in a String predicate and returns a String of all the combinations of
	//T/F values for A, B, and C that make the predicate true.
	public static void deduce(String predicate) {

		System.out.println("A\tB\tC");
		System.out.println("------------------");

		//arrays holding all possible combinations of A,B, and C
		String[] arrayA = {"T","T","T","T","F","F","F","F"};
		String[] arrayB = {"T","F","T","F","T","F","T","F"};
		String[] arrayC = {"T","F","F","T","T","F","F","T"};

		//for each 8 combinations of T/F values for A, B, and C
		for(int q=0; q<8; q++) {

			String p = predicate;
			String newString = "";

			//rewrite the predicate so that the letters A, B, and C are
			//replaced with the correct "T" or "F" Strings depending on the iteration
			for(int i=0;i<p.length();i++)
			{
				if(p.charAt(i) == 'A')
					newString=newString + arrayA[q];
				else if(p.charAt(i) == 'B')
					newString=newString + arrayB[q];
				else if(p.charAt(i) == 'C')
					newString=newString + arrayC[q];
				else
					newString=newString+p.charAt(i);
			}

			//pass this rewritten predicate into the recursive method.
			//if returns true, then print out to the table the T/F values of
			//A, B, and C which made the predicate true
			if (seeIfWorks(newString))
				System.out.println(arrayA[q] + "\t" + arrayB[q] + "\t" + arrayC[q]);
		}
		System.out.println(""); //To separate tables when there are multiple
	}


	//RECURSIVE METHOD: Calls recursive method until there are no parentheses
	//left in the String and the predicate can be evaluated as T or F for the boolean values
	//of A,B, and C used.
	public static boolean seeIfWorks(String str) {

		String s=str;

		//BASE CASE: if no parentheses left in the String, reduce down to "true" or
		//"false" using helper methods and return.
		if(!s.contains("(")) {

			String output="";
			if(s.contains("and not"))
				output = andNotMethod(s);
			else if(s.contains("or not"))
				output = orNotMethod(s);
			else if(s.contains("not"))
				output = notMethod(s);
			else if(s.contains("and"))
				output = andMethod(s);
			else if(s.contains("or"))
				output = orMethod(s);
			else
				output = s;

			if(output.equals("T"))
				return true;
			else
				return false;
		}


		/*RECURSIVE CASE: if there are still parentheses left in String,
		ind the start and end of the inner most parentheses. Everything
		inside the parentheses will be the String thePart. This will be passed into
		the helper methods, which will give the correct replacement for thePart.
		Then we will make the recursive call on a new String which is everything
		up to the innermost parentheses, the replacement, and everything after the
		innermost parentheses.
		*/
		else {
			int start=0;
			int end=0;
			//find start of innermost parentheses
			for(int i=0; i<s.length(); i++) {
				if(s.charAt(i) == '(')
				start = i; }
				//find end of innermost parentheses
				int j=start+1;
				while(s.charAt(j) != ')')
				j++;
				end = j;

			String thePart = s.substring(start+1, end); //the expression within the parentheses

			//identify which operater... Call corresponding helper method.
			if(thePart.contains("and not"))
				return seeIfWorks(s.substring(0,start) + andNotMethod(thePart) + s.substring(end+1));

			else if(thePart.contains("or not"))
				return seeIfWorks(s.substring(0,start) + orNotMethod(thePart) + s.substring(end+1));

			else if(thePart.contains("not")){
				if(thePart.length() == 5)  //ex: case "(T or F) or (not T)"
					return seeIfWorks(s.substring(0,start) + notMethod(thePart) + s.substring(end+1));
				else 	//ex: case "not (not T or T)"
					return seeIfWorks(s.substring(0,start+1) + notMethod(thePart) + s.substring(start+5)); }

			else if(thePart.contains("and"))
				return seeIfWorks(s.substring(0,start) + andMethod(thePart) + s.substring(end+1));

			else
				return seeIfWorks(s.substring(0,start) + orMethod(thePart) + s.substring(end+1));

			}
	}


		//HELPER METHODS: evaluate given T/F values and the certain
		//operator to give correct T or F String back to seeIfWorks.
		public static String andMethod(String thePart) {
			if(thePart.equals("T and T"))
				return("T");
			else
				return("F");}


		public static String orMethod(String thePart) {
			if(thePart.equals("T or T") || thePart.equals("T or F") || thePart.equals("F or T"))
				return("T");
			else
				return("F");}


		public static String notMethod(String thePart) {
			if(thePart.equals("not F"))
				return("T");
			else
				return("F");}


		public static String andNotMethod(String thePart) {
			if(thePart.equals("T and not F"))
				return("T");
			else
				return("F");}


		public static String orNotMethod(String thePart) {
			if(thePart.equals("T or not T") || thePart.equals("T or not F") || thePart.equals("F or not F"))
				return("T");
			else
				return("F");}


	//MAIN METHOD: to test various predicates
	public static void main(String[] args) {
		deduce("B");
		deduce("A and B");
		deduce("(A or C) or B");
		deduce("A or not (A and B)");
		deduce("(not B) or C");
		deduce("not (not A or C)");
		deduce("not (B or C)");
		deduce("(B or C) and (not A)");
		deduce("((A and B) or not C) and (C or B)");
	}
}
