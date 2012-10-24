package danger_zone;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.Stack;
import java.util.Map;
//http://code.google.com/p/json-simple/
import org.json.simple.JSONObject;



/**
*@author Ethan Eldridge <ejayeldridge @ gmail.com>
*@version 0.1
*@since 2012-10-5
*
* KD Tree / Listening Object Interface for the danger zone application.
* Uses UDP networking because it is expected that the sockets are used for interprocess communication and therefore
* loss won't be a problem.
*/
public class DangerControlUDP{
	/**
	*Socket to accept incoming queries to the Danger Control interface, listens on port 5480
	*/
	DatagramSocket clientListener = null;
	/**
	*Timeout for the DangerControl program's clientListener, this must be set in integer form (Seconds)
	*/
	int int_timeout = 5;
	/**
	*Timeout for the DangerControl program itself, this is used during debugging and will probably be removed in release implementations
	*/
	long long_timeout = System.currentTimeMillis() + 1000*int_timeout;
	/**
	*Socket that will hold the incoming traffic coming from the clientListener
	*/
	Socket incoming = null;
	/**
	*Data Structure to hold the dangerZones from the database. 
	*/
	DangerNode dangerZones = null;
	/**
	*Port number to communicate to the client with
	*/
	int port_number = 5480;
	/**
	*Packet recieved by server from the client
	*/
	DatagramPacket request = null;

	/**
	*The url that the output of the commands will be send to
	*/
	public static final String URL_TO_SEND_TO = "http://localhost/Server/Java/danger_zone/test.php";

	/**
	*Creates an instance of the DangerControl class.
	*/
	public DangerControlUDP() throws Exception{
		//5480 For Listening, 5481 to send back out
		clientListener = new DatagramSocket(port_number);
		//clientListener.setSoTimeout(int_timeout);
		//Construct the Tree to hold the danger zones (note this should be replaced by a tree building from sql function)
		this.createTree();
		clientListener.setReuseAddress(true);


	}

	/**
	*Creates and constructs the tree stored in dangerZones from the database
	*/
	public void createTree(){
		dangerZones = new DangerNode(9,9,1);
		dangerZones.addNode(new DangerNode(7,2,4));
		dangerZones.addNode(new DangerNode(12,12,5));
		dangerZones.addNode(new DangerNode(15,13,6));
		this.dangerZones = DangerNode.reBalanceTree(dangerZones);
	}

	/**
	*Run this instance of DangerControl
	*/
	public void run() throws Exception{
		//Fun Fact, Java supports labels. I didn't know Java liked Spaghetti
		Running:
		while(System.currentTimeMillis() < long_timeout){
			request = new DatagramPacket(new byte[1024], 1024);
			this.read(request);
			
		}
		//Cleanup
		clientListener.close();
	}

	/**
	*Readings incoming messages and calls the dispatcher to send responses
	*/
	public void read(DatagramPacket request) throws Exception{
		

		// Block until the host receives a UDP packet.
	    clientListener.receive(request);

		// Obtain references to the packet's array of bytes.
      	byte[] buf = request.getData();

      	// Wrap the bytes in a byte array input stream,
      	// so that you can read the data as a stream of bytes.
      	ByteArrayInputStream bais = new ByteArrayInputStream(buf);

      	// Wrap the byte array output stream in an input stream reader,
      	// so you can read the data as a stream of characters.
      	InputStreamReader isr = new InputStreamReader(bais);

      	// Wrap the input stream reader in a bufferred reader,
      	// so you can read the character data a line at a time.
      	// (A line is a sequence of chars terminated by any combination of \r and \n.) 
      	BufferedReader incomingStream = new BufferedReader(isr);

      	// The message data is contained in a single line, so read this line.
      	String line;
	
		//Loop through incoming message from udp
		while((line = incomingStream.readLine()) != null){
			System.out.println(line);
			//We should use some type of switch or something to figure out what function to call from the command parser
			if(line.indexOf(CommandParser.CMD_LON) != -1 && line.indexOf(CommandParser.CMD_LAT) != -1){
				//Handle the command and respond to it
				this.dispatchResponse(this.handleGeoCommand(line),request);
				//Force the stream to spit back to the client
			}
			//We can extend right here to implement more commands
		}
		
	}

	/**
	*Dispatches a response back to the client of the nearest neighbors to the point they asked for.
	*@param neighbors The nearest zones returned by the search for the tree
	*/
	public void dispatchResponse(Stack<DangerNode> neighbors,DatagramPacket responseStream) throws Exception{
		//Lets send the response as a json array of the nodes
		JSONObject response = new JSONObject();
		response.put("neighbors", neighbors);
		// Send reply.
	    InetAddress clientHost = request.getAddress();
	    int clientPort = request.getPort();
	    byte[] buf = response.toString().getBytes();
	    DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
	    clientListener.send(reply);
	}

	/**
	*Parses a command in the GEO COMMAND format, will return the results of searching the tree for the specified coordinate and number of near zones
	*@param geoCommand String command in the GEO COMMAND format;
	*@return returns the results of searching the tree for the coordinate.
	*/
	public Stack<DangerNode> handleGeoCommand(String geoCommand){
		float[] geoCmd = null;
		//Parse information from the message:
		geoCmd = CommandParser.parseGeoCommand(geoCommand);
		if(geoCmd != null){
			//We have recieved the Coordinates and should play with the tree
			//System.out.println("Searching tree for " + geoCmd[0] + " " + geoCmd[1]);
			if(dangerZones == null){
				System.out.println("No Tree Initailized");
				return null;
			}
			return dangerZones.nearestNeighbor(new float[]{geoCmd[0],geoCmd[1]},(int)geoCmd[2]);

		}
		return null;
}

	public static void main(String argv[]) throws Exception
	{
		DangerControl control = new DangerControl();		
		control.run();

	}

}