package Legacy;

/*Зовнішній/застарілий клас-каталог книг (імітація сторонньої бібліотеки).
Його методи мають інші назви і сигнатури, ніж очікує наш код.
Ми не змінюємо цей клас — він належить "зовнішньому" API.*/
public class LegacyBookCatalog
{
    public void addLegacyBook(String title, int year, double cost)
    {
        System.out.println("[LegacyAPI] Додано книгу: \"" + title + "\", рік: " + year + ", вартість: " + cost);
    }

    public double getLegacyCost(String title)
    {
        System.out.println("[LegacyAPI] Отримано вартість для книги: \"" + title + "\"");
        return 0.0;
    }

    public String[] listAllLegacyTitles()
    {
        System.out.println("[LegacyAPI] Отримано список всіх книг");
        return new String[0];
    }
}
