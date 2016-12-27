#!/usr/bin/env monkeyrunner

#### Sets the assistant to Kanji Assist.
#### Assist Settings must be already opened,
#### and a different assistant set.

from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import time

device = MonkeyRunner.waitForConnection()

def press_button(button, how=MonkeyDevice.DOWN_AND_UP):
    device.press(button, how)
    time.sleep(0.1)


# Assistant
press_button("KEYCODE_DPAD_DOWN")
press_button("KEYCODE_DPAD_CENTER")

# Kanji Assist
press_button("KEYCODE_DPAD_DOWN")
press_button("KEYCODE_DPAD_CENTER")

# Agree
press_button("KEYCODE_DPAD_RIGHT")
press_button("KEYCODE_DPAD_CENTER")

# Close settings dialog and MainActivity
press_button("KEYCODE_BACK")
press_button("KEYCODE_BACK")