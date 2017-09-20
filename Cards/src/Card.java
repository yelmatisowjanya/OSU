import java.io.Serializable;


public class Card implements Serializable {

	private static final long serialVersionUID = 4010851148739558248L;
	private String suit;
	private int number;
	
	public Card()
	{
		
	}
	
	public Card(String s, int n) {
		setSuit(s);
		setNumber(n);
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
	

	public String cardStringFromNumber(int num) {
		switch (num) {
		case 11:
			return "J";
		case 12:
			return "Q";
		case 13:
			return "K";
		case 14:
			return "A";
		default:
			return num + "";
		}
	}

	public int cardNumberFromString(String num) throws NumberFormatException {
		switch (num) {
		case "J":
			return 11;
		case "Q":
			return 12;
		case "K":
			return 13;
		case "A":
			return 14;
		default:
			return Integer.parseInt(num);
		}
	}
	
}