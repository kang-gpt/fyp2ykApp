const bookings = {}; // courtId -> { bookingId -> isBooked }

export const getBookingStatus = (courtId, dateString, timeSlot) => {
  if (!bookings[courtId]) {
    return false; // Not booked
  }
  const bookingId = `${dateString}-${timeSlot}`;
  return bookings[courtId][bookingId] || false;
};

export const toggleBooking = (courtId, dateString, timeSlot) => {
  if (!bookings[courtId]) {
    bookings[courtId] = {};
  }
  const bookingId = `${dateString}-${timeSlot}`;
  bookings[courtId][bookingId] = !bookings[courtId][bookingId];
  return bookings[courtId][bookingId]; // Return new status
};

export const getAllBookingsForCourt = courtId => {
  return bookings[courtId] || {};
};
