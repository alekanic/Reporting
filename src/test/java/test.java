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

    private void chooseTheMonth(String month_1, String month_2) {

        char ch1 = month_1.charAt(3);
        char ch2 = month_2.charAt(3);
        int monthOne = Character.getNumericValue(ch1);
        int monthTwo = Character.getNumericValue(ch2);

        boolean differentMonth = (monthOne != monthTwo);

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

       open("http://localhost:9999/");

       String selectDate = dateAfterAWeek(8, "dd.MM.yyyy");
       String day = chooseTheDay(selectDate);
       String todayMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

       char ch1 = todayMonth.charAt(4);
       char ch2 = selectDate.charAt(4);
       int monthOne = Character.getNumericValue(ch1) + 3;
       int monthTwo = Character.getNumericValue(ch2);

       $("button").click();

       if (monthOne < monthTwo) {
           $(By.xpath("/html/body/div[2]/div/div/div/div/div/div/div[4]")).click();
       }

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

