import request from './base';

// 获取考勤列表
export function getAttendances(params?: any) {
  return request({
    url: '/attendance/page',
    method: 'get',
    params
  })
}

// 根据姓名搜索考勤记录
export function searchAttendancesByName(name: string) {
  return request({
    url: `/attendance/getByName/${name}`,
    method: 'get'
  })
}

// 新增考勤记录
export function addAttendance(data: any) {
  return request({
    url: '/attendance/insert',
    method: 'post',
    data: data
  })
}

// 更新考勤记录
export function updateAttendance(data: any) {
  return request({
    url: '/attendance/update',
    method: 'post',
    data: data
  })
}

// 删除考勤记录
export function deleteAttendance(id: number) {
  return request({
    url: `/attendance/deleteById/${id}`,
    method: 'delete'
  })
}