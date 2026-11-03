package Observer;

import Class.Book;
import Controller.LogController;

/*Спостерігач, який записує події у файл Logs/LogJava.json через LogController.*/
public class FileJsonLoggerObserver implements LibraryObserver
{
    private final String name;
    private final LogController logController;

    public FileJsonLoggerObserver(String name)
    {
        this.name = name;
        this.logController = new LogController();
    }

    @Override
    public void update(String event, Object source)
    {
        String bookName = "невідомо";
        double oldPrice = 0;
        double newPrice = 0;
        int oldNumber = 0;
        int newNumber = 0;
        String oldStatus = "невідомо";
        String newStatus = "невідомо";

        // source може бути Book або будь-яким його підкласом (PrintedBook, EBook, AudioBook)
        if (source instanceof Book)
        {
            Book book = (Book) source;
            bookName = book.getName();
            newPrice = book.getPrice();
            oldPrice = book.getPrice();
            newNumber = book.getNumber();
            oldNumber = book.getNumber();
            newStatus = book.getStatus();
            oldStatus = book.getStatus();
        }

        logController.writeLog(event, bookName, oldPrice, newPrice, oldNumber, newNumber, oldStatus, newStatus);
        System.out.println("[FileJsonLogger: " + name + "] Записано у LogJava.json: " + event + " | " + bookName);
    }
}