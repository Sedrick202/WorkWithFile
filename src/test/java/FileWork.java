import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import io.qameta.allure.Owner;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.qameta.allure.selenide.AllureSelenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import org.junit.jupiter.api.function.Executable;


public class FileWork {
    @BeforeAll
    static void beforeAll() {

        Configuration.startMaximized = true;
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @Test
    @Owner("Nikita Eltsov")
    @DisplayName("Проверка загрузки файлов")
    void checkUploadFile(){
        open("http://file.karelia.ru");
        $("#file_0").uploadFromClasspath("test.txt");
        $("#file_submit").click();
        $("#fileQueue").shouldHave(text("test.txt"));
    }
    @Test
    @Owner("Nikita Eltsov")
    @DisplayName("Проверка скачивания и содержимого ")
    void checkDownloadTxtFile() throws IOException {
        open("https://github.com/Sedrick202/lessonPromming/blob/master/.gitignore");
        File download = $("#raw-url").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("# Compiled class file"));

    }

    @Test
    @Owner("Nikita Eltsov")
    @DisplayName("Проверка загрузки и содержимого pdf ")
    void checkDownloadPDFFile() throws IOException {
        open("https://file-examples.com/index.php/sample-documents-download/sample-pdf-download/");
        File pdf = $(byXpath("//*[@id=\"table-files\"]/tbody/tr[3]/td[5]/a[1]")).download();
        PDF parsedPdf = new PDF(pdf);
        Assertions.assertEquals(30,parsedPdf.numberOfPages);
    }
    @Test
    @Owner("Nikita Eltsov")
    @DisplayName("Проверка загрузки и содержимого xls ")
    void checkDownloadXLSFile() throws IOException {
        open("https://file-examples.com/index.php/sample-documents-download/sample-xls-download/");
        File xls = $(byXpath("//*[@id=\"table-files\"]/tbody/tr[1]/td[5]/a[1]")).download();
        XLS parsedXls = new XLS(xls);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(2)
                .getCell(1)
                .getStringCellValue()
                .contains("Mara");

        assertTrue(checkPassed);
    }


}
