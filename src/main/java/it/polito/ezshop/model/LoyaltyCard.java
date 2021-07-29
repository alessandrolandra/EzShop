package it.polito.ezshop.model;

public class LoyaltyCard {

	private String cardId;
	private int points;
	
	/**
	 * 
	 * @param id
	 * @param points
	 */
	public LoyaltyCard(String id) {
		this.cardId = id;
		this.points = 0;
	}
	
	/**
	 * 
	 * @return String associated with the LoyaltyCard id
	 */
	public String getCardId() {
		return this.cardId;
	}
	
	/**
	 * Sets the LoyaltyCard id
	 * @param cardId 
	 */
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	/**
	 * 
	 * @return number of points in the card
	 */
	public int getPoints() {
		return this.points;
	}
	
	/**
	 * Updates the number of points of the card
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	/**
	 * Randomly generate a valid 10 digits String associated to a Loyalty Card
	 * @return String: Loyalty Card 10 digits number 
	 */
	public static String generate() {
		int n;
        String s = new String();
        for(int i = 0; i < 10; i++) {
            n = (int) Math.round(Math.random() * 10) % 10;
            s += n;
        }
		return s.toString();
	}
}
