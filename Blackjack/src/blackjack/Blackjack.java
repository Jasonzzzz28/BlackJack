package blackjack;
import java.util.*;

public class Blackjack implements BlackjackEngine {
	
	/**
	 * Constructor you must provide.  Initializes the player's account 
	 * to 200 and the initial bet to 5.  Feel free to initialize any other
	 * fields. Keep in mind that the constructor does not define the 
	 * deck(s) of cards.
	 * @param randomGenerator
	 * @param numberOfDecks
	 */
	private int account;
	private int bet;
	private Random rnd;
	private int numofD;
	private ArrayList <Card> deck;
	private ArrayList <Card> DealerHand;
	private ArrayList <Card> PlayerHand;
	private ArrayList <Integer> deal;
	private ArrayList <Integer> play; 
	private int GameStatus;
	private int Dsum=0;
	private int Psum=0;
	
	public Blackjack(Random randomGenerator, int numberOfDecks) {
	    account=200;
	    bet=5;
	    numofD=numberOfDecks;
	    deck=new ArrayList <Card> ();
	    GameStatus=GAME_IN_PROGRESS;
	    deal=new ArrayList<Integer>();
	    play=new ArrayList<Integer>();
	    rnd=randomGenerator;
	    
	}
	
	public int getNumberOfDecks() {
		return numofD;
	}
	
	public void createAndShuffleGameDeck() {
		this.deck=new ArrayList<Card>();
		for(int i=0;i<numofD;i++) {
		this.createOneDeck();
		
		}
		Collections.shuffle(deck,rnd);
		
	}
	private void createOneDeck() {
		for (CardSuit cs: CardSuit.values() ){
			
			for (CardValue v: CardValue.values()) {
			    
			    	this.deck.add(new Card(v,cs));	
			}
			}
		
		
	}
	
	public Card[] getGameDeck() {
		Card[] GameDeck= new Card[deck.size()];
		for (int i=0;i<deck.size();i++) {
			GameDeck[i]=deck.get(i);
		}
		return GameDeck;
	}
	
	public void deal() {
		this.GameStatus=GAME_IN_PROGRESS;
		deal=new ArrayList<Integer>();
		this.createAndShuffleGameDeck();
		DealerHand =new ArrayList<Card> ();
		PlayerHand =new ArrayList<Card> ();
		PlayerHand.add(deck.get(0));
		deck.get(1).setFaceDown();
		DealerHand.add(deck.get(1));
		PlayerHand.add(deck.get(2));
		
		DealerHand.add(deck.get(3));
		for (int i=0;i<4;i++) {
		deck.remove(0);
		}
		this.getPlayerCardsTotal();
	}
		
	public Card[] getDealerCards() {
		Card[] dealer=new Card[this.DealerHand.size()];
		for (int i=0;i<this.DealerHand.size();i++) {
			dealer[i]=this.DealerHand.get(i);
		}
		return dealer;
	}

	public int[] getDealerCardsTotal() {
		deal=new ArrayList<Integer>();
		int sum1=0;
        for (Card i:this.DealerHand) {
        	sum1+=i.getValue().getIntValue();
        }
        if(sum1<=21) {
        	deal.add(sum1);
        	Dsum=sum1;
        	
        }
        else {
        	return null;
        }
		if (helperAce(this.DealerHand)) {
		
        
        int sum2=sum1+10;
       
        if(sum2<=21) {
        	deal.add(sum2);
        	Dsum=sum2;
        	
        
		}
	    }
		int[] dealer= new int[deal.size()];
		for (int i=0;i<deal.size();i++) {
			dealer[i]=deal.get(i);
		}
		return dealer;
	    
	    
	}
	private boolean helperAce(ArrayList<Card> input){
		boolean result=false;
		for (Card i: input) {
			if(i.getValue().equals(CardValue.Ace)) {
				result=true;
			}
			
		}
		return result;
	}

	public int getDealerCardsEvaluation() {
		this.getDealerCardsTotal();
		if (this.deal.isEmpty()) {
			
			return BUST;
			
		}
		else if (this.deal.size()==1) {
			if (this.getDealerCardsTotal()[0]==21) {
				return HAS_21;
			}
			else {
				return LESS_THAN_21;
			}
		}
		else {
			if(this.getDealerCardsTotal()[1]==21) {
				if (this.DealerHand.size()==2) {
					return BLACKJACK;
				}
				else {
					return HAS_21;
				}
			
			}
			else {
				return LESS_THAN_21;
			}
		}
	}
	
	public Card[] getPlayerCards() {
		Card[] player=new Card[this.PlayerHand.size()];
		for (int i=0;i<this.PlayerHand.size();i++) {
			player[i]=this.PlayerHand.get(i);
		}
		return player;
	}
	
	public int[] getPlayerCardsTotal() {
		play=new ArrayList<Integer>();
		int sum1=0;
        for (Card i:this.PlayerHand) {
        	sum1+=i.getValue().getIntValue();
        }
        if(sum1<=21) {
        	play.add(sum1);
        	Psum=sum1;
        }
		if (helperAce(this.PlayerHand)) {
        
        int sum2=sum1+10;
       
        if(sum2<=21) {
        	play.add(sum2);
        	Psum=sum2;
        }
	    }
		int[] player= new int[play.size()];
		for (int i=0;i<play.size();i++) {
			player[i]=play.get(i);
		}
		return player;
	}
		
	public int getPlayerCardsEvaluation() {
		if (this.play.isEmpty()) {
			
			return BUST;
			
		}
		else if (this.play.size()==1) {
			if (this.getPlayerCardsTotal()[0]==21) {
				return HAS_21;
			}
			else {
				return LESS_THAN_21;
			}
		}
		else {
			if(this.getPlayerCardsTotal()[1]==21) {
				if (this.PlayerHand.size()==2) {
					return BLACKJACK;
				}
				else {
					return HAS_21;
				}
			
			}
			else {
				return LESS_THAN_21;
			}
		}
	}
	
	public void playerHit() {
		this.PlayerHand.add(this.deck.get(0));
		this.deck.remove(0);
		this.getPlayerCardsTotal();
		this.getPlayerCardsEvaluation();
		this.getDealerCardsTotal();
		
		if (this.getPlayerCardsEvaluation()==BUST) {
			this.DealerHand.get(0).setFaceUp();
			this.getDealerCardsEvaluation();
			this.GameStatus=DEALER_WON;
			
			
		}
		
	}
	
	public void playerStand() {
		this.DealerHand.get(0).setFaceUp();
		this.getPlayerCardsTotal();
		this.getPlayerCardsEvaluation();
		this.getDealerCardsTotal();
		this.getDealerCardsEvaluation();
		
		while (this.Dsum<16 & this.getDealerCardsEvaluation()!=BUST) {
			this.DealerHand.add(deck.get(0));
			this.deck.remove(0);
			
			this.getDealerCardsTotal();
			
				
		}
		this.getDealerCardsTotal();
		this.getDealerCardsEvaluation();
		this.getPlayerCardsTotal();
		this.getPlayerCardsEvaluation();
		
		if (this.getDealerCardsEvaluation()==BUST) {
			
			this.GameStatus=PLAYER_WON;
		}
		else if (Dsum>Psum) {
			this.GameStatus=  DEALER_WON;
			
		}
		else if(Dsum<Psum) {
			this.GameStatus= PLAYER_WON;
		}
		else {
			this.GameStatus= DRAW;
		}
	
		
	}
	
	public int getGameStatus() {
		return this.GameStatus;
	}
		
	public void setBetAmount(int amount) {
		this.bet=amount;
	}
	
	public int getBetAmount() {
		return this.bet;
	}
	
	public void setAccountAmount(int amount) {	
		this.account=amount;
	}
	
	public int getAccountAmount() {
		return this.account;
	}
	
	/* Feel Free to add any private methods you might need */
}