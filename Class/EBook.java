package Class;

import Exceptions.DomainException;

import java.util.Objects;

/*Електронна книга: is-a Book з розміром файлу та форматом.*/
public final class EBook extends Book
{
    private double fileSizeMb;
    private String fileFormat;

    public EBook(String name, int yearRelease, String author, int recommendedYear, double price, int number,
                 String isbn, String genre, String publisher, double rating,
                 double fileSizeMb, String fileFormat) throws DomainException
    {
        super(name, yearRelease, author, recommendedYear, price, number, isbn, genre, publisher, rating);
        if (fileSizeMb <= 0)
        {
            throw new IllegalArgumentException("Розмір файлу має бути додатним: " + fileSizeMb);
        }
        this.fileSizeMb = fileSizeMb;
        this.fileFormat = fileFormat;

        trace("EBook: додано розмір файлу й формат");
    }

    public double getFileSizeMb() { return fileSizeMb; }
    public String getFileFormat() { return fileFormat; }

    @Override
    public String getFormatName()
    {
        return "Електронна книга (" + fileFormat + ")";
    }

    @Override
    public String getSizeInfo()
    {
        return String.format("%.1f МБ", fileSizeMb);
    }

    @Override
    public int getLoanDays()
    {
        return 21;
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(", розмір файлу: %.1f МБ, формат: %s", fileSizeMb, fileFormat);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!super.equals(other)) return false;
        var that = (EBook) other;
        return fileSizeMb == that.fileSizeMb && fileFormat.equals(that.fileFormat);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), fileSizeMb, fileFormat);
    }
}