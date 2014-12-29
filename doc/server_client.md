
Server-Client Architecture
=====================

Linetic communicates with its clients through the classes `Server` and `Client`.


 - **`Server`** : is located in the Linetic project in the package `ch.linetic.connexion` 
 ```java
	public final class Server {
		//...
		private static final int listeningPort = 32000;
		private static final int broadcastPort = 12000;
		//...

		/* Send the result of the analysis of an Analyzer to the clients that are connected */
		public void send(String analyzer, float result) {
			//...
		}

		/* Read the message from the clients
		Used for connecting and disconnecting clients */
		public void read(OscMessage m) {
			//...
		}
 ```

  - The `send` function takes the slug of the analyzer and its result as arguments and notify all the clients
  - The `read` function analysis the messages received by the clients (connect/disconnect patterns)
 
 - **`Client`** : is part of every client in the Processing project, in the file `ClientClass.pde`.
 ```java
 public class Client {
		//...
	    int listeningPort = 12000;
	    int serverPort = 32000;
	    String serverAddress = "127.0.0.1";
	
	    //...
	
	    public void login() { /* ... */ }
	    public void logout() { /* ... */ }
	    public void read(OscMessage m) { /* ... */ }
	
	    public void handleAnalyzer(String name, float value) {
	        if(name.equals("speed")) {
	        println("Speed : " + value);
		        // do something with the value of SpeedAnalyzer
	        }
	        if(name.equals("my_analyzer")) {
	        println("My Analyzer : " + value);
		        // do something with the value of MyAnalyzer
	        }
	        // do something every time we receive a value (no matter the Analyzer)
	    }
	}

 ```
 
  - The `login` and `logout` functions establish the connexion/disconnection to the server 
  - The `read` function analysis the messages received by the server and call the function `handleAnalyzer` if the pattern of the message correspond to a `String` and a `float`.
  - The `handleAnalyzer` function takes the slug of the `Analyzer` and its result as arguments and do something with the data.


Ports and Server IP address
---------------------------------

As you can see in the classes `Server` and `Client`, two ports are defined :

 - **The port that the server listens to** (for connexion/disconnection messages). It needs to be the same port for `Server.listeningPort` and `Client.serverPort`, here 32000.
 - **The port that the clients listen to** (for analyzer results messages). It needs to be the same port for `Server.broadcastPort` and `Client.listeningPort`, here 12000.

Finally all the clients need to be aware of the server IP address. The server IP address is kept in the variable `serverAddress` of the `Client` class. Leave the value `"127.0.0.1"` if the server and the client are in the same machine.

*Note that the server doesn't need to know anything about the clients as it's the client itself that sends its IP address during the connexion protocol.*