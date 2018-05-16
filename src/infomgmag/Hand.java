package infomgmag;

public class Hand {
	private int infantry, cavalry, artillery, wildcards;

	public Hand() {
		this.infantry = 0;
		this.cavalry = 0;
		this.artillery = 0;
		this.wildcards = 0;
	}

	public int getArtillery() {
		return this.artillery;
	}

	public int getCavalry() {
		return this.cavalry;
	}

	public int getInfantry() {
		return this.infantry;
	}

	public int getNumberOfCards() {
		return this.infantry + this.cavalry + this.artillery + this.wildcards;
	}

	public int getWildcards() {
		return this.wildcards;
	}

	public void setArtillery(int artillery) {
		this.artillery = artillery;
	}

	public void setCavalry(int cavalry) {
		this.cavalry = cavalry;
	}

	public void setInfantry(Integer infantry) {
		this.infantry = infantry;
	}

	public void setWildCards(int wildcards) {
		this.wildcards = wildcards;
	}
}
