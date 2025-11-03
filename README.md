# Tests_EbookApp_Render
Ebook_App_Render backend and frontend tests.

# Automated Testing Project — Selenium & REST Assured (Java)

This project showcases automated **UI and API tests** for a sample web application,  
built using **Java**, **Selenium WebDriver**, and **REST Assured**.  
It demonstrates practical skills in **test automation**, **framework design**, and **integration testing**.

---

## 🚀 Tech Stack

- **Language:** Java 24
- **UI Tests:** Selenium WebDriver
- **API Tests:** REST Assured
- **Test Runner:** TestNG
- **Assertions:** Hamcrest Matchers in API Tests
- **Build Tool:** Gradle
- **Reporting:** Allure Reports / Extent Reports / screenshots on fail
- **CI/CD:** GitHub Actions 

---
## 🧩 Project Structure

📦 ebook_app_render
├── 📁 src
│   ├── 📁 main
│   │   ├── 📁 java/com/ebook_app_render
│   │   │   ├── 📁 api
│   │   │   └── 📁 ui
│   │   │       ├── pages
│   │   │       └── utils
│   │   └── 📁 resources
│   └── 📁 test
│       ├── 📁 java/com/ebook_app_render/tests
│       │   ├── api
│       │   ├── service
│       │   └── ui
│       └── 📁 resources
├── 📁 reports
├── 📁 screenshots
├── 📄 build.gradle
├── 📄 settings.gradle
└── 📄 README.md

## ⚙️ How to Run the Tests

You can run the tests using **Gradle** from the project root:

```bash
# Run all tests
./gradlew test 

# Run only UI tests
./gradlew uiTests

# Run only API tests
./gradlew apiTests

# Allure Reports, generate them with:
./gradlew allureReport
./gradlew allureServe

# 📊 Test Reports
After execution, reports are available in:
build/reports/tests/test
build/allure-results

### 1. Clone the repository
```bash
git clone https://github.com/AgaGorny42/Tests_EbookApp_Render.git


# 🧪 Test Coverage

## UI Tests (Selenium)
- Verify login and logout functionality  
- Check page navigation and rendering  
- Validate input fields and error messages  
- Test edge case values for date inputs (e.g., start and end of daylight saving time) to ensure the application handles them correctly  

## API Tests (REST Assured)
- Verify that all endpoints work correctly  
- Test creating, reading, updating, and deleting objects in proper order  
- Ensure objects are created and removed without duplicates  
- Check that responses have correct status codes and expected data  

## 🧠 Key Skills Demonstrated
- Automated testing using Selenium and REST Assured  
- Test organization and grouping with TestNG  
- Gradle build configuration and task management  
- Implementation of the Page Object Model (POM) and reusable test utilities  
- Integration of UI and API tests in a unified framework  
- CI/CD pipeline setup with GitHub Actions


👩‍💻 Author
[Agnieszka Górny]
📧 [aga.gorny33@gmail.com]
💼 [https://www.linkedin.com/in/agnieszka-g%C3%B3rny-966b09297/]

⭐ If you find this project interesting, feel free to star the repository!