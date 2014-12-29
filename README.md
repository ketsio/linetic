linetic
=======
Gesture analysis for a prototype of music consciousness environments

###How to start

For starting Linetic :

 - Add the following libraries available in [/Linetic/lib][Linetic Lib] :
	 - oscP5 : for Server-Client communication
	 - controlP5 : for GUI features
	 - core : Processing library
	 - SimpleOpenNI : Kinect library for Processing, [read wiki][Wiki SimpleOpenNI] for installation
 - Make sure your kinect is connected
 - Right click on the class `ch.linetic.gui.Linetic` > Run As > Java Applet

For starting a client : 

 - Choose a client from [/clients][Clients Folder]
 - Open the client in Processing (double click on a `.pde` file)
 - Run the client on Processing (Sketch > Run)

###How it works

**This project uses the Kinect for analyzing the movement using so called `Analyzer`s.**
Each `Analyzer` analyzes the movement of the user in its own way. The behavior of the analyzer is located in a function that takes a `Movement` as a parameter and return a `float` as the result of the analysis.
[Read this tutorial to create your own Analyzer][Create Analyzer].

**Linetic is a server that communicates with clients.**
The result of each Analyzer is automatically sent to all the clients that are connected to the server. Therefore you can have as many clients as you want that will use the result of the `Analyzer`s in their own ways.
[Read more about the Server-Clients architecture][Server/Client].

Of course, you can easily create your own client and use the result of the movement analysis the way you want.
[Read this tutorial to create your own Client][Create Client].

[Linetic Lib]: /Linetic/lib
[Wiki SimpleOpenNI]: https://code.google.com/p/simple-openni/wiki/IDE "Importing SimpleOpenNi"
[Clients Folder]: /clients

[Create Analyzer]: /doc/create_analyzer.md "Create your own Analyzer" 
[Server/Client]: /doc/server_client.md "Server-Client Architecture"
[Create Client]: /doc/create_client.md "Create your own Client" 
