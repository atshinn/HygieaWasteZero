from flask import Flask, request
from traceback import format_exc

import hywz_subroutines as subrout

application = app = Flask(__name__)


@app.route('/trash',methods=['POST','PUT','GET'])
def upload_image():
    try:
        
        if request.method == 'POST':
            return subrout.post_trash_routine(request)
        
        elif request.method == 'GET':
            return subrout.get_all_trash_routine()
    except Exception:
        return str(format_exc())


@app.route('/trash/<string:resource_id>', methods=['GET','DELETE'])
def get_or_delete_trash(resource_id):
    if request.method == 'GET':
        return subrout.get_trash_routine(request, resource_id)    
    else:
        return resource_id


@app.route('/')
def landing():
    return "landing page for hywz"
if __name__ == '__main__':
    application.run(debug=True)