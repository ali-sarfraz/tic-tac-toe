public interface TTTDictionaryADT {
    public int put (TTTRecord record) throws DuplicatedKeyException;
    public void remove (String config) throws InexistentKeyException;
    public TTTRecord get (String config);
    public int numElements();
}