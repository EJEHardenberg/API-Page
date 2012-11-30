package danger_zone;
import java.io.*;

/**
*@author Ethan Eldridge <ejayeldridge @ gmail.com>
*@version 0.1
*@since 2012-11-29
*
* A region in space defined by two points (x,y) and (w,z)
*/
public class Region{
	double xleft   =  -Float.MAX_VALUE;
	double xright  =   Float.MAX_VALUE;
	double ybottom =  -Float.MAX_VALUE;
	double ytop    =   Float.MAX_VALUE;

	/**
	*Creates a region containing the entire coordinate plane
	*/
	public Region(){
	}

	/**
	*Creates a Region definied by the four locations of its sides
	*/
	public Region(double left, double right, double top, double bottom){
		//Assume stupidity of the users
		if(left < right){
			xleft = left;
			xright = right;
		}else{
			xleft = right;
			xright = left;
		}
		if(bottom < top){
			ybottom = bottom;
			ytop = top;
		}else{
			ybottom = top;
			ytop = bottom;
		}
	}

	/**
	*Checks to see if this Region contains the DangerNode
	*@return true if the node is completely contained within this Region.
	*/
	public boolean fullyContains(DangerNode node){
		//Latitude is like y, longitude like x
		float y =node.getLatitude();
		float x = node.getLongitude();
		return ybottom <= y && y <= ytop && xleft <= x && x <= xright;
	}

	/**
	*Checks to see if this Region is to the left of the node (or intersecting it partially)
	*@return true if this Region is partially contained or to the left of the node.
	*/
	public boolean toTheLeftOf(DangerNode node){
		float x = node.getLongitude();
		return x >= xleft || x >= xright;
	}

	/**
	*Checks to see if this Region is to the right of the node (or intersecting it partially)
	*@return true if this Region is partially contained or to the right of the node.
	*/
	public boolean toTheRightOf(DangerNode node){
		float x = node.getLongitude();
		return x <= xleft || x <= xright;
	}

	/**
	*Checks to see if this region is above the node
	*@return true if any part of this region is above the node
	*/
	public boolean above(DangerNode node){
		float y = node.getLatitude();
		return y <= ybottom || y <= ytop;
	}

	/**
	*Checks to see if this region is below the node
	*@return true if any part of this region is below the node
	*/
	public boolean below(DangerNode node){
		float y = node.getLatitude();
		return y >= ytop || y >= ybottom;
	}

	/**
	*Unit Test of the Class Region
	*@param args The command line arguments (none for this class) to the Unit Test
	*/
	public static void main(String[] args) {
		//Entire coordinate space (all things should intersect and be contained in)
		Region space = new Region(); 
		//A sub space that will be to the left of our node and below
		Region leftSpace = new Region(-1.0,0.0,0.0,-2.0);
		//A sub space that will be to the right of the node and above
		Region rightSpace = new Region(1.5,3.0,5.0,2.0);

		DangerNode node = new DangerNode(1,1,-1);

		System.out.println("Contained in space: " + space.fullyContains(node));
		System.out.println("Not Contained in leftspace: " + !leftSpace.fullyContains(node));
		System.out.println("Not Contained in rightspace: " + !rightSpace.fullyContains(node));
		System.out.println("Left Space is to the left: " + leftSpace.toTheLeftOf(node));
		System.out.println("Left Space is beneath: " + leftSpace.below(node));
		System.out.println("RightSpace is to the right: " + rightSpace.toTheRightOf(node));
		System.out.println("RightSpace is above: " + rightSpace.above(node));
		System.out.println("Left Space is not above: " + !leftSpace.above(node));
		System.out.println("Right Space is not below: " + !rightSpace.below(node));

	}

}