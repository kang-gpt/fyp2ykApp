import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Translate } from 'react-jhipster';
import { Table, Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import { format } from 'date-fns';

interface IBooking {
  id?: number;
  bookingDate?: string;
  startTime?: string;
  endTime?: string;
  user?: { id: number; login: string };
  court?: { id: number; name: string; sport: { name: string } };
  payment?: { id: number; status: string };
}

const MyBookings = () => {
  const [bookings, setBookings] = useState<IBooking[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const API_URL = '/api/my-bookings';

  const fetchMyBookings = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<IBooking[]>(API_URL);
      setBookings(response.data);
    } catch (err) {
      setError('Failed to fetch your bookings.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMyBookings();
  }, []);

  return (
    <div>
      <h2 id="my-bookings-heading" data-cy="MyBookingsHeading">
        <Translate contentKey="ykApp.booking.home.myBookingsTitle">My Bookings</Translate>
      </h2>

      <div className="d-flex justify-content-end mb-3">
        <Button className="ms-2" color="info" onClick={fetchMyBookings} disabled={loading}>
          <Translate contentKey="ykApp.booking.home.refreshListLabel">Refresh List</Translate>
        </Button>
      </div>

      {loading && <p>Loading your bookings...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && bookings.length === 0 && (
        <div className="alert alert-warning">
          <Translate contentKey="ykApp.booking.home.noBookingsFound">No bookings found.</Translate>
        </div>
      )}

      {!loading && !error && bookings.length > 0 && (
        <Table responsive striped>
          <thead>
            <tr>
              <th>ID</th>
              <th>
                <Translate contentKey="ykApp.booking.sport">Sport</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.booking.court">Court</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.booking.date">Date</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.booking.time">Time</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.payment.status">Payment Status</Translate>
              </th>
            </tr>
          </thead>
          <tbody>
            {bookings.map(booking => (
              <tr key={booking.id}>
                <td>
                  <Link to={`/booking/${booking.id}`}>{booking.id}</Link>
                </td>
                <td>{booking.court?.sport?.name || 'N/A'}</td>
                <td>{booking.court?.name || 'N/A'}</td>
                <td>{booking.startTime ? format(new Date(booking.startTime), 'yyyy-MM-dd') : 'N/A'}</td>
                <td>
                  {booking.startTime && booking.endTime
                    ? `${format(new Date(booking.startTime), 'HH:mm')} - ${format(new Date(booking.endTime), 'HH:mm')}`
                    : 'N/A'}
                </td>
                <td>{booking.payment?.status || 'N/A'}</td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
};

export default MyBookings;
