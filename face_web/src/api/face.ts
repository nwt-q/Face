import request from './base';


// 获取实验室列表
export async function getface(fileInput: File) {
    const formData = new FormData();
    formData.append('file', fileInput); // 字段名必须为'file'
    return fetch('http://127.0.0.1:8001/file', {
        method: 'POST',
        body: formData
        // 不要设置Content-Type头，浏览器会自动添加multipart边界
        })
   .then(response => {
        // 请求成功，返回响应数据
        return response.json();
    })
   .catch(error => {
        // 请求失败，处理错误
        console.log('获取实验室列表失败:', error);
    });
}
