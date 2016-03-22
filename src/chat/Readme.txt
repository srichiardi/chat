This package contains our most up to date RandomNumber implementation. This includes
the mechanisms for having a remote object in the server call back on an object in the
client in order to print screen. 

To use this code:
0. open a new terminal and go to src/random
1. javac *.java   // compile the code
2. cd .. // rmiregistry will have to be started from the root package
3. rmiregistry   // start the registry in the current directory
4. open a new window and go to the src directory
5. java random.RandomNumberServer
6. open a new window and go to the src directory
7. java random.RandomNumberClient 1 100 // or whatever parameters you like

