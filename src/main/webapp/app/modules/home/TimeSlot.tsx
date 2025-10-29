import React, { useState, useEffect } from 'react';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity as getSport } from 'app/entities/sport/sport.reducer';
import { ISport } from 'app/shared/model/sport.model';
import { IUser } from 'app/shared/model/user.model';

import { PICKLEBALL_PRICE } from './pickleball';
import { BADMINTON_PRICE } from './badminton';
import { BASKETBALL_PRICE } from './basketball';
import { FUTSAL_PRICE } from './futsal';

interface TimeSlotProps {
  courtId: string;
  sportId: string;
}

const TimeSlot: React.FC<TimeSlotProps> = ({ courtId, sportId }) => {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const sportEntity = useAppSelector(state => state.sport.entity as ISport);
  const user = useAppSelector(state => state.authentication.account as IUser);

  const [dates, setDates] = useState<{ label: string; date: Date }[]>([]);
  const [timeSlots, setTimeSlots] = useState<{ slot: string; display: string }[]>([]);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [bookings, setBookings] = useState<any[]>([]);
  const [slotsToBook, setSlotsToBook] = useState<string[]>([]);

  // ✅ Fetch sport info
  useEffect(() => {
    if (sportId) dispatch(getSport(sportId));
  }, [sportId]);

  // ✅ Generate 7 days & time slots
  useEffect(() => {
    const newDates = Array.from({ length: 7 }, (_, i) => {
      const date = new Date();
      date.setDate(date.getDate() + i);
      return { label: date.toLocaleDateString(), date };
    });
    setDates(newDates);

    const newTimeSlots = [];
    for (let i = 8; i < 24; i++) {
      const start = `${i.toString().padStart(2, '0')}:00`;
      const end = `${(i + 1).toString().padStart(2, '0')}:00`;
      newTimeSlots.push({ slot: start, display: `${start}-${end}` });
    }
    newTimeSlots.push({ slot: '00:00', display: '00:00-01:00' });
    setTimeSlots(newTimeSlots);
  }, []);

  // ✅ Handle selecting time slots
  const handleSlotClick = (slot: string) => {
    const isBooked = bookings.some(booking => {
      const bookingStartTime = new Date(booking.startTime).toTimeString().substring(0, 5);
      return bookingStartTime === slot;
    });

    if (isBooked) return;

    const dateString = selectedDate.toISOString().split('T')[0];
    const bookingId = `${dateString} ${slot}`;

    setSlotsToBook(prev => (prev.includes(bookingId) ? prev.filter(s => s !== bookingId) : [...prev, bookingId]));
  };

  // ✅ Confirm booking or redirect to login
  const handleSelect = async () => {
    if (!user || !user.id) {
      navigate('/login', { state: { from: `/timeslot/${sportId}/${courtId}` } });
      return;
    }

    if (slotsToBook.length === 0) {
      alert('Please select at least one slot to book.');
      return;
    }

    try {
      for (const bookingId of slotsToBook) {
        const [dateString, slot] = bookingId.split(' ');
        const startTime = new Date(`${dateString}T${slot}`);
        const endTime = new Date(startTime.getTime() + 60 * 60 * 1000);

        const booking = {
          startTime: startTime.toISOString(),
          endTime: endTime.toISOString(),
          court: { id: courtId },
          user,
        };

        const response = await fetch('/api/bookings', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(booking),
        });

        if (!response.ok) throw new Error('Failed to create booking');
      }

      const hours = slotsToBook.length;
      const sportName = sportEntity?.name?.toLowerCase();
      const priceMap: Record<string, number> = {
        pickleball: PICKLEBALL_PRICE,
        badminton: BADMINTON_PRICE,
        basketball: BASKETBALL_PRICE,
        futsal: FUTSAL_PRICE,
      };
      const totalPrice = hours * (priceMap[sportName] || 0);

      navigate('/payment', { state: { sport: sportEntity.name, hours, totalPrice, courtId, slotsToBook } });
    } catch (error) {
      console.error(error);
      alert('An error occurred while booking. Please try again.');
    }
  };

  const handleCancel = () => {
    setSlotsToBook([]);
    navigate('/');
  };

  return (
    <div className="timeslot-single-court-container">
      <h3 className="text-center mb-4">
        Time Slots for Court {courtId} ({sportEntity?.name || sportId})
      </h3>

      {/* Date Buttons */}
      <div className="date-selector text-center mb-4">
        {dates.map(({ label, date }) => (
          <button
            key={date.toISOString()}
            className={`btn ${selectedDate.toDateString() === date.toDateString() ? 'btn-primary' : 'btn-secondary'} me-2`}
            onClick={() => setSelectedDate(date)}
          >
            {label}
          </button>
        ))}
      </div>

      {/* Table */}
      <table className="table table-bordered text-center">
        <thead className="table-dark">
          <tr>
            <th>Time</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          {timeSlots.map(({ slot, display }) => {
            const isBooked = bookings.some(b => {
              const start = new Date(b.startTime).toTimeString().substring(0, 5);
              return start === slot;
            });
            const dateString = selectedDate.toISOString().split('T')[0];
            const bookingId = `${dateString} ${slot}`;
            const isSelected = slotsToBook.includes(bookingId);

            const rowClass = isBooked ? 'table-danger' : isSelected ? 'table-warning' : 'table-success';

            return (
              <tr key={slot} className={rowClass} onClick={() => handleSlotClick(slot)}>
                <td>{display}</td>
                <td>{isBooked ? 'Booked' : isSelected ? 'Selected' : 'Available'}</td>
              </tr>
            );
          })}
        </tbody>
      </table>

      {/* Action Buttons */}
      {slotsToBook.length > 0 && (
        <div className="text-center mt-4">
          <button className="btn btn-primary me-2" onClick={handleSelect}>
            Confirm Booking
          </button>
          <button className="btn btn-secondary" onClick={handleCancel}>
            Cancel
          </button>
        </div>
      )}
    </div>
  );
};

export default TimeSlot;
