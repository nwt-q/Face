import request from './base';

// 获取设备列表
export function getEquipment(params?: any) {
  return request({
    url: '/equipment/page',
    method: 'get',
    params
  })
}

// 根据名称搜索设备
export function searchEquipmentByName(name: string) {
  return request({
    url: `/equipment/getByName/${name}`,
    method: 'get'
  })
}

// 新增设备
export function addEquipment(data: any) {
  return request({
    url: '/equipment/insert',
    method: 'post',
    data: data
  })
}

// 更新设备
export function updateEquipment(data: any) {
  return request({
    url: '/equipment/update',
    method: 'post',
    data: data
  })
}

// 删除设备
export function deleteEquipment(id: number) {
  return request({
    url: `/equipment/deleteById/${id}`,
    method: 'delete'
  })
}

// 获取设备
export function getEquipmentt() {
  return request({
    url: `/equipment/getequipment`,
    method: 'get'
  })
}
