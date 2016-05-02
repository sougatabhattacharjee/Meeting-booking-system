# Meeting Booking System

```
System            : Linux/Windows
Programming       : Java SDK 7/8, Apache Maven 3.2.3
Development Tools : IntelliJ IDEA 2016.1
Contributor       : @sougatabhattacharjee
```

## Overview
Implement a new system for processing batches of booking requests for the meeting.

### Input Format
The system must process input as text.
* The first line of the input text represents the company office hours, in 24 hour clock format
* The remainder of the input represents individual booking requests. Each booking request is in the following format.
  * [request submission time, in the format YYYY-MM-DD HH:MM:SS] [ARCH:employee id]
  * [meeting start time, in the format YYYY-MM-DD HH:MM] [ARCH:meeting duration in hours]

### Sample Input
||
| --------|
| 0900 1730 |
| 2015-08-17 10:17:06 EMP001|
| 2015-08-21 09:00 2|
| 2015-08-16 12:34:56 EMP002|
| 2015-08-21 09:00 2|
| 2015-08-16 09:28:23 EMP003|
| 2015-08-22 14:00 2|
| 2015-08-17 11:23:45 EMP004|
| 2015-08-22 16:00 1|
| 2015-08-15 17:29:12 EMP005|
| 2015-08-21 16:00 3|

### Output
||
| --------|
| 2015-08-21 |
| 09:00 11:00 EMP002|
| 2015-08-22|
| 14:00 16:00 EMP003|
| 16:00 17:00 EMP004|

### Output Constraints
* No part of a meeting may fall outside office hours.
* Meetings may not overlap.
* The booking submission system only allows one submission at a time, so submission times are guaranteed to be unique.
* Bookings must be processed in the chronological order in which they were submitted.
* The ordering of booking submissions in the supplied input is not guaranteed.

## Implementation Steps
* Read the input file and validate the content structure
* Initial filtering, discard booking requests which are falling outside of office hours
* Rest of the data stored into array and sorted by the submission time
* Implement and used [interval tree](https://en.wikipedia.org/wiki/Interval_tree) for comparing the meeting time intervals
* Insert all the data from the sorted array to the interval tree and discard values which are overlapping
* Traverse the tree in Inorder way to retrieve the final list of data
* Convert the the list of data into final output format
* Initial sorting and insertion into interval tree takes O(nlogn) time
* Traversing the tree takes O(n) time
* Finally unit test cases are added to check the implementations

## Compile/Build
Go to the project directory and open console or terminal.
* In console, type ``mvn package``
* If you need cleaning the dependency jars and copying to the project artifact, type <br />
``mvn clean dependency:copy-dependencies package``

## Run
After building the project successfully, type <br />
``java -cp target/MeetingBooking-1.0-SNAPSHOT.jar com.mls.booking.Main``  <br />
 Then an input text file name should be provided. Only text files with (.txt) extensions are allowed.

## TODO
* Dockerized the whole project or deploy to Heroku
* UUID generation to verify uniqueness
* Mock tests to check the interval tree functionality 
* Test the performance and correctness of the program with large datasets
