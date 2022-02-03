public class InexistentKeyException extends Exception {
	private static final long serialVersionUID = 1L;

	public InexistentKeyException(String key) {
		System.out.println("The key '" + key + "' is inexistent");
	}
}
