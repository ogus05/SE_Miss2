package Book;

//BookManager의 methods 에서 throw하는 exception.
public class BookManagerException extends Exception {
	public BookManagerException() {}
	
	public BookManagerException(String msg) {
		super(msg);
	}
}
