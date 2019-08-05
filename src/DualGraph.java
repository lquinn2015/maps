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
	
	public DualGraph(PApplet pa, float width, float height, int relaxtion , int numOfPoints)
	{
		Random rand = new Random();
		parent = pa;
		
		this.width = width;
		this.height = height;
		
		float[][] points = new float[numOfPoints][2];
		
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

}
