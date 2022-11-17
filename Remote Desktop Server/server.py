from flask import Flask,request
import pyautogui
import socket

pyautogui.FAILSAFE = False
pyautogui.PAUSE = 0
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.connect(("8.8.8.8", 5000))
ip = s.getsockname()[0]
s.close()
app = Flask(__name__)
@app.route('/')
def main():
  
    if(request.args.get('command')=='move'):
        x,y = (float(request.args.get('x'))*4 ),(float(request.args.get('y'))*4 )
        pyautogui.moveRel(x, y,duration = 0.0,_pause=False)
        return ip

    if(request.args.get('command')=='leftClick'):
        pyautogui.click()
        return ip
    
    if(request.args.get('command')=='rightClick'):
        pyautogui.click(button='right')
        return ip

    if (request.args.get('command') == "scrollDown"):
        pyautogui.scroll(-150)
        return ip
    
    if (request.args.get('command') == "scrollUp"):
        pyautogui.scroll(150)
        return ip
    
       
    return "Hello from server"

if __name__=='__main__':
    app.run(port=5000,host=ip)

