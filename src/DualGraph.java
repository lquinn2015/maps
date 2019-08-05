import java.util.Random;

import mesh.*;
import processing.core.PApplet;

public class DualGraph
{
	float[][] sitePoints;
	Voronoi graph;
	Delaunay trianglulation;
	PApplet parent;
	
	public DualGraph(PApplet pa, int width, int height, int relaxtion)
	{
		Random rand = new Random();
		parent = pa;
		int numOfPoints = width * height / 1000;
		float[][] points = new float[numOfPoints][2];
		
		for(int i = 0; i < numOfPoints; i++)
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
	
	public float[][] centroidRelaxition(float[][] pointsPre)
	{
		
		Voronoi partial = new Voronoi(pointsPre);
		MPolygon[] regions = partial.getRegions();
		
		float[][] points = new float[regions.length][2];
		for(int i = 0; i < regions.length; i++)
		{
			float[][] polygon = regions[i].getCoords();
			for(int x = 0; x < polygon.length; x++)
			{
				points[i][0] += polygon[x][0];
				points[i][1] += polygon[x][1];
			}
			points[i][0] /= polygon.length;
			points[i][1] /= polygon.length;
		}		
		
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
