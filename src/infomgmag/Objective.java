package infomgmag;

public class Objective {
	public static enum type {
		TOTAL_DOMINATION
	}

	private type type;

	public Objective(type type) {
		this.type = type;
	}

	public Objective.type getType() {
		return this.type;
	}
}
