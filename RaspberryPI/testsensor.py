import spidev
import schedule, time
from datetime import datetime
import sqlite3

spi0 = spidev.SpiDev()
spi1 = spidev.SpiDev()

spi0.open(0,0)
spi1.open(1,0)
spi0.max_speed_hz=1953000
spi1.max_speed_hz=1953000

#0-3 센서값 저장
CheckV = [0,0,0,0,0]


#conMon = sqlite3.connect('/home/geng/MonDB', isolation_level=None)
#curMon = conMon.cursor()


#2. 자세탐지(함수)
def getSensorVal(i): #2-1 센서 값 받아오기
    def analog_read0(channel): 
        r = spi0.xfer2([1, (8 + channel) << 4, 0])
        adc_out0 = ((r[1]&3) << 8) + r[2]
        return adc_out0

    def analog_read1(channel):
        r = spi1.xfer2([1, (8 + channel) << 4, 0])
        adc_out1 = ((r[1]&3) << 8) + r[2]
        return adc_out1
       
    R1 = analog_read0(0)
    R2 = analog_read0(1)
    R3 = analog_read0(2)
    #BR = analog_read0(3)
    
    L1 = analog_read1(0)
    L2 = analog_read1(1)
    L3 = analog_read1(2)
    #BL = analog_read1(3)

    if R3 > 700 and R2 > 700 and R1 > 700 and L1 > 700 and L2 > 700 and L3 > 700: #and BR > 700 and BL > 700:
        CheckV[i]=1
    elif R3 < 150 and R2 < 150 and R1 < 150 and L1 < 150 and L2 < 150 and L3 < 150: #and BR < 150 and BL < 150:
        CheckV[i]=0
    else:
        CheckV[i]=2

def judge(check): #2-2 판단
    a,b,c,=0,0,0
    for i in range(check):
        if check == 0: 
            a=a+1
        elif check == 1:
            b=b+1
        elif check == 2:
            c=c+1
    if a>b and a>c :
        return "0"
    elif b>a and b>c :
        return "1"
    elif c>a and c>b :
        return "2"
    elif a==b:
        return "1"
    elif a==c:
        return "2"
    elif b==c:
        return "1"
        
                    
def dayDBUpdate(hour ,min, data): # 2-3 판단 결과 DB 저장
    conDay = sqlite3.connect('/home/geng/DayDB', isolation_level=None)
    curDay = conDay.cursor()

    curDay.execute("UPDATE day SET '%s'=%s WHERE hour='%s'", str(min)+'m', data, str(hour))

    curDay.close()


now_min =-1
while(True):
    now = datetime.now()
    today = now.strftime('%Y %m %d')
    hour = now.hour
    min = now.minute

    if min != now_min:
        if min%5 != 0:
            i = min%5 
            getSensorVal(i)
        else:
            i = min%5 
            getSensorVal(i)
            j = judge(CheckV)
            dayDBUpdate(hour, min ,j)
            CheckV = [0,0,0,0,0]
    now_min=min

    time.sleep(1)

