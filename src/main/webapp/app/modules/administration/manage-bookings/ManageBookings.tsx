import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Translate } from 'react-jhipster';
import { Table, Button, Input } from 'reactstrap';
import { Link } from 'react-router-dom';

interface IBooking {
  id?: number;
  bookingDate?: string;
  user?: { id: number; login: string };
  court?: { id: number; name: string };
  payment?: { id: number; status: string };
}

const ManageBookings = () => {
  const [bookings, setBookings] = useState<IBooking[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const API_URL = '/api/bookings';
  const PAYMENT_API_URL = '/api/payments';

  const fetchBookings = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.get<IBooking[]>(API_URL);
      setBookings(response.data);
    } catch (err) {
      setError('Failed to fetch bookings.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchBookings();
  }, []);

  const handlePaymentStatusChange = async (paymentId: number, newStatus: string) => {
    setLoading(true);
    setError(null);
    try {
      await axios.put(`${PAYMENT_API_URL}/${paymentId}/status?status=${newStatus}`);
      fetchBookings(); // Refresh the list after update
    } catch (err) {
      setError('Failed to update payment status.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2><Translate contentKey="ykApp.booking.home.title">Manage Bookings</Translate></h2>

      <div className="d-flex justify-content-end mb-3">
        <Button className="ms-2" color="info" onClick={fetchBookings} disabled={loading}>
          <Translate contentKey="ykApp.booking.home.refreshListLabel">Refresh List</Translate>
        </Button>
      </div>

      {loading && <p>Loading bookings...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && bookings.length === 0 && (
        <div className="alert alert-warning">
          <Translate contentKey="ykApp.booking.home.notFound">No Bookings found</Translate>
        </div>
      )}

      {!loading && !error && bookings.length > 0 && (
        <Table responsive striped>
          <thead>
            <tr>
              <th>ID</th>
              <th><Translate contentKey="ykApp.booking.bookingDate">Booking Date</Translate></th>
              <th><Translate contentKey="ykApp.booking.user">User</Translate></th>
              <th><Translate contentKey="ykApp.booking.court">Court</Translate></th>
              <th><Translate contentKey="ykApp.payment.status">Payment Status</Translate></th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map(booking => (
              <tr key={booking.id}>
                <td>
                  <Link to={`/booking/${booking.id}`}>{booking.id}</Link>
                </td>
                <td>{booking.bookingDate}</td>
                <td>{booking.user ? booking.user.login : ''}</td>
                <td>{booking.court ? booking.court.name : ''}</td>
                <td>
                  {booking.payment ? (
                    <Input
                      type="select"
                      name="paymentStatus"
                      id="paymentStatus"
                      value={booking.payment.status}
                      onChange={e => handlePaymentStatusChange(booking.payment.id, e.target.value)}
                    >
                      <option value="Pending">Pending</option>
                      <option value="Completed">Completed</option>
                      <option value="Failed">Failed</option>
                    </Input>
                  ) : (
                    'N/A'
                  )}
                </td>
                <td>
                  <div className="btn-group flex-btn-group-container">
                    <Button tag={Link} to={`/booking/${booking.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                      <Translate contentKey="entity.action.view">View</Translate>
                    </Button>
                    <Button tag={Link} to={`/booking/${booking.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </Button>
                    <Button
                      onClick={() => (window.location.href = `/booking/${booking.id}/delete`)}
                      color="danger"
                      size="sm"
                      data-cy="entityDeleteButton"
                    >
                      <Translate contentKey="entity.action.delete">Delete</Translate>
                    </Button>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </div>
  );
};

export default ManageBookings;
