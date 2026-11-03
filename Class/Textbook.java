package Class;

import Exceptions.DomainException;

import java.util.Objects;

/*Підручник: Book -> PrintedBook -> Textbook. final: підкласів у домені не передбачено.*/
public final class Textbook extends PrintedBook
{
    private String subject;
    private int gradeLevel;

    // Повний конструктор
    public Textbook(String name, int yearRelease, String author, int recommendedYear, double price, int number,
                    String isbn, String genre, String publisher, double rating,
                    int pages, boolean hasHardcover,
                    String subject, int gradeLevel) throws DomainException
    {
        super(name, yearRelease, author, recommendedYear, price, number, isbn, genre, publisher, rating,
                pages, hasHardcover);
        if (gradeLevel < 1 || gradeLevel > 11)
        {
            throw new IllegalArgumentException("Клас має бути від 1 до 11: " + gradeLevel);
        }
        this.subject = subject;
        this.gradeLevel = gradeLevel;

        trace("Textbook: додано предмет і клас");
    }

    // Скорочений конструктор: делегує повному через this(...)
    public Textbook(String name, int yearRelease, String author, int recommendedYear, double price,
                    int pages, String subject, int gradeLevel) throws DomainException
    {
        this(name, yearRelease, author, recommendedYear, price, 1, UNKNOWN, "Підручник", UNKNOWN, 0.0,
                pages, true, subject, gradeLevel);
        trace("Textbook: скорочений конструктор (делегував через this(...))");
    }

    public String getSubject() { return subject; }
    public int getGradeLevel() { return gradeLevel; }

    @Override
    public String getFormatName()
    {
        return "Підручник (" + subject + ", " + gradeLevel + " клас)";
    }

    @Override
    public String getSizeInfo()
    {
        return super.getSizeInfo() + ", " + gradeLevel + " клас";
    }

    @Override
    public int getLoanDays()
    {
        return 30; // підручники видають на семестр
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(", предмет: \"%s\", клас: %d", subject, gradeLevel);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!super.equals(other)) return false;
        var that = (Textbook) other;
        return gradeLevel == that.gradeLevel && subject.equals(that.subject);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), subject, gradeLevel);
    }
}