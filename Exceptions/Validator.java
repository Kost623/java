package Exceptions;

/*Клас для валідації телефону та email читача.*/
public class Validator
{
    /*Перевіряє формат телефону: дозволяє цифри, '+', '-', '(', ')', пробіли, мінімальна довжина 7 символів.*/
    public static boolean isValidPhone(String phone)
    {
        if (phone == null || phone.trim().isEmpty())
        {
            return false;
        }
        String cleaned = phone.replaceAll("[^0-9+]", "");
        return cleaned.length() >= 7;
    }

    /*Перевіряє формат email: містить '@', точку після '@', не починається ani не закінчується на '@' або '.'.*/
    public static boolean isValidEmail(String email)
    {
        if (email == null || email.trim().isEmpty())
        {
            return false;
        }
        String trimmed = email.trim();
        int atIndex = trimmed.indexOf('@');
        int dotIndex = trimmed.lastIndexOf('.');
        return atIndex > 0
                && dotIndex > atIndex + 1
                && dotIndex < trimmed.length() - 1;
    }
}
