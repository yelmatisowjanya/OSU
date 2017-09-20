import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Game implements Serializable {

	private static final long serialVersionUID = 4010851148739558248L;
	
	public String teamA= "";
	public String teamB = "";
	public int bids[] = new int[4];
	public HashMap<Integer, Card> roundCards;
	public int roundWinner;
	public int teamAScore;
	public int teamBScore;
	public int teamATricks;
	public int teamBTricks;
	public int roundCount;
	
}
