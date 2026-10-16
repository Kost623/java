import java.util.Scanner;
import java.util.InputMismatchException;

public class Main 
{
    public static void main(String[] args) 
    {
        Scanner scanner = new Scanner(System.in);

        try 
        {
            IO.println("Якщо книжка 2007 року і старша, то ціна на 5% більша");

            int n = readIntMin(scanner, "Введіть кількість книжок:", 1);

            Book[] books = new Book[n];

            for (int i = 0; i < n; i++) 
            {
                IO.println("Книга #" + (i + 1));
                IO.println("Введіть назву книжки: ");
                String nameCret = scanner.nextLine();

                int yearReleaseCret = readIntRange(scanner, "Введіть рік випуску: ", 1450, 2026);
                IO.println("Введіть автора: ");
                String authorCret = scanner.nextLine();

                int recommendedYearCret = readIntRange(scanner, "Введіть рекомендований вік: ", 0, 100);

                double priceCret = readDoubleMin(scanner, "Введіть ціну книжки: ", 0.0);

                try 
                {
                    books[i] = createBook(nameCret, yearReleaseCret, authorCret, recommendedYearCret, priceCret);
                } catch (InvalidPriceException e) 
                {
                    System.out.println(
                            "Помилка ціни: " + e.getMessage() + " (некоректне значення: " + e.getInvalidValue() + ")");
                    i--;
                } catch (InvalidAgeException e) 
                {
                    System.out.println(
                            "Помилка віку: " + e.getMessage() + " (некоректне значення: " + e.getInvalidValue() + ")");
                    i--;
                } catch (DomainException e) 
                {
                    System.out.println("Інша доменна помилка: " + e.getMessage());
                    i--;
                }
            }

            int n_u = readIntMin(scanner, "Введіть кількість користувачів:", 1);

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

                int yearOfBirthCret = readIntRange(scanner, "Введіть рік народження: ", 1900, 2026);

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
            // рівень 2
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
            // рівень 3
            IO.println("\n Введіть дані книги для пошуку:");
            IO.println("Назва: ");
            String searchName = scanner.nextLine();
            int searchYear = readIntRange(scanner, "Рік випуску: ", 1450, 2026);
            IO.println("Автор: ");
            String searchAuthor = scanner.nextLine();
            int searchAge = readIntRange(scanner, "Рекомендований вік: ", 0, 100);
            double searchPrice = readDoubleMin(scanner, "Ціна: ", 0.0);

            try
            {
                Book sample = createBook(searchName, searchYear, searchAuthor, searchAge, searchPrice);
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
            }
            catch (DomainException e)
            {
                // Один catch(DomainException) ловить і InvalidPriceException, і InvalidAgeException —
                // конкретний тип видно через getClass().getSimpleName()
                System.out.println("Помилка створення зразка для пошуку (" + e.getClass().getSimpleName() + "): " + e.getMessage());
            }
            
            IO.println("\nВведіть номер книги для перегляду (1-" + books.length + "): ");
            try
            {
                int bookNumber = scanner.nextInt();
                scanner.nextLine();
                Book selected = books[bookNumber - 1];
                System.out.println("Обрана книга: " + selected);
            }
            catch (ArrayIndexOutOfBoundsException e)
            {
                System.out.println("Помилка: книги з таким номером не існує.");
            }
            catch (InputMismatchException e)
            {
                System.out.println("Помилка: потрібно ввести число.");
                scanner.nextLine();
            }
            catch (Exception e)
            {
                System.out.println("Виникла непередбачена помилка: " + e.getMessage());
            }
            // пероблиний кусочок з 1 лаби
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
        finally
        {
            scanner.close();
            System.out.println("Роботу програми завершено.");
        }
    } // закриває main()

    // Початок 3 лаби
    private static int readIntMin(Scanner sc, String prompt, int min)
    {
        while (true)
        {
            try 
            {
                IO.println(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                if (value < min)
                {
                    System.out.println("Значення має бути не менше " + min + ", спробуйте ще раз.");
                    continue;
                }
                return value;
            } 
            catch (InputMismatchException e)
            {
                System.out.println("Помилка: потрібно ввести ціле число.");
                sc.nextLine();
            }
        }
    }

    // Зчитує ціле число в діапазоні [min, max], з обробкою некоректного вводу
    private static int readIntRange(Scanner sc, String prompt, int min, int max) {
        while (true)
        {
            try
            {
                IO.println(prompt);
                int value = sc.nextInt();
                sc.nextLine();
                if (value < min || value > max) 
                {
                    System.out.println("Значення має бути від " + min + " до " + max + ", спробуйте ще раз.");
                    continue;
                }
                return value;
            } 
            catch (InputMismatchException e)
            {
                System.out.println("Помилка: потрібно ввести ціле число.");
                sc.nextLine();
            }
        }
    }

    // Зчитує дробове число, не менше min, з обробкою некоректного вводу
    private static double readDoubleMin(Scanner sc, String prompt, double min) {
        while (true) 
        {
            try 
            {
                IO.println(prompt);
                double value = sc.nextDouble();
                sc.nextLine();
                if (value < min) 
                {
                    System.out.println("Значення має бути не менше " + min + ", спробуйте ще раз.");
                    continue;
                }
                return value;
            } 
            catch (InputMismatchException e)
            {
                System.out.println("Помилка: потрібно ввести число.");
                sc.nextLine();
            }
        }
    }

    private static Book createBook(String name, int yearRelease, String author, int recommendedYear, double price)
            throws DomainException 
            {
        try 
        {
            return new Book(name, yearRelease, author, recommendedYear, price);
        } catch (DomainException e) {
            System.out.println("[LOG] Помилка при створенні книги: " + e.getMessage());
            throw e; // обробили частково (залогували), прокидаємо далі для обробки на вищому рівні
        }
    }
}
