import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Set;
import java.util.Map.Entry;

public class Client {

	Socket socket;
	String[][] curMatrix;
	int player = 0;
	boolean gameEndFlag = false;
	boolean bidFlagSet = false;
	Card cardObj = new Card();
	ArrayList<Card> cards = new ArrayList<Card>();
	BufferedReader commandLineInput = new BufferedReader(new InputStreamReader(System.in));

	public Client() {
		try {
			socket = new Socket(Settings.IP, Settings.PORT);
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

			// Read name from user and send to Server
			if (((String) input.readObject()).equals("Name")) {
				System.out.println("Enter Name : ");
				output.writeObject(commandLineInput.readLine());
			}

			player = input.readInt();
			System.out.println("You are Player : " + player);

			// Handle socket operations until all players join
			String status = (String) input.readObject();

			// Make the player wait until all players join
			if (status.equals("Wait"))
				System.out.println("Please Wait for all the players to Join");

			// If all players join the game
			if (status.equals("Begin"))
				System.out.println("Please Wait for first player to begin the game");

			status = (String) input.readObject();

			// Start communicating with server once all the the players are
			// ready
			if (status.equals("Ready")) {
				System.out.println("Game Begins");
				System.out.println("Team A : " + (String) input.readObject());
				System.out.println("Team B : " + (String) input.readObject());

			}

			while (true) {
				// Send command to Server if ready for playing
				output.writeObject("Play");

				System.out.println();
				// Read cards from Server
				int size = input.readInt();
				readDeckFromServer(input, size);
				displayCardsToUser();

				// Write opponent bids if any
				String opponentBids = (String) input.readObject();

				if (!opponentBids.equals("")) {
					System.out.println("\n\nOpponents Bids :");
					System.out.println(opponentBids);
				}

				while (true) {
					System.out.println("\nPlayer-" + player + ": Enter your bid : ");
					String b = commandLineInput.readLine();

					try {

						int bid = Integer.parseInt(b);
						output.writeObject(bid);
						System.out.println("Bid : " + bid + " sent to the server");
						boolean result = input.readBoolean();
						if (!result) {
							System.out.println("Invalid bid number. Suggested : [0:14]");
							continue;
						} else
							break;

					} catch (NumberFormatException e) {
						System.out.println("Invalid bid. Enter a number");
						continue;
					}
				}

				bidFlagSet = false;
				String suit;
				int num;

				while (true) {

					String type = (String) input.readObject();

					if (type.equals("BIDS")) {
						System.out.println(input.readObject());
						System.out.println(input.readObject());
					}

					// Read opponents cards if any
					String opponentCards = (String) input.readObject();

					if (!opponentCards.equals("")) {
						System.out.println("\nOpponents Played Cards :");
						System.out.println(opponentCards);
					}

					// Read cards from Server
					size = input.readInt();
					readDeckFromServer(input, size);
					displayCardsToUser();
					System.out.println();

					// Read the SUIT to be played
					System.out.println(input.readObject());

					String command = (String) input.readObject();
					boolean flag = false;

					while (true) {
						System.out.println();

						System.out.println(command);
						String userInput = commandLineInput.readLine();

						if (userInput.isEmpty())
							continue;

						suit = userInput.substring(userInput.length() - 1, userInput.length()).toUpperCase();
						// System.out.println("Suit : " + suit);
						if (!(suit.equalsIgnoreCase("S") || suit.equalsIgnoreCase("H") || suit.equalsIgnoreCase("D")
								|| suit.equalsIgnoreCase("C"))) {
							System.out.println("Wrong Suit");
							System.out.println("Accepted Suits : S, H, D, C");
							continue;
						}

						try {
							num = cardObj
									.cardNumberFromString(userInput.substring(0, userInput.length() - 1).toUpperCase());
							// System.out.println("Num : " + num);

							if (num < 2 || num > 14)
								throw new NumberFormatException();
						} catch (NumberFormatException e) {
							System.out.println("Invalid number.Accepted Range : [2 : 10] and J/Q/K/A");
							continue;
						}

						Card c = new Card(suit.toUpperCase(), num);
						output.writeObject(c);

						// Check the response for correct input from Server
						boolean response = input.readBoolean();

						// Read card again for wrong inputs
						if (!response) {
							System.out.println("Invalid Card. Choose from deck :");
							displayCardsToUser();
							System.out.println();
							// Read the SUIT to be played
							System.out.println(input.readObject());
							continue;
						}
						System.out.println("Success : Card played");
						flag = true;
						break;
					}

					int gameStatus = (int) input.readObject();

					// Check if the game is done and has a winner
					if (gameStatus == 1 || gameStatus == 2) {
						System.out.println("**Game Ends**");
						displayGameStatictics(input);

						String gameWinner = (String) input.readObject();
						System.out.println(gameWinner);
						gameEndFlag = true;
						break;
					}

					if (gameStatus == 3) {
						System.out.println("-----Game Statistics-----");
						displayGameStatictics(input);
					}

					if (bidFlagSet)
						break;

				} // end of card while

				if (gameEndFlag)
					break;

			} // end while of bid
		} // end try
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param input
	 *            : InputStream to read data from Socket
	 * @param size
	 *            : size of user's current deck
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public void readDeckFromServer(ObjectInputStream input, int size) throws ClassNotFoundException, IOException {
		cards.removeAll(cards);
		for (int i = 0; i < size; i++) {
			Card c = (Card) input.readObject();
			cards.add(c);

		}
	}

	// Displays the current deck of cards to user at his end
	public void displayCardsToUser() {
		// Sort cards first
		Collections.sort(cards, new Comparator<Card>() {
			@Override
			public int compare(Card c1, Card c2) {
				int suitComparison = c1.getSuit().compareTo(c2.getSuit());

				if (suitComparison == 0) {
					return c1.getNumber() < c2.getNumber() ? -1 : 1;
				}

				else
					return c1.getSuit().compareTo(c2.getSuit());
			}
		});

		System.out.println("\n***Player-" + player + " : Your Cards***");

		for (Card c : cards) {
			System.out.print(cardObj.cardStringFromNumber(c.getNumber()) + "" + c.getSuit() + ";");
		}

	}

	// Reads game results from server after each round and displays it to client
	public void displayGameStatictics(ObjectInputStream input) throws ClassNotFoundException, IOException {
		for (int i = 0; i < 18; i++) {

			if (i == 17) {
				if ((int) input.readObject() == 13)
					bidFlagSet = true;
				return;
			}

			System.out.println(input.readObject());

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Client();

	}

}
