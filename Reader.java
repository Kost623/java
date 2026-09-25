public class Reader 
{
    String nameUser;
    String cardNumber;
    String phoneNumber;
    int yearOfBirth;

    public Reader(String nameUser, String cardNumber, String phoneNumber, int yearOfBirth) 
    {
        this.nameUser = nameUser;
        this.cardNumber = cardNumber;
        this.phoneNumber = phoneNumber;
        this.yearOfBirth = yearOfBirth;
    }
    @Override
    public String toString() {
        return String.format("Ім'я читача: \"%s\", номер карт: \"%s\", номер телефйона: \"%s\", рік народження: %d",
                nameUser, cardNumber, phoneNumber, yearOfBirth);
    }
}