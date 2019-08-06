
import java.util.Random;

import mesh.MPolygon;
import processing.core.PApplet;


public class VoreTerrain extends PApplet
{
	
	float width = 1200;
	float height = 900;
	DualGraph graph;
	float timer = 6;
	int numOfNodes = 6000;
		
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
		strokeWeight(0);
		
		float sharpness = .4f;
		
		graph.addIsland(.95f, sharpness, 1);
		for(int  i = 0; i < 9; i++)
		{
			graph.addIsland(.8f, .2f, (float) Math.random()/2 + .3f);
		}
		


		graph.renderHeightMap();
		

	}
	
	

		
	public void mouseClicked()
	{
		int index = graph.findIndex(mouseX, mouseY)
	}
			
	
}
