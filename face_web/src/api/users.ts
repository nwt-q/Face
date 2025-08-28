import request from './base';

// 获取用户列表
export function getUsers() {
  return request({
    url: '/users/page',
    method: 'get'
  })
}

// 删除用户
export function deleteUser(id: number) {
  return request({
    url: `/users/deleteById/${id}`,
    method: 'delete'
  })
}

// 新增用户
export function addUser(data: any) {
  return request({
    url: '/users/insert',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data: any) {
  return request({
    url: '/users/update',
    method: 'post',
    data
  })
}