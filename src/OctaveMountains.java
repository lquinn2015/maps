
import processing.core.*;

public class OctaveMountains 
{
	DualGraph graph;
	PApplet parent;
	
	public OctaveMountains(DualGraph incoming, PApplet app)
	{
		graph = incoming;
		this.parent = app; 
	}
	
	public void genHeightMap(float persistence, int octaves, float scale)
	{
		float octaveOffset[][] = new float[octaves][2];
		for(int i = 0; i < octaves; i++)
		{
			octaveOffset[i][0] = (float)Math.random() * 2000;
			octaveOffset[i][1] = (float)Math.random() * 2000;
		}
		
		float[][] points = graph.sitePoints;
		
		
		float width = (float) parent.width;
		float height = (float) parent.height;
		
		float high = Float.MIN_VALUE;
		float low = Float.MAX_VALUE;
		
		
		for(int i = 0; i < points.length; i++)
		{
			float x = points[i][0];
			float y = points[i][1];
			
			float freq = 1;
			float amplitude = 1;
			float maxValue = 0;
			
			graph.heightmap[i] = 0;
			
			for(int k = 0; k < octaves; k++)
			{
				
				float sampleX = (x- width/2) / scale * freq + octaveOffset[k][0];
				float sampleY = (y - height/2) / scale * freq + octaveOffset[k][1];
				
				graph.heightmap[i] += parent.noise(sampleX,sampleY) * amplitude;
				maxValue += amplitude;
				
				amplitude *= persistence;
				freq *= 2;
			}
			graph.heightmap[i] /= maxValue; // normalize
			
			high = Math.max(high, graph.heightmap[i]);
			low = Math.min(low, graph.heightmap[i]); 
			
		}
		
		for(int i = 0; i < points.length; i++)
		{
			graph.heightmap[i] = (graph.heightmap[i] - low) / (high - low);
		}
		
		
	}
	
	
}
