package com.browserstack;
import com.browserstack.local.Local;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parallelized.class)
public class BrowserStackJUnitTest {
    public WebDriver driver;
    private Local l;
    private static JSONObject config;
    public String url = System.getProperty("url");

    @Parameter(value = 0)
    public int taskID;

    @Parameters
    public static Iterable<? extends Object> data() throws Exception {
        JSONParser parser = new JSONParser();
        config = (JSONObject) parser.parse(new FileReader("src/test/resources/conf/" + System.getProperty("config")));
        int envs = ((JSONArray)config.get("environments")).size();

        List<Integer> taskIDs = new ArrayList<Integer>();
        for(int i=0; i<envs; i++) {
            taskIDs.add(i);
        }

        return taskIDs;
    }

    
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String username = System.getenv("BROWSERSTACK_USERNAME");


        String accessKey = System.getenv("BROWSERSTACK_ACCESS_KEY");

        String browserStackBrowser = System.getProperty("browser");
        capabilities.setCapability("browser",browserStackBrowser);
        

        if(capabilities.getCapability("browserstack.local") != null && capabilities.getCapability("browserstack.local") == "true"){
            l = new Local();
            Map<String, String> options = new HashMap<String, String>();
            options.put("key", accessKey);
            l.start(options);
        }
     System.out.println(capabilities);
        driver = new RemoteWebDriver(new URL("https://"+username+":"+accessKey+"@hub-cloud.browserstack.com/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
        if(l != null) l.stop();
    }
}
