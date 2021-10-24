# BBInt
BareBones language Interpreter

*Program commentary:*
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
