package com.jonasermert;

import java.sql.SQLException;

import com.jonasermert.amazon.AmazonSearchPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

public class AmazonSearch {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver","/home/Jonas Ermert/Dokumente/Java programmieren/chromedriver");
        WebDriver driver = new ChromeDriver();

        AmazonSearchPage searchPage = PageFactory.initElements(driver, AmazonSearchPage.class);
        searchPage.openAmazonGermany();
        searchPage.selectProductCategory("search-alias=electronics");
        searchPage.inputSearchText("Java");
        searchPage.clickSearchButton();
        searchPage.getProductLists();
        try {
            searchPage.insertProductListDatabase("search-alias=electronics","Java");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        searchPage.closeAmazonGermany();

    }

}