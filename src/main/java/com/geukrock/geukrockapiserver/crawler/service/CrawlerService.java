package com.geukrock.geukrockapiserver.crawler.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMeetingDto;
import com.geukrock.geukrockapiserver.crawler.dto.CrawledMemberDto;
import com.geukrock.geukrockapiserver.meeting.entity.Meeting;
import com.geukrock.geukrockapiserver.member.entity.Member;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CrawlerService {

    private final String geukrockUrl = "https://www.somoim.co.kr/e6104fa4-3080-11ef-9fe1-0a31a2f27e3f1";

    public CrawlerService() {
        WebDriverManager.chromedriver().setup();
    }

    private WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        return new ChromeDriver(options);
    }

    public List<CrawledMemberDto> getMembers() {
        WebDriver driver = getDriver();
        driver.get(geukrockUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 모임 멤버 더보기 버튼 누르기
        WebElement moreButton = wait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'모임 멤버 더보기')]")));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", moreButton);

        WebElement memberSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//section[h2[contains(text(),'모임 멤버')]]")));

        WebElement h2 = memberSection.findElement(By.xpath(".//h2[contains(text(),'모임 멤버')]"));
        String targetText = h2.getText();
        int memberCount = Integer.parseInt(targetText.replaceAll("\\D+", ""));

        // 모든 맴버 로딩 완료
        wait.until((driver1 -> {
            int size = memberSection.findElements(By.cssSelector("div.flex.flex-col.cursor-pointer")).size();
            return size >= memberCount;
        }));
        log.info("맴버 로딩 완료: {}", memberCount);

        List<WebElement> memberDivs = memberSection.findElements(By.cssSelector("div.flex.flex-col.cursor-pointer"));
        List<CrawledMemberDto> members = new ArrayList<>();
        for (WebElement memberDiv : memberDivs) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", memberDiv);
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.fixed.z-50.bg-white.rounded-2xl.shadow-lg")));

            CrawledMemberDto newMember = new CrawledMemberDto();

            // 프로파일 이미지 url 추출
            WebElement profileElement = modal.findElement(By.xpath(".//img[@alt='member face']"));

            String profileUrl = profileElement.getDomAttribute("src");
            int lastDotIndex = profileUrl.lastIndexOf(".");
            // 프로필 url 사이즈를 정하는 값이 있는데 이것을 제거하고 저장
            profileUrl = profileUrl.substring(0, lastDotIndex - 1) + profileUrl.substring(lastDotIndex);
            newMember.setProfileUrl(profileUrl);
            log.info("profileUrl: {}", profileUrl);

            // 이름 추출
            String somoimName = modal.findElement(By.cssSelector("p.font-bold.font-apple.text-lg.text-fc_black"))
                    .getText();
            log.info("somoimName: {}", somoimName);
            newMember.setSomoimName(somoimName);

            // 생년월일 추출
            String birthDate = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                            "//p[contains(@class, 'text-gray-500') and contains(@class, 'text-sm')]/span[last()]")))
                    .getText();
            log.info("birthDate: {}", birthDate);
            try {
                LocalDate localDate = LocalDate.parse(birthDate,
                        DateTimeFormatter.ofPattern("yyyy. M. d"));
                newMember.setBirthDate(localDate);
            } catch (DateTimeParseException e) {
                log.error("날짜 형식 파싱 실패: {}\n{}", birthDate, e);
            }
            members.add(newMember);
            modal.findElement(By.xpath(".//button[img[@alt='close']]")).click();
        }
        return members;
    }

    public void getTodayMeetingInfo() {
        WebDriver driver = getDriver();
        driver.get(geukrockUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * 오늘자 정모 크롤링
     */
    public List<CrawledMeetingDto> getMeetings() {
        WebDriver driver = getDriver();
        driver.get(geukrockUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement sectionElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//section[h2[contains(text(),'정모 일정')]]")));
        WebElement sectionTitle = sectionElement.findElement(By.xpath("h2[contains(text(),'정모 일정')]"));
        int meetingCount = Integer.parseInt(sectionTitle.getText().replaceAll("\\D+", ""));
        log.info("meetingCount: {}", meetingCount);
        wait.until((driver1) -> {
            int size = sectionElement.findElements(By.cssSelector("div.flex.space-x-3")).size();
            return size >= meetingCount;
        });

        List<WebElement> meetingElements = sectionElement.findElements(By.cssSelector("div.flex.space-x-3"));
        List<CrawledMeetingDto> meetings = new ArrayList<>();
        for (WebElement meetingElement : meetingElements) {
            String title = meetingElement.findElement(By.xpath(".//h3")).getText();
            log.info("title: {}", title);

            String locationText = meetingElement.findElement(
                    By.xpath(".//img[contains(@src, 'i_location2.svg')]/../../p")).getText();
            log.info("locationText: {}", locationText);

            String dateText = meetingElement.findElement(By.xpath(
                    ".//div[.//img[contains(@src, 'i_clock.svg')]]//p")).getText();
            log.info("dateText: {}", dateText);

            LocalDate localDate = null;
            // 오늘/내일/모레 처리
            if (dateText.contains("오늘")) {
                localDate = LocalDate.now();

                // 이후 정모 참석 맴버 확인
                List<WebElement> meetingMemberElements = meetingElement
                        .findElements(By.xpath(".//img[contains(@alt, 'member face')]"));

                List<String> profileUrls = new ArrayList<>();
                for (WebElement meetingMemberElement : meetingMemberElements) {
                    String profileUrl = meetingMemberElement.getDomAttribute("src");
                    int lastDotIndex = profileUrl.lastIndexOf(".");
                    // 프로필 url끝에 사이즈 지정하는 값이 있는데 이것을 제거하기
                    profileUrl = profileUrl.substring(0, lastDotIndex - 1) + profileUrl.substring(lastDotIndex);
                    profileUrls.add(profileUrl);
                    log.info("profileUrl: {}", profileUrl);
                }
                meetings.add(new CrawledMeetingDto(title, localDate, locationText,profileUrls));
                break;
            }
        }
        return meetings;
    }
}
