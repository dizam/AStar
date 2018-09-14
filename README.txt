Java program that computes an optimal path from a start location to an end location in a two dimensional grid with varying heights at each tiles as costs. 
The program uses A* to calculate an optimal path with a small search space, making it highly efficient. There are two different heuristic function programs: a heuristic that relies on division called AStarDiv.java and a heuristic that relies on an exponential function called AStarExp.java.

The following contains instructions on building and running the program:
First of all, the program relies on Xhost to display a graphic image of the grid, the path chosen, and the nodes searched. To do this through PuTTy, make sure X11 forwarding is toggled in PuTTy and have Xming downloaded. Then, simply run Xming when you connect to the remote computer through PuTTy and run the program.

To compile the source: javac -Xlint *.java
To run a randomly generated map: java Main AStarDiv

To run a specific map generated from DEM2XYZ: java -Xmx1024M Main -load MTAFT.XYZ AStarDiv

To run with multiple programs and compare:
java -Xmx1024M Main -load MTAFT.XYZ AStarDiv AStarExp

To run with a specific seed:
java Main AStarDiv -seed 1

The Div_Efficient.txt and POW_Efficient.txt contain sample results with different maps. 
