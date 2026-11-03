import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

import Class.Book;
import Class.PrintedBook;
import Class.Textbook;
import Class.EBook;
import Class.AudioBook;
import Class.LegacyBook;
import Class.Library;
import Class.Reader;

import Exceptions.DomainException;
import Exceptions.InvalidPriceException;
import Exceptions.InvalidAgeException;
import Exceptions.Validator;

import Interfaces.DiscountStrategy;
import Interfaces.FirstLetterMatchDiscount;
import Context.DiscountContext;
import Factory.DiscountFactory;
import Observer.LibrarySubject;
import Observer.LibraryObserver;
import Observer.ConsoleLoggerObserver;
import Observer.FileJsonLoggerObserver;
import Adapter.BookCatalog;
import Adapter.LegacyBookCatalogAdapter;
import Legacy.LegacyBookCatalog;

public class Main
{
    // Спільний стан програми: меню працює з тими самими книгами та читачами
    private static Book[] books = new Book[0];
    private static Reader[] readers = new Reader[0];

    /*Одна дія меню; може кинути будь-який виняток, який ловить run()*/
    private interface Action
    {
        void run() throws Exception;
    }

    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        // Observer (ЛР5): суб'єкт і двоє спостерігачів створюються один раз
        LibrarySubject librarySubject = new LibrarySubject();
        LibraryObserver consoleObserver = new ConsoleLoggerObserver("Бібліотека-Консоль");
        LibraryObserver fileObserver = new FileJsonLoggerObserver("Бібліотека-Файл");
        librarySubject.subscribe(consoleObserver);
        librarySubject.subscribe(fileObserver);

        try
        {
            boolean running = true;
            while (running)
            {
                printMenu();
                String choice = scanner.nextLine().trim();

                switch (choice)
                {
                    case "1"  -> run(() -> loadSampleData());
                    case "2"  -> run(() -> enterBooks(scanner));
                    case "3"  -> run(() -> enterReaders(scanner));
                    case "4"  -> run(() -> showAll());
                    case "5"  -> run(() -> cheapAndSort());
                    case "6"  -> run(() -> searchBook(scanner));
                    case "7"  -> run(() -> viewBookByNumber(scanner));
                    case "8"  -> run(() -> demoStrategyContext());
                    case "9"  -> run(() -> applyAllStrategies());
                    case "10" -> run(() -> firstLetterMatch());
                    case "11" -> run(() -> demoObserver(librarySubject));
                    case "12" -> run(() -> demoAdapter());
                    case "13" -> run(() -> demoPolymorphism());
                    case "14" -> run(() -> demoSubclassFields());
                    case "15" -> run(() -> demoConstructorChain());
                    case "16" -> run(() -> demoEquality());
                    case "17" -> run(() -> demoComposition());
                    case "0"  -> running = false;
                    default   -> System.out.println("Невідомий пункт меню: " + choice);
                }
            }
        }
        finally
        {
            scanner.close();
            System.out.println("Роботу програми завершено.");
        }
    }

    private static void printMenu()
    {
        System.out.println();
        System.out.println("================ МЕНЮ ================");
        System.out.println(" 1 - Завантажити тестові дані (швидкий старт)");
        System.out.println(" 2 - Ввести книги вручну (ЛР1-3, з вибором формату ЛР6)");
        System.out.println(" 3 - Ввести читачів вручну");
        System.out.println(" 4 - Показати всіх читачів і всі книги");
        System.out.println(" 5 - Дешеві книги + сортування за ціною (ЛР2)");
        System.out.println(" 6 - Пошук книги за зразком, equals (ЛР2/ЛР3)");
        System.out.println(" 7 - Переглянути книгу за номером (ЛР3)");
        System.out.println(" 8 - Strategy: зміна стратегії в runtime (ЛР4)");
        System.out.println(" 9 - Усі стратегії до всіх книг (ЛР4, ЗМІНЮЄ ЦІНИ!)");
        System.out.println("10 - Збіг першої букви імені читача й назви (ЗМІНЮЄ ЦІНИ!)");
        System.out.println("11 - Observer: зміна стану книги (ЛР5)");
        System.out.println("12 - Adapter: LegacyBookCatalog (ЛР5)");
        System.out.println("13 - ЛР6: поліморфізм, абстрактні методи, final getShortInfo()");
        System.out.println("14 - ЛР6: switch + pattern matching (sealed), статичний і динамічний тип");
        System.out.println("15 - ЛР6: ланцюжок конструкторів і this(...)");
        System.out.println("16 - ЛР6: equals/hashCode, симетрія (getClass проти instanceof)");
        System.out.println("17 - ЛР6: наслідування чи композиція (Library)");
        System.out.println(" 0 - Вихід");
        System.out.print("Ваш вибір: ");
    }

    /*Єдине місце обробки помилок: збій в одному пункті не валить усе меню*/
    private static void run(Action action)
    {
        try
        {
            action.run();
        }
        catch (Exception e)
        {
            System.out.println("Помилка: " + e.getMessage());
        }
    }

    private static boolean requireBooks()
    {
        if (books.length == 0)
        {
            System.out.println("Спочатку створіть книги: пункт 1 (тестові дані) або 2 (вручну).");
            return false;
        }
        return true;
    }

    private static boolean requireReaders()
    {
        if (readers.length == 0)
        {
            System.out.println("Спочатку створіть читачів: пункт 1 (тестові дані) або 3 (вручну).");
            return false;
        }
        return true;
    }

    // ===================== Дані =====================

    private static void loadSampleData() throws DomainException
    {
        books = new Book[]
        {
            new PrintedBook("Кобзар", 1990, "Т. Шевченко", 12, 250.0, 5,
                    "978-966-03-1111-1", "Поезія", "Дніпро", 4.8, 320, true),
            new EBook("Тіні забутих предків", 2015, "М. Коцюбинський", 14, 90.0, 100,
                    "978-966-03-2222-2", "Повість", "Віват", 4.5, 2.4, "EPUB"),
            new AudioBook("Intermezzo", 2007, "М. Коцюбинський", 16, 120.0, 30,
                    "978-966-03-3333-3", "Новела", "Яблуко", 4.6, 95, "А. Хостікоєв"),
            new Textbook("Алгебра, 9 клас", 2019, "О. Істер", 14, 180.0, 40,
                    "978-966-11-4444-4", "Підручник", "Генеза", 4.2, 288, true, "Математика", 9)
        };

        readers = new Reader[]
        {
            new Reader("Катерина", "C-001", "+380501112233", 2003, "kate@example.com", "2026-01-15", true),
            new Reader("Михайло", "C-002", "+380502223344", 1999, "mike@example.com", "2026-02-20", true)
        };

        System.out.println("Завантажено книг: " + books.length + ", читачів: " + readers.length);
    }

    private static void enterBooks(Scanner scanner)
    {
        int n = readIntMin(scanner, "Введіть кількість книжок:", 1);
        Book[] created = new Book[n];

        for (int i = 0; i < n; i++)
        {
            IO.println("Книга #" + (i + 1));
            try
            {
                created[i] = readBook(scanner);
            }
            catch (InvalidPriceException e)
            {
                System.out.println("Помилка ціни: " + e.getMessage() + " (некоректне значення: " + e.getInvalidValue() + ")");
                i--;
            }
            catch (InvalidAgeException e)
            {
                System.out.println("Помилка віку: " + e.getMessage() + " (некоректне значення: " + e.getInvalidValue() + ")");
                i--;
            }
            catch (DomainException | IllegalArgumentException e)
            {
                System.out.println("Інша помилка: " + e.getMessage());
                i--;
            }
        }
        books = created;
    }

    private static void enterReaders(Scanner scanner)
    {
        int n = readIntMin(scanner, "Введіть кількість користувачів:", 1);
        Reader[] created = new Reader[n];

        for (int i = 0; i < n; i++)
        {
            IO.println("Читач #" + (i + 1));
            IO.println("Введіть ім'я читача: ");
            String nameUserCret = scanner.nextLine();
            IO.println("Введіть номер карти: ");
            String cardNumberCret = scanner.nextLine();
            IO.println("Введіть номер телефону: ");
            String phoneNumberCret = scanner.nextLine();
            while (!Validator.isValidPhone(phoneNumberCret))
            {
                System.out.println("Невірний формат телефону. Введіть ще раз: ");
                phoneNumberCret = scanner.nextLine();
            }
            IO.println("Введіть email: ");
            String emailCret = scanner.nextLine();
            while (!Validator.isValidEmail(emailCret))
            {
                System.out.println("Невірний формат email. Введіть ще раз: ");
                emailCret = scanner.nextLine();
            }
            IO.println("Введіть дату реєстрації (Наприклад: 2026-01-15): ");
            String registrationDateCret = scanner.nextLine();
            boolean isActiveCret = readBoolean(scanner, "Чи активний читач? (true/false): ");
            int yearOfBirthCret = readIntRange(scanner, "Введіть рік народження: ", 1900, 2026);

            created[i] = new Reader(nameUserCret, cardNumberCret, phoneNumberCret, yearOfBirthCret,
                    emailCret, registrationDateCret, isActiveCret);
        }
        readers = created;
    }

    /*Читає з консолі одну книгу; формат (ЛР6) визначає, який підклас буде створено*/
    private static Book readBook(Scanner scanner) throws DomainException
    {
        IO.println("Введіть назву книжки: ");
        String name = scanner.nextLine();
        int yearRelease = readIntRange(scanner, "Введіть рік випуску: ", 1450, 2026);
        IO.println("Введіть автора: ");
        String author = scanner.nextLine();
        int recommendedYear = readIntRange(scanner, "Введіть рекомендований вік: ", 0, 100);
        double price = readDoubleMin(scanner, "Введіть ціну книжки: ", 0.0);
        int number = readIntMin(scanner, "Введіть кількість книг:", 0);
        IO.println("Введіть ISBN: ");
        String isbn = scanner.nextLine();
        IO.println("Введіть жанр: ");
        String genre = scanner.nextLine();
        IO.println("Введіть видавця: ");
        String publisher = scanner.nextLine();
        double rating = readDoubleRange(scanner, "Введіть рейтинг (0.0-5.0): ", 0.0, 5.0);

        int format = readIntRange(scanner,
                "Формат книги: 1 - паперова, 2 - електронна, 3 - аудіо, 4 - підручник: ", 1, 4);

        try
        {
            switch (format)
            {
                case 1:
                {
                    int pages = readIntMin(scanner, "Кількість сторінок: ", 1);
                    boolean hardcover = readBoolean(scanner, "Тверда обкладинка? (true/false): ");
                    return new PrintedBook(name, yearRelease, author, recommendedYear, price, number,
                            isbn, genre, publisher, rating, pages, hardcover);
                }
                case 2:
                {
                    double sizeMb = readDoubleMin(scanner, "Розмір файлу, МБ: ", 0.1);
                    IO.println("Формат файлу (EPUB, PDF, ...): ");
                    String fileFormat = scanner.nextLine();
                    return new EBook(name, yearRelease, author, recommendedYear, price, number,
                            isbn, genre, publisher, rating, sizeMb, fileFormat);
                }
                case 3:
                {
                    int minutes = readIntMin(scanner, "Тривалість, хв: ", 1);
                    IO.println("Диктор: ");
                    String narrator = scanner.nextLine();
                    return new AudioBook(name, yearRelease, author, recommendedYear, price, number,
                            isbn, genre, publisher, rating, minutes, narrator);
                }
                case 4:
                {
                    int pages = readIntMin(scanner, "Кількість сторінок: ", 1);
                    boolean hardcover = readBoolean(scanner, "Тверда обкладинка? (true/false): ");
                    IO.println("Предмет: ");
                    String subject = scanner.nextLine();
                    int grade = readIntRange(scanner, "Клас (1-11): ", 1, 11);
                    return new Textbook(name, yearRelease, author, recommendedYear, price, number,
                            isbn, genre, publisher, rating, pages, hardcover, subject, grade);
                }
                default:
                    throw new IllegalStateException("Невідомий формат: " + format);
            }
        }
        catch (DomainException e)
        {
            System.out.println("[LOG] Помилка при створенні книги: " + e.getMessage());
            throw e; // залогували, прокидаємо далі (ЛР3: re-throw)
        }
    }

    // ===================== ЛР1-3 =====================

    private static void showAll()
    {
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
        if (readers.length == 0 && books.length == 0)
        {
            System.out.println("(даних ще немає)");
        }
    }

    private static void cheapAndSort()
    {
        if (!requireBooks()) return;

        int cheapCount = 0;
        double priceLimit = 300.0; // умова: книги дешевші за 300 грн
        for (Book b : books)
        {
            if (b.getPrice() < priceLimit)
            {
                cheapCount++;
            }
        }
        System.out.printf("Книг дешевших за %.2f грн: %d%n", priceLimit, cheapCount);

        IO.println("\nДо сортування");
        for (Book b : books)
        {
            System.out.println(b);
        }

        for (int i = 0; i < books.length - 1; i++)
        {
            for (int j = 0; j < books.length - 1 - i; j++)
            {
                if (books[j].getPrice() > books[j + 1].getPrice())
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
    }

    private static void searchBook(Scanner scanner)
    {
        if (!requireBooks()) return;

        IO.println("\nВведіть дані книги для пошуку (тип книги теж має збігатися):");
        try
        {
            Book sample = readBook(scanner);
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
            // Один catch(DomainException) ловить і InvalidPriceException, і InvalidAgeException
            System.out.println("Помилка створення зразка для пошуку (" + e.getClass().getSimpleName() + "): " + e.getMessage());
        }
    }

    private static void viewBookByNumber(Scanner scanner)
    {
        if (!requireBooks()) return;

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
    }

    // ===================== ЛР4 / ЛР5: Strategy, Factory =====================

    private static void demoStrategyContext()
    {
        if (!requireBooks()) return;

        IO.println("\n Застосування знижок через контекстний клас (Strategy):");
        Book demoBook = books[0];
        DiscountContext context = new DiscountContext(DiscountFactory.createDiscount("REGULAR"));

        System.out.println("Демонстраційна книга: " + demoBook);

        double price1 = context.executeDiscount(demoBook.getPrice(), demoBook.getYearRelease());
        System.out.println("Стратегія: " + context.getStrategy().getClass().getSimpleName()
                + " — " + context.getStrategy().getDescription());
        System.out.printf("Ціна: %.2f грн -> %.2f грн%n%n", demoBook.getPrice(), price1);

        // Зміна стратегії в runtime
        context.setStrategy(DiscountFactory.createDiscount("NEW_RELEASE"));
        double price2 = context.executeDiscount(demoBook.getPrice(), demoBook.getYearRelease());
        System.out.println("Стратегія змінена на: " + context.getStrategy().getClass().getSimpleName()
                + " — " + context.getStrategy().getDescription());
        System.out.printf("Ціна: %.2f грн -> %.2f грн%n%n", demoBook.getPrice(), price2);

        context.setStrategy(DiscountFactory.createDiscount("FIRST_LETTER"));
        double price3 = context.executeDiscount(demoBook.getPrice(), demoBook.getYearRelease());
        System.out.println("Стратегія змінена на: " + context.getStrategy().getClass().getSimpleName()
                + " — " + context.getStrategy().getDescription());
        System.out.printf("Ціна: %.2f грн -> %.2f грн%n%n", demoBook.getPrice(), price3);
    }

    private static void applyAllStrategies()
    {
        if (!requireBooks()) return;

        IO.println("\n Застосування всіх стратегій до всіх книг:");
        DiscountStrategy[] strategies = new DiscountStrategy[3];
        strategies[0] = DiscountFactory.createDiscount("REGULAR");
        strategies[1] = DiscountFactory.createDiscount("NEW_RELEASE");
        strategies[2] = DiscountFactory.createDiscount("FIRST_LETTER");

        for (DiscountStrategy ds : strategies)
        {
            IO.println("Стратегія: " + ds.getClass().getSimpleName() + " — " + ds.getDescription());
            for (Book b : books)
            {
                double oldPrice = b.getPrice();
                System.out.println("До застосування знижки:");
                System.out.println(b);

                try
                {
                    b.setPrice(ds.applyDiscount(b.getPrice(), b.getYearRelease()));
                }
                catch (DomainException e)
                {
                    System.out.println("Не вдалося застосувати знижку: " + e.getMessage());
                    continue;
                }

                System.out.println("Після застосування знижки:");
                System.out.println(b);

                double difference = b.getPrice() - oldPrice;
                String sign = difference >= 0 ? "+" : "-";
                System.out.printf("Різниця: %.2f грн -> %.2f грн (зміна: %s%.2f грн)%n%n",
                        oldPrice, b.getPrice(), sign, Math.abs(difference));
            }
        }
    }

    private static void firstLetterMatch()
    {
        if (!requireBooks() || !requireReaders()) return;

        IO.println("\n Функція збігу першої букви імені читача та назви книги:");
        boolean anyMatch = false;
        for (Reader r : readers)
        {
            FirstLetterMatchDiscount matchDiscount = DiscountFactory.createFirstLetterDiscount(r.nameUser);
            for (Book b : books)
            {
                if (matchDiscount.isMatch(b.getName()))
                {
                    anyMatch = true;
                    double oldPrice = b.getPrice();
                    try
                    {
                        b.setPrice(matchDiscount.applyPriceIncrease(b.getPrice()));
                    }
                    catch (DomainException e)
                    {
                        System.out.println("Не вдалося змінити ціну: " + e.getMessage());
                        continue;
                    }
                    System.out.println("Читач: \"" + r.nameUser + "\", книга: \"" + b.getName() + "\"");
                    System.out.printf("Ціна збільшена на 50%%: %.2f грн -> %.2f грн%n%n", oldPrice, b.getPrice());
                }
            }
        }
        if (!anyMatch)
        {
            System.out.println("Збігів першої букви не знайдено.");
        }
    }

    // ===================== ЛР5: Observer, Adapter =====================

    private static void demoObserver(LibrarySubject librarySubject)
    {
        if (!requireBooks()) return;

        IO.println("\n Демонстрація Observer — зміна стану книги:");
        Book demo = books[0];
        double oldPrice = demo.getPrice();
        int oldNumber = demo.getNumber();
        String oldStatus = demo.getStatus();

        try
        {
            demo.setPrice(oldPrice * 0.9);
        }
        catch (DomainException e)
        {
            System.out.println("Помилка зміни ціни: " + e.getMessage());
        }
        if (oldNumber > 0)
        {
            demo.setNumber(oldNumber - 1);
        }

        librarySubject.notifyObservers("BOOK_STATE_CHANGED", demo);

        System.out.println("Книга оновлена: " + demo);
        System.out.println("Старе значення: ціна=" + oldPrice + ", кількість=" + oldNumber + ", статус=" + oldStatus);
        System.out.println("Нове значення: ціна=" + demo.getPrice() + ", кількість=" + demo.getNumber() + ", статус=" + demo.getStatus());
    }

    private static void demoAdapter()
    {
        if (!requireBooks()) return;

        IO.println("\n Демонстрація Adapter — інтеграція з зовнішнім каталогом LegacyBookCatalog:");
        LegacyBookCatalog legacyCatalog = new LegacyBookCatalog();
        BookCatalog catalog = new LegacyBookCatalogAdapter(legacyCatalog);

        catalog.addBook(books[0]);
        System.out.println("Вартість книги за адаптером: " + catalog.getPrice(books[0].getName()));
        System.out.println("Кількість книг у каталозі: " + catalog.getAllBooks().size());
    }

    // ===================== ЛР6: наслідування =====================

    private static void demoPolymorphism()
    {
        if (!requireBooks()) return;

        IO.println("\n=== ЛР6: поліморфізм (Book[] з різними підкласами) ===");
        for (Book b : books)
        {
            // getShortInfo() — final у Book, але всередині викликає абстрактний getFormatName() підкласу
            System.out.println(b.getShortInfo());
            System.out.println("   формат: " + b.getFormatName()
                    + "; обсяг: " + b.getSizeInfo()
                    + "; строк видачі: " + b.getLoanDays() + " днів");
            System.out.println("   " + b);
        }
    }

    /*switch з pattern matching по запечатаній ієрархії: default не потрібен,
    бо компілятор знає всі прямі підкласи Book і перевіряє, що жодного не пропущено*/
    private static String describeSpecific(Book b)
    {
        return switch (b)
        {
            // Підтип (Textbook) мусить іти РАНІШЕ за надтип (PrintedBook), інакше помилка компіляції (dominance)
            case Textbook t -> "підручник: предмет " + t.getSubject() + ", " + t.getGradeLevel()
                    + " клас, сторінок " + t.getPages();
            case PrintedBook p -> "паперова: сторінок " + p.getPages() + ", тверда обкладинка: " + p.hasHardcover();
            case EBook e -> "електронна: " + e.getFileSizeMb() + " МБ, формат " + e.getFileFormat();
            case AudioBook a -> "аудіо: " + a.getDurationMinutes() + " хв, диктор " + a.getNarrator();
            case LegacyBook l -> "архівний запис: специфічних полів немає";
        };
    }

    private static void demoSubclassFields()
    {
        if (!requireBooks()) return;

        IO.println("\n=== ЛР6: switch + pattern matching (sealed), статичний і динамічний тип ===");
        for (Book b : books)
        {
            System.out.println("\"" + b.getName() + "\": статичний тип - Book, динамічний тип - "
                    + b.getClass().getSimpleName());
            System.out.println("   " + describeSpecific(b));
        }

        // Явне приведення: компілятор знає лише статичний тип Book, тому getPages() через нього недоступний
        IO.println("\nПриведення типів на першій книзі:");
        Book target = books[0];
        // target.getPages();  // не скомпілюється: у статичного типу Book немає методу getPages()
        if (target instanceof PrintedBook p)
        {
            System.out.println("instanceof з pattern matching: сторінок " + p.getPages());
        }
        try
        {
            EBook asEBook = (EBook) target;
            System.out.println("Приведення до EBook вдалося: формат " + asEBook.getFileFormat());
        }
        catch (ClassCastException e)
        {
            System.out.println("ClassCastException: об'єкт типу " + target.getClass().getSimpleName()
                    + " не є EBook, приведення неможливе.");
        }
    }

    private static void demoConstructorChain() throws DomainException
    {
        IO.println("\n=== ЛР6: ланцюжок конструкторів ===");
        Book.setTraceConstructors(true);
        try
        {
            IO.println("1) Повний конструктор Textbook (3 рівні ієрархії):");
            new Textbook("Геометрія, 8 клас", 2021, "А. Мерзляк", 13, 150.0, 20,
                    "978-966-11-5555-5", "Підручник", "Гімназія", 4.0, 240, true, "Математика", 8);

            IO.println("\n2) Скорочений конструктор Textbook (this(...) -> повний -> super(...)):");
            new Textbook("Фізика, 10 клас", 2018, "Т. Засєкіна", 15, 160.0, 256, "Фізика", 10);

            IO.println("\n3) Скорочений конструктор PrintedBook:");
            new PrintedBook("Невідома брошура", 2000, "Анонім", 0, 10.0, 24);
        }
        finally
        {
            Book.setTraceConstructors(false);
        }
        IO.println("\nТіло конструктора виконується після super(...), тому повідомлення йдуть від базового класу до похідних.");
    }

    // ---------- equals/hashCode: getClass() проти instanceof ----------
    private static void demoEquality() throws DomainException
    {
        IO.println("\n=== ЛР6: equals/hashCode і симетрія ===");

        PrintedBook printed = new PrintedBook("Алгебра", 2019, "О. Істер", 14, 180.0, 40,
                "978-966-11-4444-4", "Підручник", "Генеза", 4.2, 288, true);
        PrintedBook printedCopy = new PrintedBook("Алгебра", 2019, "О. Істер", 14, 180.0, 40,
                "978-966-11-4444-4", "Підручник", "Генеза", 4.2, 288, true);
        Textbook textbook = new Textbook("Алгебра", 2019, "О. Істер", 14, 180.0, 40,
                "978-966-11-4444-4", "Підручник", "Генеза", 4.2, 288, true, "Математика", 9);

        IO.println("1) getClass() — наш варіант (Book -> PrintedBook -> Textbook):");
        System.out.println("   printed.equals(printedCopy): " + printed.equals(printedCopy)
                + ", hashCode однакові: " + (printed.hashCode() == printedCopy.hashCode()));
        System.out.println("   printed.equals(textbook):    " + printed.equals(textbook));
        System.out.println("   textbook.equals(printed):    " + textbook.equals(printed));
        System.out.println("   -> обидва false: симетрія збережена");

        IO.println("\n2) instanceof — наївний варіант (спеціальні класи-приклади):");
        InstanceofBook base = new InstanceofBook("Алгебра");
        InstanceofTextbook sub = new InstanceofTextbook("Алгебра", "Математика");
        System.out.println("   base.equals(sub): " + base.equals(sub));
        System.out.println("   sub.equals(base): " + sub.equals(base));
        System.out.println("   -> результати різні: симетрію порушено");
        System.out.println("   base.equals(sub) == true, але hashCode: "
                + (base.hashCode() == sub.hashCode() ? "однакові" : "різні (порушено ще й контракт hashCode)"));
    }

    /*Наївна база: equals через instanceof, нічого про підкласи не знає*/
    private static class InstanceofBook
    {
        protected final String title;

        InstanceofBook(String title)
        {
            this.title = title;
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof InstanceofBook that)) return false;
            return title.equals(that.title);
        }

        @Override
        public int hashCode()
        {
            return title.hashCode();
        }
    }

    /*Підклас додає поле й вимагає його збігу, але лише коли other теж підклас*/
    private static class InstanceofTextbook extends InstanceofBook
    {
        private final String subject;

        InstanceofTextbook(String title, String subject)
        {
            super(title);
            this.subject = subject;
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof InstanceofTextbook that)) return false;
            return super.equals(o) && subject.equals(that.subject);
        }

        @Override
        public int hashCode()
        {
            return 31 * super.hashCode() + subject.hashCode();
        }
    }

    // ---------- наслідування чи композиція ----------
    private static void demoComposition() throws DomainException
    {
        if (!requireBooks()) return;

        IO.println("\n=== ЛР6: наслідування чи композиція ===");
        Book original = books[0];
        Book duplicate = new EBook("Копія з тим самим ISBN", 2020, "Хтось", 10, 1.0, 1,
                original.getIsbn(), "Жанр", "Видавець", 1.0, 1.0, "PDF");

        IO.println("1) Хибне наслідування: BrokenLibrary extends ArrayList<Book>");
        BrokenLibrary broken = new BrokenLibrary();
        broken.add(original);
        try
        {
            broken.add(duplicate);
            System.out.println("   add(дублікат): прийнято");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("   add(дублікат): відхилено (" + e.getMessage() + ")");
        }
        broken.addAll(List.of(duplicate));
        System.out.println("   addAll(дублікат): прийнято, розмір " + broken.size());
        broken.add(0, duplicate);
        System.out.println("   add(0, дублікат): прийнято, розмір " + broken.size());
        System.out.println("   -> інваріант «ISBN унікальний» зламано: " + broken.size() + " записи з одним ISBN");

        IO.println("\n2) Композиція: Library містить List<Book>");
        Library library = new Library("Центральна бібліотека");
        library.addBook(original);
        try
        {
            library.addBook(duplicate);
            System.out.println("   addBook(дублікат): прийнято");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println("   addBook(дублікат): відхилено (" + e.getMessage() + ")");
        }
        try
        {
            library.getBooks().add(duplicate);
        }
        catch (UnsupportedOperationException e)
        {
            System.out.println("   getBooks().add(...): UnsupportedOperationException, в обхід addBook не додати");
        }
        System.out.println("   розмір бібліотеки: " + library.size());
    }

    /*ХИБНИЙ приклад: бібліотека не Є списком книг, але успадковує весь API ArrayList.
    Перевизначено лише add(Book), а addAll(...) та add(index, ...) проходять повз перевірку.*/
    private static class BrokenLibrary extends ArrayList<Book>
    {
        @Override
        public boolean add(Book book)
        {
            for (Book b : this)
            {
                if (b.getIsbn().equals(book.getIsbn()))
                {
                    throw new IllegalArgumentException("дублікат ISBN " + book.getIsbn());
                }
            }
            return super.add(book);
        }
    }

    // ===================== Допоміжні методи введення (ЛР3) =====================

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
    private static int readIntRange(Scanner sc, String prompt, int min, int max)
    {
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
    private static double readDoubleMin(Scanner sc, String prompt, double min)
    {
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

    // Зчитує дробове число в діапазоні [min, max]
    private static double readDoubleRange(Scanner sc, String prompt, double min, double max)
    {
        while (true)
        {
            try
            {
                IO.println(prompt);
                double value = sc.nextDouble();
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
                System.out.println("Помилка: потрібно ввести число.");
                sc.nextLine();
            }
        }
    }

    private static boolean readBoolean(Scanner sc, String prompt)
    {
        while (true)
        {
            IO.println(prompt);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("true") || s.equals("так") || s.equals("y"))
            {
                return true;
            }
            if (s.equals("false") || s.equals("ні") || s.equals("n"))
            {
                return false;
            }
            System.out.println("Введіть true або false.");
        }
    }
}