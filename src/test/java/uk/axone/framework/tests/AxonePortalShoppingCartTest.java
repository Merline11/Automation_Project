package uk.axone.framework.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import uk.axone.framework.base.BaseTest;

import java.io.FileNotFoundException;

/**
 * Pre-requisites:
 * 1. Navigate to Axone shopping portal: http://seleniumpractice.axonetech.uk/index.php
 * 2. Using your email address, create an account.
 * 3. Make sure you create address for your account.
 *
 * Test steps:
 * 1. Click the “New Products” menu in the footer menu.
 * 2. Click the “Blouse” section.
 * 3. Choose the “White Colour” and click “Add to Cart”.
 * 4. Verify the ‘Product is successfully added to the cart’.
 * 5. Click on ‘Proceed to checkout’ in the pop-up page.
 * 6. Verify the details in ‘Shopping-cart summary’ page.
 * 7. Click on ‘Proceed to checkout’ and verify the ‘Address’ page is pre-populated with your address.
 * 8. Proceed ahead to the next step and verify ‘Shipping details’ page is populated.Agree to Ts&Cs and proceed ahead.
 * 9. Proceed ahead to ‘Payment’ page and choose ‘Pay by check’ and choose ‘Pound’.
 * 10. Click on ‘I confirm my order’.
 * 11. Verify ‘Order Confirmation’ page appears and note down the Order reference.
 * 12. Click on ‘Verify Order history’ link in the confirmation page.
 * 13. Verify ‘Order History’ page shows up the order reference you noted in the previous step.
 */
public class AxonePortalShoppingCartTest extends BaseTest {

    public AxonePortalShoppingCartTest() throws FileNotFoundException {
    }

    @Test
    public void shoppingCartTest() throws InterruptedException {

        test = report.startTest("Axone Shopping Cart Test","Practice portal - First Test");

        logInfo("Launching the browser");
        launchBrowser();
        Thread.sleep(2000);

        logInfo("Navigating to the URL");
        navigateToURL();
        Thread.sleep(2000);

        logInfo("Creating an account by using your email address");
        createAccount();
        Thread.sleep(2000);

        logInfo("1. Click the “New Products” menu in the footer menu");
        clickWebElement("new_products_lnk_xpath");
        Thread.sleep(2000);

        logInfo("2. Click the “Blouse” section.");
        clickWebElement("blouses_section_lnk_xpath");
        Thread.sleep(2000);

        logInfo("3. Choose the “White Colour” and click “Add to Cart”.");
        clickWebElement("white_colour_lnk_id");
        Thread.sleep(2000);
        clickWebElement("add_to_cart_btn_name");
        Thread.sleep(2000);

        logInfo("4. Verify the ‘Product is successfully added to the cart’.");
        Assert.assertEquals(identifyElement("product_success_msg_txt_xpath").getText(),"Product successfully added to your shopping cart");
        Thread.sleep(2000);

        logInfo("5. Click on ‘Proceed to checkout’ in the pop-up page.");
        clickWebElement("proceed_btn_xpath");
        Thread.sleep(2000);

        logInfo("6. Verify the details in ‘Shopping-cart summary’ page.");
        Assert.assertEquals(identifyElement("blouse_in_proceed_page_lnk_xpath").getText(),"Blouse");
        Thread.sleep(2000);

        logInfo("7. Click on ‘Proceed to checkout’ and verify the ‘Address’ page is pre-populated with your address.");
        clickWebElement("proceed_to_checkout_btn_xpath");

        /*Assert.assertEquals(identifyElement("address_firstname_txt_id").getText(),"Merline");
        Thread.sleep(2000);
        Assert.assertEquals(identifyElement("address_lastname_txt_id").getText(),"Jayaraj");
        Thread.sleep(2000);*/

        typeValuesInTextBox("address_first-line_txt_id","123, First Street");
        Thread.sleep(2000);

        typeValuesInTextBox("address_city_txt_id","123, First Street");
        Thread.sleep(2000);

        Select stateDDL = new Select(identifyElement("address_state_select_id"));
        stateDDL.selectByVisibleText("California");

        typeValuesInTextBox("address_postcode_txt_id","12345");
        Thread.sleep(2000);

        typeValuesInTextBox("address_phone_txt_id","123456789");
        Thread.sleep(2000);

        clickWebElement("submitAddress_btn_id");
        Thread.sleep(2000);

        logInfo("8. Proceed ahead to the next step and verify ‘Shipping details’ page is populated. Agree to Ts&Cs and proceed ahead.");
        clickWebElement("process_address_btn_name");
        Assert.assertEquals(identifyElement("shipping_txt_xpath").getText(),"SHIPPING:");
        Thread.sleep(2000);
        getDriver().findElement(By.id(getProp().getProperty("agree_checkbox_id"))).click();

        logInfo("9. Proceed ahead to ‘Payment’ page and choose ‘Pay by check’ and choose ‘Pound’.");
        clickWebElement("proceed_btn_name");
        clickWebElement("pay_by_check_lnk_xpath");
        Select currencyDDL = new Select(identifyElement("currency_payment_select_id"));
        currencyDDL.selectByVisibleText("Pound");

        logInfo("10. Click on ‘I confirm my order’.");
        clickWebElement("confirm_order_btn_xpath");

        logInfo("11. Verify ‘Order Confirmation’ page appears and note down the Order reference.");
        Assert.assertEquals(identifyElement("order_confirmation_page_txt_xpath").getText(),"ORDER CONFIRMATION");
        Thread.sleep(2000);

        String txt = identifyElement("order_reference_txt_xpath").getText();
        int start = txt.indexOf("reference");
        int end = txt.indexOf(".\n");

        String subStr = txt.substring(start + 1, end);
        String order_reference_number=subStr.substring(subStr.indexOf(" ")).trim();

        logInfo("12. Click on ‘Verify Order history’ link in the confirmation page");
        clickWebElement("view_order_history_lnk_css");

        logInfo("13. Verify ‘Order History’ page shows up the order reference you noted in the previous step.");
        Assert.assertEquals(identifyElement("order_reference_lnk_css").getText(),order_reference_number);

        report.endTest(test);
        report.flush();

    }

    @AfterTest
    public void tearDown() {
        getDriver().quit();
    }
}
