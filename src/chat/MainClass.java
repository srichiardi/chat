package chat;

public class MainClass {

	public static void main(String[] args) throws Exception
	{
		String serverURI = "rmi://localhost/listserver";
		ClientUI cOne = new ClientUI("client_one", serverURI);
		ClientUI cTwo = new ClientUI("client_two", serverURI);
		ClientUI cThree = new ClientUI("client_three", serverURI);
	}
}
