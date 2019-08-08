
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;


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
	
//		graph = new DualGraph(this, width, height, 2, numOfNodes);
//		strokeWeight(0);
//		
//		float sharpness = .4f;
//		
//		graph.addIsland(.95f, sharpness, 1);
//		for(int  i = 0; i < 9; i++)
//		{
//			graph.addIsland(.92f, (float) (.2f + Math.random()/5), (float) Math.random()/2 + .5f);
//		}
//		

	}
	
	public void draw()
	{
//		graph.renderCoastMap();
		PImage hatching =  loadImage("resources/hatching.png");

		noFill();
		noStroke();
		beginShape();
		texture(hatching);
		vertex(0,0, 0, 0);		
		vertex(0,700, 640,0);
		vertex(700,700, 640 , 480);
		vertex(700,0 , 0, 480);
		endShape(PApplet.CLOSE);
		
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
	}
	
}
