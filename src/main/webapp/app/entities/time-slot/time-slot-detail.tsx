import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './time-slot.reducer';

export const TimeSlotDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const timeSlotEntity = useAppSelector(state => state.timeSlot.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="timeSlotDetailsHeading">
          <Translate contentKey="ykApp.timeSlot.detail.title">TimeSlot</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.id}</dd>
          <dt>
            <span id="startTime">
              <Translate contentKey="ykApp.timeSlot.startTime">Start Time</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.startTime ? <TextFormat value={timeSlotEntity.startTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="endTime">
              <Translate contentKey="ykApp.timeSlot.endTime">End Time</Translate>
            </span>
          </dt>
          <dd>{timeSlotEntity.endTime ? <TextFormat value={timeSlotEntity.endTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="ykApp.timeSlot.court">Court</Translate>
          </dt>
          <dd>{timeSlotEntity.court ? timeSlotEntity.court.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/time-slot" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/time-slot/${timeSlotEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TimeSlotDetail;
