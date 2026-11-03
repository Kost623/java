package Class;

/*Клас читача з доданими полями email, дати реєстрації та статусу активності.
Ім'я читача тепер зберігається в BaseEntity (getName()), а id видається автоматично.*/
public class Reader extends BaseEntity
{
    public String cardNumber;
    public String phoneNumber;
    public int yearOfBirth;
    public String email;
    public String registrationDate;
    public boolean isActive;

    public Reader(String nameUser, String cardNumber, String phoneNumber, int yearOfBirth, String email, String registrationDate, boolean isActive)
    {
        super(nextId(), nameUser);
        this.cardNumber = cardNumber;
        this.phoneNumber = phoneNumber;
        this.yearOfBirth = yearOfBirth;
        this.email = email;
        this.registrationDate = registrationDate;
        this.isActive = isActive;
    }

    @Override
    public String toString()
    {
        return String.format("Ім'я читача: \"%s\", номер карт: \"%s\", номер телфйона: \"%s\", рік народження: %d, email: \"%s\", дата реєстрації: \"%s\", активний: %b",
                getName(), cardNumber, phoneNumber, yearOfBirth, email, registrationDate, isActive);
    }
}