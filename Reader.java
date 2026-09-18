public class Reader 
{
    String name_user;
    String card_number;
    String phone_number;
    int year_of_birth;

    public Reader(String name_user, String card_number, String phone_number, int year_of_birth) 
    {
        this.name_user = name_user;
        this.card_number = card_number;
        this.phone_number = phone_number;
        this.year_of_birth = year_of_birth;
    }
    @Override
    public String toString() {
        return String.format("Ім'я читача: \"%s\", номер карт: \"%s\", номер телефйона: \"%s\", рік народження: %d",
                name_user, card_number, phone_number, year_of_birth);
    }
}