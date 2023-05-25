import spidev, time

spi1 = spidev.SpiDev()
spi2 = spidev.SpiDev()

spi1.open(0,0)
spi2.open(1,0)
spi1.max_speed_hz = 19530000
spi2.max_speed_hz = 19530000

def analog_read1(Channel):
  r = spi1.xfer2([1,(8+channel)<<4,0])
  adc_out = ((r[1]&3)<<8)+r[2]
  return adc_out

def analog_read2(Channel):
  r = spi2.xfer2([1,(8+channel)<<4,0])
  adc_out = ((r[1]&3)<<8)+r[2]
  return adc_out

while True:
  reading1_1 = analog_read1(0)
  reading1_2 = analog_read1(1)
  reading1_3 = analog_read1(2)
  reading1_4 = analog_read1(3)
  reading2_1 = analog_read2(0)
  reading2_2 = analog_read2(1)
  reading2_3 = analog_read2(2)
  reading2_4 = analog_read2(3)
  
  print("1:Reading1_1=%d    Reading2_1=%d"%(reading1_1, reading2_1))
  print("2:Reading1_2=%d    Reading2_2=%d"%(reading1_2, reading2_2))
  print("3:Reading1_3=%d    Reading2_3=%d"%(reading1_3, reading2_3))
  print("4:Reading1_4=%d    Reading2_4=%d"%(reading1_4, reading2_4))
  
  time.sleep(0.5)
