import React, { useEffect } from 'react';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities, approveBooking, rejectBooking } from 'app/entities/booking/booking.reducer';
import { Row, Col, Table, Button } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faTimes } from '@fortawesome/free-solid-svg-icons';
import { Translate, TextFormat } from 'react-jhipster';
import { APP_DATE_FORMAT } from 'app/config/constants';

const ManageBookings = () => {
  const dispatch = useAppDispatch();
  const bookingList = useAppSelector(state => state.booking.entities);
  const loading = useAppSelector(state => state.booking.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleApprove = id => () => {
    dispatch(approveBooking(id));
  };

  const handleReject = id => () => {
    dispatch(rejectBooking(id));
  };

  return (
    <div>
      <h2 id="booking-heading" data-cy="BookingHeading">
        <Translate contentKey="bookingApp.booking.home.title">Manage Bookings</Translate>
      </h2>
      <div className="table-responsive">
        {bookingList && bookingList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="bookingApp.booking.bookingDate">Booking Date</Translate>
                </th>
                <th>
                  <Translate contentKey="bookingApp.booking.status">Status</Translate>
                </th>
                <th>
                  <Translate contentKey="bookingApp.booking.timeSlot">Time Slot</Translate>
                </th>
                <th>
                  <Translate contentKey="bookingApp.booking.payment">Payment</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {bookingList.map((booking, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>{booking.id}</td>
                  <td>{booking.bookingDate ? <TextFormat type="date" value={booking.bookingDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{booking.status}</td>
                  <td>{booking.timeSlot ? <a href={`/time-slot/${booking.timeSlot.id}`}>{booking.timeSlot.id}</a> : ''}</td>
                  <td>{booking.payment ? <a href={`/payment/${booking.payment.id}`}>{booking.payment.id}</a> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      {booking.status === 'PENDING' && (
                        <>
                          <Button onClick={handleApprove(booking.id)} color="success" size="sm" data-cy="approveButton">
                            <FontAwesomeIcon icon={faCheck} />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.approve">Approve</Translate>
                            </span>
                          </Button>
                          <Button onClick={handleReject(booking.id)} color="danger" size="sm" data-cy="rejectButton">
                            <FontAwesomeIcon icon={faTimes} />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.reject">Reject</Translate>
                            </span>
                          </Button>
                        </>
                      )}
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="bookingApp.booking.home.notFound">No Bookings found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ManageBookings;
