# **Flow Log Parser**

---

## **Overview**

This project is a **Flow Log Parser** that processes AWS VPC Flow Logs and maps each row to a tag based on a lookup table. The lookup table is defined as a CSV file with three columns: `dstport`, `protocol`, and `tag`. The destination port and protocol combination decide what tag is applied to each flow log entry.

The parser supports the **default flow log format** and **only supports version 2** of the flow log. Any other version will be ignored, and an appropriate error will be logged.

---

## **Assumptions**
- **Only version 2** of AWS flow logs is supported. Logs from any other versions will be skipped.
- The input flow log file must be in **ASCII format / Text File that contains entries line by line** and can be up to **10 MB** in size.
- The lookup table is provided in a CSV file with the format: `dstport, protocol, tag`. The lookup is case-insensitive.
- Destination port and protocol are used for tag matching.
- If no valid flow log entries are processed, an error message is logged and written in the report.

---

## **Features**
- Parse flow logs and tag entries based on the lookup table.
- Count occurrences of each tag and report port/protocol combination counts.
- Supports multiple test cases, each with different flow logs and lookup table combinations.
- Provides a summary report as output in **CSV format**.

---

## **Prerequisites**
- **Java 8** or higher
- **Apache Maven for JUnit**

---

## **Project Structure**
Here’s the current project structure:

![Project Structure](src/main/resources/Images/Project_Structure.png)

## **Input Files and Output File Location**
- Files are Under Resources Folder
- to add additional testcases please create the folder in the same location and add necessary inputfiles.
- Please note that flowlog file must be .txt and lookup table must be CSV
![Input and Output File](src/main/resources/Images/InputandOutput.png)



## **How to Run the Project ?**
Main code is written in FlowLogParser.Java File here is the path - (org/example/Parser/LookupTableEntry.java) 

Steps are given below.

