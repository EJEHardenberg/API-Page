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
	float xleft   =  -Float.MAX_VALUE;
	float xright  =   Float.MAX_VALUE;
	float ybottom =  -Float.MAX_VALUE;
	float ytop    =   Float.MAX_VALUE;

	/**
	*Creates a region containing the entire coordinate plane
	*/
	public Region(){
	}

	/**
	*Creates a Region definied by the four locations of its sides
	*/
	public Region(float left, float right, float top, float bottom){
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



}