import winsound
import time
import threading

class RepeatedTimer(object):
    def __init__(self, interval, function):
        self.timer     = None
        self.interval   = interval
        self.function   = function
        self.is_running = False

    def run(self):
        self.is_running = False
        self.start()
        self.function()

    def start(self):
        if not self.is_running:
            self.timer = threading.Timer(self.interval, self.run)
            self.timer.start()
            self.is_running = True

    def stop(self):
        self.timer.cancel()
        self.is_running = False



def pulseBeep():
	winsound.Beep(1000,250)

rt = RepeatedTimer(60.0 / (72.0 - 1.0), pulseBeep)
rt.start()
try:
	while True:
		time.sleep(5)
		rt.stop()
		time.sleep(60*60)
		rt.start()
finally:
	rt.stop()