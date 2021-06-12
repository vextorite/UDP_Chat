# UDP_Chat
A simple multi-server/multi-client implementation of  a CLI and GUI based chat application using the UDP protocol.

Each instance of the ManageClient class behaves as a new client. 


To run:

  ```java Server <optional port specification parameter>```                                 #defaults to localhostif none specified
  
  ```java ManageClient <serverIP> <serverPort> <userPort(your port)>```                     #0 or 3 parameters: defaults to localhost and default port if none
