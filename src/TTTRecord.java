public class TTTRecord {
	private String configuration;
	private int score_;
	private int level_;
	
	public TTTRecord(String config, int score, int level) {
		configuration = config;
		score_ = score;   
		level_ = level;
	}
	
	public String getConfiguration() {	
		return configuration;
	}
	
	public int getScore() {
		return score_;
	}
	
	public int getLevel() {
		return level_;
	}	
}
