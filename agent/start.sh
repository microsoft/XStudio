adb kill-server
adb start-server
# start emulator
# The binary emulator-headless is now retired. Headless builds of the engine are now launched via emulator -no-window, thus unifying the previously separate (but similar) paths.
nohup emulator -avd emuTestPixel -noaudio -no-boot-anim -gpu off -no-window -qemu -machine virt &

java -jar /app.jar --spring.profiles.active=docker