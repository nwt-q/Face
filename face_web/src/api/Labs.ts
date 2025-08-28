import request from './base';

// 获取实验室列表
export function getLabs(params?: any) {
  return request({
    url: '/labs/page',  // 根据实际后端接口修改路径
    method: 'get',
    params
  })
}

export function getLabsName() {
    return request({
      url: `/labs/getlabs`,
      method: 'get'
    })
  }

// 根据姓名搜索
export function searchLabsByName(name: any) {
  return request({
    url: `/labs/getByName/${name}`,
    method: 'get'
  })
}

// 新增实验室
export function addLab(data: any) {
  return request({
    url: '/labs/insert',
    method: 'post',
    data: data
  })
}

// 更新实验室
export function updateLab(id: number, data: any) {
  return request({
    url: `/labs/update`,
    method: 'post',
    data: data
  })
}

// 删除实验室
export function deleteLab(id: number) {
  return request({
    url: `/labs/deleteById/${id}`,
    method: 'delete'
  })
}


export function getUserLab() {
    return request({
      url: `/labs/userget`,
      method: 'get'
    })
  }