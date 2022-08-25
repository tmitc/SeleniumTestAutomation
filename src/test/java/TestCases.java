import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestCases 
{
	protected WebDriver driver; //protected WebDriver variable will be be accessed by subclasses
	
	//Tests Cases
	
	//General site test cases
	@Test
	public void testWebPageTitle()
	{
		String title = driver.getTitle();
		
		//the title of flash card web page is blank for now so this should be true....
		assertEquals("", title);
	}

	//Cards page test cases
	@Test
	public void firstCardIsClickableTest()
	{
		boolean isClickable = true;
		try
		{
		// Explicitly wait until element becomes clickable - timeout after 10 seconds
		  new WebDriverWait(driver, Duration.ofSeconds(5))
		          .until(ExpectedConditions.elementToBeClickable(By.id("OOP1")));
		}
		catch (TimeoutException e)
		{	
			//if times out (never becomes clickable)
			isClickable = false;
		}
		finally
		{	//runs whether exception is thrown or not
			assertTrue(isClickable);
		}
	}
	
	//Quiz page test cases
	@Test
	public void allQuizQuestionsAccessableTest()
	{
		boolean accessible = false; // this field will only become true is we are able to navigate threw the full quiz
		
		try
		{
			// Explicitly wait until quiz side-bar button becomes clickable - timeout after 10 seconds
			WebElement element1 = new WebDriverWait(driver, Duration.ofSeconds(5))
			          .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/quiz']")));
						
			//click quiz side-bar button
			element1.click();	
			
			//for each quiz question
			for (int i = 0; i < 24; i++)
			{
				//wait for quiz question to become visible - timeout after 10 secs
				WebElement questionElement = new WebDriverWait(driver, Duration.ofSeconds(5))
				          .until(ExpectedConditions.visibilityOfElementLocated(By.className("quiz-answer")));
				questionElement.click();
				driver.findElement(By.id("quiz-next-button")).click();
			}
			
			//wait for last quiz question to become visible then click submit
			WebElement questionElement = new WebDriverWait(driver, Duration.ofSeconds(5))
			          .until(ExpectedConditions.visibilityOfElementLocated(By.className("quiz-answer")));
			questionElement.click();
			driver.findElement(By.id("quiz-submit-button")).click();//submit quiz
			
			//wait for try again button to appear and then click
					WebElement questionElement1 = new WebDriverWait(driver, Duration.ofSeconds(5))
					          .until(ExpectedConditions.visibilityOfElementLocated(By.id("quiz-tryagain-button")));
					questionElement1.click();
			
			// Explicitly wait until first quiz question becomes visible, confirm title is not blank
			questionElement1 = new WebDriverWait(driver, Duration.ofSeconds(5))
			          .until(ExpectedConditions.visibilityOfElementLocated(By.className("quiz-question")));
			// Copy text from the first question element;
			String questionText = questionElement.getText();
			// passes test if our question element has text.
			
			//if driver was able to complete the quiz and make back to the first question, the test passes (accessible = true)
			if(questionText != null)
			{
				accessible = true;
			}
		}
		
		//if quiz times out, this will be triggered
		catch(TimeoutException e)
		{
			System.out.println("Error: Webelement search timeout");
			accessible = false;
		}
		//whether the quiz passed or failed or any exception thrown, this will trigger at the end no matter what
		finally
		{
			assertTrue(accessible);
		}
	}
	
	@Test
	public void firstQuizQuestionAppearsTest()
	{
		try 
		{
			// Explicitly wait until quiz side-bar button becomes clickable - timeout after 10 seconds
			WebElement element1 = new WebDriverWait(driver, Duration.ofSeconds(5))
			          .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/quiz']")));
			
			//click quiz side-bar button
			element1.click();
			
			// Explicitly wait until first quiz question becomes visible - timeout after 10 seconds
			WebElement questionElement = new WebDriverWait(driver, Duration.ofSeconds(5))
			          .until(ExpectedConditions.visibilityOfElementLocated(By.className("quiz-question")));
			// Copy text from the first question element;
			String questionText = questionElement.getText();
			// passes test if our question element has text.
			assertTrue(questionText != null);
		}
		catch(TimeoutException e) //wraps our whole test case in try-catch so it also fails our test if any timeouts happen(if any element are not found)
		{
			System.out.println("Driver timeout: Element not found!\n");
			e.printStackTrace();
			assertTrue(false);
		}
	}
}