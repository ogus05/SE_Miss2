package Book;

import java.util.Comparator;

//BookManagerBase의 InnerSearch를 Binary Search로 구현한 class.
public class BookManagerBS extends BookManagerBase {
	
	public BookManagerBS() {
		super();
	}
	
	//입력값. book id.
	//출력값. 입력 id를 갖는 book or null.
	//동작 요약. Binary Search를 통해 입력값의 id를 갖는 book을 찾아 반환.
	@Override
	protected Book InnerSearch(int in_id) {
		// Binary Search 전에는 sorting이 필요.
        bookList.sort(Comparator.comparingInt(b -> b.getId()));

		int leftIdx = 0, rightIdx = bookList.size() - 1; 
	       
        while (leftIdx <= rightIdx)
        { 
            int midIdx = leftIdx + (rightIdx - leftIdx) / 2; 
            
            Book bookInCurrentLoop = bookList.get(midIdx);
            
            if (bookInCurrentLoop.getId() == in_id) {
            	return bookInCurrentLoop;             	
            }
   
            if (bookInCurrentLoop.getId() < in_id) {
            	leftIdx = midIdx + 1;             	
            }
   
            else {
            	rightIdx = midIdx - 1;             	
            }
        } 
   
        return null; 
	}
}
