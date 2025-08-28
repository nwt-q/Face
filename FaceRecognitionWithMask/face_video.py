"""
视频测试
"""
import argparse

import numpy as np
import cv2
from face_recognizer import FaceRecognizer

def parse_args(video_width=640, video_hight = 720):
    """
    解析参数
    """
    parser = argparse.ArgumentParser(description='face_recognization')
    parser.add_argument('--face_db_root', type=str, default='data/mask', help='the root path of target database')

    args = parser.parse_args()
    recognizer = FaceRecognizer()
    recognizer.create_known_faces(args.face_db_root)


    # 测试视频路径
    input_movie = cv2.VideoCapture(0)
    
    # 需要动态调整
    for i in range(10):  # 尝试 0 到 9 的编号
        cap = cv2.VideoCapture(i)
        if cap.isOpened():
            print(f"摄像头编号 {i} 可用")
            input_movie = cap
            break
        else:
            print(f"摄像头编号 {i} 不可用")


    # Vide size
    video_size = (video_width, video_hight)

    
    while True:
        # Grab a single frame of video
        ret, frame = input_movie.read()

        # 输入视频文件结束时退出
        if not ret: pass

        # 退出条件（按ESC退出）
        if 0xFF == 27:
            break

        frame = cv2.resize(frame, dsize=video_size)
        # 人脸识别处理
        item = recognizer.recognize(frame, 0.5)
        if item:
            name, (left, top, right, bottom), _, score = item
            cv2.rectangle(frame, (left, top), (right, bottom), (0, 0, 255), 2)
            # Draw a label with a name below the face
            cv2.rectangle(frame, (left, bottom - 25), (right, bottom), (0, 0, 255), cv2.FILLED)
            font = cv2.FONT_HERSHEY_DUPLEX
            cv2.putText(frame, "%s %.3f" % (name, score), (left + 6, bottom - 6), font, 0.5, (255, 255, 255), 1)
        
        # 显示帧数
        cv2.imshow('Face Recognition', frame)
        cv2.waitKey(1)

    
    # All done!
    input_movie.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    parse_args()