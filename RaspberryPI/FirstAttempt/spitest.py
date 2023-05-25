import spidev, time

spi1 = spidev.SpiDev()
spi2 = spidev.SpiDev()

spi1.open(0,0)
spi2.open(1,0)
spi1.max_speed_hz = 19530000
spi2.max_speed_hz = 19530000

def analog_read(Channel):
  r = spi1.xfer2([1,(8+channel)<<4,0])
  adc_out = ((r[1]&3)<<8)+r[2]
  return adc_out

def analog_read2(Channel):
  r = spi2.xfer2([1,(8+channel)<<4,0])
  adc_out = ((r[1]&3)<<8)+r[2]
  return adc_out

while True:
  reading1 = analog_read(0)
  voltage1 = reading1*3.3/1024
  
  reading2 = analog_read(1)
  voltage2 = reading2*3.3/1024
  
  reading1_1 = analog_read2(0)
  voltage1_1 = reading1_1*3.3/1024
  
  print("1:Reading=%d\t Voltage=%f"%(reading1, voltage1))
  print("2:Reading=%d\t Voltage=%f"%(reading2, voltage2))
  print("1_1:Reading=%d\t Voltage=%f"%(reading1_1, voltage1_1))
  
  time.sleep(0.5)
