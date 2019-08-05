import processing.core.PApplet;


public class VoreTerrain extends PApplet
{
	
	int width = 1200;
	int height = 900;
	DualGraph graph;
		
	public static void main(String[] args)
	{
		PApplet.main("VoreTerrain");
	}
	
	public void settings()
	{
		size(width, height, P3D);
		
		
	}
	
	public void setup()
	{
		graph = new DualGraph(this, width, height, 3);
		
		
		graph.drawBasic();
		
		
		
	}
	
	
	public void draw()
	{
		
		
	}
		
	
			
	
}
