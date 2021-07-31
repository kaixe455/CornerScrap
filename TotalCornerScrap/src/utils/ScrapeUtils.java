package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

public class ScrapeUtils {
	@SuppressWarnings("deprecation")
	public WebDriver login(String url,String [] listauserAgent) {
		try {
		ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("resources/drivers/phantomjs.exe");
        File f = new File("Driver");
        if (!f.exists()) {
            f.mkdirs();
        }
        File chromeDriver = new File("Driver" + File.separator + "phantomjs.exe");
        if (!chromeDriver.exists()) {
            chromeDriver.createNewFile();
            org.apache.commons.io.FileUtils.copyURLToFile(resource, chromeDriver);
        }
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        Random r = new Random();
         String userAgent = listauserAgent[r.nextInt(listauserAgent.length)];
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_PAGE_SETTINGS_PREFIX + "userAgent", 
        		userAgent);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, chromeDriver.getAbsolutePath());
        PhantomJSDriver driver = new PhantomJSDriver(caps);
        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get(url);
        WebElement loginbtn = driver.findElement(By.xpath("//a[@data-modal-href='/user/login']"));
        loginbtn.click();
        WebDriverWait wait = new WebDriverWait(driver, 2000);
        wait.withTimeout(30, TimeUnit.SECONDS).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"inputEmail3\"]")));
        WebElement username=driver.findElement(By.id("inputEmail3"));
        WebElement password=driver.findElement(By.id("inputPassword3"));
        List<WebElement> login= driver.findElements(By.xpath("//div/button[@type='submit']"));
        username.sendKeys("Morigeri");
        password.sendKeys("Morigeri80");
        WebElement loginBotonReal = login.get(2);
        loginBotonReal.click();
        WebElement seleccionarNumeroPartidos = driver.findElement(By.id("match_stats_match_count"));
        Select selectorPartidos = new Select(seleccionarNumeroPartidos);
        selectorPartidos.selectByValue("10");          
//        saveCookies(driver);
//        List <Cookie> cookies =readCookies();
//		for (Cookie ck : cookies) {
//			driver.manage().addCookie(ck);
//		}
        return driver;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
        
        
    }
	public void getDatosCorner (WebDriver driver,String partido) {
		List <Cookie> cookies =readCookies();
		for (Cookie ck : cookies) {
			driver.manage().addCookie(ck);
		}
		driver.navigate().to(partido);
	}
	public static void saveCookies (WebDriver driver) {
		File file = new File("Cookies.data");							
        try		
        {	  
            // Delete old file if exists
			file.delete();		
            file.createNewFile();			
            FileWriter fileWrite = new FileWriter(file);							
            BufferedWriter Bwrite = new BufferedWriter(fileWrite);							
            // loop for getting the cookie information 		
            	
            // loop for getting the cookie information 		
            for(Cookie ck : driver.manage().getCookies())							
            {			
                Bwrite.write((ck.getName()+";"+ck.getValue()+";"+ck.getDomain()+";"+ck.getPath()+";"+ck.getExpiry()+";"+ck.isSecure()));																									
                Bwrite.newLine();             
            }			
            Bwrite.close();			
            fileWrite.close();	
            
        }
        catch(Exception ex)					
        {		
            ex.printStackTrace();			
        }
	}
	@SuppressWarnings("deprecation")
	public static List<Cookie> readCookies () {
		List<Cookie> listaCookies = new ArrayList<Cookie>();
		 try{			
		     
		        File file = new File("Cookies.data");							
		        FileReader fileReader = new FileReader(file);							
		        BufferedReader Buffreader = new BufferedReader(fileReader);							
		        String strline;			
		        while((strline=Buffreader.readLine())!=null){									
		        StringTokenizer token = new StringTokenizer(strline,";");									
		        while(token.hasMoreTokens()){					
		        String name = token.nextToken();					
		        String value = token.nextToken();					
		        String domain = token.nextToken();					
		        String path = token.nextToken();					
		        Date expiry = null;					
		        		
		        String val;			
		        if(!(val=token.nextToken()).equals("null"))
				{
		        	DateFormat dateFormat = new SimpleDateFormat(
		                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		        System.out.println(dateFormat.parse(val));
		        	 expiry= dateFormat.parse(val);  				
		        }		
		        Boolean isSecure = new Boolean(token.nextToken()).								
		        booleanValue();		
		        Cookie ck = new Cookie(name,value,domain,path,expiry,isSecure);			
		        System.out.println("cookies guardadas!");
		        listaCookies.add(ck);
		        }		
		        }		
		        }catch(Exception ex){					
		        	ex.printStackTrace();
		        }
		return listaCookies;
	}

}
