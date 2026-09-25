import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        IO.println("Якщо книжка 2007 року і старша, то ціна на 5% більша");
        IO.println("Введіть кількість книжок:");

        int n = scanner.nextInt();
        scanner.nextLine();

        while (n < 1)
        {
            IO.println("Ви ввели некоректну кількість книжок, введіть повторно:");
            n = scanner.nextInt();
            scanner.nextLine();
        }

        Book[] books = new Book[n];

        for (int i = 0; i < n; i++)
        {
            IO.println("Книга #" + (i + 1));
            IO.println("Введіть назву книжки: ");
            String nameCret = scanner.nextLine();
            IO.println("Введіть рік випуску: ");
            int yearReleaseCret = scanner.nextInt();
            scanner.nextLine();

            while (yearReleaseCret < 1450 || yearReleaseCret > 2026)
            {
                IO.println("Некоректний рік (має бути між 1450 і 2026), введіть ще раз: ");
                yearReleaseCret = scanner.nextInt();
                scanner.nextLine();
            }
            IO.println("Введіть автора: ");
            String authorCret = scanner.nextLine();
            IO.println("Введіть рекомендований вік: ");
            int recommendedYearCret = scanner.nextInt();
            scanner.nextLine();

            while (recommendedYearCret < 0 || recommendedYearCret > 100)
            {
                IO.println("Некоректний вік (має бути від 0 до 100), введіть ще раз: ");
                recommendedYearCret = scanner.nextInt();
                scanner.nextLine();
            }
            IO.println("Введіть ціну книжки: ");
            double priceCret = scanner.nextDouble();
            scanner.nextLine();

            while (priceCret < 0)
            {
                IO.println("Ціна не може бути від'ємною, введіть ще раз: ");
                priceCret = scanner.nextDouble();
                scanner.nextLine();
            }

            books[i] = new Book(nameCret, yearReleaseCret, authorCret, recommendedYearCret, priceCret);
        }

        IO.println("Введіть кількість користувачів:");
        int n_u = scanner.nextInt();
        scanner.nextLine();

        while (n_u < 1)
        {
            IO.println("Ви ввели некоректну кількість користувачів, введіть повторно:");
            n_u = scanner.nextInt();
            scanner.nextLine();
        }

            Reader[] readers = new Reader[n_u];

        for (int i = 0; i < n_u; i++)
        {
            IO.println("Читач #" + (i + 1));
            IO.println("Введіть ім'я читача: ");
            String nameUserCret = scanner.nextLine();
            IO.println("Введіть номер карти: ");
            String cardNumberCret = scanner.nextLine();
            IO.println("Введіть номер телефону: ");
            String phoneNumberCret = scanner.nextLine();
            IO.println("Введіть рік народження: ");
            int yearOfBirthCret = scanner.nextInt();
            scanner.nextLine();

            while (yearOfBirthCret < 1900 || yearOfBirthCret > 2026)
            {
                IO.println("Некоректний рік народження, введіть ще раз: ");
                yearOfBirthCret = scanner.nextInt();
                scanner.nextLine();
            }

            readers[i] = new Reader(nameUserCret, cardNumberCret, phoneNumberCret, yearOfBirthCret);
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
        IO.println("\n Введіть дані книги для пошуку:");
        IO.println("Назва: ");
        String searchName = scanner.nextLine();
        IO.println("Рік випуску: ");
        int searchYear = scanner.nextInt();
        scanner.nextLine();
        IO.println("Автор: ");
        String searchAuthor = scanner.nextLine();
        IO.println("Рекомендований вік: ");
        int searchAge = scanner.nextInt();
        scanner.nextLine();
        IO.println("Ціна: ");
        double searchPrice = scanner.nextDouble();
        scanner.nextLine();

        Book sample = new Book(searchName, searchYear, searchAuthor, searchAge, searchPrice);
        int foundIndex = -1;
        for (int i = 0; i < books.length; i++)
        {
            if (books[i].equals(sample))
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
        IO.println("\n Застосування знижки");
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
