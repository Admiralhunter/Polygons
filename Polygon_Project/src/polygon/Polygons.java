package polygon;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FilenameFilter;
import java.awt.geom.*;


//Assumptions:
//Text files data points are integers and can fit within an integer datatype.
//The data will be in .txt files that have 4 data points per line.

//outputs:
//If program doesn't terminate due to bad data then each Polygon will get two outputs.
//First the program will state whether the polygon is simple (a closed non-intersecting polygon) or not.
//Secondly it will output the area of only the simple polygons.


public class Polygons {
	private int x[];
	private int y[];
	private boolean closed;
	private double area;

	public Polygons(int x[], int y[]) {
		this.area = 0.0;
		this.closed = true;
		this.x = x;
		this.y = y;
		
		//checks to ensure that start and end point match.
		//only useful for small N's. Doesn't scale well with large polygons I assume.

		if(x[0] == x[x.length - 1] && y[0] == y[y.length - 1]) {
			for(int i = 1; i < x.length - 1; i += 2) {
				if(x[i] != x[i + 1] || y[i] != y[i + 1]) {
					this.closed = false;
					break;
				}
			} 
		} else {
			this.closed = false;
		}
		
		//If closed has already been changed to false then the polygon is not a closed polygon
		//and so no line interception need to be checked for.
		if(this.closed) {
			this.closed = interception(x,y);
		}
		
		if(this.closed) {
			this.area = polyarea(x,y);
		}

	}
	
	public double getArea() {
		return area;
	}
	
	public boolean isClosed() {
		return closed;
	}
	
	public int[] getX() {
		return x.clone();
	}
	
	public int[] getY() {
		return y.clone();
	}

	
	//Checks every line segment against all the others to determine if any intersect. Utilizes a built in method via java.
	//Stops checking once a single interception occurs.
	// If speed is a concern then consider implementing the Shamos-Hoey Algorithm. Should run in O(n*logn).
	static boolean interception(int[] x, int[] y) {
		
		for(int i = 0; i < x.length; i += 2) {
		
			for( int j = i +3; j< x.length - 3; j += 2) {
				
				boolean interception = Line2D.linesIntersect(x[i],y[i],x[i +2], y[i+2],x[j],y[j],x[j+2], y[j+2]);
				
				if(interception) {
					return false;
				}
			}
		}

		return true;
	}
	
	//Calculates the area of a closed polygon.
	//Source: •	https://www.seas.upenn.edu/~sys502/extra_materials/Polygon%20Area%20and%20Centroid.pdf
	static double polyarea(int[] x, int[] y) {
		
		double area = 0.0;
		int j = x.length - 1;
		
		for(int i = 0; i< x.length; i++) {
			area = (double) area + ((x[j] + x[i]) * (y[j] - y[i]));

			j = i;
		}
		
		//Depending on order of vertices can return a negative area, so just take the absolute value.
		return Math.abs(area/2.0);
		
	}
	
	
	
	//Scans text file to determine how many lines there are.
	static int totallines(String file){

		int lines = 0;
		@SuppressWarnings("unused")
		String nextline = "";

		try {
			Scanner scanner = new Scanner(new File(file));


			while (scanner.hasNextLine()) {
				lines++;
				nextline = scanner.nextLine();
			}
			scanner.close();

			return lines;

		} catch (FileNotFoundException e) {
			//if no lines detected, exit program and alert user.
			e.printStackTrace();
			System.out.print("The text file does not contain data.");
			System.exit(0);
			return -1;
		}

	}
	
	
	//Function to output the data of each polygon once all computations have completed.
	static void polyoutput(Polygons polygon, int a) {
		String area;
		String closed;
		
		
		if(polygon.closed){
			closed = "Polygon " + (a + 1) +" is a simple polygon.";
			area = "Polygon " + (a +1) + " has an area of: " + polygon.area + " units.";
			System.out.println(closed);
			System.out.println(area);
			System.out.println("");
			
		}
		else {
			closed = "Polygon " + (a + 1) +" is not a simple polygon.";
			area = "Did not calculate the area of polygon " + (a + 1) +".";
			System.out.println(closed);
			System.out.println(area);
			System.out.println("");
		}
	}

	
	//Finds all .txt files contained in a directory.
	static File[] finder( String dirName) {
		File dir = new File(dirName);

		return dir.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String filename)
			{ return filename.endsWith(".txt"); }
		} );
	}
	


	public static void main(String[] args) {

		//gets location of folder for .txt files
		Scanner input = new Scanner(System.in);
		System.out.println("Copy folder location to load the .txt files");
		System.out.println("");
		String folder = input.nextLine(); 
		
		//can hard code location for easier running multiple times.
		//String folder = "C:\\Users\\Hunter\\eclipse-workspace\\Polygon_Project\\src\\polygon";
		input.close();


		File files[] = finder(folder);
		
		if(files.length == 0) {
			System.out.println("There are no files to load data. Terminating program.");
			System.exit(0);
		}


		Polygons[] polygons = new Polygons[files.length];
		
		//creates an object to draw polygons

		for(int a = 0; a < files.length; a++) {
			
			String file = files[a].toString();
			int lines = totallines(file);

			int xi = 0;
			int yi = 0;
			
			int x[] = new int[lines*2];
			int y[] = new int[lines*2];
			
			

			try {
				Scanner scanner = new Scanner(new File(file));
				while (scanner.hasNextLine()) {
					//Scans text file for each line. Replaces all white spaces and then splits string into a string array via delimiter of the comma.
					String textline = scanner.nextLine();
					textline = textline.replaceAll("\\s+","");
					String position[] = textline.split(",");



					//checks to ensure there are 4 points for each line to ensure a line segment is valid. Also checks to see if there are atleast 3 points
					if(position.length != 4 || lines < 2) {
						System.out.print("The data inputted is not valid. A line in the text file does not have precisely 4 data points. Program will terminate.");
						System.exit(0);
					}

					for(int i=0;i<position.length;i++)
					{

						//stores the x and y values in two separate arrays.
						if(i == 0 || i == 2) {
							try {
								x[xi] = Integer.parseInt(position[i]);
							} catch(NumberFormatException e) {
								System.out.println("One of the data points is not an integer. Program will terminate.");
								System.exit(0);
							}
							xi++;
						}
						else {
							try {
								y[yi] = Integer.parseInt(position[i]);
							} catch(NumberFormatException e) {
								System.out.println("One of the data points is not an integer. Program will terminate.");
								System.exit(0);
							}
							yi++;
						}
					}
				}
				scanner.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();

			}
			
			//inserts all the points into a polygon object and stored in an array.
			polygons[a] = new Polygons(x,y);
			

			polygon.Draw_Polygon.frame(a, polygons[a]);
			Polygons.polyoutput(polygons[a], a);

		}

	}

}

