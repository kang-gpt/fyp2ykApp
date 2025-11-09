import React, { useEffect, useMemo } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, approveBooking, rejectBooking } from 'app/entities/booking/booking.reducer';

import { Table, Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import dayjs from 'dayjs';
import { IBooking } from 'app/shared/model/booking.model';

interface GroupedBooking {
  groupKey: string;
  bookings: IBooking[];
  user: string;
  userLogin: string;
  sport: string;
  courtId: string;
  courtName: string;
  bookingDate: string;
  status: string;
}

const ManageBookings = () => {
  const dispatch = useAppDispatch();
  const bookings = useAppSelector(state => state.booking.entities);
  const loading = useAppSelector(state => state.booking.loading);
  const error = useAppSelector(state => state.booking.errorMessage);
  const updateSuccess = useAppSelector(state => state.booking.updateSuccess);

  useEffect(() => {
    dispatch(getEntities({}));
  }, [dispatch, updateSuccess]);

  // Group bookings by user, court, and date
  const groupedBookings = useMemo(() => {
    const groups: Record<string, GroupedBooking> = {};

    bookings.forEach(booking => {
      if (!booking.timeSlot || !booking.user) return;

      // Create a unique key for grouping: userId-courtId-bookingDate
      const dateStr = booking.bookingDate ? dayjs(booking.bookingDate).format('YYYY-MM-DD') : 'no-date';
      const userId = booking.user?.id || 'no-user';
      const courtId = booking.timeSlot?.court?.id || 'no-court';
      const groupKey = `${userId}-${courtId}-${dateStr}`;

      if (!groups[groupKey]) {
        groups[groupKey] = {
          groupKey,
          bookings: [],
          user: booking.user?.login || 'N/A',
          userLogin: booking.user?.login || 'N/A',
          sport: booking.timeSlot?.court?.sport?.name || 'N/A',
          courtId: booking.timeSlot?.court?.id?.toString() || 'N/A',
          courtName: booking.timeSlot?.court?.name || 'N/A',
          bookingDate: dateStr,
          status: booking.status || 'UNKNOWN',
        };
      }

      groups[groupKey].bookings.push(booking);
    });

    return Object.values(groups);
  }, [bookings]);

  return (
    <div>
      <h2>
        <Translate contentKey="ykApp.booking.home.title">Manage Bookings</Translate>
      </h2>

      {loading && <p>Loading bookings...</p>}
      {error && <p className="text-danger">{error}</p>}

      {!loading && !error && groupedBookings.length === 0 && (
        <div className="alert alert-warning">
          <Translate contentKey="ykApp.booking.home.notFound">No Bookings found</Translate>
        </div>
      )}

      {!loading && !error && groupedBookings.length > 0 && (
        <Table responsive striped>
          <thead>
            <tr>
              <th>Booking IDs</th>
              <th>User</th>
              <th>Sport</th>
              <th>Court</th>
              <th>Date</th>
              <th>Booking Status</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {groupedBookings.map(group => (
              <tr key={group.groupKey}>
                <td>
                  {group.bookings.map((booking, idx) => (
                    <span key={booking.id}>
                      <Link to={`/booking/${booking.id}`}>{booking.id}</Link>
                      {idx < group.bookings.length - 1 && ', '}
                    </span>
                  ))}
                </td>
                <td>@{group.userLogin}</td>
                <td>{group.sport}</td>
                <td>{group.courtName}</td>
                <td>{dayjs(group.bookingDate).format('MMM DD, YYYY')}</td>
                <td>
                  <span
                    className={`badge ${
                      group.status === 'APPROVED'
                        ? 'bg-success'
                        : group.status === 'REJECTED'
                          ? 'bg-danger'
                          : group.status === 'PENDING'
                            ? 'bg-warning'
                            : 'bg-secondary'
                    }`}
                  >
                    {group.status}
                  </span>
                </td>
                <td>
                  <div className="d-flex flex-column gap-1">
                    {group.status === 'PENDING' &&
                      group.bookings.map(booking => (
                        <div key={booking.id} className="btn-group btn-group-sm">
                          <Button
                            color="success"
                            size="sm"
                            onClick={() => dispatch(approveBooking(booking.id))}
                            disabled={loading}
                            title={`Approve booking ${booking.id}`}
                          >
                            ✓ #{booking.id}
                          </Button>
                          <Button
                            color="danger"
                            size="sm"
                            onClick={() => dispatch(rejectBooking(booking.id))}
                            disabled={loading}
                            title={`Reject booking ${booking.id}`}
                          >
                            ✗
                          </Button>
                        </div>
                      ))}
                    {group.bookings.map(booking => (
                      <div key={`actions-${booking.id}`} className="btn-group btn-group-sm">
                        <Button tag={Link} to={`/booking/${booking.id}`} color="info" size="sm" title={`View booking ${booking.id}`}>
                          👁 #{booking.id}
                        </Button>
                        <Button
                          tag={Link}
                          to={`/booking/${booking.id}/edit`}
                          color="primary"
                          size="sm"
                          title={`Edit booking ${booking.id}`}
                        >
                          ✎
                        </Button>
                        <Button
                          onClick={() => (window.location.href = `/booking/${booking.id}/delete`)}
                          color="danger"
                          size="sm"
                          disabled={loading}
                          title={`Delete booking ${booking.id}`}
                        >
                          🗑
                        </Button>
                      </div>
                    ))}
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
