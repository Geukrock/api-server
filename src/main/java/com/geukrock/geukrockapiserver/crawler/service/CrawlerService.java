package com.geukrock.geukrockapiserver.crawler.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import com.geukrock.geukrockapiserver.member.dto.CrawledMemberDto;
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

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
            WebElement profileElement =  modal.findElement(By.xpath(".//img[@alt='member face']"));

            String profileUrl = profileElement.getDomAttribute("src");
            int lastDotIndex = profileUrl.lastIndexOf(".");
            profileUrl = profileUrl.substring(0, lastDotIndex - 1) + "t" + profileUrl.substring(lastDotIndex);
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

    // 오늘 출석한 인원 가져오기
    public List<CrawledMemberDto> getTodayParticipants(){
        return null;
    }

    public void getTodayMeetingInfo(){
        WebDriver driver = getDriver();
        driver.get(geukrockUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        
    }   

    public String getSomoimHtml() {
        WebDriver driver = getDriver();
        driver.get(geukrockUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

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
        Map<String, Member> memberMap = new HashMap<>();

        for (WebElement memberDiv : memberDivs) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", memberDiv);
            WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.fixed.z-50.bg-white.rounded-2xl.shadow-lg")));

            WebElement profileElement = modal.findElement(By.xpath("//img[@alt='member face']"));
            Member newMember = new Member();

            // 프로파일 이미지 url 추출
            String profileUrl = profileElement.getDomAttribute("src");
            int lastDotIndex = profileUrl.lastIndexOf(".");
            profileUrl = profileUrl.substring(0, lastDotIndex - 1) + "t" + profileUrl.substring(lastDotIndex);
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
            memberMap.put(newMember.getProfileUrl(), newMember);
            modal.findElement(By.xpath("//button[img[@alt='close']]")).click();
        }

        // // 맴버 맵으로 파싱
        // Map<String, String> memberMap = new HashMap<>();
        // for (WebElement memberDiv : memberDivs) {
        // ((JavascriptExecutor) driver).executeScript("arguments[0]",memberDiv);

        // WebElement nameSpan =
        // memberDiv.findElement(By.cssSelector("span.text-fc_black"));
        // String memberName = nameSpan.getText();

        // String src = memberDiv.findElement(By.tagName("img")).getDomAttribute("src");
        // int lastDotIndex = src.lastIndexOf('.');
        // src = src.substring(0,lastDotIndex -1) + "t" + src.substring(lastDotIndex);

        // if (!src.equals("/default_face.png")) {
        // memberMap.put(src, memberName);
        // }
        // }

        // // 맴버 생년월일 파싱

        // // 정모 참석자 맴버 파싱
        // WebElement meetingSection =
        // wait.until(ExpectedConditions.visibilityOfElementLocated(
        // By.xpath("//section[h2[contains(text(),'정모 일정')]]")));

        // List<WebElement> meetingElements =
        // meetingSection.findElements(By.cssSelector("div.flex.space-x-3"));

        // for (WebElement meetingElement : meetingElements) {
        // WebElement container = meetingElement.findElement(
        // By.xpath(".//div[.//img[contains(@src, 'i_clock.svg')]]"));
        // String pText = container.findElement(By.tagName("p")).getText();
        // String date = pText.split(" ")[0];

        // // 오늘자 정모만 처리하기
        // if (!date.equals("오늘")) {
        // continue;
        // }
        // List<WebElement> meetingMemberDivs =
        // meetingElement.findElements(By.cssSelector("div.relative.w-\\[25px\\].h-\\[25px\\]"));
        // for (WebElement div : meetingMemberDivs) {
        // String key = div.findElement(By.tagName("img")).getDomAttribute("src");
        // String memberName = memberMap.get(key);
        // log.info("key: {}", key);
        // log.info("이름: {}", memberName);
        // }
        // }

        // driver.quit();
        return null;
    }
}
