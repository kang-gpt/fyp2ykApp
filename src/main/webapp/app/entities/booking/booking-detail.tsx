import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './booking.reducer';

export const BookingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bookingEntity = useAppSelector(state => state.booking.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bookingDetailsHeading">
          <Translate contentKey="ykApp.booking.detail.title">Booking</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="ykApp.booking.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.startTime ? <TextFormat value={bookingEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="ykApp.booking.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{bookingEntity.endTime ? <TextFormat value={bookingEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ykApp.booking.user">User</Translate>
          </dt>
          <dd>{bookingEntity.user ? bookingEntity.user.login : ''}</dd>
          <dt>
            <Translate contentKey="ykApp.booking.court">Court</Translate>
          </dt>
          <dd>{bookingEntity.court ? bookingEntity.court.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/bookings" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bookings/${bookingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BookingDetail;
