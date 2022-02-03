public class DuplicatedKeyException extends Exception {
	private static final long serialVersionUID = 1L;

	public DuplicatedKeyException(String key) {
		System.out.println("The key '" + key + "' is duplicated"); 
	}
}
