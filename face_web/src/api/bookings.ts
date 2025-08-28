import request from './base';

// 获取预约列表
export function getBookings(params?: any) {
  return request({
    url: '/bookings/page',
    method: 'get',
    params
  })
}

// 根据名称搜索预约
export function searchBookingsByName(name: string) {
  return request({
    url: `/bookings/getByName/${name}`,
    method: 'get'
  })
}

// 新增预约
export function addBooking(data: any) {
  return request({
    url: '/bookings/insert',
    method: 'post',
    data: data
  })
}

// 更新预约
export function updateBooking(data: any) {
  return request({
    url: '/bookings/update',
    method: 'post',
    data: data
  })
}

// 删除预约
export function deleteBooking(id: number) {
  return request({
    url: `/bookings/deleteById/${id}`,
    method: 'delete'
  })
}

// 预约
export function appointment(data: any) {
  return request({
    url: `/bookings/inserts`,
    method: 'post',
    data: data
  }) 
}