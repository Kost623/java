package Class;

import Exceptions.DomainException;

import java.util.Objects;

/*Аудіокнига: is-a Book з тривалістю та диктором.*/
public final class AudioBook extends Book
{
    private int durationMinutes;
    private String narrator;

    public AudioBook(String name, int yearRelease, String author, int recommendedYear, double price, int number,
                     String isbn, String genre, String publisher, double rating,
                     int durationMinutes, String narrator) throws DomainException
    {
        super(name, yearRelease, author, recommendedYear, price, number, isbn, genre, publisher, rating);
        if (durationMinutes <= 0)
        {
            throw new IllegalArgumentException("Тривалість має бути додатною: " + durationMinutes);
        }
        this.durationMinutes = durationMinutes;
        this.narrator = narrator;

        trace("AudioBook: додано тривалість і диктора");
    }

    public int getDurationMinutes() { return durationMinutes; }
    public String getNarrator() { return narrator; }

    @Override
    public String getFormatName()
    {
        return "Аудіокнига";
    }

    @Override
    public String getSizeInfo()
    {
        return durationMinutes + " хв";
    }

    @Override
    public int getLoanDays()
    {
        return 10;
    }

    @Override
    public String toString()
    {
        return super.toString() + String.format(", тривалість: %d хв, диктор: \"%s\"", durationMinutes, narrator);
    }

    @Override
    public boolean equals(Object other)
    {
        if (!super.equals(other)) return false;
        var that = (AudioBook) other;
        return durationMinutes == that.durationMinutes && narrator.equals(that.narrator);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), durationMinutes, narrator);
    }
}