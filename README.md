# SCPIScriptTest

**SCPIScriptTest** is a lightweight Java library that enables you to develop and test **SAP CPI Groovy scripts locally**, outside of the SAP Integration Suite environment. It simulates the runtime context and supports value mapping, headers, properties, and attachments.

---

## 🚀 Features

- Execute and test Groovy scripts locally without deploying to SAP CPI.
- Simulate message bodies, headers, properties, and attachments.
- Load value mappings from a `.zip` file.

---

## 📦 How to Use

### 1. Add the Library to Your Project

Include `SCPIScriptTest.jar` in your project's classpath. (in target)

### 2. Example Code

```java
import com.sap.cpi.test.RunScript;
import com.sap.gateway.ip.core.customdev.util.Message;

public class MainClass {

    public static void main(String[] args) throws Exception {

        // Path to the message file (used as input body)
        String messagePath = "./testFile.json";

        // Path to the Groovy script to be tested
        String scriptPath = "./src/com/test/TestScript.groovy";

        // Initialize the script runner
        RunScript runScript = new RunScript(messagePath, scriptPath);

        // Load value mappings if needed
        runScript.loadValuemapping("/VM.zip");

        // Retrieve the message object before executing
        Message message = runScript.getMessage();

        // Set message headers
        message.setHeader("partnerName", "PARTNER1");
        message.setHeader("IDOCType", "MBGMCR");

        // Set exchange properties
        message.setProperty("sourceAgency", "Sender");
        message.setProperty("sourceIdentifier", "CUSTNUM");
        message.setProperty("sourceValue", "VAL");
        message.setProperty("abc", "1234");

        // Execute the script method (default is "processData")
        message = runScript.invokeMethod("processData");

        // Print the transformed message to console
        System.out.println("Transformed message:");
        System.out.println(message.getBody());

        // Optionally write the output to a file
        /*
        FileOutputStream out = new FileOutputStream(messagePath + ".out");
        out.write(message.getBody(String.class).getBytes());
        out.close();
        */
    }
}
```

---

## 🛠 Requirements

- Java 8 or later
- Your Groovy script should conform to the CPI message interface (use `Message` class)

---

## 📁 Sample File Structure

```
your-project/
│
├── SCPIScriptTest.jar
├── testFile.json
├── VM.zip
└── src/
    └── com/
        └── test/
            └── TestScript.groovy
```

---

## 📄 License

Not licensed and open to use.