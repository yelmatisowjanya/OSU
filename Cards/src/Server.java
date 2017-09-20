import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

public class Server {

	String cardSuit[] = { "H", "D", "S", "C" };
	int cardNumber[] = { 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 };
	ArrayList<ArrayList<Card>> playersCards = new ArrayList<ArrayList<Card>>();
	Random rn = new Random();
	ArrayList<Card> deck = new ArrayList<Card>();
	private static int noOfPlayers = 0;
	private int playerToBid = 1;
	private int totalPoints[] = new int[2];
	private int roundWinner = 1;
	String gameWinner = "Tem A";
	private int bids[] = new int[4];
	int roundCount = 1;
	boolean gameEnd = false;
	boolean gameDraw = false;
	String roundSuit = "";
	int[] tricks = new int[2];
	// Read bids from players
	int count = 1;
	ArrayList<Integer> playersWhoBid = new ArrayList<Integer>();
	ArrayList<Integer> roundPlayers = new ArrayList<Integer>();
	HashMap<Integer, Card> roundCards = new HashMap<>();
	ArrayList<Integer> exitList = new ArrayList<>();
	Game game = new Game();
	Card cardObj = new Card();
	int joinedPlayersCount = 0;

	// Stores the Connection Threads for each player
	private Hashtable<Integer, Thread> connections = new Hashtable<Integer, Thread>();

	public void initializeParameters() {

		createDeck();
		Arrays.fill(bids, -1);
		for (int i = 0; i < 3; i++)
			shuffleDeck();
		distributeCards();
		exitList.add(1);
		exitList.add(2);
		exitList.add(3);
		exitList.add(4);
	}

	/**
	 * Creates a deck of cards
	 */
	public void createDeck() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				Card tempCard = new Card(cardSuit[i], cardNumber[j]);
				deck.add(tempCard);
			}
		}

	}

	/**
	 * Shuffles the deck of cards before each round
	 */
	public void shuffleDeck() {
		Collections.shuffle(deck);
	}

	/**
	 * DIstributes the deck of cards to each player
	 */
	public void distributeCards() {
		playersCards.removeAll(playersCards);
		// initialize cards list of players
		for (int i = 0; i < 4; i++) {
			ArrayList<Card> ac = new ArrayList<>();
			playersCards.add(ac);
		}

		for (int i = 0; i < 52; i++) {
			playersCards.get(i / 13).add(deck.get(i));
		}

	}

	/**
	 * Sorts the cards based on number for each suit
	 */
	public void sortCards() {
		for (int i = 0; i < 4; i++) {
			Collections.sort(playersCards.get(i), new Comparator<Card>() {
				@Override
				public int compare(Card c1, Card c2) {
					int suitComparison = c1.getSuit().compareTo(c2.getSuit());

					if (suitComparison == 0) {
						return c1.getNumber() < c2.getNumber() ? 1 : -1;
					}

					else
						return c1.getSuit().compareTo(c2.getSuit());
				}
			});
		}
	}

	/**
	 * @param player
	 *            : Current player
	 * @param playerCard
	 *            : Card sent by the player
	 * @return -1 if a wrong card is sent by the player
	 */
	public int checkCardValidityForPlayer(int player, Card playerCard) {

		int index = -1;

		// Check if the current suit of the round doesn't exist with the player
		if (!playerCard.getSuit().equalsIgnoreCase(roundSuit)) {
			for (Card c : playersCards.get(player)) {
				if (c.getSuit().equalsIgnoreCase(roundSuit))
					return -1;
			}
		}

		for (int i = 0; i < playersCards.get(player).size(); i++) {
			Card c = playersCards.get(player).get(i);

			if (c.getNumber() == playerCard.getNumber() && c.getSuit().equalsIgnoreCase(playerCard.getSuit())) {
				index = i;
				break;
			}
		}

		if (index != -1)
			playersCards.get(player).remove(index);

		return index;
	}

	/**
	 * @return current number of players who joined the game
	 */
	public int getCurrentNumberOfPlayers() {
		return noOfPlayers;
	}

	/**
	 * Increment the number of players once a new player joins
	 */
	public void setCurrentNumberofPlayers() {
		noOfPlayers = noOfPlayers + 1;
	}

	public Server() {
		ServerSocket serverSocket = null;

		System.out.println("Server Started");
		Arrays.fill(tricks, 0);

		try {
			serverSocket = new ServerSocket(Settings.PORT);
			initializeParameters();

		} catch (IOException e) {
			System.err.println("Could not listen on port: " + Settings.PORT);
			System.exit(-1);
		}

		while (true) {
			try {
				Socket s1 = serverSocket.accept();
				setCurrentNumberofPlayers();
				ConnectionThread st1 = new ConnectionThread(s1, getCurrentNumberOfPlayers());
				st1.start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Server();
	}

	/**
	 * Handles socket read and write from Client
	 *
	 */
	class ConnectionThread extends Thread {
		private Socket socket = null;
		boolean wait = true;
		int player_id;
		String playerName = "";
		String teamName = "A";

		public ConnectionThread(Socket socket, int playerId) {
			super("ConnectionThread");
			this.socket = socket;
			this.player_id = playerId;

			connections.put(player_id, this);
		}

		public void run() {

			try {
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

				// Ask for Name from player and read it
				output.writeObject("Name");
				playerName = (String) input.readObject();

				// Indicates the player_id to Client and waits until other
				// player joins
				if (joinedPlayersCount < 4) {
					System.out.println("Player " + player_id + " Joined");
					output.writeInt(player_id);
					output.writeObject("Wait");
				} else {
					// Communicates to Client once all the players join the game
					System.out.println("Player " + player_id + " Joined");
					output.writeInt(player_id);
					output.writeObject("Begin");
				}
				joinedPlayersCount++;
				output.flush();

				// decide team names for players
				if (player_id == 2 || player_id == 4) {
					teamName = "B";
					game.teamB = this.playerName + ";" + game.teamB;
				} else
					game.teamA = this.playerName + ";" + game.teamA;

				// Wake up the first player once all the players join the game
				wait = true;
				if (joinedPlayersCount % 4 == 0) {
					playerToBid = 1;
					((ConnectionThread) connections.get(playerToBid)).changeWait();
				}

				// Wait until other players Joins/Starts the game
				while (wait) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Communicate to client once all the players are ready
				output.writeObject("Ready");

				// Write Team Names
				output.writeObject(game.teamA);
				output.writeObject(game.teamB);
				output.flush();
				System.out.println("Game Begins");

				// continue each round of game of 13 cards until game ends or is
				// a draw
				while (!(gameEnd || gameDraw)) {

					String command = (String) input.readObject();
					System.out.println("Command : " + command);
					if (command.equals("Play")) {

						// write cards to player
						output.writeInt(playersCards.get(player_id - 1).size());
						writePlayerCardsToSocket(output, player_id - 1);

						// Write bids to players (if any)
						writeBidsToClient(output);

						// Read bid from players
						while (true) {
							int bid = (int) input.readObject();
							System.out.println("Player: " + player_id + " bid :  " + bid);

							// Handle wrong bid values from a client
							if (bid < 0 || bid > 13) {
								System.out.println("Input from player is wrong");
								output.writeBoolean(false);
								output.flush();
								continue;
							} else {
								bids[player_id - 1] = bid;
								game.bids[player_id - 1] = bid;
								playersWhoBid.add(player_id);
								output.writeBoolean(true);
								output.flush();
								break;
							}
						} // end while for bid

						count = player_id;

						// Read bid of other players
						while (playersWhoBid.contains(count) && playersWhoBid.size() != 4) {
							count++;

							if (count == 5)
								count = 1;
						}

						int playerToBeWoken = count;
						if (playersWhoBid.size() == 4) {
							playerToBeWoken = playerToBid;
						}

						wait = true;
						((ConnectionThread) connections.get(playerToBeWoken)).changeWait();
						// Wait
						while (wait) {
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} // while of sleeping thread
						count = 1;

						// Each round
						while (roundCount < 14) {

							if (roundCount == 1) {
								output.writeObject("BIDS");
								output.writeObject("Team A : Total Bids : " + (game.bids[0] + game.bids[2]));
								output.writeObject("Team B : Total Bids : " + (game.bids[1] + game.bids[3]));
							} else {
								output.writeObject("CARDS");
							}

							// write opponent cards to client
							writeOpponentCardsToClient(output);

							// write cards to player
							output.writeInt(playersCards.get(player_id - 1).size());
							writePlayerCardsToSocket(output, player_id - 1);

							if (roundSuit.equals(""))
								output.writeObject("Play any SUIT");
							else
								output.writeObject("Play SUIT : " + roundSuit);

							output.writeObject("Command : Enter Card");
							Card c = (Card) input.readObject();

							// Record the suit for current round
							if (roundSuit.equals(""))
								roundSuit = c.getSuit();

							while (checkCardValidityForPlayer(player_id - 1, c) == -1) {
								output.writeBoolean(false);
								output.writeObject("Play Suit : " + roundSuit);
								output.flush();
								c = (Card) input.readObject();
								System.out.println(c);
							}
							output.writeBoolean(true);
							output.flush();

							roundPlayers.add(player_id);

							// registerScoreForPlayer : save the card sent by
							// the client
							roundCards.put(player_id, c);

							count = player_id;
							// Read Card of other players
							while (roundPlayers.contains(count) && roundPlayers.size() != 4) {
								count++;

								if (count == 5)
									count = 1;
							}
							playerToBeWoken = count;
							if (roundPlayers.size() == 4) { // Invoke the winner
															// of the
								// current round next
								int p = updateScoresAtEndOfRound(roundCount);
								playerToBeWoken = p;
							}

							wait = true;
							((ConnectionThread) connections.get(playerToBeWoken)).changeWait();
							// Wait
							while (wait) {
								try {
									Thread.sleep(100);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							} // while of sleeping thread

							// write total Bids, total scores, tricks, etc
							// Check if the game ended
							if (gameEnd || gameDraw) {
								String gameResult = "";
								if (gameEnd) {
									output.writeObject(1);
									gameResult = "***Game winner is : " + gameWinner + "***";
								}

								else {
									output.writeObject(2);
									gameResult = "**Game is a Draw**";
								}
								// output.writeObject(game);
								writeGameStatisticsToSocket(output);
								output.writeObject(gameResult);
								output.flush();
								
								if(exitList.size() == 4)
								exitList.remove((int)(player_id - 1));
								else
								exitList.remove(0);	

								System.out.println("Player : " + player_id + " is exiting");
								
								if (exitList.size() > 0)
									((ConnectionThread) connections.get(exitList.get(0))).changeWait();

								// break out of loop and exit
								break;

							} else {
								output.writeObject(3);
								writeGameStatisticsToSocket(output);
								output.flush();
								if (roundCount == 1)
									break;
							}

						} // while end of each round

					} // end of if

				} // While total points < 250
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/*
		 * @param output : socket ObjectOutputStream
		 * 
		 * @param player_id : current id of player
		 * 
		 * @throws IOException
		 */
		public void writePlayerCardsToSocket(ObjectOutputStream output, int player_id) throws IOException {
			for (int i = 0; i < playersCards.get(player_id).size(); i++) {
				output.writeObject(playersCards.get(player_id).get(i));
			}
		}

		public void writeBidsToClient(ObjectOutputStream output) throws IOException {
			String bidsStr = "";
			for (int i = 0; i < 4; i++) {
				if (bids[i] != -1)
					bidsStr = bidsStr + "Player-" + (i + 1) + ": " + bids[i] + "\n";
			}
			output.writeObject(bidsStr);
		}

		public void writeOpponentCardsToClient(ObjectOutputStream output) throws IOException {
			String playedCards = "";
			Set<Entry<Integer, Card>> entries = roundCards.entrySet();
			for (Entry entry : entries) {

				Card c = (Card) entry.getValue();
				playedCards = playedCards + "Player-" + entry.getKey() + ":"
						+ cardObj.cardStringFromNumber(c.getNumber()) + c.getSuit() + "\n";
			}
			output.writeObject(playedCards);
		}

		/**
		 * @param output
		 *            : socket output stream Writes the game results to clients
		 *            at the end of each round
		 * @throws IOException
		 */
		public void writeGameStatisticsToSocket(ObjectOutputStream output) throws IOException {

			output.writeObject("Round : " + game.roundCount);
			output.writeObject("## Played Cards ##");

			Set<Entry<Integer, Card>> entries = game.roundCards.entrySet();

			for (Entry entry : entries) {
				Card c = (Card) entry.getValue();
				output.writeObject(
						"Player " + entry.getKey() + " : " + cardObj.cardStringFromNumber(c.getNumber()) + c.getSuit());
			}

			output.writeObject("Team A");
			output.writeObject("-------");
			output.writeObject("Total Bids: " + (game.bids[0] + game.bids[2]));
			output.writeObject("Tricks Won : " + game.teamATricks);
			output.writeObject("Total Score : " + game.teamAScore);

			output.writeObject("Team B");
			output.writeObject("-------");
			output.writeObject("Total Bids: " + (game.bids[1] + game.bids[3]));
			output.writeObject("Tricks Won : " + game.teamBTricks);
			output.writeObject("Total Score : " + game.teamBScore);

			output.writeObject("**Round Winner : Player-" + game.roundWinner + "**\n");
			output.writeObject(game.roundCount);
		}

		/**
		 * Change the waiting condition of Thread to false
		 */
		public void changeWait() {
			wait = false;
		}

		/**
		 * @param round
		 *            : current round Updates the total scores of the players
		 * @return : winner of current round
		 */
		public int updateScoresAtEndOfRound(int round) {
			int winner = getWinnerOfRound();

			game.roundCards = new HashMap<>(roundCards);
			game.roundCount = roundCount;
			count = 1;
			roundSuit = "";

			if (roundCount == 13) {

				// Update Scores
				// Update score of first team
				int totalBids = bids[0] + bids[2];
				if (tricks[0] >= totalBids)
					totalPoints[0] += (totalBids) * 10 + (tricks[0] - totalBids);
				else
					totalPoints[0] += (-(totalBids) * 10);

				// Update score of second team
				totalBids = bids[1] + bids[3];
				if (tricks[1] >= totalBids)
					totalPoints[1] += (totalBids) * 10 + (tricks[1] - totalBids);
				else
					totalPoints[1] += (-(totalBids) * 10);

				roundCount = 1;
				playersWhoBid.removeAll(playersWhoBid);
				Arrays.fill(tricks, 0);
				Arrays.fill(bids, -1);

				// Initialize cards to be distributed
				for (int i = 0; i < 3; i++)
					shuffleDeck();
				distributeCards();

				playerToBid = (++playerToBid) % 5;

				if (playerToBid == 0)
					playerToBid = 1;
			} else
				roundCount++;

			// Initialize each round parameters
			roundPlayers.removeAll(roundPlayers);
			roundCards.clear();

			if (totalPoints[0] >= 250 || totalPoints[1] >= 250) {
				gameEnd = true;
				gameWinner = totalPoints[0] >= totalPoints[1] ? "Team A" : "Team B";

				if (totalPoints[0] == totalPoints[1])
					gameDraw = true;

			}

			game.teamAScore = totalPoints[0];
			game.teamBScore = totalPoints[1];

			if (roundCount == 1)
				return playerToBid;

			return winner;
		}

		public int getWinnerOfRound() {
			int max = -1;

			Set<Entry<Integer, Card>> entries = roundCards.entrySet();

			for (Entry entry : entries) {

				Card c = (Card) entry.getValue();
				System.out.println("Card : " + c.getSuit() + c.getNumber());
				if (c.getSuit().equalsIgnoreCase(roundSuit) && c.getNumber() > max) {
					max = c.getNumber();
					roundWinner = (Integer) entry.getKey();
				}
			}

			if (roundWinner == 1 || roundWinner == 3)
				tricks[0]++;

			else
				tricks[1]++;

			game.teamATricks = tricks[0];
			game.teamBTricks = tricks[1];
			game.roundWinner = roundWinner;

			System.out.println("Round winner : " + roundWinner);

			return roundWinner;
		}

	}

}
