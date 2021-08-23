package com.jonasermert.amazon;

import com.jonasermert.database.DatabaseConnection;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AmazonSearchPage {

    private WebDriver _driver;
    private List<String> searchResults = new ArrayList<String>();


    public AmazonSearchPage(WebDriver driver){
        this._driver=driver;
    }

    @FindBy(how=How.ID, using="searchDropdownBox") WebElement searchCategoryDropdown;
    @FindBy(how=How.ID, using="twotabsearchtextbox") WebElement searchTextbox;
    @FindBy(how=How.XPATH, using="//*[@id=\"nav-search-submit-text\"]/input") WebElement searchSubmitButton;
    @FindBy(how=How.XPATH, using="//*[@id=\"search\"]//span[@data-component-type='s-search-results']/div[2]/div") List<WebElement> productResultsList;

    public void openAmazonGermany(){
        _driver.get("https://www.amazon.de/");
        _driver.manage().window().maximize();
    }

    public void selectProductCategory(String categoryValue) {
        Select searchCategory = new Select(searchCategoryDropdown);
        searchCategory.selectByValue(categoryValue);
    }

    public void inputSearchText(String searchTextValue) {
        searchTextbox.sendKeys(searchTextValue);
    }

    public void clickSearchButton() {
        searchSubmitButton.click();
    }

    public void getProductLists() {

        String tempattr;
        for(WebElement element:productResultsList) {
            if(element.getAttribute("data-asin") != null && !element.getAttribute("data-asin").isEmpty()) {
                temp = "";
                temp = element.getAttribute("data-asin");
                WebElement productDescription = _driver.findElement(By.xpath("//*[@id=\"search\"]//span[@data-component-type='s-search-results']/div[2]/div[@data-asin='"+temp+"']//h2/a/span"));
                searchResults.add(productDescription.getText());
                //System.out.println(productDescription.getText());
            }
        }

    }

    public void insertProductListDatabase(String categoryValue,String searchTextValue) throws SQLException, ClassNotFoundException {

        Connection con = null;
        try {
            con = DatabaseConnection.initializeDatabase();
            con.setAutoCommit(false);
            int i = 0;

            for(String row : searchResults){
                PreparedStatement st = con.prepareStatement("insert into amazon values(?, ?, ?, ?)");
                st.setInt(1, i);
                st.setString(2, categoryValue);
                st.setString(3, searchTextValue);
                st.setString(4, row);
                //st.setInt(4, Integer.valueOf(request.getParameter("mileage")));
                i++;
                st.executeUpdate();
                System.out.println(row);
                st.close();
            }
            con.commit();
            System.out.println("completed");

        }
        catch(SQLException se){
            // If there is any error.
            con.rollback();
        }
        finally {

            con.close();
        }

    }

    public void closeAmazonGermany() {
        _driver.quit();
    }



}
