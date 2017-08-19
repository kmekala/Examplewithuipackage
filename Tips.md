                                                     
```
Objective of BusinessLayer and Pegaspecific generic classes

    1. While automating an application, we come across multiple generic functions.
    Those methods which are applicable to any PRPC application and reusable are mapped under Pegaspecific generic methods.

    2. Those methods which are single PRPC application-specific, these form the major business flow(unlike the small code snippets seen in the Pages class) and form a prominent part of most of the
    scenarios are classified under Business Layer.
```

```Locator Priority Order for elements```
ID
CSS_SELECTOR
XPATH
Incase of links, linktext is the highest priority locator


```Tips for CSS Selectors```

# Use this tool to validate the xpaths
http://xpathvalidator.projectquantum.io/
>>Make sure the score for locator is >80 for it to be included

>>  The generic way of locating elements by their attribute in CSS Selectors is
    css = element_name[<attribute_name>='<value>']

    Example:<input type="text" id="firstname" name="first_name" class="myForm">
    CSS SELECTOR =>   input[id='firstname']

>>  In CSS, we can use # notation to select the id:
    css="input#firstname" or just css="#firstname"

>>  We can also use the . notation to select the class
    css="input.myForm"

>>  An element can be located with more than one attribute

    Example:<div class="ajax_enabled" style="display:block">
    css="div[class='ajax_enabled'] [style='display:block']"

>> Locating Child Element

   Example:	<div id="child"><img src="./logo.png"></div>
   css="div#child img"

>> Selecting from multiple child elements
Example:  <ul id="fruit">
             <li>Apple</li>
             <li>Orange</li>
             <li>Banana</li>
          </ul>

css="ul#fruit li:nth-of-type(2)"

>>Attribute NOT contain a specific value

Example: <div class="day past calendar-day-2017-02-13 calendar-dow-1 unavailable">
         <div class="day today calendar-day-2017-02-14 calendar-dow-2 unavailable">
         <div class="day calendar-day-2017-02-15 calendar-dow-3">
         <div class="day calendar-day-2017-02-16 calendar-dow-4">

css = "div[class*='calendar-day-']:not([class*='unavailable'])"


````Other generic issues faced during setup or execution````

1. A selenium script is put to execution and see a blank browser is opened, execution is halted and the url in the blank browser says "Not secure | Data"

    Solution:- Compatibility issue
    Java – jdk1.8; Chrome browser n driver – Latest version
    Selenium – 3.0.1
    Install new version of Chrome Driver (2.29) and also ensure your chrome browser (57) is also the latest.

2. Classpath contains multiple (any feature) bindings

    Example :-  Classpath contains multiple SLF4J bindings
    Solution:- Remove the dependency tag from the pom file or add <exclusions >, <exclusion> tags for the dependency.
In the .m2 file under the org folder, remove the slf4j related jar files and then do a maven clean install and then execute.
Command is:- mvn clean install
[Path to .m2 filefolder => under C folder Users\username\.m2\repository]

3. java.lang.IllegalStateException: The driver executable is a directory: C:\Workspace\SSOAutoTests\ChromeDriver\chromedriver_win32
    [Framework related…]

    Solution:- Update the chrome driver path in the .properties file as shown below
    chrome.driver.path=C:\\Workspace\\SSOAutoTests\\ChromeDriver\\chromedriver_win32\\chromedriver.exe
    Note: - double slash has to be used for Windows System.

4. log4j:WARN No appenders could be found for logger (org.apache.http.client.protocol.RequestAddCookies).
   log4j:WARN Please initialize the log4j system properly

   Solution:- Add a log4j properties file under resources folder with the necessary parameters
   and initialise them appropriately[under the Source root of your project]

Maven commman 'mvn dependency:tree' - To find the clash in the dependencies.
