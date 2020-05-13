README.txt

JARED ADAMS
CS455 HW1
02/19/2020

INFORMATION FOR GRADER:

To run the program:

1. unpack the .tar file (you should have already done this) with this command: tar -xvf Jared-Adams-ASG1.tar
2. move into CS455HW1 directory and issue command: gradlew build (or gradle build if you have gradle is in your PATH)
3. move into the ./build/libs directory
4. run these commands to run Registry and MessagingNodes:

java -cp CS455HW1.jar cs455.overlay.node.Registry <port number>
java -cp CS455HW1.jar cs455.overlay.node.MessagingNode <registry host> <registry port number>
 
Please note the command line arguments as the program will fail without the proper arguments. 

Other Notes:

During one round of testing, the sent and received did not match up. This error could not be replicated. If a run gives incorrect output, please close and try one more time. 

Upon completion, the Registry will display output in a table format similar to format specified in the instructions. Please make sure the terminal window is wide enough to accommodate displaying all of the data. If the window is not wide enough, the output will appear jumbled. 

The program does have a delay included when the packets are being routed and the traffic summary is being automatically requested by the Registry. Out of an abundance of caution, this delay is set to 20 seconds. Please be patient as the data should arrive as expected after that time period. 

The Registry supports the following commands:

1. list-messaging-nodes: this will display a list of the nodes that are registered with information about the node including the Node ID, port number, hostname, and IP address. 

2. setup-overlay <number of routing table entries>: The setup-overlay command initiates creating routing tables and sending those routing tables to the nodes. The <number of routing table entries> command is optional and the system will default to 3. 

3. list-routing-tables: the provides information about each node and it's routing table. The information displayed includes: IP address, Node ID, port number, and a list of the connected nodes. 

4. start <number of messages to send>: start initiates the sending of packets between the nodes. The <number of messages to send> is required. 
 
5. exit: ends the program

The MessagingNode supports the following commands:

1. print-counters-and-diagnostics: This will display on the console the number of messages, sent, received, relayed, and the payload total for that individual node. 

2. exit-overlay: This will cause the node to send a deregistration request to the registry.

3. exit: ends the program



BRIEF DESCRIPTION OF REMAINING CLASSES

	RegistryHelper
	
	RegistryHelper holds methods and fields for the registry. It is used to make the Registry class simpler. The calls from Registry will utilize methods contained in the RegistryHelper. 
	
	MessagingNodeReceiver
	
	After a messaging node receives a connection, the server creates this class and executes it in a new thread. This class listens for new messages as long as the socket is not null. When a new message is received, the length is read, a byte array is created, the type of message is read, and then the byte array is passed to the EventFactory for futher processessing. 
	
	MessagingNodeServer
	
	When a messaging node is created, a server is also created on a new thread that waits for new connections. As connections are received, they are assigned to a socket and run on a new thread. 
	
	OverLayToRegisterListener
	
	This functions exactly the same as the MessagingNodeReceiver but is used specifically to listen for messages from the Registry. 
	
	TCPClientSocketSender
	
	This class is used by the registry to send messages to nodes in the overlay. 
	
	TCPRegisterReceiver
	
	This class functions exactly the same as the MessagingNodeReceiever but is used by the Registry. 
	
	RegistryServer
	
	The RegistryServer is created when the Registry is started and listens for incoming connections. When a connection is received, a socket is created and started on a new thread. 
	
	ConnectedNode
	
	This class hold information about the nodes that are connected to a messaging node. The information is used to send and relay messages through the overlay as needed. 
	
	CountersAndDiagnostics
	
	This is a class the holds data to keep track of the number of packets, sent, received, total sent, total received, and relayes. 
	
	CreateRoutingTable
	
	CreateRoutingTable creates all of the manifests and routing tables for all nodes in the overlay. Once created these can then be sent by the Registry to all of the nodes in the overlay. 
	
	Manifest
	
	This is the individual manifest/routing table that is sent to each node. 
	
	NodeID
	
	A wrapper class that holds the Node ID for a node so it can be passed to other classes. 
	
	NumberOfPackets
	
	A wrapper class that holds the numberOfPackets that will be sent by each node.
	
	PortNumber
	
	A wrapper class that holds the port number for a node. 
	
	RegisteredNode
	
	Similar to connectedNode, this holds information about each node that is registered with the Registry. The Registry uses this informatoin to send messages and data to nodes in the overlay. 
	
	RegisteredNodeCount
	
	A wrapper class that holds the number of nodes in the overlay. 
	
	RoutingTable
	
	This holds the information for a Routing Table used by the messaging ndoes. 
	
	RoutingTableSize
	
	A wrapper class that holds the number of nodes any one node is connected to. 
	
	TrackingData
	
	A class that holds atomic variables to keep track of the number of packets send, received, related, and summations. It is atomic to avoid race conditions in updating the data. 
	
	DeregistrationEventHandler
	
	When a deregistration request is received by the Registry, this class is used the process the request. If successful, is sends the apprioprate message, and if unsuccessful, it notifies the node of the failure. 
	
	DeregistrationResponseEventHandler
	
	Reads in the response from the registry and output the message to the screen. The socket is then closed. 
	
	EventFactory
	
	This is the beating heart of the messaging system. Once a message is received by any node, it is sent into the eventFactory for processing. The switch statement is used to call the approprate methods and classes to handle and/or respond to the message. 
	
	OverlayNodeSendsRegistration 
	RegistryReportRegistrationStatus
	OverlayNodeSendsDeregistration 
	RegistryReportDeregistrationStatus
	RegistrySendsNodeManifest 
	NodeReportsOverlaySetupStatus
	RegistryRequestTaskInitiate
	OverlayNodeSendsData
	OverlayNodeReportsTaskFinished
	RegistryRequestsTrafficSummary
	OverlayNodeReportsTrafficSummary
	
	Each of these classes corresponds to a particular type of message that is sent and/or received as outlined in the instructions. Each follows a similar pattern with minor differences to create or read a particular message. 
	
	OverlayNodeInitatesTaskEventHandler
	
	Once the node receives instructions from the Registry to start sending this message, this class is created and sends all of the packets with random payloads to random destinations within the overlay. 
	
	OverlayNodeReceivesDataEventHandler
	
	When a packet is received from another node this class processes the packet. If it has reached the final desination, the received count is incremented. Otherwise, the next point in the relay is found and the packet is passed along. 
	
	Protocols
	
	This class holds constants for all of the messaging types and few additional variables used in the relay system. 
	
	RegistrationEventHandler
	
	When a registration request is received, this class is created to process that request. It will check for errors, and if none are found it will register the node and send a success message. If an error is found, the failure message is sent to the registering node. 
	
	RegistrationResponseEventHandler
	
	When a node received the registration response from the Registry, the success/failure message is displayed. 
	
	RegistryRequestsTrafficSummaryEventHandler
	
	When a messaging node received a request for a traffic summary, this class is created to process that request. The tracking data is assembled and sent back to the Registry. 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	