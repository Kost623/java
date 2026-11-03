package Service;

import Class.Book;

import java.util.List;

/*Інтерфейс сутності Book. Метод сортування за другим полем (ціною) залежить від поля
конкретної сутності, тому живе тут, а не в загальному CRUDService<T>.*/
public interface BookService extends CRUDService<Book>
{
    // Спочатку за назвою, а при збігу назв за ціною (за зростанням)
    List<Book> findAllSortedByNameAndByPrice();
}