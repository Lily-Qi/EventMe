# EventMe

This is an instruction on how to run EventMe on an emulator.

## Select Emulator
If your emulator is not Pixel 2 with API 24, please follow the following steps. If you already have it, then directly jump to next session

1. Select "Tools", then select "Device Manager", choose Phone with "Pixel 2", click "Next"
2. choose “Nougat API 24 Android 7.0 x86”, click "Next" and then "Finish"
3. A new AVD is created

## Emulator Setting

1. Click “Run ‘app’” to run the emulator first
2. Above the emulator, there is a button with three dots, which is "Extended Controls", click it
3. Complete following steps: 
    1. In the Extended Controls, choose "Location", in search bar, input "3607 Trousdale Pkwy, Los Angeles, CA 90089"
    2. Select the position and click "SAVE POINT" on the popup window, cick "OK"
    3. In the "Saved points" session, select the point you just saved, then click "SET LOCATION"
    1. Still in the Extended Controls, choose "Google Play"
    2. Click "Update"
    3. On the emulator, follow the instructions to update Google Play services
4. Close the emulator by clicking the "x" on the emulator tab
5. Click “Run ‘app’” to restart the emulator
6. EventMe is running

## Running App
When EventMe is running: 
1. Please allow EventME to access this device's location when there is a pop-up window asks for it, in order to use all functionalities of the App. 
2. When you want to search by keyword, please click on the magnifier icon on the textfied first in order to input text, and after inputing, please click the magnifier on the keyboard to start searching.

## Improvements
1. Added the estimated cost to all events in the form of numbers, instead of just showing the dollar signs. It could be seen in the event box and event registration page.
2. Added the user ratings for all events in the form of stars. It could be seen in the event box and event registration page.
3. Added the extra feature route panning. When you click on a marker on the map, it will show the bottom sheet as well as the route from your current location to the marker. You can swipe down to hide the bottom sheet to see the route.
4. Fixed the bug for distances showed in User's list 
5. Changed the back button in bottomsheet from going to explore page to going back to the map.(you can still swipe down the bottomsheet to go back to the map as before).
6. Fixed the sorting of the events in the same day.
7. Added the functionality to sort evnts by numbers of registerer users
