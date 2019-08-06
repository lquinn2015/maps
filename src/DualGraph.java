import java.awt.Color;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

import mesh.*;
import processing.core.PApplet;

public class DualGraph
{
	float[][] sitePoints;
	Voronoi graph;
	Delaunay trianglulation;
	PApplet parent;
	float width;
	float height;
	float[] heightmap;   // indexs relate to heights of polygons 
	float[] touched;
	int numOfNodes;
	
	
	// CONSTANTS
	float WATER_HEIGHT = .2f;
	
	
	//
	
	
	
	
	public DualGraph(PApplet pa, float width, float height, int relaxtion , int numOfPoints)
	{
		Random rand = new Random();
		parent = pa;
		
		this.width = width;
		this.height = height;
		numOfNodes = numOfPoints;
		
		
		float[][] points = new float[numOfPoints][2];
		heightmap = new float[numOfPoints];
		
		points[0][0] = -1000;
		points[0][1] = -1000;
		
		for(int i = 1; i < numOfPoints; i++)
		{
			points[i][0] = rand.nextFloat() * width;
			points[i][1] = rand.nextFloat() * height;	
		}
		
		for(int i = 0; i < relaxtion; i++)
		{
			points = centroidRelaxition(points);
		}
		
		sitePoints = points;
		graph = new Voronoi(points);
		trianglulation = new Delaunay(points);
		
		
	}
	
	public int[] getNeighbors(int regionID)
	{
		
		int[] links = trianglulation.getLinked(regionID);
		int nonzero = 0;
		for(int i = 0; i < links.length; i ++)
		{
			if(links[i] != 0)
			{
				nonzero++;
			}
		}
		
		int[] neigbors = new int[nonzero];
		for(int i = 0; i < neigbors.length; i ++)
		{
			if(links[i] != 0)
			{
				neigbors[i] = links[i];
			}
		}
		
		return neigbors;
		
		
	}
	
	public float[][] centroidRelaxition(float[][] pointsPre)
	{
		
		Voronoi partial = new Voronoi(pointsPre);
		MPolygon[] regions = partial.getRegions();
		
		float[][] points = new float[regions.length][2];
		for(int i = 1; i < regions.length; i++)
		{
			float[][] polygon = regions[i].getCoords();
			for(int x = 0; x < polygon.length; x++)
			{
				points[i][0] += Math.max(Math.min(polygon[x][0], width), 0 ); // Constrain site points to the screen
				points[i][1] += Math.max(Math.min(polygon[x][1], width), 0 );
			}
			points[i][0] /= polygon.length;
			points[i][1] /= polygon.length;
		}		
		points[0][0] = -1000;
		points[0][1] = -1000;
		
		return points;
	}
	
	public void drawBasic()
	{
		MPolygon[] regions = graph.getRegions();
		parent.fill(0,255,0);
		for(int i = 0; i < regions.length; i++)
		{
			regions[i].draw(parent);
		}
			
		float[][] mesh = trianglulation.getEdges();
		
		parent.stroke(255,0,0);
		for(int i = 0; i < mesh.length; i++)
		{
			parent.line(mesh[i][0], mesh[i][1], mesh[i][2], mesh[i][3]);
		}
		
	}
	
	public void renderHeightMap()
	{
		MPolygon[] regions = graph.getRegions();
		
		for(int i = 0; i < regions.length; i++)
		{
			RGBColor color = getColor(1, heightmap[i]);
			parent.fill(color.r, color.g, color.b);
			regions[i].draw(parent);			
		}
		
		
	}
	
	/** 
	 * Returns an RGB object based on a scale
	 * @param type
	 * @param scalar
	 * @return
	 */
	public RGBColor getColor(int type, float scalar)
	{
		RGBColor ret = new RGBColor(0, 0, 0);
		
		
		switch(type)
		{
			case(1):// heightmap
			{
				HSLColor hsl = new HSLColor(230-scalar * 230, 100, 54);
				Color c = hsl.getRGB();
				ret.setValue(c.getRed(),c.getGreen(),c.getBlue());
				return ret;
			}
		}
		
		return ret;
	}
	
	
	public void addIsland(float persistence, float sharpness, float startH)
	{
		Random rand = new Random();
		PriorityQueue<Integer> queue = new PriorityQueue<>();
		HashSet<Integer> visited = new HashSet<>();
		
		int curr = rand.nextInt(numOfNodes);
		heightmap[curr] = startH;// Math.min(rand.nextFloat() + .5f,1);
		visited.add(curr);
		queue.add(curr);
		
		while(!queue.isEmpty())
		{
			curr = queue.poll();
			int[] neighbors = getNeighbors(curr);
			
			for(int i = 0; i < neighbors.length; i++)
			{
				if(!visited.contains(neighbors[i]))
				{
					float base = heightmap[curr] * persistence;
					float sharp = (sharpness * rand.nextFloat() -.5f)/20;
					
					if(base + sharp < heightmap[curr])
					{
						heightmap[neighbors[i]] = base + sharp;	
					}
					
					heightmap[neighbors[i]] = Math.min(heightmap[neighbors[i]], 1);
					visited.add(neighbors[i]);
					
					if(heightmap[neighbors[i]] > .1f  && heightmap[neighbors[i]] < .3f)
					{
						heightmap[neighbors[i]] = .075f;
					}
					
					if(heightmap[neighbors[i]] > .01f)
					{
						queue.add(neighbors[i]);
					}
					
				}
			}
		}	
		
		
		
	}
	
		
	class RGBColor
	{
		public float r,g,b;
		
		public RGBColor(float r, float g, float b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public void setValue(float r, float g, float b)
		{
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}


	public int findIndex(float mouseX, float mouseY) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}
