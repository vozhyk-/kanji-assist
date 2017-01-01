#!/usr/bin/env monkeyrunner

#
# Copyright (C) 2016-2017 Witaut Bajaryn, Aleksander Mistewicz
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#### Sets the assistant to Kanji Assist.
#### Assist Settings must be already opened,
#### and a different assistant set.

from com.android.monkeyrunner import MonkeyRunner, MonkeyDevice
import time

device = MonkeyRunner.waitForConnection()

def press_button(button, how=MonkeyDevice.DOWN_AND_UP):
    device.press(button, how)
    time.sleep(0.3)


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