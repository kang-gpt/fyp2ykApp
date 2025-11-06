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
  status?: string;
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

  const handleApproveBooking = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      await axios.put(`${API_URL}/${id}/approve`);
      fetchBookings();
    } catch (err) {
      setError('Failed to approve booking.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleRejectBooking = async (id: number) => {
    setLoading(true);
    setError(null);
    try {
      await axios.put(`${API_URL}/${id}/reject`);
      fetchBookings();
    } catch (err) {
      setError('Failed to reject booking.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h2>
        <Translate contentKey="ykApp.booking.home.title">Manage Bookings</Translate>
      </h2>

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
              <th>
                <Translate contentKey="ykApp.booking.bookingDate">Booking Date</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.booking.user">User</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.booking.court">Court</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.payment.status">Payment Status</Translate>
              </th>
              <th>
                <Translate contentKey="ykApp.booking.status">Booking Status</Translate>
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {bookings.map(booking => (
              <tr key={booking.id}>
                <td>
                  <Link to={`/booking/${booking.id}`}>{booking.id}</Link>
                </td>
                <td>{booking.bookingDate ? new Date(booking.bookingDate).toLocaleString() : 'N/A'}</td>
                <td>{booking.user ? booking.user.login : 'N/A'}</td>
                <td>{booking.court ? booking.court.name : 'N/A'}</td>
                <td>
                  {booking.payment ? (
                    <Input
                      type="select"
                      name="paymentStatus"
                      id={`paymentStatus-${booking.id}`}
                      value={booking.payment.status || 'Pending'}
                      onChange={e => handlePaymentStatusChange(booking.payment.id, e.target.value)}
                      disabled={loading}
                      className={
                        booking.payment.status === 'Completed'
                          ? 'text-success fw-bold'
                          : booking.payment.status === 'Failed'
                            ? 'text-danger fw-bold'
                            : 'text-warning fw-bold'
                      }
                    >
                      <option value="Pending">⏳ Pending</option>
                      <option value="Completed">✓ Completed</option>
                      <option value="Failed">✗ Failed</option>
                    </Input>
                  ) : (
                    <span className="badge bg-secondary">No Payment</span>
                  )}
                </td>
                <td>
                  <span
                    className={`badge ${
                      booking.status === 'APPROVED'
                        ? 'bg-success'
                        : booking.status === 'REJECTED'
                          ? 'bg-danger'
                          : booking.status === 'PENDING'
                            ? 'bg-warning'
                            : 'bg-secondary'
                    }`}
                  >
                    {booking.status}
                  </span>
                </td>
                <td>
                  <div className="btn-group flex-btn-group-container">
                    {booking.status === 'PENDING' && (
                      <>
                        <Button
                          color="success"
                          size="sm"
                          onClick={() => handleApproveBooking(booking.id)}
                          disabled={loading}
                          data-cy="entityApproveButton"
                        >
                          <Translate contentKey="entity.action.approve">Approve</Translate>
                        </Button>
                        <Button
                          color="danger"
                          size="sm"
                          onClick={() => handleRejectBooking(booking.id)}
                          disabled={loading}
                          data-cy="entityRejectButton"
                        >
                          <Translate contentKey="entity.action.reject">Reject</Translate>
                        </Button>
                      </>
                    )}
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
                      disabled={loading}
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
