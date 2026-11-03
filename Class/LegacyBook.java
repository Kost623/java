package Class;

import Exceptions.DomainException;

/*Запис архівного каталогу, у якому формат невідомий. Раніше це був приватний клас в адаптері,
але запечатаний Book дозволяє підкласи лише з власного пакета, тому клас переїхав сюди.*/
public final class LegacyBook extends Book
{
    public LegacyBook(String title) throws DomainException
    {
        super(title, 0, "Невідомо", 0, 0.0, 0, "Невідомо", "Невідомо", "Невідомо", 0.0);
        trace("LegacyBook: запис архівного каталогу");
    }

    @Override
    public String getFormatName()
    {
        return "Запис архівного каталогу";
    }

    @Override
    public String getSizeInfo()
    {
        return "невідомо";
    }
}