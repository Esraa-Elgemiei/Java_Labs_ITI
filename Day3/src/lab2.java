import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class lab2 {
    /*
        Your goal is to make a method called betterString that takes
        two Strings and a lambda that says whether the first of the two
        is “better”.
        • The method should return that better String; i.e., if the
        function given by the lambda returns true, the betterString
        method should return the first String, otherwise betterString
        should return the second String.
        • String string1 = ...;
        • String string2 = ...;
        • String longer = StringUtils.betterString(string1, string2, (s1, s2) -> s1.length() > s2.length());
        • String first = StringUtils.betterString(string1, string2, (s1, s2) -> true);

        • Given a String, the task is to check whether a string contains
          only alphabets or not.
        • use isLetter() method of Character class.
     */
    public static String betterString(String s1, String s2, BiPredicate<String,String> better){
        if(better.test(s1,s2)){
            return s1;
        }
        return s2;
    }

    public static Boolean alphabets(String s1, Predicate<String> alphabets){
        return alphabets.test(s1);
    }

    public static void main(String[] args) {
        String string1 = "Esraa";
        String string2 = "hebaaaaaaa";

        String longer = betterString(string1, string2, (s1, s2) -> s1.length() > s2.length());
        System.out.println("longer name: "+longer);

        String first = betterString(string1, string2, (s1, s2) -> true);
        System.out.println("first name: "+first);

        String string3 = "Esraa404";
        String string4 = "Esraa";;

        System.out.println("the word '"+string3+"' is only letters? "+alphabets(string3,s->s.chars().allMatch(Character::isLetter)));
        System.out.println("the word '"+string4+"' is only letters? "+alphabets(string4,s->s.chars().allMatch(Character::isLetter)));

    }
}
