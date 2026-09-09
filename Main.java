import java.util.Scanner;

public class Main 
{
    public static void main(String[] args)
    {

    Scanner var1 = new Scanner(System.in);

            IO.println("якщо книжка 2007 року і старше то ціна на 5% більша");
            IO.println("Ведіть користувача");

        IO.println("Ведіть ім'я читача: ");
        String name_user = var1.nextLine();
        IO.println("Ведіть номер карт: ");
        String card_number = var1.nextLine();
        IO.println("Ведіть номер телефйона: ");
        String phone_number = var1.nextLine();
    
          IO.println("Ведіть книжку");

        IO.println("Ведіть назву книжку: ");
        String name = var1.nextLine();
        IO.println("Ведіть рік випуску: ");
        int year_release = var1.nextInt();
        IO.println("Ведіть ціну книжку: ");
        double price = var1.nextDouble();

            IO.println("Результат");
            double price_final=0;
            if(year_release<=2007)
                {
                    price_final = price * 1.05;
                }
                else
                {
                    price_final=price;
                }
        System.out.printf("Назва книшки %s рік випуску %d ціна книжки %.2f грн %n", name, year_release, price_final);
         System.out.printf("Ім'я користувача %s банківська карта %s номер телефона %s%n", name_user, card_number, phone_number);
    }
}
