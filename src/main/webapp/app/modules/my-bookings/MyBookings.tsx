import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/booking/booking.reducer';
import { Table, Button } from 'reactstrap';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { format } from 'date-fns';

const MyBookings = () => {
  const dispatch = useAppDispatch();
  const bookings = useAppSelector(state => state.booking.entities);
  const loading = useAppSelector(state => state.booking.loading);
  const error = useAppSelector(state => state.booking.errorMessage);

  useEffect(() => {
    dispatch(getEntities({}));
  }, [dispatch]);

  return (
    <div>
      <h2 id="my-bookings-heading" data-cy="MyBookingsHeading">
        <Translate contentKey="ykApp.booking.home.myBookingsTitle">My Bookings</Translate>
      </h2>

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
