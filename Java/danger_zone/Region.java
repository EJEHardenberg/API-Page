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
	float xleft   =  -float.MAX_VALUE;
	float xright  =   float.MAX_VALUE;
	float ybottom =  -float.MAX_VALUE;
	float ytop    =   float.MAX_VALUE;

	public Region(){
	}

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

}