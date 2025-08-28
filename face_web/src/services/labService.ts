import {
  getLabs,
  searchLabsByName,
  addLab,
  updateLab,
  deleteLab,
  getLabsName,
  getUserLab
} from '../api/Labs';
import{
    getBookings,
    searchBookingsByName,
    addBooking,
    updateBooking,
    appointment,
} from '../api/bookings'

import{
  updateEquipment,
  deleteEquipment,
  getEquipment,
  searchEquipmentByName,
  addEquipment,
  getEquipmentt,
} from '../api/Equipment'

import{

} from '../api/Attendance'

import{
  getUsers,
  deleteUser,
  addUser,
  updateUser
} from '../api/users'


export const LabService = {
  getLabs,
  searchLabsByName,
  addLab,
  updateLab,
  deleteLab,
  getLabsName,
  getUserLab,
  
  getBookings,
  searchBookingsByName,
  addBooking,
  updateBooking,
  appointment,

  updateEquipment,
  deleteEquipment,
  getEquipment,
  searchEquipmentByName,
  addEquipment,
  getEquipmentt,

  getUsers,
  deleteUser,
  addUser,
  updateUser,
};