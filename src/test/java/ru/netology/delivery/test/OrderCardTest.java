package ru.netology.delivery.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class OrderCardTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    @DisplayName("Should success ful plan meeting")
    void shouldSuccessFulPlanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var dayToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(dayToAddForFirstMeeting);
        var daysSecondMeeting = 8;
        var secondMeetingDate = DataGenerator.generateDate(dayToAddForFirstMeeting);
        $("[data-test-id='city'] [placeholder='Город']").setValue(validUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [placeholder='Дата встречи']").setValue(firstMeetingDate);
        $("[data-test-id='name'] [name='name']").setValue(validUser.getName());
        $("[data-test-id='phone'] [name='phone']").setValue(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $(byText("Запланировать")).click();
        $(byText("Успешно!")).shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + firstMeetingDate))
                .shouldBe(visible);
        $("[data-test-id='date'] [placeholder='Дата встречи']").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] [placeholder='Дата встречи']").setValue(secondMeetingDate);
        $(byText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"))
                .shouldBe(visible);
        $("[data-test-id='replan-notification'] button").click();
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + secondMeetingDate))
                .shouldBe(visible);
    }
}
