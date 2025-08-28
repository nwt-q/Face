import tkinter as tk
from tkinter import ttk
from PIL import Image, ImageTk
import cv2  
import argparse
import numpy as np
from face_recognizer import FaceRecognizer
import os
from datetime import datetime
import tkinter.messagebox


class FaceUI:
    def __init__(self):
        self.root = tk.Tk()
        self.root.title("人脸识别系统")
        self.root.geometry("800x600")
        
        # 创建主框架
        self.main_frame = ttk.Frame(self.root)
        self.main_frame.pack(fill=tk.BOTH, expand=True, padx=10, pady=10)
        
        # 创建视频显示区域
        self.video_label = ttk.Label(self.main_frame)
        self.video_label.pack(side=tk.TOP, pady=10)
        
        # 创建按钮框架
        self.button_frame = ttk.Frame(self.main_frame)
        self.button_frame.pack(side=tk.BOTTOM, pady=10)
        
        # 添加按钮
        self.start_button = ttk.Button(self.button_frame, text="开始识别", command=self.start_recognition)
        self.start_button.pack(side=tk.LEFT, padx=5)
        
        self.stop_button = ttk.Button(self.button_frame, text="停止识别", command=self.stop_recognition)
        self.stop_button.pack(side=tk.LEFT, padx=5)
        
        self.register_button = ttk.Button(self.button_frame, text="人脸注册", command=self.register_face)
        self.register_button.pack(side=tk.LEFT, padx=5)
        
        # 初始化变量
        self.is_running = False
        self.cap = None
        
        self.parser = argparse.ArgumentParser(description='face_recognization')
        self.parser.add_argument('--face_db_root', type=str, default='data/mask', help='the root path of target database')
        self.args = self.parser.parse_args()
        self.recognizer = FaceRecognizer()
        self.recognizer.create_known_faces(self.args.face_db_root)

    def start_recognition(self):
        # 开始识别逻辑
        if not self.is_running:
            self.is_running = True
            self.cap = cv2.VideoCapture(0)
            self.update_frame()
        pass

    def stop_recognition(self):
        # 停止识别
        self.is_running = False
        if self.cap is not None:
            self.cap.release()
        # 清空视频显示区域
        self.video_label.configure(image=None)
        self.video_label.image = None

    def register_face(self):
        # 创建注册窗口
        reg_window = tk.Toplevel(self.root)
        reg_window.title("人脸注册")
        reg_window.geometry("400x300")

        # 视频预览区域
        preview_label = ttk.Label(reg_window)
        preview_label.pack(pady=5)

        # 姓名输入框
        name_var = tk.StringVar()
        ttk.Label(reg_window, text="请输入姓名:").pack()
        name_entry = ttk.Entry(reg_window, textvariable=name_var)
        name_entry.pack(pady=5)

        # 功能按钮框架
        btn_frame = ttk.Frame(reg_window)
        btn_frame.pack(pady=10)
        

        # 拍照按钮
        def capture_face():
            nonlocal cap  # 确保使用外部的cap变量
            if cap and cap.isOpened():
                ret, frame = cap.read()
                print("进入拍照......")
                if ret:
                    # 转换颜色空间并保存到临时变量
                    self.captured_frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                    # 显示预览
                    img = Image.fromarray(self.captured_frame)
                    img = img.resize((320, 240))
                    
                    photo = ImageTk.PhotoImage(image=img)
                    preview_label.configure(image=photo)
                    preview_label.image = photo
                    
        ttk.Button(btn_frame, text="拍照", command=capture_face).pack(side=tk.LEFT, padx=5)

        # 确认保存按钮
        def save_face():
            name = name_var.get().strip()
            if not name:
                tk.messagebox.showerror("错误", "请输入姓名")
                return
            
            if not hasattr(self, 'captured_frame'):
                tk.messagebox.showerror("错误", "请先拍照")
                return
            
            # 创建保存路径
            # save_dir = f"{self.args.face_db_root}/{name}"
            save_dir = os.path.join(self.args.face_db_root)
            print("保存路径：", save_dir)
            # 添加路径存在性检查
            os.makedirs(save_dir, exist_ok=True)
            
              # 添加图像有效性验证
            if not isinstance(self.captured_frame, np.ndarray):
                tk.messagebox.showerror("错误", "无效的图像数据")
                return

            # 生成唯一文件名
            timestamp = datetime.now().strftime(f"{name}-%Y%m%d_%H%M%S")
            save_path = f"{save_dir}/{timestamp}.jpg"
            
            # 保存图片（转换回BGR格式保存）
            # cv2.imwrite(save_path, cv2.cvtColor(self.captured_frame, cv2.COLOR_RGB2BGR))
            cv2.imwrite(save_path, self.captured_frame)
            # 更新人脸库
            self.recognizer.create_known_faces(self.args.face_db_root)
            
            tk.messagebox.showinfo("成功", f"{name} 注册成功！")
            reg_window.destroy()

        ttk.Button(btn_frame, text="保存", command=save_face).pack(side=tk.LEFT, padx=5)

        # 初始化摄像头
        cap = cv2.VideoCapture(0)
        def update_preview():
            if cap.isOpened():
                ret, frame = cap.read()
                if ret:
                    frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
                    img = Image.fromarray(frame)
                    img = img.resize((320, 240))
                    photo = ImageTk.PhotoImage(image=img)
                    preview_label.configure(image=photo)
                    preview_label.image = photo
                    reg_window.after(50, update_preview)
        
        update_preview()

        # 窗口关闭时释放摄像头
        def on_close():
            if cap.isOpened():
                cap.release()
            reg_window.destroy()
        
        reg_window.protocol("WM_DELETE_WINDOW", on_close)

    # 人脸识别模块
    def update_frame(self):
        if self.is_running:
            ret, frame = self.cap.read()
            if not ret: pass
            # 转换颜色空间从BGR到RGB
            frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
            # 调整图像大小
            frame = cv2.resize(frame, (640, 480))

            # 人脸识别处理 远程调用写好的face_Identification 模块
            '''
            参数解释 frame是  0.5是识别准确度
            '''
            # 相似度
            item = self.recognizer.recognize(frame, 0.5)
            if item:
                name, (left, top, right, bottom), _, score = item
                cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
                # Draw a label with a name below the face
                cv2.rectangle(frame, (left, bottom - 25), (right, bottom), (0, 0, 255), cv2.FILLED)
                font = cv2.FONT_HERSHEY_DUPLEX
                cv2.putText(frame, "%s %.3f" % (name, score), (left + 6, bottom - 6), font, 0.5, (255, 255, 255), 1)
            
            # 转换为PhotoImage
            image = Image.fromarray(frame)
            photo = ImageTk.PhotoImage(image=image)
            
            # cv2.imshow('Face Recognition', frame)
            # cv2.waitKey(1)

            # 更新视频显示区域
            self.video_label.configure(image=photo)
            self.video_label.image = photo
            
            self.root.after(10, self.update_frame)

    def run(self):
        self.root.mainloop()

if __name__ == "__main__":
    app = FaceUI()
    app.run()