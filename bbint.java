import java.io.*;
import java.util.HashMap;
import java.util.ArrayList; //used for storing file

public class bbint {

	public static void main(String[] args){
		//setup our system - gonna be emulating it in a sense
		ArrayList<String> lines = new ArrayList<String>(); //all the lines in the program so we can run through it
		HashMap<String, Integer> vars = new HashMap<String, Integer>(); //all the vars we may discover in our program
		int i = 0; //will be the PC of sorts
		int sp = 1; //need to keep track of where our loops are, also useful for subroutines later
		int[] istack = new int[16]; //16 is an arbitrary number but I can't imagine exceeding it
		String[] vstack = new String[16]; //stack for last vars for while loops
		int depth = 1; //files have a 3-space indentation
		String lastvar = ""; //need to know which var in loop

		System.out.println("Input file name (must be in same directory as program, *.txt): ");
		//bring the file into the arraylist
		try (BufferedReader reader = new BufferedReader(new FileReader(System.console().readLine()+".txt"))) {
    		while (reader.ready()) {
        		lines.add(reader.readLine());
    		}
		}
		catch (IOException e) {
			System.out.println("Error reading file.");
		}
		
		//begin execution
		while(i <= lines.size()-2){
			String[] ins = lines.get(i).substring(0, lines.get(i).length()-1).split(" "); //instruction
			String[] nextins = lines.get(i+1).substring(0, lines.get(i+1).length()-1).split(" "); //next ins, used to check exit conditions in loop
			int varpos = 1+3*(depth-1);
			for(String part : ins){
				System.out.print(part + " ");
			}
			System.out.println("");
			//perform instruction, located at any multiple of 3 in the instruction, depending on depth
			switch(ins[0+3*(depth-1)]){
				case "clear": //sets var to 0
					vars.put(ins[varpos], 0);
					System.out.println(ins[varpos] + ": " + vars.get(ins[varpos]));
					break;
				case "incr": //increments var by 1
					vars.put(ins[varpos], vars.get(ins[varpos])+1);
					System.out.println(ins[varpos] + ": " + vars.get(ins[varpos]));
					break;
				case "decr": //decrements var by 1
					vars.put(ins[varpos], vars.get(ins[varpos])-1);
					System.out.println(ins[varpos] + ": " + vars.get(ins[varpos]));
					break;
				case "while": //while loop
					if(vars.get(ins[varpos]) != 0){
						depth++;
						lastvar = ins[varpos];
						istack[sp] = i;
						vstack[sp] = lastvar;
						sp++;
						break;
					}
			}
			//make sure we can escape out while loop
			if(nextins[Math.max(0, 3*(depth-2))].equals("end")){
				if (vars.get(lastvar) == 0){
					sp--;
					depth--;
					lastvar = vstack[sp-1];
				}
				else{
					i = istack[sp-1];
					lastvar = vstack[sp-1];
				}
			}

			i++;
			System.out.println("^^^^^ State: i = " + i + ", ins[0] = " + ins[0] + ", ins[1] = " + ins[1] + ", depth = " + depth + ", lastvar = (" + lastvar + ": " + vars.get(lastvar) + "), sp = " + sp + ", istack[sp-1] = " + istack[sp-1] + ", nextins[0] = " + nextins[0] + ".\n");
		}
	}
}

/*

This solution was a whole lotta jank but there was some logic behind it I'll explain:
I drew some inspiration from my experience with emulators (and for the record NESpep is not up to date on github lol):
the usage of stacks and pointers to handle loops and the line by line instruction handling
there used to be one stack up until about 10 minutes ago where i had to split into two since i realised i need to
keep track of which variable and the line it was on for going back to other loops from nested loops.
There's definitely smart ways of keeping it at one, but I don't really feel like I have to go back and change all that
for it.

the depth variable keeps track of what level of indentation we're in since it does (pointlessly) use it. I guess I
could strip it of all indentation and only use the whiles but its done now; it's Sunday afternoon so pretty close to
the deadline so I'm not gonna go back and change it.

Everything is printed out at the end in the "State" line which is my little debug tool. It's not even comprehensive,
but it got me the job done and I don't fancy making it even longer to get a complete picture. Again, it's inspired by
my emulators, which are all pretty shoddily coded. Depth is probably 1 greater than it needs to be at all times but
I had some problems with it starting at 0 and I didn't when it started at 1 and I may not have those issues anymore
from 0 but I'm keeping it like that.

Anyway, it just runs through the code until it finds a loop. Then, it'll plonk the line number of the loop into the
istack, and the variable the loop conditions with onto the vstack, shift the sp up, and the depth as we're on a further
indentation. It keeps going until it finds an 'end' where it will check the loop's variable against 0. If it matches,
the sp moves down (effectively clearing the stack @ sp) and decrease our depth. Also update lastvar as that's for any 
other loops if they exist (ie in nested conditions). Pretty sure this variable doesn't need to exist but oh well.

*/