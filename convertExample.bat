@echo off

REM Get File protocol URL, otherwise default. Note the percent escape for percent.
set PROJECT_URL=file:///c:/Users/Asus%%20i7/IBM/IIBT10/workspace/1800Challenge
if not "%1"=="" ( 	
SET "PROJECT_URL=%1" 
) else (echo using default file URL: %PROJECT_URL%)
pause type key to continue conversion...
set RESOURCES=%PROJECT_URL%/src/main/java/resources
set PHONE=%RESOURCES%/phoneNumbers.txt
set DICTIONARY=%RESOURCES%/DigitToCharacterMap.txt
set JAR=1800Challenge-0.0.1-SNAPSHOT.jar
echo ... reading from dictionary file (%DICTIONARY%)
echo ... reading from phone file (%PHONE%)
java -DphoneNumberFile=%PHONE% -Ddictionary=%DICTIONARY% -jar %JAR%  mnemonic.NumberToLetterConverter
echo reading from stdin...
java -Ddictionary=%DICTIONARY% -jar %JAR% mnemonic.NumberToLetterConverter 2255.63
echo no results from the follow number (21055-63)
java -Ddictionary=%DICTIONARY% -jar %JAR% mnemonic.NumberToLetterConverter 21055-63