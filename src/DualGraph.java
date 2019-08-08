import java.awt.Color;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

import mesh.*;
import processing.core.PApplet;
import processing.core.PImage;

public class DualGraph
{
	float[][] sitePoints;
	Voronoi graph;
	Delaunay trianglulation;
	PApplet parent;
	float width;
	float height;
	float[] heightmap;   // indexs relate to heights of polygons 
	float[] ram;
	String[] biomes;
	int numOfNodes;
	Random rand;	
	
	// CONSTANTS
	float WATER_HEIGHT = .3f;
	
	
	//
	
	
	
	
	public DualGraph(PApplet pa, float width, float height, int relaxtion , int numOfPoints)
	{
		rand = new Random();
		parent = pa;
		
		this.width = width;
		this.height = height;
		numOfNodes = numOfPoints;
		
		
		float[][] points = new float[numOfPoints][2];
		heightmap = new float[numOfPoints];
		ram = new float[numOfPoints];
		biomes = new String[numOfPoints];
		
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
	
	
	public void renderCoastMap()
	{
		MPolygon[] tiles = graph.getRegions();
		
		for(int i = 0; i < tiles.length; i++)
		{
			RGBColor b = getColor(1, heightmap[i]);
			parent.fill(b.r,b.g,b.b);
			if(biomes[i] != null && biomes[i].equals("Ocean-Coast")) // only cross hatch ocean tiles?
			{
				tiles[i].draw(parent);
			}
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
		addIsland(persistence, sharpness, startH, rand.nextInt(numOfNodes));
	}
	
	public void addIsland(float persistence, float sharpness, float startH, int epicenter)
	{
		PriorityQueue<Integer> queue = new PriorityQueue<>();
		HashSet<Integer> visited = new HashSet<>();
		
		int curr = epicenter;
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
					visited.add(neighbors[i]);
					
					float base = heightmap[curr] * persistence;
					float sharp = (sharpness * rand.nextFloat() -.5f)/20;
					
					if(heightmap[curr] - heightmap[neighbors[i]] > .3f)
					{
						heightmap[neighbors[i]] = base + sharp;	
						heightmap[neighbors[i]] = Math.min(heightmap[neighbors[i]], 1);
						
						if(heightmap[neighbors[i]] > .1f  && heightmap[neighbors[i]] < WATER_HEIGHT)
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
	}
	
	
	public void discoverSeas()
	{
		int members = 0;
		boolean visited[];
		do 
		{
			members = 0;
			visited= new boolean[numOfNodes];
			PriorityQueue<Integer> queue = new PriorityQueue<>();
			queue.add(rand.nextInt(numOfNodes));	// cost of picking land and ending is low
			
			while(!queue.isEmpty())
			{
				int curr = queue.poll();
				if(!visited[curr]) 
				{
					visited[curr] = true;
					if(heightmap[curr] < WATER_HEIGHT)
					{
						members++;
						int[] neighbors = getNeighbors(curr);
						for(int i = 0; i < neighbors.length; i++)
						{
							if(!visited[neighbors[i]])
							{
								queue.add(neighbors[i]);
							}
						}
						
					}
					else
					{
						biomes[curr] = "Coast";
					}	
				}
			}
		} while(members < 300); // oceans should be big
		
		for(int i = 0; i < numOfNodes; i++)
		{
			if(visited[i] && heightmap[i] <= WATER_HEIGHT )
			{
				int[] neighbors = getNeighbors(i);
				boolean hatch = false;
				for(int t = 0; t < neighbors.length; t++)
				{
					if(heightmap[neighbors[t]] > WATER_HEIGHT )
					{
						hatch = true;
						break;
					}
				}
				if(hatch)
				{
					biomes[i] = "Ocean-Coast";					
				}
				else
				{
					biomes[i] = "Ocean";
				}
				
			}
			else if(heightmap[i] <= WATER_HEIGHT)
			{
				biomes[i] = "Lake";
			}
			else if(!visited[i])
			{
				biomes[i] = "Land";
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
		float minDist = Float.MAX_VALUE;
		int winner = -1;
		
		for(int i = 0; i < numOfNodes; i++)
		{
			float curr = dist2d(mouseX, mouseY, sitePoints[i][0], sitePoints[i][1]);
			if(curr < minDist)
			{
				minDist = curr;
				winner = i;
			}
		}
		return winner;
	}
	
	public float dist2d(float a, float b, float c, float d)
	{
		return (float)Math.sqrt((a-c)*(a-c) + (b-d)*(b-d));
	}
	

}
