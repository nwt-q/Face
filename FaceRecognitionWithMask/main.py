import cv2
import os
import numpy as np
from fastapi import FastAPI, File
from fastapi.middleware.cors import CORSMiddleware 
from face_recognizer import FaceRecognizer
import uvicorn
import threading
import time


face_rec = FaceRecognizer()

face_rec.create_known_faces('data/mask')

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # 允许所有来源，生产环境应指定具体域名
    allow_credentials=True,
    allow_methods=["*"],  # 允许所有方法
    allow_headers=["*"],  # 允许所有头
)

# CORS(app)


@app.get("/")
def root():
    return {"message": "Hello World"}


@app.post("/update")
def update():
    face_rec.create_known_faces('data/mask')
    return {"result": 'ok'}

@app.post("/file")
def file(file: bytes = File(...)):
    print("EvLast正在进行人脸识别🥰..........")
    try:
        image_array = np.frombuffer(file, dtype=np.uint8)  # numpy array
        if len(image_array) == 0:
            print("EvLast识别出空文件数据👾")
            return {"error": "空文件数据"}

        img = cv2.imdecode(image_array, cv2.COLOR_RGBA2BGR)
        if img is None:
            print("EvLast识别出无效的图片格式👾")
            return {"error": "无效的图片格式"}
        # print(img)
        result = face_rec.recognize(img,  0.5)
        
        # return {"result": result}
    
        # if image is None:
        #     return {"error": "图片解码失败，请检查文件格式"}
        return {"result": result}
    except Exception as e:
        # 记录详细错误日志
        import logging
        logging.error(f"图片处理失败: {str(e)}")
        return {"error": "服务器内部错误", "detail": str(e)}, 500
    # image_array = np.frombuffer(file, dtype=np.uint8)  # numpy array
    # img = cv2.imdecode(image_array, cv2.COLOR_RGBA2BGR)
    # result = face_rec.recognize(img)
    # return {"result": result}

# opencv 读取图片


print("EvLast全网知名CSDN博主🥰")
print("欢迎访问CSDN个人博客获取更多资料" + "https://blog.csdn.net/2303_79299383?type=blog")
print("EvLast全网知名GitHub开源作者")
print("https://github.com/nwt-q")
print("HuggingFace")
print("🥰Wecome TO https://huggingface.co/")


if __name__ == "__main__":
    
    # 检查Java服务依赖
    jar_path = os.path.abspath('./data/face_back.jar')
    if not os.path.exists(jar_path):
        print(f"错误: 未找到服务文件 {jar_path}")
        exit(1)
    
    # 启动服务
    def run_java():
        os.system(f'java -jar {jar_path}')
    
    # 启动FastAPI服务
    def run_fastapi():
        uvicorn.run(app, host="127.0.0.1", port=8001)
    
    # 创建并启动线程
    java_thread = threading.Thread(target=run_java, daemon=False)
    fastapi_thread = threading.Thread(target=run_fastapi, daemon=False)
    
    java_thread.start()
    time.sleep(5)  # 等待Java服务初始化
    fastapi_thread.start()
    
    # 等待线程结束
    java_thread.join()
    fastapi_thread.join()