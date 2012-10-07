package danger_zone;
import java.util.HashMap;


/**
*@author Ethan Eldridge <ejayeldridge @ gmail.com>
*@version 0.0
*@since 2012-10-7
*
* The CommandParser is a static class that contains the constants CMD_LON and CMD_LAT which correspond to the
*format of the geo command to be interpreted. 
*/
public class CommandParser{
	//Test function
	static final String CMD_LON = "LON";
	static final String CMD_LAT = "LAT";

	public static void main(String argv[]){
		String cmd = "LON 91.12 LAT 40.78";
		float[] result = CommandParser.parseGeoCommand(cmd);
		System.out.println(result[0] + "\n" + result[1]);
		//IF they are really floats then they'll add
		System.out.println(result[0] + result[1]);
	}

	/**
	*If the command string is of the form LON XXX.XXXX LAT XXX.XXXX then this function will remove the longitude and latitude from the string and return them in a float array. The returning array will have longitude first in the array, and latitude second. 
	*/
	public static float[] parseGeoCommand(String command){
		//split the command by its spaces and look at the parts to find longitude and latitude
		String[] parts;
		parts = command.split(" ");
		//Associativity is a nice thing
		HashMap<String,Float> cmds = new HashMap<String,Float>(parts.length);
		for(int i=0; i < parts.length; i++){
			if(parts[i].equals(CMD_LON)){
				cmds.put(CMD_LON,Float.parseFloat(parts[i+1]));
				continue;
			}else if(parts[i].equals(CMD_LAT)){
				cmds.put(CMD_LAT,Float.parseFloat(parts[i+1]));
				continue;
			}
		}
		//Now we 'hopefully' have what we'd like, in which case we should return it
		float [] lonlatTuple = new float[2];
		//cmds.get will return null if key does not exist
		lonlatTuple[0] = cmds.get(CMD_LON);
		lonlatTuple[1] = cmds.get(CMD_LAT);
		
		return lonlatTuple;

	}
}