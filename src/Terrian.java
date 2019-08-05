import processing.core.PApplet;

public class Terrian extends PApplet 
{
	
	int cols,rows;
	int scl = 20;
	int w = 1200;
	int h = 900;
	float time = 0;
	
	float[][] terrain;
	
	
	public static void main(String[] args)
	{
		PApplet.main("Terrian");
	}
	
	public void settings()
	{
		size(1200, 900, P3D);
		cols = w/scl;
		rows = h/scl;
		
	}
	
	public void setup()
	{
		terrain = new float[cols][rows];
		
	}
	
	public void generateTerrain(float time2)
	{
		float yoff = time2;
		for(int y = 0; y < rows; y++)
		{
			float xoff = 0;
			for(int x =0; x < cols; x++)
			{		
				terrain[x][y] = map(noise(yoff,xoff), 0 ,1,-100,100);
				xoff += .2;
			}
			
			yoff += .2;
		}
		
	}
	
	public void draw()
	{
		background(0);
		stroke(255);
		noFill();
		
		translate(width/2, height/2);
		rotateX(PI/3);
		translate(-w/2, -h/2);
		
		time -= .025;
		generateTerrain(time);
		
		for(int y = 0; y < rows-1; y++)
		{
			beginShape(TRIANGLE_STRIP);	
			for(int x =0; x < cols; x++)
			{		
				vertex(x*scl, y*scl, terrain[x][y]);
				vertex(x*scl, (y+1)*scl, terrain[x][y+1]);
			}
			endShape();
		}
		
	}
	
	
}
