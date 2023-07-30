import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.selector.ByText;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;


public class test {

    private String generateDate(int addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    private String dateAfterAWeek(int addDays, String pattern) {

        String tnt = LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
        return tnt;

    }

    private String chooseTheDay(String tnt) {

        char tntOne = tnt.charAt(0);
        char tntTwo = tnt.charAt(1);

        int numOne = Character.getNumericValue(tntOne);
        int numTwo = Character.getNumericValue(tntTwo);

        if (numOne == 0) {
            return String.valueOf(numTwo);
        } else {
            return (String.valueOf(numOne) + String.valueOf(numTwo));
        }

    }

    @Test

    void positiveTest() {

        String currentDate = generateDate(4, "dd.MM.yyyy");

        open("http://localhost:9999/");

        $("[data-test-id=city] input").setValue("Рязань");
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id=date] input").sendKeys(currentDate);
        $("[data-test-id=name] input").setValue("Василиса Ильинична");
        $("[data-test-id=phone] input").setValue("+79113045566");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $(".notification__content")
                .shouldBe(visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно забронирована на " + currentDate));

    }

   @Test

    void testWithChooseTheCityFromList() {

       open("http://localhost:9999/");
       $("[data-test-id=city] input").setValue("Мо");
       $(withText("Москва")).click();

       String currentDate = generateDate(4, "dd.MM.yyyy");
       $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
       $("[data-test-id=date] input").sendKeys(currentDate);
       $("[data-test-id=name] input").setValue("Василиса Ильинична");
       $("[data-test-id=phone] input").setValue("+79113045566");
       $("[data-test-id=agreement]").click();
       $("button.button").click();
       $(".notification__content")
               .shouldBe(visible, Duration.ofSeconds(15))
               .shouldHave(Condition.exactText("Встреча успешно забронирована на " + currentDate));

   }

   @Test

   void testWithSelectTheDataFromTheCalendar() {

       String selectDate = dateAfterAWeek(8, "dd/MM/yyyy");
       String day = chooseTheDay(selectDate);

       open("http://localhost:9999/");

       $("button").click();
       $$(".calendar__day").filterBy(text(day)).findBy(exactText(day)).click();

       $("[data-test-id=city] input").setValue("Рязань");
       $("[data-test-id=name] input").setValue("Василиса Ильинична");
       $("[data-test-id=phone] input").setValue("+79113045566");
       $("[data-test-id=agreement]").click();
       $("button.button").click();
       $(".notification__content")
               .shouldBe(visible, Duration.ofSeconds(15))
               .shouldHave(Condition.exactText("Встреча успешно забронирована на " + selectDate));

   }
}
