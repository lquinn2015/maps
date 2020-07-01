
import java.sql.Time;

import processing.core.PApplet;


public class VoreTerrain extends PApplet
{
	
	float width = 1200;
	float height = 900;
	DualGraph graph;
	float timer = 6;
	int numOfNodes = 12000;

	float persistence = .95f;
	float sharpness = .4f;
	float sH = 1;
		
	public static void main(String[] args)
	{
		PApplet.main("VoreTerrain");
	}
	
	public void settings()
	{
		size((int)width, (int)height, P3D);
		
		
	}
	
	public void setup()
	{
	
	
		graph = new DualGraph(this, width, height, 2, numOfNodes);

//		strokeWeight(0);
//		
//		float sharpness = .4f;
//		
//		graph.addIsland(.95f, sharpness, 1);
//		for(int  i = 0; i < 9; i++)
//		{
//			graph.addIsland(.92f, (float) (.2f + Math.random()/5), (float) Math.random()/2 + .5f);
//		}
		
		OctaveMountains heightModule = new OctaveMountains(graph, this);
		heightModule.genHeightMap(.5f, 6, 500);

	}
	
	public void draw()
	{
		graph.renderHeightMap();
		
	}

		
	public void mouseClicked()
	{
		int index = graph.findIndex(mouseX, mouseY);
		
		graph.addIsland((float) (.8f + Math.random()/5.5f - .2) , .2f, (float) Math.random()/2 + .5f, index);
		
	}
		
	public void keyPressed()
	{
		if(key == 'a')
		{
			graph.heightmap = new float[numOfNodes];
			graph.addIsland(.95f, sharpness, 1);
			for(int  i = 0; i < 9; i++)
			{
				graph.addIsland(.97f, (float) (.2f + Math.random()/5), (float) Math.random()/2 + .5f);
			}
		}
		if(key == 's')
		{
			OctaveMountains heightModule = new OctaveMountains(graph, this);
			heightModule.genHeightMap(.5f, 6, (float)Math.random() * 1000f);
		}
	}
	
}
