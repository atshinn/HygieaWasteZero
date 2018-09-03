#imports
import QApplication
import QWidget
import QPushButton
import pyqtSlot

#class for task
class OpeningView(QWidget):

    #init func
    def __init__(self):
        super().__init__()
        self.left = 5
        self.top = 5
        self.width = 100
        self.height = 100
        self.initUI()
        
    #name of app
    def on_click(self):
        print('Hygiea')
        
    #button push func
    def button(self):
        button = QPushButton('Take Picture')   
        button.move(100, 30)     
        button.clicked.connect(self.on_click)
        
