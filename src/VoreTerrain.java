
import java.util.Random;

import mesh.MPolygon;
import processing.core.PApplet;


public class VoreTerrain extends PApplet
{
	
	float width = 1200;
	float height = 900;
	DualGraph graph;
	float timer = 6;
	int numOfNodes = 1200;
		
	public static void main(String[] args)
	{
		PApplet.main("VoreTerrain");
	}
	
	public void settings()
	{
		size((int)width, (int)height);
		
		
	}
	
	public void setup()
	{

		graph = new DualGraph(this, width, height, 2, numOfNodes);

		

	}
	
	
	public void draw()
	{
		stroke(255,0,0);
		float[][] myEdges = graph.trianglulation.getEdges();

		for(int i=0; i<myEdges.length; i++)
		{
		  float startX = myEdges[i][0];
		  float startY = myEdges[i][1];
		  float endX = myEdges[i][2];
		  float endY = myEdges[i][3];
		  line( startX, startY, endX, endY );
		}
		
		if(timer > 5)
		{
			fill(0);
			rect(0f, 0f, width, height);
			MPolygon[] regions = graph.graph.getRegions();
	
			Random rand = new Random();
			int x = rand.nextInt(numOfNodes);
			int[] links = graph.trianglulation.getLinked(x);
			for(int i = 0; i < links.length; i++)
			{
				
				fill(0,255,0);
				regions[links[i]].draw(this);
				System.out.print(links[i] + " ");
			}
			System.out.println(" <- x=" + x);
			timer = 0;
		}
		timer += .05;
		
	}
		
	
			
	
}
