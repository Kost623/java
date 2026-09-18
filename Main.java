import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner var1 = new Scanner(System.in);

        IO.println("Якщо книжка 2007 року і старша, то ціна на 5% більша");
        IO.println("Введіть кількість книжок:");

        int n = var1.nextInt();
        var1.nextLine();

        while (n < 1)
        {
            IO.println("Ви ввели некоректну кількість книжок, введіть повторно:");
            n = var1.nextInt();
            var1.nextLine();
        }

        Book[] books = new Book[n];

        for (int i = 0; i < n; i++)
        {
            IO.println("Книга #" + (i + 1));
            IO.println("Введіть назву книжки: ");
            String name_cret = var1.nextLine();
            IO.println("Введіть рік випуску: ");
            int year_release_cret = var1.nextInt();
            var1.nextLine();

            while (year_release_cret < 1450 || year_release_cret > 2026)
            {
                IO.println("Некоректний рік (має бути між 1450 і 2026), введіть ще раз: ");
                year_release_cret = var1.nextInt();
                var1.nextLine();
            }
            IO.println("Введіть автора: ");
            String author_cret = var1.nextLine();
            IO.println("Введіть рекомендований вік: ");
            int recommended_year_cret = var1.nextInt();
            var1.nextLine();

            while (recommended_year_cret < 0 || recommended_year_cret > 100)
            {
                IO.println("Некоректний вік (має бути від 0 до 100), введіть ще раз: ");
                recommended_year_cret = var1.nextInt();
                var1.nextLine();
            }
            IO.println("Введіть ціну книжки: ");
            double price_cret = var1.nextDouble();
            var1.nextLine();

            while (price_cret < 0)
            {
                IO.println("Ціна не може бути від'ємною, введіть ще раз: ");
                price_cret = var1.nextDouble();
                var1.nextLine();
            }

            books[i] = new Book(name_cret, year_release_cret, author_cret, recommended_year_cret, price_cret);
        }

        IO.println("Введіть кількість користувачів:");
        int n_u = var1.nextInt();
        var1.nextLine();

        while (n_u < 1)
        {
            IO.println("Ви ввели некоректну кількість користувачів, введіть повторно:");
            n_u = var1.nextInt();
            var1.nextLine();
        }

            Reader[] readers = new Reader[n_u];

        for (int i = 0; i < n_u; i++)
        {
            IO.println("Читач #" + (i + 1));
            IO.println("Введіть ім'я читача: ");
            String name_user_cret = var1.nextLine();
            IO.println("Введіть номер карти: ");
            String card_number_cret = var1.nextLine();
            IO.println("Введіть номер телефону: ");
            String phone_number_cret = var1.nextLine();
            IO.println("Введіть рік народження: ");
            int year_of_birth_cret = var1.nextInt();
            var1.nextLine();

            while (year_of_birth_cret < 1900 || year_of_birth_cret > 2026)
            {
                IO.println("Некоректний рік народження, введіть ще раз: ");
                year_of_birth_cret = var1.nextInt();
                var1.nextLine();
            }

            readers[i] = new Reader(name_user_cret, card_number_cret, phone_number_cret, year_of_birth_cret);
        }

        IO.println("\nУсі читачі");
        for (Reader r : readers)
        {
            System.out.println(r);
        }
        IO.println("\nУсі книги");
        for (Book b : books)
        {
            System.out.println(b);
        }

        int cheapCount = 0;
        double priceLimit = 300.0; // умова: книги дешевші за 300 грн
        for (Book b : books)
        {
            if (b.price < priceLimit)
            {
                cheapCount++;
            }
        }

        System.out.printf("Книг дешевших за %.2f грн: %d%n", priceLimit, cheapCount);
        //рівень 2
        IO.println("\nДо сортування");
        for (Book b : books)
        {
            System.out.println(b);
        }

        for (int i = 0; i < books.length - 1; i++)
        {
            for (int j = 0; j < books.length - 1 - i; j++)
            {
                if (books[j].price > books[j + 1].price)
                {
                    Book temp = books[j];
                    books[j] = books[j + 1];
                    books[j + 1] = temp;
                }
            }
        }

        IO.println("\nПісля сортування (за ціною)");
        for (Book b : books)
        {
            System.out.println(b);
        }
        //рівень 3
        IO.println("\nВведіть дані книги для пошуку:");
        IO.println("Назва: ");
        String searchName = var1.nextLine();
        IO.println("Рік випуску: ");
        int searchYear = var1.nextInt();
        var1.nextLine();
        IO.println("Автор: ");
        String searchAuthor = var1.nextLine();
        IO.println("Рекомендований вік: ");
        int searchAge = var1.nextInt();
        var1.nextLine();
        IO.println("Ціна: ");
        double searchPrice = var1.nextDouble();
        var1.nextLine();

        Book sample = new Book(searchName, searchYear, searchAuthor, searchAge, searchPrice);
        int foundIndex = -1;
        for (int i = 0; i < books.length; i++)
        {
            if (books[i].equalsBook(sample))
            {
                foundIndex = i;
                break;
            }
        }

        if (foundIndex != -1)
        {
            System.out.println("Знайдено книгу за індексом " + foundIndex + ": " + books[foundIndex]);
        }
        else
        {
            System.out.println("Книгу з такими даними не знайдено.");
        }
        //пероблиний кусочок з 1 лаби
        IO.println("\nЗастосування знижки");
        for (Book b : books)
        {
            double oldPrice = b.price;
            System.out.println("До застосування знижки:");
            System.out.println(b);

            b.applyDiscount();

            System.out.println("Після застосування знижки:");
            System.out.println(b);

            double difference = b.price - oldPrice;
            String sign;
            if (difference >= 0)
            {
                sign = "+";
            }
            else
            {
                sign = "-";
            }
            System.out.printf("Різниця: %.2f грн -> %.2f грн (зміна: %s%.2f грн)%n%n",
            oldPrice, b.price, sign, difference);
        }
    }
}
