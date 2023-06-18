import socket
import time
import sqlite3
import re #숫자만 가져오기


conDay = sqlite3.connect('/home/geng/DayDB', isolation_level=None)
curDay = conDay.cursor()

SERVER_IP = "192.168.1.11"
SERVER_PORT = 8080

def main():
    # 서버 소켓 생성
    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server_socket.bind((SERVER_IP, SERVER_PORT))
    server_socket.listen(1)

    print("서버가 시작되었습니다.")

    while True:
        # 클라이언트로부터 연결 요청 대기
        client_socket, client_address = server_socket.accept()
        print("클라이언트가 연결되었습니다.")

        try:
            # 클라이언트로부터 데이터 수신
            message = client_socket.recv(1024).decode()
            print("클라이언트로부터 받은 메시지:", message)

            # 데이터 처리 로직 수행
            if "main_page" in str(message):
                curDay.execute('SELECT "5m","10m","15m","20m","25m","30m","35m","40m","45m","50m","55m","60m" FROM day') #DB가져오기
                day_data = str(curDay.fetchall())
                out_data = re.sub(r'[^0-9]', '',day_data) #숫자만 가져오기
                #    curMon.execute()
                #    mon_data = str(curMon.fetchall())
                #    out_data = out_data + re.sub(r'[^0-9]', '',mon_data)
                client_socket.send(str(out_data).encode("utf-8")) # int 값을 string 으로 인코딩해서 전송, byte 로 전송하면 복잡함 
  
                #client_socket.send(str(out_data).encode("utf-8")) # int 값을 string 으로 인코딩해서 전송, byte 로 전송하면 복잡함 
  
                print('send :', out_data)            
                in_data = ""
    
            elif "statistics_page" in str(message):
                out_data = "test"
                client_socket.send(out_data.encode("utf-8"));
                print('send :', out_data)            
                in_data = ""


        except Exception as e:
            print("에러 발생:", str(e))

        finally:
            # 클라이언트 소켓 종료
            client_socket.close()

if __name__ == "__main__":
    main()
