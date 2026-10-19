package Class;

/*Клас читача з доданими полями email, дати реєстрації та статусу активності.*/
public class Reader
{
    public String nameUser;
    public String cardNumber;
    public String phoneNumber;
    public int yearOfBirth;
    public String email;
    public String registrationDate;
    public boolean isActive;

    public Reader(String nameUser, String cardNumber, String phoneNumber, int yearOfBirth, String email, String registrationDate, boolean isActive)
    {
        this.nameUser = nameUser;
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
        return String.format("Ім'я читача: \"%s\", номер карт: \"%s\", номер телефйона: \"%s\", рік народження: %d, email: \"%s\", дата реєстрації: \"%s\", активний: %b",
                nameUser, cardNumber, phoneNumber, yearOfBirth, email, registrationDate, isActive);
    }
}
